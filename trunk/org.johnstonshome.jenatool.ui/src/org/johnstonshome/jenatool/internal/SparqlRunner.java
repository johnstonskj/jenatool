/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.internal;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.johnstonshome.jenatool.ui.preferences.PluginPreferences;
import org.johnstonshome.jenatool.ui.views.SparqlResultsView;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.base.file.Location;

public class SparqlRunner {
		
	private static SparqlResultsView view = null;
	
	public static SparqlResultsView getResultsView() {
		return view;
	}
	
	public static void setResultsView(SparqlResultsView newView) {
		view = newView;
	}
	
	private String results = "";

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public void runSparqlQuery(String query) {
		IDocument current = view.getDocument();
		StringBuilder builder = new StringBuilder(current.get());
		builder.append("\n");
//		builder.append(query);
		
		PluginPreferences prefs = new PluginPreferences();
		Connection conn = Connections.getDefaultConnection();
		Dataset dataset = null;
					
		try {
			switch (conn.getType()) {
			case TDB:
				Location loc = new Location(conn.getUrl());
				dataset = TDBFactory.createDataset(loc);
				TDB.getContext().set(TDB.symUnionDefaultGraph, conn.isUnion());
				break;
			}
			
			long start = System.currentTimeMillis();
			Query parsed = QueryFactory.create(query);

			Dataset queryDataset = dataset; 
			if (parsed.getNamedGraphURIs().size() > 0) {
				queryDataset = TDBFactory.createDataset(); 
				for (Iterator<String> iterator = parsed.getNamedGraphURIs().iterator(); iterator.hasNext();) { 
					String name = iterator.next(); 
					Model model = queryDataset.getNamedModel(name); 
					model.add(dataset.getNamedModel(name).listStatements()); 
					TDB.sync(queryDataset); 
				}
			} 

			QueryExecution qe = QueryExecutionFactory.create(parsed, queryDataset);
			qe.getContext().set(TDB.symUnionDefaultGraph, conn.isUnion()) ; 

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (parsed.isSelectType()) {
				ResultSet rs = qe.execSelect();
				ResultSetFormatter.out(baos, rs, parsed);
			} else if (parsed.isDescribeType()) {
				Model model = qe.execDescribe();
				if (model.isEmpty()) {
					baos.write("No results.\n".getBytes());
				} else {
					model.write(baos, prefs.getDefaultRdfForm(), "");
				}
			} else if (parsed.isConstructType()) {
				Model model = qe.execConstruct();
				if (model.isEmpty()) {
					baos.write("No results.\n".getBytes());
				} else {
					model.write(baos, prefs.getDefaultRdfForm(), "");
				}
			} else if (parsed.isAskType()) {
				boolean result = qe.execAsk();
				baos.write(String.valueOf(result).getBytes("UTF-8"));
			}
			qe.close();
			long end = System.currentTimeMillis();
			baos.write(String.format("Query took %s.", format(end-start)).getBytes("UTF-8"));

			builder.append(baos.toString("UTF-8"));
			baos.close();
			
		} catch (Throwable e) {
			builder.append(e);
			e.printStackTrace();
		} finally {	
			if (dataset != null) {
				dataset.close();
			}
		}
		
		results = builder.toString();
	}

	public void runSparqlQuery(IFile resource) {
		try {
			InputStream is = resource.getContents();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			
			StringBuilder contents = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				contents.append(line);
				contents.append(" \n");
				line = reader.readLine();
			}
			
			reader.close();
			isr.close();
			is.close();
			
			runSparqlQuery(contents.toString());
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public static String format(long duration) {
		long fraction = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		
		fraction = duration % 1000;
		duration = duration / 1000;

		if (duration > 0) {
			seconds  = (int)(duration % 60);
			duration = duration / 60;
		}
		
		if (duration > 0) {
			minutes  = (int)(duration % 60);
			duration = duration / 60;
		}
		
		hours = (int)duration;
		
		return String.format("%d:%02d:%02d.%d", hours, minutes, seconds, fraction);
	}
}

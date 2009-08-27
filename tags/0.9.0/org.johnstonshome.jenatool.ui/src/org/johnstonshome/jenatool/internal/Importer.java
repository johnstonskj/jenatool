/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.internal;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.base.file.Location;

public class Importer {

	public boolean importResource(String resourceUrl, String format, Connection connection, String graphUrl, String baseUrl) {
		Dataset dataset = null;
		try {
			switch (connection.getType()) {
			case TDB:
				Location loc = new Location(connection.getUrl());
				dataset = TDBFactory.createDataset(loc);
				break;
			}
			Model target = null;
			if (graphUrl.equals("")) {
				target = dataset.getDefaultModel();
			} else {
				target = dataset.getNamedModel(graphUrl);
			}
			System.out.println("Graph: " + (graphUrl.equals("") ? "default" : graphUrl));
			System.out.println("*** BEFORE ***");
			target.write(System.out, "N-TRIPLES");
			target.read(resourceUrl, baseUrl, format.toUpperCase());
			System.out.println("*** AFTER ***");
			target.write(System.out, "N-TRIPLES");
			TDB.sync(dataset);
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		} finally {	
			if (dataset != null) {
				dataset.close();
			}
		}
		return true;
	}
}

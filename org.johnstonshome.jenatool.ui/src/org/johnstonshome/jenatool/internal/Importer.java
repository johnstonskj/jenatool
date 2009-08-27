/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.internal;

import com.hp.hpl.jena.rdf.model.Model;

public class Importer {

	public boolean importResource(String resourceUrl, String format, Connection connection, String graphUrl, String baseUrl) {
		try {
			connection.connect();
			Model target = null;
			if (graphUrl.equals("")) {
				target = connection.getDefaultModel();
			} else {
				target = connection.getNamedModel(graphUrl);
			}
			System.out.println("Graph: " + (graphUrl.equals("") ? "default" : graphUrl));
			System.out.println("*** BEFORE ***");
			target.write(System.out, "N-TRIPLES");
			target.read(resourceUrl, baseUrl, format.toUpperCase());
			System.out.println("*** AFTER ***");
			target.write(System.out, "N-TRIPLES");
			connection.sync();
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

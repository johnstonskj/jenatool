/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.johnstonshome.jenatool.internal.Connection;
import org.johnstonshome.jenatool.internal.ConnectionType;
import org.johnstonshome.jenatool.internal.Connections;
import org.osgi.framework.BundleContext;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.johnstonshome.jenatool.ui";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		IPath location = Platform.getStateLocation(this.getBundle());
		location = location.append("/connections.nt");
		File file = location.toFile();
    	Model model = ModelFactory.createMemModelMaker().createFreshModel();
    	Property property = model.createProperty("http://johnstonshome.org/jenatool/tdb-connection");
		List<Connection> connections = Connections.getConnections();
		if (file.exists()) {
	    	model.read(file.toURI().toString(), "N-TRIPLE");
	    	NodeIterator nodes = model.listObjectsOfProperty(property);
			while (nodes.hasNext()) {
				RDFNode node = nodes.next();
				if (node.isLiteral()) {
					connections.add(new Connection(ConnectionType.TDB, node.toString()));
				}
			}
		}
		if (connections.size() == 0) {
			connections.add(new Connection(ConnectionType.TDB, "/tmp/jena-tdb"));
		}
		Connections.setConnections(connections);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);

		IPath location = Platform.getStateLocation(this.getBundle());
		location = location.append("/connections.nt");
		File file = location.toFile();
    	Model model = ModelFactory.createMemModelMaker().createFreshModel();
    	Resource resource = model.createResource();
    	Property property = model.createProperty("http://johnstonshome.org/jenatool/tdb-connection");
		for (Connection connection : Connections.getConnections()) {
			model.add(resource, property, model.createLiteral(connection.getUrl()));
		}
		FileOutputStream os = new FileOutputStream(file);
		model.write(os, "N-TRIPLE");
		os.close();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}

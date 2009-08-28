package org.johnstonshome.jenatool.internal;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.tdb.base.file.Location;

public class TDBConnection extends Connection {

	private Dataset dataset = null;
	
	public TDBConnection(ConnectionType type) {
		super(type);
	}
	
	public TDBConnection(ConnectionType type, String url) {
		super(type, url);
	}
	
	public boolean isConnected() {
		return (dataset != null);
	}
	
	public synchronized void connect() {
		if (!isConnected()) {
			Location loc = new Location(getUrl());
			dataset = TDBFactory.createDataset(loc);		
		}
	}

	public void sync() {
		TDB.sync(dataset);
	}

	public synchronized void disconnect() {
		if (isConnected()) {
			dataset.close();
			dataset = null;
		}
	}
	
	public Model getDefaultModel() {
		return dataset.getDefaultModel();
	}
	
	public Model getNamedModel(String name) {
		return dataset.getNamedModel(name);
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public Dataset getDataset() {
		return dataset;
	}
}

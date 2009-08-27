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

public abstract class Connection {

	private ConnectionType type = ConnectionType.TDB;
	private String url = "";

	public Connection(ConnectionType type) {
		this.type = type;
	}
	
	public Connection(ConnectionType type, String url) {
		this.type = type;
		this.url = url;
	}
	
	public abstract boolean isConnected();
	public abstract void connect();
	public abstract void sync();
	public abstract void disconnect();
	
	public abstract Model getDefaultModel();
	public abstract Model getNamedModel(String name);
	
	public ConnectionType getType() {
		return type;
	}
	
	public void setType(ConnectionType type) {
		this.type = type;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return String.format("%s %s", this.type.toString(), this.url);
	}
}

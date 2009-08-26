/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.internal;

public class Connection {

	private ConnectionType type = ConnectionType.TDB;
	private String url = "";
	private String label = "";
	private boolean union = true;

	public Connection(ConnectionType type) {
		this.type = type;
	}
	
	public Connection(ConnectionType type, String url) {
		this.type = type;
		this.url = url;
		this.label = url;
	}
	
	public Connection(ConnectionType type, String url, String label) {
		this.type = type;
		this.url = url;
		this.label = label;
	}
	
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
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public boolean isUnion() {
		return union;
	}

	public void setUnion(boolean union) {
		this.union = union;
	}

	@Override
	public String toString() {
		return String.format("%s %s", this.type.toString(), this.label.equals("") ? this.url : this.label);
	}
}

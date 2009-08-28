/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Connections {

	private static List<Connection> connections = new ArrayList<Connection>();
	private static Connection defaultConnection = null;

	public static List<Connection> getConnections() {
		return connections;
	}
	
	public static void setConnections(List<Connection> newConnections) {
		connections = newConnections;
	}
	
	public static Connection getConnection(String title) {
		for (Connection connection : connections) {
			if (connection.toString().equals(title)) {
				return connection;
			}
		}
		return null;
	}
	
	public static boolean add(Connection newConnection) {
		return connections.add(newConnection);
	}
	
	public static boolean remove(Connection oldConnection) {
		return connections.remove(oldConnection);
	}
	
	public static boolean remove(String name) {
		boolean removed = false;
		for (Iterator<Connection> iterator = connections.iterator(); iterator.hasNext();) {
			Connection conn = (Connection) iterator.next();
			if (conn.toString().equals(name)) {
				iterator.remove();
				removed = true;
			}
		}
		return removed;
	}
	
	public static Connection getDefaultConnection() {
		return defaultConnection;
	}
	
	public static void setDefaultConnection(Connection newDefault) {
		defaultConnection = newDefault;
	}
}

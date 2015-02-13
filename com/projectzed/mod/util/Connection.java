/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.util;

/**
 * Utility class used primarily when trying to figure out whether to render something.
 * 
 * @author hockeyhurd
 * @version Feb 13, 2015
 */
public class Connection {

	private boolean connect;
	private int type;
	
	/**
	 * @param connect is connected.
	 * @param type type of connection.
	 */
	public Connection(boolean connect, int type) {
		this.connect = connect;
		this.type = type;
	}
	
	/**
	 * @return whether is connected or not.
	 */
	public boolean isConnected() {
		return connect;
	}
	
	/**
	 * @return connection type, (0: none, 1: fluid pipe, 2: Machine/other).
	 */
	public int getType() {
		return type;
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.fluid;


/**
 * Simple enum type for handling flow of fluid valves.
 * 
 * @author hockeyhurd
 * @version May 7, 2015
 */
public enum ValveType {

	INPUT, OUTPUT, NEUTRAL, UNKNOWN;
	
	public static final ValveType[] TYPES = new ValveType[] {
		INPUT, OUTPUT, NEUTRAL,
	};
	
	/**
	 * @param id id to use/get.
	 * @return valve type by ID.
	 */
	public static final ValveType getByID(int id) {
		return id >= 0 && id < TYPES.length ? TYPES[id] : UNKNOWN;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name();
	}
	
}

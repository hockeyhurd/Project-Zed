/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.energy.source;

/**
 * Enum for types of generation.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public enum EnumType {

	SOLAR("Sun", 20), BURNABLE("Burnable", 100), LAVA("Lava", 250), FUEL("Fuel", 600), WATER("Water", 5), FISSION("Fission", 10000),
	FUSION("Fusion", 50000), KINETIC("Kinetic", 5), OTHER("Other", 1);
	
	private String name;
	private int size;
	
	EnumType(String name, int size) {
		this.name = name;
		this.size = size;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public void setName(String newName) {
		this.name = newName;
	}

	public void setSize(int newSize) {
		this.size = newSize;
	}
	
	public final int getSize() {
		return this.size;
	}
	
}

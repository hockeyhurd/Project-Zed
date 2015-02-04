/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.energy.source;

/**
 * Enumeration for (pipe) colors.
 * 
 * @author hockeyhurd
 * @version Oct 31, 2014
 */
public enum EnumColor {

	RED("red", 0), ORANGE("orange", 1), CLEAR("clear", 2),GREEN("green", 3), BLUE("blue", 4);
	
	private static int counter = 0;
	private String color;
	private int id;
	
	/**
	 * Constructor for creating new color enumerators.
	 * @param colorAsText = color as text, NOTE: Color red = "red"
	 * @param id = id to assign to color.
	 */
	private EnumColor(String colorAsText, int id) {
		this.color = colorAsText;
		this.id = id;
	}
	
	/**
	 * Function used to get id for color.
	 * @return return color id.
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * Function used to get the color represented in a string.
	 * @return return string value stored for said color.
	 */
	public String getColorAsString() {
		return this.color;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return this.color;
	}
	
}

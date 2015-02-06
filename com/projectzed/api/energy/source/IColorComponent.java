/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.energy.source;

/**
 * 
 * 
 * @author hockeyhurd
 * @version Feb 5, 2015
 */
public interface IColorComponent {

	/**
	 * Function used to get color of component.
	 * 
	 * @return color of the component if has a color, else returns null.
	 */
	EnumColor getColor();
	
	/**
	 * Method to set color of given component.
	 * 
	 * @param color color to set.
	 */
	void setColor(EnumColor color);
	
}

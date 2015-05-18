/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.util;

/**
 * Simple interface for controlling redstone signals.
 * 
 * @author hockeyhurd
 * @version May 18, 2015
 */
public interface IRedstoneComponent {

	/**
	 * @return redstone configured type.
	 */
	EnumRedstoneType getRedstoneType();
	
	/**
	 * Sets redstone type to new type.
	 * 
	 * @param type type to set.
	 */
	void setRedstoneType(EnumRedstoneType type);
	
	/**
	 * @return redstone signal this object is receiving.
	 */
	int getRedstoneSignal();
	
	/**
	 * @return true if redstone signal != redstone threshold (getRedstoneSignal() != type.getThreshold()).
	 */
	boolean isActiveFromRedstoneSignal();
	
}

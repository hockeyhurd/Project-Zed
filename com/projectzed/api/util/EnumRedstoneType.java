/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.util;

/**
 * Simple enumeration for controlling redstone signal types.
 * 
 * @author hockeyhurd
 * @version May 18, 2015
 */
public enum EnumRedstoneType {

	DISABLED(-1), LOW(0x0), HIGH(0xf), CUSTOM(0xf);
	
	private int strength;
	
	/**
	 * @param strength max strength or threshold this type can handle.
	 */
	private EnumRedstoneType(int strength) {
		this.strength = strength;
	}
	
	/**
	 * Sets the custom redstone type to a configured numerical value.
	 * 
	 * @param type type to change as reference such that it must be of type 'CUSTOM'.
	 * @param strength strength to set value of.
	 */
	public void setCustom(EnumRedstoneType type, int strength) {
		if (type == CUSTOM) type.strength = strength;
	}
	
	/**
	 * Gets strength (threshold) for which this type handles.
	 * 
	 * @return strength (threshold).
	 */
	public int getThreshold() {
		return strength;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return "TYPE: " + name() + ", strength: " + getThreshold();
	}
	
}

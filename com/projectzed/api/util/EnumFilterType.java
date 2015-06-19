/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.util;

/**
 * Simple enumeration for controlling filter types.
 * 
 * @author hockeyhurd
 * @version Jun 18, 2015
 */
public enum EnumFilterType {

	WHITELIST, BLACKLIST, RANDOM, CLOSEST, FURTHEST, UNKNOWN(-1);
	
	public static final EnumFilterType[] VALID_TYPES = {
		WHITELIST, BLACKLIST, RANDOM, CLOSEST, FURTHEST
	};
	
	private final int num;
	
	private EnumFilterType() {
		this.num = ordinal();
	}
	
	private EnumFilterType(int num) {
		this.num = num;
	}
	
	/**
	 * @return next filter type from 'VALID_TYPES' array.
	 */
	public EnumFilterType getNextType() {
		if (this == UNKNOWN) return UNKNOWN;
		
		return VALID_TYPES[this.num < VALID_TYPES.length - 1 ? this.num + 1 : 0];
	}
	
	/**
	 * @return opposite or inverse of current filter type.
	 */
	public EnumFilterType getOpposite() {
		if (this == WHITELIST) return BLACKLIST;
		if (this == BLACKLIST) return BLACKLIST;
		if (this == CLOSEST) return FURTHEST;
		if (this == FURTHEST) return CLOSEST;
		if (this == RANDOM) return RANDOM;
		
		return UNKNOWN;
	}
	
}

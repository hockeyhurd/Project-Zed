/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.energy.source;

/**
 * House keeping class to define the source of energy generation.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public class Source {

	private EnumType type;
	private int packetSize;
	private float modifier;
	
	/**
	 * Default constructor.
	 * @param type = type of energy source.
	 * @param modifier = 
	 */
	public Source(EnumType type, float modifier) {
		this.type = type;
		this.modifier = modifier;
		this.packetSize = (int) Math.round(type.getSize() * modifier);
	}

	/**
	 * Alternative constructor with no modifier.
	 * @param type = type of energy source.
	 */
	public Source(EnumType type) {
		this.type = type;
		this.packetSize = type.getSize();
		this.modifier = 1.0f;
	}
	
	/**
	 * Alternative constructor allowing for custom/new sources of energy.
	 * @param sourceName = name of source.
	 * @param size = size of said source per tick.
	 * @param modifier = modifier value if applicable.
	 */
	public Source(String sourceName, int size, float modifier) {
		this.type = EnumType.OTHER;
		this.modifier = modifier;
		this.type.setName(sourceName);
		this.type.setSize(size);
		this.packetSize = (int) Math.round(this.type.getSize() * modifier);
	}
	
	/**
	 * Alternative constructor allowing for custom/new sources of energy.
	 * NOTE: modifier will be '1' or no change.
	 * @param sourceName = name of source.
	 * @param size = size of said source per tick.
	 */
	public Source(String sourceName, int size) {
		this(sourceName, size, 1.0f);
	}
	
	/**
	 * Gets the max amount of energy allowed to produce per tick.
	 * @return max amount of energy.
	 */
	public int getEffectiveSize() {
		return this.packetSize;
	}
	
	/**
	 * Allows changing of the modifier and updates the new packet size.
	 * @param modifier
	 */
	public void setModifier(float modifier) {
		this.modifier = modifier > 0.0f ? modifier : 1.0f;
		this.packetSize = (int) Math.round(this.type.getSize() * modifier);
	}
	
	/**
	 * @return source's modifier.
	 */
	public float getModifier() {
		return this.modifier;
	}

}

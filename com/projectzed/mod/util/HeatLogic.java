/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.util;

/**
 * Class containing seperated logic for machines that are effected/use heat.
 * 
 * @author hockeyhurd
 * @version Apr 9, 2015
 */
public class HeatLogic {

	public final int MAX;
	private int heat = 0;
	private float resistance;
	
	public HeatLogic(int max, float resistance) {
		this.MAX = max;
		this.resistance = resistance;
	}
	
	public void setResistance(float res) {
		this.resistance = res;
	}
	
	public float getResistance() {
		return this.resistance;
	}
	
	public void setCurrentHeat(int heat) {
		this.heat = heat;
	}
	
	public int getHeat() {
		return this.heat;
	}
	
	public int addHeat(int heat) {
		if (heat <= 0 || heat > this.MAX) return 0;
		
		if (this.heat + heat * this.resistance <= this.MAX) {
			this.heat += Math.ceil(heat * this.resistance);
			return heat;
		}
		
		else {
			int rem = (int) (this.MAX - Math.ceil(heat * this.resistance));
			this.heat += rem;
			
			return rem;
		}
	}
	
	public int subtractHeat(int heat) {
		if (heat <= 0 || heat > this.MAX) return 0;
		
		if (this.heat - heat * this.resistance >= 0) {
			this.heat -= Math.ceil(heat * this.resistance);
			return heat;
		}
		
		else {
			int rem = (int) Math.ceil(heat * this.resistance);
			this.heat = 0;
			
			return rem;
		}
	}

}

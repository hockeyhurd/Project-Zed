/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.heat;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Class containing seperated logic for machines that are effected/use heat.
 * 
 * @author hockeyhurd
 * @version Apr 9, 2015
 */
public class HeatLogic {

	/** Max amount of heat. */
	public final int MAX;

	private int heat = 20;
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
	
	public int addHeat(int heat, final boolean simulate) {
		if (heat <= 0 || heat > this.MAX) return 0;
		
		if (this.heat + heat * this.resistance <= this.MAX) {

			if (!simulate) {
				this.heat += Math.ceil(heat * this.resistance);
				return this.heat;
			}

			else return (int) (this.heat + Math.ceil(heat * this.resistance));
		}
		
		else {
			int rem = (int) (this.MAX - Math.ceil(heat * this.resistance));
			if (!simulate) {
				this.heat += rem;
				return this.heat;
			}
			
			else return this.heat + rem;
		}
	}
	
	public int subtractHeat(int heat, final boolean simulate) {
		if (heat <= 0 || heat > this.MAX) return 0;
		
		if (this.heat - heat * this.resistance >= 0) {
			if (!simulate) {
				this.heat -= Math.floor(heat * this.resistance);
				return this.heat;
			}

			else return (int) (this.heat - Math.ceil(heat * this.resistance));
		}
		
		else {
			int rem = (int) Math.floor(heat * this.resistance);
			if (!simulate) {
				this.heat = 0;
				return  this.heat;
			}
			
			else return this.heat - rem;
		}
	}

	private int getIncrementHeat(int mcu, int volume) {
		// int x = (int) Math.ceil(heat * resistance);
		// return (int) Math.ceil(1 * Math.pow(Math.E, x));

		return (int) Math.ceil((float) mcu / ((float) volume * 10f));
	}

	private int getDecrementHeat() {
		int x = (int) Math.ceil(heat * resistance);
		return (int) Math.ceil(10 * Math.pow(Math.E, -x));
	}

	public void update(final boolean running, int mcu, int volume) {

		// do heating:
		if (running) {
			// ProjectZed.logHelper.info("Heat before:", heat);
			// ProjectZed.logHelper.info("Increment heat:", getIncrementHeat(mcu, volume));
			heat += getIncrementHeat(mcu, volume);
			// heat = getIncrementHeat();
			// ProjectZed.logHelper.info("Heat after:", heat);
		}

		// do cooling:
		else {
			// heat -= getDecrementHeat();
			heat -= getIncrementHeat(mcu, volume);
		}
	}

	public void saveNBT(NBTTagCompound comp) {
		comp.setInteger("HeatLogic:Heat", this.heat);
		comp.setFloat("HeatLogic:Resistance", this.resistance);
	}

	public void readNBT(NBTTagCompound comp) {
		this.heat = comp.getInteger("HeatLogic:Heat");
		this.resistance = comp.getFloat("HeatLogic:Resistance");
	}

}

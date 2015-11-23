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
	private int max;

	private int heat = 20;
	private float resistance;
	
	public HeatLogic(int max, float resistance) {
		this.max = max;
		this.resistance = resistance;
	}
	
	public void setResistance(float res) {
		this.resistance = res;
	}
	
	public float getResistance() {
		return this.resistance;
	}

	public int getMaxHeat() {
		return max;
	}

	public void setMaxHeat(int maxHeat) {
		this.max = maxHeat;
	}

	public void setCurrentHeat(int heat) {
		this.heat = heat;
	}
	
	public int getHeat() {
		return this.heat;
	}
	
	public int addHeat(int heat, final boolean simulate) {
		if (heat <= 0 || heat > this.max) return 0;
		
		if (this.heat + heat * this.resistance <= this.max) {

			if (!simulate) {
				this.heat += Math.ceil(heat * this.resistance);
				return this.heat;
			}

			else return (int) (this.heat + Math.ceil(heat * this.resistance));
		}
		
		else {
			int rem = (int) (this.max - Math.ceil(heat * this.resistance));
			if (!simulate) {
				this.heat += rem;
				return this.heat;
			}
			
			else return this.heat + rem;
		}
	}
	
	public int subtractHeat(int heat, final boolean simulate) {
		if (heat <= 0 || heat > this.max) return 0;
		
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

	public static int getHeatFromValues(int mcu, int volume) {
		return (int) Math.ceil((float) mcu / ((float) volume * 10f));
	}

	@Deprecated
	private int getIncrementHeat(int mcu, int volume) {
		// int x = (int) Math.ceil(heat * resistance);
		// return (int) Math.ceil(1 * Math.pow(Math.E, x));

		return (int) Math.ceil((float) mcu / ((float) volume * 10f));
	}

	@Deprecated
	private int getDecrementHeat() {
		int x = (int) Math.ceil(heat * resistance);
		return (int) Math.ceil(10 * Math.pow(Math.E, -x));
	}

	public void update(final boolean running, int mcu, int volume) {

		// do heating:
		if (running) {
			if (heat >= max) return;

			// ProjectZed.logHelper.info("Heat before:", heat);
			// ProjectZed.logHelper.info("Increment heat:", getIncrementHeat(mcu, volume));
			heat = getIncrementHeat(mcu, volume);
			// addHeat(getIncrementHeat(mcu, volume), false);
			// heat = getIncrementHeat();
			// ProjectZed.logHelper.info("Heat after:", heat);
		}

		// do cooling:
		else {
			if (heat <= 0) return;

			// heat -= getDecrementHeat();
			heat -= getIncrementHeat(mcu, volume);
		}
	}

	public void saveNBT(NBTTagCompound comp) {
		comp.setInteger("HeatLogic:Heat", this.heat);
		comp.setInteger("HeatLogic:Max", this.max);
		comp.setFloat("HeatLogic:Resistance", this.resistance);
	}

	public void readNBT(NBTTagCompound comp) {
		this.heat = comp.getInteger("HeatLogic:Heat");
		this.max = comp.getInteger("HeatLogic:Max");
		this.resistance = comp.getFloat("HeatLogic:Resistance");
	}

}

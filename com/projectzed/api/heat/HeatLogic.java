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

	private static int startingHeat = 20;
	private int heat = startingHeat;
	private float resistance;

	/**
	 * @param max Max heat threshold.
	 * @param resistance Resistance value, used for calculating heat generation and thresholds.
	 */
	public HeatLogic(int max, float resistance) {
		this.max = max;
		this.resistance = resistance;
	}

	/**
	 * Sets the resistance.
	 *
	 * @param res Resistance value, used for calculating heat generation and thresholds.
	 */
	public void setResistance(float res) {
		this.resistance = res;
	}

	/**
	 * Gets the heat resistance.
	 *
	 * @return Heat resistance.
	 */
	public float getResistance() {
		return this.resistance;
	}

	/**
	 * Gets the max heat value.
	 *
	 * @return Max heat value.
	 */
	public int getMaxHeat() {
		return max;
	}

	/**
	 * Sets the max heat value.
	 *
	 * @param maxHeat Max heat value to set.
	 */
	public void setMaxHeat(int maxHeat) {
		this.max = maxHeat;
	}

	/**
	 * Overrides the current heat value with the providided value.
	 *
	 * @param heat Heat value to set.
	 */
	public void setCurrentHeat(int heat) {
		this.heat = heat;
	}

	/**
	 * Gets the current heat value.
	 *
	 * @return Current heat value.
	 */
	public int getHeat() {
		return this.heat;
	}

	/**
	 * Attempts to add heat to heat value.
	 *
	 * @param heat Amount of heat to add.
	 * @param simulate Boolean flag, set to 'true' to simulate, 'false' to execute changes.
	 * @return Resulting heat value.
	 */
	public int addHeat(int heat, final boolean simulate) {
		if (heat <= 0 || heat > getLimitingHeatFromResistance()) return 0;

		if (this.heat + heat * this.resistance <= getLimitingHeatFromResistance()) {

			if (!simulate) {
				this.heat += Math.ceil(heat * this.resistance);
				return this.heat;
			}

			else return (int) (this.heat + Math.ceil(heat * this.resistance));
		}

		else {
			int rem = (int) (getLimitingHeatFromResistance() - Math.ceil(heat * this.resistance));
			if (!simulate) {
				this.heat += rem;
				return this.heat;
			}

			else return this.heat + rem;
		}
	}

	/**
	 * Opposite of addHeat function except for subtraction.
	 *
	 * @deprecated
	 * Deprecated (1/19/2016) as there currently exists no use.  Therefore use with caution!
	 * <br>It is recommended to use addHeat function but with '-=' instead of '+='.
	 *
	 * @param heat Amount of heat to add.
	 * @param simulate Boolean flag, set to 'true' to simulate, 'false' to execute changes.
	 * @return Resulting heat value.
	 */
	@Deprecated
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

	/**
	 * Gets the appropriate heat from values.
	 *
	 * @param mcu McU energy amount.
	 * @param volume Volume of container.
	 * @return Heat value.
	 */
	public static int getHeatFromValues(int mcu, int volume) {
		return (int) Math.ceil((float) mcu / ((float) volume * 10f));
	}

	/**
	 * Gets the limiting threshold and is calculated by maxHeat * resistance value.
	 *
	 * @return limiting threshold.
	 */
	public int getLimitingHeatFromResistance() {
		return (int) Math.ceil(max * resistance);
	}

	/**
	 * Gets the increment heat value.
	 *
	 * @param mcu McU energy change.
	 * @param volume Volume of container.
	 * @return Increment heat value.
	 */
	private int getIncrementHeat(int mcu, int volume) {
		return (int) Math.ceil((float) mcu / ((float) volume * 10f) * resistance);
	}

	/**
	 * Main updating method.
	 *
	 * @param running Boolean flag whether to add heat or subtract heat.
	 * @param mcu McU energy change.
	 * @param volume Volume of container.
	 */
	public void update(final boolean running, int mcu, int volume) {

		// do heating:
		if (running) {
			// if (heat >= max) return;
			if (heat >= getLimitingHeatFromResistance()) return;

			addHeat(getIncrementHeat(mcu, volume), false);
		}

		// do cooling:
		else {
			if (heat <= startingHeat) return;
			if (mcu < 0) mcu = -mcu; // Normalize mcu energy input.

			heat -= getIncrementHeat(mcu, volume);
		}
	}

	/**
	 * Method to save data to NBT.
	 *
	 * @param comp NBTTagCompound to write to.
	 */
	public void saveNBT(NBTTagCompound comp) {
		comp.setInteger("HeatLogic:Heat", this.heat);
		comp.setInteger("HeatLogic:Max", this.max);
		comp.setFloat("HeatLogic:Resistance", this.resistance);
	}

	/**
	 * Method to read data from NBT.
	 *
	 * @param comp NBTTagCompound to read from.
	 */
	public void readNBT(NBTTagCompound comp) {
		this.heat = comp.getInteger("HeatLogic:Heat");
		this.max = comp.getInteger("HeatLogic:Max");
		this.resistance = comp.getFloat("HeatLogic:Resistance");
	}

}

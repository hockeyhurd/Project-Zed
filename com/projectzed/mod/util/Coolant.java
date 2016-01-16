/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.util;

import com.projectzed.api.util.FluidUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * Largely immutable class (except for ability to adjust the amount of fluid as needed), used for
 * tracking and storing fluid based coolant for anything that uses a coolant.
 *
 * @author hockeyhurd
 * @version 1/14/2016.
 */
public final class Coolant {

	/**
	 * An indication to class methods to treat the amount of coolant as if
	 *
	 */
	public static final int FULL_FLAG = -1;
	private static final String NBT_TAG_PREFIX = "Coolant Fluid ";

	public static final Coolant AIR = new Coolant(null, FULL_FLAG, 1.0f);

	private final Fluid fluid;
	private int amount;
	private final float efficiency;

	/**
	 * Creates coolant with default amount set to 'full'.
	 *
	 * @param fluid Fluid used.
	 * @param efficiency Efficiency of coolant.
	 */
	public Coolant(Fluid fluid, float efficiency) {
		this(fluid, FULL_FLAG, efficiency);
	}

	/**
	 * Creates coolant with a givent amount.
	 *
	 * @param fluid Fluid used.
	 * @param amount Amount of coolant.
	 * @param efficiency Efficiency of coolant.
	 */
	public Coolant(Fluid fluid, int amount, float efficiency) {
		this.fluid = fluid;
		this.amount = amount;
		this.efficiency = efficiency;
	}

	/**
	 * Coolant's fluid.
	 *
	 * @return Fluid in coolant.
	 */
	public Fluid getFluid() {
		return fluid;
	}

	/**
	 * Amount of coolant.
	 *
	 * @return Int amount of coolant (typically in millibuckets or 'mb').
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Sets the amount of coolant.
	 *
	 * @param amount Int amount of coolant (typically in millibuckets or 'mb').
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * Gets the base efficiency of the coolant.
	 *
	 * @return Float base efficiency.
	 */
	public float getBaseEfficiency() {
		return efficiency;
	}

	/**
	 * Gets a copy of the coolant object.
	 *
	 * @return Coolant copy object.
	 */
	public Coolant copy() {
		return new Coolant(fluid, amount, efficiency);
	}

	/**
	 * Checks if the coolant is enough to fill a given dimensioned size reactor.
	 *
	 * @param width int.
	 * @param height int.
	 * @param depth int.
	 * @param offset Amount to disregard (i.e. ReactantCore).
	 * @return True if fills the reactor's chamber, else return false.
	 */
	public boolean isFullReactorChamber(int width, int height, int depth, int offset) {
		final int effectiveVolume = calculateChamberSize(width, height, depth, offset);

		return amount == FULL_FLAG || effectiveVolume == amount / Reference.Constants.MILLI_BUCKETS_PER_BLOCK_SPACE;
	}

	/**
	 * Calculates the effective efficiency of the coolant.
	 *
	 * @param width int.
	 * @param height int.
	 * @param depth int.
	 * @param offset Amount to disregard (i.e. ReactantCore).
	 * @return Effective efficiency, calculated by the percentage filled of the reactor and the amount of coolant.
	 */
	public float getEffectiveEfficiency(int width, int height, int depth, int offset) {
		if (amount == FULL_FLAG) return efficiency;

		final int effectiveVolume = calculateChamberSize(width, height, depth, offset);
		final float percentFilled = (amount / Reference.Constants.MILLI_BUCKETS_PER_BLOCK_SPACE) / effectiveVolume;

		return percentFilled * efficiency;
	}

	/**
	 * Static function to read Coolant from NBT.
	 *
	 * @param comp NBTTagCompound to read from.
	 * @return Coolant object.
	 */
	public static Coolant readNBT(NBTTagCompound comp) {
		String fluidName = comp.getString(NBT_TAG_PREFIX + "Fluid Name");
		FluidStack fluidStack = FluidUtils.readNBT(comp, NBT_TAG_PREFIX + fluidName);
		float efficiency = comp.getFloat(NBT_TAG_PREFIX + "Efficiency");

		return new Coolant(fluidStack.getFluid(), fluidStack.amount, efficiency);
	}

	/**
	 * Method to save Coolant to NBT.
	 *
	 * @param comp NBTTagCompound to write to.
	 * @param coolant Coolant object to write.
	 */
	public static void saveNBT(NBTTagCompound comp, Coolant coolant) {
		FluidStack fluidStack = FluidUtils.createFluidStack(coolant.fluid, coolant.amount);

		comp.setString(NBT_TAG_PREFIX + "Fluid Name", coolant.fluid.getName());
		FluidUtils.saveNBT(comp, fluidStack, NBT_TAG_PREFIX + coolant.fluid.getName());
		comp.setFloat(NBT_TAG_PREFIX + "Efficiency", coolant.efficiency);
	}

	/**
	 * Static function to help calculate the chamber volume given the size in (whole) blocks.
	 *
	 * @param width int.
	 * @param height int.
	 * @param depth int.
	 * @param offset Amount to disregard (i.e. ReactantCore).
	 * @return Calculated nuclear reactor chamber size.
	 */
	private static int calculateChamberSize(int width, int height, int depth, int offset) {
		return (width - 2) * (height - 2) * (depth - 2) - offset;
	}

}

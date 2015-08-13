/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.api.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/**
 * Generic fluid/fluidstack utility class.
 *
 * @author hockeyhurd
 * @version 8/11/2015.
 */
public final class FluidUtils {

	public static final String FLUID_TAG = "FluidTank";
	public static final String FLUID_ID = "FluidID";
	public static final String FLUID_AMOUNT = "FluidAmount";

	private FluidUtils() {
	}

	/**
	 * Function attempts to read array of fluid tanks data from nbt.
	 *
	 * @param compound nbttagcompound to reference.
	 * @param tanks fluid tank array to configure.
	 * @return adjusted fluid tank array.
	 */
	public static FluidTank[] readNBT(NBTTagCompound compound, final FluidTank[] tanks) {
		for (int i = 0; i < tanks.length; i++) {
			tanks[i] = readNBT(compound, tanks[i], i);
		}

		return tanks;
	}

	/**
	 * Function attempts to read fluid tank data from nbt.
	 *
	 * @param compound nbttagcompound to reference.
	 * @param tank fluid tank to configure.
	 * @return adjusted fluid tank.
	 */
	public static FluidTank readNBT(NBTTagCompound compound, final FluidTank tank) {
		return readNBT(compound, tank, -1);
	}

	/**
	 * Function attempts to read fluid tank data from nbt.
	 *
	 * @param compound nbttagcompound to reference.
	 * @param tank fluid tank to configure.
	 * @param tankID ID of tank to check/reference.
	 * @return adjusted fluid tank.
	 */
	public static FluidTank readNBT(NBTTagCompound compound, final FluidTank tank, final int tankID) {
		final String suffix = tankID != -1 ? "" + tankID : "";

		NBTTagList list;
		if (suffix.length() > 0) list = compound.getTagList(FLUID_TAG + suffix, 10);
		else list = compound.getTagList(FLUID_TAG, 10);

		NBTTagCompound temp = list.getCompoundTagAt(0);

		final int fluidID = temp.getInteger(FLUID_ID);
		final int fluidAmount = temp.getInteger(FLUID_AMOUNT);

		tank.setFluid(creatFluidStack(fluidID, fluidAmount));

		return tank;
	}

	/**
	 * Method attempts to save fluid tank array data to nbt.
	 *
	 * @param compound nbttagcompound to reference.
	 * @param tanks fluid tank array to reference.
	 */
	public static void saveNBT(NBTTagCompound compound, final FluidTank[] tanks) {
		for (int i = 0; i < tanks.length; i++) {
			saveNBT(compound, tanks[i], i);
		}
	}

	/**
	 * Method attempts to save fluid tank data to nbt.
	 *
	 * @param compound nbttagcompound to reference.
	 * @param tank fluid tank to reference.
	 */
	public static void saveNBT(NBTTagCompound compound, final FluidTank tank) {
		saveNBT(compound, tank, -1);
	}

	/**
	 * Method attempts to save fluid tank data to nbt.
	 *
	 * @param compound nbttagcompound to reference.
	 * @param tank fluid tank to reference.
	 * @param tankID ID of tank to use/reference.
	 */
	public static void saveNBT(NBTTagCompound compound, final FluidTank tank, final int tankID) {
		final String suffix = tankID != -1 ? "" + tankID : "";
		final boolean checkLen = suffix.length() > 0;

		NBTTagList list;
		if (checkLen) list = compound.getTagList(FLUID_TAG, 10);
		else list = compound.getTagList(FLUID_TAG + suffix, 10);

		FluidStack fluidStack = tank.getFluid();

		NBTTagCompound temp = new NBTTagCompound();
		temp.setInteger(FLUID_ID, fluidStack != null ? fluidStack.getFluidID() : -1);
		temp.setInteger(FLUID_AMOUNT, fluidStack != null ? fluidStack.amount : 0);

		list.appendTag(temp);

		if (checkLen) compound.setTag(FLUID_TAG + suffix, list);
		else compound.setTag(FLUID_TAG, list);
	}

	/**
	 * Function attempts to create a fluidstack object from provided parameters.
	 *
	 * @param fluidID ID of fluid.
	 * @param fluidAmount amount of fluid that should be contained.
	 * @return fluidstack object if has sufficient parameters, else returns null.
	 */
	public static FluidStack creatFluidStack(final int fluidID, final int fluidAmount) {
		return fluidID > 0 && fluidAmount >= 0 ? new FluidStack(FluidRegistry.getFluid(fluidID), fluidAmount) : null;
	}

	/**
	 * Static function for convenient access to checking fluid equality.
	 *
	 * @param stack reference stack.
	 * @param other comparator stack.
	 * @return true if equal, else returns false.
	 */
	public static boolean areFluidsEqual(FluidStack stack, FluidStack other) {
		return stack.isFluidEqual(other);
	}

	/**
	 * Static function for convenient access to checking fluid stack equality.
	 *
	 * @param stack reference stack.
	 * @param other comparator stack.
	 * @return true if equal, else returns false.
	 */
	public static boolean areFluidStacksEqual(FluidStack stack, FluidStack other) {
		return stack.isFluidStackIdentical(other);
	}

}

/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.item;

import com.projectzed.api.util.SidedInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Interfacing for item's that have ability to change
 * its properties from a shortcut key(s).
 *
 * @author hockeyhurd
 * @version 3/24/2016.
 */
public interface IItemAdjustable {

	/**
	 * Increments some data.
	 */
	void increment(EntityPlayer player, ItemStack stack);

	/**
	 * Decrements some data.
	 */
	void decrement(EntityPlayer player, ItemStack stack);

	/**
	 * Gets the data.
	 *
	 * @return All the datas.
	 */
	Object[] getData();

	/**
	 * Sets the data.
	 *
	 * @param data Datas to set.
	 */
	void setData(ItemStack stack, Object... data);

	/**
	 * Writes NBT data to an ItemStack.
	 *
	 * @param stack ItemStack to write.
	 */
	void writeToNBT(ItemStack stack);

	/**
	 * Reads data from an ItemStack.
	 *
	 * @param stack ItemStack to read from.
	 * @return Read object(s).
	 */
	Object[] readFromNBT(ItemStack stack);

	/**
	 * Callback handleing packet sending to a given (destination) side.
	 *
	 * @param stack ItemStack to send.
	 * @param sidedInfo SidedInfo containing packet related information.
	 * @param data data to send.
	 */
	void sendPacket(ItemStack stack, SidedInfo sidedInfo, Object... data);

}

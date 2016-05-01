/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.tileentity.machine;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityPatternEncoder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

/**
 * TileEntity class for pattern encoder.
 *
 * @author hockeyhurd
 * @version 4/30/2016.
 */
public class TileEntityPatternEncoder extends AbstractTileEntityMachine {

	public static final int CRAFTING_MATRIX_SIZE = 3;
	private static final int PATTERN_IN_INDEX = 10;
	private static final int PATTERN_OUT_INDEX = 11;

	private boolean encode;

	public TileEntityPatternEncoder() {
		super("patternEncoder");
	}

	/**
	 * Gets if in encoding state.
	 *
	 * @return Encoding state.
	 */
	public boolean isEncoding() {
		return encode;
	}

	/**
	 * Sets the encoding state.
	 *
	 * @param encode boolean encode.
	 */
	public void setEncode(boolean encode) {
		this.encode = encode;
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public int getSizeUpgradeSlots() {
		return 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0x40;
	}

	@Override
	protected void initContentsArray() {
	}

	@Override
	protected void initSlotsArray() {
		slots = new ItemStack[3 * 3 + 3];
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[0];
	}

	@Override
	protected boolean canSmelt() {
		final ItemStack outStack = getStackInSlot(PATTERN_OUT_INDEX);

		// If we have an encoded pattern, we can use its contents.
		if (outStack != null && outStack.stackSize > 0) return true;

		final ItemStack inStack = getStackInSlot(PATTERN_IN_INDEX);

		// If we are out of blank patterns, we can't do anything... return false.
		if (inStack == null || inStack.stackSize == 0) return false;

		// Make sure to check that some itemstacks are in crafting matrix!
		for (int i = 0; i < CRAFTING_MATRIX_SIZE * CRAFTING_MATRIX_SIZE; i++) {
			ItemStack stack = getStackInSlot(i);
			if (stack != null && stack.stackSize > 0) return true;
		}

		return false;
	}

	@Override
	public void smeltItem() {
		if (canSmelt()) {
			ItemStack fromStack = null;
			ItemStack outStack = getStackInSlot(PATTERN_OUT_INDEX);
			if (outStack != null && outStack.stackSize > 0) fromStack = outStack;
			else {
				ItemStack inStack = getStackInSlot(PATTERN_IN_INDEX);
				if (inStack != null && inStack.stackSize > 0) fromStack = inStack;
			}

			// If has no source pattern -> return.
			if (fromStack == null || fromStack.stackSize == 0) return;


		}
	}

	@Override
	public Sound getSound() {
		return null;
	}

	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);

		comp.setBoolean("PatternEncoder:StatusEncoding", encode);
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);

		encode = comp.getBoolean("PatternEncoder:StatusEncoding");
	}

	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityPatternEncoder(this));
	}

	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityPatternEncoder(this));
	}

}

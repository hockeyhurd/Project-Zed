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

import com.projectzed.api.item.IPattern;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.SidedInfo;
import com.projectzed.api.util.Sound;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.SoundHandler;
import com.projectzed.mod.handler.message.MessageTileEntityPatternEncoder;
import com.projectzed.mod.tileentity.interfaces.IEncodable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * TileEntity class for pattern encoder.
 *
 * @author hockeyhurd
 * @version 4/30/2016.
 */
public class TileEntityPatternEncoder extends AbstractTileEntityMachine implements IEncodable {

	public static final int CRAFTING_MATRIX_SIZE = 3;
	public static final int RESULT_STACK_INDEX = 9;
	public static final int PATTERN_IN_INDEX = 10;
	public static final int PATTERN_OUT_INDEX = 11;

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
	public int[] getSlotsForFace(EnumFacing side) {
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
			int fromStackIndex = -1;
			ItemStack fromStack = null;
			ItemStack outStack = getStackInSlot(PATTERN_OUT_INDEX);
			if (outStack != null && outStack.stackSize > 0) {
				fromStack = outStack;
				fromStackIndex = PATTERN_OUT_INDEX;
			}

			else {
				ItemStack inStack = getStackInSlot(PATTERN_IN_INDEX);
				if (inStack != null && inStack.stackSize > 0) {
					fromStack = inStack;
					fromStackIndex = PATTERN_IN_INDEX;
				}
			}

			// If has no source pattern -> return.
			if (fromStack == null || fromStack.stackSize == 0 || fromStackIndex < 0) return;

			IPattern patternItem = (IPattern) fromStack.getItem();

			ItemStack[][] patternArr = new ItemStack[CRAFTING_MATRIX_SIZE][CRAFTING_MATRIX_SIZE];

			for (int y = 0; y < CRAFTING_MATRIX_SIZE; y++) {
				for (int x = 0; x < CRAFTING_MATRIX_SIZE; x++) {
					patternArr[y][x] = getStackInSlot(x + y * CRAFTING_MATRIX_SIZE);
				}
			}

			final ItemStack fromStackPull = new ItemStack(ProjectZed.craftingPattern);

			patternItem.setPattern(fromStackPull, patternArr, getStackInSlot(RESULT_STACK_INDEX));
			fromStack.stackSize--;

			if (fromStack.stackSize <= 0) fromStack = null;
			setInventorySlotContents(fromStackIndex, fromStack);
			setInventorySlotContents(PATTERN_OUT_INDEX, fromStackPull);
		}
	}

	@Override
	public void update() {
		super.update();

		if (!worldObj.isRemote) {
			if (isActiveFromRedstoneSignal() && isBurning() && canSmelt()) {
				powerMode = true;

				if (isEncoding()) {
					smeltItem();
					encode = false;
				}

				if (getSound() != null && worldObj.getTotalWorldTime() % (20L * getSound().LENGTH) == 0)
					SoundHandler.playEffect(getSound(), worldObj, worldVec());

				// PacketHandler.INSTANCE.sendToAll(new MessageTileEntityPatternEncoder(this));
				sendMessage(new SidedInfo(Side.CLIENT, SidedInfo.EnumClientPacket.ALL_AROUND,
						new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32.0f), null));
			}
		}
	}

	@Override
	public Sound getSound() {
		return null;
	}

	@Override
	public boolean encode(boolean simulate) {
		if (canSmelt()) {
			if (!simulate) cookTime = 0;

			return true;
		}

		return false;
	}

	@Override
	public void sendMessage(SidedInfo sidedInfo) {
		if (sidedInfo.isSideServer())
			PacketHandler.INSTANCE.sendToServer(new MessageTileEntityPatternEncoder(this));

		else {
			if (sidedInfo.packet == SidedInfo.EnumClientPacket.ALL)
				PacketHandler.INSTANCE.sendToAll(new MessageTileEntityPatternEncoder(this));
			else if (sidedInfo.packet == SidedInfo.EnumClientPacket.ALL_AROUND && sidedInfo.packet.getTargetPoint() != null)
				PacketHandler.INSTANCE.sendToAllAround(new MessageTileEntityPatternEncoder(this), sidedInfo.packet.getTargetPoint());
			else if (sidedInfo.packet == SidedInfo.EnumClientPacket.PLAYER && sidedInfo.packet.getPlayer() != null)
				PacketHandler.INSTANCE.sendTo(new MessageTileEntityPatternEncoder(this), (EntityPlayerMP) sidedInfo.packet.getPlayer());
			else ProjectZed.logHelper.severe("Warning sending client packet!");
		}
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

	/*@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityPatternEncoder(this));
	}*/

	@Override
	public NBTTagCompound getUpdateTag() {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityPatternEncoder(this));

		final NBTTagCompound comp = getTileData();
		saveNBT(comp);

		return comp;
	}

	@Override
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityPatternEncoder(this));
	}

}

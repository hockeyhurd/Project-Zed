/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity;

import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityFabricationTable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;

/**
 * TileEntity code for Fabrication Table.
 * 
 * @author hockeyhurd
 * @version Nov 22, 2014
 */
public class TileEntityFabricationTable extends AbstractTileEntityGeneric {

	public TileEntityFabricationTable() {
		super();
		this.customName = "fabricationTable";
		// this.slots = new ItemStack[10 + 6 * 12 + 4 * 9];
		// this.slots = new ItemStack[10 + 6 * 12];
	}

	@Override
	public int getSizeInventory() {
		return this.slots.length;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if (slot >= 0 && slot < this.slots.length) this.slots[slot] = stack;
		else ProjectZed.logHelper.warn("Error! Please check you are placing in the correct slot index!\t" + slot);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	protected void initContentsArray() {
		this.slots = new ItemStack[10 + 6 * 12];
	}

	@Override
	protected void initSlotsArray() {
		// this.craftingMatrix = new ItemStack[9];
	}

	@Override
	public void setCustomName(String name) {
		this.customName = name;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot > 3 * 3 + 1;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		int[] slots = new int[this.slots.length - (3 * 3 + 1)];
		
		for (int i = 0; i < slots.length; i++) {
			slots[i] = (3 * 3 + 1) + i;
		}
		
		return slots;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return slot > 3 * 3 + 1;
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	/*@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityFabricationTable(this));
	}*/

	@Override
	public NBTTagCompound getUpdateTag() {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityFabricationTable(this));

		final NBTTagCompound comp = getTileData();
		saveNBT(comp);

		return comp;
	}

	@Override
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityFabricationTable(this));
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

}

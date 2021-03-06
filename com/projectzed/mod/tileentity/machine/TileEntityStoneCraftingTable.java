/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.machine;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.api.tileentity.IWrenchable;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityStoneCraftingTable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Class containing tileentity code for 
 * 
 * @author hockeyhurd
 * @version Mar 31, 2015
 */
public class TileEntityStoneCraftingTable extends AbstractTileEntityGeneric implements IWrenchable {

	private EnumFacing frontFacing;

	public TileEntityStoneCraftingTable() {
		this.customName = "craftingStoneTable";
	}

	@Override
	public int getSizeInventory() {
		return this.slots.length;
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
		this.slots = new ItemStack[3 * 3 + 1];
	}

	@Override
	public void setCustomName(String name) {
		this.customName = name;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot != 0;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return false;
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

	@Override
	public void update() {
		super.update();
		
		// if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0) {

			/*PacketHandler.INSTANCE.sendToAllAround(new MessageTileEntityStoneCraftingTable(this),
					new TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 0x10));*/
		// }
	}

	/*@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityStoneCraftingTable(this));
	}*/

	@Override
	public NBTTagCompound getUpdateTag() {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityStoneCraftingTable(this));

		final NBTTagCompound comp = getTileData();
		saveNBT(comp);

		return comp;
	}
	
	@Override
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityStoneCraftingTable(this));
	}

	@Override
	public EnumFacing getRotatedState(EnumFacing facingDir, IBlockState currentState) {
		if (facingDir == EnumFacing.DOWN || facingDir == EnumFacing.UP) return frontFacing;

		// return (frontFacing = frontFacing.rotateY());
		return (frontFacing = facingDir.getOpposite());
	}

	@Override
	public EnumFacing getCurrentFacing() {
		// return frontFacing;
		return EnumFacing.getFront(getBlockMetadata()); // TODO: temp fix until sync issues are resolved.
	}

	@Override
	public void setFrontFacing(EnumFacing face) {
		this.frontFacing = face;
	}

	@Override
	public boolean canRotateTE() {
		return true;
	}

	@Override
	public void onInteract(ItemStack stack, EntityPlayer player, World world, Vector3<Integer> vec) {
	}

	@Override
	public boolean canSaveDataOnPickup() {
		return true;
	}
}

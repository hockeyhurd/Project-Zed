/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.api.tileentity.IMultiBlockable;
import com.projectzed.api.tileentity.IWrenchable;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.block.container.BlockNuclearIOPort;

/**
 * Class containing tileentity code for nuclearIOPort.
 * 
 * @author hockeyhurd
 * @version Mar 19, 2015
 */
public class TileEntityNuclearIOPort extends AbstractTileEntityNuclearComponent implements IWrenchable {

	// TODO: This class should take over IO of TileEntityNuclearController. For now this TE is set to optional use.
	
	private byte meta;
	
	public TileEntityNuclearIOPort() {
		super("nuclearIOPort");
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getInventoryName()
	 */
	@Override
	public String getInventoryName() {
		return "container.nuclearIOPort";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
		this.slots = new ItemStack[2];
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return this.slots.length;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#canUpdate()
	 */
	@Override
	public boolean canUpdate() {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#updateEntity()
	 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0) {
			byte current = (byte) this.blockMetadata;
			if (this.meta != current) setMetaOnUpdate(current);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] { 0, 1, 2, 4, 5 };
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return this.getBlockMetadata() == 1 && slot == 1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return this.getBlockMetadata() == 2 && slot == 2;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		
		this.meta = comp.getByte("ProjectZedNuclearIOPortMeta");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);

		comp.setByte("ProjectZedNuclearIOPortMeta", this.meta);
	}
	
	/**
	 * @return the correct meta data for when multiblock structure is 'active'
	 */
	public byte getStoredMeta() {
		return this.meta;
	}
	
	/**
	 * Sets stored meta data as byte.
	 * 
	 * @param meta new meta data value.
	 */
	public void setMetaOnUpdate(byte meta) {
		if (meta > 0) this.meta = meta;
	}
	
	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#reset()
	 */
	@Override
	public void reset() {
		this.isMaster = false;
		this.hasMaster = false;
		this.masterVec = Vector4Helper.zero.getVector4i();
		
		setMetaOnUpdate((byte) worldObj.getBlockMetadata(worldVec().x, worldVec().y, worldVec().z));
		((BlockNuclearIOPort) worldObj.getBlock(worldVec().x, worldVec().y, worldVec().z)).updateMeta(false, worldObj, worldVec());
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isUnique()
	 */
	@Override
	public boolean isUnique() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isSubstituable()
	 */
	@Override
	public boolean isSubstituable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getSubList()
	 */
	@Override
	public List<IMultiBlockable> getSubList() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getAmountFromSize(int, int, int)
	 */
	@Override
	public int getAmountFromSize(int width, int height, int depth) {
		return 2;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getBlock()
	 */
	@Override
	public Block getBlock() {
		return ProjectZed.nuclearIOPort;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#getRotationMatrix()
	 */
	@Override
	public byte[] getRotationMatrix() {
		return new byte[] { 1, 2 };
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#canRotateTE()
	 */
	@Override
	public boolean canRotateTE() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#canSaveDataOnPickup()
	 */
	@Override
	public boolean canSaveDataOnPickup() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#dataToSave()
	 */
	@Override
	public HashMap<String, Number> dataToSave() {
		HashMap<String, Number> data = new HashMap<String, Number>();
		
		data.put("Meta", this.meta);
		
		return data;
	}

}

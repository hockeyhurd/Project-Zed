/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity.container;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.api.tileentity.IMultiBlockable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

/**
 * Framework for nuclear chamber components.
 * 
 * @author hockeyhurd
 * @version Feb 23, 2015
 */
public abstract class AbstractTileEntityNuclearComponent extends AbstractTileEntityGeneric implements IMultiBlockable<AbstractTileEntityGeneric> {

	protected boolean isMaster, hasMaster;
	protected Vector3<Integer> masterVec = Vector3.zero.getVector3i();
	
	/**
	 * @param name
	 */
	public AbstractTileEntityNuclearComponent(String name) {
		setCustomName(name);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#setCustomName(java.lang.String)
	 */
	@Override
	public void setCustomName(String name) {
		this.customName = name;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#canUpdate()
	 */
	@Override
	public boolean canUpdate() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#isUnique()
	 */
	@Override
	public abstract boolean isUnique();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#isSubstituable()
	 */
	@Override
	public abstract boolean isSubstituable();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#getSubList()
	 */
	@Override
	public abstract List<IMultiBlockable> getSubList();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#getAmountFromSize(int, int, int)
	 */
	@Override
	public abstract int getAmountFromSize(int width, int height, int depth);

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#isMaster()
	 */
	@Override
	public boolean isMaster() {
		return isMaster;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#setIsMaster(boolean)
	 */
	@Override
	public void setIsMaster(boolean master) {
		this.isMaster = master;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#hasMaster()
	 */
	@Override
	public boolean hasMaster() {
		return hasMaster;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#setHasMaster(boolean)
	 */
	@Override
	public void setHasMaster(boolean master) {
		this.hasMaster = master;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#setMasterVec(com.hockeyhurd.api.math.Vector3)
	 */
	@Override
	public void setMasterVec(Vector3<Integer> vec) {
		this.masterVec = vec;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#getMasterVec()
	 */
	@Override
	public Vector3<Integer> getMasterVec() {
		return masterVec;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#getInstance()
	 */
	@Override
	public AbstractTileEntityGeneric getInstance() {
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#getBlock()
	 */
	@Override
	public abstract Block getBlock();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#worldVec()
	 */
	@Override
	public Vector3<Integer> worldVec() {
		return new Vector3<Integer>(this.xCoord, this.yCoord, this.zCoord);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		
		// multiblock stuffs:
		isMaster = comp.getBoolean("ProjectZedIsMaster");
		hasMaster = comp.getBoolean("ProjectZedHasMaster");

		if (masterVec == null) masterVec = Vector3.zero.getVector3i();
		masterVec.x = comp.getInteger("ProjectZedMasterX");
		masterVec.y = comp.getInteger("ProjectZedMasterY");
		masterVec.z = comp.getInteger("ProjectZedMasterZ");
	}

	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
		
		// multiblock stuffs:
		comp.setBoolean("ProjectZedIsMaster", isMaster);
		comp.setBoolean("ProjectZedHasMaster", hasMaster);
		comp.setInteger("ProjectZedMasterX", masterVec.x);
		comp.setInteger("ProjectZedMasterY", masterVec.y);
		comp.setInteger("ProjectZedMasterZ", masterVec.z);
	}

}

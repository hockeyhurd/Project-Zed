/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity.container;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlock;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.api.tileentity.IMultiBlockable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

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
		setCustomName("container." + name);
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	@Override
	protected void initContentsArray() {
	}

	@Override
	protected void initSlotsArray() {
	}

	@Override
	public void setCustomName(String name) {
		this.customName = name;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return null;
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
	public boolean canUpdate() {
		return false;
	}

	@Override
	public abstract boolean isUnique();

	@Override
	public abstract boolean isSubstituable();

	@Override
	public abstract List<IMultiBlockable> getSubList();

	@Override
	public abstract int getAmountFromSize(int width, int height, int depth);

	@Override
	public boolean isMaster() {
		return isMaster;
	}

	@Override
	public void setIsMaster(boolean master) {
		this.isMaster = master;
	}

	@Override
	public boolean hasMaster() {
		return hasMaster;
	}
	
	@Override
	public void setHasMaster(boolean master) {
		this.hasMaster = master;
	}

	@Override
	public void setMasterVec(Vector3<Integer> vec) {
		this.masterVec = vec;
	}

	@Override
	public Vector3<Integer> getMasterVec() {
		return masterVec;
	}

	@Override
	public AbstractTileEntityGeneric getInstance() {
		return this;
	}
	
	@Override
	public abstract AbstractHCoreBlock getBlock();

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
	public Vector3<Integer> worldVec() {
		return VectorHelper.toVector3i(pos);
	}
	
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

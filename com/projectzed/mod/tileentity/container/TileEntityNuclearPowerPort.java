/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.api.tileentity.IMultiBlockable;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.block.container.BlockNuclearPowerPort;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityEnergyContainer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;

import java.util.List;

/**
 * TileEntity code for nuclearPowerPort.
 * 
 * @author hockeyhurd
 * @version Mar 11, 2015
 */
public class TileEntityNuclearPowerPort extends AbstractTileEntityEnergyContainer implements IMultiBlockable<AbstractTileEntityGeneric> {

	private boolean hasMaster;
	private Vector3<Integer> masterVec = Vector3.zero.getVector3i();
	
	public TileEntityNuclearPowerPort() {
		super("nuclearPowerPort");
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getMaxImportRate()
	 */
	@Override
	public int getMaxImportRate() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getMaxExportRate()
	 */
	@Override
	public int getMaxExportRate() {
		if (hasMaster) {
			AbstractTileEntityGenerator te = (AbstractTileEntityGenerator) worldObj.getTileEntity(masterVec.x, masterVec.y, masterVec.z);
			return te.getMaxExportRate();
		}
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#requestPower(com.projectzed.api.energy.storage.IEnergyContainer, int)
	 */
	@Override
	public int requestPower(IEnergyContainer cont, int amount) {
		if (hasMaster) {
			AbstractTileEntityGenerator te = (AbstractTileEntityGenerator) worldObj.getTileEntity(masterVec.x, masterVec.y, masterVec.z);
			return te.requestPower(cont, amount);
		}
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#addPower(com.projectzed.api.energy.storage.IEnergyContainer, int)
	 */
	@Override
	public int addPower(IEnergyContainer cont, int amount) {
		if (hasMaster) {
			AbstractTileEntityGenerator te = (AbstractTileEntityGenerator) worldObj.getTileEntity(masterVec.x, masterVec.y, masterVec.z);
			return te.addPower(cont, amount);
		}
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#importContents()
	 */
	@Override
	protected void importContents() {
		if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0 && this.hasMaster) {
			AbstractTileEntityGenerator te = (AbstractTileEntityGenerator) worldObj.getTileEntity(masterVec.x, masterVec.y, masterVec.z);
			
			if (te != null && this.storedPower != te.getEnergyStored()) {
				this.storedPower = te.getEnergyStored();
				PacketHandler.INSTANCE.sendToAll(new MessageTileEntityEnergyContainer(this));
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#exportContents()
	 */
	@Override
	protected void exportContents() {
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getDescriptionPacket()
	 */
	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityEnergyContainer(this));
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
	public Block getBlock() {
		return ProjectZed.nuclearPowerPort;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#isUnique()
	 */
	@Override
	public boolean isUnique() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#isSubstituable()
	 */
	@Override
	public boolean isSubstituable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#getSubList()
	 */
	@Override
	public List<IMultiBlockable> getSubList() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#getAmountFromSize(int, int, int)
	 */
	@Override
	public int getAmountFromSize(int width, int height, int depth) {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#isMaster()
	 */
	@Override
	public boolean isMaster() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#setIsMaster(boolean)
	 */
	@Override
	public void setIsMaster(boolean master) {
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
		this.setMaxStorage(((AbstractTileEntityGenerator) worldObj.getTileEntity(vec.x, vec.y, vec.z)).getMaxStorage());
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
	 * @see com.projectzed.api.tileentity.IMultiBlockable#reset()
	 */
	@Override
	public void reset() {
		this.hasMaster = false;
		this.masterVec = Vector3.zero.getVector3i();
		this.storedPower = 0;
		
		((BlockNuclearPowerPort) worldObj.getBlock(worldVec().x, worldVec().y, worldVec().z)).updateMeta(false, worldObj, worldVec());
	}

}

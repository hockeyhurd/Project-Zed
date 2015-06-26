/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity.container;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.energy.EnergyNet;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.IWrenchable;

/**
 * Class containing generic abstractions for all containers.
 * <br>NOTE: By container, this class assumes the te will be containing
 * energy, <strike>liquids, etc</strike>; explicitly not to be confused with a chest like container.
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public abstract class AbstractTileEntityEnergyContainer extends AbstractTileEntityContainer implements IEnergyContainer, IWrenchable {

	protected int maxPowerStorage = 100000;
	protected int storedPower;
	protected boolean powerMode;
	protected int importRate, exportRate;
	protected ForgeDirection lastReceivedDir = ForgeDirection.UNKNOWN;
	
	/**
	 * Init class object through parameters.
	 * @param name = name of te (its custom name).
	 */
	public AbstractTileEntityEnergyContainer(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	public abstract int getSizeInventory();

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 */
	public abstract int getInventoryStackLimit();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initContentsArray()
	 */
	protected abstract void initContentsArray();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initSlotsArray()
	 */
	protected abstract void initSlotsArray();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	public abstract boolean isItemValidForSlot(int slot, ItemStack stack);
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getAccessibleSlotsFromSide(int)
	 */

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getAccessibleSlotsFromSide(int)
	 */
	public abstract int[] getAccessibleSlotsFromSide(int side);

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	public abstract boolean canInsertItem(int slot, ItemStack stack, int side);

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public abstract boolean canExtractItem(int slot, ItemStack stack, int side);

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#setMaxStorage(int)
	 */
	public void setMaxStorage(int max) {
		this.maxPowerStorage = max;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#getMaxStorage()
	 */
	public int getMaxStorage() {
		return this.maxPowerStorage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#setEnergyStored(int)
	 */
	public void setEnergyStored(int amount) {
		this.storedPower = amount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#getEnergyStored()
	 */
	public int getEnergyStored() {
		return this.storedPower;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#getMaxImportRate()
	 */
	public abstract int getMaxImportRate();
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#getMaxTransferRate()
	 */
	public abstract int getMaxExportRate();
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#requestPower(com.projectzed.api.storage.IEnergyContainer, int)
	 */
	public abstract int requestPower(IEnergyContainer cont, int amount);
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#addPower(com.projectzed.api.energy.storage.IEnergyContainer, int)
	 */
	public abstract int addPower(IEnergyContainer cont, int amount);
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#setLastReceivedDirection(net.minecraftforge.common.util.ForgeDirection)
	 */
	@Override
	public void setLastReceivedDirection(ForgeDirection dir) {
		this.lastReceivedDir = dir;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#getLastReceivedDirection()
	 */
	@Override
	public ForgeDirection getLastReceivedDirection() {
		return this.lastReceivedDir;
	}
	
	/**
	* Method to be defined controlling mechanism for importing energy only (for now).
	*/ 
	protected abstract void importContents();
	
	/**
	 * Method to be defined controlling mechanism for exporting energy only (for now).
	 */
	protected abstract void exportContents(); 
	
	/**
	 * Method used to transfer power from one te to another.
	 */
	public void transferPower() {
		if (this.getWorldObj().isRemote) return;
		
		if (this.storedPower >= this.maxPowerStorage) {
			this.storedPower = this.maxPowerStorage;
			// return;
		}

		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;
		
		EnergyNet.importEnergyFromNeighbors(this, worldObj, x, y, z, lastReceivedDir);
		EnergyNet.tryClearDirectionalTraffic(this, worldObj, x, y, z, lastReceivedDir);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#worldVec()
	 */
	public Vector3<Integer> worldVec() {
		return new Vector3<Integer>(this.xCoord, this.yCoord, this.zCoord);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#updateEntity()
	 */
	public void updateEntity() {
		transferPower();
		importContents();
		exportContents();
		
		this.powerMode = this.storedPower > 0;
		this.markDirty();
		
		super.updateEntity();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound comp) { 
		super.readFromNBT(comp);
		this.powerMode = comp.getBoolean("ProjectZedPowerMode");
		int size = comp.getInteger("ProjectZedPowerStored");
		this.storedPower =  size >= 0 && size <= this.maxPowerStorage ? size : 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		comp.setBoolean("ProjectZedPowerMode", this.powerMode);
		comp.setInteger("ProjectZedPowerStored", this.storedPower);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getDescriptionPacket()
	 */
	@Override
	public abstract Packet getDescriptionPacket();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#getRotationMatrix()
	 */
	@Override
	public byte[] getRotationMatrix() {
		return new byte[] { 2, 5, 3, 4 };
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
	 * @see com.projectzed.api.tileentity.IWrenchable#onInteract(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
	 */
	@Override
	public void onInteract(ItemStack stack, EntityPlayer player, World world, Vector3<Integer> vec) {
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
		data.put("ProjectZedPowerStored", this.storedPower);

		// TODO: Implement saving of items!
		
		return data;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#stacksToSave()
	 */
	@Override
	public ItemStack[] stacksToSave() {
		return this.slots;
	}

}

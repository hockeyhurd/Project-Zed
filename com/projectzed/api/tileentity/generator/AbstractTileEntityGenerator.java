/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity.generator;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.api.energy.generation.IEnergyGeneration;
import com.projectzed.api.energy.source.Source;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityGenerator;
import com.projectzed.mod.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;

/**
 * Abstract class used for easyily adding a generic generator to mod.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
// TODO: AbstractTileEntityGenerator should extend AbstractTileEntityContainer
public abstract class AbstractTileEntityGenerator extends AbstractTileEntityGeneric implements IEnergyGeneration {

	protected int maxStored = 100000;
	protected int stored;
	protected Source source;
	protected boolean powerMode = false;

	/**
	 * Default constructor.
	 * @param name = name of container. <br>
	 *            Example: 'solarArray = container.solarArray'.
	 */
	public AbstractTileEntityGenerator(String name) {
		setCustomName("container." + name);
		defineSource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	@Override
	public abstract int getSizeInventory();

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 */
	@Override
	public abstract int getInventoryStackLimit();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initContentsArray()
	 */
	@Override
	protected abstract void initContentsArray();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initSlotsArray()
	 */
	@Override
	protected abstract void initSlotsArray();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#setCustomName(java.lang.String)
	 */
	@Override
	public void setCustomName(String name) {
		this.customName = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public abstract boolean isItemValidForSlot(int slot, ItemStack stack);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public abstract int[] getAccessibleSlotsFromSide(EnumFacing side);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public abstract boolean canInsertItem(int slot, ItemStack stack, EnumFacing side);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public abstract boolean canExtractItem(int slot, ItemStack stack, EnumFacing side);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#setMaxStorage(int)
	 */
	@Override
	public void setMaxStorage(int max) {
		this.maxStored = max;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#getMaxStorage()
	 */
	@Override
	public int getMaxStorage() {
		return this.maxStored;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#setEnergyStored(int)
	 */
	@Override
	public void setEnergyStored(int amount) {
		this.stored = amount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#getEnergyStored()
	 */
	@Override
	public int getEnergyStored() {
		return this.stored;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#getMaxImportRate()
	 */
	@Override
	public int getMaxImportRate() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#getMaxTransferRate()
	 */
	@Override
	public int getMaxExportRate() {
		return Reference.Constants.BASE_PIPE_TRANSFER_RATE * 8;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#requestPower(com.projectzed.api.storage.IEnergyContainer, int)
	 */
	@Override
	public int requestPower(IEnergyContainer cont, int amount) {
		if (cont != null && this.getMaxExportRate() >= amount) {
			if (this.stored - amount >= 0) this.stored -= amount;
			else {
				amount = this.stored;
				this.stored = 0;
			}
			return amount;
		}

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#addPower(com.projectzed.api.energy.storage.IEnergyContainer, int)
	 */
	@Override
	public int addPower(IEnergyContainer cont, int amount) {
		if (cont != null && this.getMaxImportRate() >= amount) {
			if (this.stored + amount <= this.maxStored) this.stored += amount;
			else {
				amount = this.maxStored - this.stored;
				this.stored = this.maxStored;
			}

			return amount;
		}

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#setLastReceivedDirection(net.minecraftforge.common.util.ForgeDirection)
	 */
	@Override
	public void setLastReceivedDirection(EnumFacing dir) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#getLastReceivedDirection()
	 */
	@Override
	public EnumFacing getLastReceivedDirection() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.generation.IEnergyGeneration#defineSource()
	 */
	@Override
	public abstract void defineSource();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.generation.IEnergyGeneration#getSource()
	 */
	@Override
	public Source getSource() {
		return this.source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.generation.IEnergyGeneration#generatePower()
	 */
	@Override
	public void generatePower() {
		if (worldObj.isRemote) return;
		if (canProducePower()) {
			if (this.stored + this.source.getEffectiveSize() <= this.maxStored) this.stored += this.source.getEffectiveSize();
			else {
				int rem = this.maxStored - this.stored;
				this.stored += rem;
			}
		}

		if (this.stored > this.maxStored) this.stored = this.maxStored; // Redundancy check.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.generation.IEnergyGeneration#transferPower()
	 */
	@Override
	public void transferPower() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#worldVec()
	 */
	@Override
	public Vector3<Integer> worldVec() {
		return VectorHelper.toVector3i(pos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.generation.IEnergyGeneration#canProducePower()
	 */
	@Override
	public boolean canProducePower() {
		return this.powerMode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.generation.IEnergyGeneration#setPowerMode(boolean)
	 */
	@Override
	public void setPowerMode(boolean state) {
		this.powerMode = state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#updateEntity()
	 */
	@Override
	public void update() {
		generatePower();
		transferPower();
		
		// If server side and every '1' second, send packet message to all clients.
		// if (!this.getWorldObj().isRemote && this.getWorldObj().getTotalWorldTime() % 20L == 0) PacketHandler.INSTANCE.sendToAll(new MessageTileEntityGenerator(this));
		this.markDirty();
		super.update();
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		this.powerMode = comp.getBoolean("ProjectZedPowerMode");
		int size = comp.getInteger("ProjectZedPowerStored");
		this.stored = size >= 0 && size <= this.maxStored ? size : 0;
	}

	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
		comp.setInteger("ProjectZedPowerStored", this.stored);
		comp.setBoolean("ProjectZedPowerMode", this.powerMode);
	}

	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityGenerator(this));
	}

}

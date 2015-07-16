/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyStorage;
import com.projectzed.api.energy.EnergyNet;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityRFBridge;
import com.projectzed.mod.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Class containing te code for RF Bridge.
 * 
 * @author hockeyhurd
 * @version Nov 29, 2014
 */
public class TileEntityRFBridge extends AbstractTileEntityEnergyContainer implements IEnergyStorage {

	private int maxStorageRF;
	public int storedRF;
	private int importRateRF, exportRateRF;
	private boolean flip = false;
	public static final int conversionCost = 0x19; // 25

	public TileEntityRFBridge() {
		super("bridgeRF");
		this.maxPowerStorage *= 2;
		this.importRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE * 4;
		// this.exportRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE / 2 * 4;
		this.exportRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE * 4;

		this.maxStorageRF = Reference.Constants.getRFFromMcU(this.maxPowerStorage);
		this.importRateRF = Reference.Constants.getRFFromMcU(this.exportRate);
		this.exportRateRF = Reference.Constants.getRFFromMcU(this.importRate);
	}

	/**
	 * Set whether to receive rf or mcu.
	 * @param flip = mode to set (receive == true ? McU --> RF : RF --> McU).
	 */
	public void setFlip(boolean flip) {
		this.flip = flip;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getSizeInventory()
	 */
	public int getSizeInventory() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getInventoryStackLimit()
	 */
	public int getInventoryStackLimit() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#initContentsArray()
	 */
	protected void initContentsArray() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#initSlotsArray()
	 */
	protected void initSlotsArray() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getAccessibleSlotsFromSide(int)
	 */
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getMaxImportRate()
	 */
	public int getMaxImportRate() {
		return this.importRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getMaxExportRate()
	 */
	public int getMaxExportRate() {
		return this.exportRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#requestPower(com.projectzed.api.storage.IEnergyContainer, int)
	 */
	public int requestPower(IEnergyContainer cont, int amount) {
		if (flip && cont != null && this.getMaxExportRate() >= amount) {
			if (this.storedPower - amount >= 0) this.storedPower -= amount;
			else {
				amount = this.storedPower;
				this.storedPower = 0;
			}
			return amount;
		}

		else return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#addPower(com.projectzed.api.energy.storage.IEnergyContainer, int)
	 */
	public int addPower(IEnergyContainer cont, int amount) {
		if (cont != null && this.getMaxImportRate() >= amount) {
			if (this.storedPower + amount <= this.maxPowerStorage) this.storedPower += amount;
			else {
				amount = this.maxPowerStorage - this.storedPower;
				this.storedPower = this.maxPowerStorage;
			}
			
			return amount;
		}
		
		else return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#importContents()
	 */
	protected void importContents() {
		if (this.getWorldObj().isRemote) return;

		// Helper variables.
		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;

		// *Converting to RF*
		if (!flip) {
			if (this.storedPower >= this.maxPowerStorage) {
				this.storedPower = this.maxPowerStorage;
				return;
			}
			
			EnergyNet.importEnergyFromNeighbors(this, worldObj, x, y, z, lastReceivedDir);
			EnergyNet.tryClearDirectionalTraffic(this, worldObj, x, y, z, lastReceivedDir);
		}

		// *Converting to McU*
		else {
			if (this.storedRF < this.maxStorageRF) {

				if (worldObj.getTileEntity(x - 1, y, z) instanceof IEnergyHandler) {
					IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x - 1, y, z);
					this.storedRF += hand.extractEnergy(ForgeDirection.EAST, this.importRateRF, false);
				}

				if (worldObj.getTileEntity(x + 1, y, z) instanceof IEnergyHandler) {
					IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x + 1, y, z);
					this.storedRF += hand.extractEnergy(ForgeDirection.WEST, this.importRateRF, false);
				}

				if (worldObj.getTileEntity(x, y - 1, z) instanceof IEnergyHandler) {
					IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x, y - 1, z);
					this.storedRF += hand.extractEnergy(ForgeDirection.UP, this.importRateRF, false);
				}

				if (worldObj.getTileEntity(x, y + 1, z) instanceof IEnergyHandler) {
					IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x, y + 1, z);
					this.storedRF += hand.extractEnergy(ForgeDirection.DOWN, this.importRateRF, false);
				}

				if (worldObj.getTileEntity(x, y, z - 1) instanceof IEnergyHandler) {
					IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x, y, z - 1);
					this.storedRF += hand.extractEnergy(ForgeDirection.SOUTH, this.importRateRF, false);
				}

				if (worldObj.getTileEntity(x, y, z + 1) instanceof IEnergyHandler) {
					IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x, y, z + 1);
					this.storedRF += hand.extractEnergy(ForgeDirection.NORTH, this.importRateRF, false);
				}
				
				if (this.storedRF > this.maxStorageRF) this.storedRF = this.maxStorageRF;
			}
		}
	}

	/**
	 * Method used to transfer energy from one unit to another.
	 */
	protected void convertEnergy() {
		if (this.getWorldObj().isRemote) return;

		// *Converting to RF*
		if (!flip) {
			if (this.storedPower > 0 && this.storedRF < this.maxStorageRF) {

				int differenceRF = Math.min(this.importRateRF, this.maxStorageRF - this.storedRF);
				int difference = Math.min(this.exportRate, Reference.Constants.getMcUFromRF(differenceRF));
				
				if (this.storedPower - difference >= 0 && this.storedRF + differenceRF <= this.maxStorageRF) {
					this.storedPower -= difference;
					this.storedRF += differenceRF;
				}
				
				else {
					difference = Math.min(difference, this.storedPower);
					differenceRF = Math.min(differenceRF, Reference.Constants.getRFFromMcU(difference));
					
					if (this.storedPower - difference >= 0 && this.storedRF + differenceRF <= this.maxStorageRF) {
						this.storedPower -= difference;
						this.storedRF += differenceRF;
					}
				}

				if (this.storedPower < 0) this.storedPower = 0;
				if (this.storedRF > this.maxStorageRF) this.storedRF = this.maxStorageRF;
			}
		}

		// *Converting to McU*
		else {
			if (this.storedRF > 0 && this.storedPower < this.maxPowerStorage) {
				
				int difference = Math.min(this.importRate, this.maxPowerStorage - this.storedPower);
				int differenceRF = Math.min(this.exportRateRF, Reference.Constants.getRFFromMcU(difference, conversionCost));
				
				if (this.storedRF - differenceRF >= 0 && this.storedPower + difference <= this.maxPowerStorage) {
					this.storedRF -= differenceRF;
					this.storedPower += difference;
				}
				
				else {
					differenceRF = Math.min(differenceRF, this.storedRF);
					difference = Math.min(difference, Reference.Constants.getMcUFromRF(differenceRF, conversionCost));
					
					if (this.storedRF - differenceRF >= 0 && this.storedPower + difference <= this.maxPowerStorage) {
						this.storedRF -= differenceRF;
						this.storedPower += difference;
					}
				}

				if (this.storedRF < 0) this.storedRF = 0;
				if (this.storedPower > this.maxPowerStorage) this.storedPower = this.maxPowerStorage;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#exportContents()
	 */
	protected void exportContents() {
		if (this.getWorldObj().isRemote) return;

		// Helper variables.
		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;

		// *Converting to RF*
		if (this.storedRF > 0 && !flip) {

			int amount = 0;
			if (worldObj.getTileEntity(x - 1, y, z) instanceof IEnergyHandler) {
				IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x - 1, y, z);
				if (!hand.canConnectEnergy(ForgeDirection.EAST)) return;
				
				amount = hand.receiveEnergy(ForgeDirection.EAST, this.exportRateRF, true);
				amount = Math.min(amount, this.extractEnergy(this.exportRateRF, true));
				if (amount == 0) return;
				
				hand.receiveEnergy(ForgeDirection.EAST, this.extractEnergy(amount, false), false);
			}

			if (worldObj.getTileEntity(x + 1, y, z) instanceof IEnergyHandler) {
				IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x + 1, y, z);
				if (!hand.canConnectEnergy(ForgeDirection.WEST)) return;
				
				amount = hand.receiveEnergy(ForgeDirection.WEST, this.exportRateRF, true);
				amount = Math.min(amount, this.extractEnergy(this.exportRateRF, true));
				if (amount == 0) return;
				
				hand.receiveEnergy(ForgeDirection.WEST, this.extractEnergy(amount, false), false);
			}

			if (worldObj.getTileEntity(x, y - 1, z) instanceof IEnergyHandler) {
				IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x, y - 1, z);
				if (!hand.canConnectEnergy(ForgeDirection.UP)) return;
				
				amount = hand.receiveEnergy(ForgeDirection.UP, this.exportRateRF, true);
				amount = Math.min(amount, this.extractEnergy(this.exportRateRF, true));
				if (amount == 0) return;
				
				hand.receiveEnergy(ForgeDirection.UP, this.extractEnergy(amount, false), false);
			}

			if (worldObj.getTileEntity(x, y + 1, z) instanceof IEnergyHandler) {
				IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x, y + 1, z);
				if (!hand.canConnectEnergy(ForgeDirection.DOWN)) return;
				
				amount = hand.receiveEnergy(ForgeDirection.DOWN, this.exportRateRF, true);
				amount = Math.min(amount, this.extractEnergy(this.exportRateRF, true));
				if (amount == 0) return;
				
				hand.receiveEnergy(ForgeDirection.DOWN, this.extractEnergy(amount, false), false);
			}

			if (worldObj.getTileEntity(x, y, z - 1) instanceof IEnergyHandler) {
				IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x, y, z - 1);
				if (!hand.canConnectEnergy(ForgeDirection.SOUTH)) return;
				
				amount = hand.receiveEnergy(ForgeDirection.SOUTH, this.exportRateRF, true);
				amount = Math.min(amount, this.extractEnergy(this.exportRateRF, true));
				if (amount == 0) return;
				
				hand.receiveEnergy(ForgeDirection.SOUTH, this.extractEnergy(amount, false), false);
			}

			if (worldObj.getTileEntity(x, y, z + 1) instanceof IEnergyHandler) {
				IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x, y, z + 1);
				if (!hand.canConnectEnergy(ForgeDirection.NORTH)) return;
				
				amount = hand.receiveEnergy(ForgeDirection.NORTH, this.exportRateRF, true);
				amount = Math.min(amount, this.extractEnergy(this.exportRateRF, true));
				if (amount == 0) return;
				
				hand.receiveEnergy(ForgeDirection.NORTH, this.extractEnergy(amount, false), false);
			}
		}

		else return;
	}

	// RF STUFF:
	
	/*
	 * (non-Javadoc)
	 * @see cofh.api.energy.IEnergyStorage#receiveEnergy(int, boolean)
	 */
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		/*
		 * int energyReceved = Math.min(this.maxStorageRF - this.storedRF, Math.min(this.importRateRF, maxReceive)); if (!simulate) this.storedRF +=
		 * energyReceved;
		 * 
		 * return energyReceved;
		 */

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see cofh.api.energy.IEnergyStorage#extractEnergy(int, boolean)
	 */
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int energyExtracted = Math.min(this.storedRF, Math.min(this.exportRateRF, maxExtract));
		if (!simulate) this.storedRF -= energyExtracted;

		return energyExtracted;
	}

	/*
	 * (non-Javadoc)
	 * @see cofh.api.energy.IEnergyStorage#getMaxEnergyStored()
	 */
	@Override
	public int getMaxEnergyStored() {
		return this.maxStorageRF;
	}

	/**
	 * Sets amount of rf stored.
	 * @param amount = amount to be set to.
	 */
	public void setRFStored(int amount) {
		this.storedRF = amount >= 0 && amount <= getMaxEnergyStored() ? amount : 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#updateEntity()
	 */
	@Override
	public void updateEntity() {
		importContents();
		convertEnergy();
		exportContents();
		
		if (!worldObj.isRemote) PacketHandler.INSTANCE.sendToAll(new MessageTileEntityRFBridge(this));

		super.updateEntity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		this.storedRF = comp.getInteger("ProjectZedRF");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		comp.setInteger("ProjectZedRF", this.storedRF);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getDescriptionPacket()
	 */
	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityRFBridge(this));
	}

}

package com.projectzed.mod.tileentity.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyStorage;

import com.projectzed.api.energy.EnergyNet;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityContainer;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityRFBridge;
import com.projectzed.mod.util.Reference;

/**
 * Class containing te code for RF Bridge.
 * 
 * @author hockeyhurd
 * @version Nov 29, 2014
 */
public class TileEntityRFBridge extends AbstractTileEntityContainer implements IEnergyStorage {

	private int maxStorageRF;
	public int storedRF;
	private int importRateRF, exportRateRF;
	private boolean flip = false;

	public TileEntityRFBridge() {
		super("bridgeRF");
		this.maxStorage /= 10;
		this.importRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE * 4;
		this.exportRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE / 2 * 4;
		// this.exportRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE * 4;

		this.maxStorageRF = Reference.Constants.getRFFromMcU(this.maxStorage);
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
		return null;
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
			if (this.stored - amount >= 0) this.stored -= amount;
			else {
				amount = this.stored;
				this.stored = 0;
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
			if (this.stored + amount <= this.maxStorage) this.stored += amount;
			else {
				amount = this.maxStorage - this.stored;
				this.stored = this.maxStorage;
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
			if (this.stored >= this.maxStorage) {
				this.stored = this.maxStorage;
				return;
			}
			
			EnergyNet.importEnergyFromNeighbors(this, worldObj, x, y, z, lastReceivedDir);
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
			if (this.stored > 0 && this.storedRF < this.maxStorageRF) {
				if (this.stored - this.exportRate > 0 && this.storedRF + this.importRateRF <= this.maxStorageRF) {
					this.stored -= this.exportRate;
					this.storedRF += this.importRateRF;
				}

				else {
					this.storedRF += Reference.Constants.getRFFromMcU(this.stored);
					this.stored = 0;
				}

				if (this.stored < 0) this.stored = 0;
				if (this.storedRF > this.maxStorageRF) this.storedRF = this.maxStorageRF;
			}
		}

		// *Converting to McU*
		else {
			if (this.storedRF > 0 && this.stored < this.maxStorage) {
				if (this.storedRF - this.exportRateRF > 0 && this.stored + this.importRate <= this.maxStorage) {
					this.storedRF -= this.exportRateRF;
					this.stored += this.importRate;
				}

				else {
					this.stored += Reference.Constants.getMcUFromRF(this.storedRF);
					this.storedRF = 0;
				}

				if (this.storedRF < 0) this.storedRF = 0;
				if (this.stored > this.maxStorage) this.stored = this.maxStorage;
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

			if (worldObj.getTileEntity(x - 1, y, z) instanceof IEnergyHandler) {
				IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x - 1, y, z);
				hand.receiveEnergy(ForgeDirection.EAST, this.extractEnergy(this.exportRateRF, false), false);
			}

			if (worldObj.getTileEntity(x + 1, y, z) instanceof IEnergyHandler) {
				IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x + 1, y, z);
				hand.receiveEnergy(ForgeDirection.WEST, this.extractEnergy(this.exportRateRF, false), false);
			}

			if (worldObj.getTileEntity(x, y - 1, z) instanceof IEnergyHandler) {
				IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x, y - 1, z);
				hand.receiveEnergy(ForgeDirection.UP, this.extractEnergy(this.exportRateRF, false), false);
			}

			if (worldObj.getTileEntity(x, y + 1, z) instanceof IEnergyHandler) {
				IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x, y + 1, z);
				hand.receiveEnergy(ForgeDirection.DOWN, this.extractEnergy(this.exportRateRF, false), false);
			}

			if (worldObj.getTileEntity(x, y, z - 1) instanceof IEnergyHandler) {
				IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x, y, z - 1);
				hand.receiveEnergy(ForgeDirection.SOUTH, this.extractEnergy(this.exportRateRF, false), false);
			}

			if (worldObj.getTileEntity(x, y, z + 1) instanceof IEnergyHandler) {
				IEnergyHandler hand = (IEnergyHandler) worldObj.getTileEntity(x, y, z + 1);
				hand.receiveEnergy(ForgeDirection.NORTH, this.extractEnergy(this.exportRateRF, false), false);
			}
		}

		else return;
	}

	// RF STUFF:
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

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int energyExtracted = Math.min(this.storedRF, Math.min(this.exportRateRF, maxExtract));
		if (!simulate) this.storedRF -= energyExtracted;

		return energyExtracted;
	}

	@Override
	public int getMaxEnergyStored() {
		return this.maxStorageRF;
	}

	public void setRFStored(int amount) {
		this.storedRF = amount;
	}

	public void updateEntity() {
		importContents();
		convertEnergy();
		exportContents();
		PacketHandler.INSTANCE.sendToAll(new MessageTileEntityRFBridge(this));

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

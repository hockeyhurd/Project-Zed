/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyStorage;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.api.energy.EnergyNet;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityRFBridge;
import com.projectzed.mod.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Class containing te code for RF Bridge.
 * 
 * @author hockeyhurd
 * @version Nov 29, 2014
 */
public class  TileEntityRFBridge extends AbstractTileEntityEnergyContainer implements IEnergyStorage {

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
	 *
	 * @param flip mode to set (receive == true ? McU --> RF : RF --> McU).
	 */
	public void setFlip(boolean flip) {
		this.flip = flip;
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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
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
	public int getMaxImportRate() {
		return this.importRate;
	}

	@Override
	public int getMaxExportRate() {
		return this.exportRate;
	}

	@Override
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
	
	@Override
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

	@Override
	protected void importContents() {
		if (worldObj.isRemote) return;

		// *Converting to RF*
		if (!flip) {
			if (this.storedPower >= this.maxPowerStorage) {
				this.storedPower = this.maxPowerStorage;
				return;
			}

			EnergyNet.importEnergyFromNeighbors(this, worldObj, pos.getX(), pos.getY(), pos.getZ(), lastReceivedDir);
			EnergyNet.tryClearDirectionalTraffic(this, worldObj, pos.getX(), pos.getY(), pos.getZ(), lastReceivedDir);
		}

		// *Converting to McU*
		else {
			for (EnumFacing dir : EnumFacing.VALUES) {
				final BlockPos blockPos = VectorHelper
						.toBlockPos(pos.getX() + dir.getFrontOffsetX(), pos.getY() + dir.getFrontOffsetY(), pos.getZ() + dir.getFrontOffsetZ());
				final TileEntity tileEntity = worldObj.getTileEntity(blockPos);

				if (tileEntity instanceof IEnergyHandler)
					this.storedRF += ((IEnergyHandler) tileEntity).extractEnergy(dir.getOpposite(), this.importRateRF, false);
			}
		}
	}

	/**
	 * Method used to transfer energy from one unit to another.
	 */
	protected void convertEnergy() {
		if (worldObj.isRemote) return;

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

	@Override
	protected void exportContents() {
		if (worldObj.isRemote) return;

		// Helper variables.
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		// *Converting to RF*
		if (this.storedRF > 0 && !flip) {

			int amount = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				final BlockPos blockPos = VectorHelper
						.toBlockPos(pos.getX() + dir.getFrontOffsetX(), pos.getY() + dir.getFrontOffsetY(), pos.getZ() + dir.getFrontOffsetZ());
				final TileEntity tileEntity = worldObj.getTileEntity(blockPos);

				if (tileEntity instanceof IEnergyHandler) {
					IEnergyHandler handler = (IEnergyHandler) tileEntity;
					if (!handler.canConnectEnergy(dir.getOpposite())) return;

					amount = handler.receiveEnergy(dir.getOpposite(), this.exportRateRF, true);
					amount = Math.min(amount, this.extractEnergy(this.exportRateRF, true));
					if (amount == 0) return;

					handler.receiveEnergy(dir.getOpposite(), this.extractEnergy(amount, false), false);
				}
			}
		}

		// else return;
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

	/**
	 * Sets amount of rf stored.
	 *
	 * @param amount amount to be set to.
	 */
	public void setRFStored(int amount) {
		this.storedRF = amount >= 0 && amount <= getMaxEnergyStored() ? amount : 0;
	}

	@Override
	public void update() {
		importContents();
		convertEnergy();
		exportContents();
		
		if (!worldObj.isRemote) PacketHandler.INSTANCE.sendToAll(new MessageTileEntityRFBridge(this));

		super.update();
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		this.storedRF = comp.getInteger("ProjectZedRF");
	}

	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
		comp.setInteger("ProjectZedRF", this.storedRF);
	}

	/*@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityRFBridge(this));
	}*/

	@Override
	public NBTTagCompound getUpdateTag() {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityRFBridge(this));

		final NBTTagCompound comp = getTileData();
		saveNBT(comp);

		return comp;
	}

}

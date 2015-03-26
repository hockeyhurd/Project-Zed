/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import com.projectzed.api.energy.EnergyNet;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.api.util.EnumFrameType;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityEnergyContainer;
import com.projectzed.mod.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;

/**
 * Class containing code for energy bank. <br>
 * NOTE: This class should be overriden for various energy tiers.
 * 
 * @author hockeyhurd
 * @version Dec 3, 2014
 */
public class TileEntityEnergyBankBase extends AbstractTileEntityEnergyContainer implements IModularFrame {

	protected byte tier; 
	protected int[] tiers = new int[] {
		(int) 1e6, (int) 1e7, (int) 1e8, (int) 1e9, 	
	};
	
	protected byte[] openSides = new byte[ForgeDirection.VALID_DIRECTIONS.length]; 
	
	public TileEntityEnergyBankBase() {
		super("energyBank");
		this.tier = 0;
		this.maxPowerStorage = this.tiers[0];
		this.importRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE * 8;
		this.exportRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE * 8;
	}
	
	/**
	 * Allows setting tier of energy bank.
	 * @param b = tier number (should be 0, 1, 2, 3).
	 */
	public void setTier(byte b) {
		this.tier = b >= 0 && b <= tiers.length ? b : 0;
		this.maxPowerStorage = tiers[b];
	}
	
	/**
	 * @return tier of energy cell.
	 */
	public byte getTier() {
		return this.tier;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IModularFrame#getType()
	 */
	public EnumFrameType getType() {
		return EnumFrameType.POWER;
	}
	
	/**
	 * Sets given direction to given value.
	 * @param dir = direction to set.
	 * @param value = value to set (-1 = import, 0 = neutral or nothing allowed, 1 = export).
	 */
	public void setSideValve(ForgeDirection dir, byte value) {
		openSides[dir.ordinal()] = value;
	}
	
	/**
	 * Sets the side value after rotating to next value.
	 * @param dir = direction to test.
	 */
	public void setSideValveAndRotate(ForgeDirection dir) {
		openSides[dir.ordinal()] = (byte) (openSides[dir.ordinal()] == -1 ? 0 : (openSides[dir.ordinal()] == 0 ? 1 : -1));
	}
	
	/**
	 * @param dir = direction to test.
	 * @return -1 if can input, 0 neutral/nothing, or 1 to export.
	 */
	public byte getSideValve(ForgeDirection dir) {
		return openSides[dir.ordinal()];
	}
	
	/**
	 * @param dir = direction to test.
	 * @return -1 if can input, 0 neutral/nothing, or 1 to export.
	 */
	public byte getSideValve(int dir) {
		return openSides[dir];
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IModularFrame#getSidedArray()
	 */
	public byte[] getSidedArray() {
		return openSides;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getMaxImportRate()
	 */
	@Override
	public int getMaxImportRate() {
		return this.importRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getMaxExportRate()
	 */
	@Override
	public int getMaxExportRate() {
		return this.exportRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#requestPower(com.projectzed.api.energy.storage.IEnergyContainer, int)
	 */
	@Override
	public int requestPower(IEnergyContainer cont, int amount) {
		if (cont != null && this.getMaxExportRate() >= amount) {
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
	 * 
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
	@Override
	protected void importContents() {
		if (this.getWorldObj().isRemote) return;

		if (this.storedPower >= this.maxPowerStorage) {
			this.storedPower = this.maxPowerStorage;
			// return; // Should be safe to comment this out.
		}

		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;

		EnergyNet.importEnergyFromNeighbors(this, worldObj, x, y, z, lastReceivedDir);
		EnergyNet.tryClearDirectionalTraffic(this, worldObj, x, y, z, lastReceivedDir);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#exportContents()
	 */
	@Override
	@Deprecated
	protected void exportContents() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#updateEntity()
	 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if (!this.worldObj.isRemote) {
			if (this.maxPowerStorage != this.tiers[this.tier]) this.maxPowerStorage = this.tiers[this.tier]; 
			
			if (!this.powerMode) this.powerMode = true;
			if (this.lastReceivedDir != ForgeDirection.UNKNOWN) this.lastReceivedDir = ForgeDirection.UNKNOWN;
			
			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityEnergyContainer(this));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getDescriptionPacket()
	 */
	// NOTE: server -> client
	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityEnergyContainer(this));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#onDataPacket(net.minecraft.network.NetworkManager, net.minecraft.network.play.server.S35PacketUpdateTileEntity)
	 */
	// NOTE: client -> server
	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityEnergyContainer(this));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound comp) {
		
		// Make sure the tier from nbt is acceptable.
		byte tier = comp.getByte("ProjectZedEnergyBankTier");
		this.tier = tier >= 0 && tier < this.tiers.length ? tier : 0;
		if (this.maxPowerStorage != this.tiers[this.tier]) this.maxPowerStorage = this.tiers[this.tier];
		
		for (int i = 0; i < this.openSides.length; i++) {
			this.openSides[i] = comp.getByte("ProjectZedEnergyBankSide" + i);
		}
		
		super.readFromNBT(comp);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		comp.setByte("ProjectZedEnergyBankTier", this.tier);
		
		for (int i = 0; i < this.openSides.length; i++) {
			comp.setByte("ProjectZedEnergyBankSide" + i, this.openSides[i]);
		}
		
		super.writeToNBT(comp);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#dataToSave()
	 */
	@Override
	public HashMap<String, Number> dataToSave() {
		HashMap<String, Number> data = super.dataToSave();

		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			data.put(dir.name(), getSideValve(dir));
		}

		return data;
	}

}

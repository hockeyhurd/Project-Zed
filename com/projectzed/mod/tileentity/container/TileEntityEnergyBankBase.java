package com.projectzed.mod.tileentity.container;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.projectzed.api.energy.EnergyNet;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityContainer;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityContainer;
import com.projectzed.mod.util.Reference;

/**
 * Class containing code for energy bank. <br>
 * NOTE: This class should be overriden for various energy tiers.
 * 
 * @author hockeyhurd
 * @version Dec 3, 2014
 */
public class TileEntityEnergyBankBase extends AbstractTileEntityContainer {

	protected byte tier; 
	protected int[] tiers = new int[] {
		(int) 1e6, (int) 1e7, (int) 1e8, (int) 1e9, 	
	};
	
	protected byte[] openSides = new byte[ForgeDirection.VALID_DIRECTIONS.length]; 
	
	public TileEntityEnergyBankBase() {
		super("energyBank");
		this.tier = 0;
		this.maxStorage = this.tiers[0];
		this.importRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE * 4;
		this.exportRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE * 4;
	}
	
	/**
	 * Allows setting tier of energy bank.
	 * @param b = tier number (should be 0, 1, 2, 3).
	 */
	public void setTier(byte b) {
		this.tier = b >= 0 && b <= tiers.length ? b : 0;
		this.maxStorage = tiers[b];
	}
	
	/**
	 * @return tier of energy cell.
	 */
	public byte getTier() {
		return this.tier;
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
	 * 
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
	@Override
	protected void importContents() {
		if (this.getWorldObj().isRemote) return;

		if (this.stored >= this.maxStorage) {
			this.stored = this.maxStorage;
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
			if (this.maxStorage != this.tiers[this.tier]) this.maxStorage = this.tiers[this.tier]; 
			
			if (!this.powerMode) this.powerMode = true;
			if (this.lastReceivedDir != ForgeDirection.UNKNOWN) this.lastReceivedDir = ForgeDirection.UNKNOWN;
			
			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityContainer(this));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getDescriptionPacket()
	 */
	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityContainer(this));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#onDataPacket(net.minecraft.network.NetworkManager, net.minecraft.network.play.server.S35PacketUpdateTileEntity)
	 */
	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityContainer(this));
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
		if (this.maxStorage != this.tiers[this.tier]) this.maxStorage = this.tiers[this.tier];
		
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

}

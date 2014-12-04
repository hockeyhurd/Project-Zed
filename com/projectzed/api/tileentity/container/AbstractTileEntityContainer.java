package com.projectzed.api.tileentity.container;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageHandlerTileEntityContainer;

/**
 * Class containing generic abstractions for all containers.
 * <br>NOTE: By container, this class assumes the te will be containing
 * energy, <strike>liquids, etc</strike>; explicitly not to be confused with a chest like container.
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public abstract class AbstractTileEntityContainer extends AbstractTileEntityGeneric implements IEnergyContainer {

	protected int maxStorage = 100000;
	protected int stored;
	protected boolean powerMode;
	protected int importRate, exportRate;
	protected ForgeDirection lastReceivedDir = ForgeDirection.UNKNOWN;
	
	/**
	 * Init class object through parameters.
	 * @param name = name of te (its custom name).
	 */
	public AbstractTileEntityContainer(String name) {
		super();
		setCustomName("container." + name);
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
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#setCustomName(java.lang.String)
	 */
	public void setCustomName(String name) {
		this.customName = name;
	}

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
		this.maxStorage = max;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#getMaxStorage()
	 */
	public int getMaxStorage() {
		return this.maxStorage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#setEnergyStored(int)
	 */
	public void setEnergyStored(int amount) {
		this.stored = amount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#getEnergyStored()
	 */
	public int getEnergyStored() {
		return this.stored;
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
	public void setLastReceivedDirection(ForgeDirection dir) {
		this.lastReceivedDir = dir;
	}
	
	/**
	* Method to be defined controlling mechanism for importing energy only (for now).
	*/ 
	protected abstract void importContents();
	
	/**
	 * Method to be defined controlling mechanism for exporting energy only (for now).
	 * <br>Set to deprecated since 12/3/14 as mostly unuse and maybe removed later.
	 */
	@Deprecated
	protected abstract void exportContents(); 
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#worldVec()
	 */
	public Vector4Helper<Integer> worldVec() {
		return new Vector4Helper<Integer>(this.xCoord, this.yCoord, this.zCoord);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#updateEntity()
	 */
	public void updateEntity() {
		importContents();
		exportContents();
		
		this.powerMode = this.stored > 0;
		
		super.updateEntity();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void readFromNBT(NBTTagCompound comp) { 
		super.readFromNBT(comp);
		this.powerMode = comp.getBoolean("ProjectZedPowerMode");
		int size = comp.getInteger("ProjectZedPowerStored");
		this.stored =  size >= 0 && size <= this.maxStorage ? size : 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		comp.setInteger("ProjectZedPowerStored", this.stored);
		comp.setBoolean("ProjectZedPowerMode", this.powerMode);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getDescriptionPacket()
	 */
	@Override
	public abstract Packet getDescriptionPacket();

}

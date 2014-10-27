package com.projectzed.api.tileentity.generator;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;

import com.projectzed.api.generation.IEnergyGeneration;
import com.projectzed.api.source.Source;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityGenerator;

/**
 * Abstract class used for easyily adding a generic generator to mod. 
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public abstract class AbstractTileEntityGenerator extends AbstractTileEntityGeneric implements IEnergyGeneration {

	protected int maxStored = 100000;
	protected int stored;
	protected Source source;
	protected boolean powerMode = false;
	
	/**
	 * Default constructor.
	 * @param name = name of container.
	 * <br>Example: 'solarArray = container.solarArray'.
	 */
	public AbstractTileEntityGenerator(String name) {
		super();
		setCustomName("container." + name);
		defineSource();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	public abstract int getSizeInventory();

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 */
	public abstract int getInventoryStackLimit();

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initContentsArray()
	 */
	protected abstract void initContentsArray();

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initSlotsArray()
	 */
	protected abstract void initSlotsArray();

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#setCustomName(java.lang.String)
	 */
	public void setCustomName(String name) {
		this.customName = name;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	public abstract boolean isItemValidForSlot(int slot, ItemStack stack);

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getAccessibleSlotsFromSide(int)
	 */
	public abstract int[] getAccessibleSlotsFromSide(int side);

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	public abstract boolean canInsertItem(int slot, ItemStack stack, int side);

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public abstract boolean canExtractItem(int slot, ItemStack stack, int side);

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#setMaxStorage(int)
	 */
	public void setMaxStorage(int max) {
		this.maxStored = max;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#getMaxStorage()
	 */
	public int getMaxStorage() {
		return this.maxStored;
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
	 * @see com.projectzed.api.generation.IEnergyGeneration#defineSource()
	 */
	public abstract void defineSource();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.generation.IEnergyGeneration#getSource()
	 */
	public Source getSource() {
		return this.source;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.generation.IEnergyGeneration#generatePower()
	 */
	public void generatePower() {
		if (canProducePower() && this.stored < this.maxStored) this.stored += this.source.getEffectiveSize();
		if (this.stored > this.maxStored) this.stored = this.maxStored;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.generation.IEnergyGeneration#transferPower()
	 */ 
	public void transferPower() {
		
		for (int y = this.yCoord - 1; y <= this.yCoord + 1; y++) {
			for (int x = this.xCoord - 1; x <= this.xCoord + 1; x++) {
				for (int z = this.zCoord - 1; z <= this.zCoord + 1; z++) {
					
					if (worldObj.getTileEntity(x, y, z) != null && worldObj.getTileEntity(x, y, z) instanceof AbstractTileEntityMachine) {
						AbstractTileEntityMachine te = (AbstractTileEntityMachine) worldObj.getTileEntity(x, y, z);
						if (te.getEnergyStored() + 5 <= te.getMaxStorage() && this.stored - 1 >= 0) {
							this.stored -= 5; 
							break;
						}
					}
				}
			}
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.generation.IEnergyGeneration#canProducePower()
	 */
	public boolean canProducePower() {
		return this.powerMode;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.generation.IEnergyGeneration#setPowerMode(boolean)
	 */
	public void setPowerMode(boolean state) {
		this.powerMode = state;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#updateEntity()
	 */
	public void updateEntity() {
		generatePower();
		transferPower();
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
		this.stored =  size >= 0 && size <= this.maxStored ? size : 0;
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
	
	@Override
	public Packet getDescriptionPacket() { 
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityGenerator(this));
	}

}

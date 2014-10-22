package com.projectzed.api.tileentity.generator;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.projectzed.api.generation.IEnergyGeneration;
import com.projectzed.api.source.Source;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.mod.ProjectZed;

/**
 * Abstract class used for easyily adding a generic generator to mod. 
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public abstract class AbstractTileEntityGenerator extends AbstractTileEntityGeneric implements IEnergyGeneration {

	protected int maxStored = 10000;
	protected int stored;
	protected Source source;
	
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
		if (this.stored + this.source.getEffectiveSize() <= this.maxStored) this.stored += this.source.getEffectiveSize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#updateEntity()
	 */
	public void updateEntity() {
		generatePower();
		super.updateEntity();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void readFromNBT(NBTTagCompound comp) { 
		super.readFromNBT(comp);
		int size = comp.getInteger("ZedPower");
		this.stored =  size >= 0 && size <= this.maxStored ? size : 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		comp.setInteger("ZedPower", this.stored);
	}

}

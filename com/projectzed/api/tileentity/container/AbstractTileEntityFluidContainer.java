package com.projectzed.api.tileentity.container;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.api.fluid.storage.IFluidContainer;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;

/**
 * Class containing generalized abstraction code for any te container
 * that uses fluid. 
 * 
 * @author hockeyhurd
 * @version Jan 9, 2015
 */
public abstract class AbstractTileEntityFluidContainer extends AbstractTileEntityGeneric implements IFluidContainer {

	protected int maxFluidStorage = 10000;
	protected int storedFluid;
	protected boolean isEmpty;
	protected int importRate, exportRate;
	protected Fluid fluidType;
	protected ForgeDirection lastReceivedDir = ForgeDirection.UNKNOWN;
	
	/**
	 * @param name = name of te (its custom name).
	 */
	public AbstractTileEntityFluidContainer(String name) {
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

	@Override
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
	 * @see com.projectzed.api.fluid.storage.IFluidContainer#setMaxStorage(int)
	 */
	@Override
	public void setMaxStorage(int max) {
		this.maxFluidStorage = max;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.storage.IFluidContainer#getMaxStorage()
	 */
	@Override
	public int getMaxStorage() {
		return this.maxFluidStorage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.storage.IFluidContainer#setFluidStored(int)
	 */
	@Override
	public void setFluidStored(int amount) {
		this.storedFluid = amount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.storage.IFluidContainer#getFluidStored()
	 */
	@Override
	public int getFluidStored() {
		return this.storedFluid;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.storage.IFluidContainer#getFluidType()
	 */
	public abstract Fluid getFluidType();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.storage.IFluidContainer#getMaxImportRate()
	 */
	public abstract int getMaxImportRate();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.storage.IFluidContainer#getMaxExportRate()
	 */
	public abstract int getMaxExportRate();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.storage.IFluidContainer#requestFluid(com.projectzed.api.fluid.storage.IFluidContainer, int)
	 */
	public abstract int requestFluid(IFluidContainer cont, Fluid fluid, int amount);

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.storage.IFluidContainer#addFluid(com.projectzed.api.fluid.storage.IFluidContainer, net.minecraftforge.fluids.Fluid, int)
	 */
	public abstract int addFluid(IFluidContainer cont, Fluid fluid, int amount);

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.storage.IFluidContainer#setLastReceivedDirection(net.minecraftforge.common.util.ForgeDirection)
	 */
	@Override
	public void setLastReceivedDirection(ForgeDirection dir) {
		this.lastReceivedDir = dir;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.storage.IFluidContainer#getLastReceivedDirection()
	 */
	@Override
	public ForgeDirection getLastReceivedDirection() {
		return this.lastReceivedDir;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.storage.IFluidContainer#worldVec()
	 */
	@Override
	public Vector4Helper<Integer> worldVec() {
		return new Vector4Helper<Integer>(this.xCoord, this.yCoord, this.zCoord);
	}
	
	/**
	 * Method to be defined controlling mechanisum for importing fluid only (for now).
	 */
	protected abstract void importContents();
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#updateEntity()
	 */
	@Override
	public void updateEntity() {
		importContents();
		
		this.isEmpty = this.storedFluid == 0;
		super.updateEntity();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		this.isEmpty = comp.getBoolean("ProjectZedFluidMode");
		
		int size = comp.getInteger("ProjectZedFluidStored");
		this.storedFluid = size >= 0 && size <= this.maxFluidStorage ? size : 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		comp.setBoolean("ProjectZedFluidMode", this.isEmpty);
		comp.setInteger("ProjectZedFluidStored", this.storedFluid);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getDescriptionPacket()
	 */
	@Override
	public abstract Packet getDescriptionPacket();

}

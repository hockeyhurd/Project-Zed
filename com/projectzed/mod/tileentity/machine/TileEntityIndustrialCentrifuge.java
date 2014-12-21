package com.projectzed.mod.tileentity.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.registry.MetalPressRecipesRegistry;

/**
 * Class containing code for industrialCentrifuge.
 * 
 * @author hockeyhurd
 * @version Dec 21, 2014
 */
public class TileEntityIndustrialCentrifuge extends AbstractTileEntityMachine {

	private final int MAX_WATER_STORAGE = 10000;
	private int waterStored;
	
	public TileEntityIndustrialCentrifuge() {
		super("industrialCentrifuge");
		this.slots = new ItemStack[3];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return 3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
		this.slots = new ItemStack[3];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
		this.slotTop = new int[] {
			0
		};
		this.slotRight = new int[] {
			1
		};

		this.slotBottom = new int[] {
			2
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 1 || slot == 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? this.slotRight : this.slotTop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return this.isItemValidForSlot(slot, stack);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return slot == 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canSmelt()
	 */
	@Override
	protected boolean canSmelt() {
		if (this.slots[0] == null || this.stored - this.energyBurnRate <= 0) return false;
		else {
			// Check if the item in the slot 1 can be smelted (has a set furnace recipe).
			ItemStack stack = MetalPressRecipesRegistry.pressList(this.slots[0]);
			if (stack == null) return false;
			if (this.slots[1] == null) return true;
			if (!this.slots[1].isItemEqual(stack)) return false;

			// Add the result of the furnace recipe to the current stack size (already smelted so far).
			int result = this.slots[1].stackSize + stack.stackSize;

			// Make sure we aren't going over the set stack limit's size.
			return (result <= getInventoryStackLimit() && result <= stack.getMaxStackSize());
		}
	}
	
	/**
	 * Function to get the amount of water in tank.
	 * @return amount of water in mb.
	 */
	public int getWaterInTank() {
		return this.waterStored;
	}
	
	/**
	 * Function to get the max amount of water the tank can hold.
	 * @return max amount of water in mb.
	 */
	public int getMaxWaterStorage() {
		return this.MAX_WATER_STORAGE;
	}
	
	/**
	 * Function to get whether there is water in tank.
	 * @return true if has water, else returns false.
	 */
	public boolean hasWaterInTank() {
		return this.waterStored > 0;
	}
	
	/**
	 * Sets the amount of water the tank should now contain.
	 * @param amount = amount to try and set.
	 */
	public void setWaterInTank(int amount) {
		this.waterStored = amount >= 0 && amount <= this.MAX_WATER_STORAGE ? amount : 0;
	}
	
	/**
	 * Adds default amount of water to tank (1000 mb).
	 */
	public void addWaterToTank() {
		addWaterToTank(1000);
	}
	
	/**
	 * Adds given amount of water to tank.
	 * @param amount = amount of water to add in mb.
	 */
	public void addWaterToTank(int amount) {
		this.waterStored += canAddWaterToTank(1000) ? amount : 0; 
	}
	
	/**
	 * @return true if can add 1000 mb, else returns false.
	 */
	public boolean canAddWaterToTank() {
		return canAddWaterToTank(1000);
	}
	
	/**
	 * @param amount = amount to try and add.
	 * @return result of trying to add said amount.
	 */
	public boolean canAddWaterToTank(int amount) {
		return amount > 0 && this.waterStored + amount <= this.MAX_WATER_STORAGE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#smeltItem()
	 */
	@Override
	public void smeltItem() {
		if (this.canSmelt() && hasWaterInTank()) {
			ItemStack itemstack = MetalPressRecipesRegistry.pressList(this.slots[0]);

			if (this.slots[1] == null) this.slots[1] = itemstack.copy();
			else if (this.slots[1].isItemEqual(itemstack)) slots[1].stackSize += itemstack.stackSize;

			this.slots[0].stackSize--;
			if (this.slots[0].stackSize <= 0) this.slots[0] = null;
			this.waterStored -= 1000;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound comp) {
		int amount = comp.getInteger("ProjectZedWaterTank");
		this.waterStored = amount >= 0 && amount <= this.MAX_WATER_STORAGE ? amount : 0;
		
		super.readFromNBT(comp);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#writeToNBT(net.minecraft.nbt.NBTTagCompound) 
	 */
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		comp.setInteger("ProjectZedWaterTank", this.waterStored);
		
		super.writeToNBT(comp);
	}
	
}

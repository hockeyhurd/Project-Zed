package com.projectzed.mod.tileentity.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;

/**
 * 
 * @author hockeyhurd
 * @version Oct 22, 2014
 */
public class TileEntityIndustrialFurnace extends AbstractTileEntityMachine {

	public TileEntityIndustrialFurnace() {
		super("industrialFurnace");
		this.slots = new ItemStack[2];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getSizeInventory()
	 */
	public int getSizeInventory() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getInventoryStackLimit()
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#initContentsArray()
	 */
	protected void initContentsArray() {
		this.slots = new ItemStack[2];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#initSlotsArray()
	 */
	protected void initSlotsArray() {
		this.slotTop = new int[] {
			0
		};
		this.slotRight = new int[] {
			1
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 1 ? false : true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getAccessibleSlotsFromSide(int)
	 */
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? this.slotRight : this.slotTop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return this.isItemValidForSlot(slot, stack);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		// return side != 1 /*|| side != 1*/ || stack.getItem() == Items.bucket;
		return slot == 0 || slot == 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canSmelt()
	 */
	protected boolean canSmelt() {
		if (this.slots[0] == null || this.stored - this.energyBurnRate <= 0) return false;
		else {
			// Check if the item in the slot 1 can be smelted (has a set furnace recipe).
			ItemStack stack = FurnaceRecipes.smelting().getSmeltingResult(this.slots[0]);
			if (stack == null) return false;
			if (this.slots[1] == null) return true;
			if (!this.slots[1].isItemEqual(stack)) return false;

			// Add the result of the furnace recipe to the current stack size (already smelted so far).
			int result = this.slots[1].stackSize + stack.stackSize;

			// Make sure we aren't going over the set stack limit's size.
			return (result <= getInventoryStackLimit() && result <= stack.getMaxStackSize());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#smeltItem()
	 */
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.slots[0]);

			if (this.slots[1] == null) this.slots[1] = itemstack.copy();
			else if (this.slots[1].isItemEqual(itemstack)) slots[1].stackSize += itemstack.stackSize;

			this.slots[0].stackSize--;

			if (this.slots[0].stackSize <= 0) {
				this.slots[0] = null;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getSound()
	 */
	public Sound getSound() {
		return null;
	}

}

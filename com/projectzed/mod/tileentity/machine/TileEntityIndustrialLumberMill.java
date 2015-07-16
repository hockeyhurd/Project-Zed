/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.machine;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;
import com.projectzed.mod.registry.LumberMillRecipesRegistry;
import net.minecraft.item.ItemStack;

/**
 * Class containing te code for industrialLumberMill.
 * 
 * @author hockeyhurd
 * @version Nov 17, 2014
 */
public class TileEntityIndustrialLumberMill extends AbstractTileEntityMachine {

	public TileEntityIndustrialLumberMill() {
		super("industrialLumberMill");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getSizeInventory()
	 */
	public int getSizeInventory() {
		return slots.length;
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
		this.slots = new ItemStack[2 + getSizeUpgradeSlots()];
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
		return slot != 1 && super.isItemValidForSlot(slot, stack);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getAccessibleSlotsFromSide(int)
	 */
	public int[] getAccessibleSlotsFromSide(int side) {
		return openSides[side] == 1 ? this.slotRight : openSides[side] == -1 ? this.slotTop : new int[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return super.canExtractItem(slot, stack, side) && slot == 1;
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
			ItemStack stack = LumberMillRecipesRegistry.millingList(this.slots[0]);
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
			ItemStack itemstack = LumberMillRecipesRegistry.millingList(this.slots[0]);

			if (this.slots[1] == null) this.slots[1] = itemstack.copy();
			else if (this.slots[1].isItemEqual(itemstack)) slots[1].stackSize += itemstack.stackSize;

			this.slots[0].stackSize--;

			if (this.slots[0].stackSize <= 0) this.slots[0] = null;
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

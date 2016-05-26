/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.machine;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;
import com.projectzed.mod.registry.CrusherRecipesRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

/**
 * Class containing code for te data for industrial crusher.
 * 
 * @author hockeyhurd
 * @version Nov 4, 2014
 */
public class TileEntityIndustrialCrusher extends AbstractTileEntityMachine {

	public TileEntityIndustrialCrusher() {
		super("industrialCrusher");
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	protected void initContentsArray() {
		this.slots = new ItemStack[2 + getSizeUpgradeSlots()];
	}

	@Override
	protected void initSlotsArray() {
		this.slotTop = new int[] {
			0
		};
		this.slotRight = new int[] {
			1
		};
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot != 1 && super.isItemValidForSlot(slot, stack);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return openSides[side.ordinal()] == 1 ? this.slotRight : openSides[side.ordinal()] == -1 ? this.slotTop : new int[0];
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return super.canExtractItem(slot, stack, side);
	}

	@Override
	protected boolean canSmelt() {
		if (this.slots[0] == null || this.stored - this.energyBurnRate <= 0) return false;
		else {
			// Check if the item in the slot 1 can be smelted (has a set furnace recipe).
			ItemStack stack = CrusherRecipesRegistry.crusherList(this.slots[0]);
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
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#smeltItem()
	 */
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = CrusherRecipesRegistry.crusherList(this.slots[0]);

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

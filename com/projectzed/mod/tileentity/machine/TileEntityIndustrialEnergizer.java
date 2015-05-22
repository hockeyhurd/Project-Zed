/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.machine;

import net.minecraft.item.ItemStack;

import com.projectzed.api.energy.IItemChargeable;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;

/**
 * TileEntity code for industrialEnergizer.
 * 
 * @author hockeyhurd
 * @version Apr 1, 2015
 */
public class TileEntityIndustrialEnergizer extends AbstractTileEntityMachine {

	public TileEntityIndustrialEnergizer() {
		super("industrialEnergizer");
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return this.slots.length;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
		this.slots = new ItemStack[2];
	}

	/* (non-Javadoc)
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
	}

	public boolean isItemValid(ItemStack stack) {
		return stack.stackSize == 1 && stack.getItem() instanceof IItemChargeable && stack.getItemDamage() > 0;
	}
	
	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 1 && isItemValid(stack);
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] { 0, 1, 2, 3, 4, 5 };
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return this.isItemValidForSlot(slot, stack);
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canSmelt()
	 */
	@Override
	protected boolean canSmelt() {
		if (this.slots[0] == null || this.stored - this.energyBurnRate <= 0) return false;
		else {
			boolean valid = this.isItemValid(this.slots[0]);
			int chargeRate = 0;
			int left = 0;
			if (valid) {
				chargeRate = ((IItemChargeable) this.slots[0].getItem()).getChargeRate();
				left = this.slots[0].getItemDamage() * chargeRate;
				// this.energyBurnRate = 10;
				this.energyBurnRate = chargeRate;
				
				this.slots[0].setItemDamage(this.slots[0].getItemDamage() - 1);
			}
			
			if (this.cookTime == 0 && chargeRate > 0 && left > 0) {
				this.scaledTime = left / chargeRate - 1;
				// ProjectZed.logHelper.info("this.scaledTime:", this.scaledTime);
			}
			
			// Check if the item in the slot 1 can be smelted (has a set furnace recipe).
			ItemStack stack =  valid? this.slots[0] : (ItemStack) null;
			if (stack == null) return false;
			if (this.slots[1] == null) return true;
			if (!this.slots[1].isItemEqual(stack)) return false;

			// Add the result of the furnace recipe to the current stack size (already smelted so far).
			int result = this.slots[1].stackSize + stack.stackSize;

			// Make sure we aren't going over the set stack limit's size.
			return (result <= getInventoryStackLimit() && result <= stack.getMaxStackSize());
		}
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#smeltItem()
	 */
	@Override
	public void smeltItem() {
		if (this.canSmelt()) {
			// this.slots[0].setItemDamage(this.slots[0].getItemDamage() - 1);
			ItemStack itemStack = this.slots[0];
			itemStack.setItemDamage(0);
			
			if (this.slots[1] == null) this.slots[1] = itemStack.copy();
			
			else if (this.slots[1].isItemEqual(itemStack) && this.slots[1].getItemDamage() == 0) slots[1].stackSize += itemStack.stackSize;

			this.slots[0].stackSize--;

			if (this.slots[0].stackSize <= 0) this.slots[0] = null;
		}
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getSound()
	 */
	@Override
	public Sound getSound() {
		return Sound.ENERGIZER;
	}

}

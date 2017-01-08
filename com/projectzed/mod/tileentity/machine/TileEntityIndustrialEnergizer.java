/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.machine;

import com.projectzed.api.energy.IItemChargeable;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

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

	@Override
	public int getSizeInventory() {
		return this.slots.length;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
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

	public boolean isItemValid(ItemStack stack) {
		return stack.stackSize == 1 && stack.getItem() instanceof IItemChargeable && stack.getItemDamage() > 0;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot != 1 && isItemValid(stack);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return openSides[side.ordinal()] == 1 ? this.slotRight : openSides[side.ordinal()] == -1 ? this.slotTop : new int[0];
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return super.canExtractItem(slot, stack, side) && slot == 1;
	}

	@Override
	protected boolean canSmelt() {
		if (this.slots[0] == null || this.stored - this.energyBurnRate <= 0) return false;
		else {
			ItemStack stack = this.slots[0];
			boolean valid = this.isItemValid(stack);

			if (valid) {
				IItemChargeable itemChargeable = (IItemChargeable) stack.getItem();

				int chargeRate = itemChargeable.getChargeRate();
				// int amountToCharge = Math.min(itemChargeable.getChargeRate(), chargeRate);
				// amountToCharge = Math.min(amountToCharge, getEnergyStored());
				int amountToCharge = Math.min(chargeRate, getEnergyStored());
				this.energyBurnRate = amountToCharge;

				boolean result = itemChargeable.addPower(stack, amountToCharge, true) > 0;

				if (result) {
					// if (this.cookTime == 0)
						this.scaledTime = (itemChargeable.getCapacity() - itemChargeable.getStored(stack)) / amountToCharge;

					itemChargeable.addPower(stack, amountToCharge, false);
				}

				else energyBurnRate = 0;
			}

			// Check if the item in the slot 1 can be smelted (has a set furnace recipe).
			ItemStack endStack =  valid ? this.slots[0] : (ItemStack) null;
			if (endStack == null) return false;
			if (this.slots[1] == null) return true;
			if (!this.slots[1].isItemEqual(endStack)) return false;

			// Add the result of the furnace recipe to the current stack size (already smelted so far).
			int resultSize = this.slots[1].stackSize + stack.stackSize;

			// Make sure we aren't going over the set stack limit's size.
			return (resultSize <= getInventoryStackLimit() && resultSize <= stack.getMaxStackSize());
		}
	}

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

	@Override
	public Sound getSound() {
		return Sound.ENERGIZER;
	}

}

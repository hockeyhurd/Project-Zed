/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.container.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Fake container slot.
 *
 * @author hockeyhurd
 * @version 5/1/2016.
 */
public class SlotFake extends Slot {

	public SlotFake(IInventory inv, int slotNumber, int xPos, int yPos) {
		super(inv, slotNumber, xPos, yPos);
	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
		putStack(null);
	}

	@Override
	public ItemStack decrStackSize(int index) {
		return null;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return true;
	}

	@Override
	public void putStack(ItemStack stack) {
		if (stack != null) {
			stack = stack.copy();

			super.putStack(stack);
		}
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return true;
	}

}

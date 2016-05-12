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

import com.hockeyhurd.hcorelib.api.math.Vector2;
import com.projectzed.api.item.IPattern;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Special slot class to be used exclusively in containers that use crafting patterns.
 *
 * @see com.projectzed.api.item.IPattern
 *
 * @author hockeyhurd
 * @version 5/1/2016.
 */
public class SlotPattern extends Slot {

	private final boolean blankOnly;
	private final InventoryCrafting craftMatrix;
	private final IInventory craftResult;

	public SlotPattern(IInventory inv, InventoryCrafting craftMatrix, IInventory craftResult, int slotNumber, int xPos, int yPos, boolean blankOnly) {
		super(inv, slotNumber, xPos, yPos);

		this.blankOnly = blankOnly;
		this.craftMatrix = craftMatrix;
		this.craftResult = craftResult;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack != null && stack.getItem() instanceof IPattern &&
				blankOnly != ((IPattern) stack.getItem()).hasPattern(stack);
	}

	@Override
	public void putStack(ItemStack stack) {
		super.putStack(stack);

		if (stack == null || stack.getItem() == null) return;

		// Nothing we can do with a blank pattern, therefore return.
		if (blankOnly) return;

		IPattern pattern = (IPattern) stack.getItem();
		if (pattern == null || !pattern.hasPattern(stack)) return;
		final Vector2<Integer> patternVec = pattern.getPatternSize();
		final int patternSize = patternVec.x * patternVec.y;

		// Invalid size inventory... Since we are unsure how to handle this situation, just return.
		if (patternSize != craftMatrix.getSizeInventory()) return;

		for (int y = 0; y < patternVec.y; y++) {
			for (int x = 0; x < patternVec.x; x++) {
				final ItemStack currentStack = pattern.getPattern(stack)[y][x];
				craftMatrix.setInventorySlotContents(x + y * patternVec.y, currentStack);
			}
		}

		inventory.markDirty();
	}

	@Override
	public int getSlotStackLimit() {
		return !blankOnly ? 1 : 0x40;
	}

}

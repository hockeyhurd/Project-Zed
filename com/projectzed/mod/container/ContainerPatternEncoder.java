/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.container;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.container.slots.SlotFake;
import com.projectzed.mod.container.slots.SlotPattern;
import com.projectzed.mod.tileentity.machine.TileEntityPatternEncoder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

/**
 * Container for pattern encoding.
 *
 * @author hockeyhurd
 * @version 4/30/2016.
 */
public class ContainerPatternEncoder extends ContainerMachine {

	public final InventoryCrafting craftMatrix = new InventoryCrafting(this, TileEntityPatternEncoder.CRAFTING_MATRIX_SIZE,
			TileEntityPatternEncoder.CRAFTING_MATRIX_SIZE);
	private IInventory craftResult = new InventoryCraftResult();

	private SlotPattern patternIn;
	private SlotPattern patternOut;

	public ContainerPatternEncoder(InventoryPlayer inv, TileEntityPatternEncoder te) {
		super(inv, te, false);

		this.onCraftMatrixChanged(this.craftMatrix);
		addSlots(inv, te);
	}

	@Override
	protected void addSlots(InventoryPlayer inv, AbstractTileEntityMachine te) {
		// Add crafting result.
		this.addSlotToContainer(new SlotCrafting(inv.player, this.craftMatrix, this.craftResult, 0, 114, 24));

		// Add crafting matrix
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				// this.addSlotToContainer(new Slot(this.craftMatrix, x + y * 3,
				// 67 + y * 18, 6 + x * 18));
				// this.addSlotToContainer(new Slot(this.craftMatrix, x + y * 3, 20 + x * 18, 6 + y * 18));
				this.addSlotToContainer(new SlotFake(this.craftMatrix, x + y * 3, 20 + x * 18, 6 + y * 18));
				ItemStack stack = te.getStackInSlot((x + y * 3));
				if (stack != null && stack.stackSize > 0) this.craftMatrix.setInventorySlotContents(x + y * 3, stack);
			}
		}

		// Add Pattern slots:
		patternIn = new SlotPattern(te, craftMatrix, craftResult, te.getSizeInventory() - 2, 146, 15, true);
		patternOut = new SlotPattern(te, craftMatrix, craftResult, te.getSizeInventory() - 1, 146, 33, false);

		this.addSlotToContainer(patternIn);
		this.addSlotToContainer(patternOut);

		// addUpgradeInventorySlots(te);
		addPlayerInventorySlots(inv);
	}

	/**
	 * Clears crafting grid.
	 */
	public void clearSlots() {
		for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
			craftMatrix.setInventorySlotContents(i, null);
		}

		craftResult.setInventorySlotContents(0, null);

		this.onCraftMatrixChanged(craftMatrix);
		detectAndSendChanges();
	}

	@Override
	public void onCraftMatrixChanged(IInventory inv) {
		if (craftMatrix != null) craftResult.setInventorySlotContents(0,
				CraftingManager.getInstance().findMatchingRecipe(craftMatrix, te.getWorld()));
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
			this.te.setInventorySlotContents(i, this.craftMatrix.getStackInSlot(i));
		}

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotID);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotID == 0) {
				if (!this.mergeItemStack(itemstack1, 10, 46, true)) { return null; }

				slot.onSlotChange(itemstack1, itemstack);
			}

			else if (slotID >= 10 && slotID < 37) {
				if (!this.mergeItemStack(itemstack1, 37, 46, false)) return null;
			}

			else if (slotID >= 37 && slotID < 46) {
				if (!this.mergeItemStack(itemstack1, 10, 37, false)) return null;
			}

			else if (!this.mergeItemStack(itemstack1, 10, 46, false)) return null;

			if (itemstack1.stackSize == 0) slot.putStack((ItemStack) null);
			else slot.onSlotChanged();

			if (itemstack1.stackSize == itemstack.stackSize) return null;

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}

}

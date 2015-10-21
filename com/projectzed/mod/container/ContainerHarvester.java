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
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialHarvester;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

/**
 * Container class for TileEntityIndustrialHarvester.
 *
 * @author hockeyhurd
 * @version 10/21/2015.
 */
public class ContainerHarvester extends ContainerMachine {

	private final TileEntityIndustrialHarvester te2;

	public ContainerHarvester(InventoryPlayer inv, AbstractTileEntityMachine te) {
		super(inv, te);
		this.te2 = (TileEntityIndustrialHarvester) te;
	}

	@Override
	protected void addSlots(InventoryPlayer inv, AbstractTileEntityMachine te) {
		// TODO: Add and implement filtering slots!

		// Adds the 2 row, 9 col inventory box.
		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new SlotFurnace(inv.player, te, x + y * 9, 8 + x * 18, 17 + 53 + y * 18));
			}
		}

		addUpgradeInventorySlots(te);
		addPlayerInventorySlots(inv);
	}

	@Override
	protected void addPlayerInventorySlots(InventoryPlayer inv) {
		// Adds the player inventory to furnace's gui.
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				// ProjectZed.logHelper.info(x + y * 9, 47 + 84 + y * 18);
				this.addSlotToContainer(new Slot(inv, x + y * 9 + 9, 8 + x * 18, 0x30 + 84 + y * 18));
			}
		}

		// Adds the player hotbar slots to the gui.
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 0x30 + 142)); // 198
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack stack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

			if (index < te.getSizeInventory()) {
				if (!this.mergeItemStack(slotStack, te.getSizeInventory(), this.inventorySlots.size(), false)) return null;
			}

			else {
				if (!this.mergeItemStack(slotStack, 9, te.getSizeInventory(), false)) return null;
			}

			if (slotStack.stackSize == 0) slot.putStack(null);
			else slot.onSlotChanged();

			if (slotStack.stackSize == stack.stackSize) return null;
			slot.onPickupFromSlot(player, slotStack);
		}

		return stack;
	}

}

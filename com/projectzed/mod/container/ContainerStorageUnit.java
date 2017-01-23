package com.projectzed.mod.container;

import com.projectzed.mod.tileentity.machine.TileEntityIndustrialStorageUnit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * @author hockeyhurd
 * @version 1/22/2017.
 */
public class ContainerStorageUnit extends ContainerMachine {

	public ContainerStorageUnit(InventoryPlayer inventory, TileEntityIndustrialStorageUnit te) {
		super(inventory, te);

		addPlayerInventorySlots(inventory);
	}

	@Nullable
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		if (slotId == 1) {
			if (te.getStackInSlot(slotId) == null) super.slotClick(slotId, dragType, clickTypeIn, player);
			else {
				ItemStack slotStack = te.getStackInSlot(slotId);
				((TileEntityIndustrialStorageUnit) te).getBigItemStack().removeAmount(slotStack.stackSize);
				final int remaining = Math.min(slotStack.getMaxStackSize(), ((TileEntityIndustrialStorageUnit) te).getBigItemStack().getAmount());

				if (remaining > 0) {
					slotStack.stackSize = remaining;
				}

				else {
					slotStack = null;
					te.setInventorySlotContents(slotId, null);
				}
			}
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
}

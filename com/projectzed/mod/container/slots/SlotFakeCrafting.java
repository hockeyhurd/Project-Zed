package com.projectzed.mod.container.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;

/**
 * Class for emulating a crafting slot but without
 * the actual crafting part!
 *
 * @author hockeyhurd
 * @version 12/26/2016.
 */
public class SlotFakeCrafting extends SlotCrafting {

	public SlotFakeCrafting(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition,
			int yPosition) {
		super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return false;
	}
}

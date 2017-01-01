package com.projectzed.mod.container.slots;

import com.projectzed.mod.container.ITileContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

/**
 * @author hockeyhurd
 * @version 12/30/2016.
 */
public class SlotAdvancedCrafting extends SlotCrafting {

	private ITileContainer tileContainer;
	private InventoryCrafting craftingMatrix;

	public SlotAdvancedCrafting(ITileContainer tileContainer, EntityPlayer player, InventoryCrafting craftingInventory, IInventory inv, int slotIndex,
			int xPosition, int yPosition) {
		super(player, craftingInventory, inv, slotIndex, xPosition, yPosition);

		this.tileContainer = tileContainer;
		this.craftingMatrix = craftingInventory;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
		// if (SidedHelper.isServer()) {
			ItemStack[][] refillPattern = new ItemStack[9][];

			for (int i = 0; i < refillPattern.length; i++) {
				ItemStack slotStack = craftingMatrix.getStackInSlot(i);

				if (slotStack != null && slotStack.stackSize > 0) {
					refillPattern[i] = new ItemStack[1];
					refillPattern[i][0] = slotStack.copy();
				}
			}

			super.onPickupFromSlot(player, stack);
			tileContainer.fillCraftingGrid(refillPattern, 1);
		// }

		// else super.onPickupFromSlot(player, stack);
	}
}

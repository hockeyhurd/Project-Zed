package com.projectzed.mod.handler;

import net.minecraft.item.ItemStack;

import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

/**
 * Class containing code for recipes that return more than one item.
 * 
 * @author hockeyhurd
 * @version Dec 9, 2014
 */
public class CraftingEventHandler {

	/** Only static instance of this class. */
	private static final CraftingEventHandler HANLDER = new CraftingEventHandler();

	private CraftingEventHandler() {
	}

	/**
	 * @return static instance of this class.
	 */
	public static CraftingEventHandler instance() {
		return HANLDER;
	}

	/**
	 * Method used to decrease item damage of forging item after each used.
	 * 
	 * @param event = event passed as reference.
	 */
	@SubscribeEvent
	public void onCraftingEvent(PlayerEvent.ItemCraftedEvent event) {

		int count = 0;
		for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
			if (event.craftMatrix.getStackInSlot(i) != null) count++;
		}

		if (count == 2) {
			for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
				if (event.craftMatrix.getStackInSlot(i) != null) {
					ItemStack stack = event.craftMatrix.getStackInSlot(i);

					if (stack.getItem() == ProjectZed.forgingHammer && stack.getItemDamage() < stack.getMaxDamage()) {
						ItemStack newStack = new ItemStack(ProjectZed.forgingHammer, 2, stack.getItemDamage() + 1);
						event.craftMatrix.setInventorySlotContents(i, newStack);
					}
				}
			}
		}
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import com.projectzed.mod.ProjectZed;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.item.ItemStack;

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

		if (count == 2 || count == 4) {
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

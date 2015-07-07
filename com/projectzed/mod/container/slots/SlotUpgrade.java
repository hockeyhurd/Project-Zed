/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.container.slots;

import com.projectzed.api.item.IItemUpgradeComponent;
import com.projectzed.api.util.IUpgradeComponent;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Special slot class to be used exclusively in containers of TileEntities that implement IUpgradeComponent.
 * @see com.projectzed.api.util.IUpgradeComponent
 *
 * @author hockeyhurd
 * @version 7/7/2015.
 */
public class SlotUpgrade extends Slot {

	public SlotUpgrade(IInventory inventory, int slotNumber, int xPos, int yPos) {
		super(inventory, slotNumber, xPos, yPos);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack != null && stack.getItem() instanceof IItemUpgradeComponent && inventory instanceof IUpgradeComponent ?
				((IUpgradeComponent) inventory).canInsertItemUpgrade((IItemUpgradeComponent) stack.getItem(), stack) :
				false;
	}

	@Override
	public int getSlotStackLimit() {
		return 0x10;
	}

}

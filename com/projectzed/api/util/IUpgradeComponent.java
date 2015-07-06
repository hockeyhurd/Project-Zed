/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.api.util;

import com.projectzed.api.item.IItemUpgradeComponent;
import net.minecraft.item.ItemStack;

/**
 * Interface for item upgrades to be implemented on the TileEntity side of things.
 *
 * @author hockeyhurd
 * @version 7/5/2015.
 */
public interface IUpgradeComponent {

	/**
	 * @return array of upgrade slots.
	 */
	ItemStack[] getUpgradeSlots();

	ItemStack[] getCurrentUpgrades();

	/**
	 * Shortenend method for more quickly getting size or number of upgrade slots.
	 *
	 * @return effectively getUpgradeSlots().length.
	 */
	int getSizeUpgradeSlots();

	/**
	 * Gets whether given item upgrade can be used in this machine.
	 * @param component reference to item upgrade.
	 * @param stack reference to actual itemstack of item upgrades.
	 * @return true if allowed, else returns false.
	 */
	boolean canInsertItemUpgrade(IItemUpgradeComponent component, ItemStack stack);

}

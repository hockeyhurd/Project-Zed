/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.energy;

import net.minecraft.item.ItemStack;

/**
 * Base interface for all items that use McU as power.
 * 
 * @author hockeyhurd
 * @version Mar 30, 2015
 */
public interface IItemChargeable {

	/** Gets the capacity of the powered item. */
	int getCapacity();
	
	/** Gets the stored power of said itemstack. */
	int getStored(ItemStack stack);

	/** Sets the stored power to the itemstack. */
	void setStored(ItemStack stack, int amount);
	
	/** Adds and returns total of said itemstack. */
	int addPower(ItemStack stack, int amount, boolean simulate);
	
	/** Subtracts and returns total of said itemstack. */
	int subtractPower(ItemStack stack, int amount, boolean simulate);

	/** Gets the max charge rate of the item. */
	int getChargeRate();
	
}

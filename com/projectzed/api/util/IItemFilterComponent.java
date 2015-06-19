/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.util;

import net.minecraft.item.ItemStack;

/**
 * Interface for item filter component systems.
 * 
 * @author hockeyhurd
 * @version Jun 18, 2015
 */
public interface IItemFilterComponent {

	/**
	 * @return array of itemstacks to be filtered.
	 */
	ItemStack[] getItemFilter();
	
	/**
	 * @return size of item filter.
	 */
	int getItemFilterSize();
	
	/**
	 * Method used to add given itemstack to filter list/array.
	 * 
	 * @param stack itemstack to add to filter.
	 */
	void addToItemFilter(ItemStack stack);
	
	/**
	 * Method used to remove given itemstack to filter list/array.
	 * 
	 * @param stack itemstack to remove from filter.
	 */
	void removeFromItemFilter(ItemStack stack);
	
	/**
	 * Allows for quickly calling 'filterContainsItemStack' method overload.
	 * @param stack stack to check.
	 * @return true if filter contains given itemstack or not.
	 */
	boolean filterContainsItemStack(ItemStack stack);
	
	/**
	 * @param stack itemstack to check in the filter.
	 * @param stackSizeSp set to true if stack size should be considered while checking.
	 * @return true if filter contains given itemstack or not.
	 */
	boolean filterContainsItemStack(ItemStack stack, boolean stackSizeSp);
	
	/**
	 * @return item filter type, WHITELIST, BLACKLIST, etc.
	 */
	EnumFilterType getItemFilterType();
	
	/**
	 * Sets the item filter type.
	 * 
	 * @param type filter type to set to.
	 */
	void setItemFilterType(EnumFilterType type);
	
}

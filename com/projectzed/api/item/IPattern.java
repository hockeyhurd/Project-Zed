/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.api.item;

import net.minecraft.item.ItemStack;

/**
 * Interfacing for items that hold patterns.
 *
 * @author hockeyhurd
 * @version 4/30/2016.
 */
public interface IPattern {

	/**
	 * Gets if IPattern has a set pattern.
	 *
	 * @return boolean result.
	 */
	boolean hasPattern();

	/**
	 * Gets the ItemStack pattern.
	 *
	 * @return ItemStack[][].
	 */
	ItemStack[][] getPattern();

	/**
	 * Gets the output of the crafting result.
	 *
	 * @return ItemStack crafting result.
	 */
	ItemStack getCraftingResult();

	/**
	 * Function to compare patterns.
	 *
	 * @param pattern ItemStack[][] pattern to compare.
	 * @return boolean result.
	 */
	boolean isPatternEqual(ItemStack[][] pattern);

	/**
	 * Sets internal pattern to provided pattern.
	 *
	 * @param pattern ItemStack[][] pattern.
	 * @param resultStack ItemStack result.
	 */
	void setPattern(ItemStack[][] pattern, ItemStack resultStack);

	/**
	 * Clears internal pattern.
	 */
	void clearPattern();

}

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

import com.hockeyhurd.api.math.Vector2;
import net.minecraft.item.ItemStack;

/**
 * Interfacing for items that hold patterns.
 *
 * @author hockeyhurd
 * @version 4/30/2016.
 */
public interface IPattern {

	/**
	 * Gets the pattern width and height contained in vector2i.
	 *
	 * @return Vector2i.
	 */
	Vector2<Integer> getPatternSize();

	/**
	 * Gets if IPattern has a set pattern.
	 *
	 * @param stack ItemStack to reference.
	 *
	 * @return boolean result.
	 */
	boolean hasPattern(ItemStack stack);

	/**
	 * Gets the ItemStack pattern.
	 *
	 * @param stack ItemStack to reference.
	 *
	 * @return ItemStack[][].
	 */
	ItemStack[][] getPattern(ItemStack stack);

	/**
	 * Gets the output of the crafting result.
	 *
	 * @param stack ItemStack to reference.
	 *
	 * @return ItemStack crafting result.
	 */
	ItemStack getCraftingResult(ItemStack stack);

	/**
	 * Function to compare patterns.
	 *
	 * @param stack ItemStack to reference.
	 * @param otherPattern ItemStack[][] pattern to compare.
	 * @return boolean result.
	 */
	boolean isPatternEqual(ItemStack stack, ItemStack[][] otherPattern);

	/**
	 * Sets internal pattern to provided pattern.
	 *
	 * @param stack ItemStack to reference.
	 * @param pattern ItemStack[][] pattern.
	 * @param resultStack ItemStack result.
	 */
	void setPattern(ItemStack stack, ItemStack[][]pattern, ItemStack resultStack);

	/**
	 * Clears internal pattern.
	 *
	 * @param stack ItemStack to reference.
	 */
	void clearPattern(ItemStack stack);

}

package com.projectzed.mod.container;

import com.hockeyhurd.hcorelib.api.tileentity.AbstractTile;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

/**
 * Interfacing for tile entity's with a container.
 *
 * @author hockeyhurd
 * @version 12/30/2016.
 */
public interface ITileContainer {

	/**
	 * Gets the container.
	 *
	 * @return Container type.
	 */
	Container getContainer();

	/**
	 * Gets the tile in the container.
	 *
	 * @return AbstractTile.
	 */
	AbstractTile getTile();

	/**
	 * Fill crafting grid delegate.
	 *
	 * @param pattern ItemStack[][] pattern.
	 * @param limitAmount int limiting amount.
	 */
	void fillCraftingGrid(ItemStack[][] pattern, int limitAmount);

}

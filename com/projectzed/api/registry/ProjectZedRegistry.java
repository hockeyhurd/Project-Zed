/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.registry;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlock;
import com.projectzed.mod.registry.BlockRegistry;
import com.projectzed.mod.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.List;

/**
 * Goal of this class is to provide a simplistic
 * localized registry through the api to get items
 * and blocks through a controlled and intuitive manner.
 * 
 * @author hockeyhurd
 * @version Oct 20, 2014
 */
public class ProjectZedRegistry {

	private static List<AbstractHCoreBlock> blockReg;
	private static List<Item> itemReg;
	private static ProjectZedRegistry reg = new ProjectZedRegistry();
	
	private ProjectZedRegistry() {
		blockReg = BlockRegistry.instance().getBlocks();
		itemReg = ItemRegistry.instance().getItems();
	}
	
	/**
	 * Gets the instance of registry.
	 * @return registry instance.
	 */
	public static ProjectZedRegistry instance() {
		return reg;
	}
	
	/**
	 * Handles get block from registry in a controlled manner.
	 * <br>NOTE: use of this function for finding blocks of ore,
	 * use ore<Block>. Ex. To find Coal ore, use oreCoal.
	 * 
	 * @param name = name of block.
	 * @return block if found in registry, else return null.
	 */
	public static Block getBlockByName(String name) {
		return BlockRegistry.instance().getBlockByName(name);
	}
	
	/**
	 * Handles get item from registry in a controlled manner.
	 * <br>NOTE: use of this function for finding items of ingot,
	 * use ingot<Item>. Ex. To find Iron ingot, use ingotIron.
	 * 
	 * @param name name of item.
	 * @return item if found in registry, else return null.
	 */
	public static Item getItemByName(String name) {
		return ItemRegistry.instance().getItemByName(name);
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.util.OutputUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for all furnace recipes.
 * 
 * @author hockeyhurd
 * @version May 5, 2015
 */
public class FurnaceRecipeRegistry {

	private static FurnaceRecipeRegistry reg = new FurnaceRecipeRegistry();
	private Map<Object, OutputUtil> map;
	
	private FurnaceRecipeRegistry() {
	}
	
	/**
	 * Gets the single instance of this class.
	 * 
	 * @return instance.
	 */
	public static FurnaceRecipeRegistry instance() {
		return reg;
	}
	
	/**
	 * Gets stored furnace recipe mapping.
	 * 
	 * @return furnace recipe mapping.
	 */
	public Map<Object, OutputUtil> getMap() {
		return map;
	}

	/**
	 * Main method for initializing stuffs.
	 */
	public void init() {
		if (map == null || map.size() == 0) {
			map = new HashMap<Object, OutputUtil>();
			
			reg.add(ProjectZed.dustIron, createOutput(new ItemStack(Items.iron_ingot), 25f));
			reg.add(ProjectZed.dustGold, createOutput(new ItemStack(Items.gold_ingot), 25f));
			
			reg.add(ProjectZed.oreTitanium, createOutput(new ItemStack(ProjectZed.ingotTitanium), 50f));
			reg.add(ProjectZed.dustTitanium, createOutput(new ItemStack(ProjectZed.ingotTitanium), 50f));
			
			reg.add(ProjectZed.oreCopper, createOutput(new ItemStack(ProjectZed.ingotCopper), 50f));
			reg.add(ProjectZed.dustCopper, createOutput(new ItemStack(ProjectZed.ingotCopper), 50f));
			
			reg.add(ProjectZed.oreNickel, createOutput(new ItemStack(ProjectZed.ingotNickel), 50f));
			reg.add(ProjectZed.dustNickel, createOutput(new ItemStack(ProjectZed.ingotNickel), 50f));
			
			reg.add(ProjectZed.oreAluminium, createOutput(new ItemStack(ProjectZed.ingotAluminium), 50f));
			reg.add(ProjectZed.dustAluminium, createOutput(new ItemStack(ProjectZed.ingotAluminium), 50f));

			reg.add(ProjectZed.dustMixedAlloy, createOutput(new ItemStack(ProjectZed.mixedAlloy, 1), 50f));
			
		}
	}
	
	/**
	 * Helper function for quickly creating new OutputUtil objects.
	 * 
	 * @param stack itemstack input.
	 * @param xp xp allowed.
	 * @return newly created OutputUtil object.
	 */
	private OutputUtil createOutput(final ItemStack stack, final float xp) {
		return new OutputUtil(stack, xp);
	}
	
	/**
	 * Helper method for adding blocks to mapping.
	 * 
	 * @param input input block
	 * @param output output to get.
	 */
	private void add(Block input, OutputUtil output) {
		if (map != null && input != null && output.isValid()) map.put(input, output);
	}
	
	/**
	 * Helper method for adding items to mapping.
	 * 
	 * @param input input item
	 * @param output output to get.
	 */
	private void add(Item input, OutputUtil output) {
		if (map != null && input != null && output.isValid()) map.put(input, output);
	}

}

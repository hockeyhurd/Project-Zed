/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry;

import com.projectzed.mod.ProjectZed;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Class containing code for initializing the metal press's smelting recipe list. <br>
 * NOTE: This class was closely followed to PulverizerRecipes.java by author hockeyhurd. <br>
 * For more info on this click here to view in repo: click <a href="http://goo.gl/L7oiKb">here</a>.
 * 
 * @author hockeyhurd
 * @version Dec 9, 2014
 */
public class MetalPressRecipesRegistry {

	private static HashMap<ItemStack, ItemStack> mapVanilla;
	private static HashMap<String, String> mapModded;
	private static Set<Entry<String, String>> mapSet;

	private MetalPressRecipesRegistry() {
	}

	/**
	 * Main init method for initializing all the things.
	 */
	public static void init() {
		mapVanilla = new HashMap<ItemStack, ItemStack>();
		mapModded = new HashMap<String, String>();

		// Normal mapping.
		// mapVanilla.put(new ItemStack(ProjectZed.ingotAluminium, 1), new ItemStack(ProjectZed.plateAluminium, 1));
		// mapVanilla.put(new ItemStack(ProjectZed.mixedAlloy, 1), new ItemStack(ProjectZed.plateReinforced, 1));
		mapVanilla.put(new ItemStack(Items.iron_ingot), new ItemStack(ProjectZed.plateIron));
		mapVanilla.put(new ItemStack(Items.gold_ingot), new ItemStack(ProjectZed.plateGold));
		mapVanilla.put(new ItemStack(Items.dye, 1, 0x4), new ItemStack(ProjectZed.plateLapis));

		// Fall back/modded mapping.
		mapModded.put("ingotAluminium", "plateAluminium");
		mapModded.put("ingotAluminum", "plateAluminum");
		mapModded.put("mixedAlloy", "plateReinforced");
		mapModded.put("ingotTin", "plateTin");
		mapModded.put("ingotCopper", "plateCopper");
		mapModded.put("ingotBronze", "plateBronze");
		mapModded.put("ingotTitanium", "plateTitanium");
		mapModded.put("ingotNickel", "plateNickel");
		mapModded.put("ingotUranium", "plateUranium");

		initEntries();
	}

	/**
	 * Method used to init entries mapping.
	 */
	private static void initEntries() {
		mapSet = mapModded.entrySet();
	}
	
	/**
	 * Get the attempted map of recipe list.
	 * 
	 * @return map.
	 */
	public static HashMap<ItemStack, ItemStack> getMap() {
		return mapVanilla;
	}

	/**
	 * Static function used to get output of said itemstack from internal mappings and contacting to/from ore dictionary.
	 * 
	 * @param stack = stact to reference.
	 * @return output as itemstack.
	 */
	public static ItemStack pressList(ItemStack stack) {
		boolean flag = false;
		ItemStack temp = null;
		
		/*
		 * First attempt to see we have data handling for the given stack in the vanilla mapping, if not continue and use the fallback mapping
		 * (modded).
		 */
		if (mapVanilla.size() > 0 && !mapVanilla.isEmpty()) {
			flag = true;
			
			for (ItemStack itemStack : mapVanilla.keySet()) {
				if (itemStack.isItemEqual(stack)) {
					temp = mapVanilla.get(itemStack);
					break;
				}
			}
		}

		// If found data in vanilla mapping, return now, no need to continue.
		if (flag && temp != null) return temp;

		// Else not found, prepare data for collection from the Ore Dictionary.
		if (mapModded.size() > 0) {

			int currentID = OreDictionary.getOreID(stack);
			if (currentID == -1) return (ItemStack) null;
			
			String inputName = OreDictionary.getOreName(currentID);
			if (!mapModded.containsKey(inputName)) return (ItemStack) null;
			
			String outputName = mapModded.get(inputName);
			if (OreDictionary.getOres(outputName) == null || OreDictionary.getOres(outputName).size() == 0) return (ItemStack) null;
			temp = OreDictionary.getOres(outputName).get(0);
			
			if (temp == null) return (ItemStack) null;
			
			flag = true;
			Block block = null;
			Item item = null;
					
			/*
			* Checks if the stack is instance of Block or instance of Item. In theory, only one of the two objects should be null at a given
			* instance; hence returning the correct stack size below.
			*/
			if (inputName.contains("ore")) block = Block.getBlockById(OreDictionary.getOreID(inputName));
			else if (inputName.contains("ingot")) item = Item.getItemById(OreDictionary.getOreID(inputName));

			// Somewhat overly complicated but makes more sense writing like this imo.
			temp.stackSize = block != null && item == null ? 2 : (block == null && item != null ? 1 : 1);
		}

		// If found and stored in variable temp while != null, return data.
		if (flag && temp != null) {
			mapVanilla.put(stack, temp);
			return temp;
		}

		else return (ItemStack) null;
	}

}

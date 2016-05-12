/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry;

import com.hockeyhurd.hcorelib.api.util.LogicHelper;
import com.hockeyhurd.hcorelib.api.util.NumberParser;
import com.projectzed.api.registry.IRegistrable;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Class containing code for initializing the lumber mill's smelting recipe list. <br>
 * NOTE: This class was closely followed to PulverizerRecipes.java by author hockeyhurd. <br>
 * For more info on this click here to view in repo: click <a href="http://goo.gl/L7oiKb">here</a>.
 * 
 * @author hockeyhurd
 * @version Nov 17, 2014
 */
public class LumberMillRecipesRegistry implements IRegistrable {

	private static HashMap<ItemStack, ItemStack> mapVanilla;
	private static HashMap<String, String> mapModded;
	private static Set<Entry<String, String>> mapSet;

	private LumberMillRecipesRegistry() {
	}

	/**
	 * Main init method for initializing all the things.
	 */
	public static void init() {
		mapVanilla = new HashMap<ItemStack, ItemStack>();
		mapModded = new HashMap<String, String>();

		// Normal mapping.
		mapVanilla.put(new ItemStack(Blocks.log, 1, 0), new ItemStack(Blocks.planks, 8, 0));
		mapVanilla.put(new ItemStack(Blocks.log, 1, 1), new ItemStack(Blocks.planks, 8, 1));
		mapVanilla.put(new ItemStack(Blocks.log, 1, 2), new ItemStack(Blocks.planks, 8, 2));
		mapVanilla.put(new ItemStack(Blocks.log, 1, 3), new ItemStack(Blocks.planks, 8, 3));
		mapVanilla.put(new ItemStack(Blocks.log2, 1, 0), new ItemStack(Blocks.planks, 8, 4));
		mapVanilla.put(new ItemStack(Blocks.log2, 1, 1), new ItemStack(Blocks.planks, 8, 5));
		mapVanilla.put(new ItemStack(Blocks.planks, 1, 0), new ItemStack(Items.stick, 8));
		mapVanilla.put(new ItemStack(Blocks.planks, 1, 1), new ItemStack(Items.stick, 8));
		mapVanilla.put(new ItemStack(Blocks.planks, 1, 2), new ItemStack(Items.stick, 8));
		mapVanilla.put(new ItemStack(Blocks.planks, 1, 3), new ItemStack(Items.stick, 8));
		mapVanilla.put(new ItemStack(Blocks.planks, 1, 4), new ItemStack(Items.stick, 8));
		mapVanilla.put(new ItemStack(Blocks.planks, 1, 5), new ItemStack(Items.stick, 8));

		// Fall back/modded mapping.

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

	@Override
	public IRegistrable getInstance() {
		return this;
	}

	@Override
	public boolean addToRegistry(String key, String value) {
		return false;
	}

	@Override
	public boolean addToRegistry(String[] key, String[] value) {
		if (!LogicHelper.nullCheckString(key) || !LogicHelper.nullCheckString(value)) return false;

		if ((key.length == 2 || key.length == 3) && (value.length == 2 || value.length == 3)) {
			final Block blockKey = Block.getBlockFromName(key[0]);
			if (blockKey == null) return false;

			int blockAmount = NumberParser.parseInt(key[1]);
			if (blockAmount < 0) blockAmount = 0;

			int blockMeta = key.length == 3 ? NumberParser.parseInt(key[2]) : 0;
			if (blockMeta < 0) blockMeta = 0;

			ItemStack inputStack = new ItemStack(blockKey, blockAmount, blockMeta);

			Block blockValue = Block.getBlockFromName(value[0]);
			Item itemValue = (Item) Item.itemRegistry.getObject(value[0]);
			ItemStack outStack;

			int outAmount = NumberParser.parseInt(value[1]);
			if (outAmount < 0) outAmount = 1;

			int outMeta = value.length == 3 ? NumberParser.parseInt(value[2]) : 0;
			if (outMeta < 0) outMeta = 0;

			if (blockValue == null && itemValue == null) return false;
			else if (blockValue == null && itemValue != null) outStack = new ItemStack(itemValue, outAmount, outMeta);
			else outStack = new ItemStack(blockValue, outAmount, outMeta);

			mapVanilla.put(inputStack, outStack);
			return true;
		}

		return false;
	}

	/**
	 * Static function used to get output of said itemstack from internal mappings and contacting to/from ore dictionary.
	 * 
	 * @param stack = stact to reference.
	 * @return output as itemstack.
	 */
	public static ItemStack millingList(ItemStack stack) {
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

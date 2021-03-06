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
import com.projectzed.mod.ProjectZed;
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
 * Class containing code for initializing the crusher's smelting recipe list.
 * <br>NOTE: This class was closely followed to PulverizerRecipes.java by author hockeyhurd.
 * <br>For more info on this click here to view in repo: click <a href="http://goo.gl/L7oiKb">here</a>.
 * 
 * @author hockeyhurd
 * @version Nov 4, 2014
 */
public class CrusherRecipesRegistry implements IRegistrable {

	private static HashMap<ItemStack, ItemStack> mapVanilla;
	private static HashMap<String, String> mapModded;
	private static Set<Entry<String, String>> mapSet;

	private CrusherRecipesRegistry() {
	}

	/**
	 * Main init method for initializing all the things.
	 */
	public static void init() {
		mapVanilla = new HashMap<ItemStack, ItemStack>();
		mapModded = new HashMap<String, String>();

		// Normal mapping
		mapVanilla.put(new ItemStack(Blocks.IRON_ORE, 1), new ItemStack(ProjectZed.dustIron, 2));
		mapVanilla.put(new ItemStack(Blocks.GOLD_ORE, 1), new ItemStack(ProjectZed.dustGold, 2));
		mapVanilla.put(new ItemStack(Blocks.DIAMOND_ORE, 1),  new ItemStack(Items.DIAMOND, 2));
		mapVanilla.put(new ItemStack(Blocks.EMERALD_ORE, 1),  new ItemStack(Items.EMERALD, 2));
		mapVanilla.put(new ItemStack(Blocks.REDSTONE_ORE, 1), new ItemStack(Items.REDSTONE, 6));
		mapVanilla.put(new ItemStack(Blocks.LAPIS_ORE, 1), new ItemStack(Items.DYE, 9, 4));
		mapVanilla.put(new ItemStack(Items.COAL, 1), new ItemStack(ProjectZed.dustCoal, 1));
		mapVanilla.put(new ItemStack(Items.IRON_INGOT, 1), new ItemStack(ProjectZed.dustIron, 1));
		mapVanilla.put(new ItemStack(Items.GOLD_INGOT, 1), new ItemStack(ProjectZed.dustGold, 1));
		mapVanilla.put(new ItemStack(Blocks.COAL_BLOCK, 1),  new ItemStack(Items.COAL, 2));
		mapVanilla.put(new ItemStack(Blocks.QUARTZ_ORE, 1),  new ItemStack(Items.QUARTZ, 2));
		mapVanilla.put(new ItemStack(Items.BONE, 1), new ItemStack(Items.DYE, 6, 15));
		mapVanilla.put(new ItemStack(Items.BLAZE_ROD, 1), new ItemStack(Items.BLAZE_POWDER, 6));
		mapVanilla.put(new ItemStack(Blocks.COBBLESTONE, 1), new ItemStack(Blocks.GRAVEL, 1));
		mapVanilla.put(new ItemStack(Blocks.GRAVEL, 1), new ItemStack(Blocks.SAND, 1));
		mapVanilla.put(new ItemStack(Blocks.GLOWSTONE, 1), new ItemStack(Items.GLOWSTONE_DUST, 4));

		// Fall back mapping
		mapModded.put("oreGlow", "dustGlow");
		mapModded.put("ingotGlow", "dustGlow");
		mapModded.put("oreTitanium", "dustTitanium");
		mapModded.put("ingotTitanium", "dustTitanium");
		mapModded.put("oreNickel", "dustNickel");
		mapModded.put("ingotNickel", "dustNickel"); 
		mapModded.put("oreXynite", "dustXynite");
		mapModded.put("ingotXynite", "dustXynite");
		mapModded.put("oreFermite", "dustFermite");
		mapModded.put("ingotFermite", "dustFermite");
		mapModded.put("oreTanzanite", "dustTanzanite");
		mapModded.put("ingotTanzanite", "dustTanzanite");
		mapModded.put("oreCopper", "dustCopper");
		mapModded.put("ingotCopper", "dustCopper");
		mapModded.put("oreBronze", "dustBronze");
		mapModded.put("ingotBronze", "dustBronze");
		mapModded.put("oreTin", "dustTin");
		mapModded.put("ingotTin", "dustTin");
		mapModded.put("oreAluminium", "dustAluminium");
		mapModded.put("ingotAluminium", "dustAluminium");
		mapModded.put("oreLead", "dustLead");
		mapModded.put("ingotLead", "dustLead");
		mapModded.put("oreSilver", "dustSilver");
		mapModded.put("ingotSilver", "dustSilver");
		mapModded.put("oreCertusQuartz", "crystalCertusQuartz");
		mapModded.put("gemQuartz", "dustNetherQuartz");
		mapModded.put("crystalCertusQuartz", "dustCertusQuartz");
		mapModded.put("crystalFluix", "dustFluix");
		mapModded.put("oreZinc", "dustZinc");
		mapModded.put("ingotZinc", "dustZinc");
		mapModded.put("ingotInvar", "dustInvar");
		mapModded.put("ingotElectrum", "dustElectrum");
		mapModded.put("ingotSignalum", "dustSignalum");
		mapModded.put("ingotEnderium", "dustEnderium");
		mapModded.put("ingotTungsten", "dustTungsten");
		mapModded.put("oreTungsten", "ingotTungsten");
		mapModded.put("ingotBrass", "dustBrass");
		mapModded.put("coal", "dustCoal");
		mapModded.put("denseoreCoal", "oreCoal");
		mapModded.put("denseoreLapis", "oreLapis");
		mapModded.put("denseoreIron", "oreIron");
		mapModded.put("denseoreGold", "oreGold");
		mapModded.put("denseoreRedstone", "oreRedstone");
		mapModded.put("denseoreDiamond", "oreDiamond");
		mapModded.put("denseoreEmerald", "oreEmerald");
		mapModded.put("denseoreQuartz", "oreQuartz");
		
		mapModded.put("gravel", "itemSilicon");

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
		if (key == null || key.length() == 0 || value == null || value.length() == 0) return false;

		mapModded.put(key, value);

		return true;
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
			// Item itemValue = (Item) Item.itemRegistry.getObject(value[0]);
			Item itemValue = Item.getByNameOrId(value[0]);
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
	 * Static function used to get output of said itemstack from internal
	 * mappings and contacting to/from ore dictionary.
	 * 
	 * @param stack = stact to reference.
	 * @return output as itemstack.
	 */
	public static ItemStack crusherList(ItemStack stack) {

		/*
		 * First attempt to see we have data handling for the given stack in the vanilla mapping, if not continue and use the fallback mapping
		 * (modded).
		 */
		if (mapVanilla.size() > 0 && !mapVanilla.isEmpty()) {
			for (ItemStack itemStack : mapVanilla.keySet()) {
				if (itemStack.isItemEqual(stack)) return mapVanilla.get(itemStack);
			}
		}

		ItemStack tempItemStack = null;

		// Else not found, prepare data for collection from the Ore Dictionary.
		if (!mapModded.isEmpty()) {

			int[] oreIDs = OreDictionary.getOreIDs(stack);
			if (oreIDs == null || oreIDs.length == 0) return null;

			int currentID = oreIDs[0];
			if (currentID == -1) return null;
			
			String inputName = OreDictionary.getOreName(currentID);
			if (!mapModded.containsKey(inputName)) return null;
			
			String outputName = mapModded.get(inputName);
			if (OreDictionary.getOres(outputName) == null || OreDictionary.getOres(outputName).isEmpty()) return null;
			tempItemStack = OreDictionary.getOres(outputName).get(0);
			
			if (tempItemStack == null) return null;
			
			Block block = null;
			Item item = null;
			boolean isDense = false;
					
			/*
			* Checks if the stack is instance of Block or instance of Item. In theory, only one of the two objects should be null at a given
			* instance; hence returning the correct stack size below.
			*/
			if (inputName.contains("ore")) {
				block = Block.getBlockById(OreDictionary.getOreID(inputName));
				if (inputName.contains("dense")) isDense = true;
			}

			else if (inputName.contains("ingot")) item = Item.getItemById(OreDictionary.getOreID(inputName));

			// Somewhat overly complicated but makes more sense writing like this imo.
			tempItemStack.stackSize = block != null && item == null ? (!isDense ? 2 : 4) : (block == null && item != null ? 1 : 1);
		}

		// If found and stored in variable temp while != null, return data.
		if (tempItemStack != null) {
			mapVanilla.put(stack, tempItemStack);
			return tempItemStack;
		}

		return null;
	}

}

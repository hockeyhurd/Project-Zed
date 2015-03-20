/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.projectzed.mod.ProjectZed;

/**
 * Class containing code for initializing the crusher's smelting recipe list.
 * <br>NOTE: This class was closely followed to PulverizerRecipes.java by author hockeyhurd.
 * <br>For more info on this click here to view in repo: click <a href="http://goo.gl/L7oiKb">here</a>.
 * 
 * @author hockeyhurd
 * @version Nov 4, 2014
 */
public class CrusherRecipesRegistry {

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
		mapVanilla.put(new ItemStack(Blocks.iron_ore, 1), new ItemStack(ProjectZed.dustIron, 2));
		mapVanilla.put(new ItemStack(Blocks.gold_ore, 1), new ItemStack(ProjectZed.dustGold, 2));
		mapVanilla.put(new ItemStack(Blocks.diamond_ore, 1),  new ItemStack(Items.diamond, 2));
		mapVanilla.put(new ItemStack(Blocks.redstone_ore, 1), new ItemStack(Items.redstone, 6));
		mapVanilla.put(new ItemStack(Items.coal, 1), new ItemStack(ProjectZed.dustCoal, 1));
		mapVanilla.put(new ItemStack(Items.iron_ingot, 1), new ItemStack(ProjectZed.dustIron, 1));
		mapVanilla.put(new ItemStack(Items.gold_ingot, 1), new ItemStack(ProjectZed.dustGold, 1));
		mapVanilla.put(new ItemStack(Blocks.coal_block, 1),  new ItemStack(Items.coal, 2));
		mapVanilla.put(new ItemStack(Blocks.quartz_ore, 1),  new ItemStack(Items.quartz, 2));
		mapVanilla.put(new ItemStack(Items.bone, 1), new ItemStack(Items.dye, 6, 15));
		mapVanilla.put(new ItemStack(Items.blaze_rod, 1), new ItemStack(Items.blaze_powder, 6));
		mapVanilla.put(new ItemStack(Blocks.cobblestone, 1), new ItemStack(Blocks.gravel, 1));
		mapVanilla.put(new ItemStack(Blocks.gravel, 1), new ItemStack(Blocks.sand, 1));

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
		mapModded.put("coal", "dustCoal");
		mapModded.put("denseoreCoal", "oreCoal");
		mapModded.put("denseoreLapis", "oreLapis");
		mapModded.put("denseoreIron", "oreIron");
		mapModded.put("denseoreGold", "oreGold");
		mapModded.put("denseoreRedstone", "oreRedstone");
		mapModded.put("denseoreDiamond", "oreDiamond");
		mapModded.put("denseoreEmerald", "oreEmerald");
		mapModded.put("denseoreQuartz", "oreQuartz");

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
	 * Static function used to get output of said itemstack from internal
	 * mappings and contacting to/from ore dictionary.
	 * 
	 * @param stack = stact to reference.
	 * @return output as itemstack.
	 */
	public static ItemStack crusherList(ItemStack stack) {
		boolean flag = false;
		ItemStack temp = null;

		/*
		 * First attempt to see we have data handling for the given stack in the vanilla mapping, if not continue and use the fallback mapping
		 * (modded).
		 */
		if (mapVanilla.size() > 0 && mapVanilla.containsKey(stack) && mapVanilla.get(stack).getItemDamage() == stack.getItemDamage()) {
			flag = true;
			temp = mapVanilla.get(stack);
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
			boolean isDense = false;;
					
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
			temp.stackSize = block != null && item == null ? (!isDense ? 2 : 4) : (block == null && item != null ? 1 : 1);
		}

		// If found and stored in variable temp while != null, return data.
		if (flag && temp != null) {
			mapVanilla.put(stack, temp);
			return temp;
		}

		else return (ItemStack) null;
	}

}

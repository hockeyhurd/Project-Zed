package com.projectzed.mod.registry;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Class containing code for initializing the lumber mill's smelting recipe list.
 * <br>NOTE: This class was closely followed to PulverizerRecipes.java by author hockeyhurd.
 * <br>For more info on this click here to view in repo: click <a href="http://goo.gl/L7oiKb">here</a>.
 * 
 * @author hockeyhurd
 * @version Nov 17, 2014
 */
public class LumberMillRecipesRegistry {

	private static HashMap<ItemStack, ItemStack> mapVanilla;
	private static HashMap<String, String> mapModded;
	private static Set<Entry<String, String>> mapSet;
	
	private LumberMillRecipesRegistry() {
	}
	
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
		
		// Fall back/modded mapping.
	}
	
	public static ItemStack millingList(ItemStack stack) {
		boolean flag = false;
		ItemStack temp = null;

		/* First attempt to see we have data handling for the given stack
		 * in the vanilla mapping, if not continue and use the fallback mapping
		 * (modded).
		 */
		for (ItemStack currentStack : mapVanilla.keySet()) {
			if (stack.getItem() == currentStack.getItem() && stack.getItemDamage() == currentStack.getItemDamage()) {
				temp = mapVanilla.get(currentStack);
				flag = true;
				break;
			}
		}

		// If found data in vanilla mapping, return now, no need to continue.
		if (flag && temp != null) return temp;

		// Else not found, prepare data for collection from the Ore Dictionary.
		int currentID = OreDictionary.getOreID(stack);
		String current = "", current2 = "";
		for (int i = 0; i < OreDictionary.getOreNames().length; i++) {
			for (Entry<String, String> s : mapSet) {
				current = s.getKey();
				current2 = s.getValue();
				int id = OreDictionary.getOreID(current);
				
				if (current.equals(OreDictionary.getOreNames()[i]) && currentID == id) {
					flag = true;
					break;
				}
			}

			if (flag) {
				Block block = null;
				Item item = null;
				
				/* Checks if the stack is instance of Block or instance of Item.
				 * In theory, only one of the two objects should be null at a given instance;
				 * hence returning the correct stack size below.
				 */
				if (current.contains("ore")) block = Block.getBlockById(OreDictionary.getOreID(current));
				else if (current.contains("ingot")) item = Item.getItemById(OreDictionary.getOreID(current));
				temp = OreDictionary.getOres(current2).get(0);
				
				// Somewhat overly complicated but makes more sense writing like this imo.
				temp.stackSize = block != null && item == null ? 2 : (block == null && item != null ? 1 : 1);
				break;
			}
		}

		// If found and stored in variable temp while != null, return data.
		return flag && temp != null ? temp : (ItemStack) null;
	}

}

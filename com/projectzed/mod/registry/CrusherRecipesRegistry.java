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

	public static void init() {
		mapVanilla = new HashMap<ItemStack, ItemStack>();
		mapModded = new HashMap<String, String>();

		// Normal mapping
		mapVanilla.put(new ItemStack(Blocks.iron_ore, 1), new ItemStack(ProjectZed.dustIron, 2));
		mapVanilla.put(new ItemStack(Blocks.gold_ore, 1), new ItemStack(ProjectZed.dustGold, 2));
		mapVanilla.put(new ItemStack(Items.iron_ingot, 1), new ItemStack(ProjectZed.dustIron, 1));
		mapVanilla.put(new ItemStack(Items.gold_ingot, 1), new ItemStack(ProjectZed.dustGold, 1));

		// Fall back mapping
		mapModded.put("oreGlow", "dustGlow");
		mapModded.put("ingotGlow", "dustGlow");
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

		initEntries();
	}

	private static void initEntries() {
		mapSet = mapModded.entrySet();
	}
	
	public static ItemStack crusherList(ItemStack stack) {
		boolean flag = false;
		ItemStack temp = null;

		/* First attempt to see we have data handling for the given stack
		 * in the vanilla mapping, if not continue and use the fallback mapping
		 * (modded).
		 */
		for (ItemStack currentStack : mapVanilla.keySet()) {
			if (stack.getItem() == currentStack.getItem()) {
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

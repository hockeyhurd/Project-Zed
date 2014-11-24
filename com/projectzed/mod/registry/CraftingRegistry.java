package com.projectzed.mod.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.projectzed.mod.ProjectZed;

/**
 * Registry for all crafting recipes. 
 * @author hockeyhurd
 * @version Nov 9, 2014
 */
public class CraftingRegistry {

	private List<ShapelessOreRecipe> shapelessList;
	private List<ShapedOreRecipe> shapedList;
	private static CraftingRegistry reg = new CraftingRegistry();
	
	private CraftingRegistry() {
		shapelessList = new ArrayList<ShapelessOreRecipe>();
		shapedList = new ArrayList<ShapedOreRecipe>();
	}
	
	/**
	 * Get the instance of this class in its static way.
	 * @return static instance of this class.
	 */
	public static CraftingRegistry instance() {
		return reg;
	}
	
	/**
	 * Get the shapeless crafting list created during init method call.
	 * @return shapeless crafting list.
	 */
	public List<ShapelessOreRecipe> getShapelessList() {
		return reg.shapelessList;
	}
	
	/**
	 * Get the shaped crafting list created during the init method call.
	 * @return shaped crafting list.
	 */
	public List<ShapedOreRecipe> getShapedList() {
		return reg.shapedList;
	}
	
	/**
	 * Method once called, will handle the initializing of all crafting recipes.
	 */
	public void init() {
		initShapeless();
		initShaped();
	}
	
	/**
	 * Sub-init method for init'ing shapeless crafting recipes into list.
	 */
	private void initShapeless() {
		reg.shapelessList.add(createShapelessRecipe(ProjectZed.screw, 27, "ingotIron", "ingotIron", "ingotIron"));
	}
	
	/**
	 * Sub-init method for init'ing shapeless crafting recipes into list.
	 */
	private void initShaped() {
		// Blocks:
		reg.shapedList.add(createShapedRecipe(ProjectZed.fabricationTable, 1, "xyx", 'x', Blocks.chest, 'y', Blocks.crafting_table));
		reg.shapedList.add(createShapedRecipe(ProjectZed.thickenedGlass, 4, "xyx", "yzy", "xyx", 'x', "ingotTitanium", 'y', "blockGlass", 'z', "stone"));
		reg.shapedList.add(createShapedRecipe(ProjectZed.energyPipeRed, 8, "xyx", 'x', "ingotTitanium", 'y', Items.redstone));
		reg.shapedList.add(createShapedRecipe(ProjectZed.energyPipeOrange, 8, "xyx", 'x', "ingotTitanium", 'y', "dustCopper"));
		reg.shapedList.add(createShapedRecipe(ProjectZed.energyPipeClear, 4, "xyx", "yzy", "xyx", 'x', "ingotTitanium", 'y', ProjectZed.energyPipeOrange, 'z', ProjectZed.thickenedGlass));
		
		// Machine stuff:
		reg.shapedList.add(createShapedRecipe(ProjectZed.solarArray, 1, "aba", "cdc", "aea", 'a', "ingotTitanium", 'b', "gemDiamond", 'c', ProjectZed.screw, 'd', ProjectZed.machineContainer, 'e', ProjectZed.energyPipeClear));
		reg.shapedList.add(createShapedRecipe(ProjectZed.furnaceGen, 1, "aba", "aca", "ada", 'a', "cobblestone", 'b', Blocks.furnace, 'c', ProjectZed.machineContainer, 'd', ProjectZed.screw));
		reg.shapedList.add(createShapedRecipe(ProjectZed.machineContainer, 1, "xyx", "yzy", "xyx", 'x', ProjectZed.screw, 'y', "plateAluminium", 'z', "ingotTitanium"));
		reg.shapedList.add(createShapedRecipe(ProjectZed.gearAluminium, 1, " x ", "xyx", " x ", 'x', "ingotAluminium", 'y', "ingotIron"));
		reg.shapedList.add(createShapedRecipe(ProjectZed.industrialFurnace, 1, "bab", "cdc", "efe", 'a', Blocks.furnace, 'b', "ingotTitanium", 'c', ProjectZed.screw, 'd', ProjectZed.machineContainer, 'e', ProjectZed.gearAluminium, 'f', ProjectZed.energyPipeRed));
		reg.shapedList.add(createShapedRecipe(ProjectZed.industrialCrusher, 1, "bab", "cdc", "efe", 'a', Items.iron_pickaxe, 'b', "ingotTitanium", 'c', ProjectZed.screw, 'd', ProjectZed.machineContainer, 'e', ProjectZed.gearAluminium, 'f', ProjectZed.energyPipeRed));
	}
	
	/**
	 * Method used for creating a new shapeless crafting recipes.
	 * @param item = item output from said recipe.
	 * @param amount = amount of items to receive.
	 * @param objects = array of objects for how crafting recipe essentially should look.
	 * @return created shapeless recipe object if successful, else return null.
	 */
	private static ShapelessOreRecipe createShapelessRecipe(Item item, int amount, Object... objects) {
		return item == null || objects == null || objects.length < 1 ? null : new ShapelessOreRecipe(new ItemStack(item, amount), objects);
	}
	
	/**
	 * Method used for creating a new shapeless crafting recipes.
	 * @param block = block output from said recipe.
	 * @param amount = amount of items to receive.
	 * @param objects = array of objects for how crafting recipe essentially should look.
	 * @return created shapeless recipe object if successful, else return null.
	 */
	private static ShapelessOreRecipe createShapelessRecipe(Block block, int amount, Object... objects) {
		return block == null || objects == null || objects.length < 1 ? null : new ShapelessOreRecipe(new ItemStack(block, amount), objects);
	}
	
	/**
	 * Method used for creating a new shaped crafting recipe.
	 * @param item = item output from said recipe.
	 * @param amount = amount of items to receive.
	 * @param objects = array of objects for how crafting recipe essentially should look.
	 * @return created shaped recipe object if successful, else return null.
	 */
	private static ShapedOreRecipe createShapedRecipe(Item item, int amount, Object... objects) {
		return item == null || objects == null || objects.length < 4 ? null : new ShapedOreRecipe(new ItemStack(item, amount), objects);
	}
	
	/**
	 * Method used for creating a new shaped crafting recipe.
	 * @param block = block output from said recipe.
	 * @param amount = amount of items to receive.
	 * @param objects = array of objects for how crafting recipe essentially should look.
	 * @return created shaped recipe object if successful, else return null.
	 */
	private static ShapedOreRecipe createShapedRecipe(Block block, int amount, Object... objects) {
		return block == null || objects == null || objects.length < 4 ? null : new ShapedOreRecipe(new ItemStack(block, amount), objects);
	}

}

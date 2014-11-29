package com.projectzed.mod.registry;

import static com.projectzed.mod.ProjectZed.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

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
		reg.shapelessList.add(createShapelessRecipe(screw, 27, "ingotIron", "ingotIron", "ingotIron"));
	}
	
	/**
	 * Sub-init method for init'ing shapeless crafting recipes into list.
	 */
	private void initShaped() {
		// Items:
		reg.shapedList.add(createShapedRecipe(conductiveCoil, 1, "xyx", "yxy", "xyx", 'x', "ingotCopper", 'y', Items.redstone));
		
		// Blocks:
		reg.shapedList.add(createShapedRecipe(fabricationTable, 1, "xyx", 'x', Blocks.chest, 'y', Blocks.crafting_table));
		reg.shapedList.add(createShapedRecipe(thickenedGlass, 4, "xyx", "yzy", "xyx", 'x', "ingotTitanium", 'y', "blockGlass", 'z', "stone"));
		reg.shapedList.add(createShapedRecipe(energyPipeRed, 8, "xyx", 'x', "ingotTitanium", 'y', Items.redstone));
		reg.shapedList.add(createShapedRecipe(energyPipeOrange, 8, "xyx", 'x', "ingotTitanium", 'y', "dustCopper"));
		reg.shapedList.add(createShapedRecipe(energyPipeClear, 4, "xyx", "yzy", "xyx", 'x', "ingotTitanium", 'y', energyPipeOrange, 'z', thickenedGlass));
		
		// Machine stuff:
		reg.shapedList.add(createShapedRecipe(solarArray, 1, "aba", "cdc", "aea", 'a', "ingotTitanium", 'b', "gemDiamond", 'c', screw, 'd', machineContainer, 'e', conductiveCoil));
		reg.shapedList.add(createShapedRecipe(furnaceGen, 1, "aba", "aca", "ada", 'a', "cobblestone", 'b', Blocks.furnace, 'c', machineContainer, 'd', screw));
		reg.shapedList.add(createShapedRecipe(machineContainer, 1, "xyx", "yzy", "xyx", 'x', screw, 'y', "plateAluminium", 'z', "ingotTitanium"));
		reg.shapedList.add(createShapedRecipe(gearAluminium, 1, " x ", "xyx", " x ", 'x', "ingotAluminium", 'y', "ingotIron"));
		reg.shapedList.add(createShapedRecipe(industrialFurnace, 1, "bab", "cdc", "efe", 'a', Blocks.furnace, 'b', "ingotTitanium", 'c', screw, 'd', machineContainer, 'e', gearAluminium, 'f', conductiveCoil));
		reg.shapedList.add(createShapedRecipe(industrialCrusher, 1, "bab", "cdc", "efe", 'a', Items.iron_pickaxe, 'b', "ingotTitanium", 'c', screw, 'd', machineContainer, 'e', gearAluminium, 'f', conductiveCoil));
		reg.shapedList.add(createShapedRecipe(industrialLumberMill, 1, "bab", "cdc", "efe", 'a', Items.iron_axe, 'b', "ingotTitanium", 'c', screw, 'd', machineContainer, 'e', gearAluminium, 'f', conductiveCoil));
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

package com.projectzed.mod.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
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
	
	public static CraftingRegistry instance() {
		return reg;
	}
	
	public List<ShapelessOreRecipe> getShapelessList() {
		return reg.shapelessList;
	}
	
	public List<ShapedOreRecipe> getShapedList() {
		return reg.shapedList;
	}
	
	public void init() {
		initShapeless();
		initShaped();
	}
	
	private void initShapeless() {
		reg.shapelessList.add(createShapelessRecipe(ProjectZed.energyPipe, 8, "ingotTitanium", Items.redstone, "ingotTitanium"));
	}
	
	private void initShaped() {
	}
	
	private static ShapelessOreRecipe createShapelessRecipe(Item item, int amount, Object... objects) {
		return item == null || objects == null || objects.length < 1 ? null : new ShapelessOreRecipe(new ItemStack(item, amount), objects);
	}
	
	private static ShapelessOreRecipe createShapelessRecipe(Block block, int amount, Object... objects) {
		return block == null || objects == null || objects.length < 1 ? null : new ShapelessOreRecipe(new ItemStack(block, amount), objects);
	}
	
	private static ShapedOreRecipe createShapedRecipe(Item item, int amount, Object... objects) {
		return item == null || objects == null || objects.length < 4 ? null : new ShapedOreRecipe(new ItemStack(item, amount), objects);
	}
	
	private static ShapedOreRecipe createShapedRecipe(Block block, int amount, Object... objects) {
		return block == null || objects == null || objects.length < 4 ? null : new ShapedOreRecipe(new ItemStack(block, amount), objects);
	}

}

package com.projectzed.mod.nei;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.recipe.ShapedRecipeHandler;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.gui.GuiMachine;
import com.projectzed.mod.registry.CrusherRecipesRegistry;

/**
 * Class containing nei compatibly recipe manager.
 * 
 * @author hockeyhurd
 * @version Jan 27, 2015
 */
public class NEIIndustrialCrusherRecipeManager extends ShapedRecipeHandler {

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiMachine.class;
	}
	
	@Override
	public String getRecipeName() {
		return "Industrial Crusher";
	}
	
	@Override
	public String getGuiTexture() {
		return ProjectZed.assetDir + "textures/gui/GuiMachine_generic.png";
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result) {
		Set<ItemStack> keys = getStackKeyFromValue(result);
		if (CrusherRecipesRegistry.crusherList(result) != null && CrusherRecipesRegistry.getMap().containsValue(result) && keys != null && keys.size() > 0) {
			for (ItemStack stack : keys) {
				Object[] o = new Object[] { stack };
				this.arecipes.add(new CachedShapedRecipe(41, 21, o, result));
				System.out.println("Added: " + o + ", " + result);
			}
		}
		
	}
	
	private Set<ItemStack> getStackKeyFromValue(ItemStack value) {
		Set<ItemStack> keys = new HashSet<ItemStack>();
		for (Entry<ItemStack, ItemStack> e : CrusherRecipesRegistry.getMap().entrySet()) {
			if (value == e.getValue()) keys.add(e.getKey());
		}
		
		return keys;
	}
	
}

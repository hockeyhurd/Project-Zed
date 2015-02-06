/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.nei;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import codechicken.nei.NEIServerUtils;

import com.projectzed.mod.registry.MetalPressRecipesRegistry;

/**
 * 
 * 
 * @author hockeyhurd
 * @version Feb 5, 2015
 */
public class NEIIndustrialMetalPressRecipeManager extends NEIIndustrialFurnaceRecipeManager {

	@Override
	public String getRecipeName() {
		return "Industrial Metal Press";
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("smelting") && getClass() == NEIIndustrialMetalPressRecipeManager.class) {
			Map<ItemStack, ItemStack> recipes = MetalPressRecipesRegistry.getMap();
			for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
				arecipes.add(new SmeltingPair(recipe.getKey(), recipe.getValue()));
			}
		}
		
		else super.loadCraftingRecipes(outputId, results);
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		Map<ItemStack, ItemStack> recipes = MetalPressRecipesRegistry.getMap();
		for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
			if (NEIServerUtils.areStacksSameType(recipe.getValue(), result)) arecipes.add(new SmeltingPair(recipe.getKey(), recipe.getValue()));
		}
	}

	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if (inputId.equals("fuel") && getClass() == NEIIndustrialMetalPressRecipeManager.class) 
		loadCraftingRecipes("smelting");
		else super.loadUsageRecipes(inputId, ingredients);
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		
		// Try adding said ingrediant to crusher registry.
		ItemStack stack = MetalPressRecipesRegistry.pressList(ingredient);
		if (stack == null) return;
		
		Map<ItemStack, ItemStack> recipes = MetalPressRecipesRegistry.getMap();
		for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
			if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey(), ingredient)) {
				SmeltingPair arecipe = new SmeltingPair(recipe.getKey(), recipe.getValue());
				arecipe.setIngredientPermutation(Arrays.asList(arecipe.ingred), ingredient);
				arecipes.add(arecipe);
			}
		}
	}
	
}

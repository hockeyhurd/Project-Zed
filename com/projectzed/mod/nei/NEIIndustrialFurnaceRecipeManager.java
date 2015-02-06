/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 */
package com.projectzed.mod.nei;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.gui.GuiMachine;

/**
 * Class containing code for NEI integrated recipe handler.
 * <br><bold>NOTE: </bold>This class uses many reference to the standard {@link codechicken.nei.recipe.FurnaceRecipeHandler FurnaceRecipeHandler} class.
 * 
 * @author hockeyhurd
 * @version Feb 3, 2015
 */
public class NEIIndustrialFurnaceRecipeManager extends TemplateRecipeHandler {

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiMachine.class;
	}

	@Override
	public String getRecipeName() {
		return "Industrial Furnace";
	}

	@Override
	public String getGuiTexture() {
		return ProjectZed.assetDir + "textures/gui/GuiMachine_generic.png";
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "smelting"));
	}

	protected class SmeltingPair extends CachedRecipe {
		public SmeltingPair(ItemStack ingred, ItemStack result) {
			ingred.stackSize = 1;
			this.ingred = new PositionedStack(ingred, 36, 10);
			this.result = new PositionedStack(result, 116, 10);
		}

		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 48, Arrays.asList(ingred));
		}

		public PositionedStack getResult() {
			return result;
		}

		public PositionedStack getOtherStack() {
			return null;
		}

		PositionedStack ingred;
		PositionedStack result;
	}

	private static class FuelPair {
		FuelPair(ItemStack ingred, int burnTime) {
			this.stack = new PositionedStack(ingred, 51, 42, false);
			this.burnTime = burnTime;
		}

		PositionedStack stack;
		int burnTime;
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return super.newInstance();
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("smelting") && getClass() == NEIIndustrialFurnaceRecipeManager.class) {
			Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();
			for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
				arecipes.add(new SmeltingPair(recipe.getKey(), recipe.getValue()));
		}
		else super.loadCraftingRecipes(outputId, results);
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();
		for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
			if (NEIServerUtils.areStacksSameType(recipe.getValue(), result)) arecipes.add(new SmeltingPair(recipe.getKey(), recipe.getValue()));
	}

	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if (inputId.equals("fuel") && getClass() == NEIIndustrialFurnaceRecipeManager.class) 
		loadCraftingRecipes("smelting");
		else super.loadUsageRecipes(inputId, ingredients);
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();
		for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
			if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey(), ingredient)) {
				SmeltingPair arecipe = new SmeltingPair(recipe.getKey(), recipe.getValue());
				arecipe.setIngredientPermutation(Arrays.asList(arecipe.ingred), ingredient);
				arecipes.add(arecipe);
			}
	}

	@Override
	public void drawExtras(int recipe) {
		drawProgressBar(72, 10, 176, 14, 24, 16, 48, 0);
		drawProgressBar(2, 50, 0, 170, 162, 15, 48 * 2, 0);
	}

	@Override
	public String getOverlayIdentifier() {
		return "smelting";
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 */
package com.projectzed.mod.nei;

import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.projectzed.mod.ProjectZed;

/**
 * 
 * 
 * @author hockeyhurd
 * @version Feb 5, 2015
 */
public class NEIFabricationTableRecipeManager extends TemplateRecipeHandler {

	public class CachedShapedRecipe extends CachedRecipe {
		public ArrayList<PositionedStack> ingredients;
		public PositionedStack result;

		public CachedShapedRecipe(int width, int height, Object[] items, ItemStack out) {
			result = new PositionedStack(out, 119, 24);
			ingredients = new ArrayList<PositionedStack>();
			setIngredients(width, height, items);
		}

		public CachedShapedRecipe(ShapedRecipes recipe) {
			this(recipe.recipeWidth, recipe.recipeHeight, recipe.recipeItems, recipe.getRecipeOutput());
		}

		/**
		 * @param width
		 * @param height
		 * @param items an ItemStack[] or ItemStack[][]
		 */
		public void setIngredients(int width, int height, Object[] items) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (items[y * width + x] == null) continue;

					PositionedStack stack = new PositionedStack(items[y * width + x], 66 + x * 18, 5 + y * 18, false);
					stack.setMaxSize(1);
					ingredients.add(stack);
				}
			}
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 20, ingredients);
		}

		public PositionedStack getResult() {
			return result;
		}

		public void computeVisuals() {
			for (PositionedStack p : ingredients)
				p.generatePermutations();
		}
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), "crafting"));
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiCrafting.class;
	}

	@Override
	public String getRecipeName() {
		return "Fabrication Table";
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("crafting") && getClass() == NEIFabricationTableRecipeManager.class) {
			for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
				CachedShapedRecipe recipe = null;
				if (irecipe instanceof ShapedRecipes) recipe = new CachedShapedRecipe((ShapedRecipes) irecipe);
				else if (irecipe instanceof ShapedOreRecipe) recipe = forgeShapedRecipe((ShapedOreRecipe) irecipe);

				if (recipe == null) continue;

				recipe.computeVisuals();
				arecipes.add(recipe);
			}
		}
		else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
			if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
				CachedShapedRecipe recipe = null;
				if (irecipe instanceof ShapedRecipes) recipe = new CachedShapedRecipe((ShapedRecipes) irecipe);
				else if (irecipe instanceof ShapedOreRecipe) recipe = forgeShapedRecipe((ShapedOreRecipe) irecipe);

				if (recipe == null) continue;

				recipe.computeVisuals();
				arecipes.add(recipe);
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
			CachedShapedRecipe recipe = null;
			if (irecipe instanceof ShapedRecipes) recipe = new CachedShapedRecipe((ShapedRecipes) irecipe);
			else if (irecipe instanceof ShapedOreRecipe) recipe = forgeShapedRecipe((ShapedOreRecipe) irecipe);

			if (recipe == null || !recipe.contains(recipe.ingredients, ingredient.getItem())) continue;

			recipe.computeVisuals();
			if (recipe.contains(recipe.ingredients, ingredient)) {
				recipe.setIngredientPermutation(recipe.ingredients, ingredient);
				arecipes.add(recipe);
			}
		}
	}

	public CachedShapedRecipe forgeShapedRecipe(ShapedOreRecipe recipe) {
		int width;
		int height;
		try {
			width = getField(ShapedOreRecipe.class, Integer.class, recipe, 4);
			height = getField(ShapedOreRecipe.class, Integer.class, recipe, 5);
		}
		catch (Exception e) {
			NEIClientConfig.logger.error("Error loading recipe", e);
			return null;
		}

		Object[] items = recipe.getInput();
		for (Object item : items)
			if (item instanceof List && ((List<?>) item).isEmpty()) 
				return null;

		return new CachedShapedRecipe(width, height, items, recipe.getRecipeOutput());
	}

	@Override
	public String getGuiTexture() {
		return ProjectZed.assetDir + "textures/gui/GuiFabricationTable.png";
	}

	@Override
	public String getOverlayIdentifier() {
		return "Fabrication Table";
	}

	public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
		return super.hasOverlay(gui, container, recipe) || isRecipe2x2(recipe) && RecipeInfo.hasDefaultOverlay(gui, "crafting2x2");
	}

	@Override
	public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
		IRecipeOverlayRenderer renderer = super.getOverlayRenderer(gui, recipe);
		if (renderer != null) return renderer;

		IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, "crafting2x2");
		if (positioner == null) return null;
		return new DefaultOverlayRenderer(getIngredientStacks(recipe), positioner);
	}

	@Override
	public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
		IOverlayHandler handler = super.getOverlayHandler(gui, recipe);
		if (handler != null) return handler;

		return RecipeInfo.getOverlayHandler(gui, "crafting2x2");
	}

	public boolean isRecipe2x2(int recipe) {
		for (PositionedStack stack : getIngredientStacks(recipe))
			if (stack.relx > 43 || stack.rely > 24) return false;

		return true;
	}

	public static <T> T getField(Class<?> class1, Class<T> fieldType, Object instance, int fieldIndex) throws IllegalArgumentException,
			IllegalAccessException {
		Field field = class1.getDeclaredFields()[fieldIndex];
		field.setAccessible(true);
		return (T) field.get(instance);
	}

}

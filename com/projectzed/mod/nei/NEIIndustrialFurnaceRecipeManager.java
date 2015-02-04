/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.nei;

import java.util.List;
import java.util.Set;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.ShapedRecipeHandler.CachedShapedRecipe;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.gui.GuiMachine;

/**
 * 
 * 
 * @author hockeyhurd
 * @version Feb 3, 2015
 */
public class NEIIndustrialFurnaceRecipeManager extends ShapedRecipeHandler {

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
	public void loadCraftingRecipes(ItemStack result) {
		for (IRecipe irecipe : (List<IRecipe>) FurnaceRecipes.smelting().getSmeltingList()) {
			CachedShapedRecipe recipe = null;
			if (irecipe instanceof ShapedRecipes) recipe = new CachedShapedRecipe((ShapedRecipes) irecipe);
			else if (irecipe instanceof ShapedOreRecipe) recipe = forgeShapedRecipe((ShapedOreRecipe) irecipe);

			if (recipe == null) continue;

			recipe.computeVisuals();	
			arecipes.add(recipe);
		}
	}

}

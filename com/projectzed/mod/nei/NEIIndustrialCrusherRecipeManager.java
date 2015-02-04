/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
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

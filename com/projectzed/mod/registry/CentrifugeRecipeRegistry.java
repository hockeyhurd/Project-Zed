/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.util.StackMapper;

/**
 * Class containing code for initializing the centrifuge's smelting recipe list.
 * <br>NOTE: This class was closely followed to PulverizerRecipes.java by author hockeyhurd.
 * <br>For more info on this click here to view in repo: click <a href="http://goo.gl/L7oiKb">here</a>.
 * 
 * @author hockeyhurd
 * @version Dec 30, 2014
 */
public class CentrifugeRecipeRegistry {

	private static HashMap<StackMapper<ItemStack>, ItemStack> mapVanilla;
	private static HashMap<StackMapper<String>, String> mapModdedTry;
	private static Set<Entry<StackMapper<String>, String>> mapSet;
	
	private CentrifugeRecipeRegistry() {
	}
	
	/**
	 * Method used to init all mappings and recipes.
	 */
	public static void init() {
		mapVanilla = new HashMap<StackMapper<ItemStack>, ItemStack>();
		mapModdedTry = new HashMap<StackMapper<String>, String>();
		
		mapVanilla.put(createStack(new ItemStack(ProjectZed.dustUranium, 1), new ItemStack(ProjectZed.emptyFuelRod, 1)), new ItemStack(ProjectZed.fullFuelRod, 1, 9));
		mapVanilla.put(createStack(new ItemStack(ProjectZed.dustUranium, 1), new ItemStack(ProjectZed.fullFuelRod, 1, 9)), new ItemStack(ProjectZed.fullFuelRod, 1, 8));
		mapVanilla.put(createStack(new ItemStack(ProjectZed.dustUranium, 1), new ItemStack(ProjectZed.fullFuelRod, 1, 8)), new ItemStack(ProjectZed.fullFuelRod, 1, 7));
		mapVanilla.put(createStack(new ItemStack(ProjectZed.dustUranium, 1), new ItemStack(ProjectZed.fullFuelRod, 1, 7)), new ItemStack(ProjectZed.fullFuelRod, 1, 6));
		mapVanilla.put(createStack(new ItemStack(ProjectZed.dustUranium, 1), new ItemStack(ProjectZed.fullFuelRod, 1, 6)), new ItemStack(ProjectZed.fullFuelRod, 1, 5));
		mapVanilla.put(createStack(new ItemStack(ProjectZed.dustUranium, 1), new ItemStack(ProjectZed.fullFuelRod, 1, 5)), new ItemStack(ProjectZed.fullFuelRod, 1, 4));
		mapVanilla.put(createStack(new ItemStack(ProjectZed.dustUranium, 1), new ItemStack(ProjectZed.fullFuelRod, 1, 4)), new ItemStack(ProjectZed.fullFuelRod, 1, 3));
		mapVanilla.put(createStack(new ItemStack(ProjectZed.dustUranium, 1), new ItemStack(ProjectZed.fullFuelRod, 1, 3)), new ItemStack(ProjectZed.fullFuelRod, 1, 2));
		mapVanilla.put(createStack(new ItemStack(ProjectZed.dustUranium, 1), new ItemStack(ProjectZed.fullFuelRod, 1, 2)), new ItemStack(ProjectZed.fullFuelRod, 1, 1));
		mapVanilla.put(createStack(new ItemStack(ProjectZed.dustUranium, 1), new ItemStack(ProjectZed.fullFuelRod, 1, 1)), new ItemStack(ProjectZed.fullFuelRod, 1, 0));
		
		initEntries();
	}
	
	/**
	 * Method used to inint mapModded's entry set.
	 */
	private static void initEntries() {
		mapSet = mapModdedTry.entrySet();
	}
	
	/**
	 * Get the attempted size of recipe list.
	 * 
	 * @return size.
	 */
	public static int getMapSize() {
		return mapModdedTry.size();
	}
	
	/**
	 * Function used to create StackMapper object with type ItemStack<br>
	 * from said itemstacks.
	 * 
	 * @param stacks = stacks to add to StackMapper array.
	 * @return new StackMapper object.
	 */
	private static StackMapper<ItemStack> createStack(ItemStack... stacks) {
		return new StackMapper<ItemStack>(stacks);
	}
	
	/**
	 * Function to get offset values from gui set amount.
	 * 
	 * @param stack stack to reference.
	 * @param offset offset (item/block's meta data value).
	 * @return new itemstack with new meta data value.
	 */
	public static ItemStack stackOffset(ItemStack stack, int offset) {
		if (offset <= 0 || offset > 10) return null;
		
		return new ItemStack(stack.getItem(), 1, stack.getItemDamage() - offset + 1);
	}
	
	/**
	 * Static function used to get output of said itemstack from internal
	 * mappings and contacting to/from ore dictionary.
	 * 
	 * @param stack = stact to reference.
	 * @return output as itemstack.
	 */
	public static ItemStack centrifugeList(ItemStack stack, ItemStack stack2) {
		boolean flag = false;
		ItemStack temp = null;

		/* First attempt to see we have data handling for the given stack
		 * in the vanilla mapping, if not continue and use the fallback mapping
		 * (modded).
		 */
		for (StackMapper<ItemStack> mapper : mapVanilla.keySet()) {
			if (mapper.contains(stack) && mapper.contains(stack2)) {
				flag = true;
				temp = mapVanilla.get(mapper);
				break;
			}
		}

		// If found data in vanilla mapping, return now, no need to continue.
		if (flag && temp != null) return temp;

		StackMapper<String> current;
		String current2;	
		for (Entry<StackMapper<String>, String> s : mapSet) {
			current = s.getKey();
			current2 = s.getValue();

			if (current.contains(OreDictionary.getOreName(OreDictionary.getOreID(stack))) && current.contains(OreDictionary.getOreName(OreDictionary.getOreID(stack2)))) {
				flag = true;
				temp = OreDictionary.getOres(current2).get(0);
				break;
			}

		}

		// If found and stored in variable temp while != null, return data.
		if (flag && temp != null) {
			mapVanilla.put(createStack(stack, stack2), temp);
			return temp;
		}
		
		else return (ItemStack) null;
	}

}

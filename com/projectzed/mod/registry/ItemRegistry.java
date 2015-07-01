/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry;

import net.minecraft.item.Item;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Class used for initializing and containing all items that are registered for this mod.
 * 
 * @author hockeyhurd
 * @version Oct 20, 2014
 */
public final class ItemRegistry {

	private static List<Item> items;
	private static List<Item> itemOres;
	private static ItemRegistry reg = new ItemRegistry();
	
	private ItemRegistry() {
		items = new LinkedList<Item>();
		itemOres = new LinkedList<Item>();
	}
	
	/**
	 * Create new instance of this class and register all items with this registry.
	 * @param mainClass = class containing all item objects.
	 */
	public void init(Class<?> mainClass) {
		Field[] fields = mainClass.getFields();
		if (fields.length == 0) return; // if there are no objects declared, nothing to do.
		
		for (Field f : fields) {
			try {
				if (f.get(mainClass) instanceof Item) {
					Item item = (Item) f.get(mainClass); // cast object to a item.
					if (item != null) {
						reg.items.add(item); // add block to list if not null.
						if (item.getUnlocalizedName().toLowerCase().contains("ingot") || item.getUnlocalizedName().toLowerCase().contains("dust")
								|| item.getUnlocalizedName().toLowerCase().contains("nugget")) reg.itemOres.add(item);
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		
	}
	
	/**
	 * Gets the instance of this class.
	 * @return class instance.
	 */
	public static ItemRegistry instance() {
		return reg;
	}

	/**
	 * Gets the list of registered items.
	 * @return list of items registered in this current instance.
	 */
	public List<Item> getItems() {
		return items;
	}
	
	/**
	 * Gets the list for ore dictionary.
	 * @return list of items to be registered in ore dictionary.
	 */
	public List<Item> getItemOres() {
		return itemOres;
	}
	
	/**
	 * Gets the item by name specified.
	 * @param name = name of item to find.
	 * @return item if found, else null.
	 */
	public Item getItemByName(String name) {
		Item item = null;
		if (reg == null || reg.items == null || reg.items.size() == 0) return null; // if null or no objects, return null.
		
		for (Item i : reg.items) {
			if (i.getUnlocalizedName().equals(name)) { // if found, exit loop.
				item = i;
				break;
			}
		}
		
		return item;
	}
	
	/**
	 * Returns the actual unlocalized name for said block.
	 * @param i = block to get name for.
	 * @return block's unlocalized name.
	 */
	public static String getBlockName(Item i) {
		return i.getUnlocalizedName().substring(5);
	}

}

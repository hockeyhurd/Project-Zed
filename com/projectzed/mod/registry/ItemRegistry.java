/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry;

import com.hockeyhurd.hcorelib.api.item.IHItem;
import com.hockeyhurd.hcorelib.api.util.interfaces.IForgeMod;
import net.minecraft.item.Item;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used for initializing and containing all items that are registered for this mod.
 * 
 * @author hockeyhurd
 * @version Oct 20, 2014
 */
public final class ItemRegistry {

	private static Map<String, IHItem> items;
	private static Map<String, IHItem> itemOres;
	private static ItemRegistry reg = new ItemRegistry();
	
	private ItemRegistry() {
		items = new HashMap<String, IHItem>(0x80, 2.0f / 3.0f);
		itemOres = new HashMap<String, IHItem>(0x80, 2.0f / 3.0f);
	}
	
	/**
	 * Create new instance of this class and register all items with this registry.
	 * @param mainClass = class containing all item objects.
	 */
	public void init(Class<? extends IForgeMod> mainClass) {
		Field[] fields = mainClass.getFields();
		if (fields.length == 0) return; // if there are no objects declared, nothing to do.
		
		for (Field f : fields) {
			try {
				if (f.get(mainClass) instanceof Item) {
					Item item = (Item) f.get(mainClass); // cast object to a item.
					if (item instanceof IHItem) {
						items.put(((IHItem) item).getName(), (IHItem) item); // add block to list if not null.
						if (item.getUnlocalizedName().toLowerCase().contains("ingot") || item.getUnlocalizedName().toLowerCase().contains("dust")
								|| item.getUnlocalizedName().toLowerCase().contains("nugget") || item.getUnlocalizedName().toLowerCase().contains("plate")) {
								itemOres.put(((IHItem) item).getName(), (IHItem) item);
						}
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
	public Map<String, IHItem> getItems() {
		return items;
	}
	
	/**
	 * Gets the list for ore dictionary.
	 * @return list of items to be registered in ore dictionary.
	 */
	public Map<String, IHItem> getItemOres() {
		return itemOres;
	}
	
	/**
	 * Gets the item by name specified.
	 * @param name = name of item to find.
	 * @return item if found, else null.
	 */
	public Item getItemByName(String name) {
		if (name == null || name.isEmpty() || items.isEmpty()) return null;

		final IHItem result = items.get(name);

		return result != null ? result.getItem() : null;
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

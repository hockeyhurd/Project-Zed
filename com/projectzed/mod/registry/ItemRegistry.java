package com.projectzed.mod.registry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;

/**
 * Class used for initializing and containing all items that are registered for this mod.
 * 
 * @author hockeyhurd
 * @version Oct 20, 2014
 */
public class ItemRegistry {

	private static List<Item> items;
	private static ItemRegistry reg = new ItemRegistry();
	
	private ItemRegistry() {
		items = new ArrayList<Item>();
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
					if (item != null) reg.items.add(item); // add block to list if not null.
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

}

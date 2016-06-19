/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import com.hockeyhurd.hcorelib.api.handler.config.AbstractConfigHandler;
import com.hockeyhurd.hcorelib.api.util.AbstractReference;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing code for generating, reading, and writing sorting config.
 * 
 * @author hockeyhurd
 * @version Jan 8, 2015
 */
public class SortingConfigHandler extends AbstractConfigHandler {

	private Item[] sortArray;
	private String[] sortNameArray;
	
	/**
	 * @param event
	 * @param classRef
	 */
	public SortingConfigHandler(FMLPreInitializationEvent event, Class<? extends AbstractReference> classRef) {
		super(event, classRef);
	}

	/**
	 * @param event
	 * @param classRef
	 * @param name
	 */
	public SortingConfigHandler(FMLPreInitializationEvent event, Class<? extends AbstractReference> classRef, String name) {
		super(event, classRef, name);
	}

	/* (non-Javadoc)
	 * @see com.hockeyhurd.api.handler.AbstractConfigHandler#handleConfiguration()
	 */
	@Override
	public void handleConfiguration() {
		this.loadConfig();
		
		if (sortNameArray == null) initSortArray();
		this.config.getStringList("Sort List", "Mappings", sortNameArray, "List of ");
		
		this.saveConfig();
	}
	
	private void initSortArray() {
		int length = Block.REGISTRY.getKeys().size() + Item.REGISTRY.getKeys().size();
		List<Item> itemList = new ArrayList<Item>();
		List<String> itemNameList = new ArrayList<String>();
		
		Block blockCurrent;
		Item itemCurrent;
		
		for (int id = 0; id < length; id++) {
			blockCurrent = Block.getBlockById(id);
			
			if (blockCurrent != null && blockCurrent != Blocks.AIR) {
				itemList.add(Item.getItemFromBlock(blockCurrent));
				itemNameList.add(blockCurrent.getUnlocalizedName().substring(5));
			}
			
			else {
				itemCurrent = Item.getItemById(id);
				
				if (itemCurrent != null) {
					itemList.add(itemCurrent);
					itemNameList.add(itemCurrent.getUnlocalizedName().substring(5));
				}
			}
			
		}
		
		this.sortArray = new Item[itemList.size()];
		for (int i = 0; i < itemList.size(); i++) {
			this.sortArray[i] = itemList.get(i);
		}
		
		this.sortNameArray = new String[itemNameList.size()];
		for (int i = 0; i < itemNameList.size(); i++) {
			this.sortNameArray[i] = itemNameList.get(i);
		}
			
	}
	
	/**
	 * @return sorted array.
	 */
	public Item[] getSortArray() {
		return this.sortArray;
	}

}

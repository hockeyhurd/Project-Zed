package com.projectzed.mod.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import com.hockeyhurd.api.handler.AbstractConfigHandler;
import com.hockeyhurd.api.util.AbstractReference;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

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
		int length = Block.blockRegistry.getKeys().size() + Item.itemRegistry.getKeys().size();
		List<Item> itemList = new ArrayList<Item>();
		List<String> itemNameList = new ArrayList<String>();
		
		Block blockCurrent = null;
		Item itemCurrent = null;
		
		for (int id = 0; id < length; id++) {
			blockCurrent = Block.getBlockById(id);
			
			if (blockCurrent != null && blockCurrent != Blocks.air) {
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

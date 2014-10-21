package com.projectzed.mod.proxy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

import com.hockeyhurd.api.handler.NotifyPlayerOnJoinHandler;
import com.hockeyhurd.api.handler.UpdateHandler;
import com.projectzed.mod.registry.BlockRegistry;
import com.projectzed.mod.registry.ItemRegistry;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.util.Reference;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Common proxy for both client and server.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public class CommonProxy {

	protected UpdateHandler updateHandler;
	protected HashMap<Short, String> map;
	public boolean updateFlag = false;
	
	/**
	 * Default Constructor
	 */
	public CommonProxy() {
	}
	 
	/**
	 * To be used in the ClientProxy and overriden.
	 */
	public void registerRenderInformation() {
	}
	
	/**
	 * Method used for init everything: blocks, items, handlers, etc.
	 */
	public void init() {
		registerBlocks();
		registerItems();
		registerTileEntities();
	}

	private void registerBlocks() {
		for (Block b : BlockRegistry.instance().getBlocks()) {
			if (b != null) GameRegistry.registerBlock(b, b.getUnlocalizedName());
		}
	}
	
	private void registerItems() {
		for (Item i : ItemRegistry.instance().getItems()) {
			if (i != null) GameRegistry.registerItem(i, i.getUnlocalizedName());
		}
	}
	
	private void registerTileEntities() {
		Iterator iter = TileEntityRegistry.instance().getMapping().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Class<? extends TileEntity>, String> entry = (Entry<Class<? extends TileEntity>, String>) iter.next();
			if (entry.getKey() != null && entry.getValue() != null) GameRegistry.registerTileEntity(entry.getKey(), entry.getValue());
		}
	}

	public void registerUpdateHandler() {
		updateHandler = new UpdateHandler(Reference.class);
		updateHandler.check();
		this.map = updateHandler.getMap();
		this.updateFlag = updateHandler.getUpToDate();
		
		// TODO: Temporarily set to false until set-up is done on the update-server first.
		MinecraftForge.EVENT_BUS.register(new NotifyPlayerOnJoinHandler(updateHandler, this.map, Reference.class, this.updateFlag, true, false /*ProjectZed.configHandler.allowUpdating()*/));
	}

}

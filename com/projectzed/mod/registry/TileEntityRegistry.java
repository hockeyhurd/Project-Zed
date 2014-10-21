package com.projectzed.mod.registry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.tileentity.TileEntity;

/**
 * Class container for all registered tile enties in this mod.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public class TileEntityRegistry {

	private HashMap<Class<? extends TileEntity>, String> map;
	private static TileEntityRegistry reg = new TileEntityRegistry();
	
	private TileEntityRegistry() {
		map = new HashMap<Class<? extends TileEntity>, String>();
	}
	
	public void init() {
	}
	
	/**
	 * Get the instance of the registry.
	 * @return instance.
	 */
	public static TileEntityRegistry instance() {
		return reg;
	}
	
	/**
	 * Getter function for getting registered mapping.
	 * @return mapping.
	 */
	public HashMap<Class<? extends TileEntity>, String> getMapping() {
		return reg.map;
	}

}

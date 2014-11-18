package com.projectzed.mod.registry;

import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;

import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeClear;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeOrange;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeRed;
import com.projectzed.mod.tileentity.generator.TileEntityFurnaceGenerator;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCrusher;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialFurnace;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialLumberMill;

/**
 * Class container for all registered tile enties in this mod.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public class TileEntityRegistry {

	private HashMap<Class<? extends TileEntity>, String> map;
	private HashMap<Class<? extends TileEntity>, Integer> idMap;
	private static TileEntityRegistry reg = new TileEntityRegistry();
	
	private TileEntityRegistry() {
		map = new HashMap<Class<? extends TileEntity>, String>();
		idMap = new HashMap<Class<? extends TileEntity>, Integer>();
	}
	
	public void init() {
		reg.map.put(TileEntitySolarArray.class, "solarArrayTileEntity");
		reg.idMap.put(TileEntitySolarArray.class, getNextID());
		
		reg.map.put(TileEntityFurnaceGenerator.class, "furnaceGenTileEntity");
		reg.idMap.put(TileEntityFurnaceGenerator.class, getNextID());
		
		reg.map.put(TileEntityIndustrialFurnace.class, "industrialFurnaceTileEntity");
		reg.idMap.put(TileEntityIndustrialFurnace.class, getNextID());
		
		reg.map.put(TileEntityIndustrialCrusher.class, "industrialCrusherTileEntity");
		reg.idMap.put(TileEntityIndustrialCrusher.class, getNextID());
		
		reg.map.put(TileEntityIndustrialLumberMill.class, "industrialLumberMillTileEntity");
		reg.idMap.put(TileEntityIndustrialLumberMill.class, getNextID());
		
		reg.map.put(TileEntityEnergyPipeRed.class,  "energyPipeTileEntityRed");
		reg.idMap.put(TileEntityEnergyPipeRed.class, getNextID());
		
		reg.map.put(TileEntityEnergyPipeOrange.class,  "energyPipeTileEntityOrange");
		reg.idMap.put(TileEntityEnergyPipeOrange.class, getNextID());
		
		reg.map.put(TileEntityEnergyPipeClear.class,  "energyPipeTileEntityClear");
		reg.idMap.put(TileEntityEnergyPipeClear.class, getNextID());
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
	
	/**
	 * Get the id mapping.
	 * @return id mapping.
	 */
	public HashMap<Class<? extends TileEntity>, Integer> getIDMap() {
		return reg.idMap;
	}
	
	/**
	 * Get the id by class.
	 * @param theClass = class of te.
	 * @return id for correct te.
	 */
	public int getID(Class<? extends TileEntity> theClass) {
		return idMap.containsKey(theClass) ? idMap.get(theClass) : -1;
	}
	
	/**
	 * Gets the next id
	 * @return id.
	 */
	private static int getNextID() {
		return reg.idMap.size() + 1;
	}

}

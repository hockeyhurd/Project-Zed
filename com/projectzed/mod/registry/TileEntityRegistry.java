/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry;

import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;

import com.projectzed.mod.tileentity.TileEntityFabricationTable;
import com.projectzed.mod.tileentity.TileEntityNuclearControlPort;
import com.projectzed.mod.tileentity.TileEntityWickedClearGlass;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankTier0;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankTier1;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankTier2;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankTier3;
import com.projectzed.mod.tileentity.container.TileEntityLiquidNode;
import com.projectzed.mod.tileentity.container.TileEntityNuclearChamberLock;
import com.projectzed.mod.tileentity.container.TileEntityNuclearChamberWall;
import com.projectzed.mod.tileentity.container.TileEntityNuclearIOPort;
import com.projectzed.mod.tileentity.container.TileEntityNuclearPowerPort;
import com.projectzed.mod.tileentity.container.TileEntityReactantCore;
import com.projectzed.mod.tileentity.container.TileEntityReactorGlass;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeClear;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeOrange;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeRed;
import com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductBlue;
import com.projectzed.mod.tileentity.generator.TileEntityFurnaceGenerator;
import com.projectzed.mod.tileentity.generator.TileEntityNuclearController;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCentrifuge;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCrusher;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialFurnace;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialLumberMill;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialMetalPress;
import com.projectzed.mod.tileentity.machine.TileEntityRFBridge;
import com.projectzed.mod.util.ModsLoadedHelper;

/**
 * Class container for all registered tile enties in this mod.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public final class TileEntityRegistry {

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
		
		reg.map.put(TileEntityNuclearController.class, "nuclearControllerTileEntity");
		reg.idMap.put(TileEntityNuclearController.class, getNextID());
		
		reg.map.put(TileEntityFabricationTable.class, "fabricationTableTileEntity");
		reg.idMap.put(TileEntityFabricationTable.class, getNextID());
		
		reg.map.put(TileEntityIndustrialFurnace.class, "industrialFurnaceTileEntity");
		reg.idMap.put(TileEntityIndustrialFurnace.class, getNextID());
		
		reg.map.put(TileEntityIndustrialCrusher.class, "industrialCrusherTileEntity");
		reg.idMap.put(TileEntityIndustrialCrusher.class, getNextID());
		
		reg.map.put(TileEntityIndustrialLumberMill.class, "industrialLumberMillTileEntity");
		reg.idMap.put(TileEntityIndustrialLumberMill.class, getNextID());
		
		reg.map.put(TileEntityIndustrialMetalPress.class, "industrialMetalPressTileEntity");
		reg.idMap.put(TileEntityIndustrialMetalPress.class, getNextID());
		
		reg.map.put(TileEntityIndustrialCentrifuge.class, "industrialCentrifugeTileEntity");
		reg.idMap.put(TileEntityIndustrialCentrifuge.class, getNextID());
		
		reg.map.put(TileEntityEnergyPipeRed.class,  "energyPipeTileEntityRed");
		reg.idMap.put(TileEntityEnergyPipeRed.class, getNextID());
		
		reg.map.put(TileEntityEnergyPipeOrange.class,  "energyPipeTileEntityOrange");
		reg.idMap.put(TileEntityEnergyPipeOrange.class, getNextID());
		
		reg.map.put(TileEntityEnergyPipeClear.class,  "energyPipeTileEntityClear");
		reg.idMap.put(TileEntityEnergyPipeClear.class, getNextID());
		
		if (ModsLoadedHelper.instance().cofhCore) { 
			reg.map.put(TileEntityRFBridge.class, "bridgeRFTileEntity");
			reg.idMap.put(TileEntityRFBridge.class, getNextID());
		}
		
		reg.map.put(TileEntityEnergyBankBase.class, "energyBank");
		reg.idMap.put(TileEntityEnergyBankBase.class, getNextID());
		
		reg.map.put(TileEntityFluidTankTier0.class, "fluidTankTier0");
		reg.idMap.put(TileEntityFluidTankTier0.class, getNextID());
		
		reg.map.put(TileEntityFluidTankTier1.class, "fluidTankTier1");
		reg.idMap.put(TileEntityFluidTankTier1.class, getNextID());
		
		reg.map.put(TileEntityFluidTankTier2.class, "fluidTankTier2");
		reg.idMap.put(TileEntityFluidTankTier2.class, getNextID());
		
		reg.map.put(TileEntityFluidTankTier3.class, "fluidTankTier3");
		reg.idMap.put(TileEntityFluidTankTier3.class, getNextID());
		
		reg.map.put(TileEntityLiquiductBlue.class, "liquiductBlue");
		reg.idMap.put(TileEntityLiquiductBlue.class, getNextID());
		
		reg.map.put(TileEntityLiquidNode.class, "liquiductNode");
		reg.idMap.put(TileEntityLiquidNode.class, getNextID());
		
		reg.map.put(TileEntityReactorGlass.class, "nuclearReactorGlass");
		reg.idMap.put(TileEntityReactorGlass.class, getNextID());
		
		reg.map.put(TileEntityNuclearChamberWall.class, "nuclearChamberWall");
		reg.idMap.put(TileEntityNuclearChamberWall.class, getNextID());
		
		reg.map.put(TileEntityNuclearChamberLock.class, "nuclearChamberLock");
		reg.idMap.put(TileEntityNuclearChamberLock.class, getNextID());

		reg.map.put(TileEntityReactantCore.class, "nuclearReactantCore");
		reg.idMap.put(TileEntityReactantCore.class, getNextID());
		
		reg.map.put(TileEntityNuclearPowerPort.class, "nuclearPowerPort");
		reg.idMap.put(TileEntityNuclearPowerPort.class, getNextID());
		
		reg.map.put(TileEntityNuclearControlPort.class, "nuclearControlPort");
		reg.idMap.put(TileEntityNuclearControlPort.class, getNextID());
		
		reg.map.put(TileEntityNuclearIOPort.class, "nuclearIOPort");
		reg.idMap.put(TileEntityNuclearIOPort.class, getNextID());
		
		reg.map.put(TileEntityWickedClearGlass.class, "wickedClearGlass");
		reg.idMap.put(TileEntityWickedClearGlass.class, getNextID());
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

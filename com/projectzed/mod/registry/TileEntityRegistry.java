/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry;

import com.projectzed.mod.tileentity.TileEntityFabricationTable;
import com.projectzed.mod.tileentity.TileEntityNuclearControlPort;
import com.projectzed.mod.tileentity.TileEntityWickedClearGlass;
import com.projectzed.mod.tileentity.container.*;
import com.projectzed.mod.tileentity.container.pipe.*;
import com.projectzed.mod.tileentity.digger.TileEntityIndustrialQuarry;
import com.projectzed.mod.tileentity.generator.*;
import com.projectzed.mod.tileentity.machine.*;
import com.projectzed.mod.util.ModsLoadedHelper;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;

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

		reg.map.put(TileEntityLavaGenerator.class, "lavaGenTileEntity");
		reg.idMap.put(TileEntityLavaGenerator.class, getNextID());

		reg.map.put(TileEntityPetrolGenerator.class, "petrolGenTileEntity");
		reg.idMap.put(TileEntityPetrolGenerator.class, getNextID());

		reg.map.put(TileEntityHandGenerator.class, "handGenTileEntity");
		reg.idMap.put(TileEntityHandGenerator.class, getNextID());
		
		reg.map.put(TileEntityNuclearController.class, "nuclearControllerTileEntity");
		reg.idMap.put(TileEntityNuclearController.class, getNextID());
		
		reg.map.put(TileEntityFabricationTable.class, "fabricationTableTileEntity");
		reg.idMap.put(TileEntityFabricationTable.class, getNextID());

		reg.map.put(TileEntityStoneCraftingTable.class, "craftingStoneTableTileEntity");
		reg.idMap.put(TileEntityStoneCraftingTable.class, getNextID());

		reg.map.put(TileEntityRefinery.class, "refineryTileEntity");
		reg.idMap.put(TileEntityRefinery.class, getNextID());

		reg.map.put(TileEntityPatternEncoder.class, "patternEncoderTileEntity");
		reg.idMap.put(TileEntityPatternEncoder.class, getNextID());
		
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
		
		reg.map.put(TileEntityIndustrialLoader.class, "industrialLoader");
		reg.idMap.put(TileEntityIndustrialLoader.class, getNextID());
		
		reg.map.put(TileEntityIndustrialEnergizer.class, "industrialEnergizer");
		reg.idMap.put(TileEntityIndustrialEnergizer.class, getNextID());

		reg.map.put(TileEntityIndustrialPlanter.class, "industrialPlanter");
		reg.idMap.put(TileEntityIndustrialPlanter.class, getNextID());

		reg.map.put(TileEntityIndustrialHarvester.class, "industrialHarvester");
		reg.idMap.put(TileEntityIndustrialHarvester.class, getNextID());

		reg.map.put(TileEntityIndustrialStorageUnit.class, "industrialStorageUnit");
		reg.idMap.put(TileEntityIndustrialStorageUnit.class, getNextID());

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

		reg.map.put(TileEntityLiquiductClear.class, "liquiductClear");
		reg.idMap.put(TileEntityLiquiductClear.class, getNextID());
		
		reg.map.put(TileEntityLiquidNode.class, "liquiductNode");
		reg.idMap.put(TileEntityLiquidNode.class, getNextID());
		
		reg.map.put(TileEntityItemPipeGreen.class, "itemPipeGreen");
		reg.idMap.put(TileEntityItemPipeGreen.class, getNextID());
		
		reg.map.put(TileEntityItemPipeGreenOpaque.class, "itemPipeGreenOpaque");
		reg.idMap.put(TileEntityItemPipeGreenOpaque.class, getNextID());
		
		reg.map.put(TileEntityIndustrialQuarry.class, "industrialQuarry");
		reg.idMap.put(TileEntityIndustrialQuarry.class, getNextID());
		
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

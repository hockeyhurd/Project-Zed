/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

import com.projectzed.mod.gui.GuiFabricationTable;
import com.projectzed.mod.gui.GuiMachine;
import com.projectzed.mod.util.Reference;

/**
 * Class config used to load all NEI implemented recipe handlers.
 * 
 * @author hockeyhurd
 * @version Jan 27, 2015
 */
public class NEIProjectZedConfig implements IConfigureNEI {

	/*
	 * (non-Javadoc)
	 * @see codechicken.nei.api.IConfigureNEI#getName()
	 */
	@Override
	public String getName() {
		return Reference.MOD_NAME;
	}

	@Override
	public String getVersion() {
		return Reference.VERSION;
	}

	/*
	 * (non-Javadoc)
	 * @see codechicken.nei.api.IConfigureNEI#loadConfig()
	 */
	@Override
	public void loadConfig() {
		
		API.registerRecipeHandler(new NEIIndustrialFurnaceRecipeManager());
		API.registerUsageHandler(new NEIIndustrialFurnaceRecipeManager());
		API.registerGuiOverlay(GuiMachine.class, "industrialfurnace");
		API.setGuiOffset(GuiMachine.class, 0, 0);
		
		API.registerRecipeHandler(new NEIIndustrialCrusherRecipeManager());
		API.registerUsageHandler(new NEIIndustrialCrusherRecipeManager());
		API.registerGuiOverlay(GuiMachine.class, "industrialcrusher");
		API.setGuiOffset(GuiMachine.class, 0, 0);
		
		// API.registerGuiOverlay(GuiFabricationTable.class, "crafting", 66, 5);
		// API.registerGuiOverlay(GuiFabricationTable.class, "crafting", 5 + 237, 11);
		API.registerGuiOverlay(GuiFabricationTable.class, "crafting", 67, 18);
		API.setGuiOffset(GuiFabricationTable.class, 67, 18);
		// API.registerGuiOverlayHandler(GuiFabricationTable.class, new FabricationTableOverlayHandler(), "crafting");
	}

}

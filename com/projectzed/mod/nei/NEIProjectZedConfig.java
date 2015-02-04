package com.projectzed.mod.nei;

import codechicken.nei.OffsetPositioner;
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
		API.registerRecipeHandler(new NEIIndustrialCrusherRecipeManager());
		API.registerUsageHandler(new NEIIndustrialCrusherRecipeManager());
		API.registerGuiOverlay(GuiMachine.class, "industrialcrusher");
		
		/*API.registerRecipeHandler(new NEIIndustrialFurnaceRecipeManager());
		API.registerUsageHandler(new NEIIndustrialFurnaceRecipeManager());
		API.registerGuiOverlay(GuiMachine.class, "industrialfurnace");*/
		
		// API.registerGuiOverlay(GuiFabricationTable.class, "crafting", 66, 5);
		// API.registerGuiOverlay(GuiFabricationTable.class, "crafting", 5 + 237, 11);
		API.registerGuiOverlay(GuiFabricationTable.class, "crafting", new OffsetPositioner(5 + 237, 5));
	}

}

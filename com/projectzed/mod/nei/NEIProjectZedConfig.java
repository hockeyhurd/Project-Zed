package com.projectzed.mod.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

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
	}

}

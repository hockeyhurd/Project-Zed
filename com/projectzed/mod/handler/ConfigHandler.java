/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import com.hockeyhurd.api.handler.AbstractConfigHandler;
import com.hockeyhurd.api.util.AbstractReference;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Class used for all config handling needs.
 * 
 * @author hockeyhurd
 * @version Oct 29, 2014
 */
public class ConfigHandler extends AbstractConfigHandler {

	// general:
	private boolean debugMode;
	private boolean updateCheck;
	private int maxQuarrySize;
	
	// ore gen
	private boolean genTitanium;
	private boolean genNickel;
	private boolean genAluminium;
	private boolean genCopper;
	private boolean genUranium;

	// Upgrade components:
	private float burnRateModifier;
	private float effRateModifier;
	
	/**
	 * @param event = event.
	 * @param classRef = class reference.
	 */
	public ConfigHandler(FMLPreInitializationEvent event, Class<? extends AbstractReference> classRef) {
		super(event, classRef);
	}

	/* (non-Javadoc)
	 * @see com.hockeyhurd.api.handler.AbstractConfigHandler#handleConfiguration()
	 */
	public void handleConfiguration() {
		this.loadConfig();
		
		// general:
		this.updateCheck = this.getSuggestedConfig().getBoolean("update-check", "General", true, "Ability to turn off update checking.");
		this.debugMode = this.getSuggestedConfig().getBoolean("debug-mode toggle,", "General", false, "Allows displaying of debugging info!");
		this.maxQuarrySize = this.getSuggestedConfig().getInt("max quarry size", "General", 128, 3, 256, "Sets the max-size alloted for quarries to be.");
		
		// ore:
		this.genTitanium = this.getSuggestedConfig().getBoolean("ore titanium", "World Gen", true, "Toggle for generating titanium");
		this.genNickel = this.getSuggestedConfig().getBoolean("ore nickel", "World Gen", true, "Toggle for generating nickel");
		this.genAluminium = this.getSuggestedConfig().getBoolean("ore aluminium", "World Gen", true, "Toggle for generating aluminium");
		this.genCopper = this.getSuggestedConfig().getBoolean("ore copper", "World Gen", true, "Toggle for generating copper");
		this.genUranium = this.getSuggestedConfig().getBoolean("ore uranium", "World Gen", true, "Toggle for generating uranium");

		// Upgrade components:
		this.burnRateModifier = this.getSuggestedConfig()
				.getFloat("energy burn rate modifier (compounded per upgrade)", "Upgrade Components", 1.5f, 1.0f, 100.0f,
						"each upgrade compounds by this burn rate");
		this.effRateModifier = this.getSuggestedConfig()
				.getFloat("machine efficiency rate modifier (compounded per upgrade)", "Upgrade Components", 0.75f, 0.1f, 1.0f,
						"each upgrade compounds by this efficiency rate");
		
		this.saveConfig();
	}
	
	/**
	 * Function returns whether debug mode is allowed.
	 */
	public boolean isDebugMode() {
		return debugMode;
	}
	
	/** 
	 * Function returns whether we are allowed to check for updates.
	 * @return
	 */
	public boolean allowUpdating() {
		return updateCheck;
	}
	
	/**
	 * @return get max quarry size.
	 */
	public int getMaxQuarrySize() {
		return maxQuarrySize;
	}
	
	/**
	 * @return genTitanium flag
	 */
	public boolean genOreTitanium() {
		return genTitanium;
	}
	
	/**
	 * @return genNickel flag
	 */
	public boolean genOreNickel() {
		return genNickel;
	}
	
	/**
	 * @return genAluminium flag
	 */
	public boolean genOreAluminium() {
		return genAluminium;
	}
	
	/**
	 * @return genCopper flag
	 */
	public boolean genOreCopper() {
		return genCopper;
	}
	
	/**
	 * @return genUranium flag
	 */
	public boolean genOreUranium() {
		return genUranium;
	}

	/**
	 * @return burn rate modifier.
	 */
	public float getBurnRateModifier() {
		return burnRateModifier;
	}

	/**
	 * @return efficiency rate modifier.
	 */
	public float getEffRateModifier() {
		return effRateModifier;
	}

}

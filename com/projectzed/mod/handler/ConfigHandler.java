/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import com.hockeyhurd.hcorelib.api.handler.config.AbstractConfigHandler;
import com.hockeyhurd.hcorelib.api.math.Color4f;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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

	// ore gen:
	private boolean genTitanium;
	private boolean genNickel;
	private boolean genAluminium;
	private boolean genCopper;
	private boolean genUranium;

	// fluid gen:
	private boolean genOil;

	// tools:
	private int maxExchangerRadii;
	private int maxDrillRadii;
	private Color4f selectionBoxColor = new Color4f();

	// Upgrade components:
	private float burnRateModifier;
	private float effRateModifier;
	
	/**
	 * @param event event.
	 * @param modID String modID.
	 */
	public ConfigHandler(FMLPreInitializationEvent event, String modID) {
		super(event, modID);
	}

	@Override
	public void handleConfiguration() {
		this.loadConfig();
		
		// general:
		this.updateCheck = this.getSuggestedConfig().getBoolean("update-check", "General", true, "Ability to turn off update checking.");
		this.debugMode = this.getSuggestedConfig().getBoolean("debug-mode toggle,", "General", false, "Allows displaying of debugging info!");
		this.maxQuarrySize = this.getSuggestedConfig().getInt("max quarry size", "General", 128, 3, 256, "Sets the max-size allotted for quarries to be.");

		// ore:
		this.genTitanium = this.getSuggestedConfig().getBoolean("ore titanium", "World Gen", true, "Toggle for generating titanium");
		this.genNickel = this.getSuggestedConfig().getBoolean("ore nickel", "World Gen", true, "Toggle for generating nickel");
		this.genAluminium = this.getSuggestedConfig().getBoolean("ore aluminium", "World Gen", true, "Toggle for generating aluminium");
		this.genCopper = this.getSuggestedConfig().getBoolean("ore copper", "World Gen", true, "Toggle for generating copper");
		this.genUranium = this.getSuggestedConfig().getBoolean("ore uranium", "World Gen", true, "Toggle for generating uranium");

		// fluid:
		this.genOil = this.getSuggestedConfig().getBoolean("fluid oil", "World Gen", true, "Toggle for generating oil");

		// tools:
		this.maxExchangerRadii = this.getSuggestedConfig().getInt("max exchanger radii", "Tools", 5, 1, 15,
				"Sets the max radii allotted for exchanger to be.");
		this.maxDrillRadii = this.getSuggestedConfig().getInt("max drill radii", "Tools", 2, 1, 9, "Sets the max radii allotted for drill to be.");

		int color = this.getSuggestedConfig().getInt("selection box color - red", "Tools", 255, 0, 255,
				"Sets the (red) color of the selection box with the above tools.");

		selectionBoxColor.setR(color);
		// color <<= 0x10;

		color = this.getSuggestedConfig().getInt("selection box color - green", "Tools", 0, 0, 255,
				"Sets the (green) color of the selection box with the above tools.");
		selectionBoxColor.setG(color);

		color = this.getSuggestedConfig().getInt("selection box color - blue", "Tools", 0, 0, 255,
				"Sets the (blue) color of the selection box with the above tools.");
		selectionBoxColor.setB(color);

		// Alpha:
		// color |= (0xff << 0x18);

		// selectionBoxColor = new Color4f(color);

		// Upgrade components:
		this.burnRateModifier = this.getSuggestedConfig()
				.getFloat("energy burn rate modifier (compounded per upgrade)", "Upgrade Components", 1.5f, 1.0f, 100.0f,
						"each upgrade compounds by this burn rate");
		this.effRateModifier = this.getSuggestedConfig()
				.getFloat("machine efficiency rate modifier (compounded per upgrade)", "Upgrade Components", 0.85f, 0.1f, 1.0f,
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
	 * @return genOil flag
	 */
	public boolean genFluidOil() {
		return genOil;
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

	/**
	 * @return max exchanger radii.
	 */
	public int getMaxExchangerRadii() {
		return maxExchangerRadii;
	}

	/**
	 * @return max drill radii.
	 */
	public int getMaxDrillRadii() {
		return maxDrillRadii;
	}

	/**
	 * @return Gets the selection box color.
	 */
	public Color4f getSelectionBoxColor() {
		return selectionBoxColor;
	}
}

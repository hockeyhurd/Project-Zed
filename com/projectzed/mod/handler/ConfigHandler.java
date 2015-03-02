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

	private static boolean debugMode;
	private static boolean updateCheck;
	
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
		
		this.updateCheck = this.getSuggestedConfig().getBoolean("update-check", "General", true, "Ability to turn off update checking.");
		this.debugMode = this.getSuggestedConfig().getBoolean("debug-mode toggle,", "General", false, "Allows displaying of debugging info!");
		
		this.saveConfig();
	}
	
	/**
	 * Function returns whether debug mode is allowed.
	 */
	public static boolean isDebugMode() {
		return debugMode;
	}
	
	/** 
	 * Function returns whether we are allowed to check for updates.
	 * @return
	 */
	public static boolean allowUpdating() {
		return updateCheck;
	}

}

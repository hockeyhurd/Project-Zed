/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.util;

import com.hockeyhurd.hcorelib.api.util.LogHelper;
import cpw.mods.fml.common.Loader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Class used as a helper for determining what mods
 * are loaded so that I can make appropriate changes for my
 * blocks, items, machines, etc.
 * 
 * @author hockeyhurd
 * @version Nov 29, 2014
 */
public class ModsLoadedHelper {

	/** Variable for whether Thermal Expansion Exists */
	public boolean te4Loaded = false;
	
	/** Variable for whether CoFH Core Exists */
	public boolean cofhCore = false;
	
	/** Variable for whether IC2 Exists */
	public boolean ic2Loaded = false;
	
	/** Variable for whether NEI Exists */
	public boolean neiLoaded = false;
	
	/** Mapping containing name and flag of existence. */
	private HashMap<String, Boolean> mapping;
	
	/** static instance of this class */
	private static ModsLoadedHelper mlh = new ModsLoadedHelper(); 
	
	private ModsLoadedHelper() {
	}
	
	/**
	 * Getter for the single instance of this class.
	 * @return class instance object.
	 */
	public static ModsLoadedHelper instance() {
		return mlh;
	}

	/**
	 * Function used to determine whether a mod by id exists.
	 * <br>Shortened version of Loaded.isModLoaded(modid)
	 * @param modid = modid by string name.
	 * @return true if exists, else return false.
	 */
	private boolean isModLoaded(String modid) {
		return Loader.isModLoaded(modid);
	}
	
	/**
	 * Method when called initializes all variables and mappings.
	 */
	public void init() {
		if (isModLoaded("ThermalExpansion")) te4Loaded = true;
		if (isModLoaded("CoFHCore")) cofhCore = true;
		if (isModLoaded("IC2")) ic2Loaded = true;
		if (isModLoaded("NotEnoughItems")) neiLoaded = true;
		
		initMapping();
	}
	
	/**
	 * Method used to log the loading and init of this class.
	 * @param log = logger to use.
	 */
	public void logFindings(LogHelper log) {
		log.info("Detecting other soft-dependent mods.");
		
		Iterator iter = getEntries().iterator();
		do {
			Entry<String, Boolean> current = (Entry<String, Boolean>) iter.next();
			if (current.getValue()) log.info(current.getKey(), "detected! Wrapping into mod!");
			else log.warn(current.getKey(), "not detected!");
		}
		while (iter.hasNext());
		
		log.info("Finished detecting soft-dependent mods.");
	}

	/**
	 * Method used to init the mapping and add objects to said mapping.
	 */
	private void initMapping() {
		mapping = new HashMap<String, Boolean>();
		
		mapping.put("ThermalExpansion", te4Loaded);
		mapping.put("CoFHCore", cofhCore);
		mapping.put("IC2", ic2Loaded);
		mapping.put("NotEnoughItems", neiLoaded);
	}
	
	/**
	 * Function used to get all entries in the set of mapping.
	 * @return entries in mapping if not null, else returns null.
	 */
	private final Set<Entry<String, Boolean>> getEntries() {
		return mlh.mapping.entrySet();
	}

}

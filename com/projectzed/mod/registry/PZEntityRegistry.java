/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry;

import java.util.HashMap;

import net.minecraft.entity.Entity;

import com.projectzed.mod.entity.EntityAtomicBomb;

/**
 * Class registry for all entities. 
 * 
 * @author hockeyhurd
 * @version Mar 19, 2015
 */
public final class PZEntityRegistry {

	private static final PZEntityRegistry reg = new PZEntityRegistry();
	private HashMap<Class<? extends Entity>, String> map;
	private int counter;
	
	private PZEntityRegistry() {
		map = new HashMap<Class<? extends Entity>, String>();
	}
	
	public static PZEntityRegistry instance() {
		return reg;
	}
	
	public int getNextID() {
		return counter++;
	}
	
	public HashMap<Class<? extends Entity>, String> getMap() {
		return map;
	}
	
	public void init() {
		reg.map.put(EntityAtomicBomb.class, "entityAtomicBomb");
	}

}

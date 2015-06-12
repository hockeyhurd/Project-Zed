/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry.interfaces;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Interface to define basis for which tools can have common registry layout.
 * 
 * @author hockeyhurd
 * @version Jun 11, 2015
 */
public interface IToolSetRegistry {
	
	/**
	 * Method initialize block set.
	 */
	void init();
	
	/**
	 * @return set of applicable materials.
	 */
	Set getSet();
	
	/**
	 * @param block block to check.
	 * @return true if block is contained in set, else returns false.
	 */
	boolean setContainsBlock(Block block);
	
	/**
	 * Checks if block is acceptable as a material.
	 * 
	 * @param b block to reference.
	 * @return true if acceptable, else returns false.
	 */
	boolean matContains(Block b);
	
	/**
	 * Checks if material is acceptable.
	 * 
	 * @param mat material to reference.
	 * @return true if acceptable, else returns false.
	 */
	boolean matContains(Material mat);
	
}

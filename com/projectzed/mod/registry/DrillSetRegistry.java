/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.google.common.collect.Sets;

/**
 * Class containing code for drills material set.
 * 
 * @author hockeyhurd
 * @version Mar 30, 2015
 */
public class DrillSetRegistry {
	
	public static Set set;
	public static final Material[] mats = new Material[] {
		Material.rock,	
		Material.iron,	
		Material.grass,
		Material.ground,
		Material.sand,
		Material.piston,
		Material.snow,
	};
	
	private DrillSetRegistry() {
	}
	
	/**
	 * Method initialize block set.
	 */
	public static void init() {
		Iterator iter = Block.blockRegistry.iterator();
		Set<Block> temp = new HashSet<Block>();
		
		while (iter.hasNext()) {
			Block b = (Block) iter.next();
			if (b != null && matContains(b.getMaterial())) temp.add(b);
		}
		
		set = Sets.newHashSet(temp.toArray(new Block[temp.size()]));
	}
	
	/**
	 * Checks if block is acceptable as a material.
	 * 
	 * @param b block to reference.
	 * @return true if acceptable, else returns false.
	 */
	public static boolean matContains(Block b) {
		return matContains(b.getMaterial());
	}
	
	/**
	 * Checks if material is acceptable.
	 * 
	 * @param mat material to reference.
	 * @return true if acceptable, else returns false.
	 */
	public static boolean matContains(Material mat) {
		if (mats == null || mats.length == 0) return false;
		
		for (Material m : mats) {
			if (m == mat) return true;
		}
		
		return false;
	}

}

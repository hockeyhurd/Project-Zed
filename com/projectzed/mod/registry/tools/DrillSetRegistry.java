/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry.tools;

import com.google.common.collect.Sets;
import com.projectzed.mod.registry.interfaces.IToolSetRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Class containing code for drills material set.
 * 
 * @author hockeyhurd
 * @version Mar 30, 2015
 */
public class DrillSetRegistry implements IToolSetRegistry {
	
	private static final DrillSetRegistry reg = new DrillSetRegistry();
	
	private static Set set;
	public static final Material[] mats = new Material[] {
		Material.ROCK,
		Material.IRON,
		Material.ANVIL,
		Material.TNT,
		Material.GOURD,
		Material.GRASS,
		Material.GROUND,
		Material.SAND,
		Material.GLASS,
		Material.SNOW,
		Material.CRAFTED_SNOW,
		Material.PACKED_ICE,
		Material.CLAY,
		Material.PISTON,
		Material.CIRCUITS,
	};
	
	private DrillSetRegistry() {
	}
	
	/**
	 * @return instance of this registry.
	 */
	public static IToolSetRegistry instance() {
		return reg;
	}

	@Override
	public void init() {
		Iterator iter = Block.REGISTRY.iterator();
		Set<Block> temp = new HashSet<Block>();
		
		while (iter.hasNext()) {
			Block b = (Block) iter.next();
			if (b != null && matContains(b.getMaterial(b.getDefaultState()))) temp.add(b);
		}
		
		set = Sets.newHashSet(temp.toArray(new Block[temp.size()]));
	}
	
	@Override
	public Set getSet() {
		return set;
	}

	@Override
	public boolean setContainsBlock(Block block) {
		return set.contains(block);
	}
	
	@Override
	public boolean matContains(Block b) {
		return matContains(b.getMaterial(b.getDefaultState()));
	}
	
	@Override
	public boolean matContains(Material mat) {
		if (mats == null || mats.length == 0) return false;
		
		for (Material m : mats) {
			if (m == mat) return true;
		}
		
		return false;
	}

}

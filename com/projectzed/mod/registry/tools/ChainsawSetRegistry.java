/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry.tools;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.google.common.collect.Sets;
import com.projectzed.mod.registry.interfaces.IToolSetRegistry;

/**
 * Class containing code for chainsaw material set.
 * 
 * @author hockeyhurd
 * @version Jun 11, 2015
 */
public class ChainsawSetRegistry implements IToolSetRegistry {

	private static final ChainsawSetRegistry reg = new ChainsawSetRegistry();
	
	private static Set set;
	public static final Material[] mats = new Material[] {
		Material.wood,
		Material.cloth,
		Material.plants,
		Material.leaves,
		Material.vine,
		Material.web,
	};
	
	private ChainsawSetRegistry() {
	}

	/**
	 * @return instance of this registry.
	 */
	public static IToolSetRegistry instance() {
		return reg;
	}
	
	/* (non-Javadoc)
	 * @see com.projectzed.mod.registry.interfaces.IToolSetRegistry#init()
	 */
	@Override
	public void init() {
		Iterator iter = Block.blockRegistry.iterator();
		Set<Block> temp = new HashSet<Block>();
		
		while (iter.hasNext()) {
			Block b = (Block) iter.next();
			if (b != null && matContains(b.getMaterial())) temp.add(b);
		}
		
		set = Sets.newHashSet(temp.toArray(new Block[temp.size()]));
	}

	/* (non-Javadoc)
	 * @see com.projectzed.mod.registry.interfaces.IToolSetRegistry#getSet()
	 */
	@Override
	public Set getSet() {
		return set;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.registry.interfaces.IToolSetRegistry#setContainsBlock(net.minecraft.block.Block)
	 */
	@Override
	public boolean setContainsBlock(Block block) {
		return set.contains(block);
	}
	
	/* (non-Javadoc)
	 * @see com.projectzed.mod.registry.interfaces.IToolSetRegistry#matContains(net.minecraft.block.Block)
	 */
	@Override
	public boolean matContains(Block b) {
		return matContains(b.getMaterial());
	}

	/* (non-Javadoc)
	 * @see com.projectzed.mod.registry.interfaces.IToolSetRegistry#matContains(net.minecraft.block.material.Material)
	 */
	@Override
	public boolean matContains(Material mat) {
		if (mats == null || mats.length == 0) return false;
		
		for (Material m : mats) {
			if (m == mat) return true;
		}
		
		return false;
	}

}

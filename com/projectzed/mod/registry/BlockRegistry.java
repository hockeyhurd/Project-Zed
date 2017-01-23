/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.registry;

import com.hockeyhurd.hcorelib.api.block.IHBlock;
import com.hockeyhurd.hcorelib.api.util.interfaces.IForgeMod;
import net.minecraft.block.Block;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used for initializing and containing all blocks that are registered for this mod.
 * 
 * @author hockeyhurd
 * @version Oct 20, 2014
 */
public final class BlockRegistry {

	private static Map<String, IHBlock> blocks;
	private static Map<String, IHBlock> blockOres;
	private static BlockRegistry reg = new BlockRegistry();

	private BlockRegistry() {
		blocks = new HashMap<String, IHBlock>(0x80, 2.0f / 3.0f);
		blockOres = new HashMap<String, IHBlock>(0x80, 2.0f / 3.0f);
	}

	/**
	 * Create new instance of this class and register all blocks with this registry.
	 * @param mainClass = class containing all block objects.
	 */
	public void init(Class<? extends IForgeMod> mainClass) {
		Field[] fields = mainClass.getFields();
		if (fields.length == 0) return; // if there are no objects declared, nothing to do.

		for (Field f : fields) {
			try {
				final Object fieldObj = f.get(mainClass);

				if (fieldObj instanceof IHBlock) {
					final IHBlock block = (IHBlock) fieldObj; // cast object to a block.

					blocks.put(block.getName(), block); // add block to list if not null.
					if (block.getBlock().getUnlocalizedName().toLowerCase().contains("ore") || block.getBlock().getUnlocalizedName().toLowerCase()
							.contains("block")) blockOres.put(block.getName(), block);
				}
			}

			catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}

	}

	/**
	 * Gets the instance of this class.
	 * @return class instance.
	 */
	public static BlockRegistry instance() {
		return reg;
	}

	/**
	 * Gets the list of registered blocks.
	 * @return list of blocks registered in this current instance.
	 */
	public Map<String, IHBlock> getBlocks() {
		return blocks;
	}
	
	/**
	 * Gets the list for ore dictionary.
	 * @return list of blocks to be registered in ore dictionary.
	 */
	public Map<String, IHBlock> getOreBlocks() {
		return blockOres;
	}
	
	/**
	 * Gets the block by name specified.
	 *
	 * @param name name of block to find.
	 * @return block if found, else null.
	 */
	public Block getBlockByName(String name) {
		if (name == null || name.isEmpty() || blocks.isEmpty()) return null;

		final IHBlock block = blocks.get(name);

		return block != null ? block.getBlock() : null;
	}
	
	/**
	 * Returns the actual unlocalized name for said block.
	 *
	 * @param b block to get name for.
	 * @return block's unlocalized name.
	 */
	public static String getBlockName(Block b) {
		return b.getUnlocalizedName().substring(5);
	}

}

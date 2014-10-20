package com.projectzed.mod.registry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;

/**
 * Class used for initializing and containing all blocks that are registered for this mod.
 * 
 * @author hockeyhurd
 * @version Oct 20, 2014
 */
public class BlockRegistry {

	private static List<Block> blocks;
	private static BlockRegistry reg = new BlockRegistry();

	private BlockRegistry() {
		blocks = new ArrayList<Block>();
	}

	/**
	 * Create new instance of this class and register all blocks with this registry.
	 * @param mainClass = class containing all block objects.
	 */
	public void init(Class<?> mainClass) {
		Field[] fields = mainClass.getFields();
		if (fields.length == 0) return; // if there are no objects declared, nothing to do.

		for (Field f : fields) {
			try {
				if (f.get(mainClass) instanceof Block) {
					Block block = (Block) f.get(mainClass); // cast object to a block.
					if (block != null) reg.blocks.add(block); // add block to list if not null.
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
	public List<Block> getBlocks() {
		return blocks;
	}
	
	/**
	 * Gets the block by name specified.
	 * @param name = name of block to find.
	 * @return block if found, else null.
	 */
	public Block getBlockByName(String name) {
		Block block = null;
		if (reg == null || reg.blocks == null || reg.blocks.size() == 0) return null; // if null or no objects, return null.
		
		for (Block b : reg.blocks) {
			if (b.getUnlocalizedName().equals(name)) { // if found, exit loop.
				block = b;
				break;
			}
		}
		
		return block;
	}

}

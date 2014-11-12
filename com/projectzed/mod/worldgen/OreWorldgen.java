package com.projectzed.mod.worldgen;

import net.minecraft.block.Block;

import com.hockeyhurd.api.worldgen.AbstractWorldgen;

/**
 * Class used to create new instances of AbstractWorldgen class 
 * for all ores in this mod.
 * <br>@see com.hockeyhurd.api.worldgen.AbstractWorldgen
 * 
 * @author hockeyhurd
 * @version Nov 12, 2014
 */
public class OreWorldgen extends AbstractWorldgen {

	/**
	 * Complete constructor for full control over all generation needs.
	 * @param blockToSpawn = block to spawn in overworld.
	 * @param blockToSpawnNether = block to spawn in nether. 
	 * @param chanceOfSpawn = chance of spawn in overworld.
	 * @param chanceOfSpawnNether = chance of spawn in nether.
	 * @param minVeinSize = min vein size.
	 * @param maxVeinSize = max vein size.
	 * @param minY = min y level.
	 * @param maxY = max y level.
	 */
	public OreWorldgen(Block blockToSpawn, Block blockToSpawnNether, int chanceOfSpawn, int chanceOfSpawnNether, int minVeinSize, int maxVeinSize, int minY, int maxY) {
		super(blockToSpawn, blockToSpawnNether, chanceOfSpawn, chanceOfSpawnNether, minVeinSize, maxVeinSize, minY, maxY);
	}

	/**
	 * Simplified constructor if only dealing with overworld worldgen.
	 * @param blockToSpawn = block to spawn in world.
	 * @param chanceOfSpawn = chance of spawning in world.
	 * @param minVeinSize = min vein size.
	 * @param maxVeinSize = max vein size.
	 * @param minY = min y level.
	 * @param maxY = max y level.
	 */
	public OreWorldgen(Block blockToSpawn, int chanceOfSpawn, int minVeinSize, int maxVeinSize, int minY, int maxY) {
		super(blockToSpawn, chanceOfSpawn, minVeinSize, maxVeinSize, minY, maxY);
	}
	
}

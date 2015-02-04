/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
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

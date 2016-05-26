/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.util;

import com.hockeyhurd.hcorelib.api.math.Vector2;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Util class for storing chunk related data.
 * 
 * @author hockeyhurd
 * @version Jun 11, 2015
 */
public class ChunkPair {

	private final World world;
	private final Chunk chunk;
	private final Vector2<Integer> chunkVec;
	private final Vector3<Integer> chunkVec3;
	
	/**
	 * @param world world to reference.
	 * @param vec chunk location as vector3.
	 */
	public ChunkPair(World world, Vector3<Integer> vec) {
		this.world = world;
		this.chunkVec3 = vec;
		this.chunkVec = new Vector2<Integer>(vec.x, vec.z);
		
		if (world != null) this.chunk = world.getChunkFromBlockCoords(VectorHelper.toBlockPos(vec.x, 0, vec.z));
		else this.chunk = null;
	}
	
	/**
	 * @param world world to reference.
	 * @param vec chunk location as vector2 where 'y' component represents 'z' axis in-game.
	 */
	public ChunkPair(World world, Vector2<Integer> vec) {
		this.world = world;
		this.chunkVec = vec;
		this.chunkVec3 = new Vector3<Integer>(vec.x, 0, vec.y);
		
		if (world != null) this.chunk = world.getChunkFromBlockCoords(VectorHelper.toBlockPos(vec.x, 0, vec.y));
		else this.chunk = null;
	}

	/**
	 * @param vec chunk location as vector2 where 'y' component represents 'z' axis in-game.
	 */
	public ChunkPair(Vector2<Integer> vec) {
		this(null, vec);
	}
	
	/**
	 * @param vec chunk location as vector3.
	 */
	public ChunkPair(Vector3<Integer> vec) {
		this(null, vec);
	}
	
	/**
	 * @return chunk at pair.
	 */
	public Chunk getChunk() {
		return chunk;
	}
	
	/**
	 * @return chunk coordinate.
	 */
	public Vector2<Integer> getChunkPair() {
		return chunkVec.copy();
	}
	
	/**
	 * @return coordinate as vector3.
	 */
	public Vector3<Integer> getNormalizedChunkPair() {
		return chunkVec3.copy();
	}
	
	/**
	 * @return world referenced.
	 */
	public World getWorld() {
		return world;
	}

}

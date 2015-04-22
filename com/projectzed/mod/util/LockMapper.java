/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
/**
 * 
 */
package com.projectzed.mod.util;

import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector4;
import com.projectzed.mod.block.BlockNuclearChamberLock;

/**
 * Class used for mapping out location and value of Nuclear Chamber Lock.
 *
 * @author hockeyhurd
 * @version Dec 27, 2014
 */
@Deprecated
public class LockMapper {
	
	private Vector4<Integer> vec;
	private boolean isMB;
	
	/**
	 * @param vec = location of block represented in vector format.
	 * @param isMB = boolean flag whether is a multi-block structure or nah.
	 */
	public LockMapper(Vector4<Integer> vec, boolean isMB) {
		this.vec = vec;
		this.isMB = isMB;
	}
	
	/**
	 * @return vector or location of this block instance.
	 */
	public Vector4<Integer> getVec() {
		return this.vec;
	}
	
	/**
	 * @return flag whether is a multi-block structure or nah.
	 */
	public boolean isMultiBlockStructure() {
		return this.isMB;
	}
	
	/**
	 * @param world = world object as reference.
	 * @return instance of block from world co-ordinate and this recored vector.
	 */
	public BlockNuclearChamberLock getInstance(World world) {
		return (BlockNuclearChamberLock) world.getBlock(vec.x, vec.y, vec.z);
	}
}

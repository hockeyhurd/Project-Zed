/**
 * 
 */
package com.projectzed.mod.util;

import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.mod.block.BlockNuclearChamberLock;

/**
 * Class used for mapping out location and value of Nuclear Chamber Lock.
 *
 * @author hockeyhurd
 * @version Dec 27, 2014
 */
public class LockMapper {
	
	private Vector4Helper<Integer> vec;
	private boolean isMB;
	
	/**
	 * @param vec = location of block represented in vector format.
	 * @param isMB = boolean flag whether is a multi-block structure or nah.
	 */
	public LockMapper(Vector4Helper<Integer> vec, boolean isMB) {
		this.vec = vec;
		this.isMB = isMB;
	}
	
	/**
	 * @return vector or location of this block instance.
	 */
	public Vector4Helper<Integer> getVec() {
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

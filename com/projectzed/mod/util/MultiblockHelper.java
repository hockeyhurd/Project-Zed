/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.hockeyhurd.api.math.Vector4;
import com.hockeyhurd.api.util.BlockHelper;

/**
 * Class mostly used to help with determining what to render with multiblock
 * configurations.
 * 
 * @author hockeyhurd
 * @version Feb 26, 2015
 */
public class MultiblockHelper {

	private World world;
	private Vector4<Integer> vec;
	private Block[] blocks;
	private BlockHelper bh;
	private boolean[] connections = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
	private boolean xAxis, yAxis, zAxis;
	private int counter = 0;
	
	/**
	 * @param world world object as reference.
	 * @param vec global position of current block.
	 * @param blocks white-listed blocks.
	 */
	public MultiblockHelper(World world, Vector4<Integer> vec, Block[] blocks) {
		this.world = world;
		this.vec = vec;
		this.bh = new BlockHelper(world, null);
		this.blocks = new Block[blocks.length];
		
		for (int i = 0; i < blocks.length; i++) {
			this.blocks[i] = blocks[i];
		}
	}
	
	/**
	 * Function to get whether a given block is valid in multiblock structure.
	 * 
	 * @param b block to reference.
	 * @return true if valid, else returns false.
	 */
	private boolean isBlockValid(Block b) {
		if (blocks == null || blocks.length == 0 || b == null || b == Blocks.air) return false;
		
		boolean flag = false;
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] == b) {
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	/**
	 * Method used to calculate the connections made.
	 */
	public void calculateConnections() {
		
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			if (isBlockValid(bh.getBlock(vec.x + dir.offsetX, vec.y + dir.offsetY, vec.z + dir.offsetZ))) {
				if (dir == ForgeDirection.EAST || dir == ForgeDirection.WEST) xAxis = true;
				if (dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH) zAxis = true;
				if (dir == ForgeDirection.DOWN || dir == ForgeDirection.UP) yAxis = true;
				
				connections[dir.ordinal()] = true;
				counter++;
			}
		}
		
	}
	
	/**
	 * Gets the connections array for each side or ForgeDirection.
	 * 
	 * @return connection boolean array.
	 */
	public boolean[] getConnections() {
		return connections;
	}
	
	/**
	 * Gets the number of connections made.
	 * 
	 * @return number of connections.
	 */
	public int getCounter() {
		return counter;
	}
	
	/**
	 * @return true if connected along x-axis
	 */
	public boolean containsXAxis() {
		return xAxis;
	}
	
	/**
	 * @return true if connected along y-axis
	 */
	public boolean containsYAxis() {
		return yAxis;
	}
	
	/**
	 * @return true if connected along z-axis
	 */
	public boolean containsZAxis() {
		return zAxis;
	}

}

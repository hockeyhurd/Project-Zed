/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector2;
import com.hockeyhurd.api.math.Vector3;
import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Block class for quarryMarker.
 * 
 * @author hockeyhurd
 * @version Jun 21, 2015
 */
public class BlockQuarryMarker extends BlockTorch {

	private static final String name = "quarryMarker";
	
	public BlockQuarryMarker() {
		this.setTickRandomly(false);
		this.setBlockName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + name);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;
		else {
			final int len = ProjectZed.configHandler.getMaxQuarrySize();

			byte[] directions = new byte[4];
			int minX = 0;
			int maxX = 0;
			int minZ = 0;
			int maxZ = 0;

			Vector3<Integer> localVec = new Vector3<Integer>(x, y, z);
			Block currentBlock;
			for (int xx = 1; xx < len; xx++) {
				currentBlock = world.getBlock(localVec.x + xx, localVec.y, localVec.z);

				if (currentBlock != null && currentBlock != Blocks.air) {
					if (currentBlock == ProjectZed.quarryMarker) {
						directions[0] = 1;
						minX = Math.min(localVec.x + xx, localVec.x);
						maxX = Math.max(localVec.x + xx, localVec.x);
					}
					
					else directions[0] = -1;

					break;
				}
			}

			for (int xx = 1; xx < len; xx++) {
				currentBlock = world.getBlock(localVec.x - xx, localVec.y, localVec.z);

				if (currentBlock != null && currentBlock != Blocks.air) {
					if (currentBlock == ProjectZed.quarryMarker) {
						directions[1] = 1;
						minX = Math.min(localVec.x - xx, localVec.x);
						maxX = Math.max(localVec.x - xx, localVec.x);
					}
					
					else directions[1] = -1;

					break;
				}
			}

			for (int zz = 1; zz < len; zz++) {
				currentBlock = world.getBlock(localVec.x, localVec.y, localVec.z + zz);

				if (currentBlock != null && currentBlock != Blocks.air) {
					if (currentBlock == ProjectZed.quarryMarker) {
						directions[2] = 1;
						
						minZ = Math.min(localVec.z + zz, localVec.z);
						maxZ = Math.max(localVec.z + zz, localVec.z);
					}
					
					else directions[2] = -1;

					break;
				}
			}

			for (int zz = 1; zz < len; zz++) {
				currentBlock = world.getBlock(localVec.x, localVec.y, localVec.z - zz);

				if (currentBlock != null && currentBlock != Blocks.air) {
					if (currentBlock == ProjectZed.quarryMarker) {
						directions[3] = 1;
						
						minZ = Math.min(localVec.z - zz, localVec.z);
						maxZ = Math.max(localVec.z - zz, localVec.z);
					}
					
					else directions[3] = -1;

					break;
				}
			}
			
			// make sure enough valid connections have been made.
			if (minX == maxX && minZ == maxZ /*&& minX == localVec.x && maxX == localVec.x && minZ == localVec.z && maxZ == localVec.z*/) return true;
			
			Vector2<Integer>[] ret = new Vector2[4];
			
			ret[0] = new Vector2<Integer>(minX, minZ); // x0z0
			ret[1] = new Vector2<Integer>(maxX, minZ); // x1z0
			ret[2] = new Vector2<Integer>(minX, maxZ); // x0z1
			ret[3] = new Vector2<Integer>(maxX, maxZ); // x1z1

			return true;
		}
	}
	
	/**
	 * Function to get boundary of quarry markers.
	 * 
	 * @param world world object to reference.
	 * @param localVec vector of this quarry marker to reference in world.
	 * @return array of vector2's as boundary markers.
	 */
	public Vector2<Integer>[] getBounds(World world, Vector3<Integer> localVec) {
		final int len = ProjectZed.configHandler.getMaxQuarrySize();

		byte[] directions = new byte[4];
		int minX = 0;
		int maxX = 0;
		int minZ = 0;
		int maxZ = 0;

		Block currentBlock;
		for (int xx = 1; xx < len; xx++) {
			currentBlock = world.getBlock(localVec.x + xx, localVec.y, localVec.z);

			if (currentBlock != null && currentBlock != Blocks.air) {
				if (currentBlock == ProjectZed.quarryMarker) {
					directions[0] = 1;
					minX = Math.min(localVec.x + xx, localVec.x);
					maxX = Math.max(localVec.x + xx, localVec.x);
				}
				
				else directions[0] = -1;

				break;
			}
		}

		for (int xx = 1; xx < len; xx++) {
			currentBlock = world.getBlock(localVec.x - xx, localVec.y, localVec.z);

			if (currentBlock != null && currentBlock != Blocks.air) {
				if (currentBlock == ProjectZed.quarryMarker) {
					directions[1] = 1;
					minX = Math.min(localVec.x - xx, localVec.x);
					maxX = Math.max(localVec.x - xx, localVec.x);
				}
				
				else directions[1] = -1;

				break;
			}
		}

		for (int zz = 1; zz < len; zz++) {
			currentBlock = world.getBlock(localVec.x, localVec.y, localVec.z + zz);

			if (currentBlock != null && currentBlock != Blocks.air) {
				if (currentBlock == ProjectZed.quarryMarker) {
					directions[2] = 1;
					
					minZ = Math.min(localVec.z + zz, localVec.z);
					maxZ = Math.max(localVec.z + zz, localVec.z);
				}
				
				else directions[2] = -1;

				break;
			}
		}

		for (int zz = 1; zz < len; zz++) {
			currentBlock = world.getBlock(localVec.x, localVec.y, localVec.z - zz);

			if (currentBlock != null && currentBlock != Blocks.air) {
				if (currentBlock == ProjectZed.quarryMarker) {
					directions[3] = 1;
					
					minZ = Math.min(localVec.z - zz, localVec.z);
					maxZ = Math.max(localVec.z - zz, localVec.z);
				}
				
				else directions[3] = -1;

				break;
			}
		}
		
		// make sure enough valid connections have been made.
		if (minX == maxX && minZ == maxZ /*&& minX == localVec.x && maxX == localVec.x && minZ == localVec.z && maxZ == localVec.z*/) return null;
		
		Vector2<Integer>[] ret = new Vector2[4];
		
		ret[0] = new Vector2<Integer>(minX, minZ); // x0z0
		ret[1] = new Vector2<Integer>(maxX, minZ); // x1z0
		ret[2] = new Vector2<Integer>(minX, maxZ); // x0z1
		ret[3] = new Vector2<Integer>(maxX, maxZ); // x1z1
		
		return ret;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item;

import com.hockeyhurd.hcorelib.api.math.Vector4;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class ItemDongle extends Item {

	public ItemDongle() {
		super();
		this.setUnlocalizedName("itemDongle");
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing side, float clickX, float clickY, float clickZ) {
		boolean used = false;
		if (!world.isRemote) {
			Vector4<Integer> vec = new Vector4<Integer>(player.chunkCoordX, player.chunkCoordY, player.chunkCoordZ);

			int xPos = (int) player.posX;
			int zPos = (int) player.posZ;
			Chunk chunk = world.getChunkFromBlockCoords(BlockUtils.createBlockPos(xPos, 0, zPos));
			List<Block> list = new ArrayList<Block>();
			
			int chunkX = chunk.xPosition * 16;
			int chunkZ = chunk.zPosition * 16;
			
			// Search through the chunk through 3-Dimensions and getting each block to be ananlyzed.
			for (int yy = (int) player.posY; yy > 0; yy--) {
				for (int xx = 0; xx < 16; xx++) {
					for (int zz = 0; zz < 16; zz++) {
						// Get the block id of the block being analyzed,
						Block block = BlockUtils.getBlock(world, chunkX + xx, yy, chunkZ + zz).getBlock();
						if (!block.getLocalizedName().toLowerCase().contains("ore")) BlockUtils.setBlockToAir(world, chunkX + xx, yy, chunkZ + zz);
					}
				}
			}
			
			used = true;
			return EnumActionResult.SUCCESS;
		}
		
		player.swingArm(hand);

		return EnumActionResult.FAIL;
	}

}

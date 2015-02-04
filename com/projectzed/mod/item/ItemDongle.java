/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.hockeyhurd.api.math.Vector4Helper;
import com.hockeyhurd.api.util.BlockHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDongle extends Item {

	public ItemDongle() {
		super();
		this.setUnlocalizedName("itemDongle");
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		this.itemIcon = Items.stick.getIconFromDamage(0);
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ) {
		boolean used = false;
		if (!world.isRemote) {
			Vector4Helper<Integer> vec = new Vector4Helper<Integer>(player.chunkCoordX, player.chunkCoordY, player.chunkCoordZ);
			BlockHelper bh = new BlockHelper(world, player);
			
			int xPos = (int) player.posX;
			int zPos = (int) player.posZ;
			Chunk chunk = world.getChunkFromBlockCoords(xPos, zPos);
			List<Block> list = new ArrayList<Block>();
			
			int chunkX = chunk.xPosition * 16;
			int chunkZ = chunk.zPosition * 16;
			
			// Search through the chunk through 3-Dimensions and getting each block to be ananlyzed.
			for (int yy = (int) player.posY; yy > 0; yy--) {
				for (int xx = 0; xx < 16; xx++) {
					for (int zz = 0; zz < 16; zz++) {
						// Get the block id of the block being analyzed,
						Block block = bh.getBlock(chunkX + xx, yy, chunkZ + zz);
						if (!block.getLocalizedName().toLowerCase().contains("ore")) bh.setBlockToAir(chunkX + xx, yy, chunkZ + zz);
					}
				}
			}
			
			used = true;
			return used;
		}
		
		player.swingItem();
		return used;
	}

}

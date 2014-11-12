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

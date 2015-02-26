/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item;

import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector4Helper;
import com.hockeyhurd.api.util.BlockHelper;
import com.projectzed.api.tileentity.IWrenchable;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.util.WorldUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for the main wrenching tool.
 * 
 * @author hockeyhurd
 * @version Feb 3, 2015
 */
public class ItemWrench extends Item {

	private final String NAME;
	private BlockHelper bh;
	
	/**
	 * @param name name of wrench.
	 */
	public ItemWrench(String name) {
		this.NAME = name;
		this.setUnlocalizedName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setMaxStackSize(1);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#registerIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		itemIcon = reg.registerIcon(ProjectZed.assetDir + this.NAME);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#onItemUse(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int, int, float, float, float)
	 */
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ) {
		boolean used = false;
		
		if (!world.isRemote) {
			if (bh == null) bh = new BlockHelper(world, player);
			
			Vector4Helper<Integer> vecClick = new Vector4Helper<Integer>(x, y, z);
			Block b = bh.getBlock(vecClick.x, vecClick.y, vecClick.z); 
			TileEntity te = world.getTileEntity(vecClick.x, vecClick.y, vecClick.z);
			
			if (b != null && b != Blocks.air && te != null && te instanceof IWrenchable) {
				IWrenchable wrench = (IWrenchable) te;
				
				if (wrench.canRotateTE() && !player.isSneaking()) {
					used = true;
					int meta = world.getBlockMetadata(vecClick.x, vecClick.y, vecClick.z);
					world.setBlockMetadataWithNotify(vecClick.x, vecClick.y, vecClick.z, rotateBlock(wrench.getRotationMatrix(), meta), 2);
				}

				else if (player.isSneaking() && wrench.canSaveDataOnPickup() && wrench.dataToSave() != null && wrench.dataToSave().size() > 0) {
					used = true;
					
					ItemStack itemToDrop = new ItemStack(b, 1);
					NBTTagCompound comp = itemToDrop.stackTagCompound;
					if (comp == null) comp = new NBTTagCompound();
					
					float buffer = 0.0f;
					for (Entry<String, Number> e : wrench.dataToSave().entrySet()) {
						buffer = e.getValue().floatValue();
						comp.setFloat(e.getKey(), buffer);
					}
					
					itemToDrop.stackTagCompound = comp;
					bh.setBlockToAir(vecClick);
					WorldUtils.addItemDrop(itemToDrop, world, vecClick.x, vecClick.y, vecClick.z);
				}
				
			}
		}
		
		player.swingItem();
		return used;
	}
	
	private byte rotateBlock(byte[] rotMatrix, int meta) {
		byte newMeta = 0;
		
		for (int i = 0; i < rotMatrix.length; i++) {
			if (meta == rotMatrix[i]) {
				if (i + 1 < rotMatrix.length) newMeta = rotMatrix[i + 1];
				else newMeta = rotMatrix[0];
				
				break;
			}
		}
		
		return newMeta;
	}

}

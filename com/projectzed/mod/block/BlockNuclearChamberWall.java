/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.hockeyhurd.api.util.BlockHelper;
import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for nuclearChamberWall.
 * 
 * @author hockeyhurd
 * @version Dec 12, 2014
 */
public class BlockNuclearChamberWall extends Block {

	private IIcon vert, horiz;
	
	public BlockNuclearChamberWall() {
		super(Material.rock);
		this.setBlockName("nuclearChamberWall");
		this.setHardness(1.0f);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT) 
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = vert = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall_vert");
		horiz = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall_horiz");
		
	}
	
	@SideOnly(Side.CLIENT) 
	public IIcon getIcon(int side, int meta) {
		if (side == 0 && meta == 0) return vert;
		return meta == 0 || meta == 1 ? vert : (meta == 2 /*&& (side != 0 && side != 1)*/ ? horiz : vert);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockPlacedBy(net.minecraft.world.World, int, int, int, net.minecraft.entity.EntityLivingBase, net.minecraft.item.ItemStack)
	 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		BlockHelper bh = new BlockHelper(world, (EntityPlayer) player);
		boolean yCheck = isBlockTopBottom(bh, x, y, z);
		
		if (!yCheck) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		else world.setBlockMetadataWithNotify(x, y, z, 1, 2);
	}
	
	@Deprecated
	private boolean isBlockTopBottom(BlockHelper bh, int x, int y, int z) {
		boolean ret = false;
		if (bh.blockExists(x, y - 1, z) || bh.blockExists(x, y + 1, z)) {
			if (bh.getBlock(x, y - 1, z) instanceof BlockNuclearChamberWall || (bh.getBlock(x, y - 1, z) instanceof BlockNuclearChamberLock)) ret = true;
			if (!ret && (bh.getBlock(x, y + 1, z) instanceof BlockNuclearChamberWall || (bh.getBlock(x, y + 1, z) instanceof BlockNuclearChamberLock))) ret = true;
		}
		
		return ret;
	}
	
}

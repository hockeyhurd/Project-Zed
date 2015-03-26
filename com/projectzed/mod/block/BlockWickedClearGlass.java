/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.proxy.ClientProxy;
import com.projectzed.mod.tileentity.TileEntityWickedClearGlass;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Block class for wickedClearGlass.
 * 
 * @author hockeyhurd
 * @version Mar 26, 2015
 */
public class BlockWickedClearGlass extends BlockContainer {

	public BlockWickedClearGlass() {
		super(Material.glass);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setBlockName("wickedClearGlass");
		this.setHardness(0.75f);
		this.setResistance(2000.0f);
		this.setStepSound(soundTypeGlass);
		this.setLightOpacity(0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockBreakable#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "wickedClearGlassItem");
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockGlass#renderAsNormalBlock()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockBreakable#isOpaqueCube()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getRenderType()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return ClientProxy.wickedClearGlass;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#canRenderInPass(int)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderInPass(int pass) {
		ClientProxy.renderPass = pass;
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockGlass#getRenderBlockPass()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockGlass#quantityDropped(java.util.Random)
	 */
	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	@Override
	public TileEntity createNewTileEntity(World world, int id) {
		return new TileEntityWickedClearGlass();
	}
	
}

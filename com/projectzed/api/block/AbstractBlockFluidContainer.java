/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.block;

import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.mod.ProjectZed;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Class containing neccessary abstract code for fluid containers (block).
 * 
 * @author hockeyhurd
 * @version Jan 19, 2015
 */
public abstract class AbstractBlockFluidContainer extends BlockContainer {

	/** Name of the block */
	protected String name;

	/** Asset directory of block. */
	protected String assetDir;
	
	protected Random random = new Random();
	
	/**
	 * @param material
	 */
	public AbstractBlockFluidContainer(Material material, String assetDir, String name) {
		super(material);
		this.assetDir = assetDir;
		this.name = name;
		this.setRegistryName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(1.0f);
	}

	/**
	 * @return tile entity associated with this block container.
	 */
	public abstract AbstractTileEntityFluidContainer getTileEntity();

	/* (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	@Override
	public TileEntity createNewTileEntity(World world, int id) {
		return getTileEntity();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	@Override
	public abstract boolean onBlockActivated(World world, BlockPos pos, IBlockState block, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ);

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockPlacedBy(net.minecraft.world.World, int, int, int, net.minecraft.entity.EntityLivingBase, net.minecraft.item.ItemStack)
	 */
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState block, EntityLivingBase e, ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound() != null) {
			NBTTagCompound comp = stack.getTagCompound();
			
			AbstractTileEntityFluidContainer te = (AbstractTileEntityFluidContainer) world.getTileEntity(pos);
			te.readNBT(comp);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockContainer#breakBlock(net.minecraft.world.World, int, int, int, net.minecraft.block.Block, int)
	 */
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState oldBlock) {
		doBreakBlock(world, pos);
		super.breakBlock(world, pos, oldBlock);
	}
	
	/**
	 * Method allows for control of behavior of block when being destroyed.
	 *
	 * @param world world object.
	 * @param pos BlockPos.
	 */
	protected abstract void doBreakBlock(World world, BlockPos pos);
	
}

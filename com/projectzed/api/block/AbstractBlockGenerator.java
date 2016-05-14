/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.block;

import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Class used to easily generator and normalize any generator.
 * 
 * @author hockeyhurd
 * @version Oct 28, 2014
 */
public abstract class AbstractBlockGenerator extends BlockContainer {
	
	/** Name of the block */
	protected String name;
	
	/**
	 * @param material material of block
	 * @param name String name.
	 */
	public AbstractBlockGenerator(Material material, String name) {
		super(material);
		this.name = name;
		this.setRegistryName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(1.0f);
	}

	/**
	 * Method used to grab exact tile entity associated with this block.
	 * <br>Example: return new TileEntityRFBridge().
	 */
	public abstract AbstractTileEntityGenerator getTileEntity();

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	public TileEntity createNewTileEntity(World world, int id) {
		return getTileEntity();
	}

	/**
	 * Method used to update block's state
	 * @param active whether currently active or not.
	 * @param world world object.
	 * @param x x-position.
	 * @param y y-position.
	 * @param z z-position.
	 */
	public static void updateBlockState(boolean active, World world, int x, int y, int z) {
		final BlockPos blockPos = VectorHelper.toBlockPos(x, y, z);
		final TileEntity tileEntity = world.getTileEntity(blockPos);
		// keepInventory = true;

		if (tileEntity != null && tileEntity instanceof AbstractTileEntityGenerator) {

			IBlockState blockState = BlockUtils.getBlock(world, x, y, z);
			world.notifyBlockOfStateChange(blockPos, blockState.getBlock());
			/*this.active = active;
			int metaData = world.getBlockMetadata(x, y, z);

			world.setBlock(x, y, z, getBlockInstance());

			keepInventory = false;
			world.setBlockMetadataWithNotify(x, y, z, metaData, 2);

			tileEntity.validate();
			world.setTileEntity(x, y, z, tileEntity);*/
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockPlacedBy(net.minecraft.world.World, int, int, int, net.minecraft.entity.EntityLivingBase, net.minecraft.item.ItemStack)
	 */
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState block, EntityLivingBase player, ItemStack stack) {
		int dir = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (dir == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if (dir == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		if (dir == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if (dir == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		if (stack.hasDisplayName()) ((TileEntitySolarArray) world.getTileEntity(pos)).setCustomName(stack.getDisplayName());
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	@Override
	public abstract boolean onBlockActivated(World world, BlockPos pos, IBlockState block, EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing side, float hitX, float hitY, float hitZ);

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
	 * Method allows for insertion into braekBlock method while keeping this abstract.
	 */
	protected abstract void doBreakBlock(World world, BlockPos pos);

}

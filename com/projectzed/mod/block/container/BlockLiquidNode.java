/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import com.projectzed.api.block.AbstractBlockFluidContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityLiquidNode;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Block code for liquid node.
 * 
 * @author hockeyhurd
 * @version Feb 16, 2015
 */
public class BlockLiquidNode extends AbstractBlockFluidContainer {
	
	protected static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	/**
	 * @param material
	 */
	public BlockLiquidNode(Material material) {
		super(material, ProjectZed.assetDir, "liquidNode");

		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public AbstractTileEntityFluidContainer getTileEntity() {
		return new TileEntityLiquidNode();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState block, EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase player, ItemStack stack) {
		final TileEntityLiquidNode te = (TileEntityLiquidNode) world.getTileEntity(blockPos);
		final boolean isServerSide = !world.isRemote;
		if (te != null) {
			te.setFrontFacing(player.getHorizontalFacing());

			if (isServerSide) {
				if (stack.hasDisplayName()) te.setCustomName(stack.getDisplayName());
				if (stack.hasTagCompound() && stack.hasTagCompound()) {
					NBTTagCompound comp = stack.getTagCompound();

					// AbstractTileEntityFluidContainer te = (AbstractTileEntityFluidContainer) world.getTileEntity(blockPos);
					te.readNBT(comp);
				}
			}
		}
	}
	
	@Override
	protected void doBreakBlock(World world, BlockPos blockPos) {
	}

	@Override
	public EnumFacing[] getValidRotations(World world, BlockPos blockPos) {
		final TileEntityLiquidNode te = (TileEntityLiquidNode) world.getTileEntity(blockPos);

		if (te != null && te.canRotateTE())
			return EnumFacing.VALUES;

		return super.getValidRotations(world, blockPos);
	}

	@Override
	public IBlockState getActualState(IBlockState blockState, IBlockAccess world, BlockPos blockPos) {
		final TileEntityLiquidNode te = (TileEntityLiquidNode) world.getTileEntity(blockPos);

		if (te != null && te.canRotateTE()) {
			EnumFacing dir = te.getCurrentFacing();
			if (dir == null) dir = EnumFacing.NORTH;
			return blockState.withProperty(FACING, dir);
		}

		return blockState;
	}

	@Override
	public int getMetaFromState(IBlockState blockState) {
		return blockState.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);
		if (facing.getAxis() == EnumFacing.Axis.Y) facing = EnumFacing.NORTH;

		return getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public IBlockState withRotation(IBlockState blockState, Rotation rot) {
		return blockState.withProperty(FACING, rot.rotate(blockState.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState blockState, Mirror mirrorIn) {
		return blockState.withRotation(mirrorIn.toRotation(blockState.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

}

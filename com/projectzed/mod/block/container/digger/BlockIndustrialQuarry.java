/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container.digger;

import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.hockeyhurd.hcorelib.api.util.ChatUtils;
import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.digger.TileEntityIndustrialQuarry;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * Class for industrialQuarry.
 * 
 * @author hockeyhurd
 * @version Jun 18, 2015
 */
public class BlockIndustrialQuarry extends AbstractBlockContainer {

	protected static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	protected static final PropertyBool ACTIVE = PropertyBool.create("active");

	/**
	 * @param material
	 */
	public BlockIndustrialQuarry(Material material) {
		super(material, ProjectZed.assetDir, "industrialQuarry");
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
	}

	@Override
	public float getBlockHardness() {
		return 2.0f;
	}

	@Override
	public AbstractTileEntityEnergyContainer getTileEntity() {
		return new TileEntityIndustrialQuarry();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityIndustrialQuarry te = (TileEntityIndustrialQuarry) world.getTileEntity(blockPos);
			if (te != null) {
				if (stack == null || !(stack.getItem() instanceof ItemWrench)) {
					FMLNetworkHandler
							.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityIndustrialQuarry.class), world,
									blockPos.getX(), blockPos.getY(), blockPos.getZ());
					player.addChatComponentMessage(ChatUtils.createComponent(false, "is done: " + te.isDone()));
				}

				else return false;
			}

			return true;
		}

		// player.addChatComponentMessage(ChatUtils.createComponent(false, "is done: " + te.isDone()));
	}

	@Override
	protected void doBreakBlock(World world, BlockPos blockPos) {
		TileEntityIndustrialQuarry te = (TileEntityIndustrialQuarry) world.getTileEntity(blockPos);
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

	@Override
	public EnumFacing[] getValidRotations(World world, BlockPos blockPos) {
		final AbstractTileEntityEnergyContainer te = (AbstractTileEntityEnergyContainer) world.getTileEntity(blockPos);
		if (te != null && te.canRotateTE())
			return EnumFacing.HORIZONTALS;

		return super.getValidRotations(world, blockPos);
	}

	@Override
	public IBlockState getActualState(IBlockState blockState, IBlockAccess world, BlockPos blockPos) {
		final AbstractTileEntityEnergyContainer te = (AbstractTileEntityEnergyContainer) world.getTileEntity(blockPos);

		if (te != null && te.canRotateTE()) {
			EnumFacing dir = te.getCurrentFacing();
			if (dir == null || dir == EnumFacing.DOWN || dir == EnumFacing.UP) dir = EnumFacing.NORTH;
			return blockState.withProperty(FACING, dir).withProperty(ACTIVE, te.isPowered());
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
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING))).withProperty(ACTIVE, state.getValue(ACTIVE));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withProperty(ACTIVE, state.getValue(ACTIVE)).withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, ACTIVE);
	}

	/**
	 * Method used to update block's state
	 *
	 * @param active boolean flag.
	 * @param world world object.
	 * @param blockPos Block position.
	 */
	public void updateBlockState(boolean active, World world, BlockPos blockPos) {
		if (world.isRemote) return;

		final AbstractTileEntityEnergyContainer tileEntity = (AbstractTileEntityEnergyContainer) world.getTileEntity(blockPos);

		if (tileEntity != null) {
			IBlockState blockState = BlockUtils.getBlock(world, blockPos);
			blockState = blockState.withProperty(FACING, tileEntity.getCurrentFacing()).withProperty(ACTIVE, active);
			BlockUtils.setBlock(world, blockPos, blockState, 2);
			BlockUtils.updateAndNotifyNeighborsOfBlockUpdate(world, blockPos);

			tileEntity.markDirty();
		}
	}

}

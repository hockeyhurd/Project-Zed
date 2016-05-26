/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.block.container;

import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityRefinery;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Block class for refinery.
 *
 * @author hockeyhurd
 * @version 8/4/2015.
 */
public class BlockRefinery extends AbstractBlockContainer {

	public BlockRefinery(Material material) {
		super(material, ProjectZed.assetDir, "refinery");
	}

	@Override
	public boolean isVisuallyOpaque() {
		return false;
	}

	@Override
	public AbstractTileEntityEnergyContainer getTileEntity() {
		return new TileEntityRefinery();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase e, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, e, stack);

		// int dir = MathHelper.floor_double((double) (e.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		// if (dir == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		// if (dir == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		// if (dir == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		// if (dir == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);

		final EnumFacing dir = e.getHorizontalFacing();
		final TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof AbstractTileEntityEnergyContainer) {
			((AbstractTileEntityEnergyContainer) tileEntity).setFrontFacing(dir);
			BlockUtils.updateAndNotifyNeighborsOfBlockUpdate(world, pos);
		}
	}

	@Override
	protected void doBreakBlock(World world, BlockPos blockPos) {
		TileEntityRefinery te = (TileEntityRefinery) world.getTileEntity(blockPos);
		ProjectZed.logHelper.info("Stored McU:", te.getEnergyStored());
		ProjectZed.logHelper.info("Stored Oil (mb):", te.getTank(TileEntityRefinery.TankID.INPUT).getFluidAmount());
		ProjectZed.logHelper.info("Stored Petrol (mb):", te.getTank(TileEntityRefinery.TankID.OUTPUT).getFluidAmount());
	}

}

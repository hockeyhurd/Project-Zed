/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.block.machines;

import com.hockeyhurd.hcorelib.api.math.Rect;
import com.hockeyhurd.hcorelib.api.math.Vector2;
import com.projectzed.api.block.AbstractBlockMachine;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialHarvester;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * Block class for industrialHarvester.
 *
 * @author hockeyhurd
 * @version 9/10/2015.
 */
public class BlockIndustrialHarvester extends AbstractBlockMachine {

	public BlockIndustrialHarvester() {
		super("industrialHarvester");
	}

	@Override
	public AbstractTileEntityMachine getTileEntity() {
		return new TileEntityIndustrialHarvester();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			AbstractTileEntityMachine te = (AbstractTileEntityMachine) world.getTileEntity(blockPos);
			if (te != null) {
				if (player.getActiveItemStack() == null || !(player.getActiveItemStack().getItem() instanceof ItemWrench))
					FMLNetworkHandler
							.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityIndustrialHarvester.class),
									world, blockPos.getX(), blockPos.getY(), blockPos.getZ());

				else return false;
			}

			return true;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase player, ItemStack stack) {
		super.onBlockPlacedBy(world, blockPos, blockState, player, stack);

		/*
		 * Facing direction from block metadata.
		 *
		 * 2 - SOUTH (z+)
		 * 3 - NORTH (z-)
		 * 4 - EAST  (x+)
		 * 5 - WEST  (x-)
		 */

		if (!world.isRemote) {
			// ProjectZed.logHelper.info(world.getBlockMetadata(x, y, z));
			final TileEntity tileEntity = world.getTileEntity(blockPos);
			if (tileEntity == null || !(tileEntity instanceof TileEntityIndustrialHarvester)) return;

			final TileEntityIndustrialHarvester te = (TileEntityIndustrialHarvester) tileEntity;
			final int x = blockPos.getX();
			final int y = blockPos.getY();
			final int z = blockPos.getZ();

			final int distNorm = TileEntityIndustrialHarvester.DEFAULT_NORMALIZED_RECT_SIZE;
			final int dist = TileEntityIndustrialHarvester.DEFAULT_RECT_SIZE;
			byte meta = (byte) blockState.getBlock().getMetaFromState(blockState);
			Rect<Integer> rect = null;

			// SOUTH:
			if (meta == 2) {
				Vector2<Integer> min = new Vector2<Integer>(x - distNorm, z + 1);
				Vector2<Integer> max = new Vector2<Integer>(x + distNorm, z + dist);

				rect = new Rect<Integer>(min, max);
			}

			// NORTH:
			else if (meta == 3) {
				Vector2<Integer> min = new Vector2<Integer>(x - distNorm, z - dist);
				Vector2<Integer> max = new Vector2<Integer>(x + distNorm, z - 1);

				rect = new Rect<Integer>(min, max);
			}

			// EAST:
			else if (meta == 4) {
				Vector2<Integer> min = new Vector2<Integer>(x + 1, z - distNorm);
				Vector2<Integer> max = new Vector2<Integer>(x + dist, z + distNorm);

				rect = new Rect<Integer>(min, max);
			}

			// WEST:
			else if (meta == 5) {
				Vector2<Integer> min = new Vector2<Integer>(x - dist, z - distNorm);
				Vector2<Integer> max = new Vector2<Integer>(x - 1, z + distNorm);

				rect = new Rect<Integer>(min, max);
			}

			// ProjectZed.logHelper.info(rect != null ? rect.getNormalizedArea() : 0.0d);
			// ProjectZed.logHelper.info(rect != null ? rect.getArea() : 0.0d);
			if (rect != null && rect.getNormalizedArea() >= dist * dist) {
				// ProjectZed.logHelper.info("Rect:", rect);
				te.setBoundedRect(rect);
			}

			else ProjectZed.logHelper.severe("Rectangle wasn't set properly for harvester @( ", x, y, z, ") Plez Fix!");
		}
	}
}

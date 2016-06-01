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
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialPlanter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * Block class for industrialPlanter.
 *
 * @author hockeyhurd
 * @version 8/27/2015.
 */
public class BlockIndustrialPlanter extends AbstractBlockMachine {

	public BlockIndustrialPlanter() {
		super("industrialPlanter");
	}

	@Override
	public AbstractTileEntityMachine getTileEntity() {
		return new TileEntityIndustrialPlanter();
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase e, ItemStack stack) {
		super.onBlockPlacedBy(world, blockPos, blockState, e, stack);

		TileEntityIndustrialPlanter te = (TileEntityIndustrialPlanter) world.getTileEntity(blockPos);
		if (te != null && te.getBoundedRect() == null) {
			final int defaultRectSize = TileEntityIndustrialPlanter.DEFAULT_NORMALIZED_RECT_SIZE;

			@SuppressWarnings("unchecked")
			Vector2<Integer> min = Vector2.zero.getVector2i();

			@SuppressWarnings("unchecked")
			Vector2<Integer> max = Vector2.zero.getVector2i();

			min.x = blockPos.getX() - defaultRectSize;
			min.y = blockPos.getZ() - defaultRectSize;

			max.x = blockPos.getX() + defaultRectSize;
			max.y = blockPos.getZ() + defaultRectSize;

			te.setBoundedRect(new Rect<Integer>(min, max));
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			AbstractTileEntityMachine te = (AbstractTileEntityMachine) world.getTileEntity(blockPos);
			if (te != null) {
				if (stack == null || !(stack.getItem() instanceof ItemWrench))
					FMLNetworkHandler
							.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityIndustrialPlanter.class),
									world, blockPos.getX(), blockPos.getY(), blockPos.getZ());

				else return false;
			}

			return true;
		}
	}

}

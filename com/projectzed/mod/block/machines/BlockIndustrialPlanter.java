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

import com.hockeyhurd.api.math.Rect;
import com.hockeyhurd.api.math.Vector2;
import com.projectzed.api.block.AbstractBlockMachine;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialPlanter;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
	protected Block getBlockInstance() {
		return this;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase e, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, e, stack);

		TileEntityIndustrialPlanter te = (TileEntityIndustrialPlanter) world.getTileEntity(x, y, z);
		if (te != null && te.getBoundedRect() == null) {
			final int defaultRectSize = TileEntityIndustrialPlanter.DEFAULT_NORMALIZED_RECT_SIZE;

			@SuppressWarnings("unchecked")
			Vector2<Integer> min = Vector2.zero.getVector2i();

			@SuppressWarnings("unchecked")
			Vector2<Integer> max = Vector2.zero.getVector2i();

			min.x = x - defaultRectSize;
			min.y = z - defaultRectSize;

			max.x = x + defaultRectSize;
			max.y = z + defaultRectSize;

			te.setBoundedRect(new Rect<Integer>(min, max));
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			AbstractTileEntityMachine te = (AbstractTileEntityMachine) world.getTileEntity(x, y, z);
			if (te != null) {
				if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemWrench))
					FMLNetworkHandler
							.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityIndustrialPlanter.class), world, x, y,
									z);

				else return false;
			}

			return true;
		}
	}

}

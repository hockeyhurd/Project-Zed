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

import com.projectzed.api.block.AbstractBlockMachine;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.machine.TileEntityPatternEncoder;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Block class for pattern encoder.
 *
 * @author hockeyhurd
 * @version 4/30/2016.
 */
public class BlockPatternEncoder extends AbstractBlockMachine {

	public BlockPatternEncoder(String name) {
		super(name);
	}

	@Override
	public AbstractTileEntityMachine getTileEntity() {
		return new TileEntityPatternEncoder();
	}

	@Override
	protected Block getBlockInstance() {
		return this;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityPatternEncoder te = (TileEntityPatternEncoder) world.getTileEntity(x, y, z);
			if (te != null) {
				if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemWrench))
					FMLNetworkHandler.openGui(player, ProjectZed.instance,
							TileEntityRegistry.instance().getID(TileEntityPatternEncoder.class), world, x, y, z);

				else return false;
			}

			return true;
		}
	}
}

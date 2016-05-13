/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.machines;

import com.projectzed.api.block.AbstractBlockMachine;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCrusher;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * Class containing code for industrial crusher.
 * 
 * @author hockeyhurd
 * @version Nov 4, 2014
 */
public class BlockIndustrialCrusher extends AbstractBlockMachine {

	public BlockIndustrialCrusher() {
		super("industrialCrusher");
		this.name = "industrialCrusher";
		this.setRegistryName(name);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.block.AbstractBlockMachine#getTileEntity()
	 */
	public AbstractTileEntityMachine getTileEntity() {
		return new TileEntityIndustrialCrusher();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.block.AbstractBlockMachine#getBlockInstance()
	 */
	protected Block getBlockInstance() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.block.AbstractBlockMachine#onBlockActivated(net.minecraft.world.World, int, int, int,
	 * net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState block, EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			AbstractTileEntityMachine te = (AbstractTileEntityMachine) world.getTileEntity(pos);
			if (te != null) {
				if (stack == null || !(stack.getItem() instanceof ItemWrench))
					FMLNetworkHandler
							.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityIndustrialCrusher.class),
									world, pos.getX(), pos.getY(), pos.getZ());

				else return false;
			}

			return true;
		}
	}

}

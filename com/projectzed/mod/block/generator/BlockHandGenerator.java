/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.block.generator;

import com.projectzed.api.block.AbstractBlockKineticGenerator;
import com.projectzed.mod.tileentity.generator.AbstractTileEntityKineticGenerator;
import com.projectzed.mod.tileentity.generator.TileEntityHandGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Block class for handGen.
 *
 * @author hockeyhurd
 * @version 4/17/2016.
 */
public class BlockHandGenerator extends AbstractBlockKineticGenerator {

	/**
	 * @param material material of block
	 */
	public BlockHandGenerator(Material material) {
		super(material, "handGen");
	}

	@Override
	public AbstractTileEntityKineticGenerator getTileEntity() {
		return new TileEntityHandGenerator();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntityHandGenerator te = (TileEntityHandGenerator) world.getTileEntity(blockPos);

			if (te != null) te.incrementHandCounter();

			return true;
		}

		player.swingArm(hand);

		return false;
	}

}

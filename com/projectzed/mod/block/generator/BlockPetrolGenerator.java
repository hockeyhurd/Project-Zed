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

import com.projectzed.api.block.AbstractBlockGenerator;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.generator.TileEntityPetrolGenerator;
import com.projectzed.mod.util.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import java.util.Random;

/**
 * Block class for petrolGen.
 *
 * @author hockeyhurd
 * @version 8/17/2015.
 */
public class BlockPetrolGenerator extends AbstractBlockGenerator {

	/**
	 * @param material material of block
	 */
	public BlockPetrolGenerator(Material material) {
		super(material, "petrolGen");
	}

	@Override
	public AbstractTileEntityGenerator getTileEntity() {
		return new TileEntityPetrolGenerator();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityPetrolGenerator te = (TileEntityPetrolGenerator) world.getTileEntity(blockPos);
			if (te != null) FMLNetworkHandler
					.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityPetrolGenerator.class),
							world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
			return true;
		}
	}

	@Override
	public void randomTick(World world, BlockPos blockPos, IBlockState blockState, Random random) {
		if (((TileEntityPetrolGenerator) world.getTileEntity(blockPos)).isPowered()) {
			// int metaData = world.getBlockMetadata(x, y, z);
			int metaData = blockState.getBlock().getMetaFromState(blockState);
			float f = (float) blockPos.getX() + 0.5F;
			float f1 = (float) blockPos.getY() + 0.0F + random.nextFloat() * 6.0F / 16.0F;
			float f2 = (float) blockPos.getZ() + 0.5F;
			float f3 = 0.52F;
			float f4 = random.nextFloat() * 0.6F - 0.3F;

			if (metaData == 4) {
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
				world.spawnParticle(EnumParticleTypes.FLAME, (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
			}
			else if (metaData == 5) {
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
				world.spawnParticle(EnumParticleTypes.FLAME, (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
			}
			else if (metaData == 2) {
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
				world.spawnParticle(EnumParticleTypes.FLAME, (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
			}
			else if (metaData == 3) {
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
				world.spawnParticle(EnumParticleTypes.FLAME, (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected void doBreakBlock(World world, BlockPos blockPos) {
		TileEntityPetrolGenerator te = (TileEntityPetrolGenerator) world.getTileEntity(blockPos);

		WorldUtils.dropItemsFromContainerOnBreak(te);

		ProjectZed.logHelper.info("Stored McU:", te.getEnergyStored());
		ProjectZed.logHelper.info("Stored Petrol (mb):", te.getTank().getFluidAmount());
	}
}

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

import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.block.AbstractBlockGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.generator.TileEntityPetrolGenerator;
import com.projectzed.mod.util.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
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

	private static final PropertyInteger FLUID_LEVEL = PropertyInteger.create("level", 0, 8);

	/**
	 * @param material material of block
	 */
	public BlockPetrolGenerator(Material material) {
		super(material, "petrolGen", false);

		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false).withProperty(FLUID_LEVEL, 0));
	}

	/*@Override
	public IBlockState getActualState(IBlockState blockState, IBlockAccess world, BlockPos pos) {
		IBlockState superState = super.getActualState(blockState, world, pos).withProperty(FLUID_LEVEL, 0);
		TileEntityPetrolGenerator te = (TileEntityPetrolGenerator) world.getTileEntity(pos);
		if (te != null)
			return (superState.withProperty(FLUID_LEVEL, te.getAndCheckLevel()));

		return superState;
	}*/

	@Override
	public void updateBlockState(boolean active, World world, BlockPos blockPos) {
		if (world.isRemote) return;

		final TileEntityPetrolGenerator te = (TileEntityPetrolGenerator) world.getTileEntity(blockPos);
		// keepInventory = true;

		if (te != null) {

			IBlockState blockState = BlockUtils.getBlock(world, blockPos);

			int level = te.getLevel();
			if (level < 0) level = 0;
			else if (level > 8) level = 8;

			blockState = blockState.withProperty(FACING, te.getCurrentFacing()).withProperty(ACTIVE, active).withProperty(FLUID_LEVEL, level);
			BlockUtils.setBlock(world, blockPos, blockState);

			te.markDirty();
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		// TODO: Try another work around similar to the immediate code below?

		/*BlockStateContainer blockStateContainer  = super.createBlockState();
		blockStateContainer.getProperties().add(FLUID_LEVEL);
		return blockStateContainer;*/

		return new BlockStateContainer(this, FACING, ACTIVE, FLUID_LEVEL);
	}

	@Override
	public TileEntityPetrolGenerator getTileEntity() {
		return new TileEntityPetrolGenerator();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityPetrolGenerator te = (TileEntityPetrolGenerator) world.getTileEntity(blockPos);
			if (te != null)
				if (stack == null || !(stack.getItem() instanceof ItemWrench))
					FMLNetworkHandler.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityPetrolGenerator.class),
								world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
				else return false;
			return true;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState block, EntityLivingBase player, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, block, player, stack);
		if (world.isRemote) return;

		final TileEntityPetrolGenerator te = (TileEntityPetrolGenerator) world.getTileEntity(pos);
		if (te != null) {
			final int level = stack.hasTagCompound() ? stack.getTagCompound().getInteger("fluidLevel") : 0;
			te.setLevel(level);
		}
	}

	@Override
	public void randomTick(World world, BlockPos blockPos, IBlockState blockState, Random random) {
		final TileEntityPetrolGenerator te = (TileEntityPetrolGenerator) world.getTileEntity(blockPos);

		if (te != null) {
			// if (!world.isRemote) te.getAndCheckLevel();

			if (te.isPowered()) {
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
	}

	@Override
	protected void doBreakBlock(World world, BlockPos blockPos) {
		TileEntityPetrolGenerator te = (TileEntityPetrolGenerator) world.getTileEntity(blockPos);

		WorldUtils.dropItemsFromContainerOnBreak(te);

		ProjectZed.logHelper.info("Stored McU:", te.getEnergyStored());
		ProjectZed.logHelper.info("Stored Petrol (mb):", te.getTank().getFluidAmount());
	}
}

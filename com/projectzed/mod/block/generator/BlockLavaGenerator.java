/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.block.generator;

import com.projectzed.api.block.AbstractBlockGenerator;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.generator.TileEntityLavaGenerator;
import com.projectzed.mod.util.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * Block class for lava generator.
 *
 * @author hockeyhurd
 * @version 6/30/2015.
 */
public class BlockLavaGenerator extends AbstractBlockGenerator {

	public BlockLavaGenerator(Material material) {
		super(material, "lavaGen");
	}

	@Override
	public AbstractTileEntityGenerator getTileEntity() {
		return new TileEntityLavaGenerator();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityLavaGenerator te = (TileEntityLavaGenerator) world.getTileEntity(blockPos);

			if (te != null && player != null) {

				// empty item fluid into fluid tank

				FluidStack containerFluid = getFluidForItem(stack);
				if (containerFluid == null || containerFluid.getFluid() == null || containerFluid.getFluid() != FluidRegistry.LAVA) {
					return openGui(world, player, stack, blockPos.getX(), blockPos.getY(), blockPos.getZ());
				}

				if (te.fill(null, containerFluid, true) > 0 && !player.capabilities.isCreativeMode) {
					ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(stack);

					if (stack.stackSize-- <= 0) player.inventory.setInventorySlotContents(player.inventory.currentItem, null);

					if (player instanceof FakePlayer || !player.inventory.addItemStackToInventory(emptyContainer))
						world.spawnEntityInWorld(new EntityItem(world, player.posX + 0.5D, player.posY + 1.5D, player.posZ + 0.5D, emptyContainer));
					else if (player instanceof EntityPlayerMP) ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
				}

				// ProjectZed.logHelper.info("Stored: " + ((TileEntityIndustrialCentrifuge) te).getTank().getFluidAmount());
			}
		}

		return true;
	}

	protected FluidStack getFluidForItem(ItemStack stack) {
		if (stack == null) return null;

		FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(stack);

		return fluid;
	}

	protected boolean openGui(World world, EntityPlayer player, ItemStack stack, int x, int y, int z) {
		if (stack == null || !(stack.getItem() instanceof ItemWrench)) {
			FMLNetworkHandler
					.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityLavaGenerator.class), world, x, y, z);

			return true;
		}

		else return false;
	}

	@Override
	protected void doBreakBlock(World world, BlockPos blockPos) {
		TileEntityLavaGenerator te = (TileEntityLavaGenerator) world.getTileEntity(blockPos);

		WorldUtils.dropItemsFromContainerOnBreak(te);

		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}
}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.machines;

import com.projectzed.api.block.AbstractBlockMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCentrifuge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * Class containing block code for industrialCentrifuge.
 * 
 * @author hockeyhurd
 * @version Dec 21, 2014
 */
public class BlockIndustrialCentrifuge extends AbstractBlockMachine {

	public BlockIndustrialCentrifuge() {
		super("industrialCentrifuge");
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

	@Override
	public TileEntityIndustrialCentrifuge getTileEntity() {
		return new TileEntityIndustrialCentrifuge();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		// TODO: read through and clean-up this code!

		if (world.isRemote) return true;

		else {
			TileEntityIndustrialCentrifuge te = (TileEntityIndustrialCentrifuge) world.getTileEntity(blockPos);

			if (te != null && player != null) {

				// empty item fluid into fluid tank

				FluidStack containerFluid = getFluidForItem(stack);
				if (containerFluid == null || containerFluid.getFluid() == null || containerFluid.getFluid() != FluidRegistry.WATER) {
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

	protected boolean isEmptyComplexContainer(ItemStack item) {
		if (item == null) return false;

		if (item.getItem() instanceof IFluidContainerItem) {
			IFluidContainerItem container = (IFluidContainerItem) item.getItem();
			FluidStack containerFluid = container.getFluid(item);

			return (containerFluid == null || containerFluid.amount == 0);
		}

		return false;
	}

	protected boolean openGui(World world, EntityPlayer player, ItemStack stack, int x, int y, int z) {
		if (stack == null || !(stack.getItem() instanceof ItemWrench)) {
			FMLNetworkHandler
					.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityIndustrialCentrifuge.class), world, x, y, z);

			return true;
		}

		else return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase e, ItemStack stack) {
		super.onBlockPlacedBy(world, blockPos, blockState, e, stack);

		if (stack.hasTagCompound() && stack.hasTagCompound()) {
			NBTTagCompound comp = stack.getTagCompound();

			TileEntityIndustrialCentrifuge te = (TileEntityIndustrialCentrifuge) world.getTileEntity(blockPos);
			te.readNBT(comp);

			/*int id = (int) comp.getFloat("FluidID");
			int amount = (int) comp.getFloat("FluidAmount");
			if (id < 0 || amount == 0) return;

			te.getTank().setFluid(new FluidStack(FluidRegistry.getFluid(id), amount));*/
		}
	}

}

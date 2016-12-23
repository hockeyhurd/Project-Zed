/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import com.projectzed.api.block.AbstractBlockFluidContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.List;

/**
 * Class containing code for generic tank container.
 * 
 * @author hockeyhurd
 * @version Jan 7, 2015
 */
public abstract class AbstractBlockTankBase extends AbstractBlockFluidContainer {

	protected byte tier;
	protected static final float PIXEL = 1f / 16f;
	protected static final float CALC = 4f * PIXEL;
	protected static final AxisAlignedBB DEFAULT_BOUNDS = new AxisAlignedBB(CALC, 0, CALC, 1f - CALC, 1f, 1f - CALC);

	/**
	 * @param material
	 * @param name
	 */
	public AbstractBlockTankBase(Material material, String name) {
		super(material, ProjectZed.assetDir, name);
		this.tier = 0;
	}

	@Override
	public float getBlockHardness() {
		return 3.0f;
	}

	@Override
	public boolean isOpaqueCube(IBlockState blockState) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState blockState) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState blockState) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isFullyOpaque(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return DEFAULT_BOUNDS;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
		return getBoundingBox(state, world, pos);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
		return getBoundingBox(state, world, pos);
	}

	@Override
	public abstract AbstractTileEntityFluidContainer getTileEntity();

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState block, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		// TODO: read through and clean-up this code!

		if (world.isRemote) return true;

		else {
			AbstractTileEntityFluidContainer te = (AbstractTileEntityFluidContainer) world.getTileEntity(pos);

			if (te != null && player != null) {

				if (stack != null && stack.getItem() != null) {
					if (stack.getItem() instanceof ItemWrench) return false;

					// fill item from fluid tank.
					if (FluidContainerRegistry.isEmptyContainer(stack)
							|| isEmptyComplexContainer(stack)
							|| (stack.getItem() instanceof IFluidContainerItem && player.isSneaking())) {

						if (te.getTank().getFluid() == null) return true;

						if (stack.getItem() instanceof IFluidContainerItem) {
							// handle IFluidContainerItem items

							IFluidContainerItem containerItem = (IFluidContainerItem) stack.getItem();
							int fillFluidAmount = containerItem.fill(stack, te.getTank()
									.getFluid(), true);
							te.drain(null, fillFluidAmount, true);
						}
						else {
							// handle drain/fill by exchange items

							ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(
									te.getTank().getFluid(), stack);

							if (filledContainer != null) {
								int containerCapacity = FluidContainerRegistry.getContainerCapacity(te.getTank()
										.getFluid(), stack);

								if (containerCapacity > 0) {
									FluidStack drainedFluid = te.drain(null, containerCapacity, true);
									if (drainedFluid != null && drainedFluid.amount == containerCapacity) {
										if (stack.stackSize-- <= 0) {
											player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
										}

										// add filled container to player
										// inventory or drop it to the ground if
										// the inventory is full or we're
										// dealing with a fake player

										if (player instanceof FakePlayer || !player.inventory.addItemStackToInventory(filledContainer)) {
											world.spawnEntityInWorld(new EntityItem(world, player.posX + 0.5D, player.posY + 1.5D,
													player.posZ + 0.5D, filledContainer));
										}
										else if (player instanceof EntityPlayerMP) {
											((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
										}
									}
								}
							}
						}

						ProjectZed.logHelper.info("Stored: " + ((TileEntityFluidTankBase) te).getTank().getFluidAmount());
					}

					// empty item fluid into fluid tank
					else {

						FluidStack containerFluid = getFluidForItem(stack);

						if (((TileEntityFluidTankBase) te).fill(null, containerFluid, true) > 0 && !player.capabilities.isCreativeMode) {
							ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(stack);

							if (stack.stackSize-- <= 0)
								player.inventory.setInventorySlotContents(player.inventory.currentItem, null);

							if (player instanceof FakePlayer || !player.inventory.addItemStackToInventory(emptyContainer)) world
									.spawnEntityInWorld(new EntityItem(world, player.posX + 0.5D, player.posY + 1.5D, player.posZ + 0.5D,
											emptyContainer));
							else if (player instanceof EntityPlayerMP) ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
						}

						if (ProjectZed.configHandler.isDebugMode()) ProjectZed.logHelper.info("Stored: " + te.getTank().getFluidAmount());
					}
				}
				
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
	
	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.block.AbstractBlockContainer#doBreakBlock(net.minecraft
	 * .world.World, int, int, int)
	 */
	@Override
	protected void doBreakBlock(World world, BlockPos pos) {
		TileEntityFluidTankBase te = (TileEntityFluidTankBase) world.getTileEntity(pos);
		if (te != null && !world.isRemote) {
			String fluidName = te.getLocalizedFluidName() == null ? "<empty>" : te.getLocalizedFluidName();
			ProjectZed.logHelper.info("Destroyed fluid container w/fluid: " + fluidName + ", stored: " + te.getTank().getFluidAmount());
		}
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, player, tooltip, advanced);

		if (stack.hasTagCompound()) {
			NBTTagCompound comp = stack.getTagCompound();

			String fluidName = comp.getString("FluidName");
			if (fluidName == null || FluidRegistry.getFluid(fluidName) == null) return;

			FluidStack fluidStack = new FluidStack(FluidRegistry.getFluid(fluidName), comp.getInteger("Amount"));

			if (fluidStack != null && fluidStack.amount > 0) {
				tooltip.add(TextFormatting.GREEN + "Fluid: " + TextFormatting.WHITE + fluidName);
				tooltip.add(TextFormatting.GREEN + "Amount: " + TextFormatting.WHITE + fluidStack.amount);
			}
		}
	}
}

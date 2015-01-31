package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import com.projectzed.api.block.AbstractBlockFluidContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.proxy.ClientProxy;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for generic tank container.
 * 
 * @author hockeyhurd
 * @version Jan 7, 2015
 */
public abstract class AbstractBlockTankBase extends AbstractBlockFluidContainer {

	protected static final ItemStack FILLED_BOTTLE = new ItemStack(Items.potionitem);

	protected byte tier;
	protected final float PIXEL = 1f / 16f;
	protected final float CALC = 4f * PIXEL;

	/**
	 * @param material
	 * @param name
	 */
	public AbstractBlockTankBase(Material material, String name) {
		super(material, ProjectZed.assetDir, name);
		this.tier = 0;
		this.setBlockBounds(CALC, 0, CALC, 1f - CALC, 1f, 1f - CALC);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.block.AbstractBlockFluidContainer#registerBlockIcons
	 * (net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(assetDir + this.name + "_item");
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#renderAsNormalBlock()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderAsNormalBlock() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#isOpaqueCube()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#canRenderInPass(int)
	 */
	@SideOnly(Side.CLIENT)
	public boolean canRenderInPass(int pass) {
		ClientProxy.renderPass = pass;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getRenderBlockPass()
	 */
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getRenderType()
	 */
	@SideOnly(Side.CLIENT)
	public abstract int getRenderType();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#getTileEntity()
	 */
	@Override
	public abstract AbstractTileEntityFluidContainer getTileEntity();

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.block.AbstractBlockContainer#onBlockActivated(net.
	 * minecraft.world.World, int, int, int,
	 * net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		// TODO: read through and clean-up this code!

		if (world.isRemote) return true;

		else {
			AbstractTileEntityFluidContainer te = (AbstractTileEntityFluidContainer) world.getTileEntity(x, y, z);

			if (te != null && player != null) {

				if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() != null) {

					// fill item from fluid tank.
					if (FluidContainerRegistry.isEmptyContainer(player.getCurrentEquippedItem())
							|| isEmptyComplexContainer(player.getCurrentEquippedItem())
							|| (player.getCurrentEquippedItem().getItem() instanceof IFluidContainerItem && player.isSneaking())) {

						if (((TileEntityFluidTankBase) te).getTank().getFluid() == null) return true;

						if (player.getCurrentEquippedItem().getItem() instanceof IFluidContainerItem) {
							// handle IFluidContainerItem items

							IFluidContainerItem containerItem = (IFluidContainerItem) player.getCurrentEquippedItem().getItem();
							int fillFluidAmount = containerItem.fill(player.getCurrentEquippedItem(), ((TileEntityFluidTankBase) te).getTank()
									.getFluid(), true);
							((TileEntityFluidTankBase) te).drain(null, fillFluidAmount, true);
						}
						else {
							// handle drain/fill by exchange items

							ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(
									((TileEntityFluidTankBase) te).getTank().getFluid(), player.getCurrentEquippedItem());

							if (filledContainer != null) {
								int containerCapacity = FluidContainerRegistry.getContainerCapacity(((TileEntityFluidTankBase) te).getTank()
										.getFluid(), player.getCurrentEquippedItem());

								if (containerCapacity > 0) {
									FluidStack drainedFluid = ((TileEntityFluidTankBase) te).drain(null, containerCapacity, true);
									if (drainedFluid != null && drainedFluid.amount == containerCapacity) {
										if (player.getCurrentEquippedItem().stackSize-- <= 0) {
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

						FluidStack containerFluid = getFluidForItem(player.getCurrentEquippedItem());

						if (((TileEntityFluidTankBase) te).fill(null, containerFluid, true) > 0 && !player.capabilities.isCreativeMode) {
							ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(player.getCurrentEquippedItem());

							if (player.getCurrentEquippedItem().stackSize-- <= 0)
								player.inventory.setInventorySlotContents(player.inventory.currentItem, null);

							if (player instanceof FakePlayer || !player.inventory.addItemStackToInventory(emptyContainer)) world
									.spawnEntityInWorld(new EntityItem(world, player.posX + 0.5D, player.posY + 1.5D, player.posZ + 0.5D,
											emptyContainer));
							else if (player instanceof EntityPlayerMP) ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
						}

						ProjectZed.logHelper.info("Stored: " + ((TileEntityFluidTankBase) te).getTank().getFluidAmount());
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
	protected void doBreakBlock(World world, int x, int y, int z) {
		TileEntityFluidTankBase te = (TileEntityFluidTankBase) world.getTileEntity(x, y, z);
		if (te != null && !world.isRemote) {
			String fluidName = te.getLocalizedFluidName() == null ? "<empty>" : te.getLocalizedFluidName();
			ProjectZed.logHelper.info("Destroyed fluid container w/fluid: " + fluidName + ", stored: " + te.getTank().getFluidAmount());
		}
	}

}

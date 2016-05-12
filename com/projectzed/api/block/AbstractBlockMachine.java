/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 */
package com.projectzed.api.block;

import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Class containing framework code for all block machines.
 * 
 * @author hockeyhurd
 * @version Oct 23, 2014
 */
public abstract class AbstractBlockMachine extends BlockContainer {

	protected String name;
	protected boolean active;
	protected static boolean keepInventory;
	protected Random random = new Random();

	public AbstractBlockMachine(String name) {
		super(Material.rock);
		this.name = name;
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setRegistryName(name);
		this.setHardness(1f);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft
	 * .world.World, int)
	 */
	public TileEntity createNewTileEntity(World world, int id) {
		return getTileEntity();
	}

	/**
	 * Gets tile entity associated with this machine block.
	 * 
	 * @return tile entity instance.
	 */
	public abstract AbstractTileEntityMachine getTileEntity();

	/**
	 * Handles updating the blocks state being called from its te class.
	 * 
	 * @param active = state of activity.
	 * @param world = world object.
	 * @param x = x-pos.
	 * @param y = y-pos.
	 * @param z = z-pos.
	 */
	public void updateBlockState(boolean active, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tileEntity = world.getTileEntity(pos);
		keepInventory = true;

		if (tileEntity != null && tileEntity instanceof AbstractTileEntityMachine) {
			
			world.notifyBlockOfStateChange(pos, tileEntity.getBlockType());
			/*this.active = active;
			int metaData = world.getBlockMetadata(x, y, z);

			world.setBlock(x, y, z, getBlockInstance());

			keepInventory = false;
			world.setBlockMetadataWithNotify(x, y, z, metaData, 2);

			tileEntity.validate();
			world.setTileEntity(x, y, z, tileEntity);*/
		}
	}

	/**
	 * Gets the exact instance of extended class.
	 * 
	 * @return block object.
	 */
	protected abstract Block getBlockInstance();

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getItem(net.minecraft.world.World, int,
	 * int, int)
	 */
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		return Item.getItemFromBlock(getBlockInstance());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.minecraft.block.Block#randomDisplayTick(net.minecraft.world.World,
	 * int, int, int, java.util.Random)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState worldIn, World world, BlockPos pos, Random random) {
		if (this.active) {
			int metaData = worldIn.getActualState(world, pos).getBlock().getMetaFromState(worldIn);
			float f = (float) pos.getX() + 0.5f;
			float f1 = (float) pos.getY() + 0.0f + random.nextFloat() * 6.0f / 16.0f;
			float f2 = (float) pos.getZ() + 0.5f;
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

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockPlacedBy(net.minecraft.world.World,
	 * int, int, int, net.minecraft.entity.EntityLivingBase,
	 * net.minecraft.item.ItemStack)
	 */
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState block, EntityLivingBase player, ItemStack stack) {
		int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;


		// if (l == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if (l == 0) block.getBlock().rotateBlock(world, pos, EnumFacing.getFront(2));
		// if (l == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		if (l == 1) block.getBlock().rotateBlock(world, pos, EnumFacing.getFront(5));
		// if (l == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if (l == 2) block.getBlock().rotateBlock(world, pos, EnumFacing.getFront(3));
		// if (l == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		if (l == 3) block.getBlock().rotateBlock(world, pos, EnumFacing.getFront(4));

		BlockUtils.setBlock(world, pos, block);

		if (stack.hasDisplayName()) ((AbstractTileEntityMachine) world.getTileEntity(pos)).setCustomName(stack.getDisplayName());

		if (stack.hasTagCompound() && stack.getTagCompound() != null) {
			NBTTagCompound comp = stack.getTagCompound();

			AbstractTileEntityMachine te = (AbstractTileEntityMachine) world.getTileEntity(pos);
			te.readNBT(comp);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.minecraft.block.Block#onBlockActivated(net.minecraft.world.World,
	 * int, int, int, net.minecraft.entity.player.EntityPlayer, int, float,
	 * float, float)
	 */
	@Override
	public abstract boolean onBlockActivated(World world, BlockPos pos, IBlockState block, EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing side, float hitX, float hitY, float hitZ);

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState oldBlock) {
		if (!keepInventory) {
			AbstractTileEntityMachine te = (AbstractTileEntityMachine) world.getTileEntity(pos);

			if (te != null) {
				for (int j1 = 0; j1 < te.getSizeInventory(); j1++) {
					ItemStack stack = te.getStackInSlot(j1);

					if (stack != null) {
						float f = this.random.nextFloat() * 0.8F + 0.1F;
						float f1 = this.random.nextFloat() * 0.8F + 0.1F;
						float f2 = this.random.nextFloat() * 0.8F + 0.1F;

						while (stack.stackSize > 0) {
							int k1 = this.random.nextInt(21) + 10;

							if (k1 > stack.stackSize) {
								k1 = stack.stackSize;
							}

							stack.stackSize -= k1;
							EntityItem entityitem = new EntityItem(world, (double) ((float) pos.getX() + f), (double) ((float) pos.getY() + f1),
									(double) ((float) pos.getZ() + f2), new ItemStack(stack.getItem(), k1, stack.getItemDamage()));

							if (stack.hasTagCompound()) {
								entityitem.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
							}

							float f3 = 0.05F;
							entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
							entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
							entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
							world.spawnEntityInWorld(entityitem);
						}
					}
				}

				// world.func_147453_f(x, y, z, oldBlock);
				world.notifyNeighborsOfStateChange(pos, oldBlock.getBlock());
			}
		}

		// super.breakBlock(world, x, y, z, oldBlock, oldBlockMetaData);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#hasComparatorInputOverride()
	 */
	@Override
	public boolean hasComparatorInputOverride(IBlockState block) {
		return true;
	}

}

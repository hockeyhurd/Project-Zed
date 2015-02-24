/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 */
package com.projectzed.api.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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

	@SideOnly(Side.CLIENT)
	protected IIcon iconFront, iconFrontOn, iconBottom, iconSide;

	public AbstractBlockMachine(String name) {
		super(Material.rock);
		this.name = name;
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setBlockName(name);
		this.setHardness(1f);
	}

	/**
	 * NOTE: You probably want to overwrite this method if this method is used
	 * in another mod!
	 * 
	 * @see net.minecraft.block.Block#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		this.blockIcon = reg.registerIcon(ProjectZed.assetDir + "generic_side");
		this.iconFront = reg.registerIcon(ProjectZed.assetDir + name + "_front");
		this.iconFrontOn = reg.registerIcon(ProjectZed.assetDir + name + "_front_on");
		this.iconBottom = reg.registerIcon(ProjectZed.assetDir + "generic_base");
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getIcon(net.minecraft.world.IBlockAccess, int, int, int, int)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		AbstractTileEntityMachine te = (AbstractTileEntityMachine) world.getTileEntity(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		System.out.println(te.isPoweredOn());
		
		if (side == 3 && meta == 0) return this.iconFront;
		return side == 0 || side == 1 ? this.iconBottom : (side != meta ? this.blockIcon : (te.isPoweredOn() ? this.iconFrontOn : this.iconFront));
	}

	/**
	 * NOTE: You probably want to overwrite this method if this method is used
	 * in another mod!
	 * 
	 * @see net.minecraft.block.Block#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side == 3 && meta == 0) return this.iconFront;
		return side == 0 || side == 1 ? this.iconBottom : (side != meta ? this.blockIcon : (this.active ? this.iconFrontOn : this.iconFront));
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
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		keepInventory = true;

		if (tileEntity != null && tileEntity instanceof AbstractTileEntityMachine) {
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
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (this.active) {
			int metaData = world.getBlockMetadata(x, y, z);
			float f = (float) x + 0.5F;
			float f1 = (float) y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
			float f2 = (float) z + 0.5F;
			float f3 = 0.52F;
			float f4 = random.nextFloat() * 0.6F - 0.3F;

			if (metaData == 4) {
				world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
			}
			else if (metaData == 5) {
				world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
			}
			else if (metaData == 2) {
				world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
			}
			else if (metaData == 3) {
				world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
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
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if (l == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		if (l == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if (l == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);

		if (stack.hasDisplayName()) ((AbstractTileEntityMachine) world.getTileEntity(x, y, z)).setCustomName(stack.getDisplayName());

		if (stack.hasTagCompound() && stack.stackTagCompound != null) {
			NBTTagCompound comp = stack.stackTagCompound;

			AbstractTileEntityMachine te = (AbstractTileEntityMachine) world.getTileEntity(x, y, z);
			te.setEnergyStored((int) comp.getFloat("ProjectZedPowerStored"));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.minecraft.block.Block#onBlockActivated(net.minecraft.world.World,
	 * int, int, int, net.minecraft.entity.player.EntityPlayer, int, float,
	 * float, float)
	 */
	public abstract boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ);

	public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldBlockMetaData) {
		if (!keepInventory) {
			AbstractTileEntityMachine te = (AbstractTileEntityMachine) world.getTileEntity(x, y, z);

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
							EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1),
									(double) ((float) z + f2), new ItemStack(stack.getItem(), k1, stack.getItemDamage()));

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

				world.func_147453_f(x, y, z, oldBlock);
			}
		}

		// super.breakBlock(world, x, y, z, oldBlock, oldBlockMetaData);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#hasComparatorInputOverride()
	 */
	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

}

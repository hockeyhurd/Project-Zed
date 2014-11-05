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
import net.minecraft.world.World;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialFurnace;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
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
	 * NOTE: You probably want to overwrite this method if this method is used in another mod!
	 * @see net.minecraft.block.Block#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		this.blockIcon = reg.registerIcon(ProjectZed.assetDir + "generic_side");
		this.iconFront = reg.registerIcon(ProjectZed.assetDir + name + "_front");
		this.iconFrontOn = reg.registerIcon(ProjectZed.assetDir + name + "_front_on");
		this.iconBottom = reg.registerIcon(ProjectZed.assetDir + "generic_base");
	}
	
	/**
	 * NOTE: You probably want to overwrite this method if this method is used in another mod!
	 * @see net.minecraft.block.Block#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side == 3 && meta == 0) return this.iconFront;
		return side == 0 || side == 1? this.iconBottom : (side != meta ? this.blockIcon : (meta == 2 && this.active ? this.iconFrontOn : this.iconFront));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	public TileEntity createNewTileEntity(World world, int id) {
		return getTileEntity();
	}

	protected abstract AbstractTileEntityMachine getTileEntity();

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
		int metaData = world.getBlockMetadata(x, y, z);
		TileEntity tileentity = world.getTileEntity(x, y, z);
		keepInventory = true;

		this.active = active;
		world.setBlock(x, y, z, getBlockInstance());

		keepInventory = false;
		world.setBlockMetadataWithNotify(x, y, z, metaData, 2);

		if (tileentity != null) {
			tileentity.validate();
			world.setTileEntity(x, y, z, tileentity);
		}
	}
	
	/**
	 * Gets the exact instance of extended class.
	 * @return block object.
	 */
	protected abstract Block getBlockInstance();
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getItem(net.minecraft.world.World, int, int, int)
	 */
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		return Item.getItemFromBlock(getBlockInstance());
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#randomDisplayTick(net.minecraft.world.World, int, int, int, java.util.Random)
	 */
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (this.active) {
			int l = world.getBlockMetadata(x, y, z);
			float f = (float) x + 0.5F;
			float f1 = (float) y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
			float f2 = (float) z + 0.5F;
			float f3 = 0.52F;
			float f4 = random.nextFloat() * 0.6F - 0.3F;

			if (l == 4) {
				world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
			}
			else if (l == 5) {
				world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
			}
			else if (l == 2) {
				world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
			}
			else if (l == 3) {
				world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockPlacedBy(net.minecraft.world.World, int, int, int, net.minecraft.entity.EntityLivingBase, net.minecraft.item.ItemStack)
	 */
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if (l == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		if (l == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if (l == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);

		if (stack.hasDisplayName()) ((AbstractTileEntityMachine) world.getTileEntity(x, y, z)).setCustomName(stack.getDisplayName());
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
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
							EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(stack.getItem(), k1, stack.getItemDamage()));

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

}

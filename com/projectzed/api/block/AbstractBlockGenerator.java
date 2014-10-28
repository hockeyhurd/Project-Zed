package com.projectzed.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class used to easily generator and normalize any generator.
 * 
 * @author hockeyhurd
 * @version Oct 28, 2014
 */
public abstract class AbstractBlockGenerator extends BlockContainer {

	protected String name;
	protected IIcon top, base;

	/**
	 * @param material = material of block
	 */
	public AbstractBlockGenerator(Material material, String name) {
		super(material);
		this.name = name;
		this.setBlockName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(1.0f);
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + name + "_side");
		this.top = reg.registerIcon(ProjectZed.assetDir + name + "_top");
		this.base = reg.registerIcon(ProjectZed.assetDir + name + "_base");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return side == 1 ? this.top : (side == 0 ? this.base : this.blockIcon);
	}

	public abstract TileEntity createNewTileEntity(World world, int id);

	public static void updateBlockState(boolean active, World world, int x, int y, int z) {
		int metaData = world.getBlockMetadata(x, y, z);
		TileEntity tileentity = world.getTileEntity(x, y, z);

		world.setBlockMetadataWithNotify(x, y, z, metaData, 2);

		if (tileentity != null) {
			tileentity.validate();
			world.setTileEntity(x, y, z, tileentity);
		}
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if (l == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		if (l == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if (l == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		if (stack.hasDisplayName()) ((TileEntitySolarArray) world.getTileEntity(x, y, z)).setCustomName(stack.getDisplayName());
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	public abstract boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ);

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockContainer#breakBlock(net.minecraft.world.World, int, int, int, net.minecraft.block.Block, int)
	 */
	public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldBlockMetaData) {
		doBreakBlock(world, x, y, z);
		super.breakBlock(world, x, y, z, oldBlock, oldBlockMetaData);
	}
	
	/**
	 * Method allows for insertion into braekBlock method while keeping this abstract.
	 */
	protected abstract void doBreakBlock(World world, int x, int y, int z);

}

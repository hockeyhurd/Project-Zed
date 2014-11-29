package com.projectzed.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.api.tileentity.container.AbstractTileEntityContainer;
import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class used to easily create and normalize any block container (energy, fluid, etc.).
 * 
 * @author hockeyhurd
 * @version Nov 29, 2014
 */
public abstract class AbstractBlockContainer extends BlockContainer {

	/** Name of the block */
	protected String name;

	/** Asset directory of block. */
	protected String assetDir;
	
	/**
	 * @param material = material of block.
	 * @param assetDir = asset directory to find icon img.
	 * @param name = name of block.
	 */
	public AbstractBlockContainer(Material material, String assetDir, String name) {
		super(material);
		this.assetDir = assetDir;
		this.name = name;
		this.setBlockName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(1.0f);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(assetDir + name);
	}
	
	/**
	 * Method used to grab exact tile entity associated with this block.
	 * <br>Example: return new TileEntityRFBridge().
	 */
	protected abstract AbstractTileEntityContainer getTileEntity();
	
	public TileEntity createNewTileEntity(World world, int id) {
		return getTileEntity();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockContainer#breakBlock(net.minecraft.world.World, int, int, int, net.minecraft.block.Block, int)
	 */
	public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldBlockMetaData) {
		doBreakBlock(world, x, y, z);
		super.breakBlock(world, x, y, z, oldBlock, oldBlockMetaData);
	}

	/**
	 * Method allows for control of behavior of block when being destroyed.
	 * @param world = world object.
	 * @param x = x-position.
	 * @param y = y-position.
	 * @param z = z-position.
	 */
	protected abstract void doBreakBlock(World world, int x, int y, int z);

}

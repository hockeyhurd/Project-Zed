/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.block;

import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

/**
 * Class used to easily generator and normalize any generator.
 * 
 * @author hockeyhurd
 * @version Oct 28, 2014
 */
public abstract class AbstractBlockGenerator extends BlockContainer {
	
	/** Name of the block */
	protected String name;
	
	/** Icons per side of block */
	@SideOnly(Side.CLIENT)
	protected IIcon top, base, front;

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

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + name + "_side");
		this.top = reg.registerIcon(ProjectZed.assetDir + name + "_top");
		this.base = reg.registerIcon(ProjectZed.assetDir + name + "_base");
		this.front = reg.registerIcon(ProjectZed.assetDir + name + "_front");
	}

	/**
	 * NOTE: You probably want to overwrite this method if this method is used in another mod!
	 * @see net.minecraft.block.Block#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side == 3 && meta == 0) return this.front;
		return side == 0 ? this.base : (side == 1 ? this.top: (side != meta ? this.blockIcon : this.front));
	}
	
	/**
	 * Method used to grab exact tile entity associated with this block.
	 * <br>Example: return new TileEntityRFBridge().
	 */
	public abstract AbstractTileEntityGenerator getTileEntity();

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	public TileEntity createNewTileEntity(World world, int id) {
		return getTileEntity();
	}

	/**
	 * Method used to update block's state
	 * @param active = whether currently active or not.
	 * @param world = world object.
	 * @param x = x-position.
	 * @param y = y-position.
	 * @param z = z-position.
	 */
	public static void updateBlockState(boolean active, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		// keepInventory = true;

		if (tileEntity != null && tileEntity instanceof AbstractTileEntityGenerator) {

			world.markBlockForUpdate(x, y, z);
			/*this.active = active;
			int metaData = world.getBlockMetadata(x, y, z);

			world.setBlock(x, y, z, getBlockInstance());

			keepInventory = false;
			world.setBlockMetadataWithNotify(x, y, z, metaData, 2);

			tileEntity.validate();
			world.setTileEntity(x, y, z, tileEntity);*/
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockPlacedBy(net.minecraft.world.World, int, int, int, net.minecraft.entity.EntityLivingBase, net.minecraft.item.ItemStack)
	 */
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		int dir = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (dir == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if (dir == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		if (dir == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if (dir == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);
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

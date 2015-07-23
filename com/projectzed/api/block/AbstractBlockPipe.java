/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.block;

import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.proxy.ClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * Class containing necessary abstractions for a generic pipe.
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public abstract class AbstractBlockPipe extends BlockContainer {

	protected String customName;
	protected final float PIXEL = 1f / 16f;
	protected final float CALC = 11 * PIXEL / 2;

	/**
	 * @param material = material of pipe
	 * @param name = name of block.
	 */
	public AbstractBlockPipe(Material material, String name) {
		super(material);
		this.customName = name;
		this.setBlockName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(1f);
		this.setBlockBounds(11 * PIXEL / 2, 11 * PIXEL / 2, 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.block.Block#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + this.customName);
	}

	@SideOnly(Side.CLIENT)
	public boolean renderAsNormalBlock() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public abstract int getRenderType();

	@SideOnly(Side.CLIENT)
	public boolean canRenderInPass(int pass) {
		ClientProxy.renderPass = pass;
		return true;
	}

	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 1;
	}
	
	/**
	 * @return tileentity object associated with this pipe.
	 */
	public abstract AbstractTileEntityPipe getTileEntity();
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#createTileEntity(net.minecraft.world.World, int)
	 */
	@Override
	public TileEntity createTileEntity(World world, int id) {
		return getTileEntity();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		if (stack.hasTagCompound() && stack.stackTagCompound != null) {
			NBTTagCompound comp = stack.stackTagCompound;

			AbstractTileEntityPipe te = (AbstractTileEntityPipe) world.getTileEntity(x, y, z);
			te.readFromNBT(comp);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getCollisionBoundingBoxFromPool(net.minecraft.world.World, int, int, int)
	 */
	public abstract AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z);
	
	protected boolean canConnect(World world, double x, double y, double z) {
		return world.getBlock((int) x, (int) y, (int) z) == this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockContainer#breakBlock(net.minecraft.world.World, int, int, int, net.minecraft.block.Block, int)
	 */
	@Override
	public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldBlockMetaData) {
		doBreakBlock(world, x, y, z);
		super.breakBlock(world, x, y, z, oldBlock, oldBlockMetaData);
	}
	
	/**
	 * Method allows for control of behavior of block when being destroyed.
	 * 
	 * @param world = world object.
	 * @param x = x-position.
	 * @param y = y-position.
	 * @param z = z-position.
	 */
	protected abstract void doBreakBlock(World world, int x, int y, int z);

}

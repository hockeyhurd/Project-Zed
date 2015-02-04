/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.generator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.projectzed.api.block.AbstractBlockGenerator;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing solar block array.
 * 
 * @author hockeyhurd
 * @version Oct 20, 2014
 */
public class BlockSolarArray extends AbstractBlockGenerator {

	private final byte TIER;
	
	/**
	 * @param material = material of block
	 */
	public BlockSolarArray(Material material, byte tier) {
		super(material, "solarArray" + (tier > 0 ? tier : ""));
		this.setBlockName("solarArray" + (tier > 0 ? tier : ""));
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(1.0f);
		this.TIER = tier;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockGenerator#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "solar_side");
		this.top = reg.registerIcon(ProjectZed.assetDir + "solar_top" + (this.TIER > 0 ? "_" + this.TIER : ""));
		this.base = reg.registerIcon(ProjectZed.assetDir + "generic_base");
		this.front = this.blockIcon;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockGenerator#getIcon(int, int)
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return side == 1 ? this.top : (side == 0 ? this.base : this.blockIcon);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockGenerator#getTileEntity()
	 */
	@Override
	public AbstractTileEntityGenerator getTileEntity() {
		TileEntitySolarArray te = new TileEntitySolarArray();
		if (this.TIER > 0) te.setTier(this.TIER);
		return te;
	}

	public static void updateBlockState(boolean active, World world, int x, int y, int z) {
		int metaData = world.getBlockMetadata(x, y, z);
		TileEntity tileentity = world.getTileEntity(x, y, z);

		world.setBlockMetadataWithNotify(x, y, z, metaData, 2);

		if (tileentity != null) {
			tileentity.validate();
			world.setTileEntity(x, y, z, tileentity);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.block.AbstractBlockGenerator#onBlockPlacedBy(net.minecraft.world.World, int, int, int,
	 * net.minecraft.entity.EntityLivingBase, net.minecraft.item.ItemStack)
	 */
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
	 * 
	 * @see com.projectzed.api.block.AbstractBlockGenerator#onBlockActivated(net.minecraft.world.World, int, int, int,
	 * net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntitySolarArray te = (TileEntitySolarArray) world.getTileEntity(x, y, z);
			if (te != null) FMLNetworkHandler.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntitySolarArray.class), world, x, y, z);
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.block.AbstractBlockGenerator#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	protected void doBreakBlock(World world, int x, int y, int z) {
		TileEntitySolarArray te = (TileEntitySolarArray) world.getTileEntity(x, y, z);
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

	/**
	 * Method used to detect if a block is above this block (blocking our sunlight).
	 * @param world = world object.
	 * @param x = x-pos.
	 * @param y = y-pos.
	 * @param z = z-pos.
	 */
	public boolean canSeeAbove(World world, int x, int y, int z) {
		boolean clear = true;
		
		if (!world.isRemote) {
			for (int yy = y + 1; yy < 255; yy++) {
				Block b = world.getBlock(x, yy, z);
				if (b != null && !(b instanceof BlockAir)) {
					clear = false;
					break;
				}
			}
		}
		
		return clear;
	}

}

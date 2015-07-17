/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.machines;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.machine.TileEntityStoneCraftingTable;
import com.projectzed.mod.util.WorldUtils;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Block class for stone crafting table.
 * 
 * @author hockeyhurd
 * @version Mar 31, 2015
 */
public class BlockStoneCraftingTable extends BlockContainer {

	private String name;
	
	@SideOnly(Side.CLIENT)
	private IIcon base, top, front;

	/**
	 * @param material
	 */
	public BlockStoneCraftingTable(Material material) {
		super(material);
		this.name = "stoneCraftingTable";
		this.setBlockName(this.name);
		this.setHardness(1.0f);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + this.name + "_side");
		this.top = reg.registerIcon(ProjectZed.assetDir + this.name + "_top");
		this.front = reg.registerIcon(ProjectZed.assetDir + this.name + "_front");
		this.base = Blocks.stone.getBlockTextureFromSide(0);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getIcon(int, int)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? this.top : (side == 0 ? this.base : side != meta ? this.blockIcon : this.front);
	}

	@Override
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
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityStoneCraftingTable te = (TileEntityStoneCraftingTable) world.getTileEntity(x, y, z);
			if (te != null) FMLNetworkHandler.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityStoneCraftingTable.class), world, x, y, z);
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockContainer#breakBlock(net.minecraft.world.World, int, int, int, net.minecraft.block.Block, int)
	 */
	@Override
	public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldBlockMetaData) {
		TileEntityStoneCraftingTable te = (TileEntityStoneCraftingTable) world.getTileEntity(x, y, z);
		
		if (te != null) WorldUtils.dropItemsFromContainerOnBreak(te);
		
		super.breakBlock(world, x, y, z, oldBlock, oldBlockMetaData);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	@Override
	public TileEntity createNewTileEntity(World world, int id) {
		return new TileEntityStoneCraftingTable();
	}

}

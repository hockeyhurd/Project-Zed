/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.hockeyhurd.api.util.Waila;
import com.projectzed.api.block.AbstractBlockFluidContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityLiquidNode;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Block code for liquid node.
 * 
 * @author hockeyhurd
 * @version Feb 16, 2015
 */
public class BlockLiquidNode extends AbstractBlockFluidContainer {

	private IIcon[] icons = new IIcon[4];
	
	/**
	 * @param material
	 */
	public BlockLiquidNode(Material material) {
		super(material, ProjectZed.assetDir, "liquidNode");
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockFluidContainer#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		for (int i = 0; i < icons.length; i++) {
			icons[i] = reg.registerIcon(this.assetDir + this.name + "_" + i);
		}
		
		blockIcon = icons[0];
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockFluidContainer#getIcon(int, int)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		return meta >= 0 && meta < icons.length ? icons[meta] : icons[meta - (meta > icons.length ? icons.length : 1)];
	}
	
	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockFluidContainer#getTileEntity()
	 */
	@Override
	public AbstractTileEntityFluidContainer getTileEntity() {
		return new TileEntityLiquidNode();
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockFluidContainer#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockFluidContainer#onBlockPlacedBy(net.minecraft.world.World, int, int, int, net.minecraft.entity.EntityLivingBase, net.minecraft.item.ItemStack)
	 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		/*int side = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (side == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if (side == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		if (side == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if (side == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);*/
		
		if (!(player instanceof EntityPlayer)) return;
		
		Waila waila = new Waila(stack, world, (EntityPlayer) player, null, 0);
		waila.finder(false);
		
		int w = waila.getSideHit();
		
		// if (w == 4) w--;
		
		world.setBlockMetadataWithNotify(x, y, z, w, 2);
		
		ProjectZed.logHelper.info("Side:", w);

		if (stack.hasDisplayName()) ((AbstractTileEntityMachine) world.getTileEntity(x, y, z)).setCustomName(stack.getDisplayName());
	}
	
	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockFluidContainer#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	@Override
	protected void doBreakBlock(World world, int x, int y, int z) {
	}

}

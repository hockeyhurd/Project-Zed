/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import com.hockeyhurd.hcorelib.api.util.Waila;
import com.projectzed.api.block.AbstractBlockFluidContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityLiquidNode;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Block code for liquid node.
 * 
 * @author hockeyhurd
 * @version Feb 16, 2015
 */
public class BlockLiquidNode extends AbstractBlockFluidContainer {
	
	private IIcon input, output, neutral;
	
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
		blockIcon = neutral = reg.registerIcon(assetDir + name + "_side_neutral");
		input = reg.registerIcon(assetDir + name + "_side_input");
		output = reg.registerIcon(assetDir + name + "_side_output");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockFluidContainer#getIcon(int, int)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		if (/*side == 3 && */meta == 0) return neutral;
		return side == meta - 1 ? output : input;
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
		
		ForgeDirection dir = ForgeDirection.getOrientation(w);
		
		// world.setBlockMetadataWithNotify(x, y, z, w, 2);
		world.setBlockMetadataWithNotify(x, y, z, dir.ordinal() + 1, 2);

		if (stack.hasDisplayName()) ((AbstractTileEntityMachine) world.getTileEntity(x, y, z)).setCustomName(stack.getDisplayName());
		
		if (stack.hasTagCompound() && stack.stackTagCompound != null) {
			NBTTagCompound comp = stack.stackTagCompound;

			AbstractTileEntityFluidContainer te = (AbstractTileEntityFluidContainer) world.getTileEntity(x, y, z);
			te.readNBT(comp);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockFluidContainer#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	@Override
	protected void doBreakBlock(World world, int x, int y, int z) {
	}

}

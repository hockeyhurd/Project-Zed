/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.proxy.ClientProxy;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class containing block code for energy bank cell.
 * 
 * @author hockeyhurd
 * @version Dec 3, 2014
 */
public class BlockEnergyCell extends AbstractBlockContainer {

	private final byte TIER;
	
	/**
	 * @param material
	 * @param name
	 */
	public BlockEnergyCell(Material material, String name) {
		super(material, ProjectZed.assetDir, name);
		this.setRegistryName(name);
		this.TIER = Byte.parseByte("" + name.charAt(name.length() - 1));
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#renderAsNormalBlock()
	 */
	@SideOnly(Side.CLIENT)
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#isOpaqueCube()
	 */
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#canRenderInPass(int)
	 */
	@SideOnly(Side.CLIENT)
	public boolean canRenderInPass(int pass) {
		ClientProxy.renderPass = pass;
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getRenderBlockPass()
	 */
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getRenderType()
	 */
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return ClientProxy.energyCell;
	}
	
	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#getTileEntity()
	 */
	@Override
	public AbstractTileEntityEnergyContainer getTileEntity() {
		TileEntityEnergyBankBase te = new TileEntityEnergyBankBase();
		
		if (this.TIER > 0) te.setTier(this.TIER);
		return te;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityEnergyBankBase te = (TileEntityEnergyBankBase) world.getTileEntity(x, y, z);
			if (te != null) {
				if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemWrench))
					FMLNetworkHandler
							.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityEnergyBankBase.class), world, x, y, z);

				else return false;
			}

			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockPlacedBy(net.minecraft.world.World, int, int, int, net.minecraft.entity.EntityLivingBase, net.minecraft.item.ItemStack)
	 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase e, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, e, stack);
		if (stack.hasTagCompound() && stack.stackTagCompound != null) {
			NBTTagCompound comp = stack.stackTagCompound;

			TileEntityEnergyBankBase te = (TileEntityEnergyBankBase) world.getTileEntity(x, y, z);
			te.readNBT(comp);

			/*for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				te.setSideValve(dir, comp.getByte(dir.name()));
			}*/
		}
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	@Override
	protected void doBreakBlock(World world, int x, int y, int z) {
		TileEntityEnergyBankBase te = (TileEntityEnergyBankBase) world.getTileEntity(x, y, z);
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

}

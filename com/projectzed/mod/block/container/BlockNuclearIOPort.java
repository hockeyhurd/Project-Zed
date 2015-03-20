/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.block.IMetaUpdate;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.container.TileEntityNuclearIOPort;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing block code for nuclearIOPort.
 * 
 * @author hockeyhurd
 * @version Mar 19, 2015
 */
public class BlockNuclearIOPort extends AbstractBlockNuclearComponent implements IMetaUpdate {

	@SideOnly(Side.CLIENT)
	private IIcon input, output;
	
	public BlockNuclearIOPort() {
		super("nuclearIOPort");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockNuclearComponent#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		super.registerBlockIcons(reg);
		input = reg.registerIcon(this.assetDir + this.name + "_input");
		output = reg.registerIcon(this.assetDir + this.name + "_output");
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getIcon(int, int)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		return meta == 1 ? input : meta == 2 ? output : blockIcon;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.IMetaUpdate#updateMeta(boolean, net.minecraft.world.World, com.hockeyhurd.api.math.Vector4Helper)
	 */
	@Override
	public void updateMeta(boolean isActive, World world, Vector4Helper<Integer> vec) {
		TileEntity te = world.getTileEntity(vec.x, vec.y, vec.z);
		
		if (te != null && te instanceof TileEntityNuclearIOPort) updateMeta(0, world, vec);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.IMetaUpdate#updateMeta(int, net.minecraft.world.World, com.hockeyhurd.api.math.Vector4Helper)
	 */
	@Override
	public void updateMeta(int meta, World world, Vector4Helper<Integer> vec) {
		TileEntity te = world.getTileEntity(vec.x, vec.y, vec.z);
		
		if (te != null && te instanceof TileEntityNuclearIOPort) {
			int current = te.getBlockMetadata();
			
			if (current == 0) meta = 1;
			
			world.setBlockMetadataWithNotify(vec.x, vec.y, vec.z, meta, 2);
			world.markBlockForUpdate(vec.x, vec.y, vec.z);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityNuclearIOPort te = (TileEntityNuclearIOPort) world.getTileEntity(x, y, z);
			if (te != null)
				if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() != null
						&& player.getCurrentEquippedItem().getItem() instanceof ItemWrench) return false;
				FMLNetworkHandler.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityNuclearIOPort.class), world, x, y, z);
			
			return true;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockNuclearComponent#getTileEntity()
	 */
	@Override
	public AbstractTileEntityNuclearComponent getTileEntity() {
		return new TileEntityNuclearIOPort();
	}

}

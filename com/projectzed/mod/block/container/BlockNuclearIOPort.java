/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.block.IMetaUpdate;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.container.TileEntityNuclearIOPort;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

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
	 * @see com.projectzed.api.block.IMetaUpdate#updateMeta(boolean, net.minecraft.world.World, com.hockeyhurd.api.math.Vector4)
	 */
	@Override
	public void updateMeta(boolean isActive, World world, Vector3<Integer> vec) {
		TileEntity te = world.getTileEntity(vec.x, vec.y, vec.z);
		
		if (te != null && te instanceof TileEntityNuclearIOPort) {
			byte meta = ((TileEntityNuclearIOPort) te).getStoredMeta();
			
			if (meta == 0) meta = 1;
			
			world.setBlockMetadataWithNotify(vec.x, vec.y, vec.z, meta, 2);
			world.markBlockForUpdate(vec.x, vec.y, vec.z);
			
			((TileEntityNuclearIOPort) te).setMetaOnUpdate(meta);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.IMetaUpdate#updateMeta(int, net.minecraft.world.World, com.hockeyhurd.api.math.Vector4)
	 */
	@Override
	public void updateMeta(int meta, World world, Vector3<Integer> vec) {
		TileEntity te = world.getTileEntity(vec.x, vec.y, vec.z);
		
		if (te != null && te instanceof TileEntityNuclearIOPort) {
			
			world.setBlockMetadataWithNotify(vec.x, vec.y, vec.z, meta, 2);
			world.markBlockForUpdate(vec.x, vec.y, vec.z);
			
			((TileEntityNuclearIOPort) te).setMetaOnUpdate((byte) meta);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockPlacedBy(net.minecraft.world.World, int, int, int, net.minecraft.entity.EntityLivingBase, net.minecraft.item.ItemStack)
	 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase e, ItemStack stack) {
		if (stack.hasTagCompound() && stack.stackTagCompound != null) {
			NBTTagCompound comp = stack.stackTagCompound;
			byte meta = comp.getByte("Meta");
			
			TileEntityNuclearIOPort te = (TileEntityNuclearIOPort) world.getTileEntity(x, y, z);
			
			if (meta == 0) updateMeta(1, world, new Vector3<Integer>(x, y, z));
			
			else updateMeta(meta, world, new Vector3<Integer>(x, y, z));
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
			if (te != null) {
				if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemWrench)) FMLNetworkHandler
						.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityNuclearIOPort.class), world, x, y, z);

				else return false;
			}
			
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

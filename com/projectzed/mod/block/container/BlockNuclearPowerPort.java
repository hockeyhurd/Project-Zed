/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.block.IMetaUpdate;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityNuclearPowerPort;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for nuclearPowerPort.
 * 
 * @author hockeyhurd
 * @version Mar 11, 2015
 */
public class BlockNuclearPowerPort extends AbstractBlockContainer implements IMetaUpdate {

	@SideOnly(Side.CLIENT)
	private IIcon activeIcon;
	
	public BlockNuclearPowerPort() {
		super(Material.rock, ProjectZed.assetDir, "nuclearPowerPort");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		super.registerBlockIcons(reg);
		activeIcon = reg.registerIcon(this.assetDir + this.name + "_on");
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getIcon(int, int)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		return meta == 0 ? blockIcon : activeIcon;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.IMetaUpdate#updateMeta(boolean, net.minecraft.world.World, com.hockeyhurd.api.math.Vector4Helper)
	 */
	@Override
	public void updateMeta(boolean isActive, World world, Vector4Helper<Integer> vec) {
		updateMeta(isActive ? 1 : 0, world, vec);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.IMetaUpdate#updateMeta(int, net.minecraft.world.World, com.hockeyhurd.api.math.Vector4Helper)
	 */
	@Override
	public void updateMeta(int meta, World world, Vector4Helper<Integer> vec) {
		TileEntityNuclearPowerPort te = (TileEntityNuclearPowerPort) world.getTileEntity(vec.x, vec.y, vec.z);
		
		if (te != null) world.setBlockMetadataWithNotify(vec.x, vec.y, vec.z, meta, 2);
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#getTileEntity()
	 */
	@Override
	public AbstractTileEntityEnergyContainer getTileEntity() {
		return new TileEntityNuclearPowerPort();
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	@Override
	protected void doBreakBlock(World world, int x, int y, int z) {
		TileEntityNuclearPowerPort te = (TileEntityNuclearPowerPort) world.getTileEntity(x, y, z);
		if (te != null && te.hasMaster()) ProjectZed.logHelper.info("Broke TE @:", te.worldVec().toString(), "with stored power of:", te.getEnergyStored());
	}

}

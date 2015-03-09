/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.tileentity.container.TileEntityNuclearChamberLock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing block code for nuclearChamberLock.
 * 
 * @author hockeyhurd
 * @version Dec 14, 2014
 */
public class BlockNuclearChamberLock extends AbstractBlockNuclearComponent {
	
	@SideOnly(Side.CLIENT)
	private IIcon locked;
	
	public BlockNuclearChamberLock() {
		super("nuclearChamberLock");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockNuclearComponent#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(this.assetDir + this.name + "_locked");
		locked = reg.registerIcon(this.assetDir + this.name);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getIcon(int, int)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		return meta == 0 ? blockIcon : locked;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockNuclearComponent#getTileEntity()
	 */
	@Override
	public AbstractTileEntityNuclearComponent getTileEntity() {
		return new TileEntityNuclearChamberLock();
	}
	
	/**
	 * Method to update structure metadata and icon.
	 * 
	 * @param isConnected flag whether is connected/active or not.
	 * @param world world object as reference.
	 * @param vec coordinate vector.
	 */
	public void updateStructure(boolean isConnected, World world, Vector4Helper<Integer> vec) {
		TileEntity te = world.getTileEntity(vec.x, vec.y, vec.z);
		
		if (te != null && te instanceof TileEntityNuclearChamberLock) {
			
			int ret = isConnected ? 1 : 0;
			world.setBlockMetadataWithNotify(vec.x, vec.y, vec.z, ret, 2);
			world.markBlockForUpdate(vec.x, vec.y, vec.z);
		}
	}
	 
}

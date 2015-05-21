/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.block.IMetaUpdate;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.tileentity.TileEntityNuclearControlPort;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing block code for nuclearControlPort.
 * 
 * @author hockeyhurd
 * @version Mar 12, 2015
 */
public class BlockNuclearControlPort extends AbstractBlockNuclearComponent implements IMetaUpdate {

	private boolean active;
	
	@SideOnly(Side.CLIENT)
	private IIcon activeIcon;
	
	public BlockNuclearControlPort() {
		super(Material.rock, "nuclearControlPort");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockNuclearComponent#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
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

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockNuclearComponent#getTileEntity()
	 */
	@Override
	public AbstractTileEntityNuclearComponent getTileEntity() {
		return new TileEntityNuclearControlPort();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onNeighborBlockChange(net.minecraft.world.World, int, int, int, net.minecraft.block.Block)
	 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
		if (!world.isRemote) {
			TileEntityNuclearControlPort te = (TileEntityNuclearControlPort) world.getTileEntity(x, y, z);
			
			if (te != null) {
				boolean active = world.isBlockIndirectlyGettingPowered(x, y, z);
				te.setRedstoneSignal(active);
				updateMeta(active ? 1 : 0, world, te.worldVec());
				if (active) this.setLightLevel(0.75f);
				else this.setLightLevel(0.0f);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.IMetaUpdate#updateMeta(boolean, net.minecraft.world.World, com.hockeyhurd.api.math.Vector3)
	 */
	@Override
	public void updateMeta(boolean isActive, World world, Vector3<Integer> vec) {
		// updateMeta(isActive ? 1 : 0, world, vec);
	}

	@Override
	public void updateMeta(int meta, World world, Vector3<Integer> vec) {
		TileEntityNuclearControlPort te = (TileEntityNuclearControlPort) world.getTileEntity(vec.x, vec.y, vec.z);
		
		if (te != null) {
			world.setBlockMetadataWithNotify(vec.x, vec.y, vec.z, meta, 2);
			world.markBlockForUpdate(vec.x, vec.y, vec.z);
		}
	}

}

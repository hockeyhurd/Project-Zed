/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeClear;

/**
 * 
 * @author hockeyhurd
 * @version Nov 15, 2014
 */
public class BlockEnergyPipeClear extends AbstractBlockEnergyPipe {

	/**
	 * @param material
	 * @param name
	 * @param color
	 */
	public BlockEnergyPipeClear(Material material, String name, EnumColor color) {
		super(material, name, color);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.block.container.BlockEnergyPipeBase#getTileEntity()
	 */
	@Override
	public AbstractTileEntityPipe getTileEntity() {
		return new TileEntityEnergyPipeClear();
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	@Override
	public TileEntity createNewTileEntity(World world, int id) {
		return getTileEntity();
	}

}

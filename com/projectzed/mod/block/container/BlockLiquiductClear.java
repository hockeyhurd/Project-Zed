/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;

import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductClear;

/**
 * Class containing block code for liquiductClear
 * 
 * @author hockeyhurd
 * @version Apr 9, 2015
 */
public class BlockLiquiductClear extends AbstractBlockLiquiduct {

	/**
	 * @param material
	 */
	public BlockLiquiductClear(Material material) {
		super(material, "liquiductClear", EnumColor.CLEAR);
	}

	/* (non-Javadoc)
	 * @see com.projectzed.mod.block.container.AbstractBlockLiquiduct#getTileEntity()
	 */
	@Override
	public AbstractTileEntityPipe getTileEntity() {
		return new TileEntityLiquiductClear();
	}

}

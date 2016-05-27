/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeOrange;
import net.minecraft.block.material.Material;

/**
 * Class used to create more specific instance of base class
 * in which this class inherits from.
 * 
 * @author hockeyhurd
 * @version Nov 13, 2014
 */
public class BlockEnergyPipeOrange extends AbstractBlockEnergyPipe {

	/**
	 * @param material
	 * @param name
	 * @param color
	 */
	public BlockEnergyPipeOrange(Material material, String name, EnumColor color) {
		super(material, name, color);
	}
	
	@Override
	public AbstractTileEntityPipe getTileEntity() {
		return new TileEntityEnergyPipeOrange();
	}
	
}

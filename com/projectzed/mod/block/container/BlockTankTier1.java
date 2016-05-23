/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankTier1;
import net.minecraft.block.material.Material;

/**
 * Tier 1 fluid tank block code.
 * 
 * @author hockeyhurd
 * @version Jan 24, 2015
 */
public class BlockTankTier1 extends AbstractBlockTankBase {

	/**
	 * @param material
	 */
	public BlockTankTier1(Material material) {
		super(material, "fluidTankTier1");
		this.tier = (byte) 1;
	}


	@Override
	public AbstractTileEntityFluidContainer getTileEntity() {
		TileEntityFluidTankTier1 tank = new TileEntityFluidTankTier1();
		tank.setTier((byte) 1);
		return tank;
	}

}

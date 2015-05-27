/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container.pipe;

import com.projectzed.api.energy.source.EnumColor;

/**
 * Class containing tileentity code for liquiductClear.
 * 
 * @author hockeyhurd
 * @version Apr 9, 2015
 */
public class TileEntityLiquiductClear extends TileEntityLiquiductBase {

	public TileEntityLiquiductClear() {
		super("liquiductClear");
		this.importRate = 250;
		this.exportRate = 250;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductBase#getColor()
	 */
	@Override
	public EnumColor getColor() {
		return EnumColor.CLEAR;
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container.pipe;

import com.projectzed.api.energy.source.EnumColor;

/**
 * Class containing te code for liquiductBlue.
 * 
 * @author hockeyhurd
 * @version Feb 13, 2015
 */
public class TileEntityLiquiductBlue extends TileEntityLiquiductBase {

	public TileEntityLiquiductBlue() {
		super("liquiductBlue");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductBase#getColor()
	 */
	@Override
	public EnumColor getColor() {
		return EnumColor.BLUE;
	}

}

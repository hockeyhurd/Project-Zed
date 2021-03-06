/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.api.fluid;

import com.hockeyhurd.hcorelib.api.math.pathfinding.IPathTile;
import net.minecraftforge.fluids.FluidTank;

/**
 * Simple extension of IPathTile with little to no added features.
 *
 * @see com.hockeyhurd.hcorelib.api.math.pathfinding.IPathTile
 *
 * @author hockeyhurd
 * @version 4/15/2016.
 */
public interface IFluidTile extends IPathTile {

	/**
	 * Gets the tank associated with fluid tile.
	 *
	 * @return FluidTank.
	 */
	FluidTank getTank();

}

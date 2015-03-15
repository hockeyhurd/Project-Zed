/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.block;

import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector4Helper;

/**
 * Simple interface to allow updating of block metadata through method call.
 * <br><bold>NOTE: </bold>This interface should be implemented in block's class.
 * 
 * @author hockeyhurd
 * @version Mar 11, 2015
 */
public interface IMetaUpdate {

	/**
	 * Method used to update texture through metadata.
	 * 
	 * @param isActive set whether tileentity is active or not.
	 * @param world world object as reference.
	 * @param vec world vector coordinate.
	 */
	void updateMeta(boolean isActive, World world, Vector4Helper<Integer> vec);
	
	/**
	 * Method used to update texture through metadata.
	 * 
	 * @param meta metadata to set.
	 * @param world world object as reference.
	 * @param vec world vector coordinate.
	 */
	void updateMeta(int meta, World world, Vector4Helper<Integer> vec);
	
}

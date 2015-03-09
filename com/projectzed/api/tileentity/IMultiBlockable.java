/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity;

import java.util.List;

import net.minecraft.block.Block;

import com.hockeyhurd.api.math.Vector4Helper;

/**
 * Interface for multiblock structures to be implemented in the TE's class.
 * 
 * @author hockeyhurd
 * @version Jan 2, 2015
 */
public interface IMultiBlockable<T extends AbstractTileEntityGeneric> {

	/** Get the tile's object. */
	T getInstance();

	/** Get the block associated with this object. */
	Block getBlock();
	
	/** Get the location in world of TE. */
	Vector4Helper<Integer> worldVec();
	
	/** Whether we can have more than one of these in structure. */
	boolean isUnique();
	
	/** Whether this block can sub-able. */
	boolean isSubstituable();
	
	/** If has a substitutable block, put in this list, else leave null. */
	List<IMultiBlockable> getSubList();
	
	/**
	 * Get the amount of blocks should have per dimension.
	 * 
	 * @param width width of multiblock structure.
	 * @param height height width of multiblock structure.
	 * @param depth depth width of multiblock structure.
	 * @return amount of said block allowed in multiblock structure.
	 */
	int getAmountFromSize(int width, int height, int depth);
	
	/** Gets whether this is the MASTER TE object. */
	boolean isMaster();
	
	/** Set whether this object is to be the MASTER TE object. */
	void setIsMaster(boolean master);
	
	/** Gets whether this object has a MASTER TE object. */
	boolean hasMaster();
	
	/** Sets whether this object has a MASTER TE object in its multiblock structure. */
	void setHasMaster(boolean master);
	
	/** Sets the location of the MASTER TE object for reference. */
	void setMasterVec(Vector4Helper<Integer> vec);
	
	/** Gets the coordinates of the MASTER TE object in structure. */
	Vector4Helper<Integer> getMasterVec();
	
	/**
	 * Method to handle to reset internal data of multiblock structure.
	 */
	void reset();
	
}

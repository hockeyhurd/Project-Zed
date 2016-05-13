/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.fluid.container;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.api.fluid.FluidNetwork;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Helper interface to get more relevant info for this te tank.
 * 
 * @author hockeyhurd
 * @version Feb 13, 2015
 */
public interface IFluidContainer extends IFluidHandler {

	/**
	 * Function to get fluid tank associated with this te.
	 * 
	 * @return tank object.
	 */
	FluidTank getTank();
	
	/**
	 * Shortened function to get the name of the fluid in this tank currently.
	 * 
	 * @return localized name if has fluid else should be empty.
	 */
	String getLocalizedFluidName();
	
	/**
	 * Function to get max import rate for te.
	 * 
	 * @return max import rate.
	 */
	int getMaxFluidImportRate();
	
	/**
	 * Function to get max export rate for te.
	 * 
	 * @return max export rate.
	 */
	int getMaxFluidExportRate();
	
	/**
	 * Flag whether is a pipe or not.
	 * 
	 * @return true if a pipe, else returns false.
	 */
	boolean isPipe();
	
	/**
	 * Method to set last received direction.
	 * 
	 * @param dir direction to set.
	 */
	void setLastReceivedDirection(EnumFacing dir);
	
	/**
	 * Getter function to get last received direction.
	 * 
	 * @return last received direction.
	 */
	EnumFacing getLastReceivedDirection();
	
	/**
	 * Function to get world coordinate of te.
	 * 
	 * @return world vector.
	 */
	Vector3<Integer> worldVec();
	
	// Start fluid networking:
	
	/**
	 * Gets whether this container/tank can be of a source node.
	 * 
	 * @return flag.
	 */
	boolean canBeSourceNode();
	
	/**
	 * Gets whether this container can serve as 'master' of given fluid network.
	 * 
	 * @return true if eligible, else returns false.
	 */
	boolean canBeMaster();
	
	/**
	 * @return true if master of fluid network, else can return false.
	 */
	boolean isMaster();

	/**
	 * Sets whether is master or not.
	 *  
	 * @param master value to set.
	 */
	void setMaster(boolean master);
	
	/**
	 * @return if this container/tank has a fluid network.
	 */
	boolean hasFluidNetwork();
	
	/**
	 * @return fluid network if applicable, else returns null.
	 */
	FluidNetwork getNetwork();
	
	/**
	 * Method allows 'masters' to set fluid network object to fluid container.
	 * 
	 * @param network network to set.
	 */
	void setFluidNetwork(FluidNetwork network);
	
}

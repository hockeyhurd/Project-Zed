/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.energy.storage;

import net.minecraftforge.common.util.ForgeDirection;

import com.hockeyhurd.api.math.Vector3;

/**
 * Interface for an object (TileEntity, tool, etc.) that contains power.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public interface IEnergyContainer {

	/** Max allowed capacity */
	public void setMaxStorage(int max);

	/** Get the max capacity */
	public int getMaxStorage();

	/** Set the amount of energy stored. */
	public void setEnergyStored(int amount);

	/** Get the amount currently stored. */
	public int getEnergyStored();

	/** Function used to get the max import rate */
	public int getMaxImportRate();

	/** Function used to get the max export rate */
	public int getMaxExportRate();

	/**
	 * Function used to request power from one container to another with given amount
	 * @param cont = te reference.
	 * @param amount = amount of energy requested.
	 * @return amount of energy able to obtain.
	 */
	public int requestPower(IEnergyContainer cont, int amount);
	
	/**
	 * Function used to add power to this container from another.
	 * @param cont = container from as reference.
	 * @param amount = amount of energy able to add.
	 * @return
	 */
	public int addPower(IEnergyContainer cont, int amount);
	
	/**
	 * Sets the last received direction.
	 * @param dir = direction received from.
	 */
	public void setLastReceivedDirection(ForgeDirection dir);
	
	/**
	 * @return the last received direction.
	 */
	public ForgeDirection getLastReceivedDirection();

	/** Gets and stored the vector co-ordinates of this te. */
	public Vector3<Integer> worldVec();

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.energy.storage;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import net.minecraft.util.EnumFacing;

/**
 * Interface for an object (TileEntity, tool, etc.) that contains power.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public interface IEnergyContainer {

	/** Max allowed capacity */
	void setMaxStorage(int max);

	/** Get the max capacity */
	int getMaxStorage();

	/** Set the amount of energy stored. */
	void setEnergyStored(int amount);

	/** Get the amount currently stored. */
	int getEnergyStored();

	/** Function used to get the max import rate */
	int getMaxImportRate();

	/** Function used to get the max export rate */
	int getMaxExportRate();

	/**
	 * Function used to request power from one container to another with given amount
	 * @param cont = te reference.
	 * @param amount = amount of energy requested.
	 * @return amount of energy able to obtain.
	 */
	int requestPower(IEnergyContainer cont, int amount);
	
	/**
	 * Function used to add power to this container from another.
	 * @param cont = container from as reference.
	 * @param amount = amount of energy able to add.
	 * @return
	 */
	int addPower(IEnergyContainer cont, int amount);
	
	/**
	 * Sets the last received direction.
	 * @param dir = direction received from.
	 */
	void setLastReceivedDirection(EnumFacing dir);
	
	/**
	 * @return the last received direction.
	 */
	EnumFacing getLastReceivedDirection();

	/** Gets and stored the vector co-ordinates of this te. */
	Vector3<Integer> worldVec();

}

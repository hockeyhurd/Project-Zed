/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.energy.machine;

import com.projectzed.api.energy.storage.IEnergyContainer;

/**
 * Interface for all machines
 * 
 * @author hockeyhurd
 * @version Oct 23, 2014
 */
public interface IEnergyMachine extends IEnergyContainer {

	/** Method used to set the status of machine (i.e. is it 'on') */
	public void setPowerMode(boolean val);
	
	/** Function used to get whether the machine is on. */
	public boolean isPoweredOn();
	
	/** Sets the energy burn rate of the machine. */
	public void setEnergyBurnRate(int val);
	
	/** Gets the energy burn rate of the machine. */
	public int getEnergyBurnRate();
	
	/** Method used to burn energy while in use. */
	public void burnEnergy();
	
}

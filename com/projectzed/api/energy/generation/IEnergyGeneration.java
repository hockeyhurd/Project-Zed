/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.energy.generation;

import com.projectzed.api.energy.source.Source;
import com.projectzed.api.energy.storage.IEnergyContainer;

/**
 * Interface controlling how much energy can be created per tick.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public interface IEnergyGeneration extends IEnergyContainer {

	/** Method used to define source of power. */
	public void defineSource();
	
	/** Getter function for getting the source of power defined. */
	public Source getSource();
	
	/** Method used for power generation */
	public void generatePower();
	
	/** Function returning whether this machine can currently produce power. */
	public boolean canProducePower();
	
	/** Method used to set whether this generator can currently produce its power. */
	public void setPowerMode(boolean state);
	
	/** Method used for dispersing power to all other nodes. */
	public void transferPower();
	
}

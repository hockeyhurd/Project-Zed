/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity;


/**
 * Interface to be implemented for the controller of the multiblock structure.
 * 
 * @author hockeyhurd
 * @version Feb 22, 2015
 */
public interface IMultiBlockableController<T extends AbstractTileEntityGeneric> extends IMultiBlockable<AbstractTileEntityGeneric> {

	/**
	 * Function to force re-check of multiblock structure.
	 * 
	 * @return true if is multiblock structure, else returns false.
	 */
	boolean checkMultiBlockForm();

	/**
	 * Function to force check for master in multiblock structure.
	 * 
	 * @return true if has a master, else returns false.
	 */
	boolean checkForMaster();
	
	/**
	 * Method used to reset the essence of multiblock structure.
	 */
	void resetStructure();
	
	/**
	 * Method to handle to reset internal data of multiblock structure.
	 */
	void reset();
	
}

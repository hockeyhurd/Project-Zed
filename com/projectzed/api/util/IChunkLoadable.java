/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.util;

import net.minecraftforge.common.ForgeChunkManager.Ticket;

/**
 * Simple interface for any tileentity that can force chunk loading.
 * 
 * @author hockeyhurd
 * @version Apr 22, 2015
 */
public interface IChunkLoadable {

	/**
	 * Validates tileentity.
	 */
	void validate();
	
	/**
	 * Invalidates tileentity.
	 */
	void invalidate();

	/**
	 * Forces chunk loading from provided ticket.
	 * 
	 * @param ticket ticket to reference for chunk loading.
	 */
	void loadChunk(Ticket ticket);
	
	/**
	 * Releases ticket and removes force chunk loading operation.
	 */
	void unloadChunk();
	
}

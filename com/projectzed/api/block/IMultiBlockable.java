/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.block;

import java.util.List;

import net.minecraft.block.Block;

/**
 * Interface for multiblock structures to be implemented in the block's class.
 * 
 * @author hockeyhurd
 * @version Jan 2, 2015
 */
public interface IMultiBlockable<T extends Block> {

	/** Get the block's object. */
	public T getInstance();
	
	/** Whether we can have more than one of these in structure. */
	public boolean isUnique();
	
	/** Whether this block can sub-able. */
	public boolean isSubstituable();
	
	/** If has a substituable block, put in this list, else leave null. */
	public List<IMultiBlockable> getSubList();
	
	/** Get the amount of blocks should have  */
	public int getAmountFromSize(int size);
	
}

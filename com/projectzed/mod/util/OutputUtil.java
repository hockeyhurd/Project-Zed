/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.util;

import net.minecraft.item.ItemStack;

/**
 * Small util class using in mapping stacks with numerical data.
 * 
 * @author hockeyhurd
 * @version May 5, 2015
 */
public class OutputUtil {

	public final ItemStack stack;
	public final float xp;
	
	/**
	 * @param stack itemstack
	 * @param xp xp/data.
	 */
	public OutputUtil(ItemStack stack, float xp) {
		this.stack = stack;
		this.xp = xp;
	}
	
	/**
	 * Checks to see if itemstack is valid.
	 * 
	 * @return true if valid, else returns false.
	 */
	public boolean isValid() {
		return stack != null && stack.stackSize > 0;
	}

}

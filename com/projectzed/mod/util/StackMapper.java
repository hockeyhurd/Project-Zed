/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.util;

import net.minecraft.item.ItemStack;

/**
 * Class containing code for mapping itemstacks, a utilitity object used <br>
 * primarily in registries.
 * 
 * @author hockeyhurd
 * @version Dec 30, 2014
 */
public class StackMapper<T> {

	private final T[] ARRAY;
	
	/**
	 * @param output = output of recipe.
	 * @param array = input stack array.
	 */
	public StackMapper(T... array) {
		this.ARRAY = array;
	}
	
	/**
	 * @return size of input array.
	 */
	public int size() {
		return this.ARRAY.length;
	}

	/**
	 * Function used to get an itemstack at said index of input array.
	 * 
	 * @param index = index to get itemstack from.
	 * @return itemstack at index of array.
	 */
	public T get(int index) {
		return index >= 0 && index < size() ? this.ARRAY[index] : null;
	}
	
	/**
	 * Function used to see if array contains Object<T> in array.
	 * 
	 * @param check = object to check.
	 * @return true if contains said object, else returns false.
	 */
	public boolean contains(T check) {
		boolean flag = false;

		// If we are dealing with itemstacks, do this:
		if (check instanceof ItemStack) {
			ItemStack stack;
			for (T index : getArray()) {
				stack = (ItemStack) index;
				if (stack.getItem() == ((ItemStack) check).getItem() && stack.getItemDamage() == ((ItemStack) check).getItemDamage()) {
					flag = true;
					break;
				}
			}
		}
		
		// Else, generic checking should suffice.
		else { 
			for (T index : getArray()) {
				if (index == check) {
					flag = true;
					break;
				}
			}
		}
		
		return flag;
	}
	
	/**
	 * @return input itemstack array.
	 */
	public T[] getArray() {
		return this.ARRAY;
	}
	
	/**
	 * Function used to copy and replicate one array to another. 
	 * 
	 * @param refMap = map to copy from as reference.
	 * @param length = length of new array.
	 * @return copied array if successful, else returns null.
	 */
	public static StackMapper[] copyOf(StackMapper[] refMap, int length) {
		StackMapper[] outMap = null;
		
		if (refMap != null && refMap.length > 0 && length > 0 && length < refMap.length) {
			outMap = new StackMapper[length];
			
			for (int i = 0; i < length; i++) { 
				outMap[i] = refMap[i];
			}
		}
		
		return outMap;
	}
	

}

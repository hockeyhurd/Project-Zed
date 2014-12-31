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
		
		for (T map : getArray()) {
			if (map == check) {
				flag = true;
				break;
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

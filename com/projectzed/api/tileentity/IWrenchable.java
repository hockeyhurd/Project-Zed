package com.projectzed.api.tileentity;

import net.minecraft.item.ItemStack;

import com.hockeyhurd.api.math.Vector4Helper;

/**
 * Interface for all tileentities that can be wrenched.
 * <br><bold>NOTE:</bold> This should be implemented on the te side.
 * 
 * @author hockeyhurd
 * @version Feb 3, 2015
 */
public interface IWrenchable {

	/**
	 * Method to handle how the requesting wrench action should be handled.
	 * 
	 * @param stack stack reference to item in hand, usually the wrench itself.
	 * @param requestType reference to request being made (generally meaning: 0 - rotate, 1 - pickup, 2 - other if specified).
	 */
	void handleWrenchAction(ItemStack stack, byte requestType);
	
	/**
	 * Helper function to get how the tileentity as a block should be rotated.
	 * <br><bold>NOTE:</bold> This function can be nullified if 'canRotateTE' is set to false.
	 * 
	 * @return byte array containing rotation matrix data.
	 */
	byte[] getRotationMatrix();
	
	/**
	 * Function to get whether this te can be rotated or not.
	 * 
	 * @return true if can be rotated else returns false.
	 */
	boolean canRotateTE();
	
	/**
	 * Function to get whether picking up tileentity should save its data and contents.
	 * 
	 * @return true if can be saved else returns false (voids stored data).
	 */
	boolean canSaveDataOnPickup();
	
	/**
	 * Function to get world coordinates of wrenchable tileentity.
	 * 
	 * @return vector4 component.
	 */
	Vector4Helper<Integer> worldVec();
	
}

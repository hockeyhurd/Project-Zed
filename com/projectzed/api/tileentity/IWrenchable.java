/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity;

import com.hockeyhurd.api.math.Vector3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Interface for all tileentities that can be wrenched.
 * <br><bold>NOTE:</bold> This should be implemented on the te side.
 * 
 * @author hockeyhurd
 * @version Feb 3, 2015
 */
public interface IWrenchable {
	
	/**
	 * Helper function to get how the tileentity as a block should be rotated.
	 * <br><bold>NOTE:</bold> This function can be nullified if 'canRotateTE' is set to false.
	 * 
	 * @return byte containing new meta data after being rotated.
	 */
	byte getRotatedMeta(byte facingDir, byte currentMeta);
	
	/**
	 * Function to get whether this te can be rotated or not.
	 * 
	 * @return true if can be rotated else returns false.
	 */
	boolean canRotateTE();
	
	/**
	 * Method called when wrench was used on tileentity.
	 * 
	 * @param stack itemstack, (reference to wrench itself).
	 * @param player player interacted with.
	 * @param world world used.
	 * @param vec vector of tileentity.
	 */
	void onInteract(ItemStack stack, EntityPlayer player, World world, Vector3<Integer> vec);
	
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
	Vector3<Integer> worldVec();
	
}

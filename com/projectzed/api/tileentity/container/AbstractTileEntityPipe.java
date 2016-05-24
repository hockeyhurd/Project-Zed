/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity.container;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.api.tileentity.IWrenchable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Generic class used for creating new te pipes for transport.
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public abstract class AbstractTileEntityPipe extends AbstractTileEntityContainer implements IWrenchable {

	/** UP, DOWN, NORTH, EAST, SOUTH, WEST */
	protected EnumFacing[] connections;
	
	/** The last know received direction received from. */
	protected EnumFacing lastReceivedDir;

	/**
	 * @param name name of tileentity.
	 */
	public AbstractTileEntityPipe(String name) {
		super(name);
		connections = new EnumFacing[6];
	}
	
	/**
	 * Get all connections held in array.
	 * 
	 * @return forgedirectional array.
	 */
	public EnumFacing[] getConnections() {
		return connections;
	}
	
	/**
	 * Gets the forgedirection connection from array.
	 * 
	 * @param dir direction to check.
	 * @return forgedirection connection.
	 */
	public EnumFacing getConnection(EnumFacing dir) {
		return connections[dir.ordinal()];
	}
	
	/**
	 * Gets the forgedirection connection from array.
	 * 
	 * @param dir direction to check.
	 * @return forgedirection connection.
	 */
	public EnumFacing getConnection(int dir) {
		return dir >= 0 && dir < connections.length ? connections[dir] : null;
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	@Override
	protected void initContentsArray() {
	}

	@Override
	protected void initSlotsArray() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return null;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	/**
	 * Method used to update connections.
	 */
	protected abstract void updateConnections();

	@Override
	public void update() {
		updateConnections();
		super.update();
	}
	
	/**
	 * Getter function to get the last received direction if applicable.
	 * 
	 * @return last received direction.
	 */
	public EnumFacing getLastReceivedDirection() {
		return lastReceivedDir;
	}
	
	/**
	 * Method to set last received direction.
	 * 
	 * @param dir direction to set to.
	 */
	public void setLastReceivedDirection(EnumFacing dir) {
		this.lastReceivedDir = dir;
	}
	
	@Override
	public EnumFacing getRotatedState(EnumFacing facingDir, IBlockState currentState) {
		// return currentMeta;
		return facingDir;
	}

	@Override
	public EnumFacing getCurrentFacing() {
		return null;
	}

	@Override
	public void setFrontFacing(EnumFacing face) {
	}
	
	@Override
	public boolean canRotateTE() {
		return false;
	}
	
	@Override
	public void onInteract(ItemStack stack, EntityPlayer player, World world, Vector3<Integer> vec) {
	}
	
	@Override
	public boolean canSaveDataOnPickup() {
		return true;
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity.container;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.api.tileentity.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Generic class used for creating new te pipes for transport.
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public abstract class AbstractTileEntityPipe extends AbstractTileEntityContainer implements IWrenchable {

	/** UP, DOWN, NORTH, EAST, SOUTH, WEST */
	protected ForgeDirection[] connections;
	
	/** The last know received direction received from. */
	protected ForgeDirection lastReceivedDir = ForgeDirection.UNKNOWN;

	/**
	 * @param name name of tileentity.
	 */
	public AbstractTileEntityPipe(String name) {
		super(name);
		connections = new ForgeDirection[6];
	}
	
	/**
	 * Get all connections held in array.
	 * 
	 * @return forgedirectional array.
	 */
	public ForgeDirection[] getConnections() {
		return connections;
	}
	
	/**
	 * Gets the forgedirection connection from array.
	 * 
	 * @param dir direction to check.
	 * @return forgedirection connection.
	 */
	public ForgeDirection getConnection(ForgeDirection dir) {
		return connections[dir.ordinal()];
	}
	
	/**
	 * Gets the forgedirection connection from array.
	 * 
	 * @param dir direction to check.
	 * @return forgedirection connection.
	 */
	public ForgeDirection getConnection(int dir) {
		return dir >= 0 && dir < connections.length ? connections[dir] : ForgeDirection.UNKNOWN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getSizeInventory()
	 */
	public int getSizeInventory() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getInventoryStackLimit()
	 */
	public int getInventoryStackLimit() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#initContentsArray()
	 */
	protected void initContentsArray() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#initSlotsArray()
	 */
	protected void initSlotsArray() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getAccessibleSlotsFromSide(int)
	 */
	public int[] getAccessibleSlotsFromSide(int side) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/**
	 * Method used to update connections.
	 */
	protected abstract void updateConnections();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#updateEntity()
	 */
	public void updateEntity() {
		updateConnections();
		super.updateEntity();
	}
	
	/**
	 * Getter function to get the last received direction if applicable.
	 * 
	 * @return last received direction.
	 */
	public ForgeDirection getLastReceivedDirection() {
		return lastReceivedDir;
	}
	
	/**
	 * Method to set last received direction.
	 * 
	 * @param dir direction to set to.
	 */
	public void setLastReceivedDirection(ForgeDirection dir) {
		this.lastReceivedDir = dir;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#getRotationMatrix()
	 */
	@Override
	public byte getRotatedMeta(byte facingDir, byte currentMeta) {
		return currentMeta;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#canRotateTE()
	 */
	@Override
	public boolean canRotateTE() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#onInteract(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
	 */
	@Override
	public void onInteract(ItemStack stack, EntityPlayer player, World world, Vector3<Integer> vec) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#canSaveDataOnPickup()
	 */
	@Override
	public boolean canSaveDataOnPickup() {
		return true;
	}

}

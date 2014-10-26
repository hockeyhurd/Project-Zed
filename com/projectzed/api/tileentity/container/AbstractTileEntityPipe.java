package com.projectzed.api.tileentity.container;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public abstract class AbstractTileEntityPipe extends AbstractTileEntityContainer {
	
	/** UP, DOWN, NORTH, EAST, SOUTH, WEST */
	public ForgeDirection[] connections;
	
	public AbstractTileEntityPipe(String name) {
		super(name);
		connections = new ForgeDirection[6];
		this.maxStorage = 10;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getSizeInventory()
	 */
	public int getSizeInventory() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getInventoryStackLimit()
	 */
	public int getInventoryStackLimit() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#initContentsArray()
	 */
	protected void initContentsArray() {
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#initSlotsArray()
	 */
	protected void initSlotsArray() {
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getAccessibleSlotsFromSide(int)
	 */
	public int[] getAccessibleSlotsFromSide(int side) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#transferContents()
	 */
	protected void transferContents() {
		// TODO: make this work!
	}
	
	/**
	 * Method used to update connections.
	 */
	protected abstract void updateConnections();
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#updateEntity()
	 */
	public void updateEntity() {
		updateConnections();
		transferContents();
	}

}

package com.projectzed.api.tileentity.container;

import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

/**
 * API class used for general purpose containers.
 * <br>Example purpose of this class could be as simple as a chest, or 
 * <br>as extensive as a tank; just as long as the container contains something (items, fluid, energy, etc).
 * 
 * @author hockeyhurd
 * @version Feb 13, 2015
 */
public abstract class AbstractTileEntityContainer extends AbstractTileEntityGeneric {
	
	public AbstractTileEntityContainer(String name) {
		super();
		setCustomName("container." + name);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	@Override
	public abstract int getSizeInventory();

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 */
	@Override
	public abstract int getInventoryStackLimit();

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initContentsArray()
	 */
	@Override
	protected abstract void initContentsArray();

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initSlotsArray()
	 */
	@Override
	protected abstract void initSlotsArray();

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#setCustomName(java.lang.String)
	 */
	@Override
	public void setCustomName(String name) {
		this.customName = name;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public abstract boolean isItemValidForSlot(int slot, ItemStack stack);

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public abstract int[] getSlotsForFace(EnumFacing side);

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canInsertItem(int, net.minecraft.item.ItemStack, EnumFacing)
	 */
	@Override
	public abstract boolean canInsertItem(int slot, ItemStack stack, EnumFacing side);

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canExtractItem(int, net.minecraft.item.ItemStack, EnumFacing)
	 */
	@Override
	public abstract boolean canExtractItem(int slot, ItemStack stack, EnumFacing side);

}

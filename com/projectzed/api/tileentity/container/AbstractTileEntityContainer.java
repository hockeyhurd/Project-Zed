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

	@Override
	public abstract int getSizeInventory();

	@Override
	public abstract int getInventoryStackLimit();

	@Override
	protected abstract void initContentsArray();

	@Override
	protected abstract void initSlotsArray();

	@Override
	public void setCustomName(String name) {
		this.customName = name;
	}

	@Override
	public abstract boolean isItemValidForSlot(int slot, ItemStack stack);

	@Override
	public abstract int[] getSlotsForFace(EnumFacing side);

	@Override
	public abstract boolean canInsertItem(int slot, ItemStack stack, EnumFacing side);

	@Override
	public abstract boolean canExtractItem(int slot, ItemStack stack, EnumFacing side);

}

package com.projectzed.mod.tileentity.generator;

import net.minecraft.item.ItemStack;

import com.projectzed.api.source.EnumType;
import com.projectzed.api.source.Source;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;

/**
 * Class used for creating a new tile entity in the form of a solar array.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public class TileEntitySolarArray extends AbstractTileEntityGenerator {

	public TileEntitySolarArray() {
		super("solarArray");
	}

	public int getSizeInventory() {
		return 0;
	}

	public int getInventoryStackLimit() {
		return 0;
	}

	protected void initContentsArray() {
	}

	protected void initSlotsArray() {
	}

	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	public int[] getAccessibleSlotsFromSide(int side) {
		return null;
	}

	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	public void defineSource() {
		this.source = new Source(EnumType.SOLAR);
	}

}

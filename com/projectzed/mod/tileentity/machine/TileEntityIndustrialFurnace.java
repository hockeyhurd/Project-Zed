package com.projectzed.mod.tileentity.machine;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;

/**
 * 
 * @author hockeyhurd
 * @version Oct 22, 2014
 */
public class TileEntityIndustrialFurnace extends AbstractTileEntityMachine {

	public TileEntityIndustrialFurnace() {
		super("industrialFurnace");
		this.slots = new ItemStack[3];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getSizeInventory()
	 */
	public int getSizeInventory() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getInventoryStackLimit()
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#initContentsArray()
	 */
	protected void initContentsArray() {
		this.slots = new ItemStack[3];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
		this.slotTop = new int[] {
			0
		};
		this.slotRight = new int[] {
			1
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 1 ? false : true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getAccessibleSlotsFromSide(int)
	 */
	public int[] getAccessibleSlotsFromSide(int par1) {
		return par1 == 0 ? this.slotBottom : (par1 == 1 ? this.slotTop : this.slotRight);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return this.isItemValidForSlot(slot, stack);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return side != 0 || slot != 1 || stack.getItem() == Items.bucket;
	}

}

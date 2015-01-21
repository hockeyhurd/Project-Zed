package com.projectzed.mod.tileentity.container;

import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraftforge.fluids.FluidTank;

import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;

/**
 * Class containing code for te fluid tank.
 * 
 * @author hockeyhurd
 * @version Jan 10, 2015
 */
public class TileEntityFluidTank extends AbstractTileEntityFluidContainer {

	private byte tier = 0;
	private final int[] TIER_SIZE = new int[] {
			(int) 8e3, (int) 8e3 * 4, (int) 8e3 * 4 * 4, (int) 8e3 * 4 * 4 * 4 
	};
	
	public TileEntityFluidTank() {
		super("fluidTank");
		this.maxFluidStorage = this.TIER_SIZE[this.tier];
		internalTank = new FluidTank(this.maxFluidStorage);
	}
	
	/**
	 * @param tier = tier to set.
	 */
	public void setTier(byte tier) {
		this.tier = tier >= 0 && tier < this.TIER_SIZE.length ? tier : 0;
		this.maxFluidStorage = this.TIER_SIZE[tier];
	}
	
	/**
	 * @return tier to get.
	 */
	public byte getTier() {
		return this.tier;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	// TODO: Create logic for importing contents (via pipe or something).
	@Override
	protected void importContents() {
	}

	@Override
	public Packet getDescriptionPacket() {
		// TODO Create decription packet and message handler.
		return null;
	}

}

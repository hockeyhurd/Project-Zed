package com.projectzed.mod.block.container;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

import com.projectzed.api.storage.IEnergyContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityContainer;

/**
 * Class containing te code for RF Bridge.
 * 
 * @author hockeyhurd
 * @version Nov 29, 2014
 */
public class TileEntityRFBridge extends AbstractTileEntityContainer implements IEnergyHandler {

	public TileEntityRFBridge() {
		super("bridgeRF");
	}

	/* (non-Javadoc)
	 * @see cofh.api.energy.IEnergyHandler#getMaxEnergyStored(net.minecraftforge.common.util.ForgeDirection)
	 */
	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getMaxImportRate()
	 */
	@Override
	public int getMaxImportRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getMaxExportRate()
	 */
	@Override
	public int getMaxExportRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#requestPower(com.projectzed.api.storage.IEnergyContainer, int)
	 */
	@Override
	public int requestPower(IEnergyContainer cont, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#importContents()
	 */
	@Override
	protected void importContents() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#exportContents()
	 */
	@Override
	protected void exportContents() {
		// TODO Auto-generated method stub

	}
	
	// RF STUFF:
	/* (non-Javadoc)
	 * @see cofh.api.energy.IEnergyConnection#canConnectEnergy(net.minecraftforge.common.util.ForgeDirection)
	 */
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see cofh.api.energy.IEnergyHandler#receiveEnergy(net.minecraftforge.common.util.ForgeDirection, int, boolean)
	 */
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see cofh.api.energy.IEnergyHandler#extractEnergy(net.minecraftforge.common.util.ForgeDirection, int, boolean)
	 */
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see cofh.api.energy.IEnergyHandler#getEnergyStored(net.minecraftforge.common.util.ForgeDirection)
	 */
	@Override
	public int getEnergyStored(ForgeDirection from) {
		// TODO Auto-generated method stub
		return 0;
	}

}

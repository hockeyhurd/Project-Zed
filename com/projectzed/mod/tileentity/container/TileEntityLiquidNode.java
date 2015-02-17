/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import com.projectzed.api.fluid.FluidNet;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;

/**
 * TileEntity code for liquidNode.
 * 
 * @author hockeyhurd
 * @version Feb 16, 2015
 */
public class TileEntityLiquidNode extends AbstractTileEntityFluidContainer {

	private ForgeDirection lastReceivedDir = ForgeDirection.UNKNOWN;
	
	/**
	 * @param name
	 */
	public TileEntityLiquidNode() {
		super("liquidNode");
		this.maxFluidStorage = 8000;
		if (this.internalTank == null) this.internalTank = new FluidTank(this.maxFluidStorage);
		else this.internalTank.setCapacity(this.maxFluidStorage);
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#importContents()
	 */
	@Override
	protected void importContents() {
		if (this.getWorldObj() == null || this.getWorldObj().isRemote) return;
		
		if (this.internalTank.getFluidAmount() > this.maxFluidStorage) {
			FluidStack copy = this.internalTank.getFluid();
			copy.amount = this.maxFluidStorage;
			this.internalTank.setFluid(copy);
		}
		
		FluidNet.importFluidFromNeighbors(this, worldObj, xCoord, yCoord, zCoord, lastReceivedDir);
		FluidNet.tryClearDirectionalTraffic(this, worldObj, xCoord, yCoord, zCoord, lastReceivedDir);
	}
	
	protected void exportContents() {
		if (this.getWorldObj() == null || this.getWorldObj().isRemote) return;
		if (this.internalTank.getFluidAmount() == 0) return;
		
		FluidNet.exportFluidToNeighbors(this, worldObj, xCoord, yCoord, zCoord);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#updateEntity()
	 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		exportContents();
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#getDescriptionPacket()
	 */
	@Override
	public Packet getDescriptionPacket() {
		// TODO Auto-generated method stub
		return null;
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * TileEntity code for liquidNode.
 * 
 * @author hockeyhurd
 * @version Feb 16, 2015
 */
public class TileEntityLiquidNode extends AbstractTileEntityFluidContainer {

	private ForgeDirection lastReceivedDir = ForgeDirection.UNKNOWN;
	byte[] sides = new byte[ForgeDirection.VALID_DIRECTIONS.length];
	private byte cachedMeta;
	
	public TileEntityLiquidNode() {
		super("liquidNode");
		this.maxFluidStorage = 1000;
		if (this.internalTank == null) this.internalTank = new FluidTank(this.maxFluidStorage);
		else this.internalTank.setCapacity(this.maxFluidStorage);
		
		cachedMeta = -1;
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
		
		// FluidNet.importFluidFromNeighbors(this, worldObj, xCoord, yCoord, zCoord, lastReceivedDir);
		// FluidNet.tryClearDirectionalTraffic(this, worldObj, xCoord, yCoord, zCoord, lastReceivedDir);
	}
	
	protected void exportContents() {
		if (this.getWorldObj() == null || this.getWorldObj().isRemote) return;
		if (this.internalTank.getFluidAmount() == 0) return;
		if (this.internalTank.getFluid() == null || this.internalTank.getFluid().getFluid() == null) return;
		
		// FluidNet.exportFluidToNeighbors(this, worldObj, xCoord, yCoord, zCoord);
		
		ForgeDirection exportSide = ForgeDirection.UNKNOWN;
		
		for (byte i = 0; i < this.sides.length; i++) {
			if (this.sides[i] == 1) {
				exportSide = ForgeDirection.getOrientation(i);
				break;
			}
		}
		
		if (exportSide != ForgeDirection.UNKNOWN) {
			TileEntity te = worldObj.getTileEntity(worldVec().x + exportSide.offsetX, worldVec().y + exportSide.offsetY, worldVec().z + exportSide.offsetZ);
			
			if (te != null && te instanceof IFluidHandler) {
				IFluidHandler tank = (IFluidHandler) te;
				
				if (tank.canFill(exportSide.getOpposite(), this.internalTank.getFluid().getFluid())) {
					FluidStack thisStack = this.getTank().getFluid();
					int amount = getAmountFromTank(tank, thisStack, exportSide.getOpposite());
					
					// if destination tank is empty set to default size.
					if (amount == 0) amount = this.getMaxFluidExportRate();
					
					amount = Math.min(amount, thisStack.amount);
					amount = Math.min(amount, this.getMaxFluidExportRate());
					
					if (amount > 0) {
						FluidStack sendStack = thisStack.copy();
						sendStack.amount = amount;
						
						amount = sendStack.amount = tank.fill(exportSide.getOpposite(), sendStack, false);
						
						this.getTank().drain(amount, true);
						tank.fill(exportSide.getOpposite(), sendStack, true);
					}
				}
			}
		}
	}
	
	private int getAmountFromTank(IFluidHandler tank, FluidStack stack, ForgeDirection dir) {
		if (tank != null && stack != null && stack.amount > 0 && dir != ForgeDirection.UNKNOWN && tank.getTankInfo(dir) != null &&tank.getTankInfo(dir).length > 0) {
			for (int i = 0; i < tank.getTankInfo(dir).length; i++) {
				if (tank.getTankInfo(dir)[i].fluid != null && tank.getTankInfo(dir)[i].fluid.amount > 0
						&& tank.getTankInfo(dir)[i].fluid.isFluidEqual(stack)) return tank.getTankInfo(dir)[i].fluid.amount; 
			}
		}
		
		return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#updateEntity()
	 */
	@Override
	public void updateEntity() {
		// super.updateEntity();
		exportContents();
		
		if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0) {
			byte currentMeta = (byte) (worldObj.getBlockMetadata(worldVec().x, worldVec().y, worldVec().z) - 1);
			
			if (currentMeta != this.cachedMeta) {
				for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
					if (dir.ordinal() == currentMeta) sides[dir.ordinal()] = 1;
					else sides[dir.ordinal()] = -1;
				}
				
				this.cachedMeta = currentMeta;
				
				this.markDirty();
				worldObj.markBlockForUpdate(worldVec().x, worldVec().y, worldVec().z);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#getDescriptionPacket()
	 */
	@Override
	public Packet getDescriptionPacket() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#canBeSourceNode()
	 */
	@Override
	public boolean canBeSourceNode() {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#canBeMaster()
	 */
	@Override
	public boolean canBeMaster() {
		return false;
	}
	
	// START FLUID API METHODS AND HANDLERS:
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#fill(net.minecraftforge.common.util.ForgeDirection, net.minecraftforge.fluids.FluidStack, boolean)
	 */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (!worldObj.isRemote) {
			if (from != ForgeDirection.UNKNOWN && sides[from.ordinal()] != -1) return 0;

			int fillAmount = internalTank.fill(resource, doFill);

			if (doFill) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				this.markDirty();
				if (this.getBlockType() != null) worldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord, this.getBlockType());
				FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(resource, worldObj, xCoord, yCoord, zCoord, this.internalTank, fillAmount));
			}

			return fillAmount;
		}

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#drain(net.minecraftforge.common.util.ForgeDirection, net.minecraftforge.fluids.FluidStack, boolean)
	 */
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return drain(from, resource, -1, doDrain);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#drain(net.minecraftforge.common.util.ForgeDirection, int, boolean)
	 */
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return drain(from, null, maxDrain, doDrain);
	}

	/**
	 * Drains fluid from this block to another.
	 * 
	 * @param from direction drained from.
	 * @param drainFluid the fluid drained.
	 * @param drainAmount amount of fluid drained.
	 * @param doDrain whether draining should be simulated or not.
	 * @return type and amount of fluid drained.
	 */
	protected FluidStack drain(ForgeDirection from, FluidStack drainFluid, int drainAmount, boolean doDrain) {
		if (!worldObj.isRemote) {
			if (from != ForgeDirection.UNKNOWN && sides[from.ordinal()] != 1) return null;
			
			FluidStack drainedFluid = (drainFluid != null && drainFluid.isFluidEqual(internalTank.getFluid())) ? internalTank.drain(
					drainFluid.amount, doDrain) : drainAmount >= 0 ? internalTank.drain(drainAmount, doDrain) : null;
					
			if (doDrain && drainedFluid != null && drainedFluid.amount > 0) {
				this.markDirty();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				worldObj.notifyBlockChange(xCoord, yCoord, zCoord, this.getBlockType());
				FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(drainedFluid, worldObj, xCoord, yCoord, zCoord, this.internalTank));
			}
			
			return drainedFluid;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#canFill(net.minecraftforge.common.util.ForgeDirection, net.minecraftforge.fluids.Fluid)
	 */
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if (from != ForgeDirection.UNKNOWN && sides[from.ordinal()] != -1) return false;
		if (fluid != null && !isFull()) {
			FluidStack tankFluid = this.internalTank.getFluid();
			
			return tankFluid == null || tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#canDrain(net.minecraftforge.common.util.ForgeDirection, net.minecraftforge.fluids.Fluid)
	 */
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if (from != ForgeDirection.UNKNOWN && sides[from.ordinal()] != 1) return false;
		if (fluid != null && this.internalTank.getFluidAmount() > 0) {
			FluidStack tankFluid = this.internalTank.getFluid();
			
			return tankFluid != null && tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}
		
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#getRotationMatrix()
	 */
	@Override
	public byte getRotatedMeta(byte facingDir, byte currentMeta) {
		if (facingDir == 0 ^ facingDir == 1) return currentMeta;

		byte ret = (byte) ForgeDirection.getOrientation(facingDir).getOpposite().ordinal();

		return ret == currentMeta ? facingDir : ret;
	}

}

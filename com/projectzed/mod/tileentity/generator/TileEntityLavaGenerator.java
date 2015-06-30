/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.tileentity.generator;

import com.projectzed.api.energy.source.EnumType;
import com.projectzed.api.energy.source.Source;
import com.projectzed.api.fluid.FluidNetwork;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.block.generator.BlockLavaGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * @author hockeyhurd
 * @version 6/30/2015.
 */
public class TileEntityLavaGenerator extends AbstractTileEntityGenerator implements IFluidContainer {

	private int burnTime = 0;
	private static final int consumationAmount = 250;

	private int MAX_FLUID_STORAGE = 16000;
	private FluidTank internalTank;

	public TileEntityLavaGenerator() {
		super("lavaGen");
		this.maxStored = 250000;
		this.internalTank = new FluidTank(this.MAX_FLUID_STORAGE);
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	@Override
	protected void initContentsArray() {
	}

	@Override
	protected void initSlotsArray() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	@Override
	public void defineSource() {
		this.source = new Source(EnumType.LAVA);
	}

	/**
	 * Method used to consume fuel from internal tank.
	 */
	protected void consumeFuel() {
		if (internalTank.getFluid() != null && internalTank.getFluidAmount() >= consumationAmount) {
			internalTank.getFluid().amount -= consumationAmount;
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		boolean flag = this.stored > 0;
		boolean flag1 = false;

		if (this.worldObj != null && !this.worldObj.isRemote) {
			if (this.internalTank.getFluid() != null && this.internalTank.getFluidAmount() >= consumationAmount) {
				if (this.burnTime == 0) {
					// this.burnTime = getItemBurnTime(this.slots[0]);
					this.burnTime = 20 * 5 + 1;
					if (this.stored < this.maxStored) consumeFuel();
					flag1 = true;
				}
			}

			if (this.burnTime > 0) this.burnTime--;

			this.powerMode = this.burnTime > 0 && this.stored < this.maxStored;

			if (flag != this.stored > 0) flag1 = true;
		}

		if (worldObj.getTotalWorldTime() % 20L == 0 && this.blockType != null && this.blockType instanceof BlockLavaGenerator) {
			((BlockLavaGenerator) this.blockType).updateBlockState(this.canProducePower(), this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}

		if (flag1) this.markDirty();

	}

	@Override
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		int time = comp.getInteger("ProjectZedBurnTime");
		this.burnTime = time > 0 ? time : 0;

		this.internalTank.readFromNBT(comp);
	}

	@Override
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		comp.setInteger("ProjectZedBurnTime", this.burnTime);

		this.internalTank.writeToNBT(comp);
	}

	/**
	 * Gets whether tank is full or not.
	 *
	 * @return true if full, else returns false.
	 */
	public boolean isFull() {
		return this.internalTank.getFluidAmount() == this.internalTank.getCapacity();
	}

	@Override
	public FluidTank getTank() {
		return this.internalTank;
	}

	@Override
	public String getLocalizedFluidName() {
		return this.internalTank.getFluid() != null && this.internalTank.getFluid().getFluid() != null ? this.internalTank.getFluid().getFluid().getLocalizedName() : null;
	}

	@Override
	public int getFluidID() {
		return 0;
	}

	@Override
	public int getMaxFluidImportRate() {
		return 0;
	}

	@Override
	public int getMaxFluidExportRate() {
		return 0;
	}

	@Override
	public boolean isPipe() {
		return false;
	}

	@Override
	public boolean canBeSourceNode() {
		return false;
	}

	@Override
	public boolean canBeMaster() {
		return false;
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	public void setMaster(boolean master) {

	}

	@Override
	public boolean hasFluidNetwork() {
		return false;
	}

	@Override
	public FluidNetwork getNetwork() {
		return null;
	}

	@Override
	public void setFluidNetwork(FluidNetwork network) {

	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (!worldObj.isRemote) {

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

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return drain(from, resource, -1, doDrain);
	}

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

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if (fluid != null && !isFull()) {
			FluidStack tankFluid = this.internalTank.getFluid();

			return tankFluid == null || tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}

		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if (fluid != null && this.internalTank.getFluidAmount() > 0) {
			FluidStack tankFluid = this.internalTank.getFluid();

			return tankFluid != null && tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}

		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { this.internalTank.getInfo() } ;
	}
}

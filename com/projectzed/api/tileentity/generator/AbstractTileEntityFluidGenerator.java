/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.api.tileentity.generator;

import com.projectzed.api.fluid.FluidNetwork;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

/**
 * @author hockeyhurd
 * @version 8/17/2015.
 */
public abstract class AbstractTileEntityFluidGenerator extends AbstractTileEntityGenerator implements IFluidContainer {

	protected int burnTime = 0;
	protected int consumationAmount = 250;

	protected int MAX_FLUID_STORAGE = 16000;
	protected FluidTank internalTank;

	public AbstractTileEntityFluidGenerator(String name) {
		super(name);
		this.maxStored = 250000;
		this.internalTank = new FluidTank(this.MAX_FLUID_STORAGE);
	}

	// Fluid networking:
	protected FluidNetwork network;

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
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return false;
	}

	@Override
	public abstract void defineSource();

	public boolean isPowered() {
		return powerMode;
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
	public void update() {
		super.update();
		boolean flag = this.stored > 0;
		boolean flag1 = false;

		if (this.worldObj != null && !this.worldObj.isRemote) {
			// ProjectZed.logHelper.info("internalTank.getFluidAmount()", internalTank.getFluidAmount());

			if (getEnergyStored() + source.getEffectiveSize() <= maxStored) {
				if (internalTank.getFluid() != null && internalTank.getFluidAmount() >= consumationAmount) {
					if (this.burnTime == 0) {
						// this.burnTime = getItemBurnTime(this.slots[0]);
						this.burnTime = 20 * 5 + 1;
						/*if (this.stored < this.maxStored) */
						consumeFuel();
						flag1 = true;
					}
				}

				if (this.burnTime > 0) this.burnTime--;
			}

			this.powerMode = this.burnTime > 0 && this.stored < this.maxStored;

			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityGenerator(this));

			if (flag != this.stored > 0) flag1 = true;
		}

		/*if (worldObj.getTotalWorldTime() % 20L == 0 && this.blockType != null && this.blockType instanceof BlockLavaGenerator) {
			((BlockLavaGenerator) this.blockType).updateBlockState(this.canProducePower(), this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}*/

		if (flag1) this.markDirty();
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		int time = comp.getInteger("ProjectZedBurnTime");
		this.burnTime = time > 0 ? time : 0;

		this.internalTank.readFromNBT(comp);
	}

	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
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
		return this.internalTank.getFluid() != null && this.internalTank.getFluid().getFluid() != null ?
				this.internalTank.getFluid().getLocalizedName() :
				null;
	}

	@Override
	public String getFluidID() {
		return getTank().getFluid() != null ? getTank().getFluid().getFluid().getName() : null;
	}

	@Override
	public int getMaxFluidImportRate() {
		return 1000;
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
		return network != null;
	}

	@Override
	public FluidNetwork getNetwork() {
		return network;
	}

	@Override
	public void setFluidNetwork(FluidNetwork network) {
		this.network = network;
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		if (!worldObj.isRemote) {

			int fillAmount = internalTank.fill(resource, doFill);

			if (doFill) {
				worldObj.notifyBlockOfStateChange(pos, blockType);
				this.markDirty();
				if (this.getBlockType() != null) worldObj.notifyNeighborsOfStateChange(pos, blockType);
				FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(resource, worldObj, pos, this.internalTank, fillAmount));
			}

			return fillAmount;
		}

		return 0;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		return drain(from, resource, -1, doDrain);
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		return drain(from, null, maxDrain, doDrain);
	}

	/**
	 * Drains fluid from this block to another.
	 *
	 * @param from        direction drained from.
	 * @param drainFluid  the fluid drained.
	 * @param drainAmount amount of fluid drained.
	 * @param doDrain     whether draining should be simulated or not.
	 * @return type and amount of fluid drained.
	 */
	protected FluidStack drain(EnumFacing from, FluidStack drainFluid, int drainAmount, boolean doDrain) {
		if (!worldObj.isRemote) {
			FluidStack drainedFluid = (drainFluid != null && drainFluid.isFluidEqual(internalTank.getFluid())) ?
					internalTank.drain(drainFluid.amount, doDrain) :
					drainAmount >= 0 ? internalTank.drain(drainAmount, doDrain) : null;

			if (doDrain && drainedFluid != null && drainedFluid.amount > 0) {
				this.markDirty();
				worldObj.notifyBlockOfStateChange(pos, blockType);
				worldObj.notifyNeighborsOfStateChange(pos, blockType);
				FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(drainedFluid, worldObj, pos, this.internalTank, drainedFluid.amount));
			}

			return drainedFluid;
		}

		return null;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		if (fluid != null && !isFull()) {
			FluidStack tankFluid = this.internalTank.getFluid();

			return tankFluid == null || tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}

		return false;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		if (fluid != null && this.internalTank.getFluidAmount() > 0) {
			FluidStack tankFluid = this.internalTank.getFluid();

			return tankFluid != null && tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}

		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		return new FluidTankInfo[] { this.internalTank.getInfo() };
	}

}

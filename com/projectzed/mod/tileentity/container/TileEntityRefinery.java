/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.tileentity.container;

import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.fluid.FluidNetwork;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * TE class for refinery.
 *
 * @author hockeyhurd
 * @version 8/4/2015.
 */
public class TileEntityRefinery extends AbstractTileEntityEnergyContainer implements IFluidContainer {

	private final int MAX_FLUID_STORAGE = 8000;
	private FluidTank oilTank, petrolTank;
	private FluidTank[] tanks;

	public static final int ENERGY_BURN_RATE = Reference.Constants.getMcUFromRF((int) 1e5) / 1000;

	private FluidNetwork network;

	public enum TankID {
		INPUT, OUTPUT;
	}

	public TileEntityRefinery() {
		super("refinery");

		this.oilTank = new FluidTank(this.MAX_FLUID_STORAGE);
		this.petrolTank = new FluidTank(this.MAX_FLUID_STORAGE);

		this.tanks = new FluidTank[] {
			this.oilTank, this.petrolTank
		};
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
	public int getMaxImportRate() {
		return Reference.Constants.BASE_PIPE_TRANSFER_RATE * Reference.Constants.TIER3_ENERGY_PIPE_MULTIPLIER;
	}

	@Override
	public int getMaxExportRate() {
		return 0;
	}

	@Override
	public int requestPower(IEnergyContainer cont, int amount) {
		return 0;
	}

	@Override
	protected void importContents() {
	}

	@Override
	protected void exportContents() {
	}

	// end energy code.

	private boolean canProduceFuel() {
		return storedPower - ENERGY_BURN_RATE >= 0 && getTank(TankID.INPUT).getFluidAmount() - 1 >= 0
				&& (getTank(TankID.OUTPUT).getFluid() == null ^ getTank(TankID.OUTPUT).getFluidAmount() + 1 <= getTank(TankID.OUTPUT).getCapacity());
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!worldObj.isRemote) {

			if (canProduceFuel()) {
				this.storedPower -= ENERGY_BURN_RATE;
				FluidTank inputTank = getTank(TankID.INPUT);
				inputTank.getFluid().amount--;
				if (inputTank.getFluid().amount == 0) inputTank.setFluid(null);

				FluidTank outputTank = getTank(TankID.OUTPUT);
				if (outputTank.getFluid() == null) outputTank.setFluid(new FluidStack(ProjectZed.fluidPetrol, 1));
				else outputTank.getFluid().amount++;
			}

			// TODO: Implement packet handling (if necessary)!
			// PacketHandler.INSTANCE.sendToAll();
		}

		// tanks[TankID.INPUT.ordinal()] = oilTank;
		// tanks[TankID.OUTPUT.ordinal()] = petrolTank;
	}

	@Override
	public Packet getDescriptionPacket() {
		return null;
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);

		for (int i = 0; i < tanks.length; i++) {
			tanks[i].readFromNBT(comp);
		}
	}

	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);

		for (int i = 0; i < tanks.length; i++) {
			tanks[i].writeToNBT(comp);
		}
	}

	// Start fluid handling code:

	@Override
	public FluidTank getTank() {
		return getTank(TankID.OUTPUT);
	}

	public FluidTank getTank(TankID tankID) {
		return tanks[tankID.ordinal()];
	}

	@Override
	public String getLocalizedFluidName() {
		return getLocalizedFluidName(TankID.OUTPUT);
	}

	public String getLocalizedFluidName(TankID tankID) {
		return tanks[tankID.ordinal()].getFluid() != null && tanks[tankID.ordinal()].getFluid().getFluid() != null ?
				tanks[tankID.ordinal()].getFluid().getFluid().getLocalizedName() :
				null;
	}

	@Override
	public int getFluidID() {
		return getFluidID(TankID.OUTPUT);
	}

	public int getFluidID(TankID tankID) {
		return getTank(tankID).getFluid() != null ? getTank(tankID).getFluid().getFluidID() : -1;
	}

	@Override
	public int getMaxFluidImportRate() {
		return 1000;
	}

	@Override
	public int getMaxFluidExportRate() {
		return 1000;
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
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (!worldObj.isRemote) {

			int fillAmount = oilTank.fill(resource, doFill);

			if (doFill) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				this.markDirty();
				if (this.getBlockType() != null) worldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord, this.getBlockType());
				FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(resource, worldObj, xCoord, yCoord, zCoord, this.oilTank, fillAmount));
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
			FluidStack drainedFluid = (drainFluid != null && drainFluid.isFluidEqual(petrolTank.getFluid())) ? petrolTank.drain(
					drainFluid.amount, doDrain) : drainAmount >= 0 ? petrolTank.drain(drainAmount, doDrain) : null;

			if (doDrain && drainedFluid != null && drainedFluid.amount > 0) {
				this.markDirty();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				worldObj.notifyBlockChange(xCoord, yCoord, zCoord, this.getBlockType());
				FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(drainedFluid, worldObj, xCoord, yCoord, zCoord, this.petrolTank));
			}

			return drainedFluid;
		}

		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if (fluid != null && !isFull(TankID.INPUT)) {
			FluidStack tankFluid = this.oilTank.getFluid();

			return tankFluid == null || tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}

		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if (fluid != null && this.petrolTank.getFluidAmount() > 0) {
			FluidStack tankFluid = this.petrolTank.getFluid();

			return tankFluid != null && tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}

		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { oilTank.getInfo(), petrolTank.getInfo() };
	}

	public boolean isFull(TankID tankID) {
		return tanks[tankID.ordinal()].getFluidAmount() == tanks[tankID.ordinal()].getCapacity();
	}

}

/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.tileentity.container;

import com.projectzed.api.fluid.FluidNetwork;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.api.util.FluidUtils;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityRefinery;
import com.projectzed.mod.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

/**
 * TE class for refinery.
 *
 * @author hockeyhurd
 * @version 8/4/2015.
 */
public class TileEntityRefinery extends AbstractTileEntityEnergyContainer implements IFluidContainer {

	private final int MAX_FLUID_STORAGE = 8000;
	private FluidTank inputTank, outputTank;
	private FluidTank[] tanks;

	public static final int ENERGY_BURN_RATE = Reference.Constants.getMcUFromRF((int) 1e5) / 1000;

	private FluidNetwork network;

	public enum TankID {
		INPUT, OUTPUT
	}

	public TileEntityRefinery() {
		super("refinery");

		this.inputTank = new FluidTank(this.MAX_FLUID_STORAGE);
		this.outputTank = new FluidTank(this.MAX_FLUID_STORAGE);

		this.tanks = new FluidTank[] {
			this.inputTank, this.outputTank
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
	public int getMaxImportRate() {
		return Reference.Constants.BASE_PIPE_TRANSFER_RATE * Reference.Constants.TIER3_ENERGY_PIPE_MULTIPLIER;
	}

	@Override
	public int getMaxExportRate() {
		return 0;
	}

	@Override
	protected void importContents() {
	}

	@Override
	protected void exportContents() {
	}

	// end energy code.

	/**
	 * Function to check if TE contains basic necessities for producing fuel.
	 *
	 * @return true if valid parameters, else returns false.
	 */
	private boolean canProduceFuel() {
		return storedPower - ENERGY_BURN_RATE >= 0 && inputTank.getFluidAmount() - 1 >= 0 && (outputTank.getFluid() == null
				|| outputTank.getFluidAmount() + 1 <= outputTank.getCapacity());
	}

	/**
	 * Function to get output fuel from inputted fluidstack.
	 *
	 * @param fluidStack fluid stack to reference.
	 * @return output fluid if valid input, else returns NULL.
	 */
	public static Fluid getOutputFromInput(FluidStack fluidStack) {
		return fluidStack != null && fluidStack.getFluid() == ProjectZed.fluidOil ? ProjectZed.fluidPetrol : null;
	}

	@Override
	public void update() {
		super.update();
		// transferPower();

		if (!worldObj.isRemote) {

			Fluid outputFluid = getOutputFromInput(inputTank.getFluid());

			if (canProduceFuel() && outputFluid != null) {
				this.storedPower -= ENERGY_BURN_RATE;
				inputTank.getFluid().amount--;
				if (inputTank.getFluid().amount == 0) inputTank.setFluid(null);

				if (outputTank.getFluid() == null) outputTank.setFluid(new FluidStack(outputFluid, 1));
				else if (FluidUtils.areFluidsEqual(outputTank.getFluid(), (new FluidStack(outputFluid, 1)))) outputTank.getFluid().amount++;

				this.powerMode = true;
			}

			else this.powerMode = false;

			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityRefinery(this));

			// tanks[TankID.INPUT.ordinal()] = inputTank;
			// tanks[TankID.OUTPUT.ordinal()] = outputTank;
		}
	}

	/*@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityRefinery(this));
	}*/

	@Override
	public NBTTagCompound getUpdateTag() {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityRefinery(this));

		final NBTTagCompound comp = getTileData();
		saveNBT(comp);

		return comp;
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);

		// if for some reason fluid tank array is null, init it first.
		if (tanks == null) {
			// tanks = new FluidTank[TankID.values().length];
			tanks = new FluidTank[] {
					inputTank, outputTank,
			};
		}

		FluidUtils.readNBT(comp, tanks);
	}

	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);

		FluidUtils.saveNBT(comp, tanks);
	}

	// Start fluid handling code:

	/**
	 * @return number of tanks held in this tileentity.
	 */
	public int getNumTanks() {
		return tanks.length;
	}

	public FluidTank getTank(TankID tankID) {
		return tanks[tankID.ordinal()];
	}

	/**
	 * @deprecated as of 8/11/2015, use other getTank functions since this TE uses multiple tanks.
	 *
	 * @return ouput tank in TE.
	 */
	@Override
	@Deprecated
	public FluidTank getTank() {
		return getTank(TankID.OUTPUT);
	}

	public FluidTank getTank(int id) {
		return id >= 0 && id < tanks.length ? tanks[id] : null;
	}

	@Override
	public String getLocalizedFluidName() {
		return getLocalizedFluidName(TankID.OUTPUT);
	}

	public String getLocalizedFluidName(TankID tankID) {
		return tanks[tankID.ordinal()].getFluid() != null && tanks[tankID.ordinal()].getFluid().getFluid() != null ?
				tanks[tankID.ordinal()].getFluid().getFluid().getName() :
				null;
	}

	@Override
	public String getFluidID() {
		return getFluidID(TankID.OUTPUT);
	}

	public String getFluidID(TankID tankID) {
		return getTank(tankID).getFluid() != null ? getTank(tankID).getFluid().getFluid().getName() : null;
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
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		if (!worldObj.isRemote) {

			int fillAmount = inputTank.fill(resource, doFill);

			if (doFill) {
				worldObj.notifyBlockOfStateChange(pos, blockType);
				this.markDirty();
				if (this.getBlockType() != null) worldObj.notifyNeighborsOfStateChange(pos, blockType);
				FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(resource, worldObj, pos, this.inputTank, fillAmount));
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
	 * @param from direction drained from.
	 * @param drainFluid the fluid drained.
	 * @param drainAmount amount of fluid drained.
	 * @param doDrain whether draining should be simulated or not.
	 * @return type and amount of fluid drained.
	 */
	protected FluidStack drain(EnumFacing from, FluidStack drainFluid, int drainAmount, boolean doDrain) {
		if (!worldObj.isRemote) {
			FluidStack drainedFluid = (drainFluid != null && drainFluid.isFluidEqual(outputTank.getFluid())) ? outputTank.drain(
					drainFluid.amount, doDrain) : drainAmount >= 0 ? outputTank.drain(drainAmount, doDrain) : null;

			if (doDrain && drainedFluid != null && drainedFluid.amount > 0) {
				this.markDirty();
				worldObj.notifyBlockOfStateChange(pos, blockType);
				worldObj.notifyNeighborsOfStateChange(pos, blockType);
				FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(drainedFluid, worldObj, pos, this.outputTank, drainedFluid.amount));
			}

			return drainedFluid;
		}

		return null;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		if (fluid != null && !isFull(TankID.INPUT)) {
			FluidStack tankFluid = this.inputTank.getFluid();

			return tankFluid == null || tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}

		return false;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		if (fluid != null && this.outputTank.getFluidAmount() > 0) {
			FluidStack tankFluid = this.outputTank.getFluid();

			return tankFluid != null && tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}

		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		return new FluidTankInfo[] { inputTank.getInfo(), outputTank.getInfo() };
	}

	public boolean isFull(TankID tankID) {
		return tanks[tankID.ordinal()].getFluidAmount() == tanks[tankID.ordinal()].getCapacity();
	}

}

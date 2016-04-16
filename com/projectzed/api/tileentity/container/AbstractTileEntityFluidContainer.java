/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity.container;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.fluid.FluidNetwork;
import com.projectzed.api.fluid.IFluidTile;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.api.tileentity.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * Class containing generalized abstraction code for any te container that uses
 * fluid.
 * 
 * @author hockeyhurd
 * @version Jan 9, 2015
 */
public abstract class AbstractTileEntityFluidContainer extends AbstractTileEntityContainer implements IFluidContainer,
		IFluidTile, IWrenchable {

	protected int maxFluidStorage = 10000;

	protected FluidTank internalTank;
	protected ForgeDirection lastReceivedDir = ForgeDirection.UNKNOWN;
	
	protected FluidNetwork network;
	protected boolean isMaster;

	/**
	 * @param name = name of te (its custom name).
	 */
	public AbstractTileEntityFluidContainer(String name) {
		super(name);
		internalTank = new FluidTank(this.maxFluidStorage);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getTank()
	 */
	@Override
	public FluidTank getTank() {
		return this.internalTank;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getFluidID()
	 */
	@Override
	public int getFluidID() {
		return getTank().getFluid() != null ? getTank().getFluid().getFluidID() : -1;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getMaxFluidImportRate()
	 */
	@Override
	public int getMaxFluidImportRate() {
		return 1000;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getMaxFluidExportRate()
	 */
	@Override
	public int getMaxFluidExportRate() {
		return 1000;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#isPipe()
	 */
	@Override
	public boolean isPipe() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#setLastReceivedDirection(net.minecraftforge.common.util.ForgeDirection)
	 */
	@Override
	public void setLastReceivedDirection(ForgeDirection dir) {
		this.lastReceivedDir = dir;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getLastReceivedDirection()
	 */
	@Override
	public ForgeDirection getLastReceivedDirection() {
		return this.lastReceivedDir;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	public abstract int getSizeInventory();

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 */
	public abstract int getInventoryStackLimit();

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.AbstractTileEntityGeneric#initContentsArray
	 * ()
	 */
	protected abstract void initContentsArray();

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.AbstractTileEntityGeneric#initSlotsArray()
	 */
	protected abstract void initSlotsArray();

	@Override
	public void setCustomName(String name) {
		this.customName = name;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.AbstractTileEntityGeneric#isItemValidForSlot
	 * (int, net.minecraft.item.ItemStack)
	 */
	public abstract boolean isItemValidForSlot(int slot, ItemStack stack);

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#
	 * getAccessibleSlotsFromSide(int)
	 */
	public abstract int[] getAccessibleSlotsFromSide(int side);

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.AbstractTileEntityGeneric#canInsertItem
	 * (int, net.minecraft.item.ItemStack, int)
	 */
	public abstract boolean canInsertItem(int slot, ItemStack stack, int side);

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.AbstractTileEntityGeneric#canExtractItem
	 * (int, net.minecraft.item.ItemStack, int)
	 */
	public abstract boolean canExtractItem(int slot, ItemStack stack, int side);

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#fill(net.minecraftforge.common.util.ForgeDirection, net.minecraftforge.fluids.FluidStack, boolean)
	 */
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
		if (fluid != null && this.internalTank.getFluidAmount() > 0) {
			FluidStack tankFluid = this.internalTank.getFluid();
			
			return tankFluid != null && tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#getTankInfo(net.minecraftforge.common.util.ForgeDirection)
	 */
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { this.internalTank.getInfo() };
	}
	
	/**
	 * Gets whether tank is full or not.
	 * 
	 * @return true if full, else returns false.
	 */
	public boolean isFull() {
		return this.internalTank.getFluidAmount() == this.internalTank.getCapacity();
	}
	
	/**
	 * Gets the localized name of the fluid in the tank.
	 * 
	 * @return localized name of fluid in the tank.
	 */
	public String getLocalizedFluidName() {
		return this.internalTank.getFluid() != null && this.internalTank.getFluid().getFluid() != null ? this.internalTank.getFluid().getFluid().getLocalizedName() : null; 
	}

	/**
	 * Method to be defined controlling mechanisum for importing fluid only (for
	 * now).
	 */
	protected abstract void importContents();

	@Override
	public double distanceTo(Vector3<Integer> vec) {
		return this.worldVec().getNetDifference(vec);
	}

	@Override
	public Vector3<Integer> getOffsetVec(int x, int y, int z) {
		final Vector3<Integer> ret = worldVec();

		ret.x += x;
		ret.y += y;
		ret.z += z;

		return ret;
	}

	@Override
	public float getCost() {
		return 1.0f;
	}

	@Override
	public Block getTile(World world) {
		return world != null ? world.getBlock(xCoord, yCoord, zCoord) : null;
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.AbstractTileEntityGeneric#updateEntity()
	 */
	@Override
	public void updateEntity() {
		importContents();

		this.markDirty();
		super.updateEntity();
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		this.internalTank.readFromNBT(comp);
	}

	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
		this.internalTank.writeToNBT(comp);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getDescriptionPacket()
	 */
	@Override
	public abstract Packet getDescriptionPacket();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#getRotationMatrix()
	 */
	@Override
	public byte getRotatedMeta(byte facingDir, byte currentMeta) {
		if (facingDir == 0 ^ facingDir == 1) return currentMeta;

		byte ret = (byte) ForgeDirection.getOrientation(facingDir).getOpposite().ordinal();

		return ret == currentMeta ? facingDir : ret;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#canRotateTE()
	 */
	@Override
	public boolean canRotateTE() {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#onInteract(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
	 */
	@Override
	public void onInteract(ItemStack stack, EntityPlayer player, World world, Vector3<Integer> vec) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#canSaveDataOnPickup()
	 */
	@Override
	public boolean canSaveDataOnPickup() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#worldVec()
	 */
	@Override
	public Vector3<Integer> worldVec() {
		return new Vector3<Integer>(this.xCoord, this.yCoord, this.zCoord);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#canBeSourceNode()
	 */
	@Override
	public boolean canBeSourceNode() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#canBeMaster()
	 */
	@Override
	public boolean canBeMaster() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#isMaster()
	 */
	@Override
	public boolean isMaster() {
		return isMaster;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#setMaster(boolean)
	 */
	@Override
	public void setMaster(boolean master) {
		this.isMaster = master;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#hasFluidNetwork()
	 */
	@Override
	public boolean hasFluidNetwork() {
		return network != null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getNetwork()
	 */
	@Override
	public FluidNetwork getNetwork() {
		return network;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#setFluidNetwork(com.projectzed.api.fluid.FluidNetwork)
	 */
	@Override
	public void setFluidNetwork(FluidNetwork network) {
		this.network = network;
	}

}

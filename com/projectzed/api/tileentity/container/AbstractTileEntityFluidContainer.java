/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity.container;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.fluid.FluidNetwork;
import com.projectzed.api.fluid.IFluidTile;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.api.tileentity.IWrenchable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
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

	protected EnumFacing frontFacing;
	protected int maxFluidStorage = 10000;

	protected FluidTank internalTank;
	protected EnumFacing lastReceivedDir = null;
	
	protected FluidNetwork network;
	protected boolean isMaster;

	/**
	 * @param name name of te (its custom name).
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

	@Override
	public String getFluidID() {
		return internalTank.getFluid().getFluid().getName();
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
	 * @see com.projectzed.api.fluid.container.IFluidContainer#setLastReceivedDirection(net.minecraftforge.common.util.EnumFacing)
	 */
	@Override
	public void setLastReceivedDirection(EnumFacing dir) {
		this.lastReceivedDir = dir;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getLastReceivedDirection()
	 */
	@Override
	public EnumFacing getLastReceivedDirection() {
		return this.lastReceivedDir;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	@Override
	public abstract int getSizeInventory();

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 */
	@Override
	public abstract int getInventoryStackLimit();

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.AbstractTileEntityGeneric#initContentsArray
	 * ()
	 */
	@Override
	protected abstract void initContentsArray();

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.AbstractTileEntityGeneric#initSlotsArray()
	 */
	@Override
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
	@Override
	public abstract boolean isItemValidForSlot(int slot, ItemStack stack);

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#
	 * getAccessibleSlotsFromSide(int)
	 */
	@Override
	public abstract int[] getSlotsForFace(EnumFacing side);

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.AbstractTileEntityGeneric#canInsertItem
	 * (int, net.minecraft.item.ItemStack, EnumFacing)
	 */
	@Override
	public abstract boolean canInsertItem(int slot, ItemStack stack, EnumFacing side);

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.AbstractTileEntityGeneric#canExtractItem
	 * (int, net.minecraft.item.ItemStack, EnumFacing)
	 */
	@Override
	public abstract boolean canExtractItem(int slot, ItemStack stack, EnumFacing side);

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#fill(net.minecraftforge.common.util.EnumFacing, net.minecraftforge.fluids.FluidStack, boolean)
	 */
	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		if (worldObj != null && !worldObj.isRemote) {

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

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#drain(net.minecraftforge.common.util.EnumFacing, net.minecraftforge.fluids.FluidStack, boolean)
	 */
	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		return drain(from, resource, -1, doDrain);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#drain(net.minecraftforge.common.util.EnumFacing, int, boolean)
	 */
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
			FluidStack drainedFluid = (drainFluid != null && drainFluid.isFluidEqual(internalTank.getFluid())) ? internalTank.drain(
					drainFluid.amount, doDrain) : drainAmount >= 0 ? internalTank.drain(drainAmount, doDrain) : null;
					
			if (doDrain && drainedFluid != null && drainedFluid.amount > 0) {
				this.markDirty();
				worldObj.notifyBlockOfStateChange(pos, blockType);
				worldObj.notifyNeighborsOfStateChange(pos, blockType);
				FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(drainedFluid, worldObj, pos, internalTank, drainedFluid.amount));
			}
			
			return drainedFluid;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#canFill(net.minecraftforge.common.util.EnumFacing, net.minecraftforge.fluids.Fluid)
	 */
	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		if (fluid != null && !isFull()) {
			FluidStack tankFluid = this.internalTank.getFluid();
			
			return tankFluid == null || tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#canDrain(net.minecraftforge.common.util.EnumFacing, net.minecraftforge.fluids.Fluid)
	 */
	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		if (fluid != null && this.internalTank.getFluidAmount() > 0) {
			FluidStack tankFluid = this.internalTank.getFluid();
			
			return tankFluid != null && tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#getTankInfo(net.minecraftforge.common.util.EnumFacing)
	 */
	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
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
		return internalTank.getFluid() != null && internalTank.getFluid().getFluid() != null ?
				internalTank.getFluid().getLocalizedName() : null;
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
	public IBlockState getTile(World world) {
		return world != null ? BlockUtils.getBlock(world, pos) : null;
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
	public void update() {
		importContents();

		this.markDirty();
		super.update();
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

	/*@Override
	public abstract Packet getDescriptionPacket();*/

	@Override
	public abstract NBTTagCompound getUpdateTag();

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#getRotationMatrix()
	 */
	@Override
	public EnumFacing getRotatedState(EnumFacing facingDir, IBlockState block) {
		if (facingDir == EnumFacing.DOWN || facingDir == EnumFacing.UP) return frontFacing;

		return (frontFacing = frontFacing.rotateY());
	}

	@Override
	public EnumFacing getCurrentFacing() {
		return frontFacing;
	}

	@Override
	public void setFrontFacing(EnumFacing front) {
		this.frontFacing = front;
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
		return VectorHelper.toVector3i(pos);
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

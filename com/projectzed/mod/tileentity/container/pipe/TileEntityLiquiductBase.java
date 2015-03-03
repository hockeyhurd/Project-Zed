/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/

package com.projectzed.mod.tileentity.container.pipe;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.energy.source.IColorComponent;
import com.projectzed.api.fluid.FluidNet;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.util.Reference;

/**
 * Class containing te code for liquiducts.
 * <br><bold>NOTE: </bold>Although this class isn't abstract, it should be treated like such.
 * 
 * @author hockeyhurd
 * @version Feb 12, 2015
 */
public class TileEntityLiquiductBase extends AbstractTileEntityPipe implements IFluidContainer, IColorComponent {

	protected int maxFluidStorage = 2000;
	protected int importRate, exportRate;
	protected FluidTank internalTank; 
	
	/**
	 * @param name
	 */
	public TileEntityLiquiductBase(String name) {
		super(name);
		internalTank = new FluidTank(this.maxFluidStorage);
		this.importRate = Reference.Constants.BASE_FLUID_TRANSFER_RATE;
		this.exportRate = Reference.Constants.BASE_FLUID_TRANSFER_RATE;
	}

	/**
	 * Function to get the tank associated with this fluid pipe.
	 * 
	 * @return tank object.
	 */
	public FluidTank getTank() {
		return internalTank;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getLocalizedFluidName()
	 */
	@Override
	public String getLocalizedFluidName() {
		return getTank().getFluid().getLocalizedName();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getFluidID()
	 */
	@Override
	public int getFluidID() {
		return getTank().getFluid() != null ? getTank().getFluid().fluidID : -1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.source.IColorComponent#getColor()
	 */
	@Override
	public EnumColor getColor() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.source.IColorComponent#setColor(com.projectzed.api.energy.source.EnumColor)
	 */
	@Override
	public void setColor(EnumColor color) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getMaxFluidImportRate()
	 */
	@Override
	public int getMaxFluidImportRate() {
		return this.importRate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getMaxFluidExportRate()
	 */
	@Override
	public int getMaxFluidExportRate() {
		return this.exportRate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#isPipe()
	 */
	@Override
	public boolean isPipe() {
		return true;
	}
	
	protected void importContents() {
		if (this.getWorldObj() == null || this.getWorldObj().isRemote) return;
		
		if (this.internalTank.getFluidAmount() > this.maxFluidStorage) {
			FluidStack copy = this.internalTank.getFluid();
			copy.amount = this.maxFluidStorage;
			this.internalTank.setFluid(copy);
			return;
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
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateEntity()
	 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		importContents();
		exportContents();
		
		// if (!this.getWorldObj().isRemote) System.out.println(getTank().getFluidAmount());
		// if (!this.getWorldObj().isRemote && getTank().getFluidAmount() > 0) ProjectZed.logHelper.info(getTank().getFluidAmount(), lastReceivedDir.name(), worldVec().toString());
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getDescriptionPacket()
	 */
	@Override
	public Packet getDescriptionPacket() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#worldVec()
	 */
	@Override
	public Vector4Helper<Integer> worldVec() {
		return new Vector4Helper<Integer>(this.xCoord, this.yCoord, this.zCoord);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateConnections()
	 */
	@Override
	protected void updateConnections() {

		IFluidHandler cont = null;
		
		if (this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof IFluidHandler) {
			cont = (IFluidHandler) this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
			
			if (cont instanceof TileEntityLiquiductBase) {
				if (((TileEntityLiquiductBase) cont).getColor() == this.getColor()) connections[0] = ForgeDirection.UP;
			}
			
			else if (cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(ForgeDirection.UP.getOpposite()) != 0) connections[0] = ForgeDirection.UP;
			}
			
			else connections[0] = ForgeDirection.UP;
		}
		
		else connections[0] = null;
		
		if (this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof IFluidHandler) {
			cont = (IFluidHandler) this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
			
			if (cont instanceof TileEntityLiquiductBase) {
				if (((TileEntityLiquiductBase) cont).getColor() == this.getColor()) connections[1] = ForgeDirection.DOWN;
			}
			
			else if (cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(ForgeDirection.UP.getOpposite()) != 0) connections[1] = ForgeDirection.DOWN;
			}
			
			else connections[1] = ForgeDirection.DOWN;
		}
		
		else connections[1] = null;
		
		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof IFluidHandler) {
			cont = (IFluidHandler) this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
			
			if (cont instanceof TileEntityLiquiductBase) {
				if (((TileEntityLiquiductBase) cont).getColor() == this.getColor()) connections[2] = ForgeDirection.NORTH;
			}
			
			else if (cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(ForgeDirection.NORTH.getOpposite()) != 0) connections[2] = ForgeDirection.NORTH;
			}
			
			else connections[2] = ForgeDirection.NORTH;
		}
		
		else connections[2] = null;
		
		if (this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof IFluidHandler) {
			cont = (IFluidHandler) this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
			
			if (cont instanceof TileEntityLiquiductBase) {
				if (((TileEntityLiquiductBase) cont).getColor() == this.getColor()) connections[3] = ForgeDirection.EAST;
			}
			
			else if (cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(ForgeDirection.EAST.getOpposite()) != 0) connections[3] = ForgeDirection.EAST;
			}
			
			else connections[3] = ForgeDirection.EAST;
		}
		
		else connections[3] = null;
		
		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof IFluidHandler) {
			cont = (IFluidHandler) this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
			
			if (cont instanceof TileEntityLiquiductBase) {
				if (((TileEntityLiquiductBase) cont).getColor() == this.getColor()) connections[4] = ForgeDirection.SOUTH;
			}
			
			else if (cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(ForgeDirection.SOUTH.getOpposite()) != 0) connections[4] = ForgeDirection.SOUTH;
			}
			
			else connections[4] = ForgeDirection.SOUTH;
		}
		
		else connections[4] = null;
		
		if (this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof IFluidHandler) {
			cont = (IFluidHandler) this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
			
			if (cont instanceof TileEntityLiquiductBase) {
				if (((TileEntityLiquiductBase) cont).getColor() == this.getColor()) connections[5] = ForgeDirection.WEST;
			}
			
			else if (cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(ForgeDirection.WEST.getOpposite()) != 0) connections[5] = ForgeDirection.WEST;
			}
			
			else connections[5] = ForgeDirection.WEST;
		}
		
		else connections[5] = null;
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#dataToSave()
	 */
	@Override
	public HashMap<String, Number> dataToSave() {
		HashMap<String, Number> data = new HashMap<String, Number>();

		int id = -1;
		if (this.internalTank.getFluid() != null) id = this.internalTank.getFluid().fluidID;
		data.put("Fluid Amount", this.internalTank.getFluidAmount());
		data.put("Fluid ID", id);
		return data;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		this.internalTank.readFromNBT(comp);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		this.internalTank.writeToNBT(comp);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#fill(net.minecraftforge.common.util.ForgeDirection, net.minecraftforge.fluids.FluidStack, boolean)
	 */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (!worldObj.isRemote) {
			
			FluidStack altStack = resource.copy();
			altStack.amount = this.getMaxFluidImportRate();
			
			boolean useAlt = resource.amount > altStack.amount;
			int fillAmount = 0;
			
			if (useAlt) fillAmount = internalTank.fill(altStack, doFill);
			else fillAmount = internalTank.fill(resource, doFill);
			
			if (doFill) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				this.markDirty();
				if (this.getBlockType() != null) worldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord, this.getBlockType());
				
				if (useAlt) FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(altStack, worldObj, xCoord, yCoord, zCoord, this.internalTank));
				else FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(resource, worldObj, xCoord, yCoord, zCoord, this.internalTank, fillAmount));
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
	 * @param from = direction drained from.
	 * @param drainFluid = the fluid drained.
	 * @param drainAmount = amount of fluid drained.
	 * @param doDrain = whether draining should be simulated or not.
	 * @return type and amount of fluid drained.
	 */
	protected FluidStack drain(ForgeDirection from, FluidStack drainFluid, int drainAmount, boolean doDrain) {
		
		if (!worldObj.isRemote) {
			FluidStack drainedFluid = (drainFluid != null && drainFluid.isFluidEqual(internalTank.getFluid())) ? internalTank.drain(
					drainFluid.amount, doDrain) : drainAmount >= 0 ? internalTank.drain(drainAmount, doDrain) : null;
			
			FluidStack altStack = drainedFluid.copy();
			altStack.amount = this.getMaxFluidExportRate();
			boolean useAlt = drainAmount > altStack.amount;
			
			if (doDrain && drainedFluid != null && drainedFluid.amount > 0) {
				this.markDirty();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				worldObj.notifyBlockChange(xCoord, yCoord, zCoord, this.getBlockType());
				
				if (useAlt) FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(altStack, worldObj, xCoord, yCoord, zCoord, this.internalTank));
				else FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(drainedFluid, worldObj, xCoord, yCoord, zCoord, this.internalTank));
			}
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

}

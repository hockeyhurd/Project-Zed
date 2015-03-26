/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityCentrifuge;
import com.projectzed.mod.registry.CentrifugeRecipeRegistry;

/**
 * Class containing code for industrialCentrifuge.
 * 
 * @author hockeyhurd
 * @version Dec 21, 2014
 */
public class TileEntityIndustrialCentrifuge extends AbstractTileEntityMachine implements IFluidHandler {

	private final int MAX_WATER_STORAGE = 10000;
	private FluidTank internalTank;
	private byte craftingAmount = 1;
	
	public TileEntityIndustrialCentrifuge() {
		super("industrialCentrifuge");
		this.slots = new ItemStack[3];
		this.internalTank = new FluidTank(this.MAX_WATER_STORAGE);
	}
	
	/**
	 * Gets the crafting amount when processing.
	 * 
	 * @return crafting amount.
	 */
	public byte getCraftingAmount() {
		return craftingAmount;
	}
	
	/**
	 * Sets the crafting amount for processing.
	 * 
	 * @param craftingAmount craftingAmount to set.
	 */
	public void setCraftingAmount(byte craftingAmount) {
		this.craftingAmount = craftingAmount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return 3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
		this.slots = new ItemStack[3];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
		this.slotTop = new int[] {
			0
		};
		this.slotRight = new int[] {
			1
		};

		this.slotBottom = new int[] {
			2
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 1 || slot == 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? this.slotRight : this.slotTop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return this.isItemValidForSlot(slot, stack);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return slot == 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canSmelt()
	 */
	@Override
	protected boolean canSmelt() {
		if (this.slots[0] == null || this.slots[2] == null || this.stored - this.energyBurnRate <= 0 || !hasWaterInTank()) return false;
		else {
			// Check if the item in the slot 1 can be smelted (has a set furnace recipe).
			// ItemStack stack = CentrifugeRecipeRegistry.centrifugeList(this.slots[0], this.slots[2]);
			ItemStack itemStack = CentrifugeRecipeRegistry.centrifugeList(this.slots[0], this.slots[2]);
			int amountLeft = /*this.slots[2].getMaxDamage() - */this.slots[2].getItemDamage();
			int allowedAmount = Math.min(this.craftingAmount, amountLeft);
			allowedAmount = Math.min(allowedAmount, Math.max(this.slots[0].stackSize, this.slots[0].stackSize));
			
			if (allowedAmount > 0 && this.slots[0].stackSize < allowedAmount && this.slots[2].stackSize < allowedAmount) return false;
			
			if (craftingAmount > 1 && itemStack != null && itemStack.stackSize > 0) {
				itemStack = CentrifugeRecipeRegistry.stackOffset(itemStack, allowedAmount);
				if (itemStack == null) {
					itemStack = CentrifugeRecipeRegistry.centrifugeList(this.slots[0], this.slots[2]);
					itemStack = CentrifugeRecipeRegistry.stackOffset(itemStack, Math.min(this.craftingAmount, Math.max(this.slots[0].stackSize, this.slots[0].stackSize)));
				}
			}
			
			if (itemStack == null) return false;
			if (this.slots[1] == null) return true;
			if (!this.slots[1].isItemEqual(itemStack)) return false;

			// Add the result of the furnace recipe to the current stack size (already smelted so far).
			int result = this.slots[1].stackSize + itemStack.stackSize;

			// Make sure we aren't going over the set stack limit's size.
			return (result <= getInventoryStackLimit() && result <= itemStack.getMaxStackSize());
		}
	}
	
	/**
	 * Function to get whether there is water in tank.
	 * 
	 * @return true if has water, else returns false.
	 */
	public boolean hasWaterInTank() {
		return this.internalTank.getFluidAmount() > 0;
	}
	
	/**
	 * Gets the fluid tank associated with this tileentity.
	 * 
	 * @return fluid tank object.
	 */
	public FluidTank getTank() {
		return this.internalTank;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#smeltItem()
	 */
	@Override
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemStack = CentrifugeRecipeRegistry.centrifugeList(this.slots[0], this.slots[2]);
			int amountLeft = /*this.slots[2].getMaxDamage() - */this.slots[2].getItemDamage();
			int allowedAmount = Math.min(this.craftingAmount, amountLeft);
			allowedAmount = Math.min(allowedAmount, Math.max(this.slots[0].stackSize, this.slots[0].stackSize));
			int altAmount = Math.min(this.craftingAmount, Math.max(this.slots[0].stackSize, this.slots[0].stackSize));
			boolean usedAlt = false;
			
			if (allowedAmount > 0 && this.slots[0].stackSize < allowedAmount && this.slots[2].stackSize < allowedAmount) return;
			
			if (craftingAmount > 1 && itemStack != null && itemStack.stackSize > 0) {
				itemStack = CentrifugeRecipeRegistry.stackOffset(itemStack, allowedAmount);
				if (itemStack == null) {
					itemStack = CentrifugeRecipeRegistry.centrifugeList(this.slots[0], this.slots[2]);
					itemStack = CentrifugeRecipeRegistry.stackOffset(itemStack, altAmount);
					usedAlt = true;
				}
			}
			
			if (this.slots[1] == null) this.slots[1] = itemStack.copy();
			else if (this.slots[1].isItemEqual(itemStack)) slots[1].stackSize += itemStack.stackSize;

			if (this.slots[0].stackSize > this.slots[2].stackSize) {
				this.slots[0].stackSize -= !usedAlt ? allowedAmount : altAmount;
				this.slots[2].stackSize--;
				ProjectZed.logHelper.info("this one!", allowedAmount);
			}
			
			else if (this.slots[0].stackSize < this.slots[2].stackSize) {
				this.slots[0].stackSize--;
				this.slots[2].stackSize -= !usedAlt ? allowedAmount : altAmount;
			}
			
			else {
				this.slots[0].stackSize--;
				this.slots[2].stackSize--;
			}
			
			// this.slots[0].stackSize--;
			if (this.slots[0].stackSize <= 0) this.slots[0] = null;
			
			// this.slots[2].stackSize--;
			if (this.slots[2].stackSize <= 0) this.slots[2] = null;
			
			this.internalTank.drain(1000, true);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#onDataPacket(net.minecraft.network.NetworkManager, net.minecraft.network.play.server.S35PacketUpdateTileEntity)
	 */
	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityCentrifuge(this));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getSound()
	 */
	@Override
	public Sound getSound() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		this.internalTank.readFromNBT(comp);
		this.craftingAmount = comp.getByte("ProjectZedCentrifugeCraftingAmount");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		this.internalTank.writeToNBT(comp);
		comp.setByte("ProjectZedCentrifugeCraftingAmount", this.craftingAmount);
	}

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
	
}

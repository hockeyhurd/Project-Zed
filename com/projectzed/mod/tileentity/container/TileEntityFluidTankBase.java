/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.api.util.EnumFrameType;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityFluidTank;
import com.projectzed.mod.util.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Class containing code for te fluid tank.
 * 
 * @author hockeyhurd
 * @version Jan 10, 2015
 */
public class TileEntityFluidTankBase extends AbstractTileEntityFluidContainer implements IModularFrame {

	protected byte tier = 0;
	public static final int[] TIER_SIZE = new int[] {
			(int) 10e3, (int) 10e3 * 4, (int) 10e3 * 4 * 4, (int) 10e3 * 4 * 4 * 4
	};

	protected byte[] openSides = new byte[ForgeDirection.VALID_DIRECTIONS.length];

	/**
	 * Sets internal fluid tank and appropriate sizes and other useful data.
	 */
	public TileEntityFluidTankBase() {
		super("fluidTank");
		this.maxFluidStorage = this.TIER_SIZE[this.tier];
		internalTank = new FluidTank(this.maxFluidStorage);
		
		this.openSides[ForgeDirection.UP.ordinal()] = -1;
		this.openSides[ForgeDirection.DOWN.ordinal()] = 1;
	}

	/**
	 * @param tier = tier to set.
	 */
	public void setTier(byte tier) {
		this.tier = tier >= 0 && tier < this.TIER_SIZE.length ? tier : 0;
		this.maxFluidStorage = this.TIER_SIZE[tier];
		this.internalTank.setCapacity(this.maxFluidStorage);
	}

	/**
	 * @return tier to get.
	 */
	public byte getTier() {
		return this.tier;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IModularFrame#getType()
	 */
	@Override
	public EnumFrameType getType() {
		return EnumFrameType.FLUID;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.IModularFrame#setSideValve(net.minecraftforge
	 * .common.util.ForgeDirection, byte)
	 */
	@Override
	public void setSideValve(ForgeDirection dir, byte value) {
		openSides[dir.ordinal()] = value;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.IModularFrame#setSideValveAndRotate(net
	 * .minecraftforge.common.util.ForgeDirection)
	 */
	@Override
	public void setSideValveAndRotate(ForgeDirection dir) {
		openSides[dir.ordinal()] = (byte) (openSides[dir.ordinal()] == -1 ? 0 : (openSides[dir.ordinal()] == 0 ? 1 : -1));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.IModularFrame#getSideValve(net.minecraftforge
	 * .common.util.ForgeDirection)
	 */
	@Override
	public byte getSideValve(ForgeDirection dir) {
		return openSides[dir.ordinal()];
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IModularFrame#getSideValve(int)
	 */
	@Override
	public byte getSideValve(int dir) {
		return openSides[dir];
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IModularFrame#getSidedArray()
	 */
	@Override
	public byte[] getSidedArray() {
		return openSides;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer
	 * #getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer
	 * #getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer
	 * #initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer
	 * #initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer
	 * #isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer
	 * #getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[0];
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer
	 * #canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer
	 * #canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#importContents()
	 */
	@Override
	protected void importContents() {
		if (!this.worldObj.isRemote) {
			
			// export to tank below:
			if (this.getTank().getFluidAmount() > 0 && this.openSides[ForgeDirection.DOWN.ordinal()] == 1) {
				TileEntity te = worldObj.getTileEntity(worldVec().x, worldVec().y - 1, worldVec().z);
				if (te != null && te instanceof IFluidHandler) {
					IFluidHandler tank = (IFluidHandler) te;
					
					if (this.getTank().getFluid() != null && this.getTank().getFluid().getFluid() != null
							&& tank.canFill(ForgeDirection.UP, this.getTank().getFluid().getFluid())) {
						FluidStack thisStack = this.getTank().getFluid();
						int amount = getAmountFromTank(tank, thisStack, ForgeDirection.UP);
						
						// if destination tank is empty set to default size.
						if (amount == 0) amount = Reference.Constants.BASE_FLUID_TRANSFER_RATE;
						
						amount = Math.min(amount, thisStack.amount);
						amount = Math.min(amount, Reference.Constants.BASE_FLUID_TRANSFER_RATE);
						
						if (amount > 0) {
							FluidStack sendStack = thisStack.copy();
							sendStack.amount = amount;
							
							amount = sendStack.amount = tank.fill(ForgeDirection.UP, sendStack, false);
							
							this.getTank().drain(amount, true);
							tank.fill(ForgeDirection.UP, sendStack, true);
						}
						
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
		super.updateEntity();
		if (!this.getWorldObj().isRemote && this.getWorldObj().getTotalWorldTime() % 20L == 0) PacketHandler.INSTANCE.sendToAll(new MessageTileEntityFluidTank(this));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#getDescriptionPacket()
	 */
	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityFluidTank(this));
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		// Make sure the tier from nbt is acceptable.
		byte tier = comp.getByte("ProjectZedFluidTankTier");
		this.tier = tier >= 0 && tier < this.TIER_SIZE.length ? tier : 0;
		if (this.maxFluidStorage != this.TIER_SIZE[this.tier]) this.maxFluidStorage = this.TIER_SIZE[this.tier];

		for (int i = 0; i < this.openSides.length; i++) {
			this.openSides[i] = comp.getByte("ProjectZedFluidTankSide" + i);
		}

		super.readNBT(comp);
	}
	
	@Override
	public void saveNBT(NBTTagCompound comp) {
		comp.setByte("ProjectZedFluidTankTier", this.tier);
		
		for (int i = 0; i < this.openSides.length; i++) {
			comp.setByte("ProjectZedFluidTankSide" + i, this.openSides[i]);
		}
		
		super.saveNBT(comp);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#canRotateTE()
	 */
	@Override
	public boolean canRotateTE() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#onInteract(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
	 */
	@Override
	public void onInteract(ItemStack stack, EntityPlayer player, World world, Vector3<Integer> vec) {
		if (!world.isRemote && !player.isSneaking()) {
			
			ProjectZed.logHelper.info(this.openSides[ForgeDirection.DOWN.ordinal()]);
			// change valve on tank's bottom size:
			setSideValveAndRotate(ForgeDirection.DOWN);
			
			ProjectZed.logHelper.info(this.openSides[ForgeDirection.DOWN.ordinal()]);
		}
	}
	
}

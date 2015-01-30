package com.projectzed.mod.tileentity.container;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTank;

import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.api.util.EnumFrameType;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityFluidTank;

/**
 * Class containing code for te fluid tank.
 * 
 * @author hockeyhurd
 * @version Jan 10, 2015
 */
public class TileEntityFluidTankBase extends AbstractTileEntityFluidContainer implements IModularFrame {

	protected byte tier = 0;
	protected final int[] TIER_SIZE = new int[] {
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
	}

	/**
	 * @param tier = tier to set.
	 */
	public void setTier(byte tier) {
		this.tier = tier >= 0 && tier < this.TIER_SIZE.length ? tier : 0;
		this.maxFluidStorage = this.TIER_SIZE[tier];
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
		return null;
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

	// TODO: Create logic for importing contents (via pipe or something).
	@Override
	protected void importContents() {
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

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound comp) {
		// Make sure the tier from nbt is acceptable.
		byte tier = comp.getByte("ProjectZedFluidTankTier");
		this.tier = tier >= 0 && tier < this.TIER_SIZE.length ? tier : 0;
		if (this.maxFluidStorage != this.TIER_SIZE[this.tier]) this.maxFluidStorage = this.TIER_SIZE[this.tier];

		for (int i = 0; i < this.openSides.length; i++) {
			this.openSides[i] = comp.getByte("ProjectZedFluidTankSide" + i);
		}

		super.readFromNBT(comp);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		comp.setByte("ProjectZedFluidTankTier", this.tier);
		
		for (int i = 0; i < this.openSides.length; i++) {
			comp.setByte("ProjectZedFluidTankSide" + i, this.openSides[i]);
		}
		
		super.writeToNBT(comp);
	}

}

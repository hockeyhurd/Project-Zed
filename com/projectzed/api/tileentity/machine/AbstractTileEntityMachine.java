/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity.machine;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.block.AbstractBlockMachine;
import com.projectzed.api.energy.EnergyNet;
import com.projectzed.api.energy.machine.IEnergyMachine;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.tileentity.IWrenchable;
import com.projectzed.api.util.EnumFrameType;
import com.projectzed.api.util.EnumRedstoneType;
import com.projectzed.api.util.IRedstoneComponent;
import com.projectzed.api.util.Sound;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.SoundHandler;
import com.projectzed.mod.handler.message.MessageTileEntityMachine;
import com.projectzed.mod.util.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Generic abstracted class containing base code for all machines.
 * 
 * @author hockeyhurd
 * @version Oct 22, 2014
 */
public abstract class AbstractTileEntityMachine extends AbstractTileEntityGeneric implements IEnergyMachine, IModularFrame, IRedstoneComponent, IWrenchable {

	protected int[] slotTop, slotBottom, slotRight;

	protected int maxStorage = 50000;
	protected int stored;
	protected int energyBurnRate;
	protected boolean powerMode;
	protected ForgeDirection lastReceivedDir = ForgeDirection.UNKNOWN;

	public int cookTime;
	public static int defaultCookTime = 200;
	public int scaledTime = (defaultCookTime / 10) * 5;
	
	protected byte[] openSides = new byte[ForgeDirection.VALID_DIRECTIONS.length];
	protected EnumRedstoneType redstoneType;

	/**
	 * @param name name of machine.
	 */
	public AbstractTileEntityMachine(String name) {
		super();
		setCustomName("container." + name);
		this.energyBurnRate = Reference.Constants.BASE_MACH_USAGE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	public abstract int getSizeInventory();

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 */
	public abstract int getInventoryStackLimit();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initContentsArray()
	 */
	protected abstract void initContentsArray();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initSlotsArray()
	 */
	protected abstract void initSlotsArray();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#setCustomName(java.lang.String)
	 */
	public void setCustomName(String name) {
		this.customName = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	public abstract boolean isItemValidForSlot(int slot, ItemStack stack);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getAccessibleSlotsFromSide(int)
	 */
	public abstract int[] getAccessibleSlotsFromSide(int side);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return openSides[side] == -1 && isItemValidForSlot(slot, stack);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return slot == 1 && openSides[side] == 1;
	}
	
	protected void handleSidedIO() {
		TileEntity te = null;
		IInventory otherInv = null;
		ItemStack in = null;
		ItemStack out = null;
		
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			te = worldObj.getTileEntity(worldVec().x + dir.offsetX, worldVec().y + dir.offsetY, worldVec().z + dir.offsetZ);
			
			if (te != null && te instanceof IInventory) { 
				otherInv = (IInventory) te;
				
				if (openSides[dir.ordinal()] == -1) {
					
					for (int otherSlot = 0; otherSlot < otherInv.getSizeInventory(); otherSlot++) {
						ItemStack invIn = otherInv.getStackInSlot(otherSlot);
						if (invIn == null || invIn.stackSize == 0) continue;
						
						for (int thisSlot = 0; thisSlot < this.getSizeInvenotry(); thisSlot++) {
							in = this.getStackInSlot(thisSlot);
							boolean hasStack = in != null && in.stackSize > 0 && in.stackSize < in.getMaxStackSize();
							
							if (isItemValidForSlot(thisSlot, invIn) && (!hasStack || invIn.isItemEqual(in))) {
								int amount = invIn.stackSize;
								if (hasStack) amount = Math.min(in.getMaxStackSize() - in.stackSize, amount);
								
								if (hasStack) in.stackSize += amount;
								else {
									ItemStack copy = invIn.copy();
									copy.stackSize = amount;
									in = copy;
								}
								
								this.setInventorySlotContents(thisSlot, in);
								
								otherInv.decrStackSize(otherSlot, amount);
							}
						}
					}
					
				}
				
				else if (openSides[dir.ordinal()] == 1) {
					for (int thisSlot = 0; thisSlot < this.getSizeInventory(); thisSlot++) {
						out = this.getStackInSlot(thisSlot);
						if (out == null || out.stackSize == 0 || !this.canExtractItem(thisSlot, out, dir.ordinal())) continue;
						
						int remainder = 0;
						
						for (int otherSlot = 0; otherSlot < otherInv.getSizeInventory(); otherSlot++) {
							if (otherInv.isItemValidForSlot(otherSlot, out)) {
								ItemStack destStack = otherInv.getStackInSlot(otherSlot);
								if (destStack != null && destStack.stackSize == destStack.getMaxStackSize()) continue;
								
								int amount = out.stackSize;
								if (destStack != null) amount = Math.min(destStack.getMaxStackSize() - destStack.stackSize, amount);
								else {
									ItemStack copy = out.copy();
									copy.stackSize = amount;
									destStack = copy;
								}
								
								remainder -= amount;
								ProjectZed.logHelper.info("remainder:", remainder);
								
								otherInv.setInventorySlotContents(otherSlot, destStack);
								this.decrStackSize(thisSlot, amount);
							}
							
							if (remainder == 0) break;
						}
						
						if (remainder == 0) break;
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int i) {
		return cookTime > 0 && scaledTime > 0 ? cookTime * i / scaledTime : cookTime == 0 ? 0 : cookTime > 0 ? 1 : 0;
	}

	public boolean isBurning() {
		return this.stored > 0;
	}

	/**
	 * Function used to determine if item x is able to be used in slot y.
	 * @return true if valid, else return false.
	 */
	protected abstract boolean canSmelt();

	/**
	 * Method used to perform 'smelting'
	 */
	public abstract void smeltItem();

	/**
	 * Method used to transfer power from one te to another.
	 */
	public void transferPower() {
		if (this.getWorldObj().isRemote) return;
		
		if (this.stored >= this.maxStorage) {
			this.stored = this.maxStorage;
			// return;
		}

		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;
		
		EnergyNet.importEnergyFromNeighbors(this, worldObj, x, y, z, lastReceivedDir);
		EnergyNet.tryClearDirectionalTraffic(this, worldObj, x, y, z, lastReceivedDir);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#worldVec()
	 */
	public Vector3<Integer> worldVec() {
		return new Vector3<Integer>(this.xCoord, this.yCoord, this.zCoord);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#updateEntity()
	 */
	public void updateEntity() {
		transferPower();
		boolean flag = this.stored > 0;
		boolean flag1 = false;

		if (this.stored > 0) burnEnergy();

		if (!this.worldObj.isRemote) {
			
			handleSidedIO();
			
			if (!isActiveFromRedstoneSignal()) return;
			
			if (this.isBurning() && this.canSmelt()) {
				this.cookTime++;
				this.powerMode = true;

				if (this.cookTime == scaledTime) {
					this.cookTime = 0;
					this.smeltItem();
					// ProjectZed.logHelper.info("Item smelted!");
					flag1 = true;
				}
				
				if (getSound() != null && this.worldObj.getTotalWorldTime() % (20L * getSound().LENGTH) == 0) SoundHandler.playEffect(getSound(), this.worldObj, this.worldVec());
			}
			
			else {
				this.cookTime = 0;
				this.powerMode = false;
			}

			if (flag != this.stored > 0) {
				flag1 = true;
				// ((AbstractBlockMachine) this.blockType).updateBlockState(this.cookTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
			
			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityMachine(this));
		}

		if (worldObj.getTotalWorldTime() % 20L == 0 && this.blockType != null && this.blockType instanceof AbstractBlockMachine) {
			((AbstractBlockMachine) this.blockType).updateBlockState(this.isPoweredOn(), this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			// ProjectZed.logHelper.info(this.powerMode);
		}
		
		if (flag1) this.markDirty();
	}

	/** Max allowed capacity */
	public void setMaxStorage(int max) {
		this.maxStorage = max;
	}

	/** Get the max capacity */
	public int getMaxStorage() {
		return this.maxStorage;
	}

	/** Set the amount of energy stored. */
	public void setEnergyStored(int amount) {
		this.stored = amount;
	}

	/** Get the amount currently stored. */
	public int getEnergyStored() {
		return this.stored;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#getMaxImportRate()
	 */
	public int getMaxImportRate() {
		return Reference.Constants.BASE_PIPE_TRANSFER_RATE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#getMaxTransferRate()
	 */
	public int getMaxExportRate() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#requestPower(com.projectzed.api.storage.IEnergyContainer, int)
	 */
	public int requestPower(IEnergyContainer cont, int amount) {
		return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#addPower(com.projectzed.api.energy.storage.IEnergyContainer, int)
	 */
	public int addPower(IEnergyContainer cont, int amount) {
		if (cont != null && this.getMaxImportRate() >= amount) {
			if (this.stored + amount <= this.maxStorage) this.stored += amount;
			else {
				amount = this.maxStorage - this.stored;
				this.stored = this.maxStorage;
			}
			
			return amount;
		}
		
		else return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#setLastReceivedDirection(net.minecraftforge.common.util.ForgeDirection)
	 */
	public void setLastReceivedDirection(ForgeDirection dir) {
		this.lastReceivedDir = dir;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#getLastReceivedDirection()
	 */
	public ForgeDirection getLastReceivedDirection() {
		return this.lastReceivedDir;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.machine.IEnergyMachine#setEnergyBurnRate(int)
	 */
	public void setEnergyBurnRate(int val) {
		this.energyBurnRate = val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.machine.IEnergyMachine#getEnergyBurnRate()
	 */
	public int getEnergyBurnRate() {
		return this.energyBurnRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.machine.IEnergyMachine#setBurning(boolean)
	 */
	public void setPowerMode(boolean val) {
		this.powerMode = val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.machine.IEnergyMachine#isBurning()
	 */
	public boolean isPoweredOn() {
		return this.powerMode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.machine.IEnergyMachine#burnEnergy()
	 */
	public void burnEnergy() {
		if (isPoweredOn() && this.cookTime > 0) this.stored -= this.energyBurnRate;
		// PacketHandler.INSTANCE.sendToAll(new MessageTileEntityMachine(this));
	}
	
	/** Abstract function to get sound to play. If not applicable, set to null. */
	public abstract Sound getSound();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		this.cookTime = comp.getShort("CookTime");
		this.stored = comp.getInteger("ProjectZedPowerStored");
		this.powerMode = comp.getBoolean("ProjectZedPowerMode");
		
		int typeID = comp.hasKey("RedstoneType") ? comp.getInteger("RedstoneType") : 1;
		this.redstoneType = EnumRedstoneType.TYPES[typeID >= 0 && typeID < EnumRedstoneType.TYPES.length ? typeID : 1];
		
		for (int i = 0; i < this.openSides.length; i++) {
			this.openSides[i] = comp.getByte("ProjectZedMachineSide" + i);
		}
		
		if (comp.hasKey("CustomName")) this.customName = comp.getString("CustomName");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		comp.setShort("CookTime", (short) this.cookTime);
		comp.setInteger("ProjectZedPowerStored", this.stored);
		comp.setBoolean("ProjectZedPowerMode", this.powerMode);
		
		if (this.redstoneType == null) this.redstoneType = EnumRedstoneType.LOW;
		comp.setInteger("RedstoneType", this.redstoneType.ordinal());

		for (int i = 0; i < this.openSides.length; i++) {
			comp.setByte("ProjectZedMachineSide" + i, this.openSides[i]);
		}
		
		if (this.hasCustomInventoryName()) comp.setString("CustomName", this.customName);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getDescriptionPacket()
	 */
	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityMachine(this));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#onDataPacket(net.minecraft.network.NetworkManager, net.minecraft.network.play.server.S35PacketUpdateTileEntity)
	 */
	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityMachine(this));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#getRotationMatrix()
	 */
	@Override
	public byte[] getRotationMatrix() {
		return new byte[] { 2, 5, 3, 4 };
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
	 * @see com.projectzed.api.tileentity.IWrenchable#dataToSave()
	 */
	@Override
	public HashMap<String, Number> dataToSave() {
		HashMap<String, Number> data = new HashMap<String, Number>();
		data.put("ProjectZedPowerStored", this.stored);
		
		return data;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#stacksToSave()
	 */
	@Override
	public ItemStack[] stacksToSave() {
		return this.slots;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.util.IRedstoneComponent#getRedstoneType()
	 */
	@Override
	public EnumRedstoneType getRedstoneType() {
		return redstoneType;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.util.IRedstoneComponent#setRedstoneType(com.projectzed.api.util.EnumRedstoneType)
	 */
	@Override
	public void setRedstoneType(EnumRedstoneType type) {
		this.redstoneType = type;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.util.IRedstoneComponent#getRedstoneSignal()
	 */
	@Override
	public int getRedstoneSignal() {
		return worldObj.getBlockPowerInput(worldVec().x, worldVec().y, worldVec().z);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.util.IRedstoneComponent#isActiveFromRedstoneSignal()
	 */
	@Override
	public boolean isActiveFromRedstoneSignal() {
		return redstoneType == EnumRedstoneType.DISABLED ? true : redstoneType == EnumRedstoneType.LOW ? getRedstoneSignal() == 0
				: redstoneType == EnumRedstoneType.HIGH ? getRedstoneSignal() > 0 : false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IModularFrame#getType()
	 */
	@Override
	public EnumFrameType getType() {
		return EnumFrameType.ITEM;
	}
	
	/**
	 * Sets given direction to given value.
	 * @param dir = direction to set.
	 * @param value = value to set (-1 = import, 0 = neutral or nothing allowed, 1 = export).
	 */
	@Override
	public void setSideValve(ForgeDirection dir, byte value) {
		openSides[dir.ordinal()] = value;
	}
	
	/**
	 * Sets the side value after rotating to next value.
	 * @param dir = direction to test.
	 */
	@Override
	public void setSideValveAndRotate(ForgeDirection dir) {
		openSides[dir.ordinal()] = (byte) (openSides[dir.ordinal()] == -1 ? 0 : (openSides[dir.ordinal()] == 0 ? 1 : -1));
	}
	
	/**
	 * @param dir = direction to test.
	 * @return -1 if can input, 0 neutral/nothing, or 1 to export.
	 */
	@Override
	public byte getSideValve(ForgeDirection dir) {
		return openSides[dir.ordinal()];
	}
	
	/**
	 * @param dir = direction to test.
	 * @return -1 if can input, 0 neutral/nothing, or 1 to export.
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

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity.machine;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.api.block.AbstractBlockMachine;
import com.projectzed.api.energy.EnergyNet;
import com.projectzed.api.energy.machine.IEnergyMachine;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.item.IItemUpgradeComponent;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.tileentity.IWrenchable;
import com.projectzed.api.util.*;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.SoundHandler;
import com.projectzed.mod.handler.message.MessageTileEntityMachine;
import com.projectzed.mod.util.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic abstracted class containing base code for all machines.
 * 
 * @author hockeyhurd
 * @version Oct 22, 2014
 */
public abstract class AbstractTileEntityMachine extends AbstractTileEntityGeneric implements IEnergyMachine, IModularFrame, IRedstoneComponent,
		IUpgradeComponent, IWrenchable {

	protected int[] slotTop, slotBottom, slotInput, slotRight;

	protected int maxStorage = 50000;
	protected int stored;
	protected int originalEnergyBurnRate;
	protected int energyBurnRate;
	protected boolean powerMode;
	protected EnumFacing lastReceivedDir;

	public int cookTime;
	public static final int defaultCookTime = 200;
	public int scaledTime = (defaultCookTime / 10) * 5;
	public int originalScaledTime = scaledTime;

	protected byte[] openSides = new byte[EnumFacing.VALUES.length];
	protected EnumRedstoneType redstoneType;

	protected ItemStack[] lastTickUpgrades;

	/**
	 * @param name name of machine.
	 */
	public AbstractTileEntityMachine(String name) {
		super();
		setCustomName("container." + name);
		this.originalEnergyBurnRate = this.energyBurnRate = Reference.Constants.BASE_MACH_USAGE;
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
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack == null || stack.stackSize == 0) return false;

		if (stack.getItem() instanceof IItemUpgradeComponent) return canInsertItemUpgrade((IItemUpgradeComponent) stack.getItem(), stack);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if (openSides[side] == 0) return new int[0];

		int[] ret = new int[getSizeInventory() - getSizeUpgradeSlots()];

		for (int i = 0; i < ret.length; i++) {
			ret[i] = i;
		}

		return ret;

	}

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
		return slot > 0 && slot < slots.length - getSizeUpgradeSlots() && openSides[side] == 1;
	}
	
	/**
	 * Method handler for pushing/pulling itemstacks to and from neighboring inventories.
	 */
	protected void handleSidedIO() {
		TileEntity te = null;
		IInventory otherInv = null;
		ItemStack in = null;
		ItemStack out = null;
		Vec3i vec3i;

		for (EnumFacing dir : EnumFacing.VALUES) {
			vec3i = dir.getDirectionVec();
			te = worldObj.getTileEntity(new BlockPos(worldVec().x + vec3i.getX(), worldVec().y + vec3i.getY(), worldVec().z + vec3i.getZ()));
			
			if (te != null && te instanceof IInventory) { 
				otherInv = (IInventory) te;
				final int upgradeOffset = te instanceof IUpgradeComponent ? ((IUpgradeComponent) te).getSizeUpgradeSlots() : 0;
				
				// input relative to machine.
				if (openSides[dir.ordinal()] == -1) {
					
					for (int otherSlot = 0; otherSlot < otherInv.getSizeInventory() - upgradeOffset; otherSlot++) {
						// stack from other inventory to pull.
						ItemStack invIn = otherInv.getStackInSlot(otherSlot);
						if (invIn == null || invIn.stackSize == 0) continue;
						
						for (int thisSlot = 0; thisSlot < this.getSizeInventory() - getSizeUpgradeSlots(); thisSlot++) {
							// itemstack currently in the 'input' slot.
							in = this.getStackInSlot(thisSlot);
							boolean hasStack = in != null && in.stackSize > 0 && in.stackSize <= in.getMaxStackSize();
							
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
				
				// output relative to machine.
				else if (openSides[dir.ordinal()] == 1) {
					for (int thisSlot = 0; thisSlot < this.getSizeInventory() - getSizeUpgradeSlots(); thisSlot++) {
						out = this.getStackInSlot(thisSlot);
						if (out == null || out.stackSize == 0 || !this.canExtractItem(thisSlot, out, dir.ordinal())) continue;
						
						int amount = out.stackSize;
						
						for (int otherSlot = 0; otherSlot < otherInv.getSizeInventory() - upgradeOffset; otherSlot++) {
							ItemStack destStack = otherInv.getStackInSlot(otherSlot);
							if (destStack != null && destStack.stackSize == destStack.getMaxStackSize()) continue;
							
							if (/*(destStack != null && out.isItemEqual(destStack)) ||*/ otherInv.isItemValidForSlot(otherSlot, out)) {
								
								if (destStack != null) {
									if (out.isItemEqual(destStack)) {
										amount = Math.min(destStack.getMaxStackSize() - destStack.stackSize, amount);	
										otherInv.getStackInSlot(otherSlot).stackSize += amount;
									}
									
									else continue;
								}
								
								else {
									ItemStack copy = out.copy();
									copy.stackSize = amount;
									destStack = copy;
									otherInv.setInventorySlotContents(otherSlot, destStack);
								}
								
								out.stackSize -= amount;
								if (out.stackSize == 0) out = null;
								
								this.decrStackSize(thisSlot, amount);
							}
							
							if (amount == 0 || out == null || out.stackSize == 0) break;
						}
						
						if (out == null || out.stackSize == 0) continue;
						
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
		if (worldObj.isRemote) return;
		
		if (this.stored >= this.maxStorage) {
			this.stored = this.maxStorage;
			return;
		}

		EnergyNet.importEnergyFromNeighbors(this, worldObj, pos.getX(), pos.getY(), pos.getZ(), lastReceivedDir);
		EnergyNet.tryClearDirectionalTraffic(this, worldObj, pos.getX(), pos.getY(), pos.getZ(), lastReceivedDir);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#worldVec()
	 */
	public Vector3<Integer> worldVec() {
		return new Vector3<Integer>(pos.getX(), pos.getY(), pos.getZ());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#updateEntity()
	 */
	public void updateEntity() {
		calculateDataFromUpgrades();
		transferPower();
		boolean flag = this.stored > 0;
		boolean flag1 = false;

		if (this.stored > 0) burnEnergy();

		if (!this.worldObj.isRemote) {
			if (this.worldObj.getTotalWorldTime() % 20L == 0) handleSidedIO();
			
			// if (!isActiveFromRedstoneSignal()) return;
			
			if (isActiveFromRedstoneSignal() && this.isBurning() && this.canSmelt()) {
				this.cookTime++;
				this.powerMode = true;

				if (this.cookTime == scaledTime) {
					this.cookTime = 0;
					this.smeltItem();
					flag1 = true;
				}
				
				if (getSound() != null && this.worldObj.getTotalWorldTime() % (20L * getSound().LENGTH) == 0)
					SoundHandler.playEffect(getSound(), worldObj, worldVec());
			}
			
			else {
				this.cookTime = 0;
				this.powerMode = false;
			}

			if (flag != this.stored > 0) flag1 = true;
			
			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityMachine(this));
		}

		if (worldObj.getTotalWorldTime() % 20L == 0 && this.blockType != null && this.blockType instanceof AbstractBlockMachine) {
			((AbstractBlockMachine) this.blockType).updateBlockState(this.isPoweredOn(), this.worldObj, pos.getX(), pos.getY(), pos.getZ());
			// ProjectZed.logHelper.info(this.powerMode);
		}
		
		if (flag1) this.markDirty();
	}

	/** Max allowed capacity */
	@Override
	public void setMaxStorage(int max) {
		this.maxStorage = max;
	}

	/** Get the max capacity */
	@Override
	public int getMaxStorage() {
		return this.maxStorage;
	}

	/** Set the amount of energy stored. */
	@Override
	public void setEnergyStored(int amount) {
		this.stored = amount;
	}

	/** Get the amount currently stored. */
	@Override
	public int getEnergyStored() {
		return this.stored;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#getMaxImportRate()
	 */
	@Override
	public int getMaxImportRate() {
		return Reference.Constants.BASE_PIPE_TRANSFER_RATE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#getMaxTransferRate()
	 */
	@Override
	public int getMaxExportRate() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#requestPower(com.projectzed.api.storage.IEnergyContainer, int)
	 */
	@Override
	public int requestPower(IEnergyContainer cont, int amount) {
		return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#addPower(com.projectzed.api.energy.storage.IEnergyContainer, int)
	 */
	@Override
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
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#setLastReceivedDirection(net.minecraftforge.common.util.EnumFacing)
	 */
	@Override
	public void setLastReceivedDirection(EnumFacing dir) {
		// this.lastReceivedDir = dir;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#getLastReceivedDirection()
	 */
	@Override
	public EnumFacing getLastReceivedDirection() {
		return this.lastReceivedDir;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.machine.IEnergyMachine#setEnergyBurnRate(int)
	 */
	@Override
	public void setEnergyBurnRate(int val) {
		this.energyBurnRate = val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.machine.IEnergyMachine#getEnergyBurnRate()
	 */
	@Override
	public int getEnergyBurnRate() {
		return energyBurnRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.machine.IEnergyMachine#setBurning(boolean)
	 */
	@Override
	public void setPowerMode(boolean val) {
		this.powerMode = val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.machine.IEnergyMachine#isBurning()
	 */
	@Override
	public boolean isPoweredOn() {
		return this.powerMode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.machine.IEnergyMachine#burnEnergy()
	 */
	@Override
	public void burnEnergy() {
		if (isPoweredOn() && this.cookTime > 0) this.stored -= this.energyBurnRate;
		// PacketHandler.INSTANCE.sendToAll(new MessageTileEntityMachine(this));
	}

	/**
	 * Method used to calculate data from upgrades.
	 */
	protected void calculateDataFromUpgrades() {
		// checks once per second or every 20th tick for updating upgrade info.
		if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0 && getSizeUpgradeSlots() > 0) {
			ItemStack[] upgrades = getCurrentUpgrades();

			if (upgrades.length > 0) {

				if (lastTickUpgrades != null && lastTickUpgrades.length > 0 && lastTickUpgrades.length == upgrades.length) {
					boolean isSame = true;

					for (int i = 0; i < lastTickUpgrades.length; i++) {
						if (!ItemStack.areItemStacksEqual(lastTickUpgrades[i], upgrades[i])) {
							isSame = false;
							break;
						}
					}

					if (isSame) return;
				}

				else {
					lastTickUpgrades = new ItemStack[upgrades.length];

					for (int i = 0; i < upgrades.length; i++) {
						lastTickUpgrades[i] = upgrades[i];
					}
				}

				float mostEff = Float.MAX_VALUE;
				float maxPower = Float.MIN_VALUE;
				IItemUpgradeComponent comp;

				for (ItemStack stack : upgrades) {
					if (stack != null && stack.stackSize > 0) {
						comp = ((IItemUpgradeComponent) stack.getItem());

						mostEff = Math.min(mostEff, comp.operationSpeedRelativeToSize(stack.stackSize, originalScaledTime));
						maxPower = Math.max(maxPower, comp.energyBurnRateRelativeToSize(stack.stackSize, originalEnergyBurnRate));
					}
				}

				scaledTime = (int) Math.min(Math.ceil(mostEff), originalScaledTime);

				if (scaledTime < 1) scaledTime = 1;

				energyBurnRate = (int) Math.max(Math.ceil(maxPower), originalEnergyBurnRate);
			}

			else {
				scaledTime = originalScaledTime;
				energyBurnRate = originalEnergyBurnRate;
			}
		}
	}
	
	/** Abstract function to get sound to play. If not applicable, set to null. */
	public abstract Sound getSound();

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
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

	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
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
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityMachine(this));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#getRotationMatrix()
	 */
	@Override
	public byte getRotatedMeta(byte facingDir, byte currentMeta) {
		if (facingDir == 0 ^ facingDir == 1) return currentMeta;

		byte ret = (byte) EnumFacing.getFront(facingDir).getOpposite().ordinal();

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
		int max = Integer.MIN_VALUE;
		for (EnumFacing dir : EnumFacing.VALUES) {
			max = Math.max(worldObj.getRedstonePower(pos, dir), max);
		}

		return max;
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
	public void setSideValve(EnumFacing dir, byte value) {
		openSides[dir.ordinal()] = value;

		worldObj.notifyBlockOfStateChange(pos, blockType);
		markDirty();
	}
	
	/**
	 * Sets the side value after rotating to next value.
	 * @param dir = direction to test.
	 */
	@Override
	public void setSideValveAndRotate(EnumFacing dir) {
		openSides[dir.ordinal()] = (byte) (openSides[dir.ordinal()] == -1 ? 0 : (openSides[dir.ordinal()] == 0 ? 1 : -1));

		worldObj.notifyBlockOfStateChange(pos, blockType);
		markDirty();
	}
	
	/**
	 * @param dir = direction to test.
	 * @return -1 if can input, 0 neutral/nothing, or 1 to export.
	 */
	@Override
	public byte getSideValve(EnumFacing dir) {
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

	@Override
	public ItemStack[] getUpgradeSlots() {
		List<ItemStack> list = new ArrayList<ItemStack>(getSizeUpgradeSlots());

		for (int i = slots.length - getSizeUpgradeSlots(); i < slots.length; i++) {
			list.add(slots[i]);
		}

		return list.toArray(new ItemStack[list.size()]);
	}

	@Override
	public ItemStack[] getCurrentUpgrades() {
		List<ItemStack> list = new ArrayList<ItemStack>(getSizeUpgradeSlots());

		ItemStack[] upgradeSlots = getUpgradeSlots();

		for (int i = 0; i < upgradeSlots.length; i++) {
			if (upgradeSlots[i] != null && upgradeSlots[i].stackSize > 0) {
				if (!list.isEmpty()) {
					boolean added = false;

					for (ItemStack stack : list) {
						if (stack.isItemEqual(upgradeSlots[i])) {
							stack.stackSize += upgradeSlots[i].stackSize;
							if (stack.stackSize > stack.getMaxStackSize()) stack.stackSize = stack.getMaxStackSize();
							added = true;
							break;
						}
					}

					if (!added) list.add(upgradeSlots[i].copy());
				}

				else list.add(upgradeSlots[i].copy());
			}
		}

		return list.toArray(new ItemStack[list.size()]);
	}

	@Override
	public int getSizeUpgradeSlots() {
		return 0x4;
	}

	@Override
	public boolean canInsertItemUpgrade(IItemUpgradeComponent component, ItemStack stack) {
		return component.effectOnMachines(this, true) && component.maxSize() >= stack.stackSize;
	}

}

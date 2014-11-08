package com.projectzed.api.tileentity.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.api.block.AbstractBlockMachine;
import com.projectzed.api.machine.IEnergyMachine;
import com.projectzed.api.storage.IEnergyContainer;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.mod.handler.PacketHandler;
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
public abstract class AbstractTileEntityMachine extends AbstractTileEntityGeneric implements IEnergyMachine {

	protected int[] slotTop, slotBottom, slotRight;

	protected int maxStorage = 10000;
	protected int stored;
	protected int energyBurnRate = 2;
	protected boolean powerMode;

	public int cookTime;
	public static int defaultCookTime = 200;
	public static int scaledTime = (defaultCookTime / 10) * 5;

	public AbstractTileEntityMachine(String name) {
		super();
		setCustomName("container." + name);
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
	public abstract boolean canInsertItem(int slot, ItemStack stack, int side);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public abstract boolean canExtractItem(int slot, ItemStack stack, int side);

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int i) {
		return this.cookTime * i / scaledTime;
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
	// TODO: Change this code to match better with AbstractTileEntityEnergyPipe and AbstractTileEntityGenerator.
	public void transferPower() {
		if (this.stored >= this.maxStorage) {
			this.stored = this.maxStorage;
			return;
		}

		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;
		List<IEnergyContainer> containers = new ArrayList<IEnergyContainer>();

		// -x
		if (worldObj.getTileEntity(x - 1, y, z) != null && worldObj.getTileEntity(x - 1, y, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x - 1, y, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x - 1, y, z);
			containers.add(cont);
		}

		// +x
		if (worldObj.getTileEntity(x + 1, y, z) != null && worldObj.getTileEntity(x + 1, y, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x + 1, y, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x + 1, y, z);
			containers.add(cont);
		}

		// -y
		if (worldObj.getTileEntity(x, y - 1, z) != null && worldObj.getTileEntity(x, y - 1, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y - 1, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y - 1, z);
			containers.add(cont);
		}

		// +y
		if (worldObj.getTileEntity(x, y + 1, z) != null && worldObj.getTileEntity(x, y + 1, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y + 1, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y + 1, z);
			containers.add(cont);
		}

		// -z
		if (worldObj.getTileEntity(x, y, z - 1) != null && worldObj.getTileEntity(x, y, z - 1) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y, z - 1) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y, z - 1);
			containers.add(cont);
		}

		// +z
		if (worldObj.getTileEntity(x, y, z + 1) != null && worldObj.getTileEntity(x, y, z + 1) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y, z + 1) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y, z + 1);
			containers.add(cont);
		}

		if (containers.size() > 0) {
			for (IEnergyContainer c : containers) {
				if (this.stored >= this.maxStorage) {
					this.stored = this.maxStorage;
					break;
				}
				if (c.getEnergyStored() - c.getMaxExportRate() > 0) this.stored += c.requestPower(this, c.getMaxExportRate());
			}
		}

		containers.removeAll(Collections.EMPTY_LIST);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.storage.IEnergyContainer#worldVec()
	 */
	public Vector4Helper<Integer> worldVec() {
		return new Vector4Helper<Integer>(this.xCoord, this.yCoord, this.zCoord);
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

			if (this.isBurning() && this.canSmelt()) {
				this.cookTime++;
				this.powerMode = true;

				if (this.cookTime == scaledTime) {
					this.cookTime = 0;
					this.smeltItem();
					flag1 = true;
				}
			}
			else {
				this.cookTime = 0;
				this.powerMode = false;
			}

			if (flag != this.stored > 0) {
				flag1 = true;
				((AbstractBlockMachine) this.blockType).updateBlockState(this.cookTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
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
		return Reference.Constants.BASE_MACH_USAGE;
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
		PacketHandler.INSTANCE.sendToAll(new MessageTileEntityMachine(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		NBTTagList tagList = comp.getTagList("Items", 10);
		this.slots = new ItemStack[this.getSizeInvenotry()];

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound temp = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte b0 = temp.getByte("Slot");

			if (b0 >= 0 && b0 < this.slotTop.length) this.slots[b0] = ItemStack.loadItemStackFromNBT(temp);
		}

		this.cookTime = comp.getShort("CookTime");
		this.stored = comp.getInteger("ProjectZedPowerStored");
		this.powerMode = comp.getBoolean("ProjectZedPowerMode");

		if (comp.hasKey("CustomName")) this.customName = comp.getString("CustomName");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		comp.setShort("CookTime", (short) this.cookTime);
		comp.setInteger("ProjectZedPowerStored", this.stored);
		comp.setBoolean("ProjectZedPowerMode", this.powerMode);
		NBTTagList tagList = comp.getTagList("Items", 10);

		for (int i = 0; i < this.slots.length; i++) {
			if (this.slots[i] != null) {
				NBTTagCompound temp = new NBTTagCompound();
				comp.setByte("Slot", (byte) i);
				this.slots[i].writeToNBT(temp);
				tagList.appendTag(temp);
			}
		}

		comp.setTag("Items", tagList);

		if (this.hasCustomInventoryName()) comp.setString("CustomName", this.customName);
	}

	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityMachine(this));
	}

}

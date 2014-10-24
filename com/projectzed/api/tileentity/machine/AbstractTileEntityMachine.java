package com.projectzed.api.tileentity.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;

import com.projectzed.api.block.AbstractBlockMachine;
import com.projectzed.api.machine.IEnergyMachine;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityMachine;

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

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	public abstract int getSizeInventory();

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 */
	public abstract int getInventoryStackLimit();

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initContentsArray()
	 */
	protected abstract void initContentsArray();

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initSlotsArray()
	 */
	protected abstract void initSlotsArray();

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#setCustomName(java.lang.String)
	 */
	public void setCustomName(String name) {
		this.customName = name;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	public abstract boolean isItemValidForSlot(int slot, ItemStack stack);

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getAccessibleSlotsFromSide(int)
	 */
	public abstract int[] getAccessibleSlotsFromSide(int side);

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	public abstract boolean canInsertItem(int slot, ItemStack stack, int side);

	/* (non-Javadoc)
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
	
	protected boolean canSmelt() {
		if (this.slots[0] == null || this.stored + this.energyBurnRate <= 0) return false;
		else {
			// Check if the item in the slot 1 can be smelted (has a set furnace recipe).
			ItemStack stack = FurnaceRecipes.smelting().getSmeltingResult(this.slots[0]);
			if (stack == null) return false;
			if (this.slots[1] == null) return true;
			if (!this.slots[1].isItemEqual(stack)) return false;
			
			// Add the result of the furnace recipe to the current stack size (already smelted so far).
			int result = this.slots[1].stackSize + stack.stackSize;
			
			// Make sure we aren't going over the set stack limit's size.
			return (result <= getInventoryStackLimit() && result <= stack.getMaxStackSize());
		}
	}
	
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.slots[0]);

			if (this.slots[1] == null) {
				this.slots[1] = itemstack.copy();
			}
			else if (this.slots[1].isItemEqual(itemstack)) {
				slots[1].stackSize += itemstack.stackSize;
			}

			this.slots[0].stackSize--;

			if (this.slots[0].stackSize <= 0) {
				this.slots[0] = null;
			}
		}
	}
	
	// TODO: Fix this!
	public void transferPower() {
		
		for (int y = this.yCoord - 1; y <= this.yCoord + 1; y++) {
			for (int x = this.xCoord - 1; x <= this.xCoord + 1; x++) {
				for (int z = this.zCoord - 1; z <= this.zCoord + 1; z++) {
					
					if (worldObj.getTileEntity(x, y, z) != null && worldObj.getTileEntity(x, y, z) instanceof AbstractTileEntityGenerator) {
						AbstractTileEntityGenerator te = (AbstractTileEntityGenerator) worldObj.getTileEntity(x, y, z);
						if (te.getEnergyStored() > 0 && this.stored + 5 <= this.maxStorage) {
							// ProjectZed.logHelper.info("Boobs! 2");
							this.stored += 5; 
							break;
						}
					}
				}
			}
		}
		
	}
	
	public void updateEntity() {
		transferPower();
		boolean flag = this.stored > 0;
		boolean flag1 = false;

		if (this.stored > 0) burnEnergy();

		if (!this.worldObj.isRemote) {

			// ProjectZed.logHelper.info(this.cookTime);
			
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
				((AbstractBlockMachine) this.blockType).updateBlockState(this.stored > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}

		if (flag1) this.markDirty();
		// ProjectZed.logHelper.info(this.stored);
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
	 * @see com.projectzed.api.machine.IEnergyMachine#setEnergyBurnRate(int)
	 */
	public void setEnergyBurnRate(int val) {
		this.energyBurnRate = val;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.machine.IEnergyMachine#getEnergyBurnRate()
	 */
	public int getEnergyBurnRate() {
		return this.energyBurnRate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.machine.IEnergyMachine#setBurning(boolean)
	 */
	public void setPowerMode(boolean val) {
		this.powerMode = val;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.machine.IEnergyMachine#isBurning()
	 */
	public boolean isPoweredOn() {
		return this.powerMode;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.machine.IEnergyMachine#burnEnergy()
	 */
	public void burnEnergy() {
		if (isPoweredOn() && this.cookTime > 0) this.stored -= this.energyBurnRate;
		PacketHandler.INSTANCE.sendToAll(new MessageTileEntityMachine(this));
	}
	
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
		
		if (this.hasCustomInventoryName()) comp.setString("CustomName", this.customName);
	}
	
	@Override
	public Packet getDescriptionPacket() { 
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityMachine(this));
	}

}

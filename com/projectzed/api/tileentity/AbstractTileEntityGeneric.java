/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

/**
 * Class used for easily creating a generic tile entity.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public abstract class AbstractTileEntityGeneric extends TileEntity implements ISidedInventory {

	/**
	 * Include only slots in the UI and specifically not the player's inventory.
	 */
	protected ItemStack[] slots;
	protected String customName;

	public AbstractTileEntityGeneric() {
		this.customName = "container.generic";
		initContentsArray();
		initSlotsArray();
	}

	/**
	 * Method used for initializing the contents array and size of container.
	 */
	protected abstract void initContentsArray();

	/**
	 * Method used for initializing slots in the container. <br>
	 * NOTE: This is a helper method with its only purpose is to assist the dev and therefore may be left blank. See example below. <br>
	 * <br>
	 * EX. For a vanilla furnace, this would be: <br>
	 * this.slots_bottom = new int[] { 2, 1 }; <br>
	 * this.slots_top = new int[] { 0 }; <br>
	 * this.slots_sides = new int[] { 1 };
	 */
	protected abstract void initSlotsArray();

	/**
	 * Get the defined size of the inventory.
	 * @return inventory size as integer.
	 */
	public int getSizeInvenotry() {
		return this.slots != null ? this.slots.length : 0;
	}

	/**
	 * Get the itemstack in slot defined.
	 * @param slot = slot id.
	 * @return itemstack in slot.
	 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		return slot >= 0 && slot < this.slots.length ? this.slots[slot] : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.IInventory#decrStackSize(int, int)
	 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (this.slots[slot] != null) {
			ItemStack itemstack;

			if (this.slots[slot].stackSize <= amount) {
				itemstack = this.slots[slot];
				this.slots[slot] = null;
				return itemstack;
			}
			else {
				itemstack = this.slots[slot].splitStack(amount);

				if (this.slots[slot].stackSize == 0) this.slots[slot] = null;
				return itemstack;
			}
		}
		else return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.IInventory#getStackInSlotOnClosing(int)
	 */
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.IInventory#setInventorySlotContents(int, net.minecraft.item.ItemStack)
	 */
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.slots[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) stack.stackSize = this.getInventoryStackLimit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.IInventory#isUseableByPlayer(net.minecraft.entity.player.EntityPlayer)
	 */
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	/**
	 * Main update method for a given entity. <br>
	 * NOTE: This should be overriden 99% of the time.
	 */
	public void updateEntity() {
		super.updateEntity();
	}

	/**
	 * Handles loading and reading data from memory. <br>
	 * NOTE: This method should be overridden as necessary.
	 */
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		this.slots = new ItemStack[this.getSizeInvenotry()];
		NBTTagList tagList = comp.getTagList("Items", 10);

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound temp = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte b0 = temp.getByte("Slot");

			if (b0 >= 0 && b0 < this.slots.length) this.slots[b0] = ItemStack.loadItemStackFromNBT(temp);
		}

		if (comp.hasKey("CustomName")) this.customName = comp.getString("CustomName");
	}

	/**
	 * Handles saving and writing to memory. <br>
	 * NOTE: This method should be overridden as necessary.
	 */
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);

		if (this.slots != null && this.slots.length > 0) {
			NBTTagList tagList = comp.getTagList("Items", 10);

			for (int i = 0; i < this.slots.length; i++) {
				if (this.slots[i] != null) {
					NBTTagCompound temp = new NBTTagCompound();
					temp.setByte("Slot", (byte) i);
					this.slots[i].writeToNBT(temp);
					tagList.appendTag(temp);
				}
			}

			comp.setTag("Items", tagList);
		}

		if (this.hasCustomInventoryName()) comp.setString("CustomName", this.customName);
	}

	/**
	 * Method called when user opens inventory.
	 */
	public void openInventory() {
	}

	/**
	 * Method called when user closes inventory.
	 */
	public void closeInventory() {
	}

	/**
	 * Getter function for getting the custom name if the te has one.
	 */
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "container.generic";
	}

	/**
	 * Getter function for getting whether the te has a custom name or not.
	 */
	public boolean hasCustomInventoryName() {
		return this.customName != null && this.customName.length() > 0;
	}

	/**
	 * Method used to set the custom name of the tile entity. <br>
	 * NOTE: By default, the custom name is set to container.generic.
	 * @param name = new customized name.
	 */
	public abstract void setCustomName(String name);

	/**
	 * Function determines if stated itemstack can be placed in slot defined.
	 * @param slot = slot id.
	 * @param stack = stack to be placed.
	 */
	public abstract boolean isItemValidForSlot(int slot, ItemStack stack);

	/**
	 * Determines if side of block is accessible from side of said block.
	 * @param side = side id.
	 */
	public abstract int[] getAccessibleSlotsFromSide(int side);

	/**
	 * Determines if said itemstack can be placed in slot from side defined.
	 * @param slot = slot id.
	 * @param stack = stack to insert.
	 * @param side = side id.
	 */
	public abstract boolean canInsertItem(int slot, ItemStack stack, int side);

	/**
	 * Determines if said itemstack can be pulled from in slot to side defined.
	 * @param slot = slot id.
	 * @param stack = stack to insert.
	 * @param side = side id.
	 */
	public abstract boolean canExtractItem(int slot, ItemStack stack, int side);
}

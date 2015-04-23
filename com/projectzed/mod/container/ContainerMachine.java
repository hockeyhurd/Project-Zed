/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Generic container class for most machines.
 * 
 * @author hockeyhurd
 * @version Oct 23, 2014
 */
public class ContainerMachine extends Container {

	protected AbstractTileEntityMachine te;
	protected int stored;
	protected boolean powerMode;
	protected final int NUM_SLOTS;

	/** Time left for this furnace to burn for. */
	public int lastBurnTime;

	/** The start time for this fuel. */
	public int lastItemBurnTime;

	/** How long time left before item is cooked. */
	public int lastCookTime;

	/**
	 * @param inv = inventory of player as reference.
	 * @param te = tile entity to append to as reference.
	 */
	public ContainerMachine(InventoryPlayer inv, AbstractTileEntityMachine te) {
		this.te = te;
		this.NUM_SLOTS = te.getSizeInvenotry();
		addSlots(inv, te);
	}

	/**
	 * Adds all slots, player and container.
	 * 
	 * @param inv inventory.
	 * @param te tile entity object.
	 */
	protected void addSlots(InventoryPlayer inv, AbstractTileEntityMachine te) {
		// Add 'crafting' slots to container.
		
		if (this.NUM_SLOTS == 1) this.addSlotToContainer(new Slot(te, 0, 79, 21));
		
		else if (this.NUM_SLOTS == 2) {
			this.addSlotToContainer(new Slot(te, 0, 41, 21));
			this.addSlotToContainer(new SlotFurnace(inv.player, te, 1, 121, 21));
		}
		
		else if (this.NUM_SLOTS == 3) {
			this.addSlotToContainer(new Slot(te, 0, 30, 21));
			this.addSlotToContainer(new Slot(te, 2, 55, 21));
			this.addSlotToContainer(new SlotFurnace(inv.player, te, 1, 121, 21));
		}

		// Adds the player inventory to furnace's gui.
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(inv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		// Adds the player hotbar slots to the gui.
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142)); // 198
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.Container#addCraftingToCrafters(net.minecraft.inventory.ICrafting)
	 */
	@Override
	public void addCraftingToCrafters(ICrafting craft) {
		super.addCraftingToCrafters(craft);
		if (this.NUM_SLOTS > 1) craft.sendProgressBarUpdate(this, 0, this.te.cookTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.Container#canInteractWith(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.Container#detectAndSendChanges()
	 */
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		this.stored = this.te.getEnergyStored();

		if (this.NUM_SLOTS > 1) {
			for (int i = 0; i < this.crafters.size(); i++) {
				ICrafting icrafting = (ICrafting) this.crafters.get(i);
	
				if (this.lastCookTime != this.te.cookTime) icrafting.sendProgressBarUpdate(this, 0, this.te.cookTime);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.Container#updateProgressBar(int, int)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int slot, int newVal) {
		if (this.NUM_SLOTS > 1 && slot == 0) this.te.cookTime = newVal;
	}

	/**
	 * Gets the TE instance.
	 * @return te object.
	 */
	public AbstractTileEntityMachine getTE() {
		return this.te;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.Container#mergeItemStack(net.minecraft.item.ItemStack, int, int, boolean)
	 */
	@Override
	public boolean mergeItemStack(ItemStack stack, int start, int end, boolean reverse) {
		return super.mergeItemStack(stack, start, end, reverse);
	}

	/**
	 * Player shift-clicking a slot.
	 * @see net.minecraft.inventory.Container#transferStackInSlot(net.minecraft.entity.player.EntityPlayer, int)
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack stack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();
			if (index < te.getSizeInvenotry()) {
				if (!this.mergeItemStack(slotStack, te.getSizeInvenotry(), this.inventorySlots.size(), false)) return null;
			}
			
			else {
				if (!this.getSlot(0).isItemValid(slotStack) || !this.mergeItemStack(slotStack, 0, te.getSizeInvenotry(), false)) return null;
			}

			if (slotStack.stackSize == 0) slot.putStack((ItemStack) null);
			else slot.onSlotChanged();

			if (slotStack.stackSize == stack.stackSize) return null;
			slot.onPickupFromSlot(player, slotStack);
		}

		return stack;
	}

}

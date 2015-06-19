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
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.projectzed.api.tileentity.digger.AbstractTileEntityDigger;

/**
 * Generalized class for digger machines' container.
 * 
 * @author hockeyhurd
 * @version Jun 19, 2015
 */
public class ContainerDigger extends Container {

	protected AbstractTileEntityDigger te;
	protected int stored;
	protected boolean powerMode;
	protected final int NUM_SLOTS;
	
	public ContainerDigger(InventoryPlayer inv, AbstractTileEntityDigger te) {
		this.te = te;
		this.NUM_SLOTS = te.getSizeInvenotry();
		addSlots(inv, te);
	}

	private void addSlots(InventoryPlayer inv, AbstractTileEntityDigger te) {
		int offset = 0;
		if (this.NUM_SLOTS > 0) {
			offset = 32;
			
			for (int y = 0; y < 2; y++) {
				for (int x = 0; x < 9; x++) {
					this.addSlotToContainer(new Slot(te, x + y * 9, 8 + x * 18, 32 + 53 + y * 18));
				}
			}
		}
		
		// Adds the player inventory to furnace's gui.
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(inv, (x + y * 9) + 9, 8 + x * 18, offset + 32 + 84 + y * 18));
			}
		}

		// Adds the player hotbar slots to the gui.
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, offset + 32 + 142)); // 198
		}
	}
	
	/**
	 * Gets the TE instance.
	 * @return te object.
	 */
	public AbstractTileEntityDigger getTE() {
		return te;
	}

	/* (non-Javadoc)
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

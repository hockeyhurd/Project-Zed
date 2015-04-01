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
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

import com.projectzed.mod.tileentity.machine.TileEntityStoneCraftingTable;
import com.projectzed.mod.util.WorldUtils;

/**
 * Container class for craftingStoneTable.
 * 
 * @author hockeyhurd
 * @version Mar 31, 2015
 */
public class ContainerStoneCraftingTable extends Container {

	/** The crafting matrix inventory (3x3). */
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();
	
	private InventoryPlayer inv;
	private TileEntityStoneCraftingTable te;
	
	private final int NUM_SLOTS;
	
	public ContainerStoneCraftingTable(InventoryPlayer inv, TileEntityStoneCraftingTable te) {
		this.inv = inv;
		this.te = te;
		this.NUM_SLOTS = te.getSizeInvenotry();
		
		addSlots(inv, te);
		
		this.onCraftMatrixChanged(this.craftMatrix);
	}

	/**
	 * Adds all slots, player and container.
	 * @param inv inventory.
	 * @param te tile entity object.
	 */
	private void addSlots(InventoryPlayer inv, TileEntityStoneCraftingTable te) {
		// Add crafting result.
		this.addSlotToContainer(new SlotCrafting(inv.player, this.craftMatrix, this.craftResult, 0, 124, 35));

		// Add crafting matrix
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				// this.addSlotToContainer(new Slot(this.craftMatrix, x + y * 3,
				// 67 + y * 18, 6 + x * 18));
				this.addSlotToContainer(new Slot(this.craftMatrix, x + y * 3, 30 + x * 18, 17 + y * 18));
				ItemStack stack = te.getStackInSlot((x + y * 3));
				if (stack != null && stack.stackSize > 0) this.craftMatrix.setInventorySlotContents(x + y * 3, stack);
			}
		}
		
		// Adds the player inventory to table's gui.
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(inv, (x + y * 9) + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		// Adds the player hotbar slots to the gui.
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142)); // 198
		}
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.Container#canInteractWith(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	/**
	 * Callback for when the crafting matrix is changed.
	 */
	@Override
	public void onCraftMatrixChanged(IInventory inv) {
		if (this.craftMatrix != null) this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.te.getWorldObj()));
		super.onCraftMatrixChanged(inv);
	}
	
	/**
	 * Clears crafting grid matrix.
	 */
	public void clearCraftingGrid() {
		ItemStack stack;
		Slot slot;
		
		for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
			if (this.craftMatrix.getStackInSlot(i) != null) {
				stack = this.craftMatrix.getStackInSlot(i);
				if (this.mergeItemStack(this.craftMatrix.getStackInSlot(i), 3 * 3 + 1, 36, false)) {
					this.craftMatrix.setInventorySlotContents(i, (ItemStack) null);
					this.te.setInventorySlotContents(i, (ItemStack) null);
				}
				
				else {
					WorldUtils.addItemDrop(this.craftMatrix.getStackInSlot(i), this.te.getWorldObj(), this.te.xCoord, this.te.yCoord, this.te.zCoord);
					this.craftMatrix.setInventorySlotContents(i, (ItemStack) null);
				}
			}
		}
		
		this.onCraftMatrixChanged(craftMatrix);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.Container#onContainerClosed(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public void onContainerClosed(EntityPlayer player) {
		for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
			this.te.setInventorySlotContents(i, this.craftMatrix.getStackInSlot(i));
		}
		
		this.onCraftMatrixChanged(this.craftMatrix);
	}

	/**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotID);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotID == 0) {
				if (!this.mergeItemStack(itemstack1, 10, 46, true)) { return null; }

				slot.onSlotChange(itemstack1, itemstack);
			}
			
			else if (slotID >= 10 && slotID < 37) {
				if (!this.mergeItemStack(itemstack1, 37, 46, false)) return null; 
			}
			
			else if (slotID >= 37 && slotID < 46) {
				if (!this.mergeItemStack(itemstack1, 10, 37, false)) return null;
			}
			
			else if (!this.mergeItemStack(itemstack1, 10, 46, false)) return null; 

			if (itemstack1.stackSize == 0) slot.putStack((ItemStack) null);
			else slot.onSlotChanged();

			if (itemstack1.stackSize == itemstack.stackSize) return null; 

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}
	
}

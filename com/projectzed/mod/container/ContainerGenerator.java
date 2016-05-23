/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.container;

import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class used as generic container for most/all generators.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public class ContainerGenerator extends Container {

	private AbstractTileEntityGenerator te;
	private int stored;
	private boolean powerMode;
	private final int NUM_SLOTS;
	private int xOffset, yOffset;

	public ContainerGenerator(InventoryPlayer inv, AbstractTileEntityGenerator te) {
		this(inv, te, 0, 0);
	}

	public ContainerGenerator(InventoryPlayer inv, AbstractTileEntityGenerator te, int xOffset, int yOffset) {
		this.te = te;
		this.NUM_SLOTS = te.getSizeInventory();
		this.xOffset = xOffset;
		this.yOffset = yOffset;

		addSlots(inv, te);
	}

	/**
	 * Adds all slots, player and container.
	 * @param inv = inventory.
	 * @param te = tile entity object.
	 */
	private void addSlots(InventoryPlayer inv, AbstractTileEntityGenerator te) {
		if (this.NUM_SLOTS == 0) {
		}
		else if (this.NUM_SLOTS == 1) this.addSlotToContainer(new Slot(te, 0, 79, 21));

		// Adds the player inventory to furnace's gui.
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(inv, (x + y * 9) + 9, xOffset + 8 + x * 18, yOffset + 84 + y * 18));
			}
		}

		// Adds the player hotbar slots to the gui.
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inv, i, xOffset + 8 + i * 18, yOffset + 142)); // 198
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public void detectAndSendChanges() {
		this.stored = this.te.getEnergyStored();
		this.powerMode = this.te.canProducePower();
		super.detectAndSendChanges();
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int newVal, boolean mode) {
		this.te.setEnergyStored(newVal);
		this.te.setPowerMode(mode);
	}
	
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
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index <= te.getSizeInventory() - 1) {
				if (!this.mergeItemStack(itemstack1, te.getSizeInventory(), this.getInventory().size(), true)) return null;

				slot.onSlotChange(itemstack1, itemstack);
			}
			else {
				if (!this.mergeItemStack(itemstack1, 0, te.getSizeInventory(), false)) return null;
			}

			if (itemstack1.stackSize == 0) slot.putStack((ItemStack) null);
			else slot.onSlotChanged();

			if (itemstack1.stackSize == itemstack.stackSize) return null; 

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}

	/**
	 * Gets the TE instance.
	 * @return te object.
	 */
	public AbstractTileEntityGenerator getTE() {
		return this.te;
	}

}

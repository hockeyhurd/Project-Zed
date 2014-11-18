package com.projectzed.mod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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

	public ContainerGenerator(InventoryPlayer inv, AbstractTileEntityGenerator te) {
		this.te = te;
		this.NUM_SLOTS = te.getSizeInvenotry();
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
				this.addSlotToContainer(new Slot(inv, (x + y * 9) + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		// Adds the player hotbar slots to the gui.
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142)); // 198
		}
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
	
	public boolean mergeItemStack(ItemStack stack, int start, int end, boolean reverse) {
		return super.mergeItemStack(stack, start, end, reverse);
	}

	/**
	 * Player shift-clicking a slot.
	 * @see net.minecraft.inventory.Container#transferStackInSlot(net.minecraft.entity.player.EntityPlayer, int)
	 */
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index <= te.getSizeInvenotry() - 1) {
				if (!this.mergeItemStack(itemstack1, te.getSizeInvenotry(), this.getInventory().size(), true)) return null; 

				slot.onSlotChange(itemstack1, itemstack);
			}
			else {
				if (!this.mergeItemStack(itemstack1, 0, te.getSizeInvenotry(), false)) return null;
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

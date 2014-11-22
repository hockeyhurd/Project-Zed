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

import com.projectzed.mod.tileentity.TileEntityFabricationTable;

/**
 * Class containing container code for FabricationTable.
 * 
 * @author hockeyhurd
 * @version Nov 22, 2014
 */
public class ContainerFabricationTable extends Container {

	/** The crafting matrix inventory (3x3). */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
	
	private TileEntityFabricationTable te;
	private final int NUM_SLOTS;

	public ContainerFabricationTable(InventoryPlayer inv, TileEntityFabricationTable te) {
		this.te = te;
		this.NUM_SLOTS = te.getSizeInvenotry();
		addSlots(inv, te);
	}

	/**
	 * Adds all slots, player and container.
	 * @param inv = inventory.
	 * @param te = tile entity object.
	 */
	private void addSlots(InventoryPlayer inv, TileEntityFabricationTable te) {
		
		// Add crafing result.
		this.addSlotToContainer(new SlotCrafting(inv.player, this.craftMatrix, this.craftResult, 0, 161, 24));
		
		// Add crafitn matrix
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				this.addSlotToContainer(new Slot(this.craftMatrix, y + x * 3, 67 + y * 18, 6 + x * 18));
			}
		}
		
		// Add 'chest' slots
		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < 12; x++) {
				this.addSlotToContainer(new Slot(te, (x + y * 9) + 9, 12 + x * 18, 62 + y * 18));
			}
		}
		
		// Adds the player inventory to table's gui.
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(inv, (x + y * 9) + 9, 39 + x * 18, 174 + y * 18));
			}
		}

		// Adds the player hotbar slots to the gui.
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inv, i, 39 + i * 18, 232)); // 198
		}
	}
	
	/**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory p_75130_1_) {
    	this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.te.getWorldObj()));
    }

	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
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
			
			if (index < 10) {
				if (!this.mergeItemStack(itemstack1, 10 + 6 * 12, te.getSizeInvenotry(), true)) return null; 

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index >= 10 && index < 10 + 6 * 12) {
				if (!this.mergeItemStack(itemstack1, 10 + 6 * 12, te.getSizeInvenotry(), false)) return null;
			}
			
			else {
				if (!this.mergeItemStack(itemstack1, 10, te.getSizeInvenotry() - 4 * 9, false)) return null;
			}

			if (itemstack1.stackSize == 0) slot.putStack((ItemStack) null);
			else slot.onSlotChanged();

			if (itemstack1.stackSize == itemstack.stackSize) return null; 

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}

}

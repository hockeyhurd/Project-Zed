package com.projectzed.mod.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

import com.hockeyhurd.api.math.TimeLapse;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.TileEntityFabricationTable;
import com.projectzed.mod.util.WorldUtils;

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
	private InventoryPlayer inv;

	/**
	 * @param inv
	 * @param te
	 */
	public ContainerFabricationTable(InventoryPlayer inv, TileEntityFabricationTable te) {
		this.te = te;
		this.inv = inv;
		this.NUM_SLOTS = te.getSizeInvenotry();
		addSlots(inv, te);
		
		this.onCraftMatrixChanged(this.craftMatrix);
	}

	/**
	 * Adds all slots, player and container.
	 * @param inv = inventory.
	 * @param te = tile entity object.
	 */
	private void addSlots(InventoryPlayer inv, TileEntityFabricationTable te) {

		// Add crafting result.
		this.addSlotToContainer(new SlotCrafting(inv.player, this.craftMatrix, this.craftResult, 0, 161, 24));

		// Add crafting matrix
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				// this.addSlotToContainer(new Slot(this.craftMatrix, x + y * 3, 67 + y * 18, 6 + x * 18));
				this.addSlotToContainer(new Slot(this.craftMatrix, x + y * 3, 67 + x * 18, 6 + y * 18));
				// ItemStack stack = te.getStackInSlot((x + y * 3) + 0);
				// if (stack != null && stack.stackSize > 0) this.craftMatrix.setInventorySlotContents(x + y * 3, stack);
			}
		}

		// Add 'chest' slots
		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < 12; x++) {
				this.addSlotToContainer(new Slot(te, (x + y * 12) + 10, 12 + x * 18, 62 + y * 18));
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
	public void onCraftMatrixChanged(IInventory inv) {
		// Make sure te side is synced with this container's crafting matrix array.
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			this.te.setInventorySlotContents(i + 1, inv.getStackInSlot(i));
		}

		if (this.craftMatrix != null) this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.te.getWorldObj()));
	}
	
	/**
	 * Method to sort inventory.
	 * @param sortType = sort method (1 for lowest item id to highest, 2 for reverse previous, 3 for a-z by stack name, 4 z-a by stack name).
	 */
	public void sortInventory(int sortType) {
		TimeLapse timeLapse = new TimeLapse();
		HashMap<Integer, List<ItemStack>> map = new HashMap<Integer, List<ItemStack>>();
		HashMap<String, List<ItemStack>> map2 = new HashMap<String, List<ItemStack>>();
		List<ItemStack> outputList = new ArrayList<ItemStack>();
		
		int id = 0;
		String name = "";
		for (int i = 10; i < te.getSizeInvenotry(); i++) {
			if (te.getStackInSlot(i) != null) {
				id = Item.getIdFromItem(te.getStackInSlot(i).getItem());
				name = te.getStackInSlot(i).getDisplayName();
				List<ItemStack> tempList = new ArrayList<ItemStack>();
				
				if (sortType <= 2 && map.containsKey(id)) tempList = map.get(id);
				else if (sortType > 2 && map2.containsKey(name)) tempList = map2.get(name);
				
				tempList.add(te.getStackInSlot(i));
				if (sortType <= 2) map.put(id, tempList);
				else if (sortType > 2) map2.put(name, tempList);
				
				te.setInventorySlotContents(i,(ItemStack) null);
			}
		}
		
		if (sortType <= 2) {
			List<Integer> keys = new ArrayList<Integer>(map.keySet());
			Collections.sort(keys);
			if (sortType == 2) Collections.reverse(keys);
			
			for (int i : keys) {
				
				if (map.containsKey(i) && map.get(i) != null && map.get(i).size() > 0) {
					for (ItemStack stack : map.get(i)) {
						outputList.add(stack);
						map.remove(i);
					}
				}
				
			}
		}
		
		else if (sortType > 2) {
			List<String> keys = new ArrayList<String>(map2.keySet());
			Collections.sort(keys);
			if (sortType == 4) Collections.reverse(keys);
			
			for (String s : keys) {
				if (map2.containsKey(s) && map2.get(s) != null && map2.get(s).size() > 0) {
					for (ItemStack stack : map2.get(s)) {
						outputList.add(stack);
						map2.remove(s);
					}
				}
			}
		}
		
		for (int i = 0; i < outputList.size(); i++) {
			if (i + 10 <= this.te.getSizeInvenotry()) this.mergeItemStack(outputList.get(i), this.craftMatrix.getSizeInventory() + 1, this.NUM_SLOTS, false);
		}
		
		ProjectZed.logHelper.info("Completed sorting in " + timeLapse.getEffectiveTimeSince() + " ms!");
	}
	
	/**
	 * Clears crafting grid matrix.
	 */
	public void clearCraftingGrid() {
		for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
			if (this.craftMatrix.getStackInSlot(i) != null) {
				if (this.mergeItemStack(this.craftMatrix.getStackInSlot(i), 3 * 3 + 1, this.NUM_SLOTS, false)) {
					this.craftMatrix.setInventorySlotContents(i, (ItemStack) null);
					this.te.setInventorySlotContents(i + 1, (ItemStack) null);
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
	public void onContainerClosed(EntityPlayer player) {
		clearCraftingGrid();
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.Container#canInteractWith(net.minecraft.entity.player.EntityPlayer)
	 */
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.Container#detectAndSendChanges()
	 */
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.Container#mergeItemStack(net.minecraft.item.ItemStack, int, int, boolean)
	 */
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
				if (!this.mergeItemStack(itemstack1, 10, te.getSizeInvenotry(), false)) return null;

				slot.onSlotChange(itemstack1, itemstack);
			}

			else if (index >= 10 && index < te.getSizeInvenotry()) {
				if (!this.mergeItemStack(itemstack1, te.getSizeInvenotry(), this.inventorySlots.size(), false)) return null;
			}

			else {
				if (!this.mergeItemStack(itemstack1, 10, te.getSizeInvenotry(), false)) return null;
			}

			if (itemstack1.stackSize == 0) slot.putStack((ItemStack) null);
			else slot.onSlotChanged();

			if (itemstack1.stackSize == itemstack.stackSize) return null;

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}

}

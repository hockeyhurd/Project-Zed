/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.container;

import com.hockeyhurd.hcorelib.api.math.TimeLapse;
import com.hockeyhurd.hcorelib.api.tileentity.AbstractTile;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityFabricationTable;
import com.projectzed.mod.tileentity.TileEntityFabricationTable;
import com.projectzed.mod.util.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

import java.util.*;
import java.util.Map.Entry;

/**
 * Class containing container code for FabricationTable.
 *
 * @author hockeyhurd
 * @version Nov 22, 2014
 */
public class ContainerFabricationTable extends Container implements ITileContainer {

	/**
	 * The crafting matrix inventory (3x3).
	 */
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
		this.NUM_SLOTS = te.getSizeInventory();
		addSlots(inv, te);

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	/**
	 * Adds all slots, player and container.
	 *
	 * @param inv = inventory.
	 * @param te  = tile entity object.
	 */
	private void addSlots(InventoryPlayer inv, TileEntityFabricationTable te) {
		// Add crafting matrix
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				// this.addSlotToContainer(new Slot(this.craftMatrix, x + y * 3, 67 + y * 18, 6 + x * 18));
				this.addSlotToContainer(new Slot(this.craftMatrix, x + y * 3, 67 + x * 18, 6 + y * 18));
				ItemStack stack = te.getStackInSlot((x + y * 3));
				if (stack != null && stack.stackSize > 0) this.craftMatrix.setInventorySlotContents(x + y * 3, stack);
			}
		}

		// Add crafting result.
		this.addSlotToContainer(new SlotCrafting(inv.player, this.craftMatrix, this.craftResult,
				craftMatrix.getSizeInventory(), 161, 24));

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
	@Override
	public void onCraftMatrixChanged(IInventory inv) {
		if (craftMatrix != null) {
			craftResult.setInventorySlotContents(craftMatrix.getSizeInventory(),
					CraftingManager.getInstance().findMatchingRecipe(craftMatrix, te.getWorld()));
		}

		super.onCraftMatrixChanged(inv);
	}

	/**
	 * Method to sort inventory.
	 *
	 * @param sortType = sort method (1 for lowest item id to highest, 2 for reverse previous, 3 for a-z by stack name, 4 z-a by stack name).
	 */
	public void sortInventory(int sortType) {
		TimeLapse timeLapse = new TimeLapse();
		HashMap<Integer, List<ItemStack>> map = new HashMap<Integer, List<ItemStack>>();
		HashMap<String, List<ItemStack>> map2 = new HashMap<String, List<ItemStack>>();

		int id = 0;
		String name = "";
		for (int i = 10; i < te.getSizeInventory(); i++) {
			if (te.getStackInSlot(i) != null) {
				id = Item.getIdFromItem(te.getStackInSlot(i).getItem());
				name = te.getStackInSlot(i).getDisplayName();
				List<ItemStack> tempList = new ArrayList<ItemStack>(te.getSizeInventory() - 10);

				if (sortType <= 2 && map.containsKey(id)) tempList = map.get(id);
				else if (sortType > 2 && map2.containsKey(name)) tempList = map2.get(name);

				tempList.add(te.getStackInSlot(i));
				if (sortType <= 2) map.put(id, tempList);
				else if (sortType > 2) map2.put(name, tempList);

				te.setInventorySlotContents(i, null);
			}
		}

		List<ItemStack> outputList = new ArrayList<ItemStack>(map.keySet().size());

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
			if (i + 10 <= this.te.getSizeInventory())
				this.mergeItemStack(outputList.get(i), this.craftMatrix.getSizeInventory() + 1, this.NUM_SLOTS, false);
		}

		ProjectZed.logHelper.info("Completed sorting in " + timeLapse.getEffectiveTimeSince() + " ms!");
	}

	/**
	 * Clears crafting grid matrix.
	 */
	public void clearCraftingGrid() {
		for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
			if (this.craftMatrix.getStackInSlot(i) != null) {
				if (this.mergeItemStack(craftMatrix.getStackInSlot(i), 3 * 3 + 1, NUM_SLOTS, false)) {
					this.craftMatrix.setInventorySlotContents(i, null);
					this.te.setInventorySlotContents(i, null);
				}
				else {
					final int xCoord = te.getPos().getX();
					final int yCoord = te.getPos().getY();
					final int zCoord = te.getPos().getZ();

					WorldUtils.addItemDrop(craftMatrix.getStackInSlot(i), te.getWorld(), xCoord, yCoord, zCoord);
					craftMatrix.setInventorySlotContents(i, null);
				}
			}
		}

		this.onCraftMatrixChanged(craftMatrix);
	}

	@Override
	public void fillCraftingGrid(ItemStack[][] stacks, int limitAmount) {

		if (te.getWorld().isRemote) {

			// Format: Item name -> StackNode { amount, slotIndexes, ItemStack (copy) }
			Map<String, StackNode> inventoryMapping = new HashMap<String, StackNode>(te.getSizeInventory() << 1);
			buildInventoryMap(inventoryMapping);

			if (inventoryMapping.isEmpty()) return;

			/*
			 * We should only need to calculate occurances if limit amount is something other than '1'
			 * Since this was already checked in the JEI transfer handler!
			 */
			if (limitAmount > 1) {
				Map<String, Integer> occurrencesMapping = new TreeMap<String, Integer>();

				// Simulation:
				// Pass 1
				for (int stackIndex = 0; stackIndex < stacks.length; stackIndex++) {
					if (stacks[stackIndex] == null || stacks[stackIndex].length == 0) continue;
					for (int itemOptionIndex = 0; itemOptionIndex < stacks[stackIndex].length; itemOptionIndex++) {
						if (stacks[stackIndex][itemOptionIndex] == null) continue;

						ItemStack stack = stacks[stackIndex][itemOptionIndex].copy();
						// stack.stackSize = limitAmount;
						final String key = stack.getUnlocalizedName();

						if (inventoryMapping.containsKey(key)) {
							if (!occurrencesMapping.containsKey(key)) occurrencesMapping.put(key, 1);
							else occurrencesMapping.put(key, occurrencesMapping.get(key) + 1);

							break;
						}
					}
				}

				// Pass 2 calculate limiting amount:
				for (Entry<String, Integer> entry : occurrencesMapping.entrySet()) {
					limitAmount = Math.min(limitAmount, (int) Math.floor(inventoryMapping.get(entry.getKey()).amount / entry.getValue()));
					limitAmount = Math.max(limitAmount, 1);
					inventoryMapping.get(entry.getKey()).amount -= limitAmount;
				}
			}

			// limitAmount = Math.max(limitAmount, 1);
			ProjectZed.logHelper.info("limitAmount:", limitAmount);

			// Do moving:
			for (int stackIndex = 0; stackIndex < stacks.length; stackIndex++) {
				if (stacks[stackIndex] == null || stacks[stackIndex].length == 0) continue;
				for (int itemOptionIndex = 0; itemOptionIndex < stacks[stackIndex].length; itemOptionIndex++) {
					if (stacks[stackIndex][itemOptionIndex] == null) continue;

					ItemStack stack = stacks[stackIndex][itemOptionIndex].copy();
					stack.stackSize = limitAmount;
					ItemStack removeStack = stack.copy();

					// if (removeItemStack(removeStack, false)) {
					if (removeItemStack(removeStack, inventoryMapping)) {
						stack.stackSize = limitAmount;
						craftMatrix.setInventorySlotContents(stackIndex, stack);
						te.setInventorySlotContents(stackIndex, stack);
						putStackInSlot(stackIndex, stack);

						break;
					}
				}
			}

			this.onCraftMatrixChanged(craftMatrix);
			detectAndSendChanges();

			PacketHandler.INSTANCE.sendToServer(new MessageTileEntityFabricationTable(te, 2));
		}

		else {
			for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
				if (stacks[i] == null) continue;

				final ItemStack stack = stacks[i][0];
				// final ItemStack stack = te.getStackInSlot(i);
				// craftMatrix.setInventorySlotContents(i, stack);
				te.setInventorySlotContents(i, stack);
				putStackInSlot(i, stack);
			}

			onCraftMatrixChanged(craftMatrix);
			detectAndSendChanges();

			// final Vector3<Double> vec = te.worldVec().getVector3d();
			// PacketHandler.INSTANCE.sendToAllAround(new MessageTileEntityFabricationTable(te),
			//		new NetworkRegistry.TargetPoint(te.getWorld().provider.getDimension(), vec.x, vec.y, vec.z, 16.0f));
		}
	}

	@Deprecated
	private boolean removeItemStack(ItemStack stackToRemove, boolean simulate) {
		if (stackToRemove == null || stackToRemove.stackSize == 0) return false;

		for (int i = 10; i < te.getSizeInventory() && stackToRemove.stackSize > 0; i++) {
			final ItemStack stack = te.getStackInSlot(i);
			if (stack == null) continue;

			final ItemStack copyComp = stack.copy();
			copyComp.stackSize = stackToRemove.stackSize;

			// if (stackToRemove.isItemEqual(stack)) {
			if (ItemStack.areItemStacksEqual(stackToRemove, copyComp)) {
				int grabAmount = Math.min(stack.stackSize, stackToRemove.stackSize);
				stackToRemove.stackSize -= grabAmount;

				if (!simulate) {
					stack.stackSize -= grabAmount;
					if (stack.stackSize == 0) te.setInventorySlotContents(i, null);
				}
			}
		}

		return stackToRemove.stackSize == 0;
	}

	private boolean removeItemStack(ItemStack stackToRemove, Map<String, StackNode> inventoryMap) {
		if (stackToRemove == null || stackToRemove.stackSize <= 0 || inventoryMap == null || inventoryMap.isEmpty())
			return false;

		ItemStack removeStackCopy = stackToRemove.copy();

		// List<Integer> slotList = inventoryMap.get(stackToRemove.getUnlocalizedName()).slotIndexes;
		final StackNode stackNode = inventoryMap.get(stackToRemove.getUnlocalizedName());
		if (stackNode == null) return false;

		List<Integer> slotList = stackNode.slotIndexes;
		List<Integer> removeList = new LinkedList<Integer>();

		for (int slotIndex : slotList) {
			ItemStack stack = te.getStackInSlot(slotIndex);
			removeStackCopy.stackSize = stack.stackSize;

			if (ItemStack.areItemStacksEqual(removeStackCopy, stack)) {
				final int grabAmount = Math.min(stack.stackSize, stackToRemove.stackSize);
				stackToRemove.stackSize -= grabAmount;
				stack.stackSize -= grabAmount;

				if (stack.stackSize == 0) {
					removeList.add(slotIndex);
					te.setInventorySlotContents(slotIndex, null);
				}
			}

			if (stackToRemove.stackSize == 0) break;
		}

		for (Integer i : removeList) {
			slotList.remove(i);
		}

		return stackToRemove == null || stackToRemove.stackSize == 0;
	}

	/**
	 * Builds a Map of the tileentity's inventory.
	 *
	 * @param map Map to reference.
	 */
	private void buildInventoryMap(Map<String, StackNode> map) {
		if (map == null) return;

		// We need a fresh map!
		if (!map.isEmpty()) map.clear();

		for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
			final ItemStack stack = craftMatrix.getStackInSlot(i);
			if (stack == null) continue;

			final String key = stack.getUnlocalizedName();
			if (!map.containsKey(key)) map.put(key, new StackNode(stack.stackSize, i, stack));
			else {
				final StackNode stackNode = map.get(key);
				stackNode.slotIndexes.add(i);
				stackNode.amount += stack.stackSize;
			}
		}

		for (int i = 10; i < te.getSizeInventory(); i++) {
			final ItemStack stack = te.getStackInSlot(i);
			if (stack == null) continue;

			final String key = stack.getUnlocalizedName();
			if (!map.containsKey(key)) map.put(key, new StackNode(stack.stackSize, i, stack));
			else {
				final StackNode stackNode = map.get(key);
				stackNode.slotIndexes.add(i);
				stackNode.amount += stack.stackSize;
			}
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		final boolean isClient = player.getEntityWorld().isRemote;

		if (isClient) {
			for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
				this.te.setInventorySlotContents(i, this.craftMatrix.getStackInSlot(i));
			}

			PacketHandler.INSTANCE.sendToServer(new MessageTileEntityFabricationTable(te));
		}

		else {
			for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
				craftMatrix.setInventorySlotContents(i, te.getStackInSlot(i));
			}

			this.onCraftMatrixChanged(this.craftMatrix);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}

	@Override
	public boolean mergeItemStack(ItemStack stack, int start, int end, boolean reverse) {
		return super.mergeItemStack(stack, start, end, reverse);
	}

	/**
	 * Player shift-clicking a slot.
	 *
	 * @see net.minecraft.inventory.Container#transferStackInSlot(net.minecraft.entity.player.EntityPlayer, int)
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < 10) {
				if (!this.mergeItemStack(itemstack1, 10, te.getSizeInventory(), false)) return null;

				slot.onSlotChange(itemstack1, itemstack);
			}

			else if (index >= 10 && index < te.getSizeInventory()) {
				if (!this.mergeItemStack(itemstack1, te.getSizeInventory(), this.inventorySlots.size(), false)) return null;
			}

			else {
				if (!this.mergeItemStack(itemstack1, 10, te.getSizeInventory(), false)) return null;
			}

			if (itemstack1.stackSize == 0) slot.putStack((ItemStack) null);
			else slot.onSlotChanged();

			if (itemstack1.stackSize == itemstack.stackSize) return null;

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}

	@Override
	public Container getContainer() {
		return this;
	}

	@Override
	public AbstractTile getTile() {
		return te;
	}

	private static class StackNode {
		int amount;
		ItemStack itemStack;
		List<Integer> slotIndexes;

		StackNode(int amount, ItemStack itemStack) {
			this.amount = amount;
			this.itemStack = itemStack;
			slotIndexes = new LinkedList<Integer>();
		}

		StackNode(int amount, int index, ItemStack itemStack) {
			this.amount = amount;
			this.itemStack = itemStack;
			slotIndexes = new LinkedList<Integer>();
			slotIndexes.add(index);
		}
	}

}

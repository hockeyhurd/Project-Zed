/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.container;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityStoneCraftingTable;
import com.projectzed.mod.tileentity.machine.TileEntityStoneCraftingTable;
import com.projectzed.mod.util.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

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
		this.NUM_SLOTS = te.getSizeInventory();
		
		addSlots(inv, te);

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	/**
	 * @return tileentity in this container.
	 */
	public TileEntityStoneCraftingTable getTE() {
		return te;
	}

	/**
	 * Adds all slots, player and container.
	 * @param inv inventory.
	 * @param te tile entity object.
	 */
	private void addSlots(InventoryPlayer inv, TileEntityStoneCraftingTable te) {
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

		// Add crafting result.
		this.addSlotToContainer(new SlotCrafting(inv.player, this.craftMatrix, this.craftResult, craftMatrix.getSizeInventory(), 124, 35));
		
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

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	/**
	 * Callback for when the crafting matrix is changed.
	 */
	@Override
	public void onCraftMatrixChanged(IInventory inv) {
		if (this.craftMatrix != null)
			craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, te.getWorld()));
		super.onCraftMatrixChanged(inv);
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}
	
	/**
	 * Clears crafting grid matrix.
	 */
	public void clearCraftingGrid() {
		for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
			if (this.craftMatrix.getStackInSlot(i) != null) {
				ItemStack stack = this.craftMatrix.getStackInSlot(i);

				if (this.mergeItemStack(stack, 3 * 3 + 1, 36, false)) {
					this.craftMatrix.setInventorySlotContents(i, null);
					this.te.setInventorySlotContents(i, null);
				}
				
				else {
					WorldUtils.addItemDrop(craftMatrix.getStackInSlot(i), te.getWorld(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
					this.craftMatrix.setInventorySlotContents(i, null);
				}
			}
		}
		
		this.onCraftMatrixChanged(craftMatrix);
		detectAndSendChanges();
	}

	public void fillCraftingGrid(ItemStack[][] stacks, int limitAmount) {

		if (te.getWorld().isRemote) {

			final EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
			for (int stackIndex = 0; stackIndex < stacks.length; stackIndex++) {
				// for (int itemOptionIndex = 0; itemOptionIndex < stacks[stackIndex].length; itemOptionIndex++) {
				// ItemStack stack = stacks[stackIndex][itemOptionIndex].copy();
				if (stacks[stackIndex] == null || stacks[stackIndex][0] == null) continue;
				ItemStack stack = stacks[stackIndex][0].copy();

				// ProjectZed.logHelper.info("FillAmount:", fillAmount);
				stack.stackSize = Math.min(limitAmount, stack.stackSize);

				// InventoryUtils.removeByStack(this, stack);
				removeItemStack(stack.copy(), player);
				craftMatrix.setInventorySlotContents(stackIndex, stack);
				te.setInventorySlotContents(stackIndex, stack);
				putStackInSlot(stackIndex, stack);
				// }
			}

			this.onCraftMatrixChanged(craftMatrix);
			detectAndSendChanges();

			PacketHandler.INSTANCE.sendToServer(new MessageTileEntityStoneCraftingTable(te, (byte) 2));
		}

		else {
			for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
				final ItemStack stack = stacks[i][0];
				// final ItemStack stack = te.getStackInSlot(i);
				// craftMatrix.setInventorySlotContents(i, stack);
				te.setInventorySlotContents(i, stack);
				putStackInSlot(i, stack);
			}

			onCraftMatrixChanged(craftMatrix);
			detectAndSendChanges();

			final Vector3<Double> vec = te.worldVec().getVector3d();
			PacketHandler.INSTANCE.sendToAllAround(new MessageTileEntityStoneCraftingTable(te),
					new NetworkRegistry.TargetPoint(te.getWorld().provider.getDimension(), vec.x, vec.y, vec.z, 16.0f));
		}
	}

	private void removeItemStack(ItemStack stackToRemove, EntityPlayer player) {
		if (stackToRemove == null || stackToRemove.stackSize == 0) return;

		List<ItemStack> list = player.inventoryContainer.getInventory();
		for (int i = 10; i < list.size() && stackToRemove.stackSize > 0; i++) {
			final ItemStack stack = list.get(i);
			if (stackToRemove.isItemEqual(stack)) {
				int grabAmount = Math.min(stack.stackSize, stackToRemove.stackSize);
				stack.stackSize -= grabAmount;
				stackToRemove.stackSize -= grabAmount;

				if (stack.stackSize == 0) player.inventoryContainer.putStackInSlot(i, null);
			}
		}

		player.inventoryContainer.detectAndSendChanges();
		onCraftMatrixChanged(craftMatrix);
		detectAndSendChanges();
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

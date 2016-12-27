/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.container;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.container.slots.SlotFake;
import com.projectzed.mod.container.slots.SlotFakeCrafting;
import com.projectzed.mod.container.slots.SlotPattern;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityPatternEncoder;
import com.projectzed.mod.tileentity.machine.TileEntityPatternEncoder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

/**
 * Container for pattern encoding.
 *
 * @author hockeyhurd
 * @version 4/30/2016.
 */
public class ContainerPatternEncoder extends ContainerMachine {

	public final InventoryCrafting craftMatrix = new InventoryCrafting(this, TileEntityPatternEncoder.CRAFTING_MATRIX_SIZE,
			TileEntityPatternEncoder.CRAFTING_MATRIX_SIZE);
	private IInventory craftResult = new InventoryCraftResult();

	private SlotPattern patternIn;
	private SlotPattern patternOut;

	public ContainerPatternEncoder(InventoryPlayer inv, TileEntityPatternEncoder te) {
		super(inv, te, false);

		this.onCraftMatrixChanged(this.craftMatrix);
		addSlots(inv, te);
	}

	@Override
	protected void addSlots(InventoryPlayer inv, AbstractTileEntityMachine te) {
		// Add crafting matrix
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				this.addSlotToContainer(new SlotFake(this.craftMatrix, x + y * 3, 20 + x * 18, 6 + y * 18));
				ItemStack stack = te.getStackInSlot((x + y * 3));
				if (stack != null && stack.stackSize > 0) this.craftMatrix.setInventorySlotContents(x + y * 3, stack);
			}
		}

		// Add crafting result.
		this.addSlotToContainer(new SlotFakeCrafting(inv.player, this.craftMatrix, this.craftResult, 9, 114, 24));

		// Add Pattern slots:
		patternIn = new SlotPattern(this, te, craftMatrix, craftResult, te.getSizeInventory() - 2, 146, 15, true);
		patternOut = new SlotPattern(this, te, craftMatrix, craftResult, te.getSizeInventory() - 1, 146, 33, false);

		this.addSlotToContainer(patternIn);
		this.addSlotToContainer(patternOut);

		// addUpgradeInventorySlots(te);
		addPlayerInventorySlots(inv);
	}

	/**
	 * Clears crafting grid.
	 */
	public void clearSlots() {
		for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
			craftMatrix.setInventorySlotContents(i, null);
			te.setInventorySlotContents(i, null);
		}

		craftResult.setInventorySlotContents(0, null);

		this.onCraftMatrixChanged(craftMatrix);
		detectAndSendChanges();
	}

	public void fillCraftingGrid(ItemStack[][] stacks) {
		if (te.getWorld().isRemote) {
			for (int stackIndex = 0; stackIndex < stacks.length; stackIndex++) {
				if (stacks[stackIndex] == null || stacks[stackIndex].length == 0) continue;

				ItemStack stack = stacks[stackIndex][0];
				if (stack != null) stack = stack.copy();

				craftMatrix.setInventorySlotContents(stackIndex, stack);
				te.setInventorySlotContents(stackIndex, stack);
				putStackInSlot(stackIndex, stack);
			}


			onCraftMatrixChanged(craftMatrix);
			detectAndSendChanges();

			PacketHandler.INSTANCE.sendToServer(new MessageTileEntityPatternEncoder((TileEntityPatternEncoder) te, MessageTileEntityPatternEncoder.FILL));
		}

		else {
			for (int i = 0; i< craftMatrix.getSizeInventory(); i++) {
				final ItemStack stack = stacks[i][0];

				craftMatrix.setInventorySlotContents(i, stack);
				te.setInventorySlotContents(i, stack);
				putStackInSlot(i, stack);
			}

			onCraftMatrixChanged(craftMatrix);
			detectAndSendChanges();

			final Vector3<Double> vec = te.worldVec().getVector3d();
			PacketHandler.INSTANCE.sendToAllAround(new MessageTileEntityPatternEncoder((TileEntityPatternEncoder) te),
					new NetworkRegistry.TargetPoint(te.getWorld().provider.getDimension(), vec.x, vec.y, vec.z, 16.0f));
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory inv) {
		if (craftMatrix != null) {
			craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, te.getWorld()));
			te.setInventorySlotContents(TileEntityPatternEncoder.RESULT_STACK_INDEX, craftResult.getStackInSlot(0));
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
			this.te.setInventorySlotContents(i, this.craftMatrix.getStackInSlot(i));
		}

		this.onCraftMatrixChanged(this.craftMatrix);
		te.setInventorySlotContents(TileEntityPatternEncoder.RESULT_STACK_INDEX, craftResult.getStackInSlot(0));
	}

	@Override
	public void putStackInSlot(int slotID, ItemStack stack) {
		// super.putStackInSlot(slotID, stack);
		// stack.stackSize++;

		if (stack != null) getSlot(slotID).putStack(stack.copy());
	}

	@Override
	public void detectAndSendChanges() {
		for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
			te.setInventorySlotContents(i, craftMatrix.getStackInSlot(i));
		}

		super.detectAndSendChanges();
	}

	@Nullable
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		Slot slot = null;
		// ProjectZed.logHelper.info("slotId:", slotId);

		if (slotId >= 0 && slotId < craftMatrix.getSizeInventory() && (slot = getSlot(slotId)) instanceof SlotFake) {
			SlotFake slotFake = (SlotFake) slot;

			if (clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.PICKUP_ALL) {
				ItemStack stack = player.inventory.getItemStack();

				if (stack != null) {
					stack = stack.copy();
					if (dragType != 0) stack.stackSize = 1;
					slotFake.putStack(stack);
				}

				else slotFake.putStack(null);
			}

			detectAndSendChanges();

			return slotFake.getStack();
		}

		else return super.slotClick(slotId, dragType, clickTypeIn, player);
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

			ProjectZed.logHelper.info("slotID:", slotID);

			if (slotID == 9) {
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

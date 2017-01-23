package com.projectzed.mod.tileentity.machine;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;
import com.projectzed.mod.util.BigItemStack;
import net.minecraft.item.ItemStack;

/**
 * industrialStorageUnit class.
 *
 * @author hockeyhurd
 * @version 1/17/2017.
 */
public class TileEntityIndustrialStorageUnit extends AbstractTileEntityMachine {

	private BigItemStack bigItemStack;

	public TileEntityIndustrialStorageUnit() {
		super("industrialStorageUnit");

		energyBurnRate = 5;
		bigItemStack = new BigItemStack();
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0x40;
	}

	@Override
	protected void initContentsArray() {
	}

	@Override
	protected void initSlotsArray() {
		slots = new ItemStack[2 + getSizeUpgradeSlots()];
	}

	@Override
	public int getSizeUpgradeSlots() {
		return 0;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		super.setInventorySlotContents(slot, stack);

		if (slot == 0) {
			if (stack == null) bigItemStack.empty();
			else if (bigItemStack.isEmpty()) bigItemStack.setItemStack(stack, stack.stackSize);
			else if (ItemStack.areItemsEqual(bigItemStack.getItemStack(), stack)) bigItemStack.addAmount(stack.stackSize);

			if (slots[1] == null) {
				slots[1] = slots[0];
				slots[0] = null;
			}

			else {
				slots[1].stackSize += Math.min(slots[0].stackSize, slots[1].getMaxStackSize() - slots[1].stackSize);
				slots[0] = null;
			}
		}

		/*else {
			if (slots[1] == null) return;
			else if (slots[1] != null) {
				ItemStack slotStack = slots[1];
				bigItemStack.removeAmount(slots[1].stackSize);
				final int remaining = Math.min(slotStack.getMaxStackSize(), bigItemStack.getAmount());

				if (remaining > 0) {
					slotStack.stackSize = remaining;
				}

				else slotStack = null;
			}
		}*/
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 0 && (bigItemStack.isEmpty() || ItemStack.areItemsEqual(bigItemStack.getItemStack(), stack));
	}

	@Override
	protected boolean canSmelt() {
		return true;
	}

	@Override
	public void smeltItem() {
	}

	@Override
	public Sound getSound() {
		return null;
	}

	@Override
	public void update() {
		cookTime = 0;
		super.update();

		/*if (!worldObj.isRemote) {

		}*/
	}

	public BigItemStack getBigItemStack() {
		return bigItemStack;
	}

}

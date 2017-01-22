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

		if (!worldObj.isRemote) {
			if (stack == null) bigItemStack = null;
			else if (bigItemStack == null) bigItemStack = new BigItemStack(stack, stack.stackSize);
			else bigItemStack.setAmount(stack.stackSize);
		}
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 0 && (bigItemStack == null || ItemStack.areItemsEqual(bigItemStack.getItemStack(), stack));
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

}

package com.projectzed.mod.tileentity.machine;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;
import com.projectzed.mod.util.BigItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
            if (stack != null && ItemStack.areItemsEqual(bigItemStack.getItemStack(), stack))
                 bigItemStack.addAmount(stack.stackSize);

            if (slots[1] == null && stack != null) {
                // slots[1] = slots[0];
                bigItemStack.setItemStack(stack.copy(), stack.stackSize);
            }

            slots[0] = null;
        }

        else if (slot == 1) {
            // if (slots[1] == null)
                // bigItemStack.setItemStack(slots[1], slots[1] != null ? slots[1].stackSize : 0);
            // else {
            if (slots[1] != null) {
                if (bigItemStack.isEmpty()) {
                    bigItemStack.setItemStack(slots[1].copy(), slots[1].stackSize);
                }

                else
                    bigItemStack.setAmount(Math.max(bigItemStack.getAmount(), slots[1].stackSize));

                // if (bigItemStack.getAmount() == 0)
                    // bigItemStack.empty();
            }
        }
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

		if (!worldObj.isRemote) {
		    // if (slots[1] == null)
            if (bigItemStack.getItemStack() != null)
                slots[1] = bigItemStack.getItemStack();
            else if (slots[1] != null)
                bigItemStack.setItemStack(slots[1].copy(), slots[1].stackSize);
		}
    }

    public BigItemStack getBigItemStack() {
        return bigItemStack;
    }

    @Override
    public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);

		final int bigItemStackSize = comp.getInteger("bigItemStackSize");
		final ItemStack stack = slots[1];
		bigItemStack.setItemStack(stack, bigItemStackSize);
    }

    @Override
    public void saveNBT(NBTTagCompound comp) {
        super.saveNBT(comp);

        comp.setInteger("bigItemStackSize", bigItemStack.getAmount());
    }

}

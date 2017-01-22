package com.projectzed.mod.util;

import net.minecraft.item.ItemStack;

/**
 * Fake ItemStack to handle itemstacks much larger than limit of 64.
 *
 * @author hockeyhurd
 * @version 1/17/2017.
 */
public class BigItemStack {

	private ItemStack stack;
	private int amount;

	public BigItemStack(ItemStack stack, int amount) {
		this.stack = stack;
		this.amount = amount;

		if (stack.stackSize > amount) amount = stack.stackSize;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int addAmount(int amount) {
		this.amount += Math.max(0, amount);

		stack.stackSize = Math.min(this.amount, stack.getMaxStackSize());

		return this.amount;
	}

	public int removeAmount(int amount) {
		this.amount -= Math.max(0, amount);

		stack.stackSize = Math.max(0, Math.min(this.amount, stack.getMaxStackSize()));
		if (stack.stackSize == 0) stack = null;

		return this.amount;
	}

	public ItemStack getItemStack() {
		if (stack == null) return null;
		else if (amount == 0 || stack.stackSize == 0) return (stack = null);

		final int stackSize = Math.min(amount, stack.stackSize);
		ItemStack ret = stack.copy();
		ret.stackSize = stackSize;

		return ret;
	}

}

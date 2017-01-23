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

	public BigItemStack() {
		stack = null;
		amount = 0;
	}

	public BigItemStack(ItemStack stack, int amount) {
		this.stack = stack;
		this.amount = amount;

		if (stack.stackSize > amount) amount = stack.stackSize;
	}

	public BigItemStack copy() {
		return new BigItemStack(stack != null ? stack.copy() : null, amount);
	}

	public boolean isEmpty() {
		return stack == null || amount == 0;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public BigItemStack empty() {
		stack = null;
		amount = 0;

		return this;
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

	public void setItemStack(ItemStack stack, int amount) {
		if (stack == null || amount == 0) {
			this.stack = null;
			this.amount = 0;
		}

		else {
			this.stack = stack;
			this.amount = amount;
		}
	}

}

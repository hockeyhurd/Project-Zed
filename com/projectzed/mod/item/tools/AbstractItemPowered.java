/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.item.tools;

import com.hockeyhurd.api.util.NumberFormatter;
import com.projectzed.api.energy.IItemChargeable;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.util.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

/**
 * Abstract class for creating chargeable items.
 *
 * @author hockeyhurd
 * @version 3/11/2016.
 */
public abstract class AbstractItemPowered extends Item implements IItemChargeable {

	public final String name;

	// energies
	protected int capacity;
	protected int chargeRate;

	public AbstractItemPowered(String name) {
		this(name, Reference.Constants.BASE_ITEM_Capacity_RATE, Reference.Constants.BASE_ITEM_CHARGE_RATE);
	}

	public AbstractItemPowered(String name, int capacity, int chargeRate) {
		this.name = name;
		this.capacity = capacity;
		this.chargeRate = chargeRate;

		this.setUnlocalizedName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.canRepair = false;
		this.setMaxStackSize(1);
		this.setMaxDamage(capacity / 10);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		itemIcon = reg.registerIcon(ProjectZed.assetDir + this.name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		list.add(EnumChatFormatting.GREEN + "Stored: " + EnumChatFormatting.WHITE + NumberFormatter.format(getStored(stack)) + " McU");
		list.add(EnumChatFormatting.GREEN + "Capacity: " + EnumChatFormatting.WHITE + NumberFormatter.format(this.capacity) + " McU");
	}

	// start energy related things:

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public int getStored(ItemStack stack) {
		if (stack == null || stack.getItem() != this) return 0;
		return capacity - (stack.getItemDamage() * chargeRate);
	}

	@Override
	public void setStored(ItemStack stack, int amount) {
		if (stack == null || stack.getItem() != this || amount < 0 || amount > this.capacity) return;

		int progress = (capacity - amount) / chargeRate;
		stack.setItemDamage(progress);
	}

	@Override
	public int addPower(ItemStack stack, int amount, boolean simulate) {
		if (stack == null || stack.getItem() != this || amount == 0) return 0;

		amount = Math.max(amount, chargeRate);
		int current = getStored(stack);
		int ret;

		if (current + amount >= 0 && current + amount <= capacity) {
			ret = current + amount;

			if (!simulate) setStored(stack, ret);
		}

		else {
			amount = capacity - current;
			ret = current + amount;

			if (!simulate) setStored(stack, ret);
		}

		return ret;
	}

	@Override
	public int subtractPower(ItemStack stack, int amount, boolean simulate) {
		if (stack == null || stack.getItem() != this || amount == 0) return 0;

		int current = getStored(stack);
		int dif = current - amount;
		if (dif >= 0 && dif <= capacity) {
			if (!simulate) setStored(stack, dif);

			return dif;
		}

		return 0;
	}

	@Override
	public int getChargeRate() {
		return chargeRate;
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item.tools;

import com.hockeyhurd.api.util.NumberFormatter;
import com.projectzed.api.energy.IItemChargeable;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.interfaces.IToolSetRegistry;
import com.projectzed.mod.util.Reference.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Abstract class for creating chargeable item tools.
 * 
 * @author hockeyhurd
 * @version Jun 11, 2015
 */
public abstract class AbstractItemPowered extends ItemTool implements IItemChargeable {

	public final String name;
	protected IToolSetRegistry reg;
	
	// energies
	protected int capacity;
	protected int chargeRate;
	
	/**
	 * @param mat tool material of tool.
	 * @param name name of tool.
	 */
	public AbstractItemPowered(ToolMaterial mat, String name, IToolSetRegistry reg) {
		this(mat, name, Constants.BASE_ITEM_Capacity_RATE, Constants.BASE_ITEM_CHARGE_RATE, reg);
	}
	
	/**
	 * @param mat tool material of tool.
	 * @param name name of tool.
	 * @param capacity capacity of tool.
	 * @param chargeRate charge rate of tool. 
	 */
	public AbstractItemPowered(ToolMaterial mat, String name, int capacity, int chargeRate, IToolSetRegistry reg) {
		super(2.0f, mat, reg.getSet());
		this.name = name;

		// energies stuff:
		this.capacity = capacity;
		this.chargeRate = chargeRate;

		this.setUnlocalizedName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.canRepair = false;
		this.setMaxStackSize(1);
		this.setMaxDamage(capacity / 10);
		
		this.reg = reg;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#registerIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		itemIcon = reg.registerIcon(ProjectZed.assetDir + this.name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(EnumChatFormatting.GREEN + "Stored: " + EnumChatFormatting.WHITE + NumberFormatter.format(getStored(stack)) + " McU");
		list.add(EnumChatFormatting.GREEN + "Capacity: " + EnumChatFormatting.WHITE + NumberFormatter.format(this.capacity) + " McU");
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.ItemTool#func_150893_a(net.minecraft.item.ItemStack, net.minecraft.block.Block)
	 */
	@Override
	public float func_150893_a(ItemStack stack, Block block) {
		return reg.setContainsBlock(block) && stack.getItemDamage() < stack.getMaxDamage() ? this.efficiencyOnProperMaterial : 1.0f;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#func_150897_b(net.minecraft.block.Block)
	 */
	@Override
	public boolean func_150897_b(Block block) {
		return reg.setContainsBlock(block);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#onBlockStartBreak(net.minecraft.item.ItemStack, int, int, int, net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public boolean onBlockStartBreak(ItemStack stack, int X, int Y, int Z, EntityPlayer player) {
		return stack.getItemDamage() > stack.getMaxDamage();
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase e) {
		if ((double)block.getBlockHardness(world, x, y, z) != 0.0d) {
			if (stack.getItemDamage() + 1 <= stack.getMaxDamage()) stack.damageItem(1, e);
		}

		return true;
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

		// int progress = (int) Math.floor(amount / (double) capacity * chargeRate);
		int progress = (capacity - amount) / chargeRate;
		// ProjectZed.logHelper.info("Progress:", progress);

		stack.setItemDamage(/*capacity -*/ progress);
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
		if (current - amount >= 0 && current - amount <= capacity) {
			int ret = current - amount;

			if (!simulate) setStored(stack, ret);

			return ret;
		}

		return 0;
	}

	@Override
	public int getChargeRate() {
		return chargeRate;
	}
	
}

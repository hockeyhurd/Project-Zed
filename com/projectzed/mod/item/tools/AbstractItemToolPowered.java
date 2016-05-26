/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item.tools;

import com.hockeyhurd.hcorelib.api.util.NumberFormatter;
import com.projectzed.api.energy.IItemChargeable;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.interfaces.IToolSetRegistry;
import com.projectzed.mod.util.Reference.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

/**
 * Abstract class for creating chargeable item tools.
 * 
 * @author hockeyhurd
 * @version Jun 11, 2015
 */
public abstract class AbstractItemToolPowered extends ItemTool implements IItemChargeable {

	public final String name;
	protected IToolSetRegistry reg;
	
	// energies
	protected int capacity;
	protected int chargeRate;
	
	/**
	 * @param mat tool material of tool.
	 * @param name name of tool.
	 */
	public AbstractItemToolPowered(ToolMaterial mat, String name, IToolSetRegistry reg) {
		this(mat, name, Constants.BASE_ITEM_Capacity_RATE, Constants.BASE_ITEM_CHARGE_RATE, reg);
	}
	
	/**
	 * @param mat tool material of tool.
	 * @param name name of tool.
	 * @param capacity capacity of tool.
	 * @param chargeRate charge rate of tool. 
	 */
	@SuppressWarnings("unchecked")
	public AbstractItemToolPowered(ToolMaterial mat, String name, int capacity, int chargeRate, IToolSetRegistry reg) {
		super(2.0f, 2.0f, mat, (Set<Block>) reg.getSet());
		this.name = name;

		// energies stuff:
		this.capacity = capacity;
		this.chargeRate = chargeRate;

		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.canRepair = false;
		this.setMaxStackSize(1);
		this.setMaxDamage(capacity / 10);
		
		this.reg = reg;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4) {
		list.add(TextFormatting.GREEN + "Stored: " + TextFormatting.WHITE + NumberFormatter.format(getStored(stack)) + " McU");
		list.add(TextFormatting.GREEN + "Capacity: " + TextFormatting.WHITE + NumberFormatter.format(this.capacity) + " McU");
	}
	
	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState block) {
		return reg.setContainsBlock(block.getBlock()) && stack.getItemDamage() < stack.getMaxDamage() ? this.efficiencyOnProperMaterial : 1.0f;
	}

	// TODO: Does commenting this out break anything? If so find correct function to solve this?
	/*@Override
	public boolean func_150897_b(Block block) {
		return reg.setContainsBlock(block);
	}*/

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos blockPos, EntityPlayer player) {
		return stack.getItemDamage() > stack.getMaxDamage();
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState blockState, BlockPos blockPos, EntityLivingBase e) {
		if (blockState.getBlockHardness(world, blockPos) != 0.0f) {
			if (stack.getItemDamage() + 1 <= stack.getMaxDamage()) stack.damageItem(1, e);
		}

		return true;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
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

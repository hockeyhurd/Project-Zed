/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item.tools;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.hockeyhurd.api.util.TimerHelper;
import com.projectzed.api.energy.IItemChargeable;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.DrillSetRegistry;
import com.projectzed.mod.util.Reference.Constants;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * ItemTool class for generic drill.
 * 
 * @author hockeyhurd
 * @version Mar 30, 2015
 */
public class ItemMiningDrill extends ItemTool implements IItemChargeable {

	public final String name;
	private TimerHelper th;
	
	// energies
	private int capacity;
	private int chargeRate;
	
	/**
	 * @param mat tool material of drill.
	 * @param name name of drill.
	 */
	public ItemMiningDrill(ToolMaterial mat, String name) {
		this(mat, name, Constants.BASE_ITEM_Capacity_RATE, Constants.BASE_ITEM_CHARGE_RATE);
	}
	
	/**
	 * @param mat tool material of drill.
	 * @param name name of drill.
	 * @param capacity capacity of drill.
	 * @param chargeRate charge rate of drill. 
	 */
	public ItemMiningDrill(ToolMaterial mat, String name, int capacity, int chargeRate) {
		super(2.0f, mat, DrillSetRegistry.set);
		this.name = name;
		this.setUnlocalizedName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.canRepair = false;
		this.setMaxStackSize(1);
		this.setMaxDamage(capacity / 10);
		
		th = new TimerHelper(20, 2);
		this.capacity = capacity;
		this.chargeRate = chargeRate;
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
		list.add(EnumChatFormatting.GREEN + "Stored: " + EnumChatFormatting.WHITE + (this.capacity - stack.getItemDamage() * 10) + " McU");
		list.add(EnumChatFormatting.GREEN + "Capacity: " + EnumChatFormatting.WHITE + (this.capacity) + " McU");
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.ItemTool#func_150893_a(net.minecraft.item.ItemStack, net.minecraft.block.Block)
	 */
	@Override
	public float func_150893_a(ItemStack stack, Block block) {
        return DrillSetRegistry.set.contains(block) && stack.getItemDamage() < stack.getMaxDamage() ? this.efficiencyOnProperMaterial : 1.0f;
    }
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#func_150897_b(net.minecraft.block.Block)
	 */
	@Override
	public boolean func_150897_b(Block block) {
        return DrillSetRegistry.set.contains(block);
    }

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#onBlockStartBreak(net.minecraft.item.ItemStack, int, int, int, net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public boolean onBlockStartBreak(ItemStack stack, int X, int Y, int Z, EntityPlayer player) {
		return stack.getItemDamage() < stack.getMaxDamage();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#onItemUse(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int, int, float, float, float)
	 */
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ) {
		boolean used = false;
		if (!world.isRemote) {

			int slot = 0;
			ItemStack torchStack = null;
			
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				if (player.inventory.getStackInSlot(i) != null
						&& Block.getBlockFromItem(player.inventory.getStackInSlot(i).getItem()) instanceof BlockTorch) {
					slot = i;
					torchStack = player.inventory.getStackInSlot(i).copy();
					break;
				}
			}
			
			if (torchStack == null) return used;
			
			if (!th.getUse() || th.excuser()) {
				th.setUse(true);
				used = torchStack.getItem().onItemUse(torchStack, player, world, x, y, z, side, clickX, clickY, clickZ);
				
				if (used) {
					player.inventory.decrStackSize(slot, 1);
					player.inventory.markDirty();
					player.inventoryContainer.detectAndSendChanges();
				}
			}
		}
		
		if (!th.getUse()) player.swingItem();
		return used;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#onUpdate(net.minecraft.item.ItemStack, net.minecraft.world.World, net.minecraft.entity.Entity, int, boolean)
	 */
	@Override
	public void onUpdate(ItemStack stack, World world, Entity e, int i, boolean f) {
		th.update();
	}

	@Override
	public int getCapacity() {
		return this.capacity;
	}

	@Override
	public int getStored(ItemStack stack) {
		if (stack.getItem() != this) return 0;
		return this.capacity - stack.getItemDamage();
	}

	@Override
	public void setStored(ItemStack stack, int amount) {
		if (stack.getItem() != this || amount < 0 || amount > this.capacity) return;
		stack.setItemDamage(amount);
	}

	@Override
	public int addPower(ItemStack stack, int amount) {
		if (stack.getItem() != this || amount == 0) return 0;
		
		int current = this.capacity - stack.getItemDamage();
		int ret = 0;
		if (current + amount >= 0 && current + amount <= this.capacity) {
			ret = current + amount; 
			// stack.setItemDamage(ret); 
		}
		
		else {
			amount = this.capacity - current;
			ret = current + amount;
			// stack.setItemDamage(ret / 10);
		}
		
		return ret;
	}

	@Override
	public int subtractPower(ItemStack stack, int amount) {
		if (stack.getItem() != this || amount == 0) return 0;
		
		int current = this.capacity - stack.getItemDamage();
		if (current - amount >= 0 && current - amount <= this.capacity) {
			int ret = current - amount;
			stack.setItemDamage(ret);
			return ret;
		}
		
		return 0;
	}

	@Override
	public int getChargeRate() {
		return this.chargeRate;
	}

}

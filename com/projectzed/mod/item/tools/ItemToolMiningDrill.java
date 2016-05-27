/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item.tools;

import com.hockeyhurd.hcorelib.api.util.TimerHelper;
import com.projectzed.mod.registry.interfaces.IToolSetRegistry;
import com.projectzed.mod.registry.tools.DrillSetRegistry;
import com.projectzed.mod.util.Reference.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * ItemTool class for generic drill.
 * 
 * @author hockeyhurd
 * @version Mar 30, 2015
 */
public class ItemToolMiningDrill extends AbstractItemToolPowered {
	
	private TimerHelper th;
	
	/**
	 * @param mat tool material of drill.
	 * @param name name of drill.
	 */
	public ItemToolMiningDrill(ToolMaterial mat, String name) {
		this(mat, name, Constants.BASE_ITEM_Capacity_RATE, Constants.BASE_ITEM_CHARGE_RATE, DrillSetRegistry.instance());
	}
	
	/**
	 * @param mat tool material of drill.
	 * @param name name of drill.
	 * @param capacity capacity of drill.
	 * @param chargeRate charge rate of drill.
	 * @param reg registry to use.
	 */
	public ItemToolMiningDrill(ToolMaterial mat, String name, int capacity, int chargeRate, IToolSetRegistry reg) {
		super(mat, name, capacity, chargeRate, reg);
		th = new TimerHelper(20, 2);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing side,
			float clickX, float clickY, float clickZ) {
		EnumActionResult used = EnumActionResult.FAIL;
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
				used = torchStack.getItem().onItemUse(torchStack, player, world, blockPos, hand, side, clickX, clickY, clickZ);

				if (used != EnumActionResult.FAIL) {
					player.inventory.decrStackSize(slot, 1);
					player.inventory.markDirty();
					player.inventoryContainer.detectAndSendChanges();
				}
			}
		}

		if (!th.getUse()) player.swingArm(hand);
		return used;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity e, int i, boolean f) {
		super.onUpdate(stack, world, e, i, f);
		th.update();
	}

}

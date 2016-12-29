/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item.tools;

import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.hockeyhurd.hcorelib.api.util.ChatUtils;
import com.hockeyhurd.hcorelib.api.util.TimerHelper;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.IItemAdjustableRadii;
import com.projectzed.mod.registry.interfaces.IToolSetRegistry;
import com.projectzed.mod.registry.tools.DrillSetRegistry;
import com.projectzed.mod.util.Reference.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * ItemTool class for generic drill.
 * 
 * @author hockeyhurd
 * @version Mar 30, 2015
 */
public class ItemToolMiningDrill extends AbstractItemToolPowered implements IItemAdjustableRadii {

	private int radii;
	private TimerHelper timerTorch;

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
		timerTorch = new TimerHelper(20, 2);
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
			
			if (!timerTorch.getUse() || timerTorch.excuser()) {
				timerTorch.setUse(true);
				used = torchStack.getItem().onItemUse(torchStack, player, world, blockPos, hand, side, clickX, clickY, clickZ);

				if (used != EnumActionResult.FAIL) {
					player.inventory.decrStackSize(slot, 1);
					player.inventory.markDirty();
					player.inventoryContainer.detectAndSendChanges();
				}
			}
		}

		if (!timerTorch.getUse()) player.swingArm(hand);
		return used;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState blockState, BlockPos blockPos, EntityLivingBase e) {
		if (super.onBlockDestroyed(stack, world, blockState, blockPos, e) && !world.isRemote && radii > 0 && e instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) e;
			final RayTraceResult rayTrace = rayTrace(world, player, false);
			// ProjectZed.logHelper.info(rayTrace.hitInfo, player.getLookVec());

			for (int i = -radii; i <= radii; i++) {
				for (int j = -radii; j <= radii; j++) {
					BlockPos breakPos;

					if (rayTrace.sideHit == EnumFacing.DOWN || rayTrace.sideHit == EnumFacing.UP) {
						breakPos = BlockUtils.createBlockPos(rayTrace.getBlockPos().getX() + i,
								rayTrace.getBlockPos().getY(), rayTrace.getBlockPos().getZ() + j);
					}

					else if (rayTrace.sideHit == EnumFacing.NORTH || rayTrace.sideHit == EnumFacing.SOUTH) {
						breakPos = BlockUtils
								.createBlockPos(rayTrace.getBlockPos().getX() + i, rayTrace.getBlockPos().getY() + j, rayTrace.getBlockPos().getZ());
					}

					else {
						breakPos = BlockUtils
								.createBlockPos(rayTrace.getBlockPos().getX(), rayTrace.getBlockPos().getY() + j, rayTrace.getBlockPos().getZ() + i);
					}

					final IBlockState blockToBreak = BlockUtils.getBlock(world, breakPos);

					if (!(i != j && i == 0 && j == 0) && blockToBreak != null && reg.matContains(blockToBreak.getBlock()))
						BlockUtils.destroyBlock(world, breakPos);

				}
			}
		}

		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity e, int i, boolean f) {
		super.onUpdate(stack, world, e, i, f);
		timerTorch.update();
	}

	@Override
	public int getRadii() {
		return radii;
	}

	@Override
	public void increment(EntityPlayer player, ItemStack stack) {
		if (radii < ProjectZed.configHandler.getMaxDrillRadii()) {
			radii++;
			writeToNBT(stack);
			player.addChatComponentMessage(ChatUtils.createComponent(false, Constants.RADII_MSG_RADII_SET + radii));
		}
	}

	@Override
	public void decrement(EntityPlayer player, ItemStack stack) {
		if (radii > 0) {
			radii--;
			writeToNBT(stack);
			player.addChatComponentMessage(ChatUtils.createComponent(false, Constants.RADII_MSG_RADII_SET + radii));
		}
	}

	@Override
	public void writeToNBT(ItemStack stack) {
		readFromNBT(stack);

		NBTTagCompound comp = stack.getTagCompound();

		if (comp == null) {
			comp = new NBTTagCompound();
			stack.setTagCompound(comp);
		}

		comp.setInteger("ItemDrillRadii", radii);
	}

	@Override
	public Object[] readFromNBT(ItemStack stack) {
		NBTTagCompound comp = stack.getTagCompound();

		if (comp == null) {
			comp = new NBTTagCompound();
			stack.setTagCompound(comp);
		}

		int num = comp.getInteger("ItemDrillRadii");
		Integer[] val = { num };

		return val;
	}
}

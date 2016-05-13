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

import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.hockeyhurd.hcorelib.api.util.ChatUtils;
import com.hockeyhurd.hcorelib.api.util.NumberFormatter;
import com.hockeyhurd.hcorelib.api.util.TimerHelper;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.IItemAdjustable;
import com.projectzed.mod.util.Reference;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Item class for ItemBlockExchanger.
 *
 * @author hockeyhurd
 * @version 3/11/2016.
 */
public class ItemBlockExchanger extends AbstractItemPowered implements IItemAdjustable {

	private static final String msgType = TextFormatting.GREEN + "[" + Reference.MOD_NAME + "]";
	private static final String msgBlockSet = msgType + " Block set to: ";
	private static final String msgRadiiSet = msgType + " Tool radii set to: ";

	private int radii = 1;
	private IBlockState blockToPlace;
	private TimerHelper timer;

	/**
	 * @param name Name of item.
	 */
	public ItemBlockExchanger(String name) {
		super(name);

		timer = new TimerHelper(20, 2);
	}

	/**
	 * Gets the radii set.
	 *
	 * @return int.
	 */
	public int getRadii() {
		return radii;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		String blockName = blockToPlace != null ? blockToPlace.getBlock().getLocalizedName() : "<empty>";
		list.add(TextFormatting.GREEN + "Block set to: " + TextFormatting.WHITE + blockName);
		list.add(TextFormatting.GREEN + "Radii set to: " + TextFormatting.WHITE + radii);
		list.add(TextFormatting.GREEN + "Stored: " + TextFormatting.WHITE + NumberFormatter.format(getStored(stack)) + " McU");
		list.add(TextFormatting.GREEN + "Capacity: " + TextFormatting.WHITE + NumberFormatter.format(this.capacity) + " McU");
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {

		EnumActionResult result = EnumActionResult.FAIL;

		if (!world.isRemote) {

			/*
		 	* sideHit == 0, bottom sideHit == 1, top sideHit == 2, front sideHit == 3, back sideHit == 4, left sideHit == 5, right
		 	*/

			if (!timer.getUse() && !player.isSneaking() && blockToPlace != null) {
				timer.trigger();

				// ProjectZed.logHelper.info("Side:", side, blockToPlace.getLocalizedName());

				int amountToPlace = radii * 2 + 1;
				amountToPlace *= amountToPlace;

				Map<Integer, Integer> invMap = new HashMap<Integer, Integer>(player.inventory.getSizeInventory() * 3 / 2, 2.0f / 3.0f);
				int itemCount = 0;

				ItemStack current;
				for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
					current = player.inventory.getStackInSlot(i);

					if (current != null && BlockUtils.getBlockFromItem(current.getItem()) == blockToPlace) {
						invMap.put(i, current.stackSize);
						itemCount += current.stackSize;
					}
				}

				if (amountToPlace > itemCount) amountToPlace = itemCount;

				// ProjectZed.logHelper.info("Max # can place:", amountToPlace);

				int counter = amountToPlace;
				IBlockState currentBlock;
				BlockPos pos;
				final int side = facing.ordinal();

				for (int i = -radii; i <= radii; i++) {
					for (int j = -radii; j <= radii; j++) {
						if (counter <= 0) break;

						if (side == 0 || side == 1) {
							pos = VectorHelper.toBlockPos(blockPos.getX() + i, blockPos.getY(), blockPos.getZ() + j);
							currentBlock = BlockUtils.getBlock(world, pos);

							if (currentBlock != blockToPlace && currentBlock.getBlockHardness(world, pos) > 0.0f) {
								BlockUtils.destroyBlock(world, pos);
								BlockUtils.setBlock(world, pos, blockToPlace);
								// world.markBlockForUpdate(x + i, y, z + j);
								world.notifyBlockOfStateChange(pos, currentBlock.getBlock());

								counter--;
								result = EnumActionResult.SUCCESS;
							}
						}

						else if (side == 2 || side == 3) {
							pos = VectorHelper.toBlockPos(blockPos.getX() + i, blockPos.getY() + j, blockPos.getZ());
							currentBlock = BlockUtils.getBlock(world, pos);

							if (currentBlock != blockToPlace && currentBlock.getBlockHardness(world, pos) > 0.0f) {
								BlockUtils.destroyBlock(world, pos);
								BlockUtils.setBlock(world, pos, blockToPlace);
								world.notifyBlockOfStateChange(pos, currentBlock.getBlock());

								counter--;
								result = EnumActionResult.SUCCESS;
							}
						}

						else {
							pos = VectorHelper.toBlockPos(blockPos.getX(), blockPos.getY() + j, blockPos.getZ() + i);
							currentBlock = BlockUtils.getBlock(world, pos);

							if (currentBlock != blockToPlace && currentBlock.getBlockHardness(world, pos) > 0.0f) {
								BlockUtils.destroyBlock(world, pos);
								BlockUtils.setBlock(world, pos, blockToPlace);
								world.notifyBlockOfStateChange(pos, currentBlock.getBlock());

								counter--;
								result = EnumActionResult.SUCCESS;
							}
						}

						// counter--;
					}
				}

				int amountPlaced = amountToPlace - counter;

				for (int i : invMap.keySet()) {
					current = player.inventory.getStackInSlot(i);

					final int displacement = Math.min(current.stackSize, amountPlaced);

					amountPlaced -= displacement;

					if (current.stackSize - displacement <= 0) player.inventory.setInventorySlotContents(i, null);
					else {
						current.stackSize -= displacement;
						player.inventory.setInventorySlotContents(i, current);
					}

					player.inventory.markDirty();
					player.inventoryContainer.detectAndSendChanges();

					if (amountPlaced <= 0) break;
				}
			}

			else blockToPlace = BlockUtils.getBlock(world, blockPos);
		}

		else {
			if (player.isSneaking()) {
				final IBlockState newBlock = BlockUtils.getBlock(world, blockPos);

				if (newBlock != blockToPlace && !(newBlock instanceof BlockContainer)) {
					blockToPlace = newBlock;
					player.addChatComponentMessage(ChatUtils.createComponent(false, msgBlockSet + blockToPlace.getBlock().getLocalizedName()));
				}
			}

			result = EnumActionResult.SUCCESS;
		}

		if (!timer.getUse()) player.swingArm(hand);

		return result;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity e, int i, boolean f) {
		super.onUpdate(stack, world, e, i, f);
		timer.update();
	}

	@Override
	public void increment(EntityPlayer player, ItemStack stack) {
		if (radii < ProjectZed.configHandler.getMaxExchangerRadii()) {
			radii++;
			writeToNBT(stack);
			player.addChatComponentMessage(ChatUtils.createComponent(false, msgRadiiSet + radii));
		}
	}

	@Override
	public void decrement(EntityPlayer player, ItemStack stack) {
		if (radii > 0) {
			radii--;
			writeToNBT(stack);
			player.addChatComponentMessage(ChatUtils.createComponent(false, msgRadiiSet + radii));
		}
	}

	@Override
	public void writeToNBT(ItemStack stack) {
		// int value = readFromNBT(stack)[0];
		readFromNBT(stack);

		NBTTagCompound comp = stack.getTagCompound();

		// if (comp == null) comp = stack.stackTagCompound = new NBTTagCompound();

		comp.setInteger("ItemExchangerRadii", radii);
	}

	@Override
	public Integer[] readFromNBT(ItemStack stack) {
		NBTTagCompound comp = stack.getTagCompound();

		if (comp == null) {
			comp = new NBTTagCompound();
			stack.setTagCompound(comp);
		}

		int num = comp.getInteger("ItemExchangerRadii");
		Integer[] val = { num };

		return val;
	}

}

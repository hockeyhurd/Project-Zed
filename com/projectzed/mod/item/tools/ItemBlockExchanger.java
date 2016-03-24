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

import com.hockeyhurd.api.util.BlockUtils;
import com.hockeyhurd.api.util.ChatHelper;
import com.hockeyhurd.api.util.TimerHelper;
import com.projectzed.mod.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hockeyhurd
 * @version 3/11/2016.
 */
public class ItemBlockExchanger extends AbstractItemPowered {

	private static final String msg = EnumChatFormatting.GREEN + "[" + Reference.MOD_NAME + "] Block set to: ";

	private int radii = 1;
	private Block blockToPlace;
	private TimerHelper timer;
	private ChatHelper chatHelper;

	/**
	 * @param name Name of item.
	 */
	public ItemBlockExchanger(String name) {
		super(name);

		timer = new TimerHelper(20, 2);
		chatHelper = new ChatHelper();
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ) {
		boolean used = false;

		if (!world.isRemote) {

			/*
		 	* sideHit == 0, bottom sideHit == 1, top sideHit == 2, front sideHit == 3, back sideHit == 4, left sideHit == 5, right
		 	*/

			if (!player.isSneaking() && blockToPlace != null) {
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
				Block currentBlock;

				for (int i = -radii; i <= radii; i++) {
					for (int j = -radii; j <= radii; j++) {
						if (counter <= 0) break;

						if (side == 0 || side == 1) {
							currentBlock = BlockUtils.getBlock(world, x + i, y, z + j);

							if (currentBlock != blockToPlace && currentBlock.getBlockHardness(world, x + i, y, z + j) > 0.0f) {
								BlockUtils.destroyBlock(world, x + i, y, z + j);
								BlockUtils.setBlock(world, x + i, y, z + j, blockToPlace);
								world.markBlockForUpdate(x + i, y, z + j);

								counter--;
							}
						}

						else if (side == 2 || side == 3) {
							currentBlock = BlockUtils.getBlock(world, x + i, y + j, z);

							if (currentBlock != blockToPlace && currentBlock.getBlockHardness(world, x + i, y + j, z) > 0.0f) {
								BlockUtils.destroyBlock(world, x + i, y + j, z);
								BlockUtils.setBlock(world, x + i, y + j, z, blockToPlace);
								world.markBlockForUpdate(x + i, y + j, z);

								counter--;
							}
						}

						else {
							currentBlock = BlockUtils.getBlock(world, x, y + j, z + i);

							if (currentBlock != blockToPlace && currentBlock.getBlockHardness(world, x, y + j, z + i) > 0.0f) {
								BlockUtils.destroyBlock(world, x, y + j, z + i);
								BlockUtils.setBlock(world, x, y + j, z + i, blockToPlace);
								world.markBlockForUpdate(x, y + j, z + i);

								counter--;
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

			else blockToPlace = BlockUtils.getBlock(world, x, y, z);
		}

		else {
			if (player.isSneaking()) {
				final Block newBlock = BlockUtils.getBlock(world, x, y, z);

				if (newBlock != blockToPlace && !(newBlock instanceof BlockContainer)) {
					blockToPlace = newBlock;
					player.addChatComponentMessage(chatHelper.comp(msg + blockToPlace.getLocalizedName()));
				}
			}
		}

		if (!timer.getUse()) player.swingItem();

		return used;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity e, int i, boolean f) {
		super.onUpdate(stack, world, e, i, f);
		timer.update();
	}

}

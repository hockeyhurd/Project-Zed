/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.tileentity.machine;

import com.hockeyhurd.api.math.Rect;
import com.hockeyhurd.api.math.Vector3;
import com.hockeyhurd.api.util.BlockUtils;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.LinkedHashMap;

/**
 * TileEntity class for industrialHarvester.
 *
 * @author hockeyhurd
 * @version 9/10/2015.
 */
public class TileEntityIndustrialHarvester extends AbstractTileEntityMachine {

	private Vector3<Integer> currentCheckingVec;
	private Rect<Integer> boundedRect;
	private int currentSize = 1;

	public static final int DEFAULT_RECT_SIZE = 3;
	public static final int DEFAULT_NORMALIZED_RECT_SIZE = 1;

	public TileEntityIndustrialHarvester() {
		super("industrialHarvester");
	}

	public Rect<Integer> getBoundedRect() {
		return boundedRect;
	}

	public void setBoundedRect(Rect<Integer> boundedRect) {
		this.boundedRect = boundedRect;
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0x40;
	}

	@Override
	protected void initContentsArray() {
	}

	@Override
	protected void initSlotsArray() {
		this.slots = new ItemStack[2 * 9 + getSizeUpgradeSlots()];
	}

	@Override
	protected boolean canSmelt() {
		return false;
	}

	@Override
	public void smeltItem() {
	}

	/**
	 * Method used to increment current coordinate checker.
	 */
	private void incrementVector() {
		if (currentCheckingVec.x < boundedRect.max.x) currentCheckingVec.x++;
		else {
			currentCheckingVec.x = boundedRect.min.x;

			if (currentCheckingVec.z < boundedRect.max.y) currentCheckingVec.z++;
			else currentCheckingVec.z = boundedRect.min.y;
		}
	}

	@Override
	public Sound getSound() {
		return null;
	}

	private void addStackToContainer(ItemStack stack) {
		if (stack == null || stack.stackSize <= 0) return;

		ItemStack currentStack;
		int amountCanTransfer, amountToTransfer;

		// first pass, check for merge.
		for (int i = 0; i < getSizeInventory() - getSizeUpgradeSlots(); i++) {
			currentStack = slots[i];

			if (currentStack != null && currentStack.isItemEqual(stack)) {
				amountCanTransfer = currentStack.getMaxStackSize() - currentStack.stackSize;
				if (amountCanTransfer <= 0) continue;

				amountToTransfer = Math.min(amountCanTransfer, stack.stackSize);
				currentStack.stackSize += amountToTransfer;
				stack.stackSize -= amountToTransfer;
			}

			if (stack.stackSize == 0) return;
		}

		for (int i = 0; i < getSizeInventory() - getSizeUpgradeSlots(); i++) {
			if (slots[i] == null) {
				slots[i] = stack;
				break;
			}
		}

	}

	private LinkedHashMap<Vector3<Integer>, Block> createChopList(LinkedHashMap<Vector3<Integer>, Block> currentMap, Vector3<Integer> currentVec, boolean isOriginal) {
		LinkedHashMap<Vector3<Integer>, Block> newMap = new LinkedHashMap<Vector3<Integer>, Block>();
		Vector3<Integer> copy = currentVec.copy();

		Block current;
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			copy.x = currentVec.x + dir.offsetX;
			copy.y = currentVec.y + dir.offsetY;
			copy.z = currentVec.z + dir.offsetZ;

			current = BlockUtils.getBlock(worldObj, copy);
			if (current instanceof BlockLog || current instanceof BlockLeaves) {
				if (!newMap.containsKey(copy) || !currentMap.containsKey(copy)) newMap.putAll(createChopList(newMap, copy, false));
			}
		}

		if (isOriginal) currentMap.putAll(newMap);

		return newMap;
	}

	private void chopTree(BlockLog origin) {
		Vector3<Integer> currentVec = currentCheckingVec.copy();
		LinkedHashMap<Vector3<Integer>, Block> chopMap = new LinkedHashMap<Vector3<Integer>, Block>();

		chopMap.put(currentVec, origin);
		createChopList(chopMap, currentVec, true);

		if (!chopMap.isEmpty()) {
			for (Vector3<Integer> vec : chopMap.keySet()) {
				for (ItemStack stack : chopMap.get(vec).getDrops(worldObj, vec.x, vec.y, vec.z, BlockUtils.getBlockMetadata(worldObj, vec), 0)) {
					addStackToContainer(stack);
				}
			}
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!worldObj.isRemote && boundedRect != null && worldObj.getTotalWorldTime() % 20L == 0) {
			if (currentCheckingVec == null) currentCheckingVec = new Vector3<Integer>(boundedRect.min.x.intValue(), yCoord, boundedRect.min.y.intValue());

			final Block currentBlock = BlockUtils.getBlock(worldObj, currentCheckingVec);

			if (currentBlock instanceof BlockLog) {
				chopTree((BlockLog) currentBlock);
				return;
			}

			incrementVector();
		}
	}

}

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
import com.hockeyhurd.api.util.BlockHelper;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * TileEntity class for industrialPlanter.
 *
 * @author hockeyhurd
 * @version 8/27/2015.
 */
public class TileEntityIndustrialPlanter extends AbstractTileEntityMachine {

	private Vector3<Integer> currentCheckingVec;
	private Rect<Integer> boundedRect;
	private int currentSize = 1;
	private BlockHelper blockHelper;

	public static final int DEFAULT_RECT_SIZE = 3;
	public static final int DEFAULT_NORMALIZED_RECT_SIZE = 1;

	public TileEntityIndustrialPlanter() {
		super("industrialPlanter");
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
		this.slots = new ItemStack[3 * 3 + 2 * 9 + getSizeUpgradeSlots()];
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

	private Block getFirstSapling() {
		Block ret = Blocks.air;

		Block currentBlock;
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] == null) continue;

			currentBlock = Block.getBlockFromItem(slots[i].getItem());

			if (currentBlock instanceof BlockSapling) {
				ret = currentBlock;
				decrStackSize(i, 1);
				break;
			}
		}

		return ret;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!worldObj.isRemote && boundedRect != null && worldObj.getTotalWorldTime() % 20L == 0) {
			if (currentCheckingVec == null) currentCheckingVec = new Vector3<Integer>(boundedRect.min.x.intValue(), yCoord + 1, boundedRect.min.y.intValue());
			if (blockHelper == null) blockHelper = new BlockHelper(worldObj, null);

			final Block block = blockHelper.getBlock(currentCheckingVec);
			final Block sapling = getFirstSapling();

			if (block == Blocks.air && sapling != Blocks.air) {
				blockHelper.setBlock(currentCheckingVec, sapling);
			}

			incrementVector();
		}
	}

}

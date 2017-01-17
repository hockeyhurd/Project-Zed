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

import com.hockeyhurd.hcorelib.api.math.Rect;
import com.hockeyhurd.hcorelib.api.math.Vector2;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.upgrades.ItemRadialUpgrade;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot >= 9 && super.isItemValidForSlot(slot, stack) && BlockUtils.getBlockFromItem(stack.getItem()) instanceof BlockSapling;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return slot >= 9 && super.canExtractItem(slot, stack, side);
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

	/*private Block getFirstSapling() {
		Block ret = Blocks.air;

		Block currentBlock;
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] == null) continue;

			currentBlock = BlockUtils.getBlockFromItem(slots[i].getItem());

			if (currentBlock instanceof BlockSapling) {
				ret = currentBlock;
				decrStackSize(i, 1);
				break;
			}
		}

		return ret;
	}*/

	private int getFirstSapling() {
		Block currentBlock;
		for (int i = 0; i < getSizeInventory(); i++) {
			if (slots[i] != null) {
				currentBlock = BlockUtils.getBlockFromItem(slots[i].getItem());
				if (currentBlock != null && currentBlock instanceof BlockSapling) return i;
			}
		}

		return -1;
	}

	private boolean canPlaceSapling(IBlockState currentBlock, IBlockState blockBelow, Block sapling) {
		return currentBlock.getBlock() == Blocks.AIR && sapling != Blocks.AIR &&
				(blockBelow.getMaterial() == Material.GRASS || blockBelow.getMaterial() == Material.GROUND);
	}

	@Override
	protected void calculateDataFromUpgrades() {
		super.calculateDataFromUpgrades();

		if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0 && getSizeUpgradeSlots() > 0) {
			ProjectZed.logHelper.info("boundedRect:", boundedRect, "currentCheckingVec:", currentCheckingVec);
			// if (true) return;
			final ItemStack[] upgrades = getCurrentUpgrades();

			final int lastSize = currentSize;
			int max = Integer.MIN_VALUE;

			for (ItemStack upgradeComp : upgrades) {
				if (upgradeComp != null && upgradeComp.getItem() instanceof ItemRadialUpgrade) {
					max = Math.max(max, upgradeComp.getMetadata() + 1);
				}
			}

			if (max > 0) max++;

			currentSize = Math.max(max, 1);

			if (lastSize != currentSize) {
				// boundedRect
				final int dif = currentSize - lastSize;
				boundedRect.min.x -= dif;
				boundedRect.min.y -= dif;

				boundedRect.max.x += dif;
				boundedRect.max.y += dif;
			}
		}
	}

	@Override
	public void update() {
		super.update();

		if (!worldObj.isRemote && boundedRect != null && worldObj.getTotalWorldTime() % 20L == 0) {
			// if (currentCheckingVec == null) currentCheckingVec = new Vector3<Integer>(boundedRect.min.x.intValue(), pos.getY() + 2, boundedRect.min.y.intValue());

			// if (currentCheckingVec.z == 1011 && (currentCheckingVec.x >= -325)) ProjectZed.logHelper.info("break!");

			final IBlockState currentBlock = BlockUtils.getBlock(worldObj, currentCheckingVec);
			final IBlockState blockBelow = BlockUtils.getBlock(worldObj, currentCheckingVec.x, currentCheckingVec.y - 1, currentCheckingVec.z);
			final int saplingIndex = getFirstSapling();

			if (saplingIndex != -1) {
				final Block sapling = BlockUtils.getBlockFromItem(slots[saplingIndex]);

				if (canPlaceSapling(currentBlock, blockBelow, sapling)) {
					BlockUtils.setBlock(worldObj, currentCheckingVec, sapling.getDefaultState());
					decrStackSize(saplingIndex, 1);
				}
			}

			incrementVector();
		}
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);

		currentCheckingVec = new Vector3<Integer>(boundedRect.min.x.intValue(), pos.getY() + 2, boundedRect.min.y.intValue());

		if (comp.getBoolean("HasBoundedRect")) {
			int qX0 = comp.getInteger("BoundedMinX");
			int qY0 = comp.getInteger("BoundedMinY");

			int qX1 = comp.getInteger("BoundedMaxX");
			int qY1= comp.getInteger("BoundedMaxY");

			boundedRect = new Rect<Integer>(new Vector2<Integer>(qX0, qY0), new Vector2<Integer>(qX1, qY1));

			currentCheckingVec.x = comp.getInteger("CurrentCheckingVecX");
			currentCheckingVec.y = comp.getInteger("CurrentCheckingVecY");
			currentCheckingVec.z = comp.getInteger("CurrentCheckingVecZ");

			currentSize = comp.getInteger("CurrentSize");
		}
	}

	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);

		comp.setBoolean("HasBoundedRect", boundedRect != null);
		if (boundedRect != null) {
			comp.setInteger("BoundedMinX", boundedRect.min.x);
			comp.setInteger("BoundedMinY", boundedRect.min.y);

			comp.setInteger("BoundedMaxX", boundedRect.max.x);
			comp.setInteger("BoundedMaxY", boundedRect.max.y);

			// if (currentCheckingVec == null) currentCheckingVec = Vector3.zero.getVector3i();
			if (currentCheckingVec == null)
				currentCheckingVec = new Vector3<Integer>(boundedRect.min.x.intValue(), pos.getY() + 2, boundedRect.min.y.intValue());

			comp.setInteger("CurrentCheckingVecX", currentCheckingVec.x);
			comp.setInteger("CurrentCheckingVecY", currentCheckingVec.y);
			comp.setInteger("CurrentCheckingVecZ", currentCheckingVec.z);
			comp.setInteger("CurrentSize", currentSize);
		}
	}
}

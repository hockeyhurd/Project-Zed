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
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.item.IItemUpgradeComponent;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;
import com.projectzed.mod.ProjectZed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack == null || stack.stackSize == 0) return false;

		if (stack.getItem() instanceof IItemUpgradeComponent && slot < getSizeInventory() &&
				slot >= getSizeInventory() - getSizeUpgradeSlots() - 1) {
			// System.out.println("Testing!");
			return canInsertItemUpgrade((IItemUpgradeComponent) stack.getItem(), stack);
		}

		return true;
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

	/**
	 * Method to create and return a 'chop list' of blocks/trees to chop.
	 *
	 * @param currentList current list container.
	 * @param currentCheckingVec current checking vector3i.
	 * @return list of blocks to chop.
	 */
	@SuppressWarnings("unchecked")
	private List<Vector3<Integer>> createChopList(List<Vector3<Integer>> currentList, Vector3<Integer> currentCheckingVec) {
		Vector3<Integer> copy = currentCheckingVec.copy();

		Vector3<Integer> min = new Vector3<Integer>(copy.x - 5, copy.y.intValue(), copy.z - 5);
		Vector3<Integer> max = new Vector3<Integer>(copy.x + 5, copy.y + 32, copy.z + 5);

		Vector3<Integer> bufferVec = Vector3.zero.getVector3i();
		Block currentBlock;
		for (int y = min.y; y < max.y; y++) {
			for (int x = min.x; x < max.x; x++) {
				for (int z = min.z; z < max.z; z++) {
					bufferVec.x = x;
					bufferVec.y = y;
					bufferVec.z = z;

					currentBlock = BlockUtils.getBlock(worldObj, bufferVec).getBlock();

					if (currentBlock == Blocks.air) continue;
					if (currentBlock instanceof BlockLog || currentBlock instanceof BlockLeaves || currentBlock instanceof BlockCrops) {
						currentList.add(bufferVec.copy());
					}
				}
			}
		}

		return currentList;
	}

	/**
	 * Method to perform action of checking and chopping tress.
	 */
	private void chopTree() {
		Vector3<Integer> currentVec = currentCheckingVec.copy();
		int volume = getVolume();
		List<Vector3<Integer>> chopList = new ArrayList<Vector3<Integer>>(volume);

		createChopList(chopList, currentVec);

		if (!chopList.isEmpty()) {
			IBlockState block;
			List<ItemStack> dropsList;
			for (Vector3<Integer> vec : chopList) {
				/*if (vec != null) {
					ProjectZed.logHelper.info(vec, BlockUtils.getBlock(worldObj, vec).getLocalizedName());
					continue;
				}*/

				block = BlockUtils.getBlock(worldObj, vec);

				dropsList = block.getBlock().getDrops(worldObj, VectorHelper.toBlockPos(vec), block, 0);

				if (dropsList != null && !dropsList.isEmpty()) {
					for (ItemStack stack : dropsList) {
						if (stack != null && stack.stackSize > 0) {
							addStackToContainer(stack);
							// BlockUtils.destroyBlock(worldObj, vec);
						}
					}
				}

				BlockUtils.destroyBlock(worldObj, vec, false);
			}
		}

		else ProjectZed.logHelper.warn("Empty chop list, is empty!!");
	}

	/**
	 * Calculates volume of cubic area.
	 *
	 * @return volume.
	 */
	private int getVolume() {
		return Math.abs(Math.abs(boundedRect.max.x) + 5 - Math.abs(boundedRect.min.x) - 5) * (Math.abs(boundedRect.max.y) + 5 - Math.abs(boundedRect.min.y) - 5) * (32);
	}

	@Override
	public void update() {
		super.update();

		if (!worldObj.isRemote && boundedRect != null && worldObj.getTotalWorldTime() % 20L == 0) {
			if (currentCheckingVec == null) currentCheckingVec = new Vector3<Integer>(boundedRect.min.x.intValue(), pos.getY(), boundedRect.min.y.intValue());

			// ProjectZed.logHelper.info(boundedRect.min, currentCheckingVec);
			// ProjectZed.logHelper.info(boundedRect.min, boundedRect.max);

			final Block currentBlock = BlockUtils.getBlock(worldObj, currentCheckingVec).getBlock();
			final int currentMeta = BlockUtils.getBlockMetadata(worldObj, currentCheckingVec);

			// ProjectZed.logHelper.info(currentCheckingVec);
			if (currentBlock instanceof BlockLog || (currentBlock instanceof BlockCrops && currentMeta >= 7)) {
				// chopTree((BlockLog) currentBlock);
				chopTree();
				return;
			}

			incrementVector();
		}
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);

		if (currentCheckingVec == null) currentCheckingVec = Vector3.zero.getVector3i();

		if (comp.getBoolean("HasBoundedRect")) {
			int qX0 = comp.getInteger("BoundedMinX");
			int qY0 = comp.getInteger("BoundedMinY");

			int qX1 = comp.getInteger("BoundedMaxX");
			int qY1= comp.getInteger("BoundedMaxY");

			boundedRect = new Rect<Integer>(new Vector2<Integer>(qX0, qY0), new Vector2<Integer>(qX1, qY1));

			currentCheckingVec.x = comp.getInteger("CurrentCheckingVecX");
			currentCheckingVec.y = comp.getInteger("CurrentCheckingVecY");
			currentCheckingVec.z = comp.getInteger("CurrentCheckingVecZ");
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

			if (currentCheckingVec == null) currentCheckingVec = Vector3.zero.getVector3i();

			comp.setInteger("CurrentCheckingVecX", currentCheckingVec.x);
			comp.setInteger("CurrentCheckingVecY", currentCheckingVec.y);
			comp.setInteger("CurrentCheckingVecZ", currentCheckingVec.z);
		}
	}

}

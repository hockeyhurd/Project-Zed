/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.digger;

import com.hockeyhurd.hcorelib.api.math.Rect;
import com.hockeyhurd.hcorelib.api.math.Vector2;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.item.IItemUpgradeComponent;
import com.projectzed.api.tileentity.digger.AbstractTileEntityDigger;
import com.projectzed.api.util.EnumFilterType;
import com.projectzed.api.util.IItemFilterComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.block.BlockQuarryMarker;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all quarries.
 * 
 * @author hockeyhurd
 * @version Jun 18, 2015
 */
public class TileEntityQuarryBase extends AbstractTileEntityDigger implements IItemFilterComponent {

	protected List<ItemStack> filterStacks;
	protected int filterMaxSize = 0; // 3 * 3;
	protected EnumFilterType itemFilterType;

	/**
	 * @param name name of quarry as required by parent class.
	 */
	public TileEntityQuarryBase(String name) {
		super(name);
		
		this.originalEnergyBurnRate = this.energyBurnRate = 0x200; // 512
				
		this.filterStacks = new ArrayList<ItemStack>(filterMaxSize);
		this.itemFilterType = EnumFilterType.WHITELIST;
	}

	@Override
	protected void initContentsArray() {
		this.slots = new ItemStack[2 * 9 + filterMaxSize + getSizeUpgradeSlots()];
	}

	@Override
	protected void initBlackList() {
		this.blackList = new Material[] {
			Material.air, Material.lava, Material.water, Material.plants, Material.vine, Material.web
		};
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack != null && stack.stackSize > 0 && slot >= getSizeInventory() - getSizeUpgradeSlots() && slot < getSizeInventory() && stack
				.getItem() instanceof IItemUpgradeComponent && canInsertItemUpgrade((IItemUpgradeComponent) stack.getItem(), stack);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (openSides[side.ordinal()] == 0) return new int[0];

		int[] ret = new int[getSizeInventory() - getSizeUpgradeSlots() - filterMaxSize];

		for (int i = 0; i < ret.length; i++) {
			ret[i] = i;
		}

		return ret;
	}

	@Override
	protected void importContents() {
		if (!worldObj.isRemote) {
			if (worldObj.getTotalWorldTime() % 20L == 0) {
				// ProjectZed.logHelper.info("energyBurnRate:", energyBurnRate);

				isSilkTouch = false;
				waitTime = originalWaitTime;
				energyBurnRate = originalEnergyBurnRate;

				if (!isDone()) {

					ItemStack[] upgrades = getCurrentUpgrades();
					if (upgrades.length > 0) {

						float max = Float.MIN_VALUE;
						IItemUpgradeComponent comp;

						for (ItemStack stack : upgrades) {
							if (stack != null && stack.stackSize > 0) {
								comp = ((IItemUpgradeComponent) stack.getItem());
								if (!comp.effectOnDiggers(this, true)) continue;
								else {
									for (int i = 0; i < Math.min(stack.stackSize, comp.maxSize()); i++) {
										comp.effectOnDiggers(this, false);
									}
								}

								// if (!isSilkTouch && comp instanceof ItemUpgradeSilkTouch) isSilkTouch = true;

								max = Math.max(max, comp.energyBurnRateRelativeToSize(stack.stackSize, originalEnergyBurnRate));
							}
						}

						energyBurnRate = (int) Math.max(Math.ceil(max), originalEnergyBurnRate);
					}
				}

			}
		}
	}

	@Override
	protected void exportContents() {
		if (!worldObj.isRemote) {
			
			// everything that updates every tick:
			if (!this.isDone && isActiveFromRedstoneSignal() && this.storedPower - this.energyBurnRate >= 0) {
				doQuarryWork();
			}

			// everything that gets called once per second.
			if (worldObj.getTotalWorldTime() % 20L == 0) {
				handleItemSidedIO();
			}
		}

		// TODO: re-implement particles for 1.9!
		/*if (quarryRect != null && currentMineVec != null) {
			worldObj.spawnParticle("smoke", currentMineVec.x + 0.5d, currentMineVec.y + 0.5d, currentMineVec.z + 0.5d, 0d, 0d, 0d);
			worldObj.spawnParticle("flame", currentMineVec.x + 0.5d, currentMineVec.y + 0.5d, currentMineVec.z + 0.5d, 0d, 0d, 0d);
		}*/
	}
	
	/**
	 * Method to handle quarry work.
	 */
	protected void doQuarryWork() {
		// if (currentMineVec != null) ProjectZed.logHelper.info(currentMineVec.toString());
		// if (quarryRect != null) ProjectZed.logHelper.info(quarryRect.getArea());
		
		if (quarryRect == null || quarryRect.getArea() < 25d) {

			Block currentBlock;
			Vector2<Integer>[] markerCons;
			Vector3<Integer> currentVec = new Vector3<Integer>();
			for (int i = 2; i < EnumFacing.VALUES.length; i++) {
				currentVec.x = worldVec().x + EnumFacing.getFront(i).getFrontOffsetX();
				currentVec.y = worldVec().y;
				currentVec.z = worldVec().z + EnumFacing.getFront(i).getFrontOffsetZ();

				// currentBlock = worldObj.getBlock(currentVec.x, currentVec.y, currentVec.z);
				currentBlock = BlockUtils.getBlock(worldObj, currentVec).getBlock();

				if (currentBlock != null && currentBlock == ProjectZed.quarryMarker) {
					markerCons = ((BlockQuarryMarker) currentBlock).getBounds(worldObj, currentVec);
					if (markerCons != null && markerCons.length == 4) {
						markerCons[0].x++;
						markerCons[0].y++;
						markerCons[3].x--;
						markerCons[3].y--;
						
						quarryRect = new Rect<Integer>(markerCons[0], markerCons[3]);
						return;
					}
				}
			}
		}
		
		else {
			if (currentTickTime > 0) currentTickTime--;
			else {
				if (currentMineVec == null) currentMineVec = new Vector3<Integer>(quarryRect.min.x, pos.getY() - 1, quarryRect.min.y);

				if (worldObj.getTileEntity(VectorHelper.toBlockPos(currentMineVec)) != null) {
					incrementMineVec();
					return;
				}

				final BlockPos currentMinePos = VectorHelper.toBlockPos(currentMineVec);
				IBlockState currentBlock = BlockUtils.getBlock(worldObj, currentMineVec);
				int metaData = BlockUtils.getBlockMetadata(worldObj, currentMineVec);

				// ProjectZed.logHelper.info("current mat:", currentBlock.getMaterial(), blackListContains(currentBlock.getMaterial(), true));
				if (currentBlock != ProjectZed.quarryMarker && !blackListContains(currentBlock.getMaterial(), true) &&
						currentBlock.getBlockHardness(worldObj, currentMinePos) > 0f) {
					List<ItemStack> dropsList;

					if (!isSilkTouch) dropsList = currentBlock.getBlock().getDrops(worldObj, currentMinePos, currentBlock, 0);
					else {
						dropsList = new ArrayList<ItemStack>(1);
						dropsList.add(new ItemStack(currentBlock.getBlock(), 1));
					}

					if (dropsList != null && !dropsList.isEmpty()) {
	
						boolean result = false;
						for (int i = 0; i < dropsList.size(); i++) {
							if (dropsList.get(i) == null || dropsList.get(i).getItem() == null || dropsList.get(i).stackSize == 0) return;
							result = this.addItemStackToSlots(dropsList.get(i), true);
							if (!result) {
								ProjectZed.logHelper.info("I returned false!");
								return;
							}
						}
						
						if (result) {
							for (int i = 0; i < dropsList.size(); i++) {
								this.addItemStackToSlots(dropsList.get(i), false);
							}
							
							BlockUtils.setBlockToAir(worldObj, currentMineVec);
						}
					}
				}

				incrementMineVec();
				
				// reset tick timer:
				currentTickTime = waitTime;
			}
		}
	}
	
	/**
	 * Method to increment quarry mining vector.
	 */
	protected void incrementMineVec() {
		if (currentMineVec != null && quarryRect != null) {
			this.storedPower -= this.energyBurnRate;
			
			if (currentMineVec.x < quarryRect.max.x) {
				currentMineVec.x++;
			}
			
			else /*if (currentMineVec.x == quarryRect.max.x)*/ {
				currentMineVec.x = quarryRect.min.x;
				
				if (currentMineVec.z < quarryRect.max.y) {
					currentMineVec.z++;
					// currentMineVec.x = quarryRect.min.x;
				}
				
				else /*if (currentMineVec.z == quarryRect.max.y)*/ {
					// currentMineVec.x = quarryRect.min.x;
					currentMineVec.z = quarryRect.min.y;
					
					if (currentMineVec.y > 1) currentMineVec.y--;
					else this.isDone = true;
				}
			}
		}
	}

	@Override
	public ItemStack[] getItemFilter() {
		return filterStacks.toArray(new ItemStack[getItemFilterSize()]);
	}
	
	@Override
	public int getItemFilterSize() {
		return filterStacks.size();
	}

	@Override
	public void addToItemFilter(ItemStack stack) {
		if (filterStacks.size() < filterMaxSize && !filterContainsItemStack(stack, false)) filterStacks.add(stack);
	}

	@Override
	public void removeFromItemFilter(ItemStack stack) {
		if (!filterStacks.isEmpty() && filterContainsItemStack(stack, false)) filterStacks.remove(stack);
	}

	@Override
	public boolean filterContainsItemStack(ItemStack stack) {
		return filterContainsItemStack(stack, false);
	}

	@Override
	public boolean filterContainsItemStack(ItemStack stack, boolean stackSizeSp) {
		if (filterStacks.isEmpty()) return false;
		
		for (int i = 0; i < filterStacks.size(); i++) {
			if (!stackSizeSp && filterStacks.get(i).isItemEqual(stack)) return true;
			else if (stackSizeSp && ItemStack.areItemStacksEqual(filterStacks.get(i), stack)) return true;
		}
			
		return false;
	}

	@Override
	public EnumFilterType getItemFilterType() {
		return this.itemFilterType;
	}
	
	@Override
	public void setItemFilterType(EnumFilterType type) {
		if (type == EnumFilterType.BLACKLIST || type == EnumFilterType.WHITELIST) this.itemFilterType = type;
	}

}

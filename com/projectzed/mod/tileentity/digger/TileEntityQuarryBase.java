/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.digger;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.hockeyhurd.api.math.Rect;
import com.hockeyhurd.api.math.Vector2;
import com.hockeyhurd.api.math.Vector3;
import com.hockeyhurd.api.util.BlockHelper;
import com.projectzed.api.tileentity.digger.AbstractTileEntityDigger;
import com.projectzed.api.util.EnumFilterType;
import com.projectzed.api.util.IItemFilterComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.block.BlockQuarryMarker;

/**
 * Base class for all quarries.
 * 
 * @author hockeyhurd
 * @version Jun 18, 2015
 */
public class TileEntityQuarryBase extends AbstractTileEntityDigger implements IItemFilterComponent {

	protected List<ItemStack> filterStacks;
	protected int filterMaxSize = 3 * 3;
	protected EnumFilterType itemFilterType;
	
	/**
	 * @param name name of quarry as required by parent class.
	 */
	public TileEntityQuarryBase(String name) {
		super(name);
		
		this.energyBurnRate = 512;
				
		this.filterStacks = new ArrayList<ItemStack>(filterMaxSize);
		this.itemFilterType = EnumFilterType.WHITELIST;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.digger.AbstractTileEntityDigger#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return 2 * 9 + filterMaxSize;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.digger.AbstractTileEntityDigger#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
		this.slots = new ItemStack[getSizeInventory()];
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.digger.AbstractTileEntityDigger#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.digger.AbstractTileEntityDigger#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] { 0, 1, 2, 3, 4, 5 };
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.digger.AbstractTileEntityDigger#importContents()
	 */
	@Override
	protected void importContents() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.digger.AbstractTileEntityDigger#exportContents()
	 */
	@Override
	protected void exportContents() {
		if (!worldObj.isRemote) {
			
			// everything that updates every tick:
			if (!this.isDone && isActiveFromRedstoneSignal() && this.storedPower - this.energyBurnRate >= 0) doQuarryWork();
			
			// everything that gets called once per second.
			if (worldObj.getTotalWorldTime() % 20L == 0) {
				handleItemSidedIO();
			}
		}
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
			for (int i = 2; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
				currentVec.x = worldVec().x + ForgeDirection.getOrientation(i).offsetX;
				currentVec.y = worldVec().y;
				currentVec.z = worldVec().z + ForgeDirection.getOrientation(i).offsetZ;
				
				currentBlock = worldObj.getBlock(currentVec.x, currentVec.y, currentVec.z);
				
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
				if (currentMineVec == null) currentMineVec = new Vector3<Integer>(quarryRect.min.x, this.yCoord - 1, quarryRect.min.y);
				if (bh == null) bh = new BlockHelper(worldObj);
				
				if (worldObj.getTileEntity(currentMineVec.x, currentMineVec.y, currentMineVec.z) != null) {
					incrementMineVec();
					return;
				}
				
				Block currentBlock = bh.getBlock(currentMineVec);
				int metaData = bh.getBlockMetaData(currentMineVec);
				
				if (currentBlock != ProjectZed.quarryMarker && currentBlock.getBlockHardness(worldObj, currentMineVec.x, currentMineVec.y, currentMineVec.z) != -1f) {
					List<ItemStack> dropsList = currentBlock.getDrops(worldObj, currentMineVec.x, currentMineVec.y, currentMineVec.z, metaData, 0);

					if (dropsList != null && !dropsList.isEmpty()) {
	
						boolean result = false;
						for (int i = 0; i < dropsList.size(); i++) {
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
							
							bh.setBlockToAir(currentMineVec);
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

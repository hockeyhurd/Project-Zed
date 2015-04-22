/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;
import com.projectzed.mod.ProjectZed;

/**
 * Class containing tileentity code for industrialLoader.
 * 
 * @author hockeyhurd
 * @version Apr 19, 2015
 */
public class TileEntityIndustrialLoader extends AbstractTileEntityMachine {

	public Ticket heldChunk;
	
	public TileEntityIndustrialLoader() {
		super("industrialLoader");
		this.energyBurnRate = 0x200; 
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
		// 4 upgrades, 9 container
		// this.slots = new ItemStack[4 + 9];
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		// TODO: Add stuffs for uprades here.
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return slot == 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#canSmelt()
	 */
	@Override
	protected boolean canSmelt() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#smeltItem()
	 */
	@Override
	public void smeltItem() {
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.machine.AbstractTileEntityMachine#getSound()
	 */
	@Override
	public Sound getSound() {
		return null;
	}
	
	// DO LOADING HERE:
	
	@Override
	public void invalidate() {
		forceChunkLoading(null);
		super.invalidate();
	}
	
	@Override
	public void validate() {
		forceChunkLoading(null);
		super.validate();
	}
	
	public void forceChunkLoading(Ticket ticket) {
		if (this.worldObj.isRemote) return;
		
		if (ticket != null) {
			this.heldChunk = ticket;
			ForgeChunkManager.forceChunk(this.heldChunk, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
		}
		
		else {
			if (this.heldChunk == null) {
				Ticket newTicket = ForgeChunkManager.requestTicket(ProjectZed.instance, this.worldObj, Type.NORMAL);
				newTicket.getModData().setInteger("xCoord", this.xCoord);
				newTicket.getModData().setInteger("yCoord", this.yCoord);
				newTicket.getModData().setInteger("zCoord", this.zCoord);
				
				this.heldChunk = newTicket;
				
				ForgeChunkManager.forceChunk(this.heldChunk, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
			}
			
			else {
				ForgeChunkManager.releaseTicket(this.heldChunk);
				this.heldChunk = null;
			}
		}
	}

}

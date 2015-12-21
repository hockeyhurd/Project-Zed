/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.generator;

import com.projectzed.api.energy.source.EnumType;
import com.projectzed.api.energy.source.Source;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.block.generator.BlockSolarArray;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Class used for creating a new tile entity in the form of a solar array.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public class TileEntitySolarArray extends AbstractTileEntityGenerator {

	private byte tier = 0;
	private final float[] TIER_MODIFIER = new float[] {
		1.0f, 8.0f, 64.0f, 512.0f	 
	};

	private static final String invName = "solarArray";
	
	public TileEntitySolarArray() {
		super(invName);
	}
	
	/**
	 * Updates tier to said value.
	 * 
	 * @param tier = tier to set.
	 */
	public void setTier(byte tier) {
		this.tier = tier >= 0 && tier < this.TIER_MODIFIER.length ? tier : 0;
		this.source.setModifier(this.TIER_MODIFIER[this.tier]);
		if (tier > 0) this.setCustomName("container." + invName + tier); // TODO: Fix this temp fix!
	}
	
	/**
	 * @return tier to get.
	 */
	public byte getTier() {
		return this.tier;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#getSizeInventory()
	 */
	public int getSizeInventory() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#getInventoryStackLimit()
	 */
	public int getInventoryStackLimit() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#initContentsArray()
	 */
	protected void initContentsArray() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#initSlotsArray()
	 */
	protected void initSlotsArray() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#getAccessibleSlotsFromSide(int)
	 */
	public int[] getAccessibleSlotsFromSide(int side) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#defineSource()
	 */
	public void defineSource() {
		this.source = new Source(EnumType.SOLAR);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#updateEntity()
	 */
	public void updateEntity() {
		if (this.worldObj != null && !this.worldObj.isRemote /*&& this.worldObj.getTotalWorldTime() % 20L == 0L*/) {
			if (this.getBlockType() instanceof BlockSolarArray) {
				BlockSolarArray b = (BlockSolarArray) this.getBlockType();
				boolean clear = b.canSeeAbove(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
				this.powerMode = worldObj.isDaytime() && clear;
				
				if (this.source.getEffectiveSize() != this.TIER_MODIFIER[this.tier] * EnumType.SOLAR.getSize()) this.source.setModifier(this.TIER_MODIFIER[this.tier]);
				
				PacketHandler.INSTANCE.sendToAll(new MessageTileEntityGenerator(this));
			}
		}
		
		super.updateEntity();
	}
	
	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		byte tier = comp.getByte("ProjectZedSolarArrayTier");
		this.tier = tier >= 0 && tier < this.TIER_MODIFIER.length ? tier : 0;
	}
	
	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
		comp.setByte("ProjectZedSolarArrayTier", this.tier);
	}

}

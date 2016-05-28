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
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

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
	 * @param tier tier to set.
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

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	@Override
	protected void initContentsArray() {
	}

	@Override
	protected void initSlotsArray() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return null;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return false;
	}

	@Override
	public void defineSource() {
		this.source = new Source(EnumType.SOLAR);
	}

	@Override
	public void update() {
		if (this.worldObj != null && !this.worldObj.isRemote /*&& this.worldObj.getTotalWorldTime() % 20L == 0L*/) {
			if (this.getBlockType() instanceof BlockSolarArray) {
				BlockSolarArray b = (BlockSolarArray) this.getBlockType();
				boolean clear = b.canSeeAbove(this.worldObj, pos);
				this.powerMode = worldObj.isDaytime() && clear;
				
				if (this.source.getEffectiveSize() != this.TIER_MODIFIER[this.tier] * EnumType.SOLAR.getSize()) this.source.setModifier(this.TIER_MODIFIER[this.tier]);
				
				PacketHandler.INSTANCE.sendToAll(new MessageTileEntityGenerator(this));
			}
		}
		
		super.update();
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

	@Override
	public EnumFacing getRotatedState(EnumFacing facingDir, IBlockState blockState) {
		return frontFacing;
	}

	@Override
	public void setFrontFacing(EnumFacing facing) {
	}

	@Override
	public boolean canRotateTE() {
		return false;
	}

}

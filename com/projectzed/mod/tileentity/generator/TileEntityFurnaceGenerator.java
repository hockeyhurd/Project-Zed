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
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Class containing te code for furnace generator.
 * 
 * @author hockeyhurd
 * @version Nov 18, 2014
 */
public class TileEntityFurnaceGenerator extends AbstractTileEntityGenerator {

	private int burnTime = 0;

	public TileEntityFurnaceGenerator() {
		super("furnaceGen");
		this.slots = new ItemStack[1];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return this.slots.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
		this.slots = new ItemStack[1];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] {
			0
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return this.isItemValidForSlot(slot, stack);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#defineSource()
	 */
	@Override
	public void defineSource() {
		this.source = new Source(EnumType.BURNABLE);
	}

	protected static int getItemBurnTime(ItemStack stack) {
		if (stack == null) return 0;
		else {
			Item item = stack.getItem();

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
				Block block = Block.getBlockFromItem(item);

				if (block == Blocks.wooden_slab) return 150 / 4;

				if (block.getMaterial() == Material.wood) return 300 / 4;

				if (block == Blocks.coal_block) return 16000 / 4;
			}

			if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) return 200 / 4;
			if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) return 200 / 4;
			if (item instanceof ItemHoe && ((ItemHoe) item).getToolMaterialName().equals("WOOD")) return 200 / 4;
			if (item == Items.stick) return 100 / 4;
			if (item == Items.coal) return 1600 / 4;
			if (item == Items.lava_bucket) return 20000 / 4;
			if (item == Item.getItemFromBlock(Blocks.sapling)) return 100 / 4;
			if (item == Items.blaze_rod) return 2400 / 4;
			return GameRegistry.getFuelValue(stack) / 4;
		}
	}

	protected boolean isFuel() {
		return getItemBurnTime(this.slots[0]) > 0;
	}

	protected void consumeFuel() {
		if (this.isFuel()) {
			if (this.slots[0] == null) return;
			else {
				if (this.slots[0].getItem() instanceof ItemBucket) this.slots[0] = new ItemStack(Items.bucket, 1);
				else this.slots[0].stackSize--;
			}
			if (this.slots[0].stackSize <= 0) this.slots[0] = (ItemStack) null;
		}
	}

	@Override
	public void generatePower() {
		if (canProducePower() && this.stored + this.source.getEffectiveSize() <= this.maxStored) this.stored += this.source.getEffectiveSize();
		if (this.stored > this.maxStored) this.stored = this.maxStored; // Redundancy check.
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (this.worldObj != null && !this.worldObj.isRemote) {
			if (this.slots[0] != null && isFuel()) {
				if (this.burnTime == 0) {
					this.burnTime = getItemBurnTime(this.slots[0]) + 1;
					if (this.stored < this.maxStored) consumeFuel();
				}
			}

			if (this.burnTime > 0) this.burnTime--;

			this.powerMode = this.burnTime > 0 && this.stored < this.maxStored;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		int time = comp.getInteger("ProjectZedBurnTime");
		this.burnTime = time > 0 ? time : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		comp.setInteger("ProjectZedBurnTime", this.burnTime);
	}

}

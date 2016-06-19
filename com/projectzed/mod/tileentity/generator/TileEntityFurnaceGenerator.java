/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.generator;

import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.energy.source.EnumType;
import com.projectzed.api.energy.source.Source;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

	@Override
	public int getSizeInventory() {
		return this.slots.length;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	protected void initContentsArray() {
		this.slots = new ItemStack[1];
	}

	@Override
	protected void initSlotsArray() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] {
			0
		};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return this.isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return false;
	}

	@Override
	public void defineSource() {
		this.source = new Source(EnumType.BURNABLE);
	}

	protected static int getItemBurnTime(ItemStack stack, World world, BlockPos blockPos) {
		if (stack == null) return 0;
		else {
			Item item = stack.getItem();

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR) {
				Block block = Block.getBlockFromItem(item);

				if (block == Blocks.WOODEN_SLAB) return 150 / 4;

				if (BlockUtils.getBlockMaterial(world, blockPos) == Material.WOOD) return 300 / 4;

				if (block == Blocks.COAL_BLOCK) return 16000 / 4;
			}

			if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) return 200 / 4;
			if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) return 200 / 4;
			if (item instanceof ItemHoe && ((ItemHoe) item).getMaterialName().equals("WOOD")) return 200 / 4;
			if (item == Items.STICK) return 100 / 4;
			if (item == Items.COAL) return 1600 / 4;
			if (item == Items.LAVA_BUCKET) return 20000 / 4;
			if (item == Item.getItemFromBlock(Blocks.SAPLING)) return 100 / 4;
			if (item == Items.BLAZE_ROD) return 2400 / 4;
			return GameRegistry.getFuelValue(stack) / 4;
		}
	}

	protected boolean isFuel() {
		return getItemBurnTime(this.slots[0], worldObj, pos) > 0;
	}

	protected void consumeFuel() {
		if (this.isFuel()) {
			if (this.slots[0] == null) return;
			else {
				if (this.slots[0].getItem() instanceof ItemBucket) this.slots[0] = new ItemStack(Items.BUCKET, 1);
				else this.slots[0].stackSize--;
			}

			if (this.slots[0].stackSize <= 0) this.slots[0] = null;
		}
	}

	@Override
	public void generatePower() {
		if (canProducePower() && this.stored + this.source.getEffectiveSize() <= this.maxStored) this.stored += this.source.getEffectiveSize();
		if (this.stored > this.maxStored) this.stored = this.maxStored; // Redundancy check.
	}

	@Override
	public void update() {
		super.update();

		if (this.worldObj != null && !this.worldObj.isRemote) {

			if (getEnergyStored() + source.getEffectiveSize() <= maxStored) {
				if (this.slots[0] != null && isFuel()) {
					if (this.burnTime == 0) {
						this.burnTime = getItemBurnTime(this.slots[0], worldObj, pos) + 1;
						/*if (this.stored < this.maxStored) */consumeFuel();
					}
				}

				if (this.burnTime > 0) this.burnTime--;
			}

			this.powerMode = this.burnTime > 0 && this.stored < this.maxStored;

			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityGenerator(this));
		}
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		int time = comp.getInteger("ProjectZedBurnTime");
		this.burnTime = time > 0 ? time : 0;
	}

	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
		comp.setInteger("ProjectZedBurnTime", this.burnTime);
	}

}

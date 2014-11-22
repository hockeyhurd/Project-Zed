package com.projectzed.mod.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.projectzed.api.tileentity.AbstractTileEntityGeneric;

/**
 * TileEntity code for Fabrication Table.
 * 
 * @author hockeyhurd
 * @version Nov 22, 2014
 */
public class TileEntityFabricationTable extends AbstractTileEntityGeneric {

	private ItemStack[] craftingMatrix;
	
	public TileEntityFabricationTable() {
		super();
		this.customName = "fabricationTable";
		this.slots = new ItemStack[10 + 4 * 9 + 6 * 12];
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	public int getSizeInventory() {
		return this.slots.length;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initContentsArray()
	 */
	protected void initContentsArray() {
		this.slots = new ItemStack[10 + 10 * 9];
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initSlotsArray()
	 */
	protected void initSlotsArray() {
		this.craftingMatrix = new ItemStack[9];
	}
	
	/**
	 * Getter for the crafting matix.
	 * @return crafting matix itemstack array.
	 */
	public ItemStack[] getCraftingMatrix() {
		return this.craftingMatrix;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#setCustomName(java.lang.String)
	 */
	public void setCustomName(String name) {
		this.customName = name;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot != 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getAccessibleSlotsFromSide(int)
	 */
	public int[] getAccessibleSlotsFromSide(int side) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		NBTTagList tagList = comp.getTagList("Items", 10);
		this.slots = new ItemStack[this.getSizeInvenotry()];

		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound temp = tagList.getCompoundTagAt(i);
			int b0 = temp.getByte("Slot") & 255;

			if (b0 >= 0 && b0 < this.slots.length) this.slots[b0] = ItemStack.loadItemStackFromNBT(temp);
			System.out.println("Loading:\t" + this.slots[b0].getItem().getUnlocalizedName());
		}

		if (comp.hasKey("CustomName", 8)) this.customName = comp.getString("CustomName");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		NBTTagList tagList = comp.getTagList("Items", 10);

		for (int i = 0; i < this.slots.length; ++i) {
			if (this.slots[i] != null) {
				NBTTagCompound temp = new NBTTagCompound();
				comp.setByte("Slot", (byte) i);
				this.slots[i].writeToNBT(temp);
				tagList.appendTag(temp);
			}
		}

		comp.setTag("Items", tagList);

		if (this.hasCustomInventoryName()) comp.setString("CustomName", this.customName);
	}

}

package com.projectzed.mod.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;

import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.container.ContainerFabricationTable;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityFabricationTable;

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
		this.slots = new ItemStack[10 + 6 * 12 + 4 * 9];
		this.craftingMatrix = new ItemStack[3 * 3];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	public int getSizeInventory() {
		return this.slots.length;
	}

	public void setStackInSlot(ItemStack stack, int slot) {
		if (slot >= 0 && slot < this.slots.length) this.slots[slot] = stack;
		else ProjectZed.logHelper.warn("Error! Please check you are placing in the correct slot index!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initContentsArray()
	 */
	protected void initContentsArray() {
		this.slots = new ItemStack[10 + 10 * 9];
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#setCustomName(java.lang.String)
	 */
	public void setCustomName(String name) {
		this.customName = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getAccessibleSlotsFromSide(int)
	 */
	public int[] getAccessibleSlotsFromSide(int side) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		NBTTagList tagList = comp.getTagList("Items", 10);
		this.slots = new ItemStack[this.getSizeInvenotry()];

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound temp = tagList.getCompoundTagAt(i);
			byte b0 = temp.getByte("Slot");

			if (b0 >= 0 && b0 < this.slots.length) this.slots[b0] = ItemStack.loadItemStackFromNBT(temp);
		}

		if (comp.hasKey("CustomName", 8)) this.customName = comp.getString("CustomName");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		NBTTagList tagList = comp.getTagList("Items", 10);

		for (int i = 0; i < this.slots.length; i++) {
			if (this.slots[i] != null) {
				NBTTagCompound temp = new NBTTagCompound();
				temp.setByte("Slot", (byte) i);
				this.slots[i].writeToNBT(temp);
				tagList.appendTag(temp);
			}
		}

		comp.setTag("Items", tagList);

		if (this.hasCustomInventoryName()) comp.setString("CustomName", this.customName);
	}

	public void clearCraftingGrid(ContainerFabricationTable cont, EntityPlayer player) {
		// cont.clearCraftingMatrix(player);
		System.err.println("CLEAR BUTTON HIT!!!");
	}
	
	@Override
	public void updateEntity() {
		if (this.worldObj.getTotalWorldTime() % 20L == 0) {
			for (int i = 0; i < 10; i++) {
				ItemStack stack = this.slots[i];
				if (stack != null) System.out.println(i + ", " + stack);
			}
		}
	}

	/**
	 * Handles moving an itemstack from one slot to another.
	 * @param stack = stack to move.
	 * @param currentSlot = current slot of stack to move.
	 * @param min = min slot index to move to.
	 * @param max = max slot index to move to.
	 */
	private void moveStack(ItemStack stack, int currentSlot, int min, int max) {
		boolean handled = false;

		// Check to see if we can add this stack to a current stack.
		for (int i = min; i < max; i++) {
			ItemStack currentStack = this.slots[i];
			if (currentStack == stack && currentStack.stackSize + stack.stackSize <= stack.getMaxStackSize()) {
				currentStack.stackSize += stack.stackSize;
				this.slots[i] = currentStack;
				this.slots[currentSlot] = (ItemStack) null;
				handled = true;
				break;
			}
		}

		// If already handled, no need to continue.
		if (handled) return;

		// Else, search for an empty slot to move this stack to.
		else {
			for (int i = min; i < max; i++) {
				if (this.slots[i] == null) {
					this.slots[i] = stack;
					this.slots[currentSlot] = (ItemStack) null;
					handled = true;
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.tileentity.TileEntity#getDescriptionPacket()
	 */
	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityFabricationTable(this));
	}

	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityFabricationTable(this));
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.tileentity.IMultiBlockable;
import com.projectzed.api.tileentity.IWrenchable;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.block.container.BlockNuclearIOPort;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityNuclearIOPort;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class containing tileentity code for nuclearIOPort.
 * 
 * @author hockeyhurd
 * @version Mar 19, 2015
 */
public class TileEntityNuclearIOPort extends AbstractTileEntityNuclearComponent implements IWrenchable {

	// TODO: This class should take over IO of TileEntityNuclearController. For now this TE is set to optional use.
	
	private byte meta;
	private int burnTime;
	
	public TileEntityNuclearIOPort() {
		super("nuclearIOPort");
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getInventoryName()
	 */
	@Override
	public String getInventoryName() {
		return "container.nuclearIOPort";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
		this.slots = new ItemStack[2];
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return this.slots.length;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#canUpdate()
	 */
	@Override
	public boolean canUpdate() {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#updateEntity()
	 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0) {
			byte current = (byte) this.blockMetadata;
			if (this.meta != current) setMetaOnUpdate(current);

			PacketHandler.INSTANCE.sendToAllAround(new MessageTileEntityNuclearIOPort(this),
					new TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 0x10));
		}

		else if (worldObj.isRemote && burnTime > 0) {
			burnTime--; // On client side, we can predict that burn time will be decreased each tick, and will be corrected via messages 1/sec.
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] { 0, 1 };
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot != 0 || stack == null) return false;
		return stack.getItem() == ProjectZed.fullFuelRod || stack.getItemDamage() < stack.getMaxDamage();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return this.getBlockMetadata() == 1 && isItemValidForSlot(slot, stack);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return /*this.getBlockMetadata() == 2 &&*/ slot == 1;
	}
	
	/**
	 * Gets the item burn time for stack.
	 * 
	 * @param stack stack to test.
	 * @return item burn time.
	 */
	public static int getItemBurnTime(ItemStack stack) {
		if (stack == null) return 0;
		else {
			Item item = stack.getItem();

			if (item == ProjectZed.fullFuelRod && stack.getItemDamage() < stack.getMaxDamage()) return 1600;
			
			return 0;
		}
	}
	
	/**
	 * Function used to determine if item in slot is fuel.
	 * 
	 * @return true if is fuel, else returns false.
	 */
	public boolean isFuel() {
		return getItemBurnTime(this.slots[0]) > 0;
	}
	
	/**
	 * Method used to consume fuel in given slot.
	 *
	 * @param ouputPort port to send empty fuel cell to if applicable.
	 */
	public void consumeFuel(TileEntityNuclearIOPort ouputPort) {
		if (this.isFuel()) {
			if (this.slots[0] == null) return;
			else {
				ItemStack stack = this.slots[0];
				if (stack.getItemDamage() < stack.getMaxDamage() - 1) {
					stack.setItemDamage(stack.getItemDamage() + 1);
					this.slots[0] = stack;
				}
				
				else {
					this.slots[0] = null;

					if (ouputPort == null) this.slots[1] = new ItemStack(ProjectZed.emptyFuelRod, 1, 0);
					else ouputPort.setInventorySlotContents(1, new ItemStack(ProjectZed.emptyFuelRod, 1, 0));
				}
			}
		}
	}

	/**
	 * Function to run a cycle of consuming fuel and setting/resetting applicable data values.
	 *
	 * @param ouputPort port to send resulting data to if applicable.
	 * @return true if successful, else returns false.
	 */
	public boolean runCycle(TileEntityNuclearIOPort ouputPort) {
		boolean flag = false;
		
		if (this.slots[0] != null && isFuel()) {
			if (this.burnTime == 0) {
				this.burnTime = getItemBurnTime(this.slots[0]);
				consumeFuel(ouputPort);
				flag = true;
			}
		}
		
		return flag;
	}
	
	public void tickBurnTime() {
		if (this.burnTime > 0) this.burnTime--;
	}
	
	/**
	 * Gets the current burn time for instance.
	 * 
	 * @return burn time as int value.
	 */
	public int getBurnTime() {
		return this.burnTime;
	}

	/**
	 * Setter method used for syncing with client side.
	 *
	 * @param burnTime Int burn time to set.
	 */
	@SideOnly(Side.CLIENT)
	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}
	
	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		
		this.meta = comp.getByte("ProjectZedNuclearIOPortMeta");
		this.burnTime = comp.getInteger("ProjectZedNuclearBurnTime");
	}
	
	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);

		comp.setByte("ProjectZedNuclearIOPortMeta", this.meta);
		comp.setInteger("ProjectZedNuclearBurnTime", this.burnTime);
	}
	
	/**
	 * @return the correct meta data for when multiblock structure is 'active'
	 */
	public byte getStoredMeta() {
		return this.meta;
	}
	
	/**
	 * Sets stored meta data as byte.
	 * 
	 * @param meta new meta data value.
	 */
	public void setMetaOnUpdate(byte meta) {
		if (meta > 0) this.meta = meta;
	}
	
	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#reset()
	 */
	@Override
	public void reset() {
		this.isMaster = false;
		this.hasMaster = false;
		this.masterVec = Vector3.zero.getVector3i();
		
		setMetaOnUpdate((byte) worldObj.getBlockMetadata(worldVec().x, worldVec().y, worldVec().z));
		((BlockNuclearIOPort) worldObj.getBlock(worldVec().x, worldVec().y, worldVec().z)).updateMeta(false, worldObj, worldVec());
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isUnique()
	 */
	@Override
	public boolean isUnique() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isSubstituable()
	 */
	@Override
	public boolean isSubstituable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getSubList()
	 */
	@Override
	public List<IMultiBlockable> getSubList() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getAmountFromSize(int, int, int)
	 */
	@Override
	public int getAmountFromSize(int width, int height, int depth) {
		return 2;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getBlock()
	 */
	@Override
	public Block getBlock() {
		return ProjectZed.nuclearIOPort;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#getRotationMatrix()
	 */
	@Override
	public byte getRotatedMeta(byte facingDir, byte currentMeta) {
		return (byte) (currentMeta == 1 ? 2 : 1);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#canRotateTE()
	 */
	@Override
	public boolean canRotateTE() {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#onInteract(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
	 */
	@Override
	public void onInteract(ItemStack stack, EntityPlayer player, World world, Vector3<Integer> vec) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#canSaveDataOnPickup()
	 */
	@Override
	public boolean canSaveDataOnPickup() {
		return true;
	}

}

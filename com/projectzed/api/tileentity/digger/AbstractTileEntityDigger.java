/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity.digger;

import com.hockeyhurd.api.math.Rect;
import com.hockeyhurd.api.math.Vector2;
import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.energy.machine.IEnergyMachine;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.item.IItemUpgradeComponent;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.api.util.EnumFrameType;
import com.projectzed.api.util.EnumRedstoneType;
import com.projectzed.api.util.IRedstoneComponent;
import com.projectzed.api.util.IUpgradeComponent;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityDigger;
import com.projectzed.mod.util.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for creating 'digger' machines (quarries, pumps, etc., .. ).
 * 
 * @author hockeyhurd
 * @version Jun 18, 2015
 */
public abstract class AbstractTileEntityDigger extends AbstractTileEntityEnergyContainer implements IEnergyMachine, IModularFrame, IRedstoneComponent,
		IUpgradeComponent {

	protected int originalEnergyBurnRate;
	protected int energyBurnRate;
	protected int waitTime = 20;
	protected final int originalWaitTime = waitTime;
	protected int currentTickTime;
	protected boolean isDone;
	protected boolean isSilkTouch = false;
	
	protected byte[] openSides = new byte[ForgeDirection.VALID_DIRECTIONS.length];
	protected EnumRedstoneType redstoneType;

	protected Rect<Integer> quarryRect;
	protected Vector3<Integer> currentMineVec;

	protected Material[] blackList;

	/**
	 * @param name name of digger machine as required by parent class.
	 */
	public AbstractTileEntityDigger(String name) {
		super(name);
		this.maxPowerStorage = (int) 1e6;
		this.originalEnergyBurnRate = this.energyBurnRate = 0x100; // 256

		initBlackList();
	}

	/**
	 * @return quarry rectanlge.
	 */
	public Rect<Integer> getQuarryRect() {
		return quarryRect;
	}

	/**
	 * Set this quarry rectangle to specified rectangle.
	 *
	 * @param quarryRect rectangle to set.
	 */
	public void setQuarryRect(Rect<Integer> quarryRect) {
		this.quarryRect = quarryRect;
	}

	/**
	 * @return if quarry is done.
	 */
	public boolean isDone() {
		return isDone;
	}

	/**
	 * @return copy of current mineing vector, else can return null.
	 */
	public Vector3<Integer> getCurrentMineVec() {
		return currentMineVec != null ? currentMineVec.copy() : null;
	}

	public void setCurrentMineVec(Vector3<Integer> vec) {
		this.currentMineVec = vec;
	}

	public void setSilkTouch(final boolean value) {
		this.isSilkTouch = value;
	}

	public boolean hasSilkTouch() {
		return isSilkTouch;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	public int getWaitTime() {
		return waitTime;
	}

	/**
	 * Method to ensure initialization of black listed materials.
	 */
	protected abstract void initBlackList();

	/**
	 * Adds specified material to internal black list.
	 *
	 * @param material material to add.
	 */
	public void addToBlackList(final Material material) {
		if (material != null) {
			if (blackList == null || blackList.length == 0) {
				blackList = new Material[1];
				blackList[0] = material;
			}

			else {
				Material[] clone = new Material[blackList.length];

				for (int i = 0; i < blackList.length; i++) {
					clone[i] = blackList[i];
				}

				blackList = new Material[clone.length + 1];

				for (int i = 0; i < clone.length; i++) {
					blackList[i] = clone[i];
				}

				blackList[clone.length] = material;
			}
		}
	}

	/**
	 * Checks to see if given material has been black listed.
	 *
	 * @param material material to check.
	 * @return true if blacklisted, else returns false.
	 */
	public boolean blackListContains(final Material material, final boolean checkForLiquid) {
		if (blackList != null && blackList.length > 0 && material != null) {

			if (checkForLiquid && (material.isLiquid() || material instanceof MaterialLiquid)) return true;

			for (int i = 0; i < blackList.length; i++) {
				if (blackList[i] != null) {
					if (blackList[i].equals(material)) return true;
				}
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.util.IRedstoneComponent#getRedstoneType()
	 */
	@Override
	public EnumRedstoneType getRedstoneType() {
		return redstoneType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.util.IRedstoneComponent#setRedstoneType(com.projectzed.api.util.EnumRedstoneType)
	 */
	@Override
	public void setRedstoneType(EnumRedstoneType type) {
		this.redstoneType = type;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.util.IRedstoneComponent#getRedstoneSignal()
	 */
	@Override
	public int getRedstoneSignal() {
		return worldObj.getBlockPowerInput(worldVec().x, worldVec().y, worldVec().z);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.util.IRedstoneComponent#isActiveFromRedstoneSignal()
	 */
	@Override
	public boolean isActiveFromRedstoneSignal() {
		return redstoneType == EnumRedstoneType.DISABLED ? true : redstoneType == EnumRedstoneType.LOW ? getRedstoneSignal() == 0
				: redstoneType == EnumRedstoneType.HIGH ? getRedstoneSignal() > 0 : false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IModularFrame#getType()
	 */
	@Override
	public EnumFrameType getType() {
		return EnumFrameType.ITEM;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IModularFrame#setSideValve(net.minecraftforge.common.util.ForgeDirection, byte)
	 */
	@Override
	public void setSideValve(ForgeDirection dir, byte value) {
		openSides[dir.ordinal()] = value;

		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IModularFrame#setSideValveAndRotate(net.minecraftforge.common.util.ForgeDirection)
	 */
	@Override
	public void setSideValveAndRotate(ForgeDirection dir) {
		openSides[dir.ordinal()] = (byte) (openSides[dir.ordinal()] == -1 ? 0 : (openSides[dir.ordinal()] == 0 ? 1 : -1));

		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IModularFrame#getSideValve(net.minecraftforge.common.util.ForgeDirection)
	 */
	@Override
	public byte getSideValve(ForgeDirection dir) {
		return openSides[dir.ordinal()];
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IModularFrame#getSideValve(int)
	 */
	@Override
	public byte getSideValve(int dir) {
		return openSides[dir];
	}

	@Override
	public ItemStack[] getUpgradeSlots() {
		List<ItemStack> list = new ArrayList<ItemStack>(getSizeUpgradeSlots());

		for (int i = slots.length - getSizeUpgradeSlots(); i < slots.length; i++) {
			list.add(slots[i]);
		}

		return list.toArray(new ItemStack[list.size()]);
	}

	@Override
	public ItemStack[] getCurrentUpgrades() {
		List<ItemStack> list = new ArrayList<ItemStack>(getSizeUpgradeSlots());

		ItemStack[] upgradeSlots = getUpgradeSlots();

		for (int i = 0; i < upgradeSlots.length; i++) {
			if (upgradeSlots[i] != null && upgradeSlots[i].stackSize > 0) {

				if (!list.isEmpty()) {
					boolean added = false;

					for (ItemStack stack : list) {
						if (stack.isItemEqual(upgradeSlots[i])) {
							stack.stackSize += upgradeSlots[i].stackSize;
							if (stack.stackSize > stack.getMaxStackSize()) stack.stackSize = stack.getMaxStackSize();
							added = true;
							break;
						}
					}

					if (!added) list.add(upgradeSlots[i].copy());
				}

				else list.add(upgradeSlots[i].copy());
			}
		}

		return list.toArray(new ItemStack[list.size()]);
	}

	@Override
	public int getSizeUpgradeSlots() {
		return 0x4;
	}

	@Override
	public boolean canInsertItemUpgrade(IItemUpgradeComponent component, ItemStack stack) {
		return component.effectOnDiggers(this, true) && component.maxSize() >= stack.stackSize;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IModularFrame#getSidedArray()
	 */
	@Override
	public byte[] getSidedArray() {
		return openSides;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.machine.IEnergyMachine#setPowerMode(boolean)
	 */
	@Override
	public void setPowerMode(boolean val) {
		this.powerMode = val;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.machine.IEnergyMachine#isPoweredOn()
	 */
	@Override
	public boolean isPoweredOn() {
		return this.powerMode;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.machine.IEnergyMachine#setEnergyBurnRate(int)
	 */
	@Override
	public void setEnergyBurnRate(int val) {
		this.energyBurnRate = val;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.machine.IEnergyMachine#getEnergyBurnRate()
	 */
	@Override
	public int getEnergyBurnRate() {
		return energyBurnRate;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.machine.IEnergyMachine#burnEnergy()
	 */
	@Override
	public void burnEnergy() {
		if (isPoweredOn() && currentTickTime == 0 && storedPower - energyBurnRate >= 0) storedPower -= energyBurnRate;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return slots != null ? slots.length : 0;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.slots[slot] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) stack.stackSize = this.getInventoryStackLimit();
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#initContentsArray()
	 */
	@Override
	protected abstract void initContentsArray();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public abstract boolean isItemValidForSlot(int slot, ItemStack stack);

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public abstract int[] getAccessibleSlotsFromSide(int side);

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return slot >= slots.length - getSizeUpgradeSlots() && slot < slots.length &&isItemValidForSlot(slot, stack);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return slot >= 0 && slot < slots.length - getSizeUpgradeSlots() && openSides[side] == 1;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getMaxImportRate()
	 */
	@Override
	public int getMaxImportRate() {
		return Reference.Constants.BASE_PIPE_TRANSFER_RATE * Reference.Constants.TIER3_ENERGY_PIPE_MULTIPLIER;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getMaxExportRate()
	 */
	@Override
	public int getMaxExportRate() {
		return Reference.Constants.BASE_PIPE_TRANSFER_RATE * Reference.Constants.TIER3_ENERGY_PIPE_MULTIPLIER;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#requestPower(com.projectzed.api.energy.storage.IEnergyContainer, int)
	 */
	@Override
	public int requestPower(IEnergyContainer cont, int amount) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#addPower(com.projectzed.api.energy.storage.IEnergyContainer, int)
	 */
	@Override
	public int addPower(IEnergyContainer cont, int amount) {
		if (cont != null && this.getMaxImportRate() >= amount) {
			if (this.storedPower + amount <= this.maxPowerStorage) this.storedPower += amount;
			else {
				amount = this.maxPowerStorage - this.storedPower;
				this.storedPower = this.maxPowerStorage;
			}
			
			return amount;
		}
		
		else return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#importContents()
	 */
	@Override
	protected abstract void importContents();

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#exportContents()
	 */
	@Override
	protected abstract void exportContents();
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if (!worldObj.isRemote) PacketHandler.INSTANCE.sendToAll(new MessageTileEntityDigger(this));
	}
	
	/**
	 * Method handler for pushing/pulling itemstacks to and from neighboring inventories.
	 */
	protected void handleItemSidedIO() {
		TileEntity te;
		IInventory otherInv;
		ItemStack out;
		
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			te = worldObj.getTileEntity(worldVec().x + dir.offsetX, worldVec().y + dir.offsetY, worldVec().z + dir.offsetZ);
			
			if (te != null && te instanceof IInventory) { 
				otherInv = (IInventory) te;
				final int upgradeOffset = te instanceof IUpgradeComponent ? ((IUpgradeComponent) te).getSizeUpgradeSlots() : 0;
				
				// input relative to machine.
				/*if (openSides[dir.ordinal()] == -1) {
					
					for (int otherSlot = 0; otherSlot < otherInv.getSizeInventory(); otherSlot++) {
						// stack from other inventory to pull.
						ItemStack invIn = otherInv.getStackInSlot(otherSlot);
						if (invIn == null || invIn.stackSize == 0) continue;
						
						for (int thisSlot = 0; thisSlot < this.getSizeInvenotry(); thisSlot++) {
							// itemstack currently in the 'input' slot.
							in = this.getStackInSlot(thisSlot);
							boolean hasStack = in != null && in.stackSize > 0 && in.stackSize <= in.getMaxStackSize();
							
							if (isItemValidForSlot(thisSlot, invIn) && (!hasStack || invIn.isItemEqual(in))) {
								int amount = invIn.stackSize;
								if (hasStack) amount = Math.min(in.getMaxStackSize() - in.stackSize, amount);
								
								if (hasStack) in.stackSize += amount;
								else {
									ItemStack copy = invIn.copy();
									copy.stackSize = amount;
									in = copy;
								}
								
								this.setInventorySlotContents(thisSlot, in);
								
								otherInv.decrStackSize(otherSlot, amount);
							}
						}
					}
					
				}*/
				
				// output relative to machine.
				if (openSides[dir.ordinal()] == 1) {
					for (int thisSlot = 0; thisSlot < this.getSizeInventory() - this.getSizeUpgradeSlots(); thisSlot++) {
						out = this.getStackInSlot(thisSlot);
						if (out == null || out.stackSize == 0 || !this.canExtractItem(thisSlot, out, dir.ordinal())) continue;
						
						int amount = out.stackSize;
						
						for (int otherSlot = 0; otherSlot < otherInv.getSizeInventory() - upgradeOffset; otherSlot++) {
							ItemStack destStack = otherInv.getStackInSlot(otherSlot);
							if (destStack != null && destStack.stackSize == destStack.getMaxStackSize()) continue;
							
							if (/*(destStack != null && out.isItemEqual(destStack)) ||*/ otherInv.isItemValidForSlot(otherSlot, out)) {
								
								if (destStack != null) {
									if (out.isItemEqual(destStack)) {
										amount = Math.min(destStack.getMaxStackSize() - destStack.stackSize, amount);	
										otherInv.getStackInSlot(otherSlot).stackSize += amount;
									}
									
									else continue;
								}
								
								else {
									ItemStack copy = out.copy();
									copy.stackSize = amount;
									destStack = copy;
									otherInv.setInventorySlotContents(otherSlot, destStack);
								}
								
								out.stackSize -= amount;
								if (out.stackSize == 0) out = null;
								
								this.decrStackSize(thisSlot, amount);
							}
							
							if (amount == 0 || out == null || out.stackSize == 0) break;
						}
						
						if (out == null || out.stackSize == 0) continue;
						
					}
				}
			}
		}
	}
	
	protected boolean addItemStackToSlots(ItemStack stack, boolean simulate) {
		if (stack != null && stack.stackSize > 0 && this.slots != null && this.slots.length > 0) {
			
			ItemStack copy = stack.copy();
			ItemStack current;
			
			// first pass, try merge:
			for (int i = 0; i < this.slots.length - this.getSizeUpgradeSlots(); i++) {
				current = this.slots[i];
				
				if (current != null && current.isItemEqual(copy) && current.stackSize < current.getMaxStackSize()) {
					int amount = current.getMaxStackSize() - current.stackSize;
					amount = Math.min(amount, copy.stackSize);
					// if (!simulate) current.stackSize += amount;
					if (!simulate) this.slots[i].stackSize += amount;
					copy.stackSize -= amount;
				
					if (copy == null || copy.stackSize == 0) return true;
				}
			}

			// second pass, place remaining into inventory:
			if (copy != null && copy.stackSize > 0) {
				for (int i = 0; i < this.slots.length; i++) {
					current = this.slots[i];
					
					if (current == null || current.stackSize == 0) {
						if (!simulate) this.slots[i] = copy;
						return true;
					}
				}
			}
		}
		
		return false;
	}

	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityDigger(this));
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		
		isDone = comp.getBoolean("IsQuarryDone");
		currentMineVec = Vector3.zero.getVector3i();
		
		if (comp.getBoolean("HasQuarryRect")) {
			int qX0 = comp.getInteger("QuarryMinX");
			int qY0 = comp.getInteger("QuarryMinY");
			
			int qX1 = comp.getInteger("QuarryMaxX");
			int qY1= comp.getInteger("QuarryMaxY");
			
			quarryRect = new Rect<Integer>(new Vector2<Integer>(qX0, qY0), new Vector2<Integer>(qX1, qY1));
		
			currentMineVec.x = comp.getInteger("CurrentMineVecX");
			currentMineVec.y = comp.getInteger("CurrentMineVecY");
			currentMineVec.z = comp.getInteger("CurrentMineVecZ");
		}
		
		currentTickTime = comp.getInteger("CurrentTickTime");
		
		int typeID = comp.hasKey("RedstoneType") ? comp.getInteger("RedstoneType") : 1;
		this.redstoneType = EnumRedstoneType.TYPES[typeID >= 0 && typeID < EnumRedstoneType.TYPES.length ? typeID : 1];
		
		for (int i = 0; i < this.openSides.length; i++) {
			this.openSides[i] = comp.getByte("ProjectZedDiggerSide" + i);
		}
	}
	
	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
		
		comp.setBoolean("IsQuarryDone", isDone);
		comp.setBoolean("HasQuarryRect", quarryRect != null);
		if (quarryRect != null) {
			comp.setInteger("QuarryMinX", quarryRect.min.x);
			comp.setInteger("QuarryMinY", quarryRect.min.y);
			
			comp.setInteger("QuarryMaxX", quarryRect.max.x);
			comp.setInteger("QuarryMaxY", quarryRect.max.y);
		
			if (currentMineVec == null) currentMineVec = Vector3.zero.getVector3i();
			
			comp.setInteger("CurrentMineVecX", currentMineVec.x);
			comp.setInteger("CurrentMineVecY", currentMineVec.y);
			comp.setInteger("CurrentMineVecZ", currentMineVec.z);
		}
		
		comp.setInteger("CurrentTickTime", currentTickTime);
		
		if (this.redstoneType == null) this.redstoneType = EnumRedstoneType.LOW;
		comp.setInteger("RedstoneType", this.redstoneType.ordinal());

		for (int i = 0; i < this.openSides.length; i++) {
			comp.setByte("ProjectZedDiggerSide" + i, this.openSides[i]);
		}
	}

}

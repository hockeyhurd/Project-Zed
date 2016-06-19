/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity.container;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.api.energy.EnergyNet;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.IWrenchable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Class containing generic abstractions for all containers.
 * <br>NOTE: By container, this class assumes the te will be containing
 * energy, <strike>liquids, etc</strike>; explicitly not to be confused with a chest like container.
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public abstract class AbstractTileEntityEnergyContainer extends AbstractTileEntityContainer implements IEnergyContainer, IWrenchable {

	protected EnumFacing frontFacing;

	protected int maxPowerStorage = 100000;
	protected int storedPower;
	protected boolean powerMode;
	protected int importRate, exportRate;
	protected EnumFacing lastReceivedDir;
	
	/**
	 * Init class object through parameters.
	 * @param name name of te (its custom name).
	 */
	public AbstractTileEntityEnergyContainer(String name) {
		super(name);
	}

	@Override
	public abstract int getSizeInventory();

	@Override
	public abstract int getInventoryStackLimit();

	@Override
	protected abstract void initContentsArray();

	@Override
	protected abstract void initSlotsArray();

	@Override
	public abstract boolean isItemValidForSlot(int slot, ItemStack stack);

	@Override
	public abstract int[] getSlotsForFace(EnumFacing side);

	@Override
	public abstract boolean canInsertItem(int slot, ItemStack stack, EnumFacing side);

	@Override
	public abstract boolean canExtractItem(int slot, ItemStack stack, EnumFacing side);

	@Override
	public void setMaxStorage(int max) {
		this.maxPowerStorage = max;
	}

	@Override
	public int getMaxStorage() {
		return this.maxPowerStorage;
	}

	@Override
	public void setEnergyStored(int amount) {
		this.storedPower = amount;
	}

	@Override
	public int getEnergyStored() {
		return this.storedPower;
	}

	@Override
	public abstract int getMaxImportRate();

	@Override
	public abstract int getMaxExportRate();

	@Override
	public int requestPower(IEnergyContainer cont, int amount) {
		if (cont != null && this.getMaxExportRate() >= amount) {
			if (this.storedPower - amount >= 0) this.storedPower -= amount;
			else {
				amount = this.storedPower;
				this.storedPower = 0;
			}

			return amount;
		}

		return 0;
	}

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

		return 0;
	}

	@Override
	public void setLastReceivedDirection(EnumFacing dir) {
		this.lastReceivedDir = dir;
	}
	
	@Override
	public EnumFacing getLastReceivedDirection() {
		return this.lastReceivedDir;
	}
	
	/**
	* Method to be defined controlling mechanism for importing energy only (for now).
	*/ 
	protected abstract void importContents();
	
	/**
	 * Method to be defined controlling mechanism for exporting energy only (for now).
	 */
	protected abstract void exportContents(); 
	
	/**
	 * Method used to transfer power from one te to another.
	 */
	public void transferPower() {
		if (worldObj.isRemote) return;

		if (this.storedPower >= this.maxPowerStorage) {
			this.storedPower = this.maxPowerStorage;
			return;
		}

		int xCoord = pos.getX();
		int yCoord = pos.getY();
		int zCoord = pos.getZ();

		EnergyNet.importEnergyFromNeighbors(this, worldObj, xCoord, yCoord, zCoord, lastReceivedDir);
		EnergyNet.tryClearDirectionalTraffic(this, worldObj, xCoord, yCoord, zCoord, lastReceivedDir);
	}

	/**
	 * @return if container is 'powered'.
	 */
	public boolean isPowered() {
		return powerMode;
	}

	/**
	 * Sets the powered mode.
	 * <br><bold>NOTE: </bold> This method is intended to only be used in networking!
	 *
	 * @param powered mode to set.
	 */
	public void setPowered(boolean powered) {
		this.powerMode = powered;
	}

	@Override
	public Vector3<Integer> worldVec() {
		return new Vector3<Integer>(pos.getX(), pos.getY(), pos.getZ());
	}
	
	@Override
	public void update() {
		transferPower();
		importContents();
		exportContents();
		
		// this.powerMode = this.storedPower > 0;
		this.markDirty();
		
		super.update();
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		this.powerMode = comp.getBoolean("ProjectZedPowerMode");
		int size = comp.getInteger("ProjectZedPowerStored");
		this.storedPower =  size >= 0 && size <= this.maxPowerStorage ? size : 0;
	}
	
	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
		comp.setBoolean("ProjectZedPowerMode", this.powerMode);
		comp.setInteger("ProjectZedPowerStored", this.storedPower);
	}
	
	/*@Override
	public abstract Packet getDescriptionPacket();*/

	@Override
	public abstract NBTTagCompound getUpdateTag();

	@Override
	public EnumFacing getRotatedState(EnumFacing facingDir, IBlockState currentState) {
		if (facingDir == EnumFacing.DOWN || facingDir == EnumFacing.UP) return frontFacing;

		return (frontFacing = frontFacing.rotateY());
	}

	@Override
	public EnumFacing getCurrentFacing() {
		// return frontFacing;
		return EnumFacing.getFront(getBlockMetadata()); // TODO: temp fix until sync issues are resolved.
	}

	@Override
	public void setFrontFacing(EnumFacing face) {
		this.frontFacing = face;
	}

	@Override
	public boolean canRotateTE() {
		return true;
	}
	
	@Override
	public void onInteract(ItemStack stack, EntityPlayer player, World world, Vector3<Integer> vec) {
	}
	
	@Override
	public boolean canSaveDataOnPickup() {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		 return 0;
	}

}

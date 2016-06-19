/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.api.util.EnumFrameType;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityEnergyContainer;
import com.projectzed.mod.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Class containing code for energy bank. <br>
 * NOTE: This class should be overriden for various energy tiers.
 * 
 * @author hockeyhurd
 * @version Dec 3, 2014
 */
public class TileEntityEnergyBankBase extends AbstractTileEntityEnergyContainer implements IModularFrame {

	protected int tier;
	protected int[] tiers = new int[] {
		(int) 1e6, (int) 1e7, (int) 1e8, (int) 1e9, 	
	};
	
	protected byte[] openSides = new byte[EnumFacing.VALUES.length];
	
	public TileEntityEnergyBankBase() {
		super("energyBank");
		this.tier = 0;
		this.maxPowerStorage = this.tiers[0];
		this.importRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE * 8;
		this.exportRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE * 8;
	}
	
	/**
	 * Allows setting tier of energy bank.
	 * @param tier tier number (should be 0, 1, 2, 3).
	 */
	public void setTier(int tier) {
		this.tier = tier >= 0 && tier <= tiers.length ? tier : 0;
		this.maxPowerStorage = tiers[tier];
	}
	
	/**
	 * @return tier of energy cell.
	 */
	public int getTier() {
		return this.tier;
	}

	@Override
	public EnumFrameType getType() {
		return EnumFrameType.POWER;
	}
	
	/**
	 * Sets given direction to given value.
	 *
	 * @param dir direction to set.
	 * @param value value to set (-1 = import, 0 = neutral or nothing allowed, 1 = export).
	 */
	@Override
	public void setSideValve(EnumFacing dir, byte value) {
		openSides[dir.ordinal()] = value;
	}
	
	/**
	 * Sets the side value after rotating to next value.
	 * @param dir direction to test.
	 */
	@Override
	public void setSideValveAndRotate(EnumFacing dir) {
		openSides[dir.ordinal()] = (byte) (openSides[dir.ordinal()] == -1 ? 0 : (openSides[dir.ordinal()] == 0 ? 1 : -1));
	}
	
	/**
	 * @param dir direction to test.
	 * @return -1 if can input, 0 neutral/nothing, or 1 to export.
	 */
	@Override
	public byte getSideValve(EnumFacing dir) {
		return openSides[dir.ordinal()];
	}
	
	/**
	 * @param dir = direction to test.
	 * @return -1 if can input, 0 neutral/nothing, or 1 to export.
	 */
	@Override
	public byte getSideValve(int dir) {
		return openSides[dir];
	}

	@Override
	public byte[] getSidedArray() {
		return openSides;
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
	public int getMaxImportRate() {
		return this.importRate;
	}

	@Override
	public int getMaxExportRate() {
		return this.exportRate;
	}

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

		else return 0;
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

		else return 0;
	}

	@Override
	protected void importContents() {
	}

	@Override
	@Deprecated
	protected void exportContents() {
	}
	
	@Override
	public void update() {
		super.update();
		
		if (!this.worldObj.isRemote) {
			if (this.maxPowerStorage != this.tiers[this.tier]) this.maxPowerStorage = this.tiers[this.tier]; 
			
			if (!this.powerMode) this.powerMode = true;
			if (this.lastReceivedDir != null) this.lastReceivedDir = null;
			
			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityEnergyContainer(this));
		}
	}
	
	// NOTE: server -> client
	/*@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityEnergyContainer(this));
	}*/

	@Override
	public NBTTagCompound getUpdateTag() {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityEnergyContainer(this));

		final NBTTagCompound comp = getTileData();
		saveNBT(comp);

		return comp;
	}

	// NOTE: client -> server
	@Override
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityEnergyContainer(this));
	}
	
	@Override
	public void readNBT(NBTTagCompound comp) {
		
		// Make sure the tier from nbt is acceptable.
		int tier = comp.getInteger("ProjectZedEnergyBankTier");
		this.tier = tier >= 0 && tier < this.tiers.length ? tier : 0;
		if (this.maxPowerStorage != this.tiers[this.tier]) this.maxPowerStorage = this.tiers[this.tier];

		this.openSides = new byte[EnumFacing.VALUES.length];

		for (int i = 0; i < this.openSides.length; i++) {
			this.openSides[i] = comp.getByte("ProjectZedEnergyBankSide" + i);
		}
		
		super.readNBT(comp);
	}
	
	@Override
	public void saveNBT(NBTTagCompound comp) {
		comp.setInteger("ProjectZedEnergyBankTier", this.tier);

		if (this.openSides == null) this.openSides = new byte[EnumFacing.VALUES.length];
		
		for (int i = 0; i < this.openSides.length; i++) {
			comp.setByte("ProjectZedEnergyBankSide" + i, this.openSides[i]);
		}
		
		super.saveNBT(comp);
	}

}

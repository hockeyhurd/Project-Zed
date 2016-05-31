/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity.generator;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.api.energy.generation.IEnergyGeneration;
import com.projectzed.api.energy.source.Source;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.api.tileentity.IWrenchable;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityGenerator;
import com.projectzed.mod.util.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Abstract class used for easyily adding a generic generator to mod.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
// TODO: AbstractTileEntityGenerator should extend AbstractTileEntityContainer
public abstract class AbstractTileEntityGenerator extends AbstractTileEntityGeneric implements IEnergyGeneration, IWrenchable {

	protected int maxStored = 100000;
	protected int stored;
	protected Source source;
	protected boolean powerMode = false;
	protected EnumFacing frontFacing = EnumFacing.NORTH;

	/**
	 * Default constructor.
	 * @param name name of container. <br>
	 *            Example: 'solarArray = container.solarArray'.
	 */
	public AbstractTileEntityGenerator(String name) {
		setCustomName("container." + name);
		defineSource();
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
	public void setCustomName(String name) {
		this.customName = name;
	}

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
		this.maxStored = max;
	}

	@Override
	public int getMaxStorage() {
		return this.maxStored;
	}

	@Override
	public void setEnergyStored(int amount) {
		this.stored = amount;
	}

	@Override
	public int getEnergyStored() {
		return this.stored;
	}

	@Override
	public int getMaxImportRate() {
		return 0;
	}

	@Override
	public int getMaxExportRate() {
		return Reference.Constants.BASE_PIPE_TRANSFER_RATE * 8;
	}

	@Override
	public int requestPower(IEnergyContainer cont, int amount) {
		if (cont != null && this.getMaxExportRate() >= amount) {
			if (this.stored - amount >= 0) this.stored -= amount;
			else {
				amount = this.stored;
				this.stored = 0;
			}
			return amount;
		}

		return 0;
	}

	@Override
	public int addPower(IEnergyContainer cont, int amount) {
		if (cont != null && this.getMaxImportRate() >= amount) {
			if (this.stored + amount <= this.maxStored) this.stored += amount;
			else {
				amount = this.maxStored - this.stored;
				this.stored = this.maxStored;
			}

			return amount;
		}

		return 0;
	}

	@Override
	public void setLastReceivedDirection(EnumFacing dir) {
	}

	@Override
	public EnumFacing getLastReceivedDirection() {
		return null;
	}

	@Override
	public abstract void defineSource();

	@Override
	public Source getSource() {
		return this.source;
	}

	@Override
	public void generatePower() {
		if (worldObj.isRemote) return;
		if (canProducePower()) {
			if (this.stored + this.source.getEffectiveSize() <= this.maxStored) this.stored += this.source.getEffectiveSize();
			else {
				int rem = this.maxStored - this.stored;
				this.stored += rem;
			}
		}

		if (this.stored > this.maxStored) this.stored = this.maxStored; // Redundancy check.
	}

	@Override
	public void transferPower() {
	}

	@Override
	public Vector3<Integer> worldVec() {
		return VectorHelper.toVector3i(pos);
	}

	@Override
	public boolean canProducePower() {
		return powerMode && stored < maxStored;
	}

	@Override
	public void setPowerMode(boolean state) {
		this.powerMode = state;
	}

	@Override
	public void update() {
		generatePower();
		transferPower();
		
		// If server side and every '1' second, send packet message to all clients.
		// if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0) PacketHandler.INSTANCE.sendToAll(new MessageTileEntityGenerator(this));
		this.markDirty();
		super.update();
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		this.powerMode = comp.getBoolean("ProjectZedPowerMode");
		int size = comp.getInteger("ProjectZedPowerStored");
		this.stored = size >= 0 && size <= this.maxStored ? size : 0;
	}

	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
		comp.setInteger("ProjectZedPowerStored", this.stored);
		comp.setBoolean("ProjectZedPowerMode", this.powerMode);
	}

	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityGenerator(this));
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

	// IWrenchable stuffs:
	@Override
	public EnumFacing getRotatedState(EnumFacing facingDir, IBlockState blockState) {
		if (facingDir == EnumFacing.DOWN || facingDir == EnumFacing.UP) return frontFacing;

		// return (frontFacing = frontFacing.rotateY());
		return (frontFacing = facingDir.getOpposite());
	}

	@Override
	public EnumFacing getCurrentFacing() {
		// return frontFacing;
		/*IBlockState blockState = BlockUtils.getBlock(worldObj, pos);
		for (IProperty<?> prop : blockState.getProperties().keySet()) {
			if (prop.getName().equals("facing") || prop.getName().equals("rotation")) {
				return (EnumFacing) blockState.getValue(prop);
			}
		}*/

		return EnumFacing.getFront(getBlockMetadata()); // TODO: temp fix until sync issues are resolved.

		// return frontFacing;
	}

	@Override
	public void setFrontFacing(EnumFacing facing) {
		this.frontFacing = facing;
		// ProjectZed.logHelper.info("new face:", frontFacing);
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

}

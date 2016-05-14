/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container.pipe;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.api.energy.EnergyNet;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.energy.source.IColorComponent;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityEnergyContainer;
import com.projectzed.mod.util.Reference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Class containing code for energy pipe;
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public class TileEntityEnergyPipeBase extends AbstractTileEntityPipe implements IEnergyContainer, IColorComponent {

	public boolean flag;
	protected int containerSize = 0;
	protected int maxPowerStorage = 100000;
	protected int storedPower;
	protected boolean powerMode;
	protected int importRate, exportRate;
	
	public TileEntityEnergyPipeBase() {
		super("energyPipe");
		this.maxPowerStorage = Reference.Constants.BASE_PIPE_TRANSFER_RATE;
		this.importRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE;
		this.exportRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.source.IColorComponent#getColor()
	 */
	@Override
	public EnumColor getColor() {
		return null;
	}
	
	/**
	 * Method is nullified on this implementation as their currently is no use for this
	 * <br>since energy pipes are independent of each other.
	 */
	public void setColor(EnumColor color) {
	}

	@Override
	protected void updateConnections() {
		for (EnumFacing dir : EnumFacing.VALUES) {
			final BlockPos tilePos = new BlockPos(pos.getX() + dir.getFrontOffsetX(), pos.getY() + dir.getFrontOffsetY(),
					pos.getZ() + dir.getFrontOffsetZ());

			final TileEntity tileEntity = worldObj.getTileEntity(tilePos);
			if (tileEntity instanceof IEnergyContainer) {
				if (tileEntity instanceof TileEntityEnergyPipeBase) {
					final TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) tileEntity;
					if (pipe.getColor() == getColor()) connections[dir.ordinal()] = dir.getOpposite();
				}

				else if (tileEntity instanceof IModularFrame) {
					final IModularFrame frame = (IModularFrame) tileEntity;
					if (frame.getSideValve(dir.getOpposite()) != 0) connections[dir.ordinal()] = dir.getOpposite();
					else connections[dir.ordinal()] = null;
				}

				else connections[dir.ordinal()] = dir.getOpposite();
			}

			else connections[dir.ordinal()] = null;
		}
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
	public void update() {
		super.update();
		importContents();
		exportContents();
	}

	@Override
	public int getMaxImportRate() {
		return this.importRate;
	}
	
	@Override
	public int getMaxExportRate() {
		return this.exportRate;
	}

	public int getContainerSize() {
		return this.containerSize;
	}

	protected void importContents() {
		if (worldObj.isRemote) return;
		
		if (this.storedPower >= this.maxPowerStorage) {
			this.storedPower = this.maxPowerStorage;
			return;
		}

		EnergyNet.importEnergyFromNeighbors(this, worldObj, pos.getX(), pos.getY(), pos.getZ(), lastReceivedDir);
		EnergyNet.tryClearDirectionalTraffic(this, worldObj, pos.getX(), pos.getY(), pos.getZ(), lastReceivedDir);
		
		// We don't need to send to client every tick! Once/sec. or so should suffice. (no gui)
		if (worldObj.getTotalWorldTime() % 20L == 0) PacketHandler.INSTANCE.sendToAll(new MessageTileEntityEnergyContainer(this));
	}

	@Deprecated
	protected void exportContents() {
	}
	
	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityEnergyContainer(this));
	}

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
	public void setLastReceivedDirection(EnumFacing dir) {
		this.lastReceivedDir = dir;
	}

	@Override
	public EnumFacing getLastReceivedDirection() {
		return this.lastReceivedDir;
	}

	@Override
	public Vector3<Integer> worldVec() {
		return VectorHelper.toVector3i(pos);
	}

	@Override
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		this.powerMode = comp.getBoolean("ProjectZedPowerMode");
		int size = comp.getInteger("ProjectZedPowerStored");
		this.storedPower = size >= 0 && size <= this.maxPowerStorage ? size : 0;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		comp.setBoolean("ProjectZedPowerMode", this.powerMode);
		comp.setInteger("ProjectZedPowerStored", this.storedPower);
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container.pipe;

import com.hockeyhurd.hcorelib.api.math.Vector3;
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
import net.minecraftforge.common.util.ForgeDirection;

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

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateConnections()
	 */
	@Override
	protected void updateConnections() {
		if (this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof IEnergyContainer) {
			if (this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
				if (pipe.getColor() == this.getColor()) connections[0] = ForgeDirection.UP;
			}
			
			else if (this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof IModularFrame) {
				IModularFrame frame = (IModularFrame) this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
				if (frame.getSideValve(ForgeDirection.UP.getOpposite()) != 0) connections[0] = ForgeDirection.UP;
				else connections[0] = null;
			}
			
			else connections[0] = ForgeDirection.UP;
		}
		else connections[0] = null;

		if (this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof IEnergyContainer) {
			if (this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
				if (pipe.getColor() == this.getColor()) connections[1] = ForgeDirection.DOWN;
			}
			
			else if (this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof IModularFrame) {
				IModularFrame frame = (IModularFrame) this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
				if (frame.getSideValve(ForgeDirection.DOWN.getOpposite()) != 0) connections[1] = ForgeDirection.DOWN;
				else connections[1] = null;
			}
			
			else connections[1] = ForgeDirection.DOWN;
		}
		else connections[1] = null;

		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof IEnergyContainer) {
			if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
				if (pipe.getColor() == this.getColor()) connections[2] = ForgeDirection.NORTH;
			}
			
			else if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof IModularFrame) {
				IModularFrame frame = (IModularFrame) this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
				if (frame.getSideValve(ForgeDirection.NORTH.getOpposite()) != 0) connections[2] = ForgeDirection.NORTH;
				else connections[2] = null;
			}
			
			else connections[2] = ForgeDirection.NORTH;
		}
		else connections[2] = null;

		if (this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof IEnergyContainer) {
			if (this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
				if (pipe.getColor() == this.getColor()) connections[3] = ForgeDirection.EAST;
			}
			
			else if (this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof IModularFrame) {
				IModularFrame frame = (IModularFrame) this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
				if (frame.getSideValve(ForgeDirection.EAST.getOpposite()) != 0) connections[3] = ForgeDirection.EAST;
				else connections[3] = null;
			}
			
			// else connections[3] = ForgeDirection.EAST;
		}
		else connections[3] = null;

		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof IEnergyContainer) {
			if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
				if (pipe.getColor() == this.getColor()) connections[4] = ForgeDirection.SOUTH;
			}
			
			else if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof IModularFrame) {
				IModularFrame frame = (IModularFrame) this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
				if (frame.getSideValve(ForgeDirection.SOUTH.getOpposite()) != 0) connections[4] = ForgeDirection.SOUTH;
				else connections[4] = null;
			}
			
			else connections[4] = ForgeDirection.SOUTH;
		}
		else connections[4] = null;

		if (this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof IEnergyContainer) {
			if (this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
				if (pipe.getColor() == this.getColor()) connections[5] = ForgeDirection.WEST;
			}
			
			else if (this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof IModularFrame) {
				IModularFrame frame = (IModularFrame) this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
				if (frame.getSideValve(ForgeDirection.WEST.getOpposite()) != 0) connections[5] = ForgeDirection.WEST;
				else connections[5] = null;
			}
			
			else connections[5] = ForgeDirection.WEST;
		}
		else connections[5] = null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#requestPower(com.projectzed.api.storage.IEnergyContainer, int)
	 */
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
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#addPower(com.projectzed.api.energy.storage.IEnergyContainer, int)
	 */
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
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateEntity()
	 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		importContents();
		exportContents();
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.storage.IEnergyContainer#getMaxImportRate()
	 */
	public int getMaxImportRate() {
		return this.importRate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getMaxTransferRate()
	 */
	public int getMaxExportRate() {
		return this.exportRate;
	}

	public int getContainerSize() {
		return this.containerSize;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#importContents()
	 */
	protected void importContents() {
		if (this.getWorldObj().isRemote) return;
		
		if (this.storedPower >= this.maxPowerStorage) {
			this.storedPower = this.maxPowerStorage;
			return;
		}

		EnergyNet.importEnergyFromNeighbors(this, worldObj, xCoord, yCoord, zCoord, lastReceivedDir);
		EnergyNet.tryClearDirectionalTraffic(this, worldObj, xCoord, yCoord, zCoord, lastReceivedDir);
		
		// We don't need to send to client every tick! Once/sec. or so should suffice. (no gui)
		if (this.getWorldObj().getTotalWorldTime() % 20L == 0) PacketHandler.INSTANCE.sendToAll(new MessageTileEntityEnergyContainer(this));
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#exportContents()
	 */
	@Deprecated
	protected void exportContents() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getDescriptionPacket()
	 */
	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityEnergyContainer(this));
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#setMaxStorage(int)
	 */
	@Override
	public void setMaxStorage(int max) {
		this.maxPowerStorage = max;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#getMaxStorage()
	 */
	@Override
	public int getMaxStorage() {
		return this.maxPowerStorage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#setEnergyStored(int)
	 */
	@Override
	public void setEnergyStored(int amount) {
		this.storedPower = amount;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#getEnergyStored()
	 */
	@Override
	public int getEnergyStored() {
		return this.storedPower;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#setLastReceivedDirection(net.minecraftforge.common.util.ForgeDirection)
	 */
	@Override
	public void setLastReceivedDirection(ForgeDirection dir) {
		this.lastReceivedDir = dir;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#getLastReceivedDirection()
	 */
	@Override
	public ForgeDirection getLastReceivedDirection() {
		return this.lastReceivedDir;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.storage.IEnergyContainer#worldVec()
	 */
	@Override
	public Vector3<Integer> worldVec() {
		return new Vector3<Integer>(this.xCoord, this.yCoord, this.zCoord);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		this.powerMode = comp.getBoolean("ProjectZedPowerMode");
		int size = comp.getInteger("ProjectZedPowerStored");
		this.storedPower = size >= 0 && size <= this.maxPowerStorage ? size : 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		comp.setBoolean("ProjectZedPowerMode", this.powerMode);
		comp.setInteger("ProjectZedPowerStored", this.storedPower);
	}

}

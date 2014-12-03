package com.projectzed.mod.tileentity.container.pipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.util.Reference;

/**
 * Class containing coe for energy pipe;
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public class TileEntityEnergyPipeBase extends AbstractTileEntityPipe {

	public boolean flag;
	protected int containerSize = 0;
	
	public TileEntityEnergyPipeBase() {
		super("energyPipe");
		this.maxStorage = Reference.Constants.BASE_PIPE_TRANSFER_RATE /* * 2*/;
		this.importRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE;
		this.exportRate = Reference.Constants.BASE_PIPE_TRANSFER_RATE;
	}
	
	public EnumColor getColor() {
		return null;
	}

	/*
	 * TODO: Change ... instanceof AbstractTileEntityGenerator to ... instanceof AbstractTileEntityContainer once properly changed! (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateConnections()
	 */
	protected void updateConnections() {
		if (this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof IEnergyContainer) {
			if (this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
				if (pipe.getColor() == this.getColor()) connections[0] = ForgeDirection.UP;
			}
			
			else connections[0] = ForgeDirection.UP;
		}
		else connections[0] = null;

		if (this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof IEnergyContainer) {
			if (this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
				if (pipe.getColor() == this.getColor()) connections[1] = ForgeDirection.DOWN;
			}
			
			else connections[1] = ForgeDirection.DOWN;
		}
		else connections[1] = null;

		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof IEnergyContainer) {
			if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
				if (pipe.getColor() == this.getColor()) connections[2] = ForgeDirection.NORTH;
			}
			
			else connections[2] = ForgeDirection.NORTH;
		}
		else connections[2] = null;

		if (this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof IEnergyContainer) {
			if (this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
				if (pipe.getColor() == this.getColor()) connections[3] = ForgeDirection.EAST;
			}
			
			else connections[3] = ForgeDirection.EAST;
		}
		else connections[3] = null;

		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof IEnergyContainer) {
			if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
				if (pipe.getColor() == this.getColor()) connections[4] = ForgeDirection.SOUTH;
			}
			
			else connections[4] = ForgeDirection.SOUTH;
		}
		else connections[4] = null;

		if (this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof IEnergyContainer) {
			if (this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
				if (pipe.getColor() == this.getColor()) connections[5] = ForgeDirection.WEST;
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
			if (this.stored - amount >= 0) this.stored -= amount;
			else {
				amount = this.stored;
				this.stored = 0;
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
			if (this.stored + amount <= this.maxStorage) this.stored += amount;
			else {
				amount = this.maxStorage - this.stored;
				this.stored = this.maxStorage;
			}
			
			return amount;
		}
		
		else return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateEntity()
	 */
	public void updateEntity() {
		super.updateEntity();
		if (!this.getWorldObj().isRemote && this.stored < this.maxStorage) System.out.println(this.stored);
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
		
		if (this.stored >= this.maxStorage) {
			this.stored = this.maxStorage;
			return;
		}

		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;
		List<IEnergyContainer> containers = new ArrayList<IEnergyContainer>();

		// -x
		if (worldObj.getTileEntity(x - 1, y, z) != null && worldObj.getTileEntity(x - 1, y, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x - 1, y, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x - 1, y, z);
			if (cont instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) cont;
				if (pipe.getColor() == this.getColor()) containers.add(cont);
			}
			
			else containers.add(cont);
		}

		// +x
		if (worldObj.getTileEntity(x + 1, y, z) != null && worldObj.getTileEntity(x + 1, y, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x + 1, y, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x + 1, y, z);
			if (cont instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) cont;
				if (pipe.getColor() == this.getColor()) containers.add(cont);
			}
			
			else containers.add(cont);
		}

		// -y
		if (worldObj.getTileEntity(x, y - 1, z) != null && worldObj.getTileEntity(x, y - 1, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y - 1, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y - 1, z);
			if (cont instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) cont;
				if (pipe.getColor() == this.getColor()) containers.add(cont);
			}
			
			else containers.add(cont);
		}

		// +y
		if (worldObj.getTileEntity(x, y + 1, z) != null && worldObj.getTileEntity(x, y + 1, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y + 1, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y + 1, z);
			if (cont instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) cont;
				if (pipe.getColor() == this.getColor()) containers.add(cont);
			}
			
			else containers.add(cont);
		}

		// -z
		if (worldObj.getTileEntity(x, y, z - 1) != null && worldObj.getTileEntity(x, y, z - 1) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y, z - 1) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y, z - 1);
			if (cont instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) cont;
				if (pipe.getColor() == this.getColor()) containers.add(cont);
			}
			
			else containers.add(cont);
		}

		// +z
		if (worldObj.getTileEntity(x, y, z + 1) != null && worldObj.getTileEntity(x, y, z + 1) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y, z + 1) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y, z + 1);
			if (cont instanceof TileEntityEnergyPipeBase) {
				TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) cont;
				if (pipe.getColor() == this.getColor()) containers.add(cont);
			}
			
			else containers.add(cont);
		}

		if (containers.size() > 0) {
			for (IEnergyContainer c : containers) {
				if (this.stored >= this.maxStorage) {
					this.stored = this.maxStorage;
					break;
				}
				
				if (this.stored < this.maxStorage && c.getEnergyStored() > 0) {
					if (c instanceof TileEntityEnergyPipeBase && this.stored >= c.getEnergyStored()) continue;
					
					int amount = Math.min(this.importRate, c.getMaxExportRate());
					this.stored += c.requestPower(this, amount);
				}
			}
		}

		containers.removeAll(Collections.EMPTY_LIST);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#exportContents()
	 */
	protected void exportContents() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void readFromNBT(NBTTagCompound comp) {
		// super.readFromNBT(comp);
		
		int size = comp.getInteger("ProjectZedPowerStored");
		this.stored =  size >= 0 && size <= this.maxStorage ? size : 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void writeToNBT(NBTTagCompound comp) {
		// super.writeToNBT(comp);
		comp.setInteger("ProjectZedPowerStored", this.stored);
	}

}

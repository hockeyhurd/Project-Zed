package com.projectzed.mod.tileentity.container.pipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import com.projectzed.api.generation.IEnergyGeneration;
import com.projectzed.api.storage.IEnergyContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;

/**
 * Class containing coode for energy pipe;
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public class TileEntityEnergyPipe extends AbstractTileEntityPipe {

	public boolean flag;
	protected int transferRate;
	protected int containerSize = 0;
	
	public TileEntityEnergyPipe() {
		super("energyPipe");
		this.maxStorage = 200;
		this.transferRate = 10;
	}

	/*
	 * TODO: Change ... instanceof AbstractTileEntityGenerator to ... instanceof AbstractTileEntityContainer once properly changed! (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateConnections()
	 */
	protected void updateConnections() {
		if (this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof IEnergyContainer) connections[0] = ForgeDirection.UP;
		else connections[0] = null;

		if (this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof IEnergyContainer) connections[1] = ForgeDirection.DOWN;
		else connections[1] = null;

		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof IEnergyContainer) connections[2] = ForgeDirection.NORTH;
		else connections[2] = null;

		if (this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof IEnergyContainer) connections[3] = ForgeDirection.EAST;
		else connections[3] = null;

		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof IEnergyContainer) connections[4] = ForgeDirection.SOUTH;
		else connections[4] = null;

		if (this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof IEnergyContainer) connections[5] = ForgeDirection.WEST;
		else connections[5] = null;
	}

	protected void importContents() {
		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;
		List<IEnergyContainer> containers = new ArrayList<IEnergyContainer>();

		// -x
		if (worldObj.getTileEntity(x - 1, y, z) != null && worldObj.getTileEntity(x - 1, y, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x - 1, y, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x - 1, y, z);
			if (cont.getEnergyStored() > 0 && this.stored + cont.getMaxTransferRate() <= this.maxStorage) containers.add(cont);
		}

		// +x
		if (worldObj.getTileEntity(x + 1, y, z) != null && worldObj.getTileEntity(x + 1, y, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x + 1, y, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x + 1, y, z);
			if (cont.getEnergyStored() > 0 && this.stored + cont.getMaxTransferRate() <= this.maxStorage) containers.add(cont);
		}

		// -y
		if (worldObj.getTileEntity(x, y - 1, z) != null && worldObj.getTileEntity(x, y - 1, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y - 1, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y - 1, z);
			if (cont.getEnergyStored() > 0 && this.stored + cont.getMaxTransferRate() <= this.maxStorage) containers.add(cont);
		}

		// +y
		if (worldObj.getTileEntity(x, y + 1, z) != null && worldObj.getTileEntity(x, y + 1, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y + 1, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y + 1, z);
			if (cont.getEnergyStored() > 0 && this.stored + cont.getMaxTransferRate() <= this.maxStorage) containers.add(cont);
		}

		// -z
		if (worldObj.getTileEntity(x, y, z - 1) != null && worldObj.getTileEntity(x, y, z - 1) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y, z - 1) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y, z - 1);
			if (cont.getEnergyStored() > 0 && this.stored + cont.getMaxTransferRate() <= this.maxStorage) containers.add(cont);
		}

		// +z
		if (worldObj.getTileEntity(x, y, z + 1) != null && worldObj.getTileEntity(x, y, z + 1) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y, z + 1) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y, z + 1);
			if (cont.getEnergyStored() > 0 && this.stored + cont.getMaxTransferRate() <= this.maxStorage) containers.add(cont);
		}

		if (containers.size() > 0) {
			for (IEnergyContainer c : containers) {
				if (this.stored + c.getMaxTransferRate() <= this.maxStorage) this.stored += c.getMaxTransferRate();
			}
		}

		containers.removeAll(Collections.EMPTY_LIST);
	}

	protected void exportContents() {
		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;
		List<IEnergyContainer> containers = new ArrayList<IEnergyContainer>();
		flag = false;

		// -x
		if (worldObj.getTileEntity(x - 1, y, z) != null && worldObj.getTileEntity(x - 1, y, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x - 1, y, z) instanceof IEnergyGeneration)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x - 1, y, z);
			if (cont.getEnergyStored() + cont.getMaxTransferRate() <= cont.getMaxStorage() && this.stored - this.getMaxTransferRate() > 0) containers.add(cont);
		}

		// +x
		if (worldObj.getTileEntity(x + 1, y, z) != null && worldObj.getTileEntity(x + 1, y, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x + 1, y, z) instanceof IEnergyGeneration)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x + 1, y, z);
			if (cont.getEnergyStored() + cont.getMaxTransferRate() <= cont.getMaxStorage() && this.stored - this.getMaxTransferRate() > 0) containers.add(cont);
		}

		// -y
		if (worldObj.getTileEntity(x, y - 1, z) != null && worldObj.getTileEntity(x, y - 1, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y - 1, z) instanceof IEnergyGeneration)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y - 1, z);
			if (cont.getEnergyStored() + cont.getMaxTransferRate() <= cont.getMaxStorage() && this.stored - this.getMaxTransferRate() > 0) containers.add(cont);
		}

		// +y
		if (worldObj.getTileEntity(x, y + 1, z) != null && worldObj.getTileEntity(x, y + 1, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y + 1, z) instanceof IEnergyGeneration)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y + 1, z);
			if (cont.getEnergyStored() + cont.getMaxTransferRate() <= cont.getMaxStorage() && this.stored - this.getMaxTransferRate() > 0) containers.add(cont);
		}

		// -z
		if (worldObj.getTileEntity(x, y, z - 1) != null && worldObj.getTileEntity(x, y, z - 1) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y, z - 1) instanceof IEnergyGeneration)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y, z - 1);
			if (cont.getEnergyStored() + cont.getMaxTransferRate() <= cont.getMaxStorage() && this.stored - this.getMaxTransferRate() > 0) containers.add(cont);
		}

		// +z
		if (worldObj.getTileEntity(x, y, z + 1) != null && worldObj.getTileEntity(x, y, z + 1) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y, z + 1) instanceof IEnergyGeneration)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y, z + 1);
			if (cont.getEnergyStored() + cont.getMaxTransferRate() <= cont.getMaxStorage() && this.stored - this.getMaxTransferRate() > 0) containers.add(cont);
		}

		if (containers.size() > 0) {

			for (IEnergyContainer c : containers) {
				if (!(c instanceof TileEntityEnergyPipe)) flag = true;
			}

			if (flag) {
				this.containerSize = containers.size();
				for (IEnergyContainer c : containers) {
					if (c instanceof TileEntityEnergyPipe) {
						TileEntityEnergyPipe te = (TileEntityEnergyPipe) c; 
						if (te.getContainerSize() - 1 > 0) this.containerSize = te.getContainerSize() - 1;
					}
					if (this.containerSize < 0) this.containerSize = 0; // Redundant check
					if (this.stored - this.getMaxTransferRate() > 0) this.stored -= this.getMaxTransferRate() * containers.size();
				}
			}
		}

		containers.removeAll(Collections.EMPTY_LIST);
	}

	public void updateEntity() {
		super.updateEntity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityContainer#getMaxTransferRate()
	 */
	public int getMaxTransferRate() {
		return this.transferRate;
	}
	
	public int getContainerSize() {
		return this.containerSize;
	}

}

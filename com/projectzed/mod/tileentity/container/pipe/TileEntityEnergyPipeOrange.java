package com.projectzed.mod.tileentity.container.pipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import com.projectzed.api.source.EnumColor;
import com.projectzed.api.storage.IEnergyContainer;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;

/**
 * Class used to create more specific instance of base class
 * in which this class inherits from.
 * 
 * @author hockeyhurd
 * @version Nov 13, 2014
 */
public class TileEntityEnergyPipeOrange extends TileEntityEnergyPipeBase {

	public TileEntityEnergyPipeOrange() {
		super();
		this.importRate = super.importRate * 4;
		this.exportRate = super.exportRate * 4;
	}
	
	public EnumColor getColor() {
		return EnumColor.ORANGE;
	}
	
	/*
	 * TODO: Change ... instanceof AbstractTileEntityGenerator to ... instanceof AbstractTileEntityContainer once properly changed! (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateConnections()
	 */
	protected void updateConnections() {
		if (this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof IEnergyContainer) {
			if (!(this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityEnergyPipeRed)) connections[0] = ForgeDirection.UP;
		}
		else connections[0] = null;

		if (this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof IEnergyContainer) {
			if (!(this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityEnergyPipeRed)) connections[1] = ForgeDirection.DOWN;
		}
		else connections[1] = null;

		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof IEnergyContainer) {
			if (!(this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityEnergyPipeRed)) connections[2] = ForgeDirection.NORTH;
		}
		else connections[2] = null;

		if (this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof IEnergyContainer) {
			if (!(this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof TileEntityEnergyPipeRed)) connections[3] = ForgeDirection.EAST;
		}
		else connections[3] = null;

		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof IEnergyContainer) {
			if (!(this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityEnergyPipeRed)) connections[4] = ForgeDirection.SOUTH;
		}
		else connections[4] = null;

		if (this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof IEnergyContainer) {
			if (!(this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityEnergyPipeRed)) connections[5] = ForgeDirection.WEST;
		}
		else connections[5] = null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeBase#importContents()
	 */
	protected void importContents() {
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
			if (!(cont instanceof TileEntityEnergyPipeRed)) containers.add(cont);
		}

		// +x
		if (worldObj.getTileEntity(x + 1, y, z) != null && worldObj.getTileEntity(x + 1, y, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x + 1, y, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x + 1, y, z);
			if (!(cont instanceof TileEntityEnergyPipeRed)) containers.add(cont);
		}

		// -y
		if (worldObj.getTileEntity(x, y - 1, z) != null && worldObj.getTileEntity(x, y - 1, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y - 1, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y - 1, z);
			if (!(cont instanceof TileEntityEnergyPipeRed)) containers.add(cont);
		}

		// +y
		if (worldObj.getTileEntity(x, y + 1, z) != null && worldObj.getTileEntity(x, y + 1, z) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y + 1, z) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y + 1, z);
			if (!(cont instanceof TileEntityEnergyPipeRed)) containers.add(cont);
		}

		// -z
		if (worldObj.getTileEntity(x, y, z - 1) != null && worldObj.getTileEntity(x, y, z - 1) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y, z - 1) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y, z - 1);
			if (!(cont instanceof TileEntityEnergyPipeRed)) containers.add(cont);
		}

		// +z
		if (worldObj.getTileEntity(x, y, z + 1) != null && worldObj.getTileEntity(x, y, z + 1) instanceof IEnergyContainer && !(worldObj.getTileEntity(x, y, z + 1) instanceof AbstractTileEntityMachine)) {
			IEnergyContainer cont = (IEnergyContainer) worldObj.getTileEntity(x, y, z + 1);
			if (!(cont instanceof TileEntityEnergyPipeRed)) containers.add(cont);
		}

		if (containers.size() > 0) {
			for (IEnergyContainer c : containers) {
				if (this.stored >= this.maxStorage) {
					this.stored = this.maxStorage;
					break;
				}
				if (c.getEnergyStored() - c.getMaxExportRate() > 0) this.stored += c.requestPower(this, c.getMaxExportRate());
			}
		}

		containers.removeAll(Collections.EMPTY_LIST);
	}
	
}

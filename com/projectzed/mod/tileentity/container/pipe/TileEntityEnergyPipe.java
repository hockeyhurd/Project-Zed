package com.projectzed.mod.tileentity.container.pipe;

import net.minecraftforge.common.util.ForgeDirection;

import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;

/**
 * Class containing coode for energy pipe;
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public class TileEntityEnergyPipe extends AbstractTileEntityPipe {
	
	public boolean test;
	
	public TileEntityEnergyPipe() {
		super("energyPipe");
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateConnections()
	 */
	protected void updateConnections() {
		if (this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityEnergyPipe) connections[0] = ForgeDirection.UP;
		if (this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityEnergyPipe) connections[1] = ForgeDirection.DOWN;
		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityEnergyPipe) connections[2] = ForgeDirection.NORTH;
		if (this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof TileEntityEnergyPipe) connections[3] = ForgeDirection.EAST;
		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityEnergyPipe) connections[4] = ForgeDirection.SOUTH;
		if (this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityEnergyPipe) connections[5] = ForgeDirection.WEST;
	}
	
	public void updateEntity() {
		super.updateEntity();
	}

}

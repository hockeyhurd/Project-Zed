package com.projectzed.mod.tileentity.container.pipe;

import net.minecraftforge.common.util.ForgeDirection;

import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;

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

	/* TODO: Change ... instanceof AbstractTileEntityGenerator to ... instanceof AbstractTileEntityContainer once properly changed!
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateConnections()
	 */
	protected void updateConnections() {
		if (this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityEnergyPipe || this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof AbstractTileEntityGenerator) connections[0] = ForgeDirection.UP;
		else connections[0] = null;
		
		if (this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityEnergyPipe || this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof AbstractTileEntityGenerator) connections[1] = ForgeDirection.DOWN;
		else connections[1] = null;
		
		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityEnergyPipe || this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof AbstractTileEntityGenerator) connections[2] = ForgeDirection.NORTH;
		else connections[2] = null;
		
		if (this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof TileEntityEnergyPipe || this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof AbstractTileEntityGenerator) connections[3] = ForgeDirection.EAST;
		else connections[3] = null;
		
		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityEnergyPipe || this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof AbstractTileEntityGenerator) connections[4] = ForgeDirection.SOUTH;
		else connections[4] = null;
		
		if (this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityEnergyPipe || this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof AbstractTileEntityGenerator) connections[5] = ForgeDirection.WEST;
		else connections[5] = null;
	}

	public void updateEntity() {
		super.updateEntity();
	}

}

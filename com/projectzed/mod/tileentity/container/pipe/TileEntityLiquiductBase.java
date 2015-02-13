package com.projectzed.mod.tileentity.container.pipe;

import net.minecraft.network.Packet;

import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;

/**
 * 
 * 
 * @author hockeyhurd
 * @version Feb 12, 2015
 */
public class TileEntityLiquiductBase extends AbstractTileEntityPipe {

	/**
	 * @param name
	 */
	public TileEntityLiquiductBase(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#requestPower(com.projectzed.api.energy.storage.IEnergyContainer, int)
	 */
	@Override
	public int requestPower(IEnergyContainer cont, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#addPower(com.projectzed.api.energy.storage.IEnergyContainer, int)
	 */
	@Override
	public int addPower(IEnergyContainer cont, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateConnections()
	 */
	@Override
	protected void updateConnections() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getMaxImportRate()
	 */
	@Override
	public int getMaxImportRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getMaxExportRate()
	 */
	@Override
	public int getMaxExportRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#importContents()
	 */
	@Override
	protected void importContents() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#exportContents()
	 */
	@Override
	protected void exportContents() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getDescriptionPacket()
	 */
	@Override
	public Packet getDescriptionPacket() {
		// TODO Auto-generated method stub
		return null;
	}

}

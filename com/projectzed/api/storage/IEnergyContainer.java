package com.projectzed.api.storage;

/**
 * Interface for an object (TileEntity, tool, etc.) that contains power.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public interface IEnergyContainer {
	
	/** Max allowed capactiy */
	public void setMaxStorage(int max);
	
	/** Set the amount of energy stored. */
	public void setEnergyStored(int amount);
	
	/** Get the amount currently stored. */
	public int getEnergyStored();
	
}

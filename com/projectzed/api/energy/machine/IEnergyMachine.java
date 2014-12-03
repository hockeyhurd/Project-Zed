package com.projectzed.api.energy.machine;

import com.projectzed.api.energy.storage.IEnergyContainer;

/**
 * Interface for all machines
 * 
 * @author hockeyhurd
 * @version Oct 23, 2014
 */
public interface IEnergyMachine extends IEnergyContainer {

	/** Method used to set the status of machine (i.e. is it 'on') */
	public void setPowerMode(boolean val);
	
	/** Function used to get whether the machine is on. */
	public boolean isPoweredOn();
	
	/** Sets the energy burn rate of the machine. */
	public void setEnergyBurnRate(int val);
	
	/** Gets the energy burn rate of the machine. */
	public int getEnergyBurnRate();
	
	/** Method used to burn energy while in use. */
	public void burnEnergy();
	
}

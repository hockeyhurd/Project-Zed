package com.projectzed.api.generation;

import com.projectzed.api.source.Source;
import com.projectzed.api.storage.IEnergyContainer;

/**
 * Interface controlling how much energy can be created per tick.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public interface IEnergyGeneration extends IEnergyContainer {

	/** Method used to define source of power. */
	public void defineSource();
	
	/** Getter function for getting the source of power defined. */
	public Source getSource();
	
	/** Method used for power generation */
	public void generatePower();
	
	/** Function returning whether this machine can currently produce power. */
	public boolean canProducePower();
	
	/** Method used to set whether this generator can currently produce its power. */
	public void setPowerMode(boolean state);
	
	/** Method used for dispersing power to all other nodes. */
	public void transferPower();
	
}

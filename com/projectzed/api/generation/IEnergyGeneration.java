package com.projectzed.api.generation;

import com.projectzed.api.source.Source;

/**
 * Interface controlling how much energy can be created per tick.
 * 
 * @author hockeyhurd
 * @version Oct 19, 2014
 */
public interface IEnergyGeneration {

	public void defineSource();
	public Source getSource();
	
}

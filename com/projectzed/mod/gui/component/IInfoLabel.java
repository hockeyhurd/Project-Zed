package com.projectzed.mod.gui.component;

import java.util.List;

import com.hockeyhurd.api.math.Vector4Helper;

/**
 * Interface for gui labels.
 * 
 * @author hockeyhurd
 * @version Dec 31, 2014
 */
public interface IInfoLabel<N> {

	/** Get label to display to user. */
	public List<String> getLabel();
	
	/** Get effective percent full of storage. */
	public float getPercent();
	
	/** Function to determine if label can be shown */
	public boolean isVisible(boolean ignoreMouse);
	
	/** Handles update appropriate values. */
	public void update(Vector4Helper<Integer> mouseVec, Vector4Helper<Integer> pos, Vector4Helper<Integer> minMax, N stored, N max);
	
}

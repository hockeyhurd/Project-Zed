package com.projectzed.mod.gui.component;

import java.util.List;

/**
 * Interface for gui containers to implement custom labels (and maybe guibuttons).
 * 
 * @author hockeyhurd
 * @version Dec 31, 2014
 */
public interface IInfoContainer {

	/** Method inherited from given gui's parent class. */
	public void drawScreen(int x, int y, float f);	
	
	/** Ensure components are init here. */
	public void initGui();
	
	/** List to contain all components. */
	List<IInfoLabel> getComponents();
	
	IInfoLabel visibleComp();
	
	/** Method to ensure data is being updated. */
	void update();
	
}

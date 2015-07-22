/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
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
	void drawScreen(int x, int y, float f);
	
	/** Ensure components are init here. */
	void initGui();
	
	/** List to contain all components. */
	List<IInfoLabel> getComponents();

	/** Function should return current visible component dependent on mouse location. */
	IInfoLabel visibleComp();
	
	/** Method to ensure data is being updated. */
	void updateScreen();
	
}

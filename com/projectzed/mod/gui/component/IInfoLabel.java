/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui.component;

import com.hockeyhurd.hcorelib.api.math.Vector2;

import java.util.List;

/**
 * Interface for gui labels.
 * 
 * @author hockeyhurd
 * @version Dec 31, 2014
 */
public interface IInfoLabel<N> {

	/** Get label to display to user. */
	List<String> getLabel();
	
	/** Get effective percent full of storage. */
	float getPercent();
	
	/** Function to determine if label can be shown */
	boolean isVisible(boolean ignoreMouse);

	/** Handles update appropriate values. */
	void update(Vector2<Integer> mouseVec, Vector2<Integer> pos, Vector2<Integer> minMax, N[] data);
	
}

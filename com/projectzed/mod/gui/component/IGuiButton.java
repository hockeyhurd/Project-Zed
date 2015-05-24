/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;

import com.hockeyhurd.api.math.Vector2;

/**
 * 
 * 
 * @author hockeyhurd
 * @version May 23, 2015
 */
public interface IGuiButton {

	/**
	 * @param minecraft minecraft client instance.
	 * @param x x-pos.
	 * @param y y-pos.
	 */
	void drawButton(Minecraft minecraft, int x, int y);
	
	/**
	 * @param minecraft minecraft client instance.
	 * @param x x-pos.
	 * @param y y-pos.
	 * @return true if mouse was pressed, else returns false.
	 */
	boolean mousePressed(Minecraft minecraft, int x, int y);
	
	/**
	 * @return true if button is active, else returns false.
	 */
	boolean isActive();

	/**
	 * Sets whether button is active or not.
	 * 
	 * @param active value to set.
	 */
	void setActive(boolean active);
	
	/**
	 * @return vector position of button.
	 */
	Vector2<Integer> getPos();
	
}

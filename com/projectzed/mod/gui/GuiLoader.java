/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialLoader;

/**
 * Class containing gui code for industrial loader.
 * 
 * @author hockeyhurd
 * @version Apr 21, 2015
 */
public class GuiLoader extends GuiMachine {

	private final TileEntityIndustrialLoader te2;
	
	/**
	 * @param inv
	 * @param te
	 */
	public GuiLoader(InventoryPlayer inv, TileEntityIndustrialLoader te) {
		super(inv, te);
		this.te2 = te;
		texture = new ResourceLocation("projectzed", "textures/gui/GuiGenerator_generic0.png");
	}

}

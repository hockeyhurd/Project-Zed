/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.proxy;

import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import com.hockeyhurd.api.item.AbstractItemRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Item rendering class for wickedClearGlass.
 * 
 * @author hockeyhurd
 * @version Mar 26, 2015
 */
@SideOnly(Side.CLIENT)
public class WickedClearGlassItemRenderer extends AbstractItemRenderer implements IItemRenderer {

	/**
	 * @param icon icon to draw.
	 */
	public WickedClearGlassItemRenderer(IIcon icon) {
		super(icon);
	}

}

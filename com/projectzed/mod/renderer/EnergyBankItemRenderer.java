/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.renderer;

import com.hockeyhurd.api.item.AbstractItemRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

/**
 * Simple class extending AbstractItemRenderer.
 * <br><br>For more info on implementation, see: {@link com.hockeyhurd.api.item.AbstractItemRenderer}.
 * 
 * @author hockeyhurd
 * @version Dec 7, 2014
 */
@SideOnly(Side.CLIENT)
public class EnergyBankItemRenderer extends AbstractItemRenderer {
	
	/**
	 * @param icon = icon to draw from.
	 */
	public EnergyBankItemRenderer(IIcon icon) {
		super(icon);
	}

}

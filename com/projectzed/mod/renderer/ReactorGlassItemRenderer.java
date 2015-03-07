/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.renderer;

import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import com.hockeyhurd.api.item.AbstractItemRenderer;

/**
 * Simple item renderer of nuclearReactorGlass.
 * 
 * @author hockeyhurd
 * @version Mar 7, 2015
 */
public class ReactorGlassItemRenderer extends AbstractItemRenderer implements IItemRenderer {

	/**
	 * @param icon
	 */
	public ReactorGlassItemRenderer(IIcon icon) {
		super(icon);
	}

}

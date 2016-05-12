/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.renderer;

import com.hockeyhurd.hcorelib.api.item.AbstractItemRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Simple item renderer of nuclearReactorGlass.
 * 
 * @author hockeyhurd
 * @version Mar 7, 2015
 */
@SideOnly(Side.CLIENT)
public class ReactorGlassItemRenderer extends AbstractItemRenderer {

	/**
	 * @param icon
	 */
	public ReactorGlassItemRenderer(IIcon icon) {
		super(icon);
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		super.renderItem(type, item, data);
	}

}

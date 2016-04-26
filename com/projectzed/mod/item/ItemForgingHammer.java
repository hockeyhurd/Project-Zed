/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item;

import com.hockeyhurd.api.item.AbstractHCoreItem;
import com.projectzed.mod.ProjectZed;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

/**
 * Class containing code for forgingHammer.
 * 
 * @author hockeyhurd
 * @version Dec 9, 2014
 */
public class ItemForgingHammer extends AbstractHCoreItem {

	public ItemForgingHammer() {
		super(ProjectZed.modCreativeTab, "forgingHammer", ProjectZed.assetDir);
		this.setMaxDamage(256);
		this.setMaxStackSize(1);
	}
	
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		list.add(EnumChatFormatting.GREEN + "Uses left " + EnumChatFormatting.GRAY + (stack.getMaxDamage() - stack.getItemDamage()));
	}

}

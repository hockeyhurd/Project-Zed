/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item;

import com.hockeyhurd.hcorelib.api.item.AbstractHCoreItem;
import com.projectzed.mod.ProjectZed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Class containing code for forgingHammer.
 * 
 * @author hockeyhurd
 * @version Dec 9, 2014
 */
public class ItemForgingHammer extends AbstractHCoreItem {

	public ItemForgingHammer() {
		super(ProjectZed.modCreativeTab, ProjectZed.assetDir, "forgingHammer");
		this.setMaxDamage(0xff);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean hasContainerItem(ItemStack itemStack) {
		return true;
	}

	/*@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack) {
		*//*final int itemDamage = stack.getItemDamage();
		if (itemDamage >= 256) return false;

		stack.setItemDamage(itemDamage + 1);*//*

		return false;
	}*/

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		// if (stack.attemptDamageItem(1, itemRand)) return new ItemStack(Items.bowl);
		if (stack.attemptDamageItem(1, itemRand)) return null;

		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
		list.add(TextFormatting.GREEN + "Uses left " + TextFormatting.GRAY + (stack.getMaxDamage() - stack.getItemDamage() + 1));
	}

}

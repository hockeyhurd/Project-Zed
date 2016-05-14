/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item.metals;

import com.hockeyhurd.hcorelib.api.item.AbstractHCoreItem;
import com.projectzed.mod.ProjectZed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Class containing code for all urnanium dust stuff.
 * 
 * @author hockeyhurd
 * @version Nov 6, 2014
 */
public class ItemDustUranium extends AbstractHCoreItem {
	
	private final boolean ENRICHED;
	
	/**
	 * @param name
	 * @param assetDir
	 */
	public ItemDustUranium(String name, String assetDir, boolean enriched) {
		super(ProjectZed.modCreativeTab, name, assetDir);
		this.ENRICHED = enriched;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		list.add(this.ENRICHED ? "UF6" : "UO2");
	}
	
}

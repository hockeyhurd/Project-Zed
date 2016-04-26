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
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Class containing code for nuclear fuel rods.
 * 
 * @author hockeyhurd
 * @version Dec 18, 2014
 */
public class ItemFuelRod extends AbstractHCoreItem {

	private boolean isEmpty;
	private String name, assetDir;
	private IIcon[] icons = new IIcon[10];
	
	/**
	 * @param name
	 * @param assetDir
	 */
	public ItemFuelRod(String name, String assetDir, boolean isEmpty) {
		super(ProjectZed.modCreativeTab, name, assetDir);
		this.name = name;
		this.assetDir = assetDir;
		this.isEmpty = isEmpty;
		this.setMaxDamage(10);
		if (!isEmpty) this.maxStackSize = 1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.hockeyhurd.api.item.AbstractItemMetalic#registerIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		if (isEmpty) itemIcon = reg.registerIcon(this.assetDir + name);
		
		// if (!isEmpty) {
		else {
			for (int i = 0; i < icons.length; i++) {
				icons[i] = reg.registerIcon(this.assetDir + name + "_" + i);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#getIconFromDamage(int)
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		if (this.isEmpty || meta >= this.getMaxDamage()) return this.itemIcon;
		else if (meta >= 0 && meta < this.icons.length) return this.icons[this.icons.length - meta - 1];
		else return null;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		
		if (!isEmpty && stack.getItemDamage() < stack.getMaxDamage()) {
			int left = this.icons.length - stack.getItemDamage();
			list.add(EnumChatFormatting.GREEN + "Uses left: " + EnumChatFormatting.WHITE + left);
		}
	}
	
}

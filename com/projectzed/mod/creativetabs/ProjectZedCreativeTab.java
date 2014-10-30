package com.projectzed.mod.creativetabs;

import net.minecraft.item.Item;

import com.hockeyhurd.api.creativetab.AbstractCreativeTab;
import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author hockeyhurd
 * @version Oct 20, 2014
 */
public class ProjectZedCreativeTab extends AbstractCreativeTab {

	/**
	 * @param par1
	 * @param par2
	 */
	public ProjectZedCreativeTab(int par1, String par2) {
		super(par1, par2);
	}
	
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return Item.getItemFromBlock(ProjectZed.industrialFurnace);
	}

}

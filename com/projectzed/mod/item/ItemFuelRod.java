package com.projectzed.mod.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.hockeyhurd.api.item.AbstractItemMetalic;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for nuclear fuel rods.
 * 
 * @author hockeyhurd
 * @version Dec 18, 2014
 */
public class ItemFuelRod extends AbstractItemMetalic {

	private boolean isEmpty;
	private String name, assetDir;
	private IIcon[] icons = new IIcon[10];
	
	/**
	 * @param name
	 * @param assetDir
	 */
	public ItemFuelRod(String name, String assetDir, boolean isEmpty) {
		super(name, assetDir);
		this.name = name;
		this.assetDir = assetDir;
		this.isEmpty = isEmpty;
		this.setMaxDamage(10);
		if (isEmpty) this.setDamage(new ItemStack(this), 10);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.hockeyhurd.api.item.AbstractItemMetalic#registerIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		itemIcon = reg.registerIcon(this.assetDir + name);
		for (int i = 0; i < icons.length; i++) {
			icons[i] = reg.registerIcon(this.assetDir + name + "_" + i);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		if (this.isEmpty) return this.itemIcon;
		else if (meta >= 0 && meta < this.icons.length) return this.icons[this.icons.length - meta - 1];
		else return null;
	}

}

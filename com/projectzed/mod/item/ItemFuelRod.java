package com.projectzed.mod.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
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
		if (!isEmpty) this.maxStackSize = 1;
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

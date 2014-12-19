package com.projectzed.mod.item.metals;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.hockeyhurd.api.item.AbstractItemMetalic;
import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for all urnanium dust stuff.
 * 
 * @author hockeyhurd
 * @version Nov 6, 2014
 */
public class ItemDustUranium extends AbstractItemMetalic {

	private final int ATOMIC_NUMBER;
	
	/**
	 * @param name
	 * @param assetDir
	 */
	public ItemDustUranium(String name, String assetDir, int atomicNum) {
		super(name, assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.ATOMIC_NUMBER = atomicNum;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		list.add("U - " + this.ATOMIC_NUMBER);
		list.add(this.ATOMIC_NUMBER == 235 ? EnumChatFormatting.WHITE + "Stable" : EnumChatFormatting.RED + "Un-Stable");
	}
	
}

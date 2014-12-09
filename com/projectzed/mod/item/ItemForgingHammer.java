package com.projectzed.mod.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.hockeyhurd.api.item.AbstractItemMetalic;
import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for forgingHammer.
 * 
 * @author hockeyhurd
 * @version Dec 9, 2014
 */
public class ItemForgingHammer extends AbstractItemMetalic {

	public ItemForgingHammer() {
		super("forgingHammer", ProjectZed.assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setMaxDamage(256);
	}
	
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		list.add(EnumChatFormatting.GREEN + "Uses left " + EnumChatFormatting.GRAY + (stack.getMaxDamage() - stack.getItemDamage()));
	}

}

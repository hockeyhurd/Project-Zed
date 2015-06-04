/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item.armor;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.hockeyhurd.api.item.armor.AbstractArmor;
import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Main armor class for ZPlated Armoring.
 * 
 * @author hockeyhurd
 * @version Jun 2, 2015
 */
public class ArmorSetZPlated extends AbstractArmor {

	public static final String PATH_MAT = "ZPlated";
	
	/**
	 * @param material armor material to use.
	 * @param renderIndex render index of item.
	 * @param armorType armor type ordinal.
	 */
	public ArmorSetZPlated(ArmorMaterial material, int renderIndex, int armorType) {
		super(material, renderIndex, armorType, ProjectZed.assetDir, PATH_MAT);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#getArmorTexture(net.minecraft.item.ItemStack, net.minecraft.entity.Entity, int, java.lang.String)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity e, int slot, String type) {
		if (stack.toString().contains("leggings")) return ProjectZed.assetDir + PATH_MAT + "_2.png";
		if (stack.toString().contains("Leggings") && stack.getItem() == ProjectZed.zPlatedLeg) return ProjectZed.assetDir + PATH_MAT + "_2.png"; 
		
		return ProjectZed.assetDir + PATH_MAT + "_1.png";
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.ItemArmor#registerIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		itemIcon = reg.registerIcon(ProjectZed.assetDir + getUnlocalizedName().substring(5));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if (armorType == 0) list.add(EnumChatFormatting.DARK_RED + "Ability: Underwater vision and breathing!");
		else if (armorType == 1) list.add(EnumChatFormatting.DARK_RED + "Ability: Protection from fire!");
		else if (armorType == 2) list.add(EnumChatFormatting.DARK_RED + "Ability: Step assist!");
		else if (armorType == 3) list.add(EnumChatFormatting.DARK_RED + "Ability: Protection from fall damage!");
		list.add(EnumChatFormatting.GREEN + "Ability: Flight (when combined)!");
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#onArmorTick(net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack)
	 */
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		super.onArmorTick(world, player, stack);
		
		// handle flight checks:
		if (world.getTotalWorldTime() % 20L == 0 && !player.capabilities.isCreativeMode) {
			boolean flag = true;
			byte counter = 0;
			
			if (player.getCurrentArmor(0) == null || player.getCurrentArmor(0).getItem() != ProjectZed.zPlatedBoot) {
				flag = false;
				counter++;
			}
			
			if (player.getCurrentArmor(1) == null || player.getCurrentArmor(1).getItem() != ProjectZed.zPlatedLeg) {
				flag = false;
				counter++;
			}
			
			if (player.getCurrentArmor(2) == null || player.getCurrentArmor(2).getItem() != ProjectZed.zPlatedChest) {
				flag = false;
				counter++;
			}
			
			if (player.getCurrentArmor(3) == null || player.getCurrentArmor(3).getItem() != ProjectZed.zPlatedHelm) {
				flag = false;
				counter++;
			}
			
			if (counter == 4) return;
			
			if (flag) player.capabilities.allowFlying = true;
			else player.capabilities.allowFlying = false;
		}
		
		// handle individual armor abilities:
		if (player.capabilities.isCreativeMode) return;
		
		if (player.getCurrentArmor(0) != null && player.getCurrentArmor(0).getItem() == ProjectZed.zPlatedBoot) {
			if (!player.isCollidedVertically) player.fallDistance = 0f;
		}
		
		if (player.getCurrentArmor(1) != null && player.getCurrentArmor(1).getItem() == ProjectZed.zPlatedLeg) {
			if (!player.isSneaking()) {
				if (player.stepHeight < 1f) player.stepHeight = 1f;
			}
			
			else {
				if (player.stepHeight > 0.5f) player.stepHeight = 0.5f;
			}
		}
		
		if (player.getCurrentArmor(2) != null && player.getCurrentArmor(2).getItem() == ProjectZed.zPlatedChest) {
			if (player.isBurning()) player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 5, 0));
			// if (player.on)
			player.fireResistance = Integer.MAX_VALUE;
			// player.hurtResistantTime = Integer.MAX_VALUE;
		}
		
		else player.fireResistance = 1;
		
		if (player.getCurrentArmor(3) != null && player.getCurrentArmor(3).getItem() == ProjectZed.zPlatedHelm) {
			if (player.isInWater()) {
				player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 5, 0));
				player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 5, 0));
			}
		}
	}

}

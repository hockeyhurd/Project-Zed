/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item.armor;

import com.hockeyhurd.hcorelib.api.item.armor.AbstractArmor;
import com.hockeyhurd.hcorelib.api.util.NumberFormatter;
import com.hockeyhurd.hcorelib.api.util.enums.EnumArmorType;
import com.projectzed.api.energy.IItemChargeable;
import com.projectzed.mod.ProjectZed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Main armor class for ZPlated Armoring.
 *
 * @author hockeyhurd
 * @version Jun 2, 2015
 */
public class ArmorSetZPlated extends AbstractArmor implements IItemChargeable {

	// energies:
	private int capacity;
	private int chargeRate;
	private ItemStack[] armorInventory;

	/**
	 * @param material    armor material to use.
	 * @param renderIndex render index of item.
	 * @param armorType   armor type ordinal.
	 */
	public ArmorSetZPlated(ArmorMaterial material, int renderIndex, EnumArmorType armorType) {
		// super(material, renderIndex, armorType, ProjectZed.assetDir, "zPlatedArmor", PATH_MAT);
		super(material, renderIndex, armorType, ProjectZed.assetDir, "zPlated");

		// energies stuff:
		capacity = (int) 1e5;
		chargeRate = 1000;

		this.canRepair = false;
		this.setMaxStackSize(1);
		this.setMaxDamage(capacity / chargeRate);
	}
	
	/*@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity e, int slot, String type) {
		if (stack.toString().contains("leggings")) return ProjectZed.assetDir + PATH_MAT + "_2.png";
		if (stack.toString().contains("Leggings") && stack.getItem() == ProjectZed.zPlatedLeg) return ProjectZed.assetDir + PATH_MAT + "_2.png"; 
		
		return ProjectZed.assetDir + PATH_MAT + "_1.png";
	}*/

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4) {
		if (armorType == EnumArmorType.HELMET) list.add(TextFormatting.DARK_RED + "Ability: Underwater vision and breathing!");
		else if (armorType == EnumArmorType.CHEST) list.add(TextFormatting.DARK_RED + "Ability: Protection from fire!");
		else if (armorType == EnumArmorType.LEGGINGS) list.add(TextFormatting.DARK_RED + "Ability: Step assist!");
		else if (armorType == EnumArmorType.BOOTS) list.add(TextFormatting.DARK_RED + "Ability: Protection from fall damage!");
		list.add(TextFormatting.GREEN + "Ability: Flight (when combined)!");

		list.add(TextFormatting.GREEN + "Stored: " + TextFormatting.WHITE + NumberFormatter.format(getStored(stack)) + " McU");
		list.add(TextFormatting.GREEN + "Capacity: " + TextFormatting.WHITE + NumberFormatter.format(this.capacity) + " McU");
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		super.onArmorTick(world, player, stack);

		// handle flight checks:
		if (armorInventory == null || (world.getTotalWorldTime() % 20L == 0 && !player.capabilities.isCreativeMode)) {
			boolean flag = true;
			int counter = 0;

			armorInventory = player.inventory.armorInventory;

			if (armorInventory[0] == null || armorInventory[0].getItem() != ProjectZed.zPlatedBoot) {
				flag = false;
				counter++;
			}

			if (armorInventory[1] == null || armorInventory[1].getItem() != ProjectZed.zPlatedLeg) {
				flag = false;
				counter++;
			}

			if (armorInventory[2] == null || armorInventory[2].getItem() != ProjectZed.zPlatedChest) {
				flag = false;
				counter++;
			}

			if (armorInventory[3] == null || armorInventory[3].getItem() != ProjectZed.zPlatedHelm) {
				flag = false;
				counter++;
			}

			if (counter == 4) return;

			if (flag) player.capabilities.allowFlying = true;
			else player.capabilities.allowFlying = false;
		}

		// handle individual armor abilities:
		if (player.capabilities.isCreativeMode) return;

		if (armorInventory[0] != null && armorInventory[0].getItem() == ProjectZed.zPlatedBoot) {
			if (!player.isCollidedVertically) player.fallDistance = 0f;
		}

		if (armorInventory[1] != null && armorInventory[1].getItem() == ProjectZed.zPlatedLeg) {
			if (!player.isSneaking()) {
				if (player.stepHeight < 1f) player.stepHeight = 1f;
			}

			else {
				if (player.stepHeight > 0.5f) player.stepHeight = 0.5f;
			}
		}

		if (armorInventory[2] != null && armorInventory[2].getItem() == ProjectZed.zPlatedChest) {
			if (player.isBurning()) player.addPotionEffect(new PotionEffect(Potion.getPotionById(12), 5, 0)); // Fire resistance.
			// if (player.on)
			player.fireResistance = Integer.MAX_VALUE;
			// player.hurtResistantTime = Integer.MAX_VALUE;
		}

		else player.fireResistance = 1;

		if (armorInventory[3] != null && armorInventory[3].getItem() == ProjectZed.zPlatedHelm) {
			if (player.isInWater()) {
				player.addPotionEffect(new PotionEffect(Potion.getPotionById(13), 300, 0)); // Water breathing.
				// player.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 5, 0)); // Night vision.
			}

			player.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 300, 0)); // Night vision.
		}
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public int getStored(ItemStack stack) {
		if (stack == null || stack.getItem() != this) return 0;
		return capacity - (stack.getItemDamage() * chargeRate);
	}

	@Override
	public void setStored(ItemStack stack, int amount) {
		if (stack == null || stack.getItem() != this || amount < 0 || amount > this.capacity) return;

		// int progress = (int) Math.floor(amount / (double) capacity * chargeRate);
		int progress = (capacity - amount) / chargeRate;
		// ProjectZed.logHelper.info("Progress:", progress);

		stack.setItemDamage(/*capacity -*/ progress);
	}

	@Override
	public int addPower(ItemStack stack, int amount, boolean simulate) {
		if (stack == null || stack.getItem() != this || amount == 0) return 0;

		amount = Math.max(amount, chargeRate);
		int current = getStored(stack);
		int ret;

		if (current + amount >= 0 && current + amount <= capacity) {
			ret = current + amount;

			if (!simulate) setStored(stack, ret);
		}

		else {
			amount = capacity - current;
			ret = current + amount;

			if (!simulate) setStored(stack, ret);
		}

		return ret;
	}

	@Override
	public int subtractPower(ItemStack stack, int amount, boolean simulate) {
		if (stack == null || stack.getItem() != this || amount == 0) return 0;

		int current = getStored(stack);
		if (current - amount >= 0 && current - amount <= capacity) {
			int ret = current - amount;

			if (!simulate) setStored(stack, ret);

			return ret;
		}

		return 0;
	}

	@Override
	public int getChargeRate() {
		return chargeRate;
	}
}

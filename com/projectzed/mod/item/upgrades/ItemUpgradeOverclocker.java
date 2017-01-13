/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.item.upgrades;

import com.projectzed.api.item.AbstractItemUpgrade;
import com.projectzed.api.tileentity.digger.AbstractTileEntityDigger;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialEnergizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Item class for overclockerUpgrade.
 *
 * @author hockeyhurd
 * @version 7/4/2015.
 */
public class ItemUpgradeOverclocker extends AbstractItemUpgrade {

	/**
	 * @param name name of upgrade component.
	 */
	public ItemUpgradeOverclocker(String name) {
		super(name);
		// this.maxStackSize = 0xc;
		setMaxStackSize(0x10);
	}

	@Override
	public boolean effectOnMachines(AbstractTileEntityMachine te, boolean simulate) {
		return !(te instanceof TileEntityIndustrialEnergizer);
	}

	@Override
	public boolean effectOnGenerators(AbstractTileEntityGenerator te, boolean simulate) {
		return true;
	}

	@Override
	public boolean effectOnDiggers(AbstractTileEntityDigger te, boolean simulate) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addInfo(ItemStack stack, EntityPlayer player, List list) {
		list.add("Decreases machine processing time by " + (ProjectZed.configHandler.getEffRateModifier() * 100f) + "%.");
		list.add("Increases power usage by " + (ProjectZed.configHandler.getBurnRateModifier() * 100f) + "%.");
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected int addShiftInfo(ItemStack stack, EntityPlayer player, List list, boolean simulate) {
		return 0;
	}

}

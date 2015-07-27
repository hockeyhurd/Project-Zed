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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Item class for silkTouchUpgrade.
 *
 * @author hockeyhurd
 * @version 7/4/2015.
 */
public class ItemUpgradeSilkTouch extends AbstractItemUpgrade {

	/**
	 * @param name name of upgrade component.
	 */
	public ItemUpgradeSilkTouch(String name) {
		super(name);
		this.maxStackSize = 1;
	}

	@Override
	public float energyBurnRateRelativeToSize(int stackSize, float originalRate) {
		return originalRate * 2f;
	}

	@Override
	public float operationSpeedRelativeToSize(int stackSize, float originalTickTime) {
		return originalTickTime;
	}

	@Override
	public boolean effectOnMachines(AbstractTileEntityMachine te, boolean simulate) {
		return false;
	}

	@Override
	public boolean effectOnGenerators(AbstractTileEntityGenerator te, boolean simulate) {
		return false;
	}

	@Override
	public boolean effectOnDiggers(AbstractTileEntityDigger te, boolean simulate) {
		if (!simulate) te.setSilkTouch(true);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addInfo(ItemStack stack, EntityPlayer player, List list) {
		list.add("Increases power usage by 2x original burn rate.");
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected int addShiftInfo(ItemStack stack, EntityPlayer player, List list, boolean simulate) {
		return 0;
	}


}

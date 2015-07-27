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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Class for efficiencyUpgrade.
 *
 * @author hockeyhurd
 * @version 7/24/2015.
 */
public class ItemUpgradeEfficiency extends AbstractItemUpgrade {

	/**
	 * @param name name of upgrade component.
	 */
	public ItemUpgradeEfficiency(String name) {
		super(name);
		this.setMaxStackSize(0x4);
	}

	@Override
	public float energyBurnRateRelativeToSize(int stackSize, float originalRate) {
		if (stackSize == 0) return originalRate;
		if (stackSize == 1) return originalRate * 2f;

		for (int i = 0; i < stackSize; i++) {
			originalRate *= 2f;
		}

		return originalRate;
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
		if (!simulate) te.setWaitTime(te.getWaitTime() / 2);
		return true;
	}

	@Override
	protected void addInfo(ItemStack stack, EntityPlayer player, List list) {
		list.add("Doubles digging speed!");
	}

	@Override
	protected int addShiftInfo(ItemStack stack, EntityPlayer player, List list, boolean simulate) {
		return 0x0;
	}

}

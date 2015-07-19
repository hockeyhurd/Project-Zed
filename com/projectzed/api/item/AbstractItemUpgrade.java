/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.api.item;

import com.hockeyhurd.api.item.AbstractItemMetalic;
import com.projectzed.api.tileentity.digger.AbstractTileEntityDigger;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Abstract class providing basis for item upgrade component(s).
 *
 * @author hockeyhurd
 * @version 6/29/2015.
 */
public abstract class AbstractItemUpgrade extends AbstractItemMetalic implements IItemUpgradeComponent {

	protected float burnRateModifier;
	protected float effRateModifier;

	/**
	 * @param name name of upgrade component.
	 */
	public AbstractItemUpgrade(String name) {
		super(name, ProjectZed.assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setMaxStackSize(0x10); // 16

		this.burnRateModifier = ProjectZed.configHandler.getBurnRateModifier();
		this.effRateModifier = ProjectZed.configHandler.getEffRateModifier();
	}

	@Override
	public int maxSize() {
		return this.maxStackSize;
	}

	@Override
	public float energyBurnRateRelativeToSize(int stackSize, float originalRate) {
		return stackSize > 0 ? (float) ((10f * Math.pow(burnRateModifier, stackSize)) + originalRate) : originalRate;
	}

	@Override
	public float operationSpeedRelativeToSize(int stackSize, float originalTickTime) {
		float ret = stackSize > 0 ? (float) (Math.pow(effRateModifier, stackSize) * originalTickTime) : 1.0f;

		if (ret < 1f) ret = 1f; // clamp minimum number of operations is 1 per tick.

		return ret;
	}

	@Override
	public abstract boolean effectOnMachines(AbstractTileEntityMachine te, boolean simulate);

	@Override
	public abstract boolean effectOnGenerators(AbstractTileEntityGenerator te, boolean simulate);

	@Override
	public abstract boolean effectOnDiggers(AbstractTileEntityDigger te, boolean simulate);

	@Override
	@SideOnly(Side.CLIENT)
	public abstract void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4);

}
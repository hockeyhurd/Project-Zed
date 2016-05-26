/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.api.item;

import com.hockeyhurd.hcorelib.api.item.AbstractHCoreItem;
import com.projectzed.api.tileentity.digger.AbstractTileEntityDigger;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * Abstract class providing basis for item upgrade component(s).
 *
 * @author hockeyhurd
 * @version 6/29/2015.
 */
public abstract class AbstractItemUpgrade extends AbstractHCoreItem implements IItemUpgradeComponent {

	protected float burnRateModifier;
	protected float effRateModifier;

	/**
	 * @param name name of upgrade component.
	 */
	public AbstractItemUpgrade(String name) {
		super(ProjectZed.modCreativeTab, ProjectZed.assetDir, name);
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
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		addInfo(stack, player, list);

		if (addShiftInfo(stack, player, list, false) > 0)
			list.add(TextFormatting.GRAY + "<" + TextFormatting.GREEN + "shift for more info" + TextFormatting.GRAY + ">");

		if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) addShiftInfo(stack, player, list, false);
	}

	@SideOnly(Side.CLIENT)
	protected abstract void addInfo(ItemStack stack, EntityPlayer player, List list);

	@SideOnly(Side.CLIENT)
	protected abstract int addShiftInfo(ItemStack stack, EntityPlayer player, List list, boolean simulate);

}

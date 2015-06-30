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
import com.projectzed.mod.ProjectZed;

/**
 * Abstract class providing basis for item upgrade component(s).
 *
 * @author hockeyhurd
 * @version 6/29/2015.
 */
public class AbstractItemUpgrade extends AbstractItemMetalic implements IUpgradeComponent {

	/**
	 * @param name name of upgrade component.
	 */
	public AbstractItemUpgrade(String name) {
		super(name, ProjectZed.assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setMaxStackSize(0x10); // 16
	}

	@Override
	public int maxSize() {
		return this.maxStackSize;
	}

	@Override
	public float energyBurnRateRelativeToSize(int stackSize, float originalRate) {
		return (float) ((10f * Math.pow(ProjectZed.configHandler.getBurnRateModifier(), stackSize)) + originalRate);
	}

	@Override
	public float operationSpeedRelativeToSize(int stackSize, float originalTickTime) {
		float ret = (float) (Math.pow(ProjectZed.configHandler.getEffRateModifier(), stackSize) + originalTickTime);

		if (ret < 1f) ret = 1f; // clamp minium number of operations is 1 per tick.

		return ret;
	}
}

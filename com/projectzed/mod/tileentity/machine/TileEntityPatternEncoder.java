/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.tileentity.machine;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.Sound;
import net.minecraft.item.ItemStack;

/**
 * TileEntity class for pattern encoder.
 *
 * @author hockeyhurd
 * @version 4/30/2016.
 */
public class TileEntityPatternEncoder extends AbstractTileEntityMachine {

	public static final int CRAFTING_MATRIX_SIZE = 3;

	public TileEntityPatternEncoder() {
		super("patternEncoder");
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public int getSizeUpgradeSlots() {
		return 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0x40;
	}

	@Override
	protected void initContentsArray() {
	}

	@Override
	protected void initSlotsArray() {
		slots = new ItemStack[3 * 3 + 1];
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[0];
	}

	@Override
	protected boolean canSmelt() {
		return false;
	}

	@Override
	public void smeltItem() {
	}

	@Override
	public Sound getSound() {
		return null;
	}

}

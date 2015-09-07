/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.container;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialPlanter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Container class for industrial planter.
 *
 * @author hockeyhurd
 * @version 9/1/2015.
 */
public class ContainerPlanter extends ContainerMachine {

	private final TileEntityIndustrialPlanter te2;

	/**
	 * @param inv = inventory of player as reference.
	 * @param te  = tile entity to append to as reference.
	 */
	public ContainerPlanter(InventoryPlayer inv, AbstractTileEntityMachine te) {
		super(inv, te);
		this.te2 = (TileEntityIndustrialPlanter) te;
	}

	@Override
	protected void addSlots(InventoryPlayer inv, AbstractTileEntityMachine te) {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				this.addSlotToContainer(new Slot(te, x + y * 3, 67 + x * 18, 10 + y * 18));
			}
		}

		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(te, 9 + x + y * 9, 8 + x * 18, 17 + 53 + y * 18));
			}
		}

		addUpgradeInventorySlots(te);
		addPlayerInventorySlots(inv);
	}

	@Override
	protected void addPlayerInventorySlots(InventoryPlayer inv) {
		// Adds the player inventory to furnace's gui.
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				// ProjectZed.logHelper.info(x + y * 9, 47 + 84 + y * 18);
				this.addSlotToContainer(new Slot(inv, x + y * 9 + 9, 8 + x * 18, 0x30 + 84 + y * 18));
			}
		}

		// Adds the player hotbar slots to the gui.
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 0x30 + 142)); // 198
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		return super.transferStackInSlot(player, index); // TODO: This must be changed.
	}

}

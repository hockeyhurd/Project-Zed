/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCentrifuge;

/**
 * Class containing, container code for industrial centrifuge.
 * 
 * @author hockeyhurd
 * @version Dec 21, 2014
 */
public class ContainerCentrifuge extends ContainerMachine {

	private int waterStored;

	/**
	 * @see com.projectzed.mod.container.ContainerMachine#ContainerMachine
	 * @param inv
	 * @param te
	 */
	public ContainerCentrifuge(InventoryPlayer inv, AbstractTileEntityMachine te) {
		super(inv, te);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.container.ContainerMachine#detectAndSendChanges()
	 */
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		this.waterStored = ((TileEntityIndustrialCentrifuge) this.te).getTank().getFluidAmount();
	}

}

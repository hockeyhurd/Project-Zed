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

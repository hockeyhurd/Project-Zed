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
	 * @param inv
	 * @param te
	 */
	public ContainerCentrifuge(InventoryPlayer inv, TileEntityIndustrialCentrifuge te) {
		super(inv, te);
		addSlots(inv, te);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.container.ContainerMachine#addSlots(net.minecraft.entity.player.InventoryPlayer, com.projectzed.api.tileentity.machine.AbstractTileEntityMachine)
	 */
	@Override
	protected void addSlots(InventoryPlayer inv, AbstractTileEntityMachine te) {
		// Add 'crafting' slots to container.
		this.addSlotToContainer(new Slot(te, 0, 29, 21));
		this.addSlotToContainer(new SlotFurnace(inv.player, te, 1, 121, 21));
		this.addSlotToContainer(new Slot(te, 2, 54, 21));

		// Adds the player inventory to furnace's gui.
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(inv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		// Adds the player hotbar slots to the gui.
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142)); // 198
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.container.ContainerMachine#detectAndSendChanges()
	 */
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		this.waterStored = ((TileEntityIndustrialCentrifuge) this.te).getWaterInTank();
	}

}

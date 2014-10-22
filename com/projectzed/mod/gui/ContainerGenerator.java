package com.projectzed.mod.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class used as generic container for most/all generators.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public class ContainerGenerator extends Container {

	private AbstractTileEntityGenerator te;
	private int stored;

	public ContainerGenerator(InventoryPlayer inv, AbstractTileEntityGenerator te) {
		this.te = te;
		addSlots(inv, te);
	}

	/**
	 * Adds all slots, player and container.
	 * @param inv = inventory.
	 * @param te = tile entity object.
	 */
	private void addSlots(InventoryPlayer inv, AbstractTileEntityGenerator te) {
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
	 * 
	 * @see net.minecraft.inventory.Container#canInteractWith(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		this.stored = this.te.getEnergyStored();
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int newVal) {
		this.te.setEnergyStored(newVal);
	}

}

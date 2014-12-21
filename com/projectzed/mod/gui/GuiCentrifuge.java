package com.projectzed.mod.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCentrifuge;

/**
 * Class containing gui code for industrial centrifuge. 
 * 
 * @author hockeyhurd
 * @version Dec 21, 2014
 */
public class GuiCentrifuge extends GuiMachine {

	private int waterStored;
	private TileEntityIndustrialCentrifuge te2;
	
	/**
	 * @param inv
	 * @param te
	 */
	public GuiCentrifuge(InventoryPlayer inv, TileEntityIndustrialCentrifuge te) {
		super(inv, te);
		this.texture = new ResourceLocation("projectzed", "textures/gui/GuiCentrifuge.png");
		this.te2 = te;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiMachine#drawGuiContainerForegroundLayer(int, int)
	 */
	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiMachine#drawGuiContainerBackgroundLayer(float, int, int)
	 */
	@Override
	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		super.drawGuiContainerBackgroundLayer(f, x, y);
		
		float progressWater = (float) ((float) this.te2.getWaterInTank() / (float) this.te2.getMaxWaterStorage()) * 40f;
		progressWater = 40 - progressWater;
		int v = 0 - (int) progressWater;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 17, 200, v, 16, guiTop + 3);
	}

}

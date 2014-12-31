package com.projectzed.mod.gui;

import java.util.ArrayList;
import java.util.List;

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
	private List<String> list;
	
	/**
	 * @param inv
	 * @param te
	 */
	public GuiCentrifuge(InventoryPlayer inv, TileEntityIndustrialCentrifuge te) {
		super(inv, te);
		this.texture = new ResourceLocation("projectzed", "textures/gui/GuiCentrifuge.png");
		this.te2 = te;
		this.list = new ArrayList<String>();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiMachine#drawGuiContainerForegroundLayer(int, int)
	 */
	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);
		
		if (this.mouseX >= guiLeft + 7 && this.mouseX <= guiLeft + 7 + 16 && this.mouseY >= guiTop + 17 && this.mouseY <= guiTop + 17 + 41) {
			// System.out.println("True");
			if (list.size() == 0) list.add(this.te2.getWaterInTank() + " (mb) / " + this.te2.getMaxWaterStorage() + " (mb)");
			else list.set(0, this.te2.getWaterInTank() + " (mb) / " + this.te2.getMaxWaterStorage() + " (mb)");
			this.drawHoveringText(list, mouseX - 125, mouseY - 25, this.fontRendererObj);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiMachine#drawGuiContainerBackgroundLayer(float, int, int)
	 */
	@Override
	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		super.drawGuiContainerBackgroundLayer(f, x, y);
		
		float progressWater = (float) ((float) this.te2.getWaterInTank() / (float) this.te2.getMaxWaterStorage()) * 41f;
		progressWater = 41 - progressWater;
		int v = 0 - (int) progressWater;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 17, 200, v, 16, 41);
	}
	
}

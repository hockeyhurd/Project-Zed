package com.projectzed.mod.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.mod.gui.component.FluidLabel;
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
	 * @see
	 * com.projectzed.mod.gui.GuiMachine#drawGuiContainerForegroundLayer(int,
	 * int)
	 */
	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.mod.gui.GuiMachine#drawGuiContainerBackgroundLayer(float,
	 * int, int)
	 */
	@Override
	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		super.drawGuiContainerBackgroundLayer(f, x, y);

		float progressWater = (float) ((float) this.te2.getWaterInTank() / (float) this.te2.getMaxWaterStorage()) * 41f;
		progressWater = 41 - progressWater;
		int v = 0 - (int) progressWater;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 17, 200, v, 16, 41);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiMachine#initGui()
	 */
	@Override
	public void initGui() {
		super.initGui();

		this.labelList.add(new FluidLabel<Integer>(new Vector4Helper<Integer>(guiLeft + 7, guiTop + 17, 0), new Vector4Helper<Integer>(16, 41, 0),
				this.te2.getWaterInTank(), this.te2.getMaxWaterStorage()));
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiMachine#updateScreen()
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();

		if (getComponents() != null && getComponents().size() > 1) {
			getComponents().get(1).update(new Vector4Helper<Integer>(this.mouseX, this.mouseY, 0), this.te2.getWaterInTank(),
					this.te2.getMaxWaterStorage());
		}
	}

}

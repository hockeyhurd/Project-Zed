/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
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

	private Vector4Helper<Integer> pos2, minMax2;

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

		float progressWater = (float) ((float) this.te2.getTank().getFluidAmount() / (float) this.te2.getTank().getCapacity()) * 39f;
		progressWater = 39f - progressWater;
		int v = 0 - (int) progressWater;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 17, 200, v, 16, 39);
		
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 17, 216, 0, 16, 39);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiMachine#initGui()
	 */
	@Override
	public void initGui() {
		super.initGui();

		this.pos2 = new Vector4Helper<Integer>(guiLeft + 7, guiTop + 17, 0);
		this.minMax2 = new Vector4Helper<Integer>(guiLeft + 7 + 16, guiTop + 17 + 41, 0);

		this.labelList.add(new FluidLabel<Integer>(this.pos2, this.minMax2, this.te2.getTank().getFluidAmount(), this.te2.getTank().getCapacity()));
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiMachine#updateScreen()
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();

		this.pos2.x = guiLeft + 7;
		this.pos2.y = guiTop + 17;

		this.minMax2.x = guiLeft + 7 + 16;
		this.minMax2.y = guiTop + 17 + 41;

		if (getComponents() != null && getComponents().size() > 1) {
			getComponents().get(1).update(this.mouseVec, this.pos2, this.minMax2, this.te2.getTank().getFluidAmount(), this.te2.getTank().getCapacity());
		}
	}

}

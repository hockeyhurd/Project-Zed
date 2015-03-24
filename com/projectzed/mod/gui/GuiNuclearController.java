/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui;

import static com.hockeyhurd.api.util.NumberFormatter.format;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.mod.gui.component.FluidLabel;
import com.projectzed.mod.tileentity.generator.TileEntityNuclearController;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Gui class specific to nuclear controllers.
 * 
 * @author hockeyhurd
 * @version Mar 20, 2015
 */
@SideOnly(Side.CLIENT)
public class GuiNuclearController extends GuiGenerator {

	// TODO: implement these in both controller TE classes and sync to here!
	// private int storedFuel;
	// private int storedCoolant;
	
	/**
	 * @param inv player's inventory.
	 * @param te tileentity object to reference.
	 */
	public GuiNuclearController(InventoryPlayer inv, TileEntityNuclearController te) {
		super(inv, te);
		texture = new ResourceLocation("projectzed", "textures/gui/GuiNuclearController.png");
	}

	public TileEntityNuclearController getTE() {
		return (TileEntityNuclearController) te;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiGenerator#initGui()
	 */
	@Override
	public void initGui() {
		super.initGui();
		
		// this.labelList.add(new FluidLabel<Integer>(new Vector4Helper<Integer>(guiLeft + 7, guiTop + 17, 0), new Vector4Helper<Integer>(
		//		guiLeft + 7 + 16, guiTop + 17 + 41, 0), storedCoolant, getTE().getCoolantFluidStored()));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiGenerator#drawGuiContainerForegroundLayer(int, int)
	 */
	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		String name = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName(), new Object[0]);

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);

		/*String genSize = "Generation rate: " + format(genRate) + " McU/t";
		this.fontRendererObj.drawString(I18n.format(genSize, new Object[0]), this.xSize / 2 - this.fontRendererObj.getStringWidth(genSize) / 2,
				this.ySize - 116, 4210752);*/
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiGenerator#drawGuiContainerBackgroundLayer(float, int, int)
	 */
	@Override
	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		super.drawGuiContainerBackgroundLayer(f, x, y);
		
		// float progressWater = (float) ((float) this.te2.getTank().getFluidAmount() / (float) this.te2.getTank().getCapacity()) * 39f;
		float progressCoolant = 39f;
		progressCoolant = 39f - progressCoolant;
		int v = 0 - (int) progressCoolant;
		
		// water:
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 17, 176, v, 16, 39);
		this.drawTexturedModalRect(guiLeft + 8, guiTop + 17, 176 + 16, 0, 16, 39);
		
		// heat:
		this.drawTexturedModalRect(guiLeft + 7 + 17, guiTop + 17, 176 + 32, v, 16, 39);
		this.drawTexturedModalRect(guiLeft + 8 + 17, guiTop + 17, 176 + 48, 0, 16, 39);
		
		// fuel stored:
		this.drawTexturedModalRect(guiLeft + 7 + 17 + 17, guiTop + 17, 176, v + 41, 16, 39);
		this.drawTexturedModalRect(guiLeft + 8 + 17 + 17, guiTop + 17, 176 + 16, 41, 16, 39);
	}
	
}

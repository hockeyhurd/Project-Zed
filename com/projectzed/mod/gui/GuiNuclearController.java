/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui;

import com.hockeyhurd.api.math.Vector2;
import com.projectzed.mod.gui.component.HeatLabel;
import com.projectzed.mod.tileentity.generator.TileEntityNuclearController;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

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
	private static final float DATA_BAR_HEIGHT = 39.0f;

	private final TileEntityNuclearController te2;
	private Vector2<Integer> pos2, minMax2;
	private int lastStored;
	
	/**
	 * @param inv player's inventory.
	 * @param te tileentity object to reference.
	 */
	public GuiNuclearController(InventoryPlayer inv, TileEntityNuclearController te) {
		super(inv, te);
		this.te2 = te;
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
		
		// this.labelList.add(new FluidLabel<Integer>(new Vector4<Integer>(guiLeft + 7, guiTop + 17, 0), new Vector4<Integer>(
		//		guiLeft + 7 + 16, guiTop + 17 + 41, 0), storedCoolant, getTE().getCoolantFluidStored()));

		pos2 = new Vector2<Integer>(guiLeft + 7 + 0x10, guiTop + 17);
		minMax2 = new Vector2<Integer>(guiLeft + 7 + 0x20, guiTop + 17 + 41);

		this.labelList.add(new HeatLabel<Integer>(pos2, minMax2, te2.getHeatLogic().getHeat(), te2.getHeatLogic().getMaxHeat()));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiGenerator#drawGuiContainerForegroundLayer(int, int)
	 */
	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		String name = !this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName(), new Object[0]);

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
		float progressCoolant = DATA_BAR_HEIGHT;
		progressCoolant = DATA_BAR_HEIGHT - progressCoolant;
		int v = 0 - (int) progressCoolant;

		final float currentHeat = ((TileEntityNuclearController) this.te).getHeatLogic().getHeat();
		final float maxHeat = ((TileEntityNuclearController) this.te).getHeatLogic().getMaxHeat();
		float progressHeat = currentHeat / maxHeat * DATA_BAR_HEIGHT;
		// ProjectZed.logHelper.info("progressHeat:", progressHeat, currentHeat / maxHeat);
		progressHeat = DATA_BAR_HEIGHT - progressHeat;

		// Clamp values accordingly:
		if (progressHeat < 0.0f) progressHeat = 0.0f;
		// if (progressHeat > 1.0f) progressHeat = 1.0f;
		int vh = 0 - (int) progressHeat;

		// water:
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 17, 176, v, 16, 39);
		this.drawTexturedModalRect(guiLeft + 8, guiTop + 17, 176 + 16, 0, 16, 39);
		
		// heat:
		// this.drawTexturedModalRect(guiLeft + 7 + 17, guiTop + 17, 176 + 32, v, 16, 39);
		this.drawTexturedModalRect(guiLeft + 7 + 17, guiTop + 17, 176 + 32, vh, 16, 39);
		this.drawTexturedModalRect(guiLeft + 8 + 17, guiTop + 17, 176 + 48, 0, 16, 39);
		// this.drawTexturedModalRect(guiLeft + 8 + 17, guiTop + 17, 176 + 48, (int) progressHeat, 16, 39);

		// fuel stored:
		this.drawTexturedModalRect(guiLeft + 7 + 17 + 17, guiTop + 17, 176, v + 41, 16, 39);
		this.drawTexturedModalRect(guiLeft + 8 + 17 + 17, guiTop + 17, 176 + 16, 41, 16, 39);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateScreen() {
		super.updateScreen();

		pos2.x = guiLeft + 7 + 0x10;
		pos2.y = guiTop + 17;

		minMax2.x = guiLeft + 7 + 0x20;
		minMax2.y = guiTop + 17 + 41;

		genRate = te.getEnergyStored() - lastStored;

		if (getComponents() != null && getComponents().size() > 1) {
			getComponents().get(1).update(mouseVec, pos2, minMax2, new Integer[] { te2.getHeatLogic().getHeat(), te2.getHeatLogic().getMaxHeat() } );
		}

		lastStored = te.getEnergyStored();
	}

}

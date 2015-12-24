/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 */

package com.projectzed.mod.gui;

import com.hockeyhurd.api.math.Vector2;
import com.projectzed.mod.container.ContainerGenerator;
import com.projectzed.mod.gui.component.FluidLabel;
import com.projectzed.mod.tileentity.generator.TileEntityLavaGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Gui class for TileEntityLavaGenerator.
 *
 * @author hockeyhurd
 * @version 7/1/2015.
 */
@SideOnly(Side.CLIENT)
public class GuiLavaGen extends GuiGenerator {

	private Vector2<Integer> pos2, minMax2;

	/**
	 * @param inv
	 * @param te
	 */
	public GuiLavaGen(InventoryPlayer inv, TileEntityLavaGenerator te) {
		super(new ContainerGenerator(inv, te, 0, 0x20), inv, te);
		this.yOffset = 0x20;

		this.xSize += xOffset;
		this.ySize += yOffset;

		texture = new ResourceLocation("projectzed", "textures/gui/GuiLavaGenerator.png");
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		super.drawGuiContainerBackgroundLayer(f, x, y);

		float progressLava =
				((float) ((TileEntityLavaGenerator) te).getTank().getFluidAmount() / (float) ((TileEntityLavaGenerator) te).getTank().getCapacity())
						* 59f;
		progressLava = 59f - progressLava;
		int v = 0 - (int) progressLava;

		this.drawTexturedModalRect(guiLeft + 7, guiTop + 17, 192 - 0x10, v, 0x10, 59);
		this.drawTexturedModalRect(guiLeft + 8, guiTop + 17, 192, 0, 16, 59);
	}

	@Override
	public void initGui() {
		super.initGui();

		this.pos2 = new Vector2<Integer>(guiLeft + 7, guiTop + 17);
		this.minMax2 = new Vector2<Integer>(guiLeft + 7 + 16, guiTop + 17 + 61);

		this.labelList.add(new FluidLabel<Integer>(pos2, minMax2, ((TileEntityLavaGenerator) te).getTank().getFluidAmount(),
				((TileEntityLavaGenerator) te).getTank().getCapacity()));
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		this.pos2.x = guiLeft + 7;
		this.pos2.y = guiTop + 17;

		this.minMax2.x = guiLeft + 7 + 16;
		this.minMax2.y = guiTop + 17 + 61;

		if (getComponents() != null && getComponents().size() > 1) {
			getComponents().get(1).update(mouseVec, pos2, minMax2, new Integer[] { ((TileEntityLavaGenerator) te).getTank().getFluidAmount(),
					((TileEntityLavaGenerator) te).getTank().getCapacity()
			});
		}
	}

}

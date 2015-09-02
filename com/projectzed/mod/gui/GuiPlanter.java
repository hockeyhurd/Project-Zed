/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.gui;

import com.projectzed.mod.container.ContainerPlanter;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialPlanter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * @author hockeyhurd
 * @version 9/1/2015.
 */
@SideOnly(Side.CLIENT)
public class GuiPlanter extends GuiMachine {

	/**
	 * @param inv
	 * @param te
	 */
	public GuiPlanter(InventoryPlayer inv, TileEntityIndustrialPlanter te) {
		super(new ContainerPlanter(inv, te), inv, te);
		this.ySize = 213;

		this.texture = new ResourceLocation("projectzed", "textures/gui/GuiPlanter.png");
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

		int xStart = (width - xSize) / 2;
		int yStart = (height - ySize) / 2;

		this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize - upgradeXOffset, ySize);

		float progress = ((float) this.te.getEnergyStored() / (float) this.te.getMaxStorage()) * 160f;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 61 + 0x30, 0, 170 + 0x30, (int) progress, 17);

		if (this.te.getSizeInvenotry() > 1) {
			/*int i1 = 0;
			if (this.te.isPoweredOn() && this.te.cookTime > 0) {
				i1 = this.te.getCookProgressScaled(24);
				// ProjectZed.logHelper.info(i1);
				this.drawTexturedModalRect(guiLeft + 78, guiTop + 21, 176, 14, i1 + 1, 16);
			}*/

			upgradePanel.renderContainer(f, x, y);
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		this.pos.y = guiTop + 61 + 0x30;
		this.minMax.y = guiTop + 61 + 17 + 0x30;
	}
}

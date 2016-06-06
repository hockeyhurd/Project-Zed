/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui.component;

import com.hockeyhurd.hcorelib.api.client.gui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * Class containing code for custom clear button to be used primarily with Fabrication table.
 * 
 * @author hockeyhurd
 * @version Dec 31, 2014
 */
@SideOnly(Side.CLIENT)
public class GuiClearButton extends GuiButton {

	protected final ResourceLocation TEXTURE = new ResourceLocation("projectzed", "textures/gui/clearButton.png");;
	protected final float PIXEL;

	/**
	 * @param id
	 * @param x
	 * @param y
	 * @param text
	 */
	public GuiClearButton(int id, int x, int y, String text) {
		super(id, x, y, text);
		this.width = 12;
		this.height = 12;
		this.PIXEL = 1f / 12f;
	}

	/**
	 * @param id
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param text
	 */
	public GuiClearButton(int id, int x, int y, int width, int height, String text) {
		super(id, x, y, width, height, text);
		this.width = 12;
		this.height = 12;
		this.PIXEL = 1f / 12f;
	}
	
	@Override
	public void drawButton(Minecraft minecraft, int x, int y) {
			if (this.visible) {
				FontRenderer fontRenderer = minecraft.fontRendererObj;

				GuiHelper.simpleRenderGui(TEXTURE, GuiHelper.DEFAULT_COL, xPosition, yPosition, 0, 0, width, height);

				/*	this.TESS.addVertexWithUV(xPosition, yPosition, 0, 0, 0);// bottom left texture
					this.TESS.addVertexWithUV(xPosition, yPosition + height, 0, 0, calc);// top left
					this.TESS.addVertexWithUV(xPosition + width, yPosition + height, 0, calc, calc);// top right
					this.TESS.addVertexWithUV(xPosition + width, yPosition, 0, calc, 0);// bottom right*/

				mouseDragged(minecraft, x, y);
				int j = 0xe0e0e0;

				if (packedFGColour != 0) j = packedFGColour;
				else if (!this.enabled) j = 0xa0a0a0;
				else if (this.hovered) j = 0xffffa0;

				// this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xffffffff);
				this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
		}
	}

}

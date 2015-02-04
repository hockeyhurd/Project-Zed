/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

/**
 * Code for buttons to handle inventory sorting.
 * 
 * @author hockeyhurd
 * @version Jan 16, 2015
 */
public class GuiSortButton extends GuiButton {

	protected final Tessellator TESS;
	protected final ResourceLocation TEXTURE; 
	protected final float PIXEL;
	protected float calc;
	protected final String NAME;
	
	/**
	 * @param id
	 * @param x
	 * @param y
	 * @param name
	 */
	public GuiSortButton(int id, int x, int y, String name) {
		super(id, x, y, "");
		this.width = 16;
		this.height = 16;
		this.PIXEL = 1f / 16f;
		this.NAME = name;
		
		this.TEXTURE = new ResourceLocation("projectzed", "textures/gui/" + name + "Button.png");
		this.TESS = Tessellator.instance;
	}

	/**
	 * @param id
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param name
	 */
	public GuiSortButton(int id, int x, int y, int width, int height, String name) {
		super(id, x, y, width, height, "");
		this.width = 16;
		this.height = 16;
		this.PIXEL = 1f / 16f;
		this.NAME = name;
		
		this.TEXTURE = new ResourceLocation("projectzed", "textures/gui/" + name + "Button.png");
		this.TESS = Tessellator.instance;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.GuiButton#drawButton(net.minecraft.client.Minecraft, int, int)
	 */
	@Override
	public void drawButton(Minecraft minecraft, int x, int y) {
		if (this.visible) {
			FontRenderer fontrenderer = minecraft.fontRenderer;
			GL11.glColor4f(1f, 1f, 1f, 1f);
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.TEXTURE);

			calc = width * this.PIXEL;
			
			this.TESS.startDrawingQuads();

				this.TESS.addVertexWithUV(xPosition, yPosition, 0, 0, 0);// bottom left texture
				this.TESS.addVertexWithUV(xPosition, yPosition + height, 0, 0, calc);// top left
				this.TESS.addVertexWithUV(xPosition + width, yPosition + height, 0, calc, calc);// top right
				this.TESS.addVertexWithUV(xPosition + width, yPosition, 0, calc, 0);// bottom right

			this.TESS.draw();

			this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xffffffff);
		}
	}

}

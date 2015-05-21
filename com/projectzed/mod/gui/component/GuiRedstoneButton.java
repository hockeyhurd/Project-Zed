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

import com.projectzed.api.util.EnumRedstoneType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * 
 * @author hockeyhurd
 * @version May 20, 2015
 */
@SideOnly(Side.CLIENT)
public class GuiRedstoneButton extends GuiButton {

	protected final Tessellator TESS;
	protected final ResourceLocation TEXTURE = new ResourceLocation("projectzed", "textures/gui/buttons.png");
	protected final float PIXEL;
	protected EnumRedstoneType type;
	
	protected boolean active;
	protected float calc, calc2, dif;
	protected static final float SIZE = 16f;
	
	/**
	 * @param id
	 * @param x
	 * @param y
	 * @param text
	 */
	public GuiRedstoneButton(int id, int x, int y, String text, EnumRedstoneType type) {
		super(id, x, y, text);
		this.width = 16;
		this.height = 16;
		this.PIXEL = 1f / 64f;
		
		this.type = type;
		this.TESS = Tessellator.instance;
	}

	/**
	 * @param id
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param text
	 */
	public GuiRedstoneButton(int id, int x, int y, int width, int height, String text, EnumRedstoneType type) {
		super(id, x, y, 16, 16, text);
		this.width = width;
		this.height = height;
		this.PIXEL = 1f / 64f;
		
		this.type = type;
		this.TESS = Tessellator.instance;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.GuiButton#drawButton(net.minecraft.client.Minecraft, int, int)
	 */
	@Override
	public void drawButton(Minecraft minecraft, int x, int y) {
		if (this.visible) {
			FontRenderer fontRenderer = minecraft.fontRenderer;
			GL11.glColor4f(1f, 1f, 1f, 1f);
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.TEXTURE);
			
			calc = (SIZE * this.type.ordinal()) * this.PIXEL;
			dif = 3f * this.width * this.PIXEL;
			calc2 = (SIZE * (this.type.ordinal() + 1)) * this.PIXEL;
			// ProjectZed.logHelper.info("calc2:", calc2 / this.PIXEL, dif);
			// need to offset height by '2 * 16'
			
			this.TESS.startDrawingQuads();

			if (this.type.ordinal() == 0) {
				this.TESS.addVertexWithUV(xPosition, yPosition, 0, calc, calc2);// bottom left texture
				this.TESS.addVertexWithUV(xPosition, yPosition + height, 0, calc, dif - calc2);// top left
				this.TESS.addVertexWithUV(xPosition + width, yPosition + height, 0, calc2, dif - calc2);// top right
				this.TESS.addVertexWithUV(xPosition + width, yPosition, 0, calc2, calc2);// bottom right
			}
			
			else {
				this.TESS.addVertexWithUV(xPosition, yPosition, 0, calc, dif - calc2);// bottom left texture
				this.TESS.addVertexWithUV(xPosition, yPosition + height, 0, calc, calc2);// top left
				this.TESS.addVertexWithUV(xPosition + width, yPosition + height, 0, calc2, calc2);// top right
				this.TESS.addVertexWithUV(xPosition + width, yPosition, 0, calc2, dif - calc2);// bottom right
			}
			
			this.TESS.draw();
			
			this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xffffffff);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.GuiButton#mousePressed(net.minecraft.client.Minecraft, int, int)
	 */
	@Override
	public boolean mousePressed(Minecraft minecraft, int x, int y) {
		boolean ret = super.mousePressed(minecraft, x, y);

		if (ret) active = !active; 			
		
		return ret;
	}
	
	public EnumRedstoneType getType() {
		return type;
	}
	
	public void setType(EnumRedstoneType type) {
		this.type = type;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
}

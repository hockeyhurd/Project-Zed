package com.projectzed.mod.gui.component;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for custom clear button to be used primarily with Fabrication table.
 * 
 * @author hockeyhurd
 * @version Dec 31, 2014
 */
@SideOnly(Side.CLIENT)
public class GuiClearButton extends GuiButton {

	protected final Tessellator TESS;
	protected final ResourceLocation TEXTURE = new ResourceLocation("projectzed", "textures/gui/clearButton.png");; 
	protected final float PIXEL;
	protected float calc;
	
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
	public GuiClearButton(int id, int x, int y, int width, int height, String text) {
		super(id, x, y, width, height, text);
		this.width = 12;
		this.height = 12;
		this.PIXEL = 1f / 12f;
		
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

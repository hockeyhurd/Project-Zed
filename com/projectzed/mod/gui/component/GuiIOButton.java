package com.projectzed.mod.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class used to implement input, ouput, off, gui interface. <br>
 * Primarily to be used with energy cell gui but may be used <br>
 * in other future gui interfaces.
 * 
 * @author hockeyhurd
 * @version Dec 7, 2014
 */
@SideOnly(Side.CLIENT)
public class GuiIOButton extends GuiButton {

	protected final Tessellator TESS;
	protected final ResourceLocation TEXTURE;
	protected byte stateID;
	protected final float PIXEL;
	
	protected float calc, calc2, dif;

	/**
	 * @param id
	 * @param x
	 * @param y
	 * @param text
	 */
	public GuiIOButton(int id, int x, int y, String text, byte state) {
		super(id, x, y, text);
		this.TEXTURE = new ResourceLocation("projectzed", "textures/gui/buttons.png");
		this.width = 16;
		this.height = 16;
		this.PIXEL = 1f / 64f;
		
		this.stateID = state;
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
	public GuiIOButton(int id, int x, int y, int width, int height, String text, byte state) {
		super(id, x, y, 16, 16, text);
		this.TEXTURE = new ResourceLocation("projectzed", "textures/gui/buttons.png");
		this.width = 16;
		this.height = 16;
		this.PIXEL = 1f / 64f;
		
		this.stateID = state;
		this.TESS = Tessellator.instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.client.gui.GuiButton#drawButton(net.minecraft.client.Minecraft, int, int)
	 */
	@Override
	public void drawButton(Minecraft minecraft, int x, int y) {
		if (this.visible) {
			FontRenderer fontrenderer = minecraft.fontRenderer;
			GL11.glColor4f(1f, 1f, 1f, 1f);
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.TEXTURE);

			calc = (width * (this.stateID + 2)) * this.PIXEL;
			dif = 16f * this.PIXEL; 
			calc2 = this.width * (this.stateID + 1) * this.PIXEL;
			
			this.TESS.startDrawingQuads();

			if (this.stateID == -1) {
				this.TESS.addVertexWithUV(xPosition, yPosition, 0, 0, 0);// bottom left texture
				this.TESS.addVertexWithUV(xPosition, yPosition + height, 0, 0, calc);// top left
				this.TESS.addVertexWithUV(xPosition + width, yPosition + height, 0, calc, calc);// top right
				this.TESS.addVertexWithUV(xPosition + width, yPosition, 0, calc, 0);// bottom right
			}
			
			else {
				this.TESS.addVertexWithUV(xPosition, yPosition, 0, calc2, 0);// bottom left texture
				this.TESS.addVertexWithUV(xPosition, yPosition + height, 0, calc2, dif);// top left
				this.TESS.addVertexWithUV(xPosition + width, yPosition + height, 0, calc, dif);// top right
				this.TESS.addVertexWithUV(xPosition + width, yPosition, 0, calc, 0);// bottom right
			}

			this.TESS.draw();

			this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xffffffff);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.GuiButton#mousePressed(net.minecraft.client.Minecraft, int, int)
	 */
	@Override
	public boolean mousePressed(Minecraft minecraft, int x, int y) {
		boolean ret = super.mousePressed(minecraft, x, y); 
		if (ret) this.stateID = shiftButtonMatrix();

		return ret;
	}

	/**
	 * @return new button id.
	 */
	private byte shiftButtonMatrix() {
		return (byte) (this.stateID == -1 ? 0 : (this.stateID == 0 ? 1 : -1));
	}

}

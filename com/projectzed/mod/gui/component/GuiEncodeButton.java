/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.gui.component;

import com.hockeyhurd.hcorelib.api.math.Vector2;
import com.hockeyhurd.hcorelib.api.util.TessellatorHelper;
import com.projectzed.mod.util.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Encode button for gui interfaces.
 *
 * @author hockeyhurd
 * @version 5/3/2016.
 */
@SideOnly(Side.CLIENT)
public class GuiEncodeButton extends GuiButton implements IGuiButton {

	protected static final TessellatorHelper tessHelp = new TessellatorHelper();
	protected static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(Reference.MOD_NAME.toLowerCase(),
			"textures/gui/encodeButton.png");
	protected ResourceLocation TEXTURE = DEFAULT_TEXTURE;
	protected static final float PIXEL = 1.0f / 32.0f;
	protected Vector2<Integer> pos;
	protected boolean active;

	public GuiEncodeButton(int id, int x, int y, String text) {
		super(id, x, y, text);

		this.width = 30;
		this.height = 11;

		pos = new Vector2<Integer>(x, y);
	}

	@Override
	public void drawButton(Minecraft minecraft, int x, int y) {
		if (this.visible) {
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			minecraft.getTextureManager().bindTexture(TEXTURE);

			tessHelp.startDrawingQuads();
			tessHelp.addVertUV(xPosition, yPosition, 0, 0.0f, height * PIXEL);
			tessHelp.addVertUV(xPosition, yPosition + height, 0, 0.0f, 0.0f);
			tessHelp.addVertUV(xPosition + width, yPosition + height, 0, width * PIXEL, 0.0f);
			tessHelp.addVertUV(xPosition + width, yPosition, 0, width * PIXEL, height * PIXEL);
			tessHelp.draw();
		}
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public Vector2<Integer> getPos() {
		return pos;
	}

}

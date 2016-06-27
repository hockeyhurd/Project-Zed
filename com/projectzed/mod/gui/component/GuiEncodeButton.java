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

import com.hockeyhurd.hcorelib.api.client.gui.GuiHelper;
import com.hockeyhurd.hcorelib.api.math.Vector2;
import com.projectzed.mod.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Encode button for gui interfaces.
 *
 * @author hockeyhurd
 * @version 5/3/2016.
 */
@SideOnly(Side.CLIENT)
public class GuiEncodeButton extends GuiButton implements IGuiButton {

	protected static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(Reference.MOD_NAME.toLowerCase(),
			"textures/gui/encodeButton.png");
	protected ResourceLocation TEXTURE = DEFAULT_TEXTURE;
	protected Vector2<Integer> pos;
	protected boolean active;
	protected final Gui parentGui;

	public GuiEncodeButton(Gui parentGui, int id, int x, int y, String text) {
		super(id, x, y, text);

		this.parentGui = parentGui;
		this.width = 30;
		this.height = 11;

		pos = new Vector2<Integer>(x, y);
	}

	@Override
	public void drawButton(Minecraft minecraft, int x, int y) {
		if (this.visible) {
			final FontRenderer fontRenderer = minecraft.fontRendererObj;
			GuiHelper.simpleRenderGui(TEXTURE, GuiHelper.DEFAULT_COL, xPosition, yPosition, 0, 0, width, height, 32.0f, 32.0f);

			mouseDragged(minecraft, x, y);

			int j = 0xe0e0e0;

			if (packedFGColour != 0) j = packedFGColour;
			else if (!this.enabled) j = 0xa0a0a0;
			else if (this.hovered) j = 0xffffa0;

			// this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xffffffff);
			this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
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

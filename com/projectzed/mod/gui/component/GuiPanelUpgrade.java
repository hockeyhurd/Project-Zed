/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.gui.component;

import com.hockeyhurd.hcorelib.api.math.Vector2;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Gui class for rendering upgrade panel.
 *
 * @author hockeyhurd
 * @version 7/6/2015.
 */
@SideOnly(Side.CLIENT)
public class GuiPanelUpgrade {

	private final ResourceLocation texture;
	private int numSlots;
	public Vector2<Double> location, thisSize;

	private final Tessellator TESS;
	private final double calcX, calcY;

	public GuiPanelUpgrade(Vector2<Double> location) {
		this(location, new Vector2<Double>(32d, 86d), 4);
	}

	public GuiPanelUpgrade(Vector2<Double> location, Vector2<Double> thisSize, int numSlots) {
		this.location = location;
		this.thisSize = thisSize;
		this.numSlots = numSlots;

		this.texture = new ResourceLocation("projectzed", "textures/gui/GuiDefaultUpgradeSlots.png");
		this.TESS = Tessellator.instance;
		this.calcX = (1d / 96d * thisSize.x);
		this.calcY = (1d / 96d * thisSize.y);
	}

	public int getNumSlots() {
		return numSlots;
	}

	public void setNumSlots(int numSlots) {
		this.numSlots = numSlots;
	}

	public void renderContainer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

		// ProjectZed.logHelper.info(thisSize.toString());
		// gui.drawTexturedModalRect(location.x, location.y, 0, 0, thisSize.x, thisSize.y);

		TESS.startDrawingQuads();

		TESS.addVertexWithUV(location.x, location.y, 0d, 0d, 0d);
		TESS.addVertexWithUV(location.x, location.y + thisSize.y, 0d, 0d, calcY);
		TESS.addVertexWithUV(location.x + thisSize.x, location.y + thisSize.y, 0d, calcX, calcY);
		TESS.addVertexWithUV(location.x + thisSize.x, location.y, 0d, calcX, 0d);

		TESS.draw();
	}

}

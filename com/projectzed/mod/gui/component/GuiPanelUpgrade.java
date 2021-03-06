/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.gui.component;

import com.hockeyhurd.hcorelib.api.client.gui.GuiHelper;
import com.hockeyhurd.hcorelib.api.math.Vector2;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Gui class for rendering upgrade panel.
 *
 * @author hockeyhurd
 * @version 7/6/2015.
 */
@SideOnly(Side.CLIENT)
public class GuiPanelUpgrade {

	private static final ResourceLocation texture = new ResourceLocation("projectzed", "textures/gui/GuiDefaultUpgradeSlots.png");
	private final static float textureSize = 96.0f;
	private int numSlots;
	public Vector2<Integer> location, thisSize;

	public GuiPanelUpgrade(Vector2<Integer> location) {
		this(location, new Vector2<Integer>(32, 86), 4);
	}

	public GuiPanelUpgrade(Vector2<Integer> location, Vector2<Integer> thisSize, int numSlots) {
		this.location = location;
		this.thisSize = thisSize;
		this.numSlots = numSlots;
	}

	public int getNumSlots() {
		return numSlots;
	}

	public void setNumSlots(int numSlots) {
		this.numSlots = numSlots;
	}

	public void renderContainer(Gui gui, float f, int x, int y) {
		GuiHelper.simpleRenderGui(texture, GuiHelper.DEFAULT_COL, location.x, location.y, 0, 0, thisSize.x, thisSize.y, textureSize, textureSize);
	}

}

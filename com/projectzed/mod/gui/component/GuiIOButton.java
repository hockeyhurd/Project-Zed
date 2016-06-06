/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui.component;

import com.hockeyhurd.hcorelib.api.client.gui.GuiHelper;
import com.hockeyhurd.hcorelib.api.math.Vector2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class used to implement input, ouput, off, gui interface. <br>
 * Primarily to be used with energy cell gui but may be used <br>
 * in other future gui interfaces.
 * 
 * @author hockeyhurd
 * @version Dec 7, 2014
 */
@SideOnly(Side.CLIENT)
public class GuiIOButton extends GuiButton implements IGuiButton {

	protected static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("projectzed", "textures/gui/buttons.png");
	protected ResourceLocation TEXTURE = DEFAULT_TEXTURE;
	protected byte stateID;

	protected boolean active;
	protected Vector2<Integer> pos = Vector2.zero.getVector2i();
	protected final Gui parentGui;

	/**
	 * @param id
	 * @param x
	 * @param y
	 * @param text
	 */
	public GuiIOButton(Gui parentGui, int id, int x, int y, String text, byte state) {
		super(id, x, y, text);

		this.parentGui = parentGui;
		this.width = 16;
		this.height = 16;

		this.stateID = state;
		this.pos.x = x;
		this.pos.y = y;
	}

	/**
	 * @param id
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param text
	 */
	public GuiIOButton(Gui parentGui, int id, int x, int y, int width, int height, String text, byte state) {
		super(id, x, y, 16, 16, text);

		this.parentGui = parentGui;
		this.width = 16;
		this.height = 16;

		this.stateID = state;
		this.pos.x = x;
		this.pos.y = y;
	}

	@Override
	public void drawButton(Minecraft minecraft, int x, int y) {
		if (this.visible) {
			FontRenderer fontrenderer = minecraft.fontRendererObj;

			GuiHelper.simpleRenderGui(TEXTURE, GuiHelper.DEFAULT_COL, xPosition, yPosition, 0, 0, width, height);

			mouseDragged(minecraft, x, y);
			int j = 0xe0e0e0;

			if (packedFGColour != 0) j = packedFGColour;
			else if (!this.enabled) j = 0xa0a0a0;
			else if (this.hovered) j = 0xffffa0;

			this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
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
	 * Sets the state id.
	 * 
	 * @param id id value to set.
	 */
	public void setStateID(byte id) {
		this.stateID = id;
	}

	/**
	 * Gets the state id of the gui button.
	 * 
	 * @return state id.
	 */
	public byte getStateID() {
		return this.stateID;
	}
	
	/**
	 * @return new button id.
	 */
	private byte shiftButtonMatrix() {
		return (byte) (this.stateID == -1 ? 0 : (this.stateID == 0 ? 1 : -1));
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.component.IGuiButton#isActive()
	 */
	@Override
	public boolean isActive() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.component.IGuiButton#setActive(boolean)
	 */
	@Override
	public void setActive(boolean active) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.component.IGuiButton#getPos()
	 */
	@Override
	public Vector2<Integer> getPos() {
		return pos;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + stateID;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GuiIOButton other = (GuiIOButton) obj;
		if (pos == null) {
			if (other.pos != null) return false;
		}
		else if (!pos.equals(other.pos)) return false;
		if (stateID != other.stateID) return false;
		return true;
	}

}

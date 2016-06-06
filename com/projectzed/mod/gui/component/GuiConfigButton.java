/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui.component;

import com.hockeyhurd.hcorelib.api.client.gui.GuiHelper;
import com.hockeyhurd.hcorelib.api.math.Rect;
import com.hockeyhurd.hcorelib.api.math.Vector2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Gui buttons for controlling tabs/widgets.
 * 
 * @author hockeyhurd
 * @version May 20, 2015
 */
@SideOnly(Side.CLIENT)
public class GuiConfigButton extends GuiButton implements IGuiButton {

	// protected static final Tessellator TESS = Tessellator.instance;
	protected static final ResourceLocation DEFFAULT_TEXTURE = new ResourceLocation("projectzed", "textures/gui/buttons.png");
	protected ResourceLocation TEXTURE = DEFFAULT_TEXTURE;
	protected final float PIXEL;
	protected int stateID;
	
	protected int x = 16, y = 16;
	protected static final int rateOfMovement = 8;
	protected final Rect<Integer> rect;
	protected boolean active;
	protected int calc, calc2, dif;
	protected Vector2<Integer> pos = Vector2.zero.getVector2i();
	protected static final int SIZE = 16;
	
	public enum EnumConfigType {
		SIDED_IO, REDSTONE
	}
	
	protected EnumConfigType type;
	
	/**
	 * @param id
	 * @param x
	 * @param y
	 * @param text
	 */
	public GuiConfigButton(int id, int x, int y, String text, byte stateID, Rect<Integer> rect, EnumConfigType type) {
		super(id, x, y, text);
		this.width = 16;
		this.height = 16;
		this.PIXEL = 1f / 64f;
		
		this.stateID = stateID;
		this.rect = rect;
		this.pos.x = x;
		this.pos.y = y;
		this.type = type;
	}

	/**
	 * @param id
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param text
	 */
	public GuiConfigButton(int id, int x, int y, int width, int height, String text, int stateID, Rect<Integer> rect, EnumConfigType type) {
		super(id, x, y, 16, 16, text);
		this.width = 16;
		this.height = 16;
		this.PIXEL = 1f / 64f;
		
		this.stateID = stateID;
		this.rect = rect;
		this.pos.x = x;
		this.pos.y = y;
		this.type = type;
	}
	
	/**
	 * @return type of config button.
	 */
	public EnumConfigType getConfigType() {
		return this.type;
	}
	
	@Override
	public void drawButton(Minecraft minecraft, int x, int y) {
		if (this.visible) {
			FontRenderer fontRenderer = minecraft.fontRendererObj;

			calc = SIZE * stateID;
			dif = 3 * SIZE;
			calc2 = SIZE * (stateID + 1);

			if (stateID == 0) GuiHelper.simpleRenderGui(TEXTURE, GuiHelper.DEFAULT_COL, xPosition, yPosition, calc, calc2, 16, 16, 64.0f, 64.0f);
			else GuiHelper.simpleRenderGui(TEXTURE, GuiHelper.DEFAULT_COL, xPosition, yPosition, calc, dif - calc2, 16, 16, 64.0f, 64.0f);

			mouseDragged(minecraft, x, y);
			int j = 0xe0e0e0;

			if (packedFGColour != 0) j = packedFGColour;
			else if (!enabled) j = 0xa0a0a0;
			else if (hovered) j = 0xffffa0;

			if (active) {
				if (this.x < rect.max.x) this.x += rateOfMovement;
				if (this.y < rect.max.y) this.y += rateOfMovement;
				
				Gui.drawRect(rect.min.x, rect.min.y, rect.min.x - this.x, rect.min.y + this.y, rect.getColor());
			}
			
			else if (!active && (this.x != 16 || this.y != 16)) {
				if (this.x > 16) this.x -= rateOfMovement;
				if (this.y > 16) this.y -= rateOfMovement;
				
				Gui.drawRect(rect.min.x, rect.min.y, rect.min.x - this.x, rect.min.y + this.y, rect.getColor());
			}
			
			this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
		}
	}
	
	@Override
	public boolean mousePressed(Minecraft minecraft, int x, int y) {
		boolean ret = super.mousePressed(minecraft, x, y);
		
		if (ret) active = !active;
		
		return ret;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
	public Rect getRect() {
		return rect;
	}
	
	public boolean isExpanding() {
		return (this.x != 16 && this.x != rect.max.x) || (this.y != 16 && this.y != rect.max.y);
	}
	
	@Override
	public Vector2<Integer> getPos() {
		return pos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rect == null) ? 0 : rect.hashCode());
		result = prime * result + stateID;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GuiConfigButton other = (GuiConfigButton) obj;
		if (rect == null) {
			if (other.rect != null) return false;
		}
		else if (!rect.equals(other.rect)) return false;
		if (stateID != other.stateID) return false;
		if (x != other.x) return false;
		if (y != other.y) return false;
		return true;
	}
	
}

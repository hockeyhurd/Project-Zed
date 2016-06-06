/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui.component;

import com.hockeyhurd.hcorelib.api.client.gui.GuiHelper;
import com.hockeyhurd.hcorelib.api.math.Vector2;
import com.projectzed.api.util.EnumRedstoneType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Gui buttons for redstone conrol.
 * 
 * @author hockeyhurd
 * @version May 20, 2015
 */
@SuppressWarnings("unchecked")
@SideOnly(Side.CLIENT)
public class GuiRedstoneButton extends GuiButton implements IGuiButton {

	protected static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("projectzed", "textures/gui/buttons.png");
	protected ResourceLocation TEXTURE = DEFAULT_TEXTURE;
	protected EnumRedstoneType type;

	protected final Gui parentGui;
	protected boolean active;
	protected Vector2<Integer> min = Vector2.zero.getVector2i();
	protected Vector2<Integer> max = Vector2.zero.getVector2i();
	
	protected Vector2<Integer> pos = Vector2.zero.getVector2i();
	protected static final int SIZE = 16;
	
	/**
	 * @param id
	 * @param x
	 * @param y
	 * @param text
	 */
	public GuiRedstoneButton(Gui parentGui, int id, int x, int y, String text, EnumRedstoneType type) {
		super(id, x, y, text);

		this.parentGui = parentGui;
		this.width = 16;
		this.height = 16;

		this.type = type;
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
	public GuiRedstoneButton(Gui parentGui, int id, int x, int y, int width, int height, String text, EnumRedstoneType type) {
		super(id, x, y, 16, 16, text);

		this.parentGui = parentGui;
		this.width = width;
		this.height = height;

		this.type = type;
	}

	@Override
	public void drawButton(Minecraft minecraft, int x, int y) {
		if (this.visible) {
			FontRenderer fontRenderer = minecraft.fontRendererObj;
			/*GL11.glColor4f(1f, 1f, 1f, 1f);
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.TEXTURE);
			
			min.x = (SIZE * (this.type.ordinal() + 0)) * this.PIXEL;
			min.y = (SIZE * ((active ? 0 : 1) + 2)) * this.PIXEL;
			max.x = (SIZE * (this.type.ordinal() + 1)) * this.PIXEL;
			max.y = (SIZE * ((active ? 0 : 1) + 3)) * this.PIXEL;
			
			this.TESS.startDrawingQuads();
			
			this.TESS.addVertexWithUV(xPosition, yPosition, 0, min.x, min.y);
			this.TESS.addVertexWithUV(xPosition, yPosition + height, 0, min.x, max.y);
			this.TESS.addVertexWithUV(xPosition + width, yPosition + height, 0, max.x, max.y);
			this.TESS.addVertexWithUV(xPosition + width, yPosition, 0, max.x, min.y);
			
			this.TESS.draw();
			
			this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xffffffff);*/

			min.x = (SIZE * (this.type.ordinal() + 0));
			min.y = (SIZE * ((active ? 0 : 1) + 2));
			max.x = (SIZE * (this.type.ordinal() + 1));
			max.y = (SIZE * ((active ? 0 : 1) + 3));

			GuiHelper.simpleRenderGui(TEXTURE, GuiHelper.DEFAULT_COL, xPosition, yPosition, 0, 0, max.x, max.y);
			mouseDragged(minecraft, x, y);

			int j = 0xe0e0e0;

			if (packedFGColour != 0) j = packedFGColour;
			else if (!this.enabled) j = 0xa0a0a0;
			else if (this.hovered) j = 0xffffa0;

			this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
		}
	}
	
	@Override
	public boolean mousePressed(Minecraft minecraft, int x, int y) {
		boolean ret = super.mousePressed(minecraft, x, y);

		// if (ret) active = !active; 			
		
		return ret;
	}
	
	public EnumRedstoneType getType() {
		return type;
	}
	
	public void setType(EnumRedstoneType type) {
		this.type = type;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GuiRedstoneButton other = (GuiRedstoneButton) obj;
		if (type != other.type) return false;
		return true;
	}
	
}

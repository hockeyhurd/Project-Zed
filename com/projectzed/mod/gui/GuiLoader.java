/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui;

import static com.projectzed.mod.tileentity.machine.TileEntityIndustrialLoader.MAX_RADII;
import static com.projectzed.mod.tileentity.machine.TileEntityIndustrialLoader.MIN_RADII;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.projectzed.mod.container.ContainerLoader;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityLoader;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialLoader;

/**
 * Class containing gui code for industrial loader.
 * 
 * @author hockeyhurd
 * @version Apr 21, 2015
 */
public class GuiLoader extends GuiContainer {

	private final TileEntityIndustrialLoader te;
	protected ResourceLocation texture;
	private byte radii;
	
	/**
	 * @param inv
	 * @param te
	 */
	public GuiLoader(InventoryPlayer inv, TileEntityIndustrialLoader te) {
		super(new ContainerLoader(inv, te));
		this.te = te;
		texture = new ResourceLocation("projectzed", "textures/gui/GuiLoader.png");
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#drawGuiContainerForegroundLayer(int, int)
	 */
	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);
		
		String name = radii < 10 ? " " + radii : "" + radii;
		int xPos = this.xSize - 132 - this.fontRendererObj.getStringWidth(name) / 2;
		int yPos = this.ySize - 100 - this.fontRendererObj.getStringWidth(name) / 2;
		this.fontRendererObj.drawString(name, xPos, yPos, 4210752);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#drawGuiContainerBackgroundLayer(float, int, int)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		if (radii > 1) {
			for (int yy = -radii + 1; yy < radii; yy++) {
				for (int xx = -radii + 1; xx < radii; xx++) {
					this.drawTexturedModalRect(guiLeft + 125 + (xx * 6), guiTop + 40 + (yy * 6), 0, 171, 6, 6);
				}
			}
		}
		
		else this.drawTexturedModalRect(guiLeft + 125, guiTop + 40, 0, 171, 6, 6);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#initGui()
	 */
	@Override
	public void initGui() {
		super.initGui();
		
		this.buttonList.add(new GuiButton(0, guiLeft + 10, guiTop + 56, 20, 20, "-"));
		this.buttonList.add(new GuiButton(1, guiLeft + 60, guiTop + 56, 20, 20, "+"));
		
		this.radii = te.getRadii();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.GuiScreen#actionPerformed(net.minecraft.client.gui.GuiButton)
	 */
	@Override
	public void actionPerformed(GuiButton button) {
		boolean didSomething = false;
		
		if (button.displayString.equals("-")) {
			if (!this.isShiftKeyDown() && radii - 1 >= MIN_RADII) radii--;
			else if (this.isShiftKeyDown() && radii > MIN_RADII) radii = MIN_RADII;
			
			didSomething = true;
		}
		
		else if (button.displayString.equals("+")) {
			if (!this.isShiftKeyDown() && radii + 1 <= MAX_RADII) radii++;
			else if (this.isShiftKeyDown() && radii < MAX_RADII) radii = MAX_RADII;
			
			didSomething = true;
		}
		
		if (didSomething) {
			this.te.setRadii(this.radii);
			PacketHandler.INSTANCE.sendToServer(new MessageTileEntityLoader(this.te, this.radii));
		}
	}

}

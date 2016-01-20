/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui;

import com.projectzed.mod.container.ContainerNuclearIOPort;
import com.projectzed.mod.tileentity.container.TileEntityNuclearIOPort;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Class containing gui code for NuclearIOPort.
 * 
 * @author hockeyhurd
 * @version Mar 19, 2015
 */
@SideOnly(Side.CLIENT)
public class GuiNuclearIOPort extends GuiContainer {

	private TileEntityNuclearIOPort te;
	private final ResourceLocation texture;
	
	/**
	 * @param inv player's inventory
	 * @param te tileentity as reference.
	 */
	public GuiNuclearIOPort(InventoryPlayer inv, TileEntityNuclearIOPort te) {
		super(new ContainerNuclearIOPort(inv, te));
		texture = new ResourceLocation("projectzed", "textures/gui/GuiNuclearIOPort.png");

		this.te = te;
		this.xSize = 176;
		this.ySize = 166;
		this.te = te;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#drawGuiContainerForegroundLayer(int, int)
	 */
	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		String name = !this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName(), new Object[0]);
		String burnTime = String.format("Burn time left: %d", te.getBurnTime());

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(burnTime, this.xSize / 2 - this.fontRendererObj.getStringWidth(burnTime) / 2, 46, 4210752);
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
	}

}

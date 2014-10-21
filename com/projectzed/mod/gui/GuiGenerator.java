package com.projectzed.mod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;

/**
 * Generic class for gui's of generators.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public class GuiGenerator extends GuiContainer {

	public final ResourceLocation texture;
	private AbstractTileEntityGenerator te;
	public int storedEnergy;
	
	public GuiGenerator(InventoryPlayer inv, TileEntitySolarArray te) {
		super(new ContainerGenerator(inv, te));
		texture = new ResourceLocation("projectzed", "textures/gui/GuiGenerator_generic.png");
		
		this.te = te;
		this.xSize = 176;
		this.ySize = 166;
	}
	
	public void drawGuiContainerForegroundLayer(int x, int y) {
		String name = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName(), new Object[0]);
		
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
	}

	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		// int i1 = this.te.getCookProgressScaled(24);
        // this.drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 14, i1 + 1, 16);
	}

}

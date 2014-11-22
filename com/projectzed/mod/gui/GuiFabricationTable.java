package com.projectzed.mod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.projectzed.mod.container.ContainerFabricationTable;
import com.projectzed.mod.tileentity.TileEntityFabricationTable;

/**
 * Class containing gui code for fabrication table.
 * 
 * @author hockeyhurd
 * @version Nov 22, 2014
 */
public class GuiFabricationTable extends GuiContainer {

	public final ResourceLocation texture;
	private TileEntityFabricationTable te;
	private String stringToDraw;
	
	public GuiFabricationTable(InventoryPlayer inv, TileEntityFabricationTable te) {
		super(new ContainerFabricationTable(inv, te));
		this.te = te;
		this.xSize = 237;
		this.ySize = 256;
		int slots = this.te.getSizeInventory();
		
		texture = new ResourceLocation("projectzed", "textures/gui/GuiFabricationTable.png");
	}

	public void drawGuiContainerForegroundLayer(int x, int y) {
		// String name = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName(), new Object[0]);

		// this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		// this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
	}

	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}

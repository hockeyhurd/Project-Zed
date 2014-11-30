package com.projectzed.mod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.projectzed.mod.block.container.TileEntityRFBridge;
import com.projectzed.mod.container.ContainerRFBridge;
import com.projectzed.mod.util.Reference.Constants;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing gui code for RF Bridge.
 * 
 * @author hockeyhurd
 * @version Nov 29, 2014
 */
@SideOnly(Side.CLIENT)
public class GuiRFBridge extends GuiContainer {

	public final ResourceLocation texture;
	private TileEntityRFBridge te;
	private String stringToDraw;

	public GuiRFBridge(InventoryPlayer inv, TileEntityRFBridge te) {
		super(new ContainerRFBridge(inv, te));
		this.te = te;
		this.xSize = 176;
		this.ySize = 166;
		int slots = this.te.getSizeInventory();
		
		texture = new ResourceLocation("projectzed", "textures/gui/GuiRFBridge.png");
	}

	public void drawGuiContainerForegroundLayer(int x, int y) {
		String name = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName(), new Object[0]);

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		// this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);

		this.stringToDraw = "Power: " + this.te.storedRF + " / " + this.te.getMaxEnergyStored() + " RF";
		this.fontRendererObj.drawString(I18n.format(this.stringToDraw, new Object[0]), this.xSize / 2 - this.fontRendererObj.getStringWidth(this.stringToDraw) / 2, this.ySize - 130,
				4210752);
		
		this.stringToDraw = "Power: " + this.te.getEnergyStored() + " / " + this.te.getMaxStorage() + " " + Constants.ENERGY_UNIT;
		this.fontRendererObj.drawString(I18n.format(this.stringToDraw, new Object[0]), this.xSize / 2 - this.fontRendererObj.getStringWidth(this.stringToDraw) / 2, this.ySize - 116,
				4210752);
	}

	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		float progress = (float) ((float) this.te.getEnergyStored() / (float) this.te.getMaxStorage()) * 160f;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 61, 0, 170, (int) progress, 17);
		
		float progressRF = (float) ((float) this.te.storedRF / (float) this.te.getMaxEnergyStored()) * 40f;
		progressRF = 40 - progressRF;
		int v = 192 - (int) progressRF;
		// this.drawTexturedModalRect(guiLeft + 7, guiTop + 17, 0, 192, 16, (int) progressRF);
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 17, 0, v, 16, guiTop + 3);
	}

}

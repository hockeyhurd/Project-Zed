package com.projectzed.mod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.container.ContainerMachine;

/**
 * 
 * @author hockeyhurd
 * @version Oct 23, 2014
 */
public class GuiMachine extends GuiContainer {

	public final ResourceLocation texture;
	private AbstractTileEntityMachine te;
	
	public GuiMachine(InventoryPlayer inv, AbstractTileEntityMachine te) {
		super(new ContainerMachine(inv, te));
		texture = new ResourceLocation("projectzed", "textures/gui/GuiMachine_generic.png");

		this.te = te;
		this.xSize = 176;
		this.ySize = 166;
	}

	public void drawGuiContainerForegroundLayer(int x, int y) {
		String name = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName(), new Object[0]);

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		// this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);

		this.fontRendererObj.drawString(I18n.format("Power: " + (this.te.getEnergyStored()) + " / " + this.te.getMaxStorage(), new Object[0]), this.xSize / 2 - this.fontRendererObj.getStringWidth("Power: " + this.te.getEnergyStored() + " / " + this.te.getMaxStorage()) / 2, this.ySize - 116,
				4210752);
	}

	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		float progress = (float) ((float) this.te.getEnergyStored() / (float) this.te.getMaxStorage()) * 160f;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 61, 0, 170, (int) progress, 17);
		
		int i1 = 0;
		if (this.te.isPoweredOn() && this.te.cookTime > 0) {
            i1 = this.te.getCookProgressScaled(24);
            ProjectZed.logHelper.info(i1);
            this.drawTexturedModalRect(guiLeft + 78, guiTop + 21, 176, 14, i1 + 1, 16);
		}
	}


}

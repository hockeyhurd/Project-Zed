package com.projectzed.mod.gui;

import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.container.ContainerGenerator;
import com.projectzed.mod.tileentity.generator.TileEntityNuclear;
import com.projectzed.mod.util.Reference.Constants;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Generic class for gui's of generators.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
@SideOnly(Side.CLIENT)
public class GuiGenerator extends GuiContainer {

	public final ResourceLocation texture;
	private AbstractTileEntityGenerator te;
	private String stringToDraw;
	private final DecimalFormat df = new DecimalFormat("###,###,###");

	public GuiGenerator(InventoryPlayer inv, AbstractTileEntityGenerator te) {
		super(new ContainerGenerator(inv, te));
		this.te = te;
		this.xSize = 176;
		this.ySize = 166;
		int slots = this.te.getSizeInventory();
		
		texture = new ResourceLocation("projectzed", "textures/gui/GuiGenerator_generic" + slots + ".png");
	}

	public void drawGuiContainerForegroundLayer(int x, int y) {
		String name = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName(), new Object[0]);

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		// this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
		
		if (this.te.getSource().getEffectiveSize() > 0) {
			String genSize = "Generation rate: " + df.format(this.te.getSource().getEffectiveSize()) + " McU/t";
			// if (this.te instanceof TileEntityNuclear && !this.te.canProducePower()) genSize = "Error! Reactor not set correctly!"; 
			this.fontRendererObj.drawString(I18n.format(genSize, new Object[0]), this.xSize / 2 - this.fontRendererObj.getStringWidth(genSize) / 2, this.ySize - 126,
					4210752);
		}

		this.stringToDraw = "Power: " + df.format(this.te.getEnergyStored()) + " / " + df.format(this.te.getMaxStorage()) + " " + Constants.ENERGY_UNIT;
		this.fontRendererObj.drawString(I18n.format(this.stringToDraw, new Object[0]), this.xSize / 2 - this.fontRendererObj.getStringWidth(this.stringToDraw) / 2, this.ySize - 116,
				4210752);
	}

	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		float progress = (float) ((float) this.te.getEnergyStored() / (float) this.te.getMaxStorage()) * 160f;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 61, 0, 170, (int) progress, 17);
	}

}

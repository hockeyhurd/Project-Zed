package com.projectzed.mod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.projectzed.mod.container.ContainerFabricationTable;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityFabricationTable;
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
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#initGui()
	 */
	public void initGui() {
		super.initGui();
		
		int posX = (this.width - this.xSize) / 2 + 10;
		int posY = (this.height - this.ySize) / 2 + 43;
		
		this.buttonList.add(new GuiButton(0, posX, posY, 45, 16, "Clear"));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.GuiScreen#actionPerformed(net.minecraft.client.gui.GuiButton)
	 */
	public void actionPerformed(GuiButton button) {
		switch (button.id) {
			case 0:
				PacketHandler.INSTANCE.sendToAll(new MessageTileEntityFabricationTable(this.te));
				// PacketHandler.INSTANCE.sendToServer(new MessageTileEntityFabricationTable(this.te));
				break;
		}
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

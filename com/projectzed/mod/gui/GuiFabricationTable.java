/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.projectzed.mod.container.ContainerFabricationTable;
import com.projectzed.mod.gui.component.GuiClearButton;
import com.projectzed.mod.gui.component.GuiSortButton;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityFabricationTable;
import com.projectzed.mod.tileentity.TileEntityFabricationTable;

/**
 * Class containing gui code for fabrication table.
 * 
 * @author hockeyhurd
 * @version Nov 22, 2014
 */
// @Optional.Interface(iface = "codechicken.nei.api.INEIGuiHandler", modid = "NotEnoughItems")
public class GuiFabricationTable extends GuiContainer /*implements INEIGuiHandler*/ {

	public final ResourceLocation texture;
	private TileEntityFabricationTable te;
	private String stringToDraw;
	
	/**
	 * @param inv
	 * @param te
	 */
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
		
		int posX = (this.width - this.xSize) / 2 + 50;
		int posY = (this.height - this.ySize) / 2 + 8;
		
		this.buttonList.add(new GuiClearButton(0, posX, posY, ""));
		this.buttonList.add(new GuiSortButton(1, posX - 24, posY + 16, "sort123"));
		this.buttonList.add(new GuiSortButton(2, posX - 4, posY + 16, "sort321"));
		this.buttonList.add(new GuiSortButton(3, posX - 24, posY + 34, "sortAZ"));
		this.buttonList.add(new GuiSortButton(4, posX - 4, posY + 34, "sortZA"));
		// this.buttonList.add(new GuiButton(1, posX - 20, posY + 20, 32, 20, "Sort"));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.GuiScreen#actionPerformed(net.minecraft.client.gui.GuiButton)
	 */
	public void actionPerformed(GuiButton button) {
			if (button.id == 0) {
				((ContainerFabricationTable)this.inventorySlots).clearCraftingGrid();
				PacketHandler.INSTANCE.sendToServer(new MessageTileEntityFabricationTable(this.te, 1));
			}
				
			else if (button.id > 0 && button.id <= 4) {
				((ContainerFabricationTable)this.inventorySlots).sortInventory(button.id);
				PacketHandler.INSTANCE.sendToServer(new MessageTileEntityFabricationTable(this.te, 2));
			}
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#drawGuiContainerForegroundLayer(int, int)
	 */
	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#drawGuiContainerBackgroundLayer(float, int, int)
	 */
	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	/*@Override
    @Optional.Method(modid = "NotEnoughItems")
	public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility) {
		return currentVisibility;
	}

	@Override
	public Iterable<Integer> getItemSpawnSlots(GuiContainer gui, ItemStack item) {
		return null;
	}

	@Override
	@Optional.Method(modid = "NotEnoughItems")
	public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui) {
		return null;
	}

	@Override
	public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button) {
		return false;
	}

	@Override
	public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
		return false;
	}*/

}

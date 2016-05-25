/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui;

import com.projectzed.mod.container.ContainerStoneCraftingTable;
import com.projectzed.mod.gui.component.GuiClearButton;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityStoneCraftingTable;
import com.projectzed.mod.tileentity.machine.TileEntityStoneCraftingTable;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Gui class for craftingStoneTable.
 * 
 * @author hockeyhurd
 * @version Mar 31, 2015
 */
@SideOnly(Side.CLIENT)
public class GuiStoneCraftingTable extends GuiContainer {

	public final ResourceLocation texture;
	private TileEntityStoneCraftingTable te;
	private String stringToDraw;
	
	/**
	 * @param inv player's inventory.
	 * @param te te to reference.
	 */
	public GuiStoneCraftingTable(InventoryPlayer inv, TileEntityStoneCraftingTable te) {
		super(new ContainerStoneCraftingTable(inv, te));
		this.te = te;
		int slots = this.te.getSizeInventory();
		
		texture = new ResourceLocation("textures/gui/container/crafting_table.png");
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#initGui()
	 */
	@Override
	public void initGui() {
		super.initGui();
		
		int posX = (this.width - this.xSize) / 2 + 10;
		int posY = (this.height - this.ySize) / 2 + 8;
		
		this.buttonList.add(new GuiClearButton(0, posX, posY, ""));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.GuiScreen#actionPerformed(net.minecraft.client.gui.GuiButton)
	 */
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id >= 0) {
			if (button.id == 0) {
				((ContainerStoneCraftingTable) this.inventorySlots).clearCraftingGrid();
				PacketHandler.INSTANCE.sendToServer(new MessageTileEntityStoneCraftingTable(this.te, (byte) 1));
			}
			
			// ProjectZed.logHelper.info("button id hit:", button.id);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#drawGuiContainerBackgroundLayer(float, int, int)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

}

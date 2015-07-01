/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui;

import com.hockeyhurd.api.math.Vector2;
import com.projectzed.mod.gui.component.FluidLabel;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityCentrifuge;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCentrifuge;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Class containing gui code for industrial centrifuge.
 * 
 * @author hockeyhurd
 * @version Dec 21, 2014
 */
public class GuiCentrifuge extends GuiMachine {

	private int waterStored;
	private TileEntityIndustrialCentrifuge te2;
	private GuiButton[] buttons;
	private byte amount = 1;

	private Vector2<Integer> pos2, minMax2;

	/**
	 * @param inv
	 * @param te
	 */
	public GuiCentrifuge(InventoryPlayer inv, TileEntityIndustrialCentrifuge te) {
		super(inv, te);
		this.texture = new ResourceLocation("projectzed", "textures/gui/GuiCentrifuge.png");
		this.te2 = te;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.mod.gui.GuiMachine#drawGuiContainerForegroundLayer(int,
	 * int)
	 */
	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);
		
		String name = amount < 10 ? " " + amount : "" + amount;
		int xPos = this.xSize / 3 - this.fontRendererObj.getStringWidth(name) / 2;
		int yPos = this.ySize / 3 - this.fontRendererObj.getStringWidth(name) / 2;
		this.fontRendererObj.drawString(name, xPos, yPos, 4210752);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.mod.gui.GuiMachine#drawGuiContainerBackgroundLayer(float,
	 * int, int)
	 */
	@Override
	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		super.drawGuiContainerBackgroundLayer(f, x, y);

		float progressWater =  ((float) this.te2.getTank().getFluidAmount() / (float) this.te2.getTank().getCapacity()) * 39f;
		progressWater = 39f - progressWater;
		int v = 0 - (int) progressWater;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 17, 200, v, 16, 39);
		
		this.drawTexturedModalRect(guiLeft + 8, guiTop + 17, 216, 0, 16, 39);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiMachine#initGui()
	 */
	@Override
	public void initGui() {
		super.initGui();

		this.pos2 = new Vector2<Integer>(guiLeft + 7, guiTop + 17);
		this.minMax2 = new Vector2<Integer>(guiLeft + 7 + 16, guiTop + 17 + 41);

		this.labelList.add(new FluidLabel<Integer>(this.pos2, this.minMax2, this.te2.getTank().getFluidAmount(), this.te2.getTank().getCapacity()));
		
		this.buttons = new GuiButton[] {
			// new GuiButton(0, guiLeft + 25, guiTop + 42, 20, 20, "-"),
			new GuiButton(0, guiLeft + 38, guiTop + 49, 12, 12, "-"),
			new GuiButton(1, guiLeft + 69, guiTop + 49, 12, 12, "+"),
		};

		for (GuiButton b : this.buttons) {
			this.buttonList.add(b);
		}
		
		this.amount = this.te2.getCraftingAmount();
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.GuiMachine#updateScreen()
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();

		this.pos2.x = guiLeft + 7;
		this.pos2.y = guiTop + 17;

		this.minMax2.x = guiLeft + 7 + 16;
		this.minMax2.y = guiTop + 17 + 41;

		if (getComponents() != null && getComponents().size() > 1) {
			getComponents().get(1).update(this.mouseVec, this.pos2, this.minMax2, this.te2.getTank().getFluidAmount(), this.te2.getTank().getCapacity());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.GuiScreen#actionPerformed(net.minecraft.client.gui.GuiButton)
	 */
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id >= 0 && button.id < this.buttons.length) {
			// ProjectZed.logHelper.info("Button ID:\t" + button.id);
			if (button.id == 0) {
				if (!this.isShiftKeyDown() && amount - 1 >= 1) amount--;
				else if (this.isShiftKeyDown() && amount > 1) amount = 1;
			}
			
			else if (button.id == 1) {
				if (!this.isShiftKeyDown() && amount + 1 <= 10) amount++;
				else if (this.isShiftKeyDown() && amount < 10) amount = 10;
			}
			
			this.te2.setCraftingAmount(amount);
			PacketHandler.INSTANCE.sendToServer(new MessageTileEntityCentrifuge(te2));
		}
	}

}

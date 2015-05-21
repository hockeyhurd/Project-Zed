/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.hockeyhurd.api.math.Vector2;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.container.ContainerMachine;
import com.projectzed.mod.gui.component.GuiConfigButton;
import com.projectzed.mod.gui.component.IInfoContainer;
import com.projectzed.mod.gui.component.IInfoLabel;
import com.projectzed.mod.gui.component.PowerLabel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing gui code for all machines.
 * 
 * @author hockeyhurd
 * @version Oct 23, 2014
 */
@SideOnly(Side.CLIENT)
public class GuiMachine extends GuiContainer implements IInfoContainer {

	public ResourceLocation texture;
	private AbstractTileEntityMachine te;
	private String stringToDraw;

	protected Vector2<Integer> mouseVec, pos, minMax;
	protected List<IInfoLabel> labelList;
	protected GuiConfigButton[] configButtons;

	/**
	 * @param inv
	 * @param te
	 */
	public GuiMachine(InventoryPlayer inv, AbstractTileEntityMachine te) {
		super(new ContainerMachine(inv, te));
		if (te.getSizeInvenotry() == 1) texture = new ResourceLocation("projectzed", "textures/gui/GuiMachineSingleSlot.png");
		else if (te.getSizeInvenotry() == 2) texture = new ResourceLocation("projectzed", "textures/gui/GuiMachine_generic.png");
		else if (te.getSizeInvenotry() == 0) texture = new ResourceLocation("projectzed", "textures/gui/GuiGenerator_generic0.png");

		this.te = te;
		this.xSize = 176;
		this.ySize = 166;

		this.labelList = new ArrayList<IInfoLabel>();
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#
	 * drawGuiContainerForegroundLayer(int, int)
	 */
	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		String name = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName(), new Object[0]);

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		// this.fontRendererObj.drawString(I18n.format("container.inventory",
		// new Object[0]), 8, this.ySize - 96 + 2, 4210752);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#
	 * drawGuiContainerBackgroundLayer(float, int, int)
	 */
	@Override
	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		float progress = (float) ((float) this.te.getEnergyStored() / (float) this.te.getMaxStorage()) * 160f;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 61, 0, 170, (int) progress, 17);
	
		if (this.te.getSizeInvenotry() > 1) {
			int i1 = 0;
			if (this.te.isPoweredOn() && this.te.cookTime > 0) {
				i1 = this.te.getCookProgressScaled(24);
				this.drawTexturedModalRect(guiLeft + 78, guiTop + 21, 176, 14, i1 + 1, 16);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#drawScreen(int, int,
	 * float)
	 */
	@Override
	public void drawScreen(int x, int y, float f) {
		super.drawScreen(x, y, f);
		
		this.mouseVec.x = x;
		this.mouseVec.y = y;
		
		if (visibleComp() != null) this.drawHoveringText(visibleComp().getLabel(), x, y, this.fontRendererObj);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#initGui()
	 */
	@Override
	public void initGui() {
		super.initGui();

		this.mouseVec = Vector2.zero;
		this.pos = new Vector2<Integer>(guiLeft + 7, guiTop + 61);
		this.minMax = new Vector2<Integer>(guiLeft + 7 + 162, guiTop + 61 + 17);

		this.labelList.add(new PowerLabel<Integer>(this.pos, this.minMax, this.te.getEnergyStored(), this.te.getMaxStorage(), true));
		
		configButtons = new GuiConfigButton[] {
				new GuiConfigButton(0, guiLeft - 16, guiTop + 16, null, (byte) 0),
				new GuiConfigButton(1, guiLeft - 16, guiTop + 16 + 20, null, (byte) 1), 
		};
		
		for (GuiConfigButton button : configButtons) {
			this.buttonList.add(button);
		}
		
	}

	@Override
	public void actionPerformed(GuiButton button) {
		boolean isActive = false;
		
		if (button.id == 0) {
			ProjectZed.logHelper.info("button.id:", button.id);
			if (configButtons == null || configButtons.length == 0) return;
			isActive = configButtons[button.id].isActive();
			ProjectZed.logHelper.info(isActive);
			isActive = !isActive;
			ProjectZed.logHelper.info(isActive);
			
			for (int i = 0; i < configButtons.length; i++) {
				if (button.id == i) continue;
				configButtons[i].setActive(isActive);
				configButtons[i].visible = !configButtons[i].visible;
			}
			
			configButtons[button.id].setActive(isActive);
		}
			
		else if (button.id == 1) {
			ProjectZed.logHelper.info("button.id:", button.id);
			if (configButtons == null || configButtons.length == 0) return;
			
			for (int i = 0; i < configButtons.length; i++) {
				if (button.id == i) continue;
				configButtons[i].setActive(false);
				configButtons[i].visible = !configButtons[i].visible;
			}
			
			configButtons[button.id].setActive(!configButtons[button.id].isActive());
		}
		
		// ProjectZed.logHelper.info("button.id:", button.id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.component.IInfoContainer#getComponents()
	 */
	@Override
	public List<IInfoLabel> getComponents() {
		return this.labelList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.component.IInfoContainer#visibleComp()
	 */
	@Override
	public IInfoLabel visibleComp() {
		if (getComponents() != null && getComponents().size() > 0) {
			IInfoLabel label = null;

			for (IInfoLabel index : getComponents()) {
				if (index.isVisible(false)) {
					label = index;
					break;
				}
			}

			return label;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.gui.component.IInfoContainer#update()
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();

		this.pos.x = guiLeft + 7;
		this.pos.y = guiTop + 61;

		this.minMax.x = guiLeft + 7 + 162;
		this.minMax.y = guiTop + 61 + 17;

		if (this.te != null && getComponents() != null && getComponents().size() > 0) {
			getComponents().get(0).update(this.mouseVec, this.pos, this.minMax, this.te.getEnergyStored(), this.te.getMaxStorage());
		}
		
		if (configButtons != null && configButtons.length > 0) {
			for (GuiConfigButton button : configButtons) {
				if (button.getX() != this.guiLeft || button.getY() != this.guiTop) button.setPos(guiLeft, guiTop);
			}
		}
		
	}

}

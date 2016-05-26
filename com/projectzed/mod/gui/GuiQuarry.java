/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui;

import com.hockeyhurd.hcorelib.api.math.Rect;
import com.hockeyhurd.hcorelib.api.math.Vector2;
import com.projectzed.api.tileentity.digger.AbstractTileEntityDigger;
import com.projectzed.api.util.EnumRedstoneType;
import com.projectzed.mod.gui.component.GuiConfigButton;
import com.projectzed.mod.gui.component.GuiConfigButton.EnumConfigType;
import com.projectzed.mod.gui.component.GuiRedstoneButton;
import com.projectzed.mod.gui.component.IGuiButton;
import com.projectzed.mod.gui.component.PowerLabel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;

/**
 * Gui for all quarries.
 * 
 * @author hockeyhurd
 * @version Jun 21, 2015
 */
@SideOnly(Side.CLIENT)
public class GuiQuarry extends GuiDigger {

	/**
	 * @param inv
	 * @param te
	 */
	public GuiQuarry(InventoryPlayer inv, AbstractTileEntityDigger te) {
		super(inv, te);
		this.xSize = 176 + upgradeXOffset;
		this.ySize = 198;
	}

	@Override
	protected ResourceLocation getResourceTexture() {
		return new ResourceLocation("projectzed", "textures/gui/GuiQuarry.png");
	}
	
	@Override
	public void initGui() {
		this.mc.thePlayer.openContainer = this.inventorySlots;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
		
		this.mouseVec = Vector2.zero;
		this.pos = new Vector2<Integer>(guiLeft + 7, guiTop + 61 + 32);
		this.minMax = new Vector2<Integer>(guiLeft + 7 + 162, guiTop + 61 + 17 + 32);

		int guiLeft = (width - xSize) / 2;
		int guiTop = (height - ySize) / 2;
		
		this.labelList.add(new PowerLabel<Integer>(this.pos, this.minMax, this.te.getEnergyStored(), this.te.getMaxStorage(), true));
		
		int counter = 0;
		
		// if new list, 'create' new list object.
		if (buttons == null) buttons = new LinkedList<IGuiButton>();
		
		// if list has cached buttons, clear it for new init. we do this because some buttons depend upon player facing ForgeDirection.
		if (!buttons.isEmpty()) buttons.clear();
		
		GuiConfigButton sidedIOButton = new GuiConfigButton(counter++, guiLeft - 16, guiTop + 16, null, (byte) 0, new Rect<Integer>(new Vector2<Integer>(guiLeft - 16, guiTop + 16), new Vector2<Integer>(60, 60), 0xffff0000), EnumConfigType.SIDED_IO);
		sidedIOButton.setActive(false);
		
		GuiConfigButton redstonToggleButton = new GuiConfigButton(counter++, guiLeft - 16, guiTop + 16 + 20, null, (byte) 1, new Rect<Integer>(new Vector2<Integer>(guiLeft - 16, guiTop + 16 + 20), new Vector2<Integer>(75, 35), 0xff0000ff), EnumConfigType.REDSTONE);
		redstonToggleButton.setActive(false);
		
		buttons.addLast(sidedIOButton);
		buttons.addLast(redstonToggleButton); 
		
		GuiRedstoneButton[] redstoneButtons = new GuiRedstoneButton[] {
			new GuiRedstoneButton(counter++, guiLeft - 72 - 8, guiTop + 24 + 20, null, EnumRedstoneType.DISABLED),
			new GuiRedstoneButton(counter++, guiLeft - 72 + 16 - 4, guiTop + 24 + 20, null, EnumRedstoneType.LOW),
			new GuiRedstoneButton(counter++, guiLeft - 72 + 32 - 0, guiTop + 24 + 20, null, EnumRedstoneType.HIGH)
		};
		
		for (int i = 0; i < redstoneButtons.length; i++) {
			if (redstoneButtons[i].getType() != this.te.getRedstoneType()) redstoneButtons[i].setActive(false);
			else redstoneButtons[i].setActive(true);
			
			redstoneButtons[i].visible = false;
			buttons.addLast(redstoneButtons[i]);
		}
		
		waila.finder(false);
		
		getLayoutFromFacingDirection(waila.getSideHit(), counter, guiLeft - 72, guiTop + 38);
		
		IGuiButton current;
		
		for (int i = 0; i < buttons.size(); i++) {
			current = buttons.get(i);
			if (!(current instanceof GuiConfigButton)) ((GuiButton) current).visible = false;
			
			// if (current instanceof GuiButton) this.buttonList.add(current);
			this.buttonList.add((GuiButton) current);
		}
		
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		
		int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        
		this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize - upgradeXOffset, ySize);

		float progress = (float) ((float) this.te.getEnergyStored() / (float) this.te.getMaxStorage()) * 160f;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 61 + 32, 0, 170 + 32, (int) progress, 17);

		upgradePanel.renderContainer(this, f, x, y);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		this.pos.x = guiLeft + 7;
		this.pos.y = guiTop + 61 + 32;

		this.minMax.x = guiLeft + 7 + 162;
		this.minMax.y = guiTop + 61 + 17 + 32;

		if (this.te != null && getComponents() != null && getComponents().size() > 0) {
			getComponents().get(0).update(this.mouseVec, this.pos, this.minMax, new Integer[] { this.te.getEnergyStored(), this.te.getMaxStorage(), te.getEnergyBurnRate() });
		}
		
	}
	
}

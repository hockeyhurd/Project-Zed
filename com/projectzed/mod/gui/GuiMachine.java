/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.hockeyhurd.api.math.Rect;
import com.hockeyhurd.api.math.Vector2;
import com.hockeyhurd.api.util.Waila;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.EnumRedstoneType;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.container.ContainerMachine;
import com.projectzed.mod.gui.component.GuiConfigButton;
import com.projectzed.mod.gui.component.GuiIOButton;
import com.projectzed.mod.gui.component.GuiRedstoneButton;
import com.projectzed.mod.gui.component.IInfoContainer;
import com.projectzed.mod.gui.component.IInfoLabel;
import com.projectzed.mod.gui.component.PowerLabel;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityMachine;

import cpw.mods.fml.client.FMLClientHandler;
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
	
	// button controls:
	protected GuiConfigButton[] configButtons;
	protected HashMap<GuiRedstoneButton, Integer> redstoneButtons;
	protected EnumRedstoneType redstoneType;
	
	protected HashMap<GuiIOButton, Integer> ioButtons;
	protected int startIndexIO = 0;
	protected int guiOffset = 0;
	protected Waila waila;

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
		this.guiOffset = 0; // 76;
		this.xSize = 176 + this.guiOffset;
		this.ySize = 166;

		this.labelList = new ArrayList<IInfoLabel>();
		this.redstoneType = te.getRedstoneType();
		
		EntityPlayer player = (EntityPlayer) FMLClientHandler.instance().getClient().thePlayer;
		
		waila = new Waila(null, player.worldObj, player, null, 0);
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
		
		int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        
		this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);

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

		int guiLeft = (width - xSize) / 2;
		int guiTop = (height - ySize) / 2;
		
		this.labelList.add(new PowerLabel<Integer>(this.pos, this.minMax, this.te.getEnergyStored(), this.te.getMaxStorage(), true));
		
		int counter = 0;
		
		if (configButtons == null || configButtons.length == 0) {
			configButtons = new GuiConfigButton[] {
					new GuiConfigButton(counter++, guiLeft - 16, guiTop + 16, null, (byte) 0, new Rect<Integer>(new Vector2<Integer>(guiLeft - 16, guiTop + 16), new Vector2<Integer>(60, 60), 0xffff0000)),
					new GuiConfigButton(counter++, guiLeft - 16, guiTop + 16 + 20, null, (byte) 1, new Rect<Integer>(new Vector2<Integer>(guiLeft - 16, guiTop + 16 + 20), new Vector2<Integer>(75, 35), 0xff0000ff)), 
			};
		}
		
		if (redstoneButtons == null || redstoneButtons.isEmpty()) {
			redstoneButtons = new HashMap<GuiRedstoneButton, Integer>();
			redstoneButtons.put(new GuiRedstoneButton(counter, guiLeft - 72 - 8, guiTop + 24 + 20, null, EnumRedstoneType.DISABLED), counter++);
			redstoneButtons.put(new GuiRedstoneButton(counter, guiLeft - 72 + 16 - 4, guiTop + 24 + 20, null, EnumRedstoneType.LOW), counter++);
			redstoneButtons.put(new GuiRedstoneButton(counter, guiLeft - 72 + 32 - 0, guiTop + 24 + 20, null, EnumRedstoneType.HIGH), counter++);
		}
		
		for (int i = 0; i < configButtons.length; i++) {
			configButtons[i].setActive(false);
			this.buttonList.add(configButtons[i]);
		}
		
		for (GuiRedstoneButton button : redstoneButtons.keySet()) {
			if (button.getType() != this.te.getRedstoneType()) button.setActive(false);
			else if (button.getType() == this.te.getRedstoneType()) button.setActive(true);

			button.visible = false;
			this.buttonList.add(button);
		}
		
		waila.finder(false);
		
		startIndexIO = counter;
		
		if (ioButtons == null || ioButtons.isEmpty()) ioButtons = new HashMap<GuiIOButton, Integer>();
		
		getLayoutFromFacingDirection(getFacingDirection(waila.getSideHit()), counter, guiLeft - 72, guiTop + 38);
		
		if (ioButtons != null && !ioButtons.isEmpty()) {
			for (GuiIOButton b : ioButtons.keySet()) {
				b.visible = false;
				this.buttonList.add(b);
			}
		}
		
	}

	@Override
	public void actionPerformed(GuiButton button) {
		boolean isActive = false;
		
		if (button.id == 0) {
			ProjectZed.logHelper.info("button.id:", button.id);
			isActive = ((GuiConfigButton) this.buttonList.get(button.id)).isActive();
			
			for (int i = 0; i < configButtons.length; i++) {
				if (button.id == i) continue;
				((GuiConfigButton) this.buttonList.get(i)).setActive(isActive);
				((GuiConfigButton) this.buttonList.get(i)).visible = !((GuiConfigButton) this.buttonList.get(i)).visible;
			}
			
			((GuiConfigButton) this.buttonList.get(button.id)).setActive(isActive);
			
			int index = 0;
			
			for (GuiRedstoneButton redButton : redstoneButtons.keySet()) {
				index = redstoneButtons.get(redButton);
				
				((GuiRedstoneButton) this.buttonList.get(index)).visible = false;
			}
			
			index = 0;
			
			for (GuiIOButton ioButton : ioButtons.keySet()) {
				index = ioButtons.get(ioButton);
				
				if (index >= 0 && this.buttonList.get(index) instanceof GuiIOButton) ((GuiIOButton) this.buttonList.get(index)).visible = isActive;
			}
			
		}
			
		else if (button.id == 1) {
			ProjectZed.logHelper.info("button.id:", button.id);
			isActive = ((GuiConfigButton) this.buttonList.get(button.id)).isActive();
			
			for (int i = 0; i < configButtons.length; i++) {
				if (button.id == i) continue;
				((GuiConfigButton) this.buttonList.get(i)).setActive(isActive);
				((GuiConfigButton) this.buttonList.get(i)).visible = !((GuiConfigButton) this.buttonList.get(i)).visible;
			}
			
			((GuiConfigButton) this.buttonList.get(button.id)).setActive(isActive);
			
			int index = 0;
			
			for (GuiRedstoneButton redButton : redstoneButtons.keySet()) {
				index = redstoneButtons.get(redButton);
				
				((GuiRedstoneButton) this.buttonList.get(index)).visible = isActive;
			}
			
			index = 0;
			
			for (GuiIOButton ioButton : ioButtons.keySet()) {
				index = ioButtons.get(ioButton);
				
				if (index >= 0 && this.buttonList.get(index) instanceof GuiIOButton) ((GuiIOButton) this.buttonList.get(index)).visible = false;
			}
		}
		
		else if (button instanceof GuiRedstoneButton) {
			if (redstoneType == ((GuiRedstoneButton) button).getType()) return;
			else redstoneType = ((GuiRedstoneButton) button).getType();
			
			te.setRedstoneType(redstoneType);
			PacketHandler.INSTANCE.sendToServer(new MessageTileEntityMachine(te));

			for (Object b : this.buttonList) {
				if (!(b instanceof GuiRedstoneButton)) continue;
				if (((GuiRedstoneButton) b).getType() != redstoneType) ((GuiRedstoneButton) b).setActive(false);
				else ((GuiRedstoneButton) b).setActive(true);
			}
		}
		
		else if (button instanceof GuiIOButton) {
			if (!this.isShiftKeyDown()) {
				ForgeDirection dirToSet = getDirectionFromName(button.displayString);	
				
				ProjectZed.logHelper.info("Pre-Val:\t" + te.getSideValve(dirToSet));
				te.setSideValveAndRotate(dirToSet);
				ProjectZed.logHelper.info("Post-Val:\t" + te.getSideValve(dirToSet));
			}
			
			else if (this.isShiftKeyDown()) {
				int index = 0;
				
				for (GuiIOButton ioButton : ioButtons.keySet()) {
					index = ioButtons.get(ioButton);
					
					((GuiIOButton) this.buttonList.get(index)).setStateID((byte) 0);
					te.setSideValve(ForgeDirection.VALID_DIRECTIONS[index - startIndexIO], (byte) 0);
				}
			}
			
			PacketHandler.INSTANCE.sendToServer(new MessageTileEntityMachine(te));
		}
		
		else {
			ProjectZed.logHelper.info("button.id:", button.id);
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
		
	}
	
	/**
	 * NOTE: This function should only be used if this te is instance of TileEntityEnergyBankBase.
	 * 
	 * @param dir = direction player is facing.
	 * @param posX = position x to start drawing button.
	 * @param posY = position y to start drawing button.
	 * @return gui button array for side player is currently facing.
	 */
	private void getLayoutFromFacingDirection(ForgeDirection dir, int index, int posX, int posY) {
		
		if (dir == ForgeDirection.SOUTH) {
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)), index++);

			ioButtons.put(new GuiIOButton(index, posX + 32 + 4, posY + 16 + 2, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 32 + 4, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)), index++);
			ioButtons.put(new GuiIOButton(index, posX, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)), index++);
		}

		else if (dir == ForgeDirection.NORTH) {
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)), index++);

			ioButtons.put(new GuiIOButton(index, posX + 32 + 4, posY + 16 + 2, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 32 + 4, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)), index++);
			ioButtons.put(new GuiIOButton(index, posX, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)), index++);
		}

		else if (dir == ForgeDirection.EAST) {
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)), index++);

			ioButtons.put(new GuiIOButton(index, posX + 32 + 4, posY + 16 + 2, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 32 + 4, posY, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)), index++);
			ioButtons.put(new GuiIOButton(index, posX, posY, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)), index++);
		}

		else if (dir == ForgeDirection.WEST) {
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)), index++);

			ioButtons.put(new GuiIOButton(index, posX + 32 + 4, posY + 16 + 2, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 32 + 4, posY, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)), index++);
			ioButtons.put(new GuiIOButton(index, posX, posY, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)), index++);
		}

		else if (dir == ForgeDirection.DOWN) {
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY + 16 + 2, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY - 16 - 2, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)), index++);

			ioButtons.put(new GuiIOButton(index, posX + 32 + 4, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 32 + 4, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)), index++);
			ioButtons.put(new GuiIOButton(index, posX, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)), index++);
		}

		else {
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY + 16 + 2, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY - 16 - 2, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)), index++);

			ioButtons.put(new GuiIOButton(index, posX + 32 + 4, posY + 16 + 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 16 + 2, posY, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)), index++);
			ioButtons.put(new GuiIOButton(index, posX + 32 + 4, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)), index++);
			ioButtons.put(new GuiIOButton(index, posX, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)), index++);
		}
		
	}
	
	/**
	 * NOTE: This function should only be used if this te is instance of TileEntityEnergyBankBase.
	 * 
	 * @param side = side to get.
	 * @return opposite direction of side 'side'.
	 */
	private ForgeDirection getFacingDirection(int side) {
		return side >= 0 && side < ForgeDirection.VALID_DIRECTIONS.length ? ForgeDirection.VALID_DIRECTIONS[side].getOpposite() : ForgeDirection.UNKNOWN;
	}
	
	/**
	 * NOTE: This function should only be used if this te is instance of TileEntityEnergyBankBase.
	 * 
	 * @param name = name of side.
	 * @return direction associated by button's name.
	 */
	private ForgeDirection getDirectionFromName(String name) {
		ForgeDirection dir = ForgeDirection.UNKNOWN;
		
		if (name.equalsIgnoreCase("n")) dir = ForgeDirection.NORTH;
		else if (name.equalsIgnoreCase("s")) dir = ForgeDirection.SOUTH;
		else if (name.equalsIgnoreCase("e")) dir = ForgeDirection.EAST;
		else if (name.equalsIgnoreCase("w")) dir = ForgeDirection.WEST;
		else if (name.equalsIgnoreCase("t")) dir = ForgeDirection.UP;
		else if (name.equalsIgnoreCase("b")) dir = ForgeDirection.DOWN;
		
		return dir;
	}
	
	/**
	 * NOTE: This function should only be used if this te is instance of TileEntityEnergyBankBase.
	 * 
	 * @param dir = direction to get.
	 * @return value of the 'valve' on side specified.
	 */
	private byte getSideValueFromTE(ForgeDirection dir) {
		return te instanceof AbstractTileEntityMachine ? ((AbstractTileEntityMachine) te).getSideValve(dir) : 0;
	}

}

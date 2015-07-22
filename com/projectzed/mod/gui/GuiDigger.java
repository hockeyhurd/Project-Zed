/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui;

import com.hockeyhurd.api.math.Rect;
import com.hockeyhurd.api.math.Vector2;
import com.hockeyhurd.api.util.Waila;
import com.projectzed.api.tileentity.digger.AbstractTileEntityDigger;
import com.projectzed.api.util.EnumRedstoneType;
import com.projectzed.mod.container.ContainerDigger;
import com.projectzed.mod.gui.component.*;
import com.projectzed.mod.gui.component.GuiConfigButton.EnumConfigType;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityDigger;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Generic class for digger machines.
 * 
 * @author hockeyhurd
 * @version Jun 18, 2015
 */
@SideOnly(Side.CLIENT)
public abstract class GuiDigger extends GuiContainer implements IInfoContainer {

	public ResourceLocation texture;
	protected AbstractTileEntityDigger te;
	private String stringToDraw;
	
	protected Vector2<Integer> mouseVec, pos, minMax;
	protected List<IInfoLabel> labelList;
	protected LinkedList<IGuiButton> buttons;
	protected EnumRedstoneType redstoneType;

	protected Waila waila;
	protected GuiPanelUpgrade upgradePanel;
	protected int upgradeXOffset = 0x20;
	
	public GuiDigger(InventoryPlayer inv, AbstractTileEntityDigger te) {
		super(new ContainerDigger(inv, te));
		this.texture = getResourceTexture();
		if (this.texture == null) this.texture = new ResourceLocation("projectzed", "textures/gui/GuiDefault.png");
		
		this.te = te;
		this.xSize = 176 + upgradeXOffset;
		this.ySize = 166;
		
		this.labelList = new ArrayList<IInfoLabel>();
		this.redstoneType = te.getRedstoneType();
		
		EntityPlayer player = (EntityPlayer) FMLClientHandler.instance().getClient().thePlayer;
		
		waila = new Waila(null, player.worldObj, player, null, 0);
		upgradePanel = new GuiPanelUpgrade(new Vector2<Double>((double) guiLeft + xSize - upgradeXOffset, (double) guiTop));
	}
	
	protected abstract ResourceLocation getResourceTexture();

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		stringToDraw = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName(), new Object[0]);
		
		this.fontRendererObj.drawString(stringToDraw, this.xSize / 2 - this.fontRendererObj.getStringWidth(stringToDraw) / 2, 6, 4210752);
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		
		int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        
		this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize - upgradeXOffset, ySize);

		float progress =  ((float) this.te.getEnergyStored() / (float) this.te.getMaxStorage()) * 160f;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 61, 0, 170, (int) progress, 17);

		upgradePanel.renderContainer(f, x, y);

	}

	@Override
	public void drawScreen(int x, int y, float f) {
		super.drawScreen(x, y, f);
		
		this.mouseVec.x = x;
		this.mouseVec.y = y;
		
		if (visibleComp() != null) this.drawHoveringText(visibleComp().getLabel(), x, y, this.fontRendererObj);
	}
	
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
		
		getLayoutFromFacingDirection(getFacingDirection(waila.getSideHit()), counter, guiLeft - 72, guiTop + 38);
		
		IGuiButton current;
		
		for (int i = 0; i < buttons.size(); i++) {
			current = buttons.get(i);
			if (!(current instanceof GuiConfigButton)) ((GuiButton) current).visible = false;
			
			if (current instanceof GuiButton) this.buttonList.add(current);
		}
		
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		boolean isActive = false;
		
		// ProjectZed.logHelper.info("button.id:", button.id);
		
		if (button instanceof IGuiButton) {
			
			if (button instanceof GuiConfigButton) {
				
				boolean activeState = ((IGuiButton) button).isActive();
				EnumConfigType configType = ((GuiConfigButton) button).getConfigType();
				
				for (int i = 0; i < buttonList.size(); i++) {
					if (!(buttonList.get(i) instanceof IGuiButton)) continue;
					if (i != button.id) {
						if (!(buttonList.get(i) instanceof GuiConfigButton)) {
							((IGuiButton) buttonList.get(i)).setActive(!activeState);
							((GuiButton) buttonList.get(i)).visible = !activeState;
						}
						
						else ((IGuiButton) buttonList.get(i)).setActive(false);
					}
					
				}
					
					
				if (configType == EnumConfigType.SIDED_IO) {
					for (int i = 0; i < buttonList.size(); i++) {
						if (buttonList.get(i) instanceof GuiConfigButton) continue;

						if (buttonList.get(i) instanceof GuiIOButton) {
							((GuiIOButton) buttonList.get(i)).visible = activeState;
						}

						else ((GuiButton) buttonList.get(i)).visible = false;
					}
				}

				else if (configType == EnumConfigType.REDSTONE) {
					for (int i = 0; i < buttonList.size(); i++) {
						if (buttonList.get(i) instanceof GuiConfigButton) continue;

						if (buttonList.get(i) instanceof GuiRedstoneButton) {
							((GuiRedstoneButton) buttonList.get(i)).visible = activeState;
							
							if (((GuiRedstoneButton) buttonList.get(i)).getType() == this.redstoneType) ((GuiRedstoneButton) buttonList.get(i)).setActive(true);
							else ((GuiRedstoneButton) buttonList.get(i)).setActive(false); // ensure all else are set to false!
						}

						else ((GuiButton) buttonList.get(i)).visible = false;
					}
				}
				
			}
			
			else if (button instanceof GuiRedstoneButton) {
				this.redstoneType = ((GuiRedstoneButton) button).getType();
				
				for (int i = 0; i < this.buttonList.size(); i++) {
					if (!(this.buttonList.get(i) instanceof GuiRedstoneButton)) continue;
					
					if (((GuiRedstoneButton) this.buttonList.get(i)).getType() != this.redstoneType) ((GuiRedstoneButton) this.buttonList.get(i)).setActive(false);
					else ((GuiRedstoneButton) this.buttonList.get(i)).setActive(true);
				}
				
				te.setRedstoneType(redstoneType);
			}
			
			else if (button instanceof GuiIOButton) {
				if (!this.isShiftKeyDown()) {
					ForgeDirection dirToSet = getDirectionFromName(button.displayString);

					// ProjectZed.logHelper.info("Pre-Val:\t" + te.getSideValve(dirToSet));
					te.setSideValveAndRotate(dirToSet);
					// ProjectZed.logHelper.info("Post-Val:\t" + te.getSideValve(dirToSet));
				}

				else if (this.isShiftKeyDown()) {
					int index = 0;

					for (int i = 0; i < this.buttonList.size(); i++) {
						if (this.buttonList.get(i) instanceof GuiIOButton) {
							((GuiIOButton) this.buttonList.get(i)).setStateID((byte) 0);
						}
					}

					for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
						te.setSideValve(dir, (byte) 0);
					}

				}
			}
			
			PacketHandler.INSTANCE.sendToServer(new MessageTileEntityDigger(te));
		}
		
		else return;
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
			getComponents().get(0).update(this.mouseVec, this.pos, this.minMax, new Integer[] { this.te.getEnergyStored(), this.te.getMaxStorage(), te.getEnergyBurnRate() });
		}

		upgradePanel.location.x = (double) (guiLeft + xSize - upgradeXOffset);
		upgradePanel.location.y = (double) guiTop;

	}
	
	/**
	 * NOTE: This function should only be used if this te is instance of TileEntityEnergyBankBase.
	 * 
	 * @param dir = direction player is facing.
	 * @param posX = position x to start drawing button.
	 * @param posY = position y to start drawing button.
	 * @return gui button array for side player is currently facing.
	 */
	protected void getLayoutFromFacingDirection(ForgeDirection dir, int index, int posX, int posY) {
		
		if (dir == ForgeDirection.SOUTH) {
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)));
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)));

			buttons.addLast(new GuiIOButton(index++, posX + 32 + 4, posY + 16 + 2, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)));
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)));
			buttons.addLast(new GuiIOButton(index++, posX + 32 + 4, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)));
			buttons.addLast(new GuiIOButton(index++, posX, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)));
		}

		else if (dir == ForgeDirection.NORTH) {
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)));
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)));

			buttons.addLast(new GuiIOButton(index++, posX + 32 + 4, posY + 16 + 2, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)));
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)));
			buttons.addLast(new GuiIOButton(index++, posX + 32 + 4, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)));
			buttons.addLast(new GuiIOButton(index++, posX, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)));
		}

		else if (dir == ForgeDirection.EAST) {
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)));
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)));

			buttons.addLast(new GuiIOButton(index++, posX + 32 + 4, posY + 16 + 2, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)));
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)));
			buttons.addLast(new GuiIOButton(index++, posX + 32 + 4, posY, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)));
			buttons.addLast(new GuiIOButton(index++, posX, posY, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)));
		}

		else if (dir == ForgeDirection.WEST) {
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)));
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)));

			buttons.addLast(new GuiIOButton(index++, posX + 32 + 4, posY + 16 + 2, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)));
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)));
			buttons.addLast(new GuiIOButton(index++, posX + 32 + 4, posY, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)));
			buttons.addLast(new GuiIOButton(index++, posX, posY, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)));
		}

		else if (dir == ForgeDirection.DOWN) {
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY + 16 + 2, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)));
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY - 16 - 2, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)));

			buttons.addLast(new GuiIOButton(index++, posX + 32 + 4, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)));
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)));
			buttons.addLast(new GuiIOButton(index++, posX + 32 + 4, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)));
			buttons.addLast(new GuiIOButton(index++, posX, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)));
		}

		else {
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY + 16 + 2, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)));
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY - 16 - 2, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)));

			buttons.addLast(new GuiIOButton(index++, posX + 32 + 4, posY + 16 + 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)));
			buttons.addLast(new GuiIOButton(index++, posX + 16 + 2, posY, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)));
			buttons.addLast(new GuiIOButton(index++, posX + 32 + 4, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)));
			buttons.addLast(new GuiIOButton(index++, posX, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)));
		}
		
	}
	
	/**
	 * NOTE: This function should only be used if this te is instance of TileEntityEnergyBankBase.
	 * 
	 * @param side = side to get.
	 * @return opposite direction of side 'side'.
	 */
	protected ForgeDirection getFacingDirection(int side) {
		return side >= 0 && side < ForgeDirection.VALID_DIRECTIONS.length ? ForgeDirection.VALID_DIRECTIONS[side].getOpposite() : ForgeDirection.UNKNOWN;
	}
	
	/**
	 * NOTE: This function should only be used if this te is instance of TileEntityEnergyBankBase.
	 * 
	 * @param name = name of side.
	 * @return direction associated by button's name.
	 */
	protected ForgeDirection getDirectionFromName(String name) {
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
	protected byte getSideValueFromTE(ForgeDirection dir) {
		return te instanceof AbstractTileEntityDigger ? ((AbstractTileEntityDigger) te).getSideValve(dir) : 0;
	}
	
}

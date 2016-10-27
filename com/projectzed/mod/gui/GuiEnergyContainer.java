/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.gui;

import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.container.ContainerEnergyContainer;
import com.projectzed.mod.gui.component.GuiIOButton;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityEnergyContainer;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.util.Reference.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import static com.hockeyhurd.hcorelib.api.util.NumberFormatter.format;

/**
 * Class containing code for energy cell gui.
 * 
 * @author hockeyhurd
 * @version Dec 3, 2014
 */
@SideOnly(Side.CLIENT)
public class GuiEnergyContainer extends GuiContainer {

	public final ResourceLocation texture;
	private AbstractTileEntityEnergyContainer te;
	private String stringToDraw;
	
	// This should only be for Energy cells.
	private GuiButton[] buttons;
	private boolean isEnergyCell;
	private final EntityPlayer player;
	
	/**
	 * @param inv
	 * @param te
	 */
	public GuiEnergyContainer(InventoryPlayer inv, AbstractTileEntityEnergyContainer te) {
		super(new ContainerEnergyContainer(inv, te));
		this.te = te;
		this.xSize = 176;
		this.ySize = 192;
		// int slots = this.te.getSizeInventory();
		
		texture = new ResourceLocation("projectzed", "textures/gui/GuiEnergyCell.png");
		
		player = FMLClientHandler.instance().getClient().thePlayer;
		
		isEnergyCell = te instanceof TileEntityEnergyBankBase;
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		String name = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName(), new Object[0]);

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);

		String formatStored = this.te.getEnergyStored() < 1e6 ? format(this.te.getEnergyStored()) : "" + format( (double) this.te.getEnergyStored() / 1e6) + " mil.";
		
		this.stringToDraw = "Power: " + formatStored + " / " + Constants.convertToString(this.te.getMaxStorage()) + " " + Constants.ENERGY_UNIT;
		this.fontRendererObj.drawString(I18n.format(this.stringToDraw, new Object[0]), this.xSize / 2 - this.fontRendererObj.getStringWidth(this.stringToDraw) / 2, this.ySize - 110,
				4210752);
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		float progress = ((float) this.te.getEnergyStored() / (float) this.te.getMaxStorage()) * 160f;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 61 + 32, 0, 202, (int) progress, 17);
	}

	@Override
	public void initGui() {
		super.initGui();
		
		int posX = (this.width - this.xSize) / 2 + 62;
		int posY = (this.height - this.ySize) / 2 + 36;

		if (isEnergyCell) {
			
			// this.buttons = getLayoutFromFacingDirection(waila.getSideHit(), posX, posY);
			this.buttons = getLayoutFromFacingDirection(player.getHorizontalFacing(), posX, posY);

			if (this.buttons != null) {
				for (GuiButton b : buttons) {
					// b.visible = false;
					this.buttonList.add(b);
				}
			}
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
	private GuiButton[] getLayoutFromFacingDirection(EnumFacing dir, int posX, int posY) {
		GuiButton[] buttons = null;
		
		if (dir == EnumFacing.SOUTH) {
			buttons = new GuiIOButton[] {
					new GuiIOButton(this, 0, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(EnumFacing.DOWN)),
					new GuiIOButton(this, 1, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(EnumFacing.UP)),
					
					new GuiIOButton(this, 2, posX + 32 + 4, posY + 16 + 2, 16, 16, "S", getSideValueFromTE(EnumFacing.SOUTH)),
					new GuiIOButton(this, 3, posX + 16 + 2, posY, 16, 16, "N", getSideValueFromTE(EnumFacing.NORTH)),
					new GuiIOButton(this, 4, posX + 32 + 4, posY, 16, 16, "W", getSideValueFromTE(EnumFacing.WEST)),
					new GuiIOButton(this, 5, posX, posY, 16, 16, "E", getSideValueFromTE(EnumFacing.EAST))
			};
		}
		
		else if (dir == EnumFacing.NORTH) {
			buttons = new GuiIOButton[] {
					new GuiIOButton(this, 0, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(EnumFacing.DOWN)),
					new GuiIOButton(this, 1, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(EnumFacing.UP)),
					
					new GuiIOButton(this, 2, posX + 32 + 4, posY + 16 + 2, 16, 16, "N", getSideValueFromTE(EnumFacing.NORTH)),
					new GuiIOButton(this, 3, posX + 16 + 2, posY, 16, 16, "S", getSideValueFromTE(EnumFacing.SOUTH)),
					new GuiIOButton(this, 4, posX + 32 + 4, posY, 16, 16, "E", getSideValueFromTE(EnumFacing.EAST)),
					new GuiIOButton(this, 5, posX, posY, 16, 16, "W", getSideValueFromTE(EnumFacing.WEST))
			};
		}
		
		else if (dir == EnumFacing.EAST) {
			buttons = new GuiIOButton[] {
					new GuiIOButton(this, 0, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(EnumFacing.DOWN)),
					new GuiIOButton(this, 1, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(EnumFacing.UP)),
					
					new GuiIOButton(this, 2, posX + 32 + 4, posY + 16 + 2, 16, 16, "E", getSideValueFromTE(EnumFacing.EAST)),
					new GuiIOButton(this, 3, posX + 16 + 2, posY, 16, 16, "W", getSideValueFromTE(EnumFacing.WEST)),
					new GuiIOButton(this, 4, posX + 32 + 4, posY, 16, 16, "S", getSideValueFromTE(EnumFacing.SOUTH)),
					new GuiIOButton(this, 5, posX, posY, 16, 16, "N", getSideValueFromTE(EnumFacing.NORTH))
			};
		}
		
		else if (dir == EnumFacing.WEST) {
			buttons = new GuiIOButton[] {
					new GuiIOButton(this, 0, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(EnumFacing.DOWN)),
					new GuiIOButton(this, 1, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(EnumFacing.UP)),
					
					new GuiIOButton(this, 2, posX + 32 + 4, posY + 16 + 2, 16, 16, "W", getSideValueFromTE(EnumFacing.WEST)),
					new GuiIOButton(this, 3, posX + 16 + 2, posY, 16, 16, "E", getSideValueFromTE(EnumFacing.EAST)),
					new GuiIOButton(this, 4, posX + 32 + 4, posY, 16, 16, "N", getSideValueFromTE(EnumFacing.NORTH)),
					new GuiIOButton(this, 5, posX, posY, 16, 16, "S", getSideValueFromTE(EnumFacing.SOUTH))
			};
		}
		
		else if (dir == EnumFacing.DOWN) {
			buttons = new GuiIOButton[] {
					new GuiIOButton(this, 0, posX + 16 + 2, posY + 16 + 2, 16, 16, "S", getSideValueFromTE(EnumFacing.SOUTH)),
					new GuiIOButton(this, 1, posX + 16 + 2, posY - 16 - 2, 16, 16, "N", getSideValueFromTE(EnumFacing.NORTH)),
					
					new GuiIOButton(this, 2, posX + 32 + 4, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(EnumFacing.DOWN)),
					new GuiIOButton(this, 3, posX + 16 + 2, posY, 16, 16, "T", getSideValueFromTE(EnumFacing.UP)),
					new GuiIOButton(this, 4, posX + 32 + 4, posY, 16, 16, "E", getSideValueFromTE(EnumFacing.EAST)),
					new GuiIOButton(this, 5, posX, posY, 16, 16, "W", getSideValueFromTE(EnumFacing.WEST))
			};
		}
		
		else {
			buttons = new GuiIOButton[] {
					new GuiIOButton(this, 0, posX + 16 + 2, posY + 16 + 2, 16, 16, "S", getSideValueFromTE(EnumFacing.SOUTH)),
					new GuiIOButton(this, 1, posX + 16 + 2, posY - 16 - 2, 16, 16, "N", getSideValueFromTE(EnumFacing.NORTH)),
					
					new GuiIOButton(this, 2, posX + 32 + 4, posY + 16 + 2, 16, 16, "T", getSideValueFromTE(EnumFacing.UP)),
					new GuiIOButton(this, 3, posX + 16 + 2, posY, 16, 16, "B", getSideValueFromTE(EnumFacing.DOWN)),
					new GuiIOButton(this, 4, posX + 32 + 4, posY, 16, 16, "E", getSideValueFromTE(EnumFacing.EAST)),
					new GuiIOButton(this, 5, posX, posY, 16, 16, "W", getSideValueFromTE(EnumFacing.WEST))
			};
		}
		
		return buttons;
	}

	/**
	 * NOTE: This function should only be used if this te is instance of TileEntityEnergyBankBase.
	 * 
	 * @param name = name of side.
	 * @return direction associated by button's name.
	 */
	private EnumFacing getDirectionFromName(String name) {
		EnumFacing dir = null;
		
		if (name.equalsIgnoreCase("n")) dir = EnumFacing.NORTH;
		else if (name.equalsIgnoreCase("s")) dir = EnumFacing.SOUTH;
		else if (name.equalsIgnoreCase("e")) dir = EnumFacing.EAST;
		else if (name.equalsIgnoreCase("w")) dir = EnumFacing.WEST;
		else if (name.equalsIgnoreCase("t")) dir = EnumFacing.UP;
		else if (name.equalsIgnoreCase("b")) dir = EnumFacing.DOWN;
		
		return dir;
	}
	
	/**
	 * NOTE: This function should only be used if this te is instance of TileEntityEnergyBankBase.
	 * 
	 * @param dir = direction to get.
	 * @return value of the 'valve' on side specified.
	 */
	private byte getSideValueFromTE(EnumFacing dir) {
		return te instanceof TileEntityEnergyBankBase ? ((TileEntityEnergyBankBase) te).getSideValve(dir) : 0;
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (isEnergyCell && button.id >= 0 && button.id < buttons.length) {
			TileEntityEnergyBankBase te = (TileEntityEnergyBankBase) this.te;
			
			if (!isShiftKeyDown()) {
				EnumFacing dirToSet = getDirectionFromName(button.displayString);
				
				ProjectZed.logHelper.info("Pre-Val:\t" + te.getSideValve(dirToSet));
				te.setSideValveAndRotate(dirToSet);
				ProjectZed.logHelper.info("Post-Val:\t" + te.getSideValve(dirToSet));
			}
			
			else if (isShiftKeyDown()) {
				// ProjectZed.logHelper.info(true);
				for (EnumFacing dir : EnumFacing.VALUES) {
					if (this.buttons[dir.ordinal()] instanceof GuiIOButton) ((GuiIOButton) this.buttons[dir.ordinal()]).setStateID((byte) 0);
					te.setSideValve(dir, (byte) 0);
				}
			}

			PacketHandler.INSTANCE.sendToServer(new MessageTileEntityEnergyContainer(te));
		}
	}

}

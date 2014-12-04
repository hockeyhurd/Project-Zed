package com.projectzed.mod.gui;

import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.hockeyhurd.api.util.Waila;
import com.projectzed.api.tileentity.container.AbstractTileEntityContainer;
import com.projectzed.mod.container.ContainerEnergyContainer;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.util.Reference.Constants;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for energy cell gui.
 * 
 * @author hockeyhurd
 * @version Dec 3, 2014
 */
@SideOnly(Side.CLIENT)
public class GuiEnergyContainer extends GuiContainer {

	public final ResourceLocation texture;
	private AbstractTileEntityContainer te;
	private String stringToDraw;
	private final DecimalFormat df = new DecimalFormat("###,###,###");
	
	// This should only be for Energy cells.
	private GuiButton[] buttons;
	private boolean isEnergyCell;
	
	public GuiEnergyContainer(InventoryPlayer inv, AbstractTileEntityContainer te) {
		super(new ContainerEnergyContainer(inv, te));
		this.te = te;
		this.xSize = 176;
		this.ySize = 192;
		int slots = this.te.getSizeInventory();
		
		texture = new ResourceLocation("projectzed", "textures/gui/GuiEnergyCell.png");
		
		isEnergyCell = te instanceof TileEntityEnergyBankBase;
	}

	public void drawGuiContainerForegroundLayer(int x, int y) {
		String name = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName(), new Object[0]);

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		// this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);

		this.stringToDraw = "Power: " + df.format(this.te.getEnergyStored()) + " / " + df.format(this.te.getMaxStorage()) + " " + Constants.ENERGY_UNIT;
		this.fontRendererObj.drawString(I18n.format(this.stringToDraw, new Object[0]), this.xSize / 2 - this.fontRendererObj.getStringWidth(this.stringToDraw) / 2, this.ySize - 110,
				4210752);
	}

	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		float progress = (float) ((float) this.te.getEnergyStored() / (float) this.te.getMaxStorage()) * 160f;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 61 + 32, 0, 202, (int) progress, 17);
	}
	
	public void initGui() {
		super.initGui();
		
		int posX = (this.width - this.xSize) / 2 + 62;
		int posY = (this.height - this.ySize) / 2 + 36;

		if (isEnergyCell) {
			EntityPlayer player = (EntityPlayer) FMLClientHandler.instance().getClient().thePlayer;
			
			Waila waila = new Waila(null, player.worldObj, player, null, 0);
			waila.finder(false);
			
			this.buttons = getLayoutFromFacingDirection(getFacingDirection(waila.getSideHit()), posX, posY);
	
			if (this.buttons != null) {
				for (GuiButton b : buttons) {
					// b.visible = false;
					this.buttonList.add(b);
				}
			}
		}
		
	}
	
	private GuiButton[] getLayoutFromFacingDirection(ForgeDirection dir, int posX, int posY) {
		GuiButton[] buttons = null;
		
		if (dir == ForgeDirection.SOUTH) {
			buttons = new GuiButton[] {
					new GuiButton(0, posX + 16 + 2, posY + 16 + 2, 16, 16, "B"),
					new GuiButton(1, posX + 16 + 2, posY - 16 - 2, 16, 16, "T"),
					
					
					new GuiButton(2, posX + 32 + 4, posY + 16 + 2, 16, 16, "N"),
					new GuiButton(3, posX + 16 + 2, posY, 16, 16, "S"),
					new GuiButton(4, posX + 32 + 4, posY, 16, 16, "W"),
					new GuiButton(5, posX, posY, 16, 16, "E")
			};
		}
		
		else if (dir == ForgeDirection.EAST) {
			buttons = new GuiButton[] {
					new GuiButton(0, posX + 16 + 2, posY + 16 + 2, 16, 16, "B"),
					new GuiButton(1, posX + 16 + 2, posY - 16 - 2, 16, 16, "T"),
					
					
					new GuiButton(2, posX + 32 + 4, posY + 16 + 2, 16, 16, "W"),
					new GuiButton(3, posX + 16 + 2, posY, 16, 16, "E"),
					new GuiButton(4, posX + 32 + 4, posY, 16, 16, "S"),
					new GuiButton(5, posX, posY, 16, 16, "N")
			};
		}
		
		else if (dir == ForgeDirection.WEST) {
			buttons = new GuiButton[] {
					new GuiButton(0, posX + 16 + 2, posY + 16 + 2, 16, 16, "B"),
					new GuiButton(1, posX + 16 + 2, posY - 16 - 2, 16, 16, "T"),
					
					
					new GuiButton(2, posX + 32 + 4, posY + 16 + 2, 16, 16, "E"),
					new GuiButton(3, posX + 16 + 2, posY, 16, 16, "W"),
					new GuiButton(4, posX + 32 + 4, posY, 16, 16, "N"),
					new GuiButton(5, posX, posY, 16, 16, "S")
			};
		}
		
		else {
			buttons = new GuiButton[] {
					new GuiButton(0, posX + 16 + 2, posY + 16 + 2, 16, 16, "B"),
					new GuiButton(1, posX + 16 + 2, posY - 16 - 2, 16, 16, "T"),
					
					
					new GuiButton(2, posX + 32 + 4, posY + 16 + 2, 16, 16, "S"),
					new GuiButton(3, posX + 16 + 2, posY, 16, 16, "N"),
					new GuiButton(4, posX + 32 + 4, posY, 16, 16, "E"),
					new GuiButton(5, posX, posY, 16, 16, "W")
			};
		}
		
		return buttons;
	}
	
	private ForgeDirection getFacingDirection(int side) {
		return side >= 0 && side < ForgeDirection.VALID_DIRECTIONS.length ? ForgeDirection.VALID_DIRECTIONS[side].getOpposite() : ForgeDirection.UNKNOWN;
	}
	
	public void actionPerformed(GuiButton button) {
		if (isEnergyCell && button.id >= 0 && button.id < buttons.length) {
			System.out.println(button.id);
			
			
			// Do packet handling here!
		}
	}

}

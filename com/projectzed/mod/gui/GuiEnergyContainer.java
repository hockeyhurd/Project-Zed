package com.projectzed.mod.gui;

import static com.hockeyhurd.api.util.NumberFormatter.format;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.hockeyhurd.api.math.Mathd;
import com.hockeyhurd.api.util.Waila;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.container.ContainerEnergyContainer;
import com.projectzed.mod.gui.component.GuiIOButton;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityContainer;
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
	private AbstractTileEntityEnergyContainer te;
	private String stringToDraw;
	
	// This should only be for Energy cells.
	private GuiButton[] buttons;
	private boolean isEnergyCell;
	private Waila waila;
	
	/**
	 * @param inv
	 * @param te
	 */
	public GuiEnergyContainer(InventoryPlayer inv, AbstractTileEntityEnergyContainer te) {
		super(new ContainerEnergyContainer(inv, te));
		this.te = te;
		this.xSize = 176;
		this.ySize = 192;
		int slots = this.te.getSizeInventory();
		
		texture = new ResourceLocation("projectzed", "textures/gui/GuiEnergyCell.png");
		
		EntityPlayer player = (EntityPlayer) FMLClientHandler.instance().getClient().thePlayer;
		
		waila = new Waila(null, player.worldObj, player, null, 0);
		
		isEnergyCell = te instanceof TileEntityEnergyBankBase;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#drawGuiContainerForegroundLayer(int, int)
	 */
	public void drawGuiContainerForegroundLayer(int x, int y) {
		String name = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName(), new Object[0]);

		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);

		String formatStored = this.te.getEnergyStored() < 1e6 ? format(this.te.getEnergyStored()) : "" + format( (double) this.te.getEnergyStored() / 1e6) + " mil.";
		
		this.stringToDraw = "Power: " + formatStored + " / " + Constants.convertToString(this.te.getMaxStorage()) + " " + Constants.ENERGY_UNIT;
		this.fontRendererObj.drawString(I18n.format(this.stringToDraw, new Object[0]), this.xSize / 2 - this.fontRendererObj.getStringWidth(this.stringToDraw) / 2, this.ySize - 110,
				4210752);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#drawGuiContainerBackgroundLayer(float, int, int)
	 */
	public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		float progress = (float) ((float) this.te.getEnergyStored() / (float) this.te.getMaxStorage()) * 160f;
		this.drawTexturedModalRect(guiLeft + 7, guiTop + 61 + 32, 0, 202, (int) progress, 17);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#initGui()
	 */
	public void initGui() {
		super.initGui();
		
		int posX = (this.width - this.xSize) / 2 + 62;
		int posY = (this.height - this.ySize) / 2 + 36;

		if (isEnergyCell) {
			
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
	
	/**
	 * NOTE: This function should only be used if this te is instance of TileEntityEnergyBankBase.
	 * 
	 * @param dir = direction player is facing.
	 * @param posX = position x to start drawing button.
	 * @param posY = position y to start drawing button.
	 * @return gui button array for side player is currently facing.
	 */
	private GuiButton[] getLayoutFromFacingDirection(ForgeDirection dir, int posX, int posY) {
		GuiButton[] buttons = null;
		
		if (dir == ForgeDirection.SOUTH) {
			buttons = new GuiIOButton[] {
					new GuiIOButton(0, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)),
					new GuiIOButton(1, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)),
					
					new GuiIOButton(2, posX + 32 + 4, posY + 16 + 2, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)),
					new GuiIOButton(3, posX + 16 + 2, posY, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)),
					new GuiIOButton(4, posX + 32 + 4, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)),
					new GuiIOButton(5, posX, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST))
			};
		}
		
		else if (dir == ForgeDirection.NORTH) {
			buttons = new GuiIOButton[] {
					new GuiIOButton(0, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)),
					new GuiIOButton(1, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)),
					
					new GuiIOButton(2, posX + 32 + 4, posY + 16 + 2, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)),
					new GuiIOButton(3, posX + 16 + 2, posY, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)),
					new GuiIOButton(4, posX + 32 + 4, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)),
					new GuiIOButton(5, posX, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST))
			};
		}
		
		else if (dir == ForgeDirection.EAST) {
			buttons = new GuiIOButton[] {
					new GuiIOButton(0, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)),
					new GuiIOButton(1, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)),
					
					new GuiIOButton(2, posX + 32 + 4, posY + 16 + 2, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)),
					new GuiIOButton(3, posX + 16 + 2, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)),
					new GuiIOButton(4, posX + 32 + 4, posY, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)),
					new GuiIOButton(5, posX, posY, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH))
			};
		}
		
		else if (dir == ForgeDirection.WEST) {
			buttons = new GuiIOButton[] {
					new GuiIOButton(0, posX + 16 + 2, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)),
					new GuiIOButton(1, posX + 16 + 2, posY - 16 - 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)),
					
					new GuiIOButton(2, posX + 32 + 4, posY + 16 + 2, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST)),
					new GuiIOButton(3, posX + 16 + 2, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)),
					new GuiIOButton(4, posX + 32 + 4, posY, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)),
					new GuiIOButton(5, posX, posY, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH))
			};
		}
		
		else if (dir == ForgeDirection.DOWN) {
			buttons = new GuiIOButton[] {
					new GuiIOButton(0, posX + 16 + 2, posY + 16 + 2, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)),
					new GuiIOButton(1, posX + 16 + 2, posY - 16 - 2, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)),
					
					new GuiIOButton(2, posX + 32 + 4, posY + 16 + 2, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)),
					new GuiIOButton(3, posX + 16 + 2, posY, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)),
					new GuiIOButton(4, posX + 32 + 4, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)),
					new GuiIOButton(5, posX, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST))
			};
		}
		
		else {
			buttons = new GuiIOButton[] {
					new GuiIOButton(0, posX + 16 + 2, posY + 16 + 2, 16, 16, "S", getSideValueFromTE(ForgeDirection.SOUTH)),
					new GuiIOButton(1, posX + 16 + 2, posY - 16 - 2, 16, 16, "N", getSideValueFromTE(ForgeDirection.NORTH)),
					
					new GuiIOButton(2, posX + 32 + 4, posY + 16 + 2, 16, 16, "T", getSideValueFromTE(ForgeDirection.UP)),
					new GuiIOButton(3, posX + 16 + 2, posY, 16, 16, "B", getSideValueFromTE(ForgeDirection.DOWN)),
					new GuiIOButton(4, posX + 32 + 4, posY, 16, 16, "E", getSideValueFromTE(ForgeDirection.EAST)),
					new GuiIOButton(5, posX, posY, 16, 16, "W", getSideValueFromTE(ForgeDirection.WEST))
			};
		}
		
		return buttons;
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
		return te instanceof TileEntityEnergyBankBase ? ((TileEntityEnergyBankBase) te).getSideValve(dir) : 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.GuiScreen#actionPerformed(net.minecraft.client.gui.GuiButton)
	 */
	public void actionPerformed(GuiButton button) {
		if (isEnergyCell && button.id >= 0 && button.id < buttons.length) {
			ForgeDirection dirToSet = getDirectionFromName(button.displayString);
			
			TileEntityEnergyBankBase te = (TileEntityEnergyBankBase) this.te;
			ProjectZed.logHelper.info("Pre-Val:\t" + te.getSideValve(dirToSet));
			te.setSideValveAndRotate(dirToSet);
			ProjectZed.logHelper.info("Post-Val:\t" + te.getSideValve(dirToSet));
			
			PacketHandler.INSTANCE.sendToServer(new MessageTileEntityContainer(te));
		}
	}

}

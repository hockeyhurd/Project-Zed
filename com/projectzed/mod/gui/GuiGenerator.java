package com.projectzed.mod.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.container.ContainerGenerator;
import com.projectzed.mod.gui.component.IInfoContainer;
import com.projectzed.mod.gui.component.IInfoLabel;
import com.projectzed.mod.gui.component.PowerLabel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Generic class for gui's of generators.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
@SideOnly(Side.CLIENT)
public class GuiGenerator extends GuiContainer implements IInfoContainer {

	public final ResourceLocation texture;
	private AbstractTileEntityGenerator te;
	private String stringToDraw;
	private final DecimalFormat df = new DecimalFormat("###,###,###");

	protected Vector4Helper<Integer> mouseVec, pos, minMax, distOffset;
	protected List<IInfoLabel> labelList;
	protected int genRate, stored, maxStorage;

	/**
	 * @param inv
	 * @param te
	 */
	public GuiGenerator(InventoryPlayer inv, AbstractTileEntityGenerator te) {
		super(new ContainerGenerator(inv, te));
		this.te = te;
		this.xSize = 176;
		this.ySize = 166;
		int slots = this.te.getSizeInventory();

		texture = new ResourceLocation("projectzed", "textures/gui/GuiGenerator_generic" + slots + ".png");
		this.labelList = new ArrayList<IInfoLabel>();

		this.stored = this.te.getEnergyStored();
		this.maxStorage = this.te.getMaxStorage();
		this.genRate = te.getSource().getEffectiveSize();
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

		String genSize = "Generation rate: " + df.format(genRate) + " McU/t";
		// if (this.te instanceof TileEntityNuclear &&
		// !this.te.canProducePower()) genSize =
		// "Error! Reactor not set correctly!";
		this.fontRendererObj.drawString(I18n.format(genSize, new Object[0]), this.xSize / 2 - this.fontRendererObj.getStringWidth(genSize) / 2,
				this.ySize - 116, 4210752);

		if (visibleComp() != null) {
			this.drawHoveringText(visibleComp().getLabel(), (Integer) visibleComp().getPos().x, (Integer) visibleComp().getPos().y,
					this.fontRendererObj);
		}
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
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#drawScreen(int, int,
	 * float)
	 */
	@Override
	public void drawScreen(int x, int y, float f) {
		this.mouseVec.x = x;
		this.mouseVec.y = y;

		super.drawScreen(x, y, f);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#updateScreen()
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();

		this.pos.x = guiLeft + 7;
		this.pos.y = guiTop + 61;

		this.minMax.x = guiLeft + 7 + 162;
		this.minMax.y = guiTop + 61 + 17;

		if (this.te != null) {
			this.stored = this.te.getEnergyStored();
			this.maxStorage = this.te.getMaxStorage();
			this.genRate = this.te.getSource().getEffectiveSize() > 0 && this.te.canProducePower()
					&& this.stored + this.te.getSource().getEffectiveSize() <= this.maxStorage ? this.te.getSource().getEffectiveSize() : 0;

			if (getComponents() != null && getComponents().size() > 0) {
				getComponents().get(0).update(this.mouseVec, this.pos, this.minMax, this.te.getEnergyStored(), this.te.getMaxStorage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.gui.inventory.GuiContainer#initGui()
	 */
	@Override
	public void initGui() {
		super.initGui();

		this.mouseVec = Vector4Helper.zero;
		this.pos = new Vector4Helper<Integer>(guiLeft + 7, guiTop + 61, 0);
		this.minMax = new Vector4Helper<Integer>(guiLeft + 7 + 162, guiTop + 61 + 17, 0);
		this.distOffset = new Vector4Helper<Integer>(this.mouseVec.x, 20, 0);

		this.labelList.add(new PowerLabel<Integer>(this.pos, this.minMax, this.distOffset, this.te.getEnergyStored(), this.te.getMaxStorage()));
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

}

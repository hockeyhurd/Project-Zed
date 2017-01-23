package com.projectzed.mod.gui;

import com.projectzed.mod.container.ContainerStorageUnit;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialStorageUnit;
import com.projectzed.mod.util.BigItemStack;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Gui class for TileEntityIndustrialStorageUnit.
 *
 * @author hockeyhurd
 * @version 1/21/2017.
 */
@SideOnly(Side.CLIENT)
public class GuiStorageUnit extends GuiMachine {

	public GuiStorageUnit(InventoryPlayer inventory, TileEntityIndustrialStorageUnit te) {
		super(new ContainerStorageUnit(inventory, te), inventory, te);

		texture = new ResourceLocation("projectzed", "textures/gui/GuiMachine_generic_no_arrow.png");
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);

		String drawString;
		final BigItemStack bigItemStack = ((TileEntityIndustrialStorageUnit) te).getBigItemStack();

		if (bigItemStack.isEmpty()) drawString = "Amount: 0";
		else drawString = String.format("Amount: %d", bigItemStack.getAmount());

		// this.fontRendererObj.drawString(stringToDraw, this.xSize / 2 - this.fontRendererObj.getStringWidth(stringToDraw) / 2 - (upgradeXOffset / 2), 6, 4210752);
		fontRendererObj.drawString(drawString, (xSize >> 1) - (fontRendererObj.getStringWidth(drawString) >> 1) - (upgradeXOffset >> 1), 18, 4210752);
	}
}

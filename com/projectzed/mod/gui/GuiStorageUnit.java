package com.projectzed.mod.gui;

import com.projectzed.mod.tileentity.machine.TileEntityIndustrialStorageUnit;
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
		super(inventory, te);

		texture = new ResourceLocation("projectzed", "textures/gui/GuiMachine_generic_no_arrow.png");
	}

}

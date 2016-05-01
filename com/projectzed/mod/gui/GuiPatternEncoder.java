/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.gui;

import com.projectzed.mod.container.ContainerPatternEncoder;
import com.projectzed.mod.tileentity.machine.TileEntityPatternEncoder;
import com.projectzed.mod.util.Reference;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * @author hockeyhurd
 * @version 4/30/2016.
 */
public class GuiPatternEncoder extends GuiMachine {

	public GuiPatternEncoder(InventoryPlayer inv, TileEntityPatternEncoder te) {
		super(new ContainerPatternEncoder(inv, te), inv, te);

		this.texture = new ResourceLocation(Reference.MOD_NAME.toLowerCase(), "textures/gui/GuiPatternEncoder.png");
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
	}

}

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
import com.projectzed.mod.gui.component.GuiClearButton;
import com.projectzed.mod.gui.component.GuiEncodeButton;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityPatternEncoder;
import com.projectzed.mod.tileentity.interfaces.IEncodable;
import com.projectzed.mod.tileentity.machine.TileEntityPatternEncoder;
import com.projectzed.mod.util.Reference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Gui clas for pattern encoder.
 *
 * @author hockeyhurd
 * @version 4/30/2016.
 */
@SideOnly(Side.CLIENT)
public class GuiPatternEncoder extends GuiMachine {

	private final IEncodable encodable;
	private GuiClearButton clearButton;
	private GuiEncodeButton encodeButton;

	public GuiPatternEncoder(InventoryPlayer inv, TileEntityPatternEncoder te) {
		super(new ContainerPatternEncoder(inv, te), inv, te);

		this.texture = new ResourceLocation(Reference.MOD_NAME.toLowerCase(), "textures/gui/GuiPatternEncoder.png");
		this.encodable = te;
	}

	@Override
	public void drawGuiContainerForegroundLayer(int x, int y) {
	}

	@Override
	public void initGui() {
		super.initGui();

		clearButton = new GuiClearButton(buttonList.size(), pos.x - 2, pos.y - 0x35, "");
		buttonList.add(clearButton);

		encodeButton = new GuiEncodeButton(this, buttonList.size(), pos.x + 70, pos.y - 0x10, "");
		buttonList.add(encodeButton);
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == clearButton.id) {
			((ContainerPatternEncoder)inventorySlots).clearSlots();
			PacketHandler.INSTANCE.sendToServer(new MessageTileEntityPatternEncoder((TileEntityPatternEncoder) te,
					MessageTileEntityPatternEncoder.CLEAR));
		}

		else if (button.id == encodeButton.id) {
			encodable.encode(false);
			PacketHandler.INSTANCE.sendToServer(
					new MessageTileEntityPatternEncoder((TileEntityPatternEncoder) te, MessageTileEntityPatternEncoder.ENCODE));
		}

		else super.actionPerformed(button);
	}
}

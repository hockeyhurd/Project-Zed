/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.gui.config;

import com.hockeyhurd.hcorelib.api.client.gui.ModGuiConfig;
import com.projectzed.mod.ProjectZed;
import net.minecraft.client.gui.GuiScreen;

/**
 * Gui config class for ProjectZed.
 *
 * @author hockeyhurd
 * @version 4/26/2016.
 */
public final class PZGuiConfig extends ModGuiConfig {

	public PZGuiConfig(GuiScreen parent) {
		super(parent, ProjectZed.configHandler, ProjectZed.modID, ProjectZed.modID + " config");
	}

}

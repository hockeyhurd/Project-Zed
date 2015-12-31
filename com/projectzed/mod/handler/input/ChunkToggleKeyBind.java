/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.handler.input;

import com.hockeyhurd.api.handler.input.AbstractKeyBinding;
import com.projectzed.mod.renderer.ChunkLoaderWorldRenderer;
import com.projectzed.mod.util.Reference;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * KeyBind class for toggling rendering of chunk loader.
 *
 * @author hockeyhurd
 * @version 12/30/2015.
 */
@SideOnly(Side.CLIENT)
public final class ChunkToggleKeyBind extends AbstractKeyBinding {

	private final ChunkLoaderWorldRenderer chunkLoaderWorldRenderer;

	/**
	 * Creates ChunkToggleKeyBind KeyBinding.
	 *
	 * @param chunkLoaderWorldRenderer Instance of ChunkLoaderWorldRenderer.
	 */
	public ChunkToggleKeyBind(ChunkLoaderWorldRenderer chunkLoaderWorldRenderer) {
		super("Chunk Loader Overlay", Keyboard.KEY_F4, Reference.MOD_NAME);

		this.chunkLoaderWorldRenderer = chunkLoaderWorldRenderer;
	}

	@Override
	protected void activate(KeyInputEvent event) {
		// ProjectZed.logHelper.info("onKeyPressed called!");
		chunkLoaderWorldRenderer.toggleVisibility();
	}

}

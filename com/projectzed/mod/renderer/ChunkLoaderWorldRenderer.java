/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.renderer;

import com.hockeyhurd.api.math.Color4i;
import com.hockeyhurd.api.math.Rect;
import com.hockeyhurd.api.math.Vector3;
import com.hockeyhurd.api.renderer.EnumShape;
import com.hockeyhurd.api.renderer.IWorldRenderable;
import com.hockeyhurd.api.renderer.ShapeRendererUtils;
import com.hockeyhurd.api.util.ChunkHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * World renderer for chunk loading overlay.
 *
 * @author hockeyhurd
 * @version 12/28/2015.
 */
@SideOnly(Side.CLIENT)
public class ChunkLoaderWorldRenderer implements IWorldRenderable {

	private static final ChunkLoaderWorldRenderer instance = new ChunkLoaderWorldRenderer();

	private final Color4i CHUNK_COLOR = new Color4i(0x7f00ff00);
	private boolean visible = false;
	private EntityPlayer player;

	private ChunkLoaderWorldRenderer() {
	}

	/** Instance of class. */
	public static ChunkLoaderWorldRenderer instance() {
		return instance;
	}

	/**
	 * Get current visibility.
	 *
	 * @return Current visibility.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets visibility.
	 *
	 * @param visible Boolean flag.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Toggles visibility.
	 */
	public void toggleVisibility() {
		visible = !visible;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(RenderWorldLastEvent event) {
		if (player == null) player = Minecraft.getMinecraft().thePlayer;

		if (visible) {
			Vector3<Float> playerVec = new Vector3<Double>(player.posX, player.posY, player.posZ).getVector3f();

			final Rect<Integer> chunkRect = ChunkHelper.getChunkBounds(ChunkHelper.getChunkFromPlayerLocation(player));

			Vector3<Float> minVec = chunkRect.min.toVector3().getVector3f();
			minVec.z = minVec.y;
			minVec.y = playerVec.y;

			Vector3<Float> maxVec = chunkRect.max.toVector3().getVector3f();
			maxVec.z = maxVec.y;
			maxVec.y = playerVec.y + 1.0f;

			playerVec.x = (float) (player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks);
			playerVec.y = (float) (player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks);
			playerVec.z = (float) (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks);

			minVec.x *= 0x10;
			minVec.z *= 0x10;
			maxVec.x *= 0x10;
			maxVec.z *= 0x10;

			minVec.y = playerVec.y + 20.0f;

			ShapeRendererUtils.renderShape(EnumShape.SQUARE, new Vector3[] { minVec, maxVec }, playerVec, CHUNK_COLOR );
		}
	}

}

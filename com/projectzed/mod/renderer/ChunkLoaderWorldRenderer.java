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
import com.projectzed.api.util.IChunkLoadable;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.ChunkLoaderManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * World renderer for chunk loading overlay.
 *
 * @author hockeyhurd
 * @version 12/28/2015.
 */
@SideOnly(Side.CLIENT)
public class ChunkLoaderWorldRenderer implements IWorldRenderable {

	private static final ChunkLoaderWorldRenderer instance = new ChunkLoaderWorldRenderer();
	private static final ChunkLoaderManager managerInstance = ChunkLoaderManager.instance();

	private final Color4i CHUNK_COLOR = new Color4i(0x7f00ff00);
	private boolean visible = false;
	private Minecraft minecraft;
	private EntityPlayer player;
	private int timer = 0;
	private boolean forceReload = false;
	private int lastDimensionID, currentDimensionID;
	private IChunkLoadable[] chunkLoadables;
	private List<Chunk> chunkList;

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
		if (minecraft == null) minecraft = Minecraft.getMinecraft();
		if (chunkList == null) chunkList = new LinkedList<Chunk>();
		if (player == null) {
			player = minecraft.thePlayer;
			lastDimensionID = player.getEntityWorld().provider.dimensionId;
		}

		currentDimensionID = player.getEntityWorld().provider.dimensionId;
		if (currentDimensionID != lastDimensionID) {
			forceReload = true;
			player = null;
			return;
		}

		if (visible) {

			if (minecraft.inGameHasFocus && !minecraft.isGamePaused() && (forceReload || timer++ % 100 == 0 /*&& timer > 0*/)) {
				if (!chunkList.isEmpty()) chunkList.clear();

				chunkLoadables = managerInstance.getIChunkLoadablesInPlayerRange(player);
				if (chunkLoadables.length > 0) ProjectZed.logHelper.info(chunkLoadables[0].worldVec());

				for (IChunkLoadable loadable : chunkLoadables) {
					if (loadable.getChunksLoaded() == null || loadable.getChunksLoaded().length == 0) continue;

					for (Chunk chunk : loadable.getChunksLoaded()) {
						chunkList.add(chunk);
					}
				}

				if (timer > 100) timer = 0;
				forceReload = false;
			}

			// If no chunks to 'render', then return!
			if (chunkList.isEmpty()) {
				// ProjectZed.logHelper.warn("Chunk list is empty! Nothing to render here!");
				return;
			}

			final Vector3<Float> playerVec = new Vector3<Double>(player.posX, player.posY, player.posZ).getVector3f();

			playerVec.x = (float) (player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks);
			playerVec.y = (float) (player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks);
			playerVec.z = (float) (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks);

			for (Chunk chunk : chunkList) {
				// final Rect<Integer> chunkRect = ChunkHelper.getChunkBounds(ChunkHelper.getChunkFromPlayerLocation(player));
				final Rect<Integer> chunkRect = ChunkHelper.getChunkBounds(chunk);

				Vector3<Float> minVec = chunkRect.min.toVector3().getVector3f();
				minVec.z = minVec.y;
				minVec.y = playerVec.y + 20.0f;

				Vector3<Float> maxVec = chunkRect.max.toVector3().getVector3f();
				maxVec.z = maxVec.y;
				maxVec.y = minVec.y + 1.0f;

				minVec.x *= 0x10;
				minVec.z *= 0x10;
				maxVec.x *= 0x10;
				maxVec.z *= 0x10;

				ShapeRendererUtils.renderShape(EnumShape.SQUARE, new Vector3[] { minVec, maxVec }, playerVec, CHUNK_COLOR);
			}
		}
	}

}

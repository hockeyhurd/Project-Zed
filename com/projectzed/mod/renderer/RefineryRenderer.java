/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.renderer;

import com.hockeyhurd.api.math.Vector3;
import com.hockeyhurd.api.util.TessellatorHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Rendering class for refinery.
 *
 * @author hockeyhurd
 * @version 8/4/2015.
 */
@SideOnly(Side.CLIENT)
public class RefineryRenderer extends TileEntitySpecialRenderer {

	private ResourceLocation texture;
	private final float PIXEL = 1f / 64f;
	private final float TEX_SPACING = 0.00001f;
	private TessellatorHelper tessHelp;

	public RefineryRenderer() {
		this.texture = new ResourceLocation("projectzed", "textures/blocks/refinery.png");
		tessHelp = new TessellatorHelper(null);
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
		GL11.glTranslated(x, y, z);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0 % 65536, 0xf0 / 65536);
		this.bindTexture(texture);

		tessHelp.tess.startDrawingQuads();

		renderPlatform();
		renderFurnace();
		renderSideTanks();
		renderSmokeStack();

		// renderTopTank();

		tessHelp.tess.draw();

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glTranslated(-x, -y, -z);
	}

	private void renderPlatform() {

		Vector3<Float> min = new Vector3<Float>(0f, 0f, 0f);
		Vector3<Float> max = new Vector3<Float>(1f, 1f / 16f, 1f);

		{
			tessHelp.tess.setNormal(-1f, 0f, 0f);

			// x-
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 16f * PIXEL, 1f);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 0f, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 0f, 1f);

			tessHelp.tess.setNormal(1f, 0f, 0f);

			// x+
			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 0f, 1f);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 0f, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 16f * PIXEL, 1f);

			tessHelp.tess.setNormal(0f, 0f, -1f);

			// z-
			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 16f * PIXEL, 1f);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 0f, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 0f, 1f);

			tessHelp.tess.setNormal(0f, 0f, 1f);

			// z+
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 0f, 1f);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 0f, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 16f * PIXEL, 1f);

			tessHelp.tess.setNormal(0f, -1f, 0f);

			// y-
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 0f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 0f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 16f * PIXEL, 48f * PIXEL);

			tessHelp.tess.setNormal(0f, 1f, 0f);

			// y+
			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 16f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 0f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 0f * PIXEL, 48f * PIXEL);

		}
	}

	// TODO-ADD: Add rendering of fluids!!!
	private void renderSideTanks() {
		Vector3<Float> min = new Vector3<Float>(6f / 16f, 1f / 16f + TEX_SPACING, 0f);
		Vector3<Float> max = new Vector3<Float>(1f - 6f / 16f, 1f - (5f / 16f + TEX_SPACING), 1f - 12f / 16f);

		Vector3<Float> min2 = new Vector3<Float>(max.x, min.y, 1f);
		Vector3<Float> max2 = new Vector3<Float>(min.x, max.y, 1f - 4f / 16f);

		{

			// x-
			tessHelp.tess.setNormal(-1f, 0f, 0f);

			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 0f, 16f * PIXEL);

			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 0f, 16f * PIXEL);

			// x+
			tessHelp.tess.setNormal(1f, 0f, 0f);

			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 0f, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 16f * PIXEL, 16f * PIXEL);

			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 0f, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 16f * PIXEL, 16f * PIXEL);

			// z-
			tessHelp.tess.setNormal(0f, 0f, -1f);

			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 0f, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 16f * PIXEL, 16f * PIXEL);

			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 0f, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 16f * PIXEL, 16f * PIXEL);

			// z+
			tessHelp.tess.setNormal(0f, 0f, 1f);

			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 0f, 16f * PIXEL);

			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 0f, 16f * PIXEL);

			// y+
			tessHelp.tess.setNormal(0f, 1f, 1f);

			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 0f, 16f * PIXEL);

			tessHelp.tess.setNormal(0f, -1f, 1f);

			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 0f, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 16f * PIXEL, 16f * PIXEL);

		}

		{

			// x-
			tessHelp.tess.setNormal(-1f, 0f, 0f);

			tessHelp.tess.addVertexWithUV(min2.x, max2.y, min2.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min2.y, min2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min2.y, max2.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, max2.y, max2.z, 0f, 16f * PIXEL);

			tessHelp.tess.addVertexWithUV(max2.x, max2.y, min2.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min2.y, min2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min2.y, max2.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, max2.y, max2.z, 0f, 16f * PIXEL);

			// x+
			tessHelp.tess.setNormal(1f, 0f, 0f);

			tessHelp.tess.addVertexWithUV(max2.x, max2.y, max2.z, 0f, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min2.y, max2.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min2.y, min2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, max2.y, min2.z, 16f * PIXEL, 16f * PIXEL);

			tessHelp.tess.addVertexWithUV(min2.x, max2.y, max2.z, 0f, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min2.y, max2.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min2.y, min2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, max2.y, min2.z, 16f * PIXEL, 16f * PIXEL);

			// z-
			tessHelp.tess.setNormal(0f, 0f, -1f);

			tessHelp.tess.addVertexWithUV(max2.x, max2.y, min2.z, 0f, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min2.y, min2.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min2.y, min2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, max2.y, min2.z, 16f * PIXEL, 16f * PIXEL);

			tessHelp.tess.addVertexWithUV(max2.x, max2.y, max2.z, 0f, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min2.y, max2.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min2.y, max2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, max2.y, max2.z, 16f * PIXEL, 16f * PIXEL);

			// z+
			tessHelp.tess.setNormal(0f, 0f, 1f);

			tessHelp.tess.addVertexWithUV(min2.x, max2.y, max2.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min2.y, max2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min2.y, max2.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, max2.y, max2.z, 0f, 16f * PIXEL);

			tessHelp.tess.addVertexWithUV(min2.x, max2.y, min2.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min2.y, min2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min2.y, min2.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, max2.y, min2.z, 0f, 16f * PIXEL);

			// y+
			tessHelp.tess.setNormal(0f, 1f, 1f);

			tessHelp.tess.addVertexWithUV(max2.x, max2.y, max2.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, max2.y, min2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, max2.y, min2.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, max2.y, max2.z, 0f, 16f * PIXEL);

			tessHelp.tess.setNormal(0f, -1f, 1f);

			tessHelp.tess.addVertexWithUV(min2.x, max2.y, max2.z, 0f, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, max2.y, min2.z, 0f, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, max2.y, min2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, max2.y, max2.z, 16f * PIXEL, 16f * PIXEL);

		}

		// renderFluids();
	}

	private void renderFurnace() {
		Vector3<Float> min = new Vector3<Float>(4f / 16f - TEX_SPACING * 2f, 1f / 16f + TEX_SPACING, 4f / 16f + TEX_SPACING * 2f);
		Vector3<Float> max = new Vector3<Float>(1f - 4f / 16f - TEX_SPACING * 2f, 1f - (5f / 16f + TEX_SPACING), 1f - 4f / 16f - TEX_SPACING * 2f);

		// Vector3<Float> min = new Vector3<Float>(0f, 0f, 0f);
		// Vector3<Float> max = new Vector3<Float>(1f, 1f, 1f);

		{

			// x-
			tessHelp.tess.setNormal(-1f, 0f, 0f);

			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 48f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 64f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 64f * PIXEL, 16f * PIXEL);

			// x+
			tessHelp.tess.setNormal(1f, 0f, 0f);

			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 32f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 16f * PIXEL, 16f * PIXEL);

			// z+
			tessHelp.tess.setNormal(0f, 0f, -1f);

			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 32f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 16f * PIXEL, 16f * PIXEL);

			// z+
			tessHelp.tess.setNormal(0f, 0f, 1f);

			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 32f * PIXEL, 16f * PIXEL);

			// y+
			tessHelp.tess.setNormal(0f, 1f, 0f);

			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 0f * PIXEL, 16f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 0f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 16f * PIXEL, 16f * PIXEL);

		}

	}

	// TODO-ADD: Add rendering of fluids!!!
	private void renderTopTank() {
		Vector3<Float> min = new Vector3<Float>(0f, 1f - 3f / 16f, 0f);
		Vector3<Float> max = new Vector3<Float>(1f, 1f, 1f);

		{
			tessHelp.tess.setNormal(-1f, 0f, 0f);

			// x-
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 32f * PIXEL, 1f);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 16f * PIXEL, 1f);

			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 32f * PIXEL, 1f);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 16f * PIXEL, 1f);

			tessHelp.tess.setNormal(1f, 0f, 0f);

			// x+
			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 16f * PIXEL, 1f);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 32f * PIXEL, 1f);

			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 16f * PIXEL, 1f);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 32f * PIXEL, 1f);

			tessHelp.tess.setNormal(0f, 0f, -1f);

			// z-
			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 32f * PIXEL, 1f);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 16f * PIXEL, 1f);

			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 32f * PIXEL, 1f);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 16f * PIXEL, 1f);

			tessHelp.tess.setNormal(0f, 0f, 1f);

			// z+
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 16f * PIXEL, 1f);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 32f * PIXEL, 1f);

			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 16f * PIXEL, 1f);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 32f * PIXEL, 1f);

			tessHelp.tess.setNormal(0f, -1f, 0f);

			// y-
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 16f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 32f * PIXEL, 48f * PIXEL);

			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 16f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 32f * PIXEL, 48f * PIXEL);

			tessHelp.tess.setNormal(0f, 1f, 0f);

			// y+
			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 16f * PIXEL, 48f * PIXEL);

			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 16f * PIXEL, 48f * PIXEL);

		}

	}

	private void renderSmokeStack() {
		Vector3<Float> min = new Vector3<Float>(6f / 16f, 1f - 5f / 16f, 6f / 16f);
		Vector3<Float> max = new Vector3<Float>(1f - 6f / 16f, 1f, 1f - 6f / 16f);

		Vector3<Float> min2 = new Vector3<Float>(7f / 16f, min.y, 7f / 16f);
		Vector3<Float> max2 = new Vector3<Float>(1f - 7f / 16f, max.y, 1f - 7f / 16f);

		{

			// x-
			tessHelp.tess.setNormal(-1f, 0f, 0f);

			tessHelp.tess.addVertexWithUV(min.x, max.y, min2.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min2.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max2.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, max2.z, 16f * PIXEL, 48f * PIXEL);

			tessHelp.tess.addVertexWithUV(max.x, max.y, min2.z, 48f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min2.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max2.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, max2.z, 32f * PIXEL, 48f * PIXEL);

			// x+
			tessHelp.tess.setNormal(1f, 0f, 0f);

			tessHelp.tess.addVertexWithUV(max.x, max.y, max2.z, 16f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max2.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min2.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min2.z, 32f * PIXEL, 48f * PIXEL);

			tessHelp.tess.addVertexWithUV(min.x, max.y, max2.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max2.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min2.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min2.z, 48f * PIXEL, 48f * PIXEL);

			// z-
			tessHelp.tess.setNormal(0f, 0f, -1f);

			tessHelp.tess.addVertexWithUV(max2.x, max.y, min.z, 16f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, max.y, min.z, 32f * PIXEL, 48f * PIXEL);

			tessHelp.tess.addVertexWithUV(max2.x, max.y, max.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min.y, max.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min.y, max.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, max.y, max.z, 48f * PIXEL, 48f * PIXEL);

			// z+
			tessHelp.tess.setNormal(0f, 0f, 1f);

			tessHelp.tess.addVertexWithUV(min2.x, max.y, max.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min.y, max.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min.y, max.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, max.y, max.z, 16f * PIXEL, 48f * PIXEL);

			tessHelp.tess.addVertexWithUV(min2.x, max.y, min.z, 48f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min.y, min.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, max.y, min.z, 32f * PIXEL, 48f * PIXEL);

			// y-
			/*tessHelp.tess.setNormal(0f, 1f, 0f);

			tessHelp.tess.addVertexWithUV(max.x, min.y, max.z, 48f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max.z, 32f * PIXEL, 48f * PIXEL);*/

			// y+
			tessHelp.tess.setNormal(0f, 1f, 0f);

			tessHelp.tess.addVertexWithUV(max.x, max.y, max.z, 64f * PIXEL, 64f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min.z, 64f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min.z, 48f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, max.z, 48f * PIXEL, 64f * PIXEL);

		}

		{
			// x-/z-
			tessHelp.tess.setNormal(-1f, 0f, -1f);

			tessHelp.tess.addVertexWithUV(min2.x, max.y, min.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min2.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, min2.z, 16f * PIXEL, 48f * PIXEL);

			tessHelp.tess.addVertexWithUV(max.x, max.y, max2.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max2.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min.y, max.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, max.y, max.z, 48f * PIXEL, 48f * PIXEL);

			// x+/z-
			tessHelp.tess.setNormal(-1f, 0f, 1f);

			tessHelp.tess.addVertexWithUV(max.x, max.y, min2.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min2.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, max.y, min.z, 16f * PIXEL, 48f * PIXEL);

			tessHelp.tess.addVertexWithUV(min2.x, max.y, max.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min.y, max.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max2.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, max.y, max2.z, 48f * PIXEL, 48f * PIXEL);

			// x-/z+
			tessHelp.tess.setNormal(-1f, 0f, 1f);

			tessHelp.tess.addVertexWithUV(min.x, max.y, max2.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, max2.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min.y, max.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, max.y, max.z, 16f * PIXEL, 48f * PIXEL);

			tessHelp.tess.addVertexWithUV(max2.x, max.y, min.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, min2.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, min2.z, 48f * PIXEL, 48f * PIXEL);

			// x+/z+
			tessHelp.tess.setNormal(1f, 0f, 1f);

			tessHelp.tess.addVertexWithUV(max2.x, max.y, max.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(max2.x, min.y, max.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, min.y, max2.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(max.x, max.y, max2.z, 16f * PIXEL, 48f * PIXEL);

			tessHelp.tess.addVertexWithUV(min.x, max.y, min2.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.tess.addVertexWithUV(min.x, min.y, min2.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, min.y, min.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.tess.addVertexWithUV(min2.x, max.y, min.z, 48f * PIXEL, 48f * PIXEL);
		}

	}

}

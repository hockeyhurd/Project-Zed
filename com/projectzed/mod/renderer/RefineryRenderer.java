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
import com.projectzed.mod.tileentity.container.TileEntityRefinery;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Rendering class for refinery.
 *
 * @author hockeyhurd
 * @version 8/4/2015.
 */
@SideOnly(Side.CLIENT)
public final class RefineryRenderer extends TileEntitySpecialRenderer {

	private ResourceLocation texture;
	public static final float PIXEL = 1f / 64f;
	public static final float TEX_SPACING = 0.001f;
	private TessellatorHelper tessHelp;

	// vectors and calculations:
	public static final Vector3<Float> minP = new Vector3<Float>(0f, 0f, 0f);
	public static final Vector3<Float> maxP = new Vector3<Float>(1f, 1f / 16f, 1f);

	public static final Vector3<Float> minT = new Vector3<Float>(6f / 16f, 1f / 16f + TEX_SPACING, 0f);
	public static final Vector3<Float> maxT = new Vector3<Float>(1f - 6f / 16f, 1f - (5f / 16f + TEX_SPACING), 1f - 12f / 16f);

	public static final Vector3<Float> minT2 = new Vector3<Float>(maxT.x, minT.y.floatValue(), 1f);
	public static final Vector3<Float> maxT2 = new Vector3<Float>(minT.x, maxT.y.floatValue(), 1f - 4f / 16f);

	public static final Vector3<Float> minF = new Vector3<Float>(4f / 16f - TEX_SPACING * 2f, 1f / 16f + TEX_SPACING, 4f / 16f + TEX_SPACING * 2f);
	public static final Vector3<Float> maxF = new Vector3<Float>(1f - 4f / 16f - TEX_SPACING * 2f, 1f - (5f / 16f + TEX_SPACING), 1f - 4f / 16f - TEX_SPACING * 2f);

	public static final Vector3<Float> minSt = new Vector3<Float>(6f / 16f, 1f - 5f / 16f, 6f / 16f);
	public static final Vector3<Float> maxSt = new Vector3<Float>(1f - 6f / 16f, 1f, 1f - 6f / 16f);

	public static final Vector3<Float> minSt2 = new Vector3<Float>(7f / 16f, minSt.y, 7f / 16f);
	public static final Vector3<Float> maxSt2 = new Vector3<Float>(1f - 7f / 16f, maxSt.y, 1f - 7f / 16f);

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

		final double angle = te.blockMetadata == 3 ? 90.0f : te.blockMetadata == 5 ? 180.0f : te.blockMetadata == 2 ? -90.0f : 0.0f;

		GL11.glTranslatef(0.5f, 0.5f, 0.5f);
		GL11.glRotated(angle, 0, 1, 0);
		GL11.glTranslatef(-0.5f, -0.5f, -0.5f);

		tessHelp.tess.startDrawingQuads();

		renderPlatform();
		renderFurnace();
		renderSideTanks((TileEntityRefinery) te);
		renderSmokeStack();

		tessHelp.draw();

		renderFluid((TileEntityRefinery) te);

		// renderTopTank();

		// tessHelp.tess.draw();

		GL11.glTranslatef(0.5f, 0.5f, 0.5f);
		GL11.glRotated(-angle, 0, 1, 0);
		GL11.glTranslatef(-0.5f, -0.5f, -0.5f);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glTranslated(-x, -y, -z);
	}

	private void renderPlatform() {

		tessHelp.setNormal(-1f, 0f, 0f);

		// x-
		tessHelp.addVertUV(minP.x, maxP.y, minP.z, 16f * PIXEL, 1f);
		tessHelp.addVertUV(minP.x, minP.y, minP.z, 16f * PIXEL, 61f * PIXEL);
		tessHelp.addVertUV(minP.x, minP.y, maxP.z, 0f, 61f * PIXEL);
		tessHelp.addVertUV(minP.x, maxP.y, maxP.z, 0f, 1f);

		tessHelp.setNormal(1f, 0f, 0f);

		// x+
		tessHelp.addVertUV(maxP.x, maxP.y, maxP.z, 0f, 1f);
		tessHelp.addVertUV(maxP.x, minP.y, maxP.z, 0f, 61f * PIXEL);
		tessHelp.addVertUV(maxP.x, minP.y, minP.z, 16f * PIXEL, 61f * PIXEL);
		tessHelp.addVertUV(maxP.x, maxP.y, minP.z, 16f * PIXEL, 1f);

		tessHelp.setNormal(0f, 0f, -1f);

		// z-
		tessHelp.addVertUV(minP.x, maxP.y, maxP.z, 16f * PIXEL, 1f);
		tessHelp.addVertUV(minP.x, minP.y, maxP.z, 16f * PIXEL, 61f * PIXEL);
		tessHelp.addVertUV(maxP.x, minP.y, maxP.z, 0f, 61f * PIXEL);
		tessHelp.addVertUV(maxP.x, maxP.y, maxP.z, 0f, 1f);

		tessHelp.setNormal(0f, 0f, 1f);

		// z+
		tessHelp.addVertUV(maxP.x, maxP.y, minP.z, 0f, 1f);
		tessHelp.addVertUV(maxP.x, minP.y, minP.z, 0f, 61f * PIXEL);
		tessHelp.addVertUV(minP.x, minP.y, minP.z, 16f * PIXEL, 61f * PIXEL);
		tessHelp.addVertUV(minP.x, maxP.y, minP.z, 16f * PIXEL, 1f);

		tessHelp.setNormal(0f, -1f, 0f);

		// y-
		tessHelp.addVertUV(minP.x, minP.y, maxP.z, 0f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minP.x, minP.y, minP.z, 0f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxP.x, minP.y, minP.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxP.x, minP.y, maxP.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.setNormal(0f, 1f, 0f);

		// y+
		tessHelp.addVertUV(maxP.x, maxP.y, maxP.z, 16f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxP.x, maxP.y, minP.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minP.x, maxP.y, minP.z, 0f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minP.x, maxP.y, maxP.z, 0f * PIXEL, 48f * PIXEL);

	}

	// TODO-ADD: Add rendering of fluids!!!
	private void renderSideTanks(TileEntityRefinery te) {

		// Tank 1:

		{
			// x-
			tessHelp.setNormal(-1f, 0f, 0f);

			tessHelp.addVertUV(minT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(minT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);

			tessHelp.addVertUV(maxT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(maxT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);

			// x+
			tessHelp.setNormal(1f, 0f, 0f);

			tessHelp.addVertUV(maxT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(maxT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);

			tessHelp.addVertUV(minT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(minT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);

			// z-
			tessHelp.setNormal(0f, 0f, -1f);

			tessHelp.addVertUV(maxT.x, maxT.y, minT.z, 0f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(maxT.x, minT.y, minT.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);

			tessHelp.addVertUV(maxT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(maxT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT.x, minT.y, maxT.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT.x, maxT.y, maxT.z, 16f * PIXEL, 16f * PIXEL);

			// z+
			tessHelp.setNormal(0f, 0f, 1f);

			tessHelp.addVertUV(minT.x, maxT.y, maxT.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(minT.x, minT.y, maxT.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);

			tessHelp.addVertUV(minT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(minT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT.x, minT.y, minT.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT.x, maxT.y, minT.z, 0f * PIXEL, 16f * PIXEL);

			// y+
			tessHelp.setNormal(0f, 1f, 0f);

			tessHelp.addVertUV(maxT.x, maxT.y, maxT.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(maxT.x, maxT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT.x, maxT.y, minT.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);

			tessHelp.setNormal(0f, -1f, 0f);

			tessHelp.addVertUV(minT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(minT.x, maxT.y, minT.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT.x, maxT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT.x, maxT.y, maxT.z, 16f * PIXEL, 16f * PIXEL);

		}

		// Tank 2:

		{
			// x-
			tessHelp.setNormal(-1f, 0f, 0f);

			tessHelp.addVertUV(minT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(minT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);

			tessHelp.addVertUV(maxT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(maxT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);

			// x+
			tessHelp.setNormal(1f, 0f, 0f);

			tessHelp.addVertUV(maxT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(maxT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);

			tessHelp.addVertUV(minT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(minT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);

			// z-
			tessHelp.setNormal(0f, 0f, -1f);

			tessHelp.addVertUV(maxT2.x, maxT2.y, minT2.z, 0f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(maxT2.x, minT2.y, minT2.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);

			tessHelp.addVertUV(maxT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(maxT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT2.x, minT2.y, maxT2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT2.x, maxT2.y, maxT2.z, 16f * PIXEL, 16f * PIXEL);

			// z+
			tessHelp.setNormal(0f, 0f, 1f);

			tessHelp.addVertUV(minT2.x, maxT2.y, maxT2.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(minT2.x, minT2.y, maxT2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);

			tessHelp.addVertUV(minT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(minT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT2.x, minT2.y, minT2.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT2.x, maxT2.y, minT2.z, 0f * PIXEL, 16f * PIXEL);

			// y+
			tessHelp.setNormal(0f, 1f, 0f);

			tessHelp.addVertUV(maxT2.x, maxT2.y, maxT2.z, 16f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(maxT2.x, maxT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT2.x, maxT2.y, minT2.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(minT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);

			tessHelp.setNormal(0f, -1f, 0f);

			tessHelp.addVertUV(minT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(minT2.x, maxT2.y, minT2.z, 0f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT2.x, maxT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			tessHelp.addVertUV(maxT2.x, maxT2.y, maxT2.z, 16f * PIXEL, 16f * PIXEL);

		}

		/*for (int i = 0; i < te.getNumTanks(); i++) {
			ProjectZed.logHelper.info(te.getTank(i).getFluid().getLocalizedName());
		}*/
	}

	private void renderFurnace() {
		// x-
		tessHelp.setNormal(-1f, 0f, 0f);

		tessHelp.addVertUV(minF.x, maxF.y, minF.z, 48f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(minF.x, minF.y, minF.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minF.x, minF.y, maxF.z, 64f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minF.x, maxF.y, maxF.z, 64f * PIXEL, 16f * PIXEL);

		// x+
		tessHelp.setNormal(1f, 0f, 0f);

		tessHelp.addVertUV(maxF.x, maxF.y, maxF.z, 32f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxF.x, minF.y, maxF.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxF.x, minF.y, minF.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxF.x, maxF.y, minF.z, 16f * PIXEL, 16f * PIXEL);

		// z+
		tessHelp.setNormal(0f, 0f, -1f);

		tessHelp.addVertUV(maxF.x, maxF.y, minF.z, 32f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxF.x, minF.y, minF.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minF.x, minF.y, minF.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minF.x, maxF.y, minF.z, 16f * PIXEL, 16f * PIXEL);

		// z+
		tessHelp.setNormal(0f, 0f, 1f);

		tessHelp.addVertUV(minF.x, maxF.y, maxF.z, 16f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(minF.x, minF.y, maxF.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxF.x, minF.y, maxF.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxF.x, maxF.y, maxF.z, 32f * PIXEL, 16f * PIXEL);

		// y+
		tessHelp.setNormal(0f, 1f, 0f);

		tessHelp.addVertUV(maxF.x, maxF.y, maxF.z, 0f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxF.x, maxF.y, minF.z, 0f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minF.x, maxF.y, minF.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minF.x, maxF.y, maxF.z, 16f * PIXEL, 16f * PIXEL);

	}

	// TODO-ADD: Add rendering of fluids!!!
	private void renderTopTank() {
		Vector3<Float> min = new Vector3<Float>(0f, 1f - 3f / 16f, 0f);
		Vector3<Float> max = new Vector3<Float>(1f, 1f, 1f);

		{
			tessHelp.setNormal(-1f, 0f, 0f);

			// x-
			tessHelp.addVertUV(min.x, max.y, min.z, 32f * PIXEL, 1f);
			tessHelp.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, max.z, 16f * PIXEL, 1f);

			tessHelp.addVertUV(max.x, max.y, min.z, 32f * PIXEL, 1f);
			tessHelp.addVertUV(max.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, max.z, 16f * PIXEL, 1f);

			tessHelp.setNormal(1f, 0f, 0f);

			// x+
			tessHelp.addVertUV(max.x, max.y, max.z, 16f * PIXEL, 1f);
			tessHelp.addVertUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, min.z, 32f * PIXEL, 1f);

			tessHelp.addVertUV(min.x, max.y, max.z, 16f * PIXEL, 1f);
			tessHelp.addVertUV(min.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, min.z, 32f * PIXEL, 1f);

			tessHelp.setNormal(0f, 0f, -1f);

			// z-
			tessHelp.addVertUV(min.x, max.y, max.z, 32f * PIXEL, 1f);
			tessHelp.addVertUV(min.x, min.y, max.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, max.z, 16f * PIXEL, 1f);

			tessHelp.addVertUV(min.x, max.y, min.z, 32f * PIXEL, 1f);
			tessHelp.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, min.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, min.z, 16f * PIXEL, 1f);

			tessHelp.setNormal(0f, 0f, 1f);

			// z+
			tessHelp.addVertUV(max.x, max.y, min.z, 16f * PIXEL, 1f);
			tessHelp.addVertUV(max.x, min.y, min.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, min.z, 32f * PIXEL, 1f);

			tessHelp.addVertUV(max.x, max.y, max.z, 16f * PIXEL, 1f);
			tessHelp.addVertUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, max.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, max.z, 32f * PIXEL, 1f);

			tessHelp.setNormal(0f, -1f, 0f);

			// y-
			tessHelp.addVertUV(min.x, min.y, max.z, 16f * PIXEL, 48f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, max.z, 32f * PIXEL, 48f * PIXEL);

			tessHelp.addVertUV(min.x, max.y, max.z, 16f * PIXEL, 48f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, max.z, 32f * PIXEL, 48f * PIXEL);

			tessHelp.setNormal(0f, 1f, 0f);

			// y+
			tessHelp.addVertUV(max.x, max.y, max.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, max.z, 16f * PIXEL, 48f * PIXEL);

			tessHelp.addVertUV(max.x, min.y, max.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, max.z, 16f * PIXEL, 48f * PIXEL);

		}

	}

	private void renderSmokeStack() {

		// x-
		tessHelp.setNormal(-1f, 0f, 0f);

		tessHelp.addVertUV(minSt.x, maxSt.y, minSt2.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, minSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, maxSt2.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, maxSt.y, maxSt2.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(maxSt.x, maxSt.y, minSt2.z, 48f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, minSt2.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, maxSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, maxSt.y, maxSt2.z, 32f * PIXEL, 48f * PIXEL);

		// x+
		tessHelp.setNormal(1f, 0f, 0f);

		tessHelp.addVertUV(maxSt.x, maxSt.y, maxSt2.z, 16f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, maxSt2.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, minSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, maxSt.y, minSt2.z, 32f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(minSt.x, maxSt.y, maxSt2.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, maxSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, minSt2.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, maxSt.y, minSt2.z, 48f * PIXEL, 48f * PIXEL);

		// z-
		tessHelp.setNormal(0f, 0f, -1f);

		tessHelp.addVertUV(maxSt2.x, maxSt.y, minSt.z, 16f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, minSt.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, minSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, maxSt.y, minSt.z, 32f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(maxSt2.x, maxSt.y, maxSt.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, maxSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, maxSt.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, maxSt.y, maxSt.z, 48f * PIXEL, 48f * PIXEL);

		// z+
		tessHelp.setNormal(0f, 0f, 1f);

		tessHelp.addVertUV(minSt2.x, maxSt.y, maxSt.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, maxSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, maxSt.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, maxSt.y, maxSt.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(minSt2.x, maxSt.y, minSt.z, 48f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, minSt.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, minSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, maxSt.y, minSt.z, 32f * PIXEL, 48f * PIXEL);

		// y-
			/*tessHelp.setNormal(0f, 1f, 0f);

			tessHelp.addVertUV(max.x, min.y, max.z, 48f * PIXEL, 48f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, min.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, max.z, 32f * PIXEL, 48f * PIXEL);*/

		// y+
		tessHelp.setNormal(0f, 1f, 0f);

		tessHelp.addVertUV(maxSt.x, maxSt.y, maxSt.z, 64f * PIXEL, 64f * PIXEL);
		tessHelp.addVertUV(maxSt.x, maxSt.y, minSt.z, 64f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt.x, maxSt.y, minSt.z, 48f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt.x, maxSt.y, maxSt.z, 48f * PIXEL, 64f * PIXEL);

		// x-/z-
		tessHelp.setNormal(-1f, 0f, -1f);

		tessHelp.addVertUV(minSt2.x, maxSt.y, minSt.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, minSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, minSt2.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, maxSt.y, minSt2.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(maxSt.x, maxSt.y, maxSt2.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, maxSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, maxSt.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, maxSt.y, maxSt.z, 48f * PIXEL, 48f * PIXEL);

		// x+/z-
		tessHelp.setNormal(-1f, 0f, 1f);

		tessHelp.addVertUV(maxSt.x, maxSt.y, minSt2.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, minSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, minSt.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, maxSt.y, minSt.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(minSt2.x, maxSt.y, maxSt.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, maxSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, maxSt2.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, maxSt.y, maxSt2.z, 48f * PIXEL, 48f * PIXEL);

		// x-/z+
		tessHelp.setNormal(-1f, 0f, 1f);

		tessHelp.addVertUV(minSt.x, maxSt.y, maxSt2.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, maxSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, maxSt.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, maxSt.y, maxSt.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(maxSt2.x, maxSt.y, minSt.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, minSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, minSt2.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, maxSt.y, minSt2.z, 48f * PIXEL, 48f * PIXEL);

		// x+/z+
		tessHelp.setNormal(1f, 0f, 1f);

		tessHelp.addVertUV(maxSt2.x, maxSt.y, maxSt.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, maxSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, maxSt2.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, maxSt.y, maxSt2.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(minSt.x, maxSt.y, minSt2.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, minSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, minSt.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, maxSt.y, minSt.z, 48f * PIXEL, 48f * PIXEL);

	}

	private void renderFluid(TileEntityRefinery te) {
		this.bindTexture(TextureMap.locationBlocksTexture);

		if (te.getTank(0).getFluid() != null && te.getTank(0).getFluidAmount() > 0) {
			IIcon inputIcon = te.getTank(0).getFluid().getFluid().getStillIcon();
			// float progress = ((float) te.getTank(0).getFluidAmount() / te.getTank(0).getCapacity() * 10f) / 16f;
			float progress = (3f + ((int) (te.getTank(0).getFluidAmount() / (float) (te.getTank(0).getCapacity()) * 10))) / 16f;
			// ProjectZed.logHelper.info("progress", te.getTank(0).getFluidAmount(), progress, progress * (maxT.y - TEX_SPACING));

			// ProjectZed.logHelper.info(inputIcon.getIconName());
			if (progress > 3f / 16f) {

				float minU = inputIcon.getMinU();
				float maxU = inputIcon.getMaxU();
				float minV = inputIcon.getMinV();
				float maxV = inputIcon.getMaxV();

				tessHelp.startDrawingQuads();

				// x-
				tessHelp.setNormal(-1f, 0f, 0f);

				tessHelp.addVertUV(minT.x + TEX_SPACING, maxT.y * progress, minT.z - TEX_SPACING, maxU, maxV);
				tessHelp.addVertUV(minT.x + TEX_SPACING, minT.y, minT.z - TEX_SPACING, maxU, minV);
				tessHelp.addVertUV(minT.x + TEX_SPACING, minT.y, maxT.z - TEX_SPACING, minU, minV);
				tessHelp.addVertUV(minT.x + TEX_SPACING, maxT.y * progress, maxT.z - TEX_SPACING, minU, maxV);

				// x+
				tessHelp.setNormal(1f, 0f, 0f);

				tessHelp.addVertUV(maxT.x - TEX_SPACING, maxT.y * progress, maxT.z - TEX_SPACING, minU, maxV);
				tessHelp.addVertUV(maxT.x - TEX_SPACING, minT.y, maxT.z - TEX_SPACING, minU, minV);
				tessHelp.addVertUV(maxT.x - TEX_SPACING, minT.y, minT.z - TEX_SPACING, maxU, minV);
				tessHelp.addVertUV(maxT.x - TEX_SPACING, maxT.y * progress, minT.z - TEX_SPACING, maxU, maxV);

				// z-
				tessHelp.setNormal(0f, 0f, -1f);

				tessHelp.addVertUV(maxT.x + TEX_SPACING, maxT.y * progress, minT.z + TEX_SPACING, minU, maxV);
				tessHelp.addVertUV(maxT.x + TEX_SPACING, minT.y, minT.z + TEX_SPACING, minU, minV);
				tessHelp.addVertUV(minT.x + TEX_SPACING, minT.y, minT.z + TEX_SPACING, maxU, minV);
				tessHelp.addVertUV(minT.x + TEX_SPACING, maxT.y * progress, minT.z + TEX_SPACING, maxU, maxV);

				// y+
				tessHelp.setNormal(0f, 1f, 0f);

				tessHelp.addVertUV(maxT.x - TEX_SPACING, maxT.y * progress, maxT.z - TEX_SPACING, maxU, maxV);
				tessHelp.addVertUV(maxT.x - TEX_SPACING, maxT.y * progress, minT.z - TEX_SPACING, maxU, minV);
				tessHelp.addVertUV(minT.x - TEX_SPACING, maxT.y * progress, minT.z - TEX_SPACING, minU, minV);
				tessHelp.addVertUV(minT.x - TEX_SPACING, maxT.y * progress, maxT.z - TEX_SPACING, minU, maxV);

				// this.bindTexture(texture);

				tessHelp.draw();
			}
		}

		if (te.getTank(1).getFluid() != null && te.getTank(1).getFluidAmount() > 0) {
			IIcon inputIcon = te.getTank(1).getFluid().getFluid().getStillIcon();
			// float progress = ((float) te.getTank(1).getFluidAmount() / te.getTank(1).getCapacity() * 10f) / 16f;
			float progress = (3f + ((int) (te.getTank(1).getFluidAmount() / (float) (te.getTank(1).getCapacity()) * 10))) / 16f;
			// ProjectZed.logHelper.info("progress", te.getTank(1).getFluidAmount(), progress, progress * (maxT.y - TEX_SPACING));

			if (progress > 3f / 16f) {

				float minU = inputIcon.getMinU();
				float maxU = inputIcon.getMaxU();
				float minV = inputIcon.getMinV();
				float maxV = inputIcon.getMaxV();

				tessHelp.startDrawingQuads();

				// x-
				tessHelp.setNormal(-1f, 0f, 0f);

				// ProjectZed.logHelper.info(minT.y, minT2.y, maxT2.y * progress);

				tessHelp.addVertUV(minT2.x - TEX_SPACING, maxT2.y * progress, minT2.z - TEX_SPACING, maxU, maxV);
				tessHelp.addVertUV(minT2.x - TEX_SPACING, minT2.y, minT2.z - TEX_SPACING, maxU, minV);
				tessHelp.addVertUV(minT2.x - TEX_SPACING, minT2.y, maxT2.z - TEX_SPACING, minU, minV);
				tessHelp.addVertUV(minT2.x - TEX_SPACING, maxT2.y * progress, maxT2.z - TEX_SPACING, minU, maxV);

				// x+
				tessHelp.setNormal(1f, 0f, 0f);

				tessHelp.addVertUV(maxT2.x + TEX_SPACING, maxT2.y * progress, maxT2.z + TEX_SPACING, minU, maxV);
				tessHelp.addVertUV(maxT2.x + TEX_SPACING, minT2.y, maxT2.z + TEX_SPACING, minU, minV);
				tessHelp.addVertUV(maxT2.x + TEX_SPACING, minT2.y, minT2.z + TEX_SPACING, maxU, minV);
				tessHelp.addVertUV(maxT2.x + TEX_SPACING, maxT2.y * progress, minT2.z + TEX_SPACING, maxU, maxV);

				// z-
				tessHelp.setNormal(0f, 0f, -1f);

				tessHelp.addVertUV(maxT2.x - TEX_SPACING, maxT2.y * progress, minT2.z - TEX_SPACING, minU, maxV);
				tessHelp.addVertUV(maxT2.x - TEX_SPACING, minT2.y, minT2.z - TEX_SPACING, minU, minV);
				tessHelp.addVertUV(minT2.x - TEX_SPACING, minT2.y, minT2.z - TEX_SPACING, maxU, minV);
				tessHelp.addVertUV(minT2.x - TEX_SPACING, maxT2.y * progress, minT2.z - TEX_SPACING, maxU, maxV);

				// y+
				tessHelp.setNormal(0f, 1f, 0f);

				tessHelp.addVertUV(maxT2.x - TEX_SPACING, maxT2.y * progress, maxT2.z - TEX_SPACING, maxU, maxV);
				tessHelp.addVertUV(maxT2.x - TEX_SPACING, maxT2.y * progress, minT2.z - TEX_SPACING, maxU, minV);
				tessHelp.addVertUV(minT2.x - TEX_SPACING, maxT2.y * progress, minT2.z - TEX_SPACING, minU, minV);
				tessHelp.addVertUV(minT2.x - TEX_SPACING, maxT2.y * progress, maxT2.z - TEX_SPACING, minU, maxV);

				// this.bindTexture(texture);

				tessHelp.draw();
			}
		}

	}

}

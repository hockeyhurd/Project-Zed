package com.projectzed.mod.renderer;

import com.hockeyhurd.hcorelib.api.client.util.RenderHelper;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityRefinery;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Refinery TESR renderer.
 *
 * @author hockeyhurd
 * @version 6/22/2016.
 */
@SideOnly(Side.CLIENT)
public final class RefineryRenderer extends TileEntitySpecialRenderer<TileEntityRefinery> {

	private static final TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
	private static final ResourceLocation texture = new ResourceLocation("projectzed", "textures/blocks/refinery.png");
	public static final float PIXEL = 1f / 64f;
	public static final float TEX_SPACING = 0.001f;

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
	}

	@Override
	public void renderTileEntityAt(TileEntityRefinery te, double x, double y, double z, float partialTicks, int destroyStage) {
		final Vector3<Double> renderVec = new Vector3<Double>(x, y, z);
		final Vector3<Integer> atVec = VectorHelper.toVector3i(te.getPos());
		// final int metadata = te.getBlockMetadata();
		final int metadata = te.getCurrentFacing().ordinal();
		ProjectZed.logHelper.info("metadata:", metadata);
		final float angle = metadata == 3 ? 90.0f : metadata == 5 ? 180.0f : metadata == 2 ? -90.0f : 0.0f;
		final float translation = 0.5f;

		RenderHelper.setupPreRender(texture, renderVec);

		RenderHelper.translatef(translation, translation, translation);
		RenderHelper.rotatef(angle, 0, 1, 0);
		RenderHelper.translatef(-translation, -translation, -translation);

		RenderHelper.startDrawingQuads();

		renderPlatform();
		renderFurnace(te.isPowered());
		renderSideTanks(te);
		renderSmokeStack();

		RenderHelper.draw();

		RenderHelper.translatef(translation, translation, translation);
		RenderHelper.rotatef(-angle, 0, 1, 0);
		RenderHelper.translatef(-translation, -translation, -translation);

		RenderHelper.finishPostRender(renderVec);
	}

	private static void renderPlatform() {

		// TessellatorHelper.setNormal(-1f, 0f, 0f);

		// x-
		RenderHelper.addVertUV(minP.x, maxP.y, minP.z, 16f * PIXEL, 1f);
		RenderHelper.addVertUV(minP.x, minP.y, minP.z, 16f * PIXEL, 61f * PIXEL);
		RenderHelper.addVertUV(minP.x, minP.y, maxP.z, 0f, 61f * PIXEL);
		RenderHelper.addVertUV(minP.x, maxP.y, maxP.z, 0f, 1f);

		// TessellatorHelper.setNormal(1f, 0f, 0f);

		// x+
		RenderHelper.addVertUV(maxP.x, maxP.y, maxP.z, 0f, 1f);
		RenderHelper.addVertUV(maxP.x, minP.y, maxP.z, 0f, 61f * PIXEL);
		RenderHelper.addVertUV(maxP.x, minP.y, minP.z, 16f * PIXEL, 61f * PIXEL);
		RenderHelper.addVertUV(maxP.x, maxP.y, minP.z, 16f * PIXEL, 1f);

		// TessellatorHelper.setNormal(0f, 0f, -1f);

		// z-
		RenderHelper.addVertUV(minP.x, maxP.y, maxP.z, 16f * PIXEL, 1f);
		RenderHelper.addVertUV(minP.x, minP.y, maxP.z, 16f * PIXEL, 61f * PIXEL);
		RenderHelper.addVertUV(maxP.x, minP.y, maxP.z, 0f, 61f * PIXEL);
		RenderHelper.addVertUV(maxP.x, maxP.y, maxP.z, 0f, 1f);

		// TessellatorHelper.setNormal(0f, 0f, 1f);

		// z+
		RenderHelper.addVertUV(maxP.x, maxP.y, minP.z, 0f, 1f);
		RenderHelper.addVertUV(maxP.x, minP.y, minP.z, 0f, 61f * PIXEL);
		RenderHelper.addVertUV(minP.x, minP.y, minP.z, 16f * PIXEL, 61f * PIXEL);
		RenderHelper.addVertUV(minP.x, maxP.y, minP.z, 16f * PIXEL, 1f);

		// TessellatorHelper.setNormal(0f, -1f, 0f);

		// y-
		RenderHelper.addVertUV(minP.x, minP.y, maxP.z, 0f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(minP.x, minP.y, minP.z, 0f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxP.x, minP.y, minP.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxP.x, minP.y, maxP.z, 16f * PIXEL, 48f * PIXEL);

		// TessellatorHelper.setNormal(0f, 1f, 0f);

		// y+
		RenderHelper.addVertUV(maxP.x, maxP.y, maxP.z, 16f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(maxP.x, maxP.y, minP.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minP.x, maxP.y, minP.z, 0f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minP.x, maxP.y, maxP.z, 0f * PIXEL, 48f * PIXEL);
	}

	// TODO-ADD: Add rendering of fluids!!!
	private static void renderSideTanks(TileEntityRefinery te) {

		// Tank 1:

		{
			// x-
			// RenderHelper.setNormal(-1f, 0f, 0f);

			RenderHelper.addVertUV(minT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(minT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);

			RenderHelper.addVertUV(maxT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(maxT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);

			// x+
			// RenderHelper.setNormal(1f, 0f, 0f);

			RenderHelper.addVertUV(maxT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(maxT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);

			RenderHelper.addVertUV(minT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(minT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);

			// z-
			// RenderHelper.setNormal(0f, 0f, -1f);

			RenderHelper.addVertUV(maxT.x, maxT.y, minT.z, 0f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(maxT.x, minT.y, minT.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);

			RenderHelper.addVertUV(maxT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(maxT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT.x, minT.y, maxT.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT.x, maxT.y, maxT.z, 16f * PIXEL, 16f * PIXEL);

			// z+
			// RenderHelper.setNormal(0f, 0f, 1f);

			RenderHelper.addVertUV(minT.x, maxT.y, maxT.z, 16f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(minT.x, minT.y, maxT.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);

			RenderHelper.addVertUV(minT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(minT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT.x, minT.y, minT.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT.x, maxT.y, minT.z, 0f * PIXEL, 16f * PIXEL);

			// y+
			// RenderHelper.setNormal(0f, 1f, 0f);

			RenderHelper.addVertUV(maxT.x, maxT.y, maxT.z, 16f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(maxT.x, maxT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT.x, maxT.y, minT.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);

			// RenderHelper.setNormal(0f, -1f, 0f);

			RenderHelper.addVertUV(minT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(minT.x, maxT.y, minT.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT.x, maxT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT.x, maxT.y, maxT.z, 16f * PIXEL, 16f * PIXEL);

		}

		// Tank 2:

		{
			// x-
			// RenderHelper.setNormal(-1f, 0f, 0f);

			RenderHelper.addVertUV(minT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(minT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);

			RenderHelper.addVertUV(maxT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);

			// x+
			// RenderHelper.setNormal(1f, 0f, 0f);

			RenderHelper.addVertUV(maxT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);

			RenderHelper.addVertUV(minT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(minT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);

			// z-
			// RenderHelper.setNormal(0f, 0f, -1f);

			RenderHelper.addVertUV(maxT2.x, maxT2.y, minT2.z, 0f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, minT2.y, minT2.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);

			RenderHelper.addVertUV(maxT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT2.x, minT2.y, maxT2.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT2.x, maxT2.y, maxT2.z, 16f * PIXEL, 16f * PIXEL);

			// z+
			// RenderHelper.setNormal(0f, 0f, 1f);

			RenderHelper.addVertUV(minT2.x, maxT2.y, maxT2.z, 16f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(minT2.x, minT2.y, maxT2.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);

			RenderHelper.addVertUV(minT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(minT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, minT2.y, minT2.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, maxT2.y, minT2.z, 0f * PIXEL, 16f * PIXEL);

			// y+
			// RenderHelper.setNormal(0f, 1f, 0f);

			RenderHelper.addVertUV(maxT2.x, maxT2.y, maxT2.z, 16f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, maxT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT2.x, maxT2.y, minT2.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(minT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);

			// RenderHelper.setNormal(0f, -1f, 0f);

			RenderHelper.addVertUV(minT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(minT2.x, maxT2.y, minT2.z, 0f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, maxT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
			RenderHelper.addVertUV(maxT2.x, maxT2.y, maxT2.z, 16f * PIXEL, 16f * PIXEL);

		}

		/*for (int i = 0; i < te.getNumTanks(); i++) {
			ProjectZed.logHelper.info(te.getTank(i).getFluid().getLocalizedName());
		}*/
	}

	private static void renderFurnace(final boolean powered) {
		// x-
		// RenderHelper.setNormal(-1f, 0f, 0f);

		if (powered) {
			RenderHelper.addVertUV(minF.x, maxF.y, minF.z, 48f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(minF.x, minF.y, minF.z, 48f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(minF.x, minF.y, maxF.z, 64f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(minF.x, maxF.y, maxF.z, 64f * PIXEL, 16f * PIXEL);
		}

		else {
			RenderHelper.addVertUV(minF.x, maxF.y, minF.z, 32f * PIXEL, 16f * PIXEL);
			RenderHelper.addVertUV(minF.x, minF.y, minF.z, 32f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(minF.x, minF.y, maxF.z, 48f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(minF.x, maxF.y, maxF.z, 48f * PIXEL, 16f * PIXEL);
		}

		// x+
		// RenderHelper.setNormal(1f, 0f, 0f);

		RenderHelper.addVertUV(maxF.x, maxF.y, maxF.z, 32f * PIXEL, 16f * PIXEL);
		RenderHelper.addVertUV(maxF.x, minF.y, maxF.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxF.x, minF.y, minF.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxF.x, maxF.y, minF.z, 16f * PIXEL, 16f * PIXEL);

		// z+
		// RenderHelper.setNormal(0f, 0f, -1f);

		RenderHelper.addVertUV(maxF.x, maxF.y, minF.z, 32f * PIXEL, 16f * PIXEL);
		RenderHelper.addVertUV(maxF.x, minF.y, minF.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minF.x, minF.y, minF.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minF.x, maxF.y, minF.z, 16f * PIXEL, 16f * PIXEL);

		// z+
		// RenderHelper.setNormal(0f, 0f, 1f);

		RenderHelper.addVertUV(minF.x, maxF.y, maxF.z, 16f * PIXEL, 16f * PIXEL);
		RenderHelper.addVertUV(minF.x, minF.y, maxF.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxF.x, minF.y, maxF.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxF.x, maxF.y, maxF.z, 32f * PIXEL, 16f * PIXEL);

		// y+
		// RenderHelper.setNormal(0f, 1f, 0f);

		RenderHelper.addVertUV(maxF.x, maxF.y, maxF.z, 0f * PIXEL, 16f * PIXEL);
		RenderHelper.addVertUV(maxF.x, maxF.y, minF.z, 0f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minF.x, maxF.y, minF.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minF.x, maxF.y, maxF.z, 16f * PIXEL, 16f * PIXEL);

	}

	// TODO-ADD: Add rendering of fluids!!!
	private static void renderTopTank() {
		Vector3<Float> min = new Vector3<Float>(0f, 1f - 3f / 16f, 0f);
		Vector3<Float> max = new Vector3<Float>(1f, 1f, 1f);

		{
			// RenderHelper.setNormal(-1f, 0f, 0f);

			// x-
			RenderHelper.addVertUV(min.x, max.y, min.z, 32f * PIXEL, 1f);
			RenderHelper.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(min.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(min.x, max.y, max.z, 16f * PIXEL, 1f);

			RenderHelper.addVertUV(max.x, max.y, min.z, 32f * PIXEL, 1f);
			RenderHelper.addVertUV(max.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(max.x, max.y, max.z, 16f * PIXEL, 1f);

			// RenderHelper.setNormal(1f, 0f, 0f);

			// x+
			RenderHelper.addVertUV(max.x, max.y, max.z, 16f * PIXEL, 1f);
			RenderHelper.addVertUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(max.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(max.x, max.y, min.z, 32f * PIXEL, 1f);

			RenderHelper.addVertUV(min.x, max.y, max.z, 16f * PIXEL, 1f);
			RenderHelper.addVertUV(min.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(min.x, max.y, min.z, 32f * PIXEL, 1f);

			// RenderHelper.setNormal(0f, 0f, -1f);

			// z-
			RenderHelper.addVertUV(min.x, max.y, max.z, 32f * PIXEL, 1f);
			RenderHelper.addVertUV(min.x, min.y, max.z, 32f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(max.x, max.y, max.z, 16f * PIXEL, 1f);

			RenderHelper.addVertUV(min.x, max.y, min.z, 32f * PIXEL, 1f);
			RenderHelper.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(max.x, min.y, min.z, 16f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(max.x, max.y, min.z, 16f * PIXEL, 1f);

			// RenderHelper.setNormal(0f, 0f, 1f);

			// z+
			RenderHelper.addVertUV(max.x, max.y, min.z, 16f * PIXEL, 1f);
			RenderHelper.addVertUV(max.x, min.y, min.z, 16f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(min.x, max.y, min.z, 32f * PIXEL, 1f);

			RenderHelper.addVertUV(max.x, max.y, max.z, 16f * PIXEL, 1f);
			RenderHelper.addVertUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(min.x, min.y, max.z, 32f * PIXEL, 61f * PIXEL);
			RenderHelper.addVertUV(min.x, max.y, max.z, 32f * PIXEL, 1f);

			// RenderHelper.setNormal(0f, -1f, 0f);

			// y-
			RenderHelper.addVertUV(min.x, min.y, max.z, 16f * PIXEL, 48f * PIXEL);
			RenderHelper.addVertUV(min.x, min.y, min.z, 16f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(max.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(max.x, min.y, max.z, 32f * PIXEL, 48f * PIXEL);

			RenderHelper.addVertUV(min.x, max.y, max.z, 16f * PIXEL, 48f * PIXEL);
			RenderHelper.addVertUV(min.x, max.y, min.z, 16f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(max.x, max.y, min.z, 32f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(max.x, max.y, max.z, 32f * PIXEL, 48f * PIXEL);

			// RenderHelper.setNormal(0f, 1f, 0f);

			// y+
			RenderHelper.addVertUV(max.x, max.y, max.z, 32f * PIXEL, 48f * PIXEL);
			RenderHelper.addVertUV(max.x, max.y, min.z, 32f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(min.x, max.y, min.z, 16f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(min.x, max.y, max.z, 16f * PIXEL, 48f * PIXEL);

			RenderHelper.addVertUV(max.x, min.y, max.z, 32f * PIXEL, 48f * PIXEL);
			RenderHelper.addVertUV(max.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(min.x, min.y, min.z, 16f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(min.x, min.y, max.z, 16f * PIXEL, 48f * PIXEL);

		}

	}

	private static void renderSmokeStack() {

		// x-
		// // RenderHelper.setNormal(-1f, 0f, 0f);

		RenderHelper.addVertUV(minSt.x, maxSt.y, minSt2.z, 32f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(minSt.x, minSt.y, minSt2.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt.x, minSt.y, maxSt2.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt.x, maxSt.y, maxSt2.z, 16f * PIXEL, 48f * PIXEL);

		RenderHelper.addVertUV(maxSt.x, maxSt.y, minSt2.z, 48f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(maxSt.x, minSt.y, minSt2.z, 48f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt.x, minSt.y, maxSt2.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt.x, maxSt.y, maxSt2.z, 32f * PIXEL, 48f * PIXEL);

		// x+
		// // RenderHelper.setNormal(1f, 0f, 0f);

		RenderHelper.addVertUV(maxSt.x, maxSt.y, maxSt2.z, 16f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(maxSt.x, minSt.y, maxSt2.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt.x, minSt.y, minSt2.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt.x, maxSt.y, minSt2.z, 32f * PIXEL, 48f * PIXEL);

		RenderHelper.addVertUV(minSt.x, maxSt.y, maxSt2.z, 32f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(minSt.x, minSt.y, maxSt2.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt.x, minSt.y, minSt2.z, 48f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt.x, maxSt.y, minSt2.z, 48f * PIXEL, 48f * PIXEL);

		// z-
		// // RenderHelper.setNormal(0f, 0f, -1f);

		RenderHelper.addVertUV(maxSt2.x, maxSt.y, minSt.z, 16f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(maxSt2.x, minSt.y, minSt.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt2.x, minSt.y, minSt.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt2.x, maxSt.y, minSt.z, 32f * PIXEL, 48f * PIXEL);

		RenderHelper.addVertUV(maxSt2.x, maxSt.y, maxSt.z, 32f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(maxSt2.x, minSt.y, maxSt.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt2.x, minSt.y, maxSt.z, 48f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt2.x, maxSt.y, maxSt.z, 48f * PIXEL, 48f * PIXEL);

		// z+
		// // RenderHelper.setNormal(0f, 0f, 1f);

		RenderHelper.addVertUV(minSt2.x, maxSt.y, maxSt.z, 32f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(minSt2.x, minSt.y, maxSt.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt2.x, minSt.y, maxSt.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt2.x, maxSt.y, maxSt.z, 16f * PIXEL, 48f * PIXEL);

		RenderHelper.addVertUV(minSt2.x, maxSt.y, minSt.z, 48f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(minSt2.x, minSt.y, minSt.z, 48f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt2.x, minSt.y, minSt.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt2.x, maxSt.y, minSt.z, 32f * PIXEL, 48f * PIXEL);

		// y-
			/*// // RenderHelper.setNormal(0f, 1f, 0f);

			RenderHelper.addVertUV(max.x, min.y, max.z, 48f * PIXEL, 48f * PIXEL);
			RenderHelper.addVertUV(max.x, min.y, min.z, 48f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			RenderHelper.addVertUV(min.x, min.y, max.z, 32f * PIXEL, 48f * PIXEL);*/

		// y+
		// // RenderHelper.setNormal(0f, 1f, 0f);

		RenderHelper.addVertUV(maxSt.x, maxSt.y, maxSt.z, 64f * PIXEL, 64f * PIXEL);
		RenderHelper.addVertUV(maxSt.x, maxSt.y, minSt.z, 64f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(minSt.x, maxSt.y, minSt.z, 48f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(minSt.x, maxSt.y, maxSt.z, 48f * PIXEL, 64f * PIXEL);

		// x-/z-
		// // RenderHelper.setNormal(-1f, 0f, -1f);

		RenderHelper.addVertUV(minSt2.x, maxSt.y, minSt.z, 32f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(minSt2.x, minSt.y, minSt.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt.x, minSt.y, minSt2.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt.x, maxSt.y, minSt2.z, 16f * PIXEL, 48f * PIXEL);

		RenderHelper.addVertUV(maxSt.x, maxSt.y, maxSt2.z, 32f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(maxSt.x, minSt.y, maxSt2.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt2.x, minSt.y, maxSt.z, 48f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt2.x, maxSt.y, maxSt.z, 48f * PIXEL, 48f * PIXEL);

		// x+/z-
		// // RenderHelper.setNormal(-1f, 0f, 1f);

		RenderHelper.addVertUV(maxSt.x, maxSt.y, minSt2.z, 32f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(maxSt.x, minSt.y, minSt2.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt2.x, minSt.y, minSt.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt2.x, maxSt.y, minSt.z, 16f * PIXEL, 48f * PIXEL);

		RenderHelper.addVertUV(minSt2.x, maxSt.y, maxSt.z, 32f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(minSt2.x, minSt.y, maxSt.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt.x, minSt.y, maxSt2.z, 48f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt.x, maxSt.y, maxSt2.z, 48f * PIXEL, 48f * PIXEL);

		// x-/z+
		// // RenderHelper.setNormal(-1f, 0f, 1f);

		RenderHelper.addVertUV(minSt.x, maxSt.y, maxSt2.z, 32f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(minSt.x, minSt.y, maxSt2.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt2.x, minSt.y, maxSt.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt2.x, maxSt.y, maxSt.z, 16f * PIXEL, 48f * PIXEL);

		RenderHelper.addVertUV(maxSt2.x, maxSt.y, minSt.z, 32f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(maxSt2.x, minSt.y, minSt.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt.x, minSt.y, minSt2.z, 48f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt.x, maxSt.y, minSt2.z, 48f * PIXEL, 48f * PIXEL);

		// x+/z+
		// // RenderHelper.setNormal(1f, 0f, 1f);

		RenderHelper.addVertUV(maxSt2.x, maxSt.y, maxSt.z, 32f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(maxSt2.x, minSt.y, maxSt.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt.x, minSt.y, maxSt2.z, 16f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(maxSt.x, maxSt.y, maxSt2.z, 16f * PIXEL, 48f * PIXEL);

		RenderHelper.addVertUV(minSt.x, maxSt.y, minSt2.z, 32f * PIXEL, 48f * PIXEL);
		RenderHelper.addVertUV(minSt.x, minSt.y, minSt2.z, 32f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt2.x, minSt.y, minSt.z, 48f * PIXEL, 32f * PIXEL);
		RenderHelper.addVertUV(minSt2.x, maxSt.y, minSt.z, 48f * PIXEL, 48f * PIXEL);

	}

	private static void renderFluid(TileEntityRefinery te) {
		RenderHelper.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		if (te.getTank(0).getFluid() != null && te.getTank(0).getFluidAmount() > 0) {

			final ResourceLocation fluidLocation = te.getTank(0).getFluid().getFluid().getStill();
			TextureAtlasSprite inputIcon = textureMap.getTextureExtry(fluidLocation.toString());
			// float progress = ((float) te.getTank(0).getFluidAmount() / te.getTank(0).getCapacity() * 10f) / 16f;
			float progress = (3f + ((int) (te.getTank(0).getFluidAmount() / (float) (te.getTank(0).getCapacity()) * 10))) / 16f;
			// ProjectZed.logHelper.info("progress", te.getTank(0).getFluidAmount(), progress, progress * (maxT.y - TEX_SPACING));

			// ProjectZed.logHelper.info(inputIcon.getIconName());
			if (progress > 3f / 16f) {

				float minU = inputIcon.getMinU();
				float maxU = inputIcon.getMaxU();
				float minV = inputIcon.getMinV();
				float maxV = inputIcon.getMaxV();

				RenderHelper.startDrawingQuads();

				// x-
				// RenderHelper.setNormal(-1f, 0f, 0f);

				RenderHelper.addVertUV(minT.x + TEX_SPACING, maxT.y * progress, minT.z - TEX_SPACING, maxU, maxV);
				RenderHelper.addVertUV(minT.x + TEX_SPACING, minT.y, minT.z - TEX_SPACING, maxU, minV);
				RenderHelper.addVertUV(minT.x + TEX_SPACING, minT.y, maxT.z - TEX_SPACING, minU, minV);
				RenderHelper.addVertUV(minT.x + TEX_SPACING, maxT.y * progress, maxT.z - TEX_SPACING, minU, maxV);

				// x+
				// RenderHelper.setNormal(1f, 0f, 0f);

				RenderHelper.addVertUV(maxT.x - TEX_SPACING, maxT.y * progress, maxT.z - TEX_SPACING, minU, maxV);
				RenderHelper.addVertUV(maxT.x - TEX_SPACING, minT.y, maxT.z - TEX_SPACING, minU, minV);
				RenderHelper.addVertUV(maxT.x - TEX_SPACING, minT.y, minT.z - TEX_SPACING, maxU, minV);
				RenderHelper.addVertUV(maxT.x - TEX_SPACING, maxT.y * progress, minT.z - TEX_SPACING, maxU, maxV);

				// z-
				// RenderHelper.setNormal(0f, 0f, -1f);

				RenderHelper.addVertUV(maxT.x + TEX_SPACING, maxT.y * progress, minT.z + TEX_SPACING, minU, maxV);
				RenderHelper.addVertUV(maxT.x + TEX_SPACING, minT.y, minT.z + TEX_SPACING, minU, minV);
				RenderHelper.addVertUV(minT.x + TEX_SPACING, minT.y, minT.z + TEX_SPACING, maxU, minV);
				RenderHelper.addVertUV(minT.x + TEX_SPACING, maxT.y * progress, minT.z + TEX_SPACING, maxU, maxV);

				// y+
				// RenderHelper.setNormal(0f, 1f, 0f);

				RenderHelper.addVertUV(maxT.x - TEX_SPACING, maxT.y * progress, maxT.z - TEX_SPACING, maxU, maxV);
				RenderHelper.addVertUV(maxT.x - TEX_SPACING, maxT.y * progress, minT.z - TEX_SPACING, maxU, minV);
				RenderHelper.addVertUV(minT.x - TEX_SPACING, maxT.y * progress, minT.z - TEX_SPACING, minU, minV);
				RenderHelper.addVertUV(minT.x - TEX_SPACING, maxT.y * progress, maxT.z - TEX_SPACING, minU, maxV);

				// this.bindTexture(texture);

				RenderHelper.draw();
			}
		}

		if (te.getTank(1).getFluid() != null && te.getTank(1).getFluidAmount() > 0) {
			final ResourceLocation fluidLocation = te.getTank(1).getFluid().getFluid().getStill();
			TextureAtlasSprite inputIcon = textureMap.getTextureExtry(fluidLocation.toString());
			// float progress = ((float) te.getTank(1).getFluidAmount() / te.getTank(1).getCapacity() * 10f) / 16f;
			float progress = (3f + ((int) (te.getTank(1).getFluidAmount() / (float) (te.getTank(1).getCapacity()) * 10))) / 16f;
			// ProjectZed.logHelper.info("progress", te.getTank(1).getFluidAmount(), progress, progress * (maxT.y - TEX_SPACING));

			if (progress > 3f / 16f) {

				float minU = inputIcon.getMinU();
				float maxU = inputIcon.getMaxU();
				float minV = inputIcon.getMinV();
				float maxV = inputIcon.getMaxV();

				RenderHelper.startDrawingQuads();

				// x-
				// RenderHelper.setNormal(-1f, 0f, 0f);

				// ProjectZed.logHelper.info(minT.y, minT2.y, maxT2.y * progress);

				RenderHelper.addVertUV(minT2.x - TEX_SPACING, maxT2.y * progress, minT2.z - TEX_SPACING, maxU, maxV);
				RenderHelper.addVertUV(minT2.x - TEX_SPACING, minT2.y, minT2.z - TEX_SPACING, maxU, minV);
				RenderHelper.addVertUV(minT2.x - TEX_SPACING, minT2.y, maxT2.z - TEX_SPACING, minU, minV);
				RenderHelper.addVertUV(minT2.x - TEX_SPACING, maxT2.y * progress, maxT2.z - TEX_SPACING, minU, maxV);

				// x+
				// RenderHelper.setNormal(1f, 0f, 0f);

				RenderHelper.addVertUV(maxT2.x + TEX_SPACING, maxT2.y * progress, maxT2.z + TEX_SPACING, minU, maxV);
				RenderHelper.addVertUV(maxT2.x + TEX_SPACING, minT2.y, maxT2.z + TEX_SPACING, minU, minV);
				RenderHelper.addVertUV(maxT2.x + TEX_SPACING, minT2.y, minT2.z + TEX_SPACING, maxU, minV);
				RenderHelper.addVertUV(maxT2.x + TEX_SPACING, maxT2.y * progress, minT2.z + TEX_SPACING, maxU, maxV);

				// z-
				// RenderHelper.setNormal(0f, 0f, -1f);

				RenderHelper.addVertUV(maxT2.x - TEX_SPACING, maxT2.y * progress, minT2.z - TEX_SPACING, minU, maxV);
				RenderHelper.addVertUV(maxT2.x - TEX_SPACING, minT2.y, minT2.z - TEX_SPACING, minU, minV);
				RenderHelper.addVertUV(minT2.x - TEX_SPACING, minT2.y, minT2.z - TEX_SPACING, maxU, minV);
				RenderHelper.addVertUV(minT2.x - TEX_SPACING, maxT2.y * progress, minT2.z - TEX_SPACING, maxU, maxV);

				// y+
				// RenderHelper.setNormal(0f, 1f, 0f);

				RenderHelper.addVertUV(maxT2.x - TEX_SPACING, maxT2.y * progress, maxT2.z - TEX_SPACING, maxU, maxV);
				RenderHelper.addVertUV(maxT2.x - TEX_SPACING, maxT2.y * progress, minT2.z - TEX_SPACING, maxU, minV);
				RenderHelper.addVertUV(minT2.x - TEX_SPACING, maxT2.y * progress, minT2.z - TEX_SPACING, minU, minV);
				RenderHelper.addVertUV(minT2.x - TEX_SPACING, maxT2.y * progress, maxT2.z - TEX_SPACING, minU, maxV);

				// this.bindTexture(texture);

				RenderHelper.draw();
			}
		}

	}

}

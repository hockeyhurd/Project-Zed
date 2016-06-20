package com.projectzed.mod.renderer;

import com.hockeyhurd.hcorelib.api.client.util.RenderHelper;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author hockeyhurd
 * @version 6/20/2016.
 */
@SideOnly(Side.CLIENT)
public class FluidTankRenderer extends TileEntitySpecialRenderer<TileEntityFluidTankBase> {

	private static final TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
	private final ResourceLocation texture;
	private final int tier;
	private static final float PIXEL = 1.0f / 48.0f;
	private static final boolean renderInside = true;

	private Vector3<Float> minVec, maxVec;
	private Vector3<Float> fluidMinVec, fluidMaxVec;

	public FluidTankRenderer(int tier) {
		this.texture = new ResourceLocation("projectzed", "textures/blocks/fluidTankTier" + tier + ".png");
		this.tier = tier;

		minVec = new Vector3<Float>(48f / 4f * PIXEL, 0f + 0.001f, 48f / 4f * PIXEL);
		maxVec = new Vector3<Float>(1f - 48f / 4f * PIXEL, 1f /*- 48f / 8f * this.PIXEL*/, 1f - 48f / 4f * PIXEL);

		fluidMinVec = new Vector3<Float>(5f / 16f, 2f / 16f, 5f / 16f);
		fluidMaxVec = new Vector3<Float>(1f - 5f / 16f, 1f -  1f / 16f, 1f - 5f / 16f);
	}

	@Override
	public void renderTileEntityAt(TileEntityFluidTankBase te, double x, double y, double z, float partialTicks, int destroyStage) {
		final Vector3<Double> renderVec = new Vector3<Double>(x, y, z);
		// final Vector3<Integer> atVec = VectorHelper.toVector3i(te.getPos());

		RenderHelper.setupPreRender(texture, renderVec);

		drawCuboid(te, minVec, maxVec, 0);
		drawCuboid(te, minVec, maxVec, 1);

		drawFluid(te, fluidMinVec, fluidMaxVec);

		RenderHelper.finishPostRender(renderVec);
	}

	protected static void drawCuboid(TileEntityFluidTankBase te, float min, float max, int layer) {
		drawCuboid(te, new Vector3<Float>(min, min, min), new Vector3<Float>(max, max, max), layer);
	}

	protected static void drawCuboid(TileEntityFluidTankBase te, Vector3<Float> minVec, Vector3<Float> maxVec, int layer) {
		if (te.getWorld() != null && te.getWorld().getTotalWorldTime() % 20L == 0) {
			te = (TileEntityFluidTankBase) te.getWorld().getTileEntity(te.getPos());
			// tier = te.getTier();
		}

		RenderHelper.startDrawingQuads();

		int counter = 0;
		for (byte valve : te.getSidedArray()) {

			float max = 1f;
			float min = 0f;
			float difU = 0f;
			float difV = 0f;

			if (valve == -1) {
				if (layer == 0) {
					max = 16f * PIXEL;
					min = 0f * PIXEL;
					difU = 0f * PIXEL;
					difV = 0f * PIXEL;
				}

				else if (layer == 1) {
					max = 32f * PIXEL;
					min = 16f * PIXEL;
					difU = 16f * PIXEL;
					difV = 0f * PIXEL;
				}
			}

			else if (valve == 1) {
				if (layer == 0) {
					max = 48f * PIXEL;
					min = 32f * PIXEL;
					difU = 0f * PIXEL;
					difV = 32f * PIXEL;
				}

				else if (layer == 1) {
					max = 48f * PIXEL;
					min = 32f * PIXEL;
					difU = 0f * PIXEL;
					difV = 16f * PIXEL;
				}
			}

			else {
				if (layer == 0) {
					max = 32f * PIXEL;
					min = 16f * PIXEL;
					difU = 0f * PIXEL;
					difV = 16f * PIXEL;
				}

				else if (layer == 1) {
					max = 32f * PIXEL;
					min = 16f * PIXEL;
					difU = 0f * PIXEL;
					difV = 0f * PIXEL;
				}
			}

			// -zz
			if (counter == 2) {
				RenderHelper.drawZNeg(minVec, maxVec, min - difU, min - difV, max - difU, max - difV);
				RenderHelper.drawZNeg(maxVec, minVec, min - difU, min - difV, max - difU, max - difV);
			}

			// +zz
			else if (counter == 3) {
				RenderHelper.drawZPos(minVec, maxVec, min - difU, min - difV, max - difU, max - difV);
				RenderHelper.drawZPos(maxVec, minVec, min - difU, min - difV, max - difU, max - difV);
			}

			// -xx
			else if (counter == 4) {
				RenderHelper.drawXNeg(minVec, maxVec, min - difU, min - difV, max - difU, max - difV);
				RenderHelper.drawXNeg(maxVec, minVec, min - difU, min - difV, max - difU, max - difV);
			}

			// +xx
			else if (counter == 5) {
				RenderHelper.drawXPos(minVec, maxVec, min - difU, min - difV, max - difU, max - difV);
				RenderHelper.drawXPos(maxVec, minVec, min - difU, min - difV, max - difU, max - difV);
			}

			// +yy
			else if (counter == 1 && layer == 0) {

				if (valve == -1) {
					max = 48f * PIXEL;
					min = 32f * PIXEL;
					difU = 32f * PIXEL;
					difV = 0f * PIXEL;
				}

				else if (valve == 0) {
					max = 48f * PIXEL;
					min = 32f * PIXEL;
					difU = 16f * PIXEL;
					difV = 0f * PIXEL;
				}

				else if (valve == 1) {
					max = 48f * PIXEL;
					min = 32f * PIXEL;
					difU = 0f * PIXEL;
					difV = 0f * PIXEL;
				}

				RenderHelper.drawYPos(minVec, maxVec, min - difU, min - difV, max - difU, max - difV);
				RenderHelper.drawYPos(maxVec, minVec, min - difU, min - difV, max - difU, max - difV);
			}

			// -yy
			else if (counter == 0 && layer == 0) {

				if (valve == -1) {
					max = 48f * PIXEL;
					min = 32f * PIXEL;
					difU = 32f * PIXEL;
					difV = 0f * PIXEL;
				}

				else if (valve == 0) {
					max = 48f * PIXEL;
					min = 32f * PIXEL;
					difU = 16f * PIXEL;
					difV = 0f * PIXEL;
				}

				else if (valve == 1) {
					max = 48f * PIXEL;
					min = 32f * PIXEL;
					difU = 0f * PIXEL;
					difV = 0f * PIXEL;
				}

				RenderHelper.drawYNeg(minVec, maxVec, min - difU, min - difV, max - difU, max - difV);
				RenderHelper.drawYNeg(maxVec, minVec, min - difU, min - difV, max - difU, max - difV);
			}

			counter++;
		}

		RenderHelper.draw();
	}

	/**
	 * Method to handler rendering of fluids inside of the tank.
	 *
	 * @param te tilentity object to reference.
	 * @param vec0 min vector.
	 * @param vec1 max vector.
	 */
	protected static void drawFluid(TileEntityFluidTankBase te, Vector3<Float> vec0, Vector3<Float> vec1) {
		if (te == null || te.getTank() == null) {
			// System.err.println("Error something is null!");
			return;
		}

		FluidStack fluid = te.getTank().getFluid();

		if (fluid == null /*|| this.progressIndex == 0*/) {
			// System.out.println(te.getTank().getFluidAmount());
			return;
		}

		final ResourceLocation fluidTexture = fluid.getFluid().getStill();
		// final ResourceLocation fluidTexture = fluid.getFluid().getFlowing();
		if (fluidTexture == null) {
			// System.out.println("null returning!");
			return;
		}

		TextureAtlasSprite sprite = textureMap.getTextureExtry(fluidTexture.toString());
		if (sprite == null) {
			// ProjectZed.logHelper.info("Sprite is null!");
			sprite = textureMap.getTextureExtry(TextureMap.LOCATION_MISSING_TEXTURE.toString());

			if (sprite == null)
				return;
		}

		final Vector3<Float> maxVecY = vec1.copy();

		// vec1.y = (3f + this.progressIndex) / 16f;
		vec1.y = (3f + ((int) (te.getTank().getFluidAmount() / (float) (te.getTank().getCapacity()) * 10))) / 16f;

		RenderHelper.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		// RenderHelper.bindTexture(fluidTexture);
		RenderHelper.startDrawingQuads();

		RenderHelper.drawZNeg(vec0, vec1, sprite.getMinU(), sprite.getMinV(), sprite.getMaxU(), sprite.getMaxV());
		RenderHelper.drawZPos(vec0, vec1, sprite.getMinU(), sprite.getMinV(), sprite.getMaxU(), sprite.getMaxV());
		RenderHelper.drawXNeg(vec0, vec1, sprite.getMinU(), sprite.getMinV(), sprite.getMaxU(), sprite.getMaxV());
		RenderHelper.drawXPos(vec0, vec1, sprite.getMinU(), sprite.getMinV(), sprite.getMaxU(), sprite.getMaxV());

		RenderHelper.drawYNeg(vec0, maxVecY, sprite.getMinU(), sprite.getMinV(), sprite.getMaxU(), sprite.getMaxV());
		RenderHelper.drawYPos(vec0, maxVecY, sprite.getMinU(), sprite.getMinV(), sprite.getMaxU(), sprite.getMaxV());

		RenderHelper.draw();

	}

}

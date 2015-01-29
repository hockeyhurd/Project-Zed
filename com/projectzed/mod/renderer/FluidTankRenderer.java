package com.projectzed.mod.renderer;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class handling all client side rendering code for fluid tanks.
 * 
 * @author hockeyhurd
 * @version Jan 22, 2015
 */
@SideOnly(Side.CLIENT)
public class FluidTankRenderer extends TileEntitySpecialRenderer {

	private ResourceLocation texture, fluidTexture;
	private final float PIXEL = 1f / 48f;
	private float progressBar = 0.0f;
	private int progressIndex = 0;
	private byte tier = 0;
	private boolean renderInside = true;

	private Vector4Helper<Float> minVec, maxVec;
	private Vector4Helper<Float> fluidMinVec, fluidMaxVec;

	/**
	 * @param tier
	 */
	public FluidTankRenderer(byte tier) {
		// this.texture = new ResourceLocation("projectzed",
		// "textures/blocks/fluidTankGeneric.png");
		this.texture = new ResourceLocation("projectzed", "textures/blocks/fluidTankTier" + tier + ".png");
		this.fluidTexture = new ResourceLocation("/terrain.png");

		minVec = new Vector4Helper<Float>(48f / 4f * this.PIXEL, 0f, 48f / 4f * this.PIXEL);
		maxVec = new Vector4Helper<Float>(1f - 48f / 4f * this.PIXEL, 1f /*- 48f / 8f * this.PIXEL*/, 1f - 48f / 4f * this.PIXEL);
		
		// fluidMinVec = new Vector4Helper<Float>(48f / 6f * this.PIXEL, 1f / 48f, 48f / 6f * this.PIXEL);
		// fluidMaxVec = new Vector4Helper<Float>(1f - 48f / 6f * this.PIXEL, 1f - 1f / 48f, 1f - 48f / 6f * this.PIXEL);
		
		fluidMinVec = new Vector4Helper<Float>(1f / 16f, 1f / 16f, 1f / 16f);
		fluidMaxVec = new Vector4Helper<Float>(1f - 1f / 16f, 1f - 1f / 16f, 1f - 1f / 16f);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer#
	 * renderTileEntityAt(net.minecraft.tileentity.TileEntity, double, double,
	 * double, float)
	 */
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {

		GL11.glTranslated(x, y, z);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0 % 65536, 0xf0 / 65536);

		this.bindTexture(texture);

		drawCuboid((TileEntityFluidTankBase) te, minVec, maxVec, (byte) 0);
		drawCuboid((TileEntityFluidTankBase) te, minVec, maxVec, (byte) 1);

		drawFluid((TileEntityFluidTankBase) te, fluidMinVec, fluidMaxVec);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glTranslated(-x, -y, -z);
	}

	protected void drawCuboid(TileEntityFluidTankBase te, float min, float max, byte layer) {
		drawCuboid(te, new Vector4Helper<Float>(min, min, min), new Vector4Helper<Float>(max, max, max), layer);
	}

	protected void drawCuboid(TileEntityFluidTankBase te, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, byte layer) {

		if (te.getWorldObj() != null && te.getWorldObj().getTotalWorldTime() % 20L == 0) {
			te = (TileEntityFluidTankBase) te.getWorldObj().getTileEntity(te.xCoord, te.yCoord, te.zCoord);
			tier = te.getTier();
			this.progressBar = te.getTank().getFluidAmount() / (float) (te.getTank().getCapacity());
			progressIndex = (int) (this.progressBar * 8);

			// if (this.progressBar > 0 && te.getTank().getFluid() != null)
			// ProjectZed.logHelper.info(te.getTank().getFluid().getLocalizedName());
			// else ProjectZed.logHelper.info(this.progressBar);
		}

		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();

		byte counter = 0;
		for (byte valve : te.getSidedArray()) {

			float max = 1f;
			float min = 0f;
			float difU = 0f;
			float difV = 0f;

			if (valve == -1) {
				if (layer == 0) {
					max = 16f * this.PIXEL;
					min = 0f * this.PIXEL;
					difU = 0f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}

				else if (layer == 1) {
					max = 32f * this.PIXEL;
					min = 16f * this.PIXEL;
					difU = 16f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
			}

			else if (valve == 1) {
				if (layer == 0) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 0f * this.PIXEL;
					difV = 32f * this.PIXEL;
				}

				else if (layer == 1) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 0f * this.PIXEL;
					difV = 16f * this.PIXEL;
				}
			}

			else {
				if (layer == 0) {
					max = 32f * this.PIXEL;
					min = 16f * this.PIXEL;
					difU = 0f * this.PIXEL;
					difV = 16f * this.PIXEL;
				}

				else if (layer == 1) {
					max = 32f * this.PIXEL;
					min = 16f * this.PIXEL;
					difU = 0f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
			}

			if (layer == 2) {

				if (progressIndex == 0) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 32f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}

				else if (progressIndex == 1) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 16f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}

				else if (progressIndex == 2) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 0f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}

				else if (progressIndex == 3) {
					max = 64f * this.PIXEL;
					min = 48f * this.PIXEL;
					difU = 48f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}

				else if (progressIndex == 4) {
					max = 64f * this.PIXEL;
					min = 48f * this.PIXEL;
					difU = 32f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}

				else if (progressIndex == 5) {
					max = 64f * this.PIXEL;
					min = 48f * this.PIXEL;
					difU = 16f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}

				else if (progressIndex == 6) {
					max = 80f * this.PIXEL;
					min = 64f * this.PIXEL;
					difU = 64f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}

				else if (progressIndex == 7) {
					max = 80f * this.PIXEL;
					min = 64f * this.PIXEL;
					difU = 48f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}

				else if (progressIndex == 8) {
					max = 80f * this.PIXEL;
					min = 64f * this.PIXEL;
					difU = 32f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
			}

			// -zz
			if (counter == 2) drawZNeg(tess, minVec, maxVec, min, max, difU, difV);

			// +zz
			if (counter == 3) drawZPos(tess, minVec, maxVec, min, max, difU, difV);

			// -xx
			if (counter == 4) drawXNeg(tess, minVec, maxVec, min, max, difU, difV);

			// +xx
			if (counter == 5) drawXPos(tess, minVec, maxVec, min, max, difU, difV);

			// +yy
			if (counter == 1) drawYPos(tess, minVec, maxVec, min, max, difU, difV);

			// -yy
			if (counter == 0) drawYNeg(tess, minVec, maxVec, min, max, difU, difV);

			counter++;
		}

		tess.draw();
	}

	protected void drawFluid(TileEntityFluidTankBase te, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec) {
		if (te == null || te.getTank() == null) {
			// System.err.println("Error something is null!");
			return;
		}

		FluidStack fluid = te.getTank().getFluid();

		if (fluid == null /*|| this.progressIndex == 0*/) {
			// System.out.println(te.getTank().getFluidAmount());
			return;
		}

		IIcon icon = fluid.getFluid().getStillIcon();
		if (icon == null) {
			System.out.println("null returning!");
			return;
		}

		this.bindTexture(TextureMap.locationBlocksTexture);
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		
		/*tess.addVertexWithUV(minVec.x, maxVec.y, maxVec.z, icon.getMinU(), icon.getMinV());
		tess.addVertexWithUV(minVec.x, minVec.y, maxVec.z, icon.getMinU(), icon.getMaxV());
		tess.addVertexWithUV(maxVec.x, minVec.y, maxVec.z, icon.getMaxU(), icon.getMaxV());
		tess.addVertexWithUV(maxVec.x, maxVec.y, maxVec.z, icon.getMaxU(), icon.getMinV());*/
		
		tess.draw();
		
	}

	protected void drawZNeg(Tessellator tess, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, float min, float max, float difU, float difV) {
		tess.addVertexWithUV(minVec.x, maxVec.y, maxVec.z, min - difU, min - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, maxVec.z, min - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, maxVec.y, maxVec.z, max - difU, min - difV);

		if (renderInside) {
			tess.addVertexWithUV(maxVec.x, maxVec.y, maxVec.z, max - difU, min - difV);
			tess.addVertexWithUV(maxVec.x, minVec.y, maxVec.z, max - difU, max - difV);
			tess.addVertexWithUV(minVec.x, minVec.y, maxVec.z, min - difU, max - difV);
			tess.addVertexWithUV(minVec.x, maxVec.y, maxVec.z, min - difU, min - difV);
		}
	}

	protected void drawZPos(Tessellator tess, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, float min, float max, float difU, float difV) {
		tess.addVertexWithUV(maxVec.x, maxVec.y, minVec.z, min - difU, min - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, minVec.z, min - difU, max - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, minVec.z, max - difU, max - difV);
		tess.addVertexWithUV(minVec.x, maxVec.y, minVec.z, max - difU, min - difV);

		if (renderInside) {
			tess.addVertexWithUV(minVec.x, maxVec.y, minVec.z, max - difU, min - difV);
			tess.addVertexWithUV(minVec.x, minVec.y, minVec.z, max - difU, max - difV);
			tess.addVertexWithUV(maxVec.x, minVec.y, minVec.z, min - difU, max - difV);
			tess.addVertexWithUV(maxVec.x, maxVec.y, minVec.z, min - difU, min - difV);
		}
	}

	protected void drawXNeg(Tessellator tess, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, float min, float max, float difU, float difV) {
		tess.addVertexWithUV(minVec.x, maxVec.y, minVec.z, min - difU, min - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, minVec.z, min - difU, max - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(minVec.x, maxVec.y, maxVec.z, max - difU, min - difV);

		if (renderInside) {
			tess.addVertexWithUV(minVec.x, maxVec.y, maxVec.z, max - difU, min - difV);
			tess.addVertexWithUV(minVec.x, minVec.y, maxVec.z, max - difU, max - difV);
			tess.addVertexWithUV(minVec.x, minVec.y, minVec.z, min - difU, max - difV);
			tess.addVertexWithUV(minVec.x, maxVec.y, minVec.z, min - difU, min - difV);
		}
	}

	protected void drawXPos(Tessellator tess, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, float min, float max, float difU, float difV) {
		tess.addVertexWithUV(maxVec.x, maxVec.y, maxVec.z, max - difU, min - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, minVec.z, min - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, maxVec.y, minVec.z, min - difU, min - difV);

		if (renderInside) {
			tess.addVertexWithUV(maxVec.x, maxVec.y, minVec.z, min - difU, min - difV);
			tess.addVertexWithUV(maxVec.x, minVec.y, minVec.z, min - difU, max - difV);
			tess.addVertexWithUV(maxVec.x, minVec.y, maxVec.z, max - difU, max - difV);
			tess.addVertexWithUV(maxVec.x, maxVec.y, maxVec.z, max - difU, min - difV);
		}
	}

	protected void drawYNeg(Tessellator tess, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, float min, float max, float difU, float difV) {
		tess.addVertexWithUV(maxVec.x, minVec.y - 0.01d, minVec.z, max - difU, min - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y - 0.01d, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(minVec.x, minVec.y - 0.01d, maxVec.z, min - difU, max - difV);
		tess.addVertexWithUV(minVec.x, minVec.y - 0.01d, minVec.z, min - difU, min - difV);

		if (renderInside) {
			tess.addVertexWithUV(minVec.x, minVec.y - 0.01d, minVec.z, min - difU, min - difV);
			tess.addVertexWithUV(minVec.x, minVec.y - 0.01d, maxVec.z, min - difU, max - difV);
			tess.addVertexWithUV(maxVec.x, minVec.y - 0.01d, maxVec.z, max - difU, max - difV);
			tess.addVertexWithUV(maxVec.x, minVec.y - 0.01d, minVec.z, max - difU, min - difV);
		}
	}

	protected void drawYPos(Tessellator tess, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, float min, float max, float difU, float difV) {
		tess.addVertexWithUV(minVec.x, maxVec.y - 0.01d, minVec.z, min - difU, min - difV);
		tess.addVertexWithUV(minVec.x, maxVec.y - 0.01d, maxVec.z, min - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, maxVec.y - 0.01d, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, maxVec.y - 0.01d, minVec.z, max - difU, min - difV);

		if (renderInside) {
			tess.addVertexWithUV(maxVec.x, maxVec.y - 0.01d, minVec.z, max - difU, min - difV);
			tess.addVertexWithUV(maxVec.x, maxVec.y - 0.01d, maxVec.z, max - difU, max - difV);
			tess.addVertexWithUV(minVec.x, maxVec.y - 0.01d, maxVec.z, min - difU, max - difV);
			tess.addVertexWithUV(minVec.x, maxVec.y - 0.01d, minVec.z, min - difU, min - difV);
		}
	}

}

package com.projectzed.mod.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;

/**
 * Class containing rendering code for energy cell bank. <br>
 * <br>
 * Also, Never forget 12/7/1941. <br>
 * "A day in which will live in infamy." ~FDR.
 * 
 * @author hockeyhurd
 * @version Dec 7, 2014
 */
public class EnergyBankRenderer extends TileEntitySpecialRenderer {

	private ResourceLocation texture;
	private final float PIXEL = 1f / 48f;

	public EnergyBankRenderer(byte tier) {
		super();
		// this.texture = new ResourceLocation("projectzed", "textures/blocks/energyCellTier" + tier + ".png");
		this.texture = new ResourceLocation("projectzed", "textures/blocks/energyCellGeneric.png");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer#renderTileEntityAt(net.minecraft.tileentity.TileEntity, double, double,
	 * double, float)
	 */
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
		GL11.glTranslated(x, y, z);
		GL11.glDisable(GL11.GL_LIGHTING);
		this.bindTexture(texture);

		int xx = te.xCoord;
		int yy = te.yCoord;
		int zz = te.zCoord;

		// TODO: Testing code (must remove):
		
		// -1
		/*drawCuboid((TileEntityEnergyBankBase) te, 0f, 1f, 0, -1);
		drawCuboid((TileEntityEnergyBankBase) te, 1f / 48f, 1f - 1f / 48f, 1, -1);
		drawCuboid((TileEntityEnergyBankBase) te, 2f / 48f, 1f - 2f / 48f, 2, -1);*/

		// +1
		/*drawCuboid((TileEntityEnergyBankBase) te, 0f, 1f, 0, 1);
		drawCuboid((TileEntityEnergyBankBase) te, 1f / 48f, 1f - 1f / 48f, 1, 1);
		drawCuboid((TileEntityEnergyBankBase) te, 2f / 48f, 1f - 2f / 48f, 2, 1);*/
		
		// 0
		/*drawCuboid((TileEntityEnergyBankBase) te, 0f, 1f, 0, 0);
		drawCuboid((TileEntityEnergyBankBase) te, 1f / 48f, 1f - 1f / 48f, 1, 0);
		drawCuboid((TileEntityEnergyBankBase) te, 2f / 48f, 1f - 2f / 48f, 2, 0);*/

		drawCuboid((TileEntityEnergyBankBase) te, 0f, 1f, 0, 0);
		drawCuboid((TileEntityEnergyBankBase) te, 1f / 48f, 1f - 1f / 48f, 1, 0);
		drawCuboid((TileEntityEnergyBankBase) te, 2f / 48f, 1f - 2f / 48f, 2, -1);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
	}

	protected void drawCuboid(TileEntityEnergyBankBase te, float min, float max, int layer, int valve) {
		drawCuboid(te, new Vector4Helper<Float>(min, min, min), new Vector4Helper<Float>(max, max, max), layer, valve);
	}

	/**
	 * Method used for drawing energy cell bank into world. <br>
	 * <br>
	 * NOTE: This class resembles
	 * {@link com.hockeyhurd.api.util.TessellatorHelper#drawCuboid(float, float, float, double, com.hockeyhurd.api.renderer.Color4i)}
	 * 
	 * @param te = te to draw as reference.
	 * @param minVec
	 * @param maxVec
	 * @param layer
	 * @param valve
	 */
	protected void drawCuboid(TileEntityEnergyBankBase te, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, int layer, int valve) {
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();

		float max = 1f;
		float min = 0f;
		float difU = 0f;
		float difV = 0f;
		
		if (valve == -1) {
			if (layer == 0) {
				max = 16f * this.PIXEL;
				min = 0f;
				difU = 0f;
				difV = 0f;
			}
			
			else if (layer == 1) {
				max = 32f * this.PIXEL;
				min = 16f * this.PIXEL;
				difU = 16f * this.PIXEL;
				difV = 0f;
			}
			
			else if (layer == 2) {
				max = 48f * this.PIXEL;
				min = 32f * this.PIXEL;
				
				difU = 32f * this.PIXEL;
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
				max = 32f * this.PIXEL;
				min = 16f * this.PIXEL;
				difU = 32f * this.PIXEL;
				difV = 0f * this.PIXEL;
			}
			
			else if (layer == 2) {
				max = 16f * this.PIXEL;
				min = 0f * this.PIXEL;
				difU = 16f * this.PIXEL;
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
			
			else if (layer == 2) {
				max = 32f * this.PIXEL;
				min = 16f * this.PIXEL;
				difU = 0f * this.PIXEL;
				difV = 32f * this.PIXEL;
			}
		}

		// -zz
		tess.addVertexWithUV(minVec.x, maxVec.y, maxVec.z, min - difU, min - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, maxVec.z, min - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, maxVec.y, maxVec.z, max - difU, min - difV);

		// +zz
		tess.addVertexWithUV(maxVec.x, maxVec.y, minVec.z, min - difU, min - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, minVec.z, min - difU, max - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, minVec.z, max - difU, max - difV);
		tess.addVertexWithUV(minVec.x, maxVec.y, minVec.z, max - difU, min - difV);

		// -xx
		tess.addVertexWithUV(minVec.x, maxVec.y, minVec.z, min - difU, min - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, minVec.z, min - difU, max - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(minVec.x, maxVec.y, maxVec.z, max - difU, min - difV);

		// +xx
		tess.addVertexWithUV(maxVec.x, maxVec.y, maxVec.z, max - difU, min - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, minVec.z, min - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, maxVec.y, minVec.z, min - difU, min - difV);

		// +yy
		tess.addVertexWithUV(minVec.x, maxVec.y - 0.01d, minVec.z, min - difU, min - difV);
		tess.addVertexWithUV(minVec.x, maxVec.y - 0.01d, maxVec.z, min - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, maxVec.y - 0.01d, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, maxVec.y - 0.01d, minVec.z, max - difU, min - difV);

		// -yy
		tess.addVertexWithUV(maxVec.x, minVec.y, minVec.z, max - difU, min - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, maxVec.z, min - difU, max - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, minVec.z, min - difU, min - difV);

		tess.draw();
	}

}

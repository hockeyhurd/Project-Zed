package com.projectzed.mod.renderer;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing rendering code for energy cell bank. <br>
 * <br>
 * Also, Never forget 12/7/1941. <br>
 * "A day in which will live in infamy." ~FDR.
 * 
 * @author hockeyhurd
 * @version Dec 7, 2014
 */
@SideOnly(Side.CLIENT)
public class EnergyBankRenderer extends TileEntitySpecialRenderer {

	private ResourceLocation texture;
	private final float PIXEL = 1f / 144f;
	private float progressBar = 0.0f;

	/**
	 * @param tier
	 */
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
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0 % 65536, 0xf0 / 65536);
		this.bindTexture(texture);

		int xx = te.xCoord;
		int yy = te.yCoord;
		int zz = te.zCoord;

		drawCuboid((TileEntityEnergyBankBase) te, 0f, 1f, (byte) 0);
		drawCuboid((TileEntityEnergyBankBase) te, 1f / 48f, 1f - 1f / 48f, (byte) 1);
		drawCuboid((TileEntityEnergyBankBase) te, 2f / 48f, 1f - 2f / 48f, (byte) 2);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
	}

	/**
	 * Method used for drawing energy cell bank into world.<br>
	 * NOTE: This method resembles
	 * {@link com.hockeyhurd.api.util.TessellatorHelper#drawCuboid(float, float, float, double, com.hockeyhurd.api.renderer.Color4i)}
	 * 
	 * @param te = te to draw.
	 * @param min = min size.
	 * @param max = max size.
	 */
	protected void drawCuboid(TileEntityEnergyBankBase te, float min, float max, byte layer) {
		drawCuboid(te, new Vector4Helper<Float>(min, min, min), new Vector4Helper<Float>(max, max, max), layer);
	}

	/**
	 * Method used for drawing energy cell bank into world. <br>
	 * <br>
	 * NOTE: This method resembles
	 * {@link com.hockeyhurd.api.util.TessellatorHelper#drawCuboid(float, float, float, double, com.hockeyhurd.api.renderer.Color4i)}
	 * 
	 * @param te = te to draw as reference.
	 * @param minVec = min vec to draw.
	 * @param maxVec = max vec to draw.
	 * @param layer = layer to draw.
	 * @param valve = valve, (blue : -1, grey : 0, orange : 1).
	 */
	protected void drawCuboid(TileEntityEnergyBankBase te, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, byte layer) {
		
		if (te.getWorldObj() != null && te.getWorldObj().getTotalWorldTime() % 20L == 0) {
			te = (TileEntityEnergyBankBase) te.getWorldObj().getTileEntity(te.xCoord, te.yCoord, te.zCoord);
			this.progressBar = (float) ((TileEntityEnergyBankBase) te).getEnergyStored() / ((TileEntityEnergyBankBase) te).getMaxStorage();
		}
		
		int progressIndex = (int) (this.progressBar * 8);
		
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
					max = 32f * this.PIXEL;
					min = 16f * this.PIXEL;
					difU = 32f * this.PIXEL;
					difV = 0f * this.PIXEL;
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
	
	protected void drawZNeg(Tessellator tess, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, float min, float max, float difU, float difV) {
		tess.addVertexWithUV(minVec.x, maxVec.y, maxVec.z, min - difU, min - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, maxVec.z, min - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, maxVec.y, maxVec.z, max - difU, min - difV);
	}
	
	protected void drawZPos(Tessellator tess, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, float min, float max, float difU, float difV) {
		tess.addVertexWithUV(maxVec.x, maxVec.y, minVec.z, min - difU, min - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, minVec.z, min - difU, max - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, minVec.z, max - difU, max - difV);
		tess.addVertexWithUV(minVec.x, maxVec.y, minVec.z, max - difU, min - difV);
	}
	
	protected void drawXNeg(Tessellator tess, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, float min, float max, float difU, float difV) {
		tess.addVertexWithUV(minVec.x, maxVec.y, minVec.z, min - difU, min - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, minVec.z, min - difU, max - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(minVec.x, maxVec.y, maxVec.z, max - difU, min - difV);
	}
	
	protected void drawXPos(Tessellator tess, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, float min, float max, float difU, float difV) {
		tess.addVertexWithUV(maxVec.x, maxVec.y, maxVec.z, max - difU, min - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, minVec.z, min - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, maxVec.y, minVec.z, min - difU, min - difV);
	}
	
	protected void drawYNeg(Tessellator tess, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, float min, float max, float difU, float difV) {
		tess.addVertexWithUV(maxVec.x, minVec.y, minVec.z, max - difU, min - difV);
		tess.addVertexWithUV(maxVec.x, minVec.y, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, maxVec.z, min - difU, max - difV);
		tess.addVertexWithUV(minVec.x, minVec.y, minVec.z, min - difU, min - difV);
	}
	
	protected void drawYPos(Tessellator tess, Vector4Helper<Float> minVec, Vector4Helper<Float> maxVec, float min, float max, float difU, float difV) {
		tess.addVertexWithUV(minVec.x, maxVec.y - 0.01d, minVec.z, min - difU, min - difV);
		tess.addVertexWithUV(minVec.x, maxVec.y - 0.01d, maxVec.z, min - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, maxVec.y - 0.01d, maxVec.z, max - difU, max - difV);
		tess.addVertexWithUV(maxVec.x, maxVec.y - 0.01d, minVec.z, max - difU, min - difV);
	}

}

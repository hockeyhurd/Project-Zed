/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.renderer;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.hockeyhurd.api.math.Vector4;
import com.hockeyhurd.api.util.TessellatorHelper;
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
	private TessellatorHelper tessHelp;

	/**
	 * @param tier
	 */
	public EnergyBankRenderer(byte tier) {
		// this.texture = new ResourceLocation("projectzed", "textures/blocks/energyCellTier" + tier + ".png");
		this.texture = new ResourceLocation("projectzed", "textures/blocks/energyCellGeneric.png");
		tessHelp = new TessellatorHelper(null);
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
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0 % 65536, 0xf0 / 65536);
		this.bindTexture(texture);

		drawCuboid((TileEntityEnergyBankBase) te, 0f, 1f, (byte) 0);
		drawCuboid((TileEntityEnergyBankBase) te, 1f / 48f, 1f - 1f / 48f, (byte) 1);
		drawCuboid((TileEntityEnergyBankBase) te, 2f / 48f, 1f - 2f / 48f, (byte) 2);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
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
		drawCuboid(te, new Vector4<Float>(min, min, min), new Vector4<Float>(max, max, max), layer);
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
	protected void drawCuboid(TileEntityEnergyBankBase te, Vector4<Float> minVec, Vector4<Float> maxVec, byte layer) {
		
		if (te.getWorldObj() != null && te.getWorldObj().getTotalWorldTime() % 20L == 0) {
			te = (TileEntityEnergyBankBase) te.getWorldObj().getTileEntity(te.xCoord, te.yCoord, te.zCoord);
		}
		
		Tessellator tess = tessHelp.tess;
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
				
				if ((int)((float) te.getEnergyStored() / ((TileEntityEnergyBankBase) te).getMaxStorage() * 8f) == 0) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 32f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
				
				else if ((int)((float) te.getEnergyStored() / ((TileEntityEnergyBankBase) te).getMaxStorage() * 8f) == 1) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 16f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
				
				else if ((int)((float) te.getEnergyStored() / ((TileEntityEnergyBankBase) te).getMaxStorage() * 8f) == 2) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 0f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
				
				else if ((int)((float) te.getEnergyStored() / ((TileEntityEnergyBankBase) te).getMaxStorage() * 8f) == 3) {
					max = 64f * this.PIXEL;
					min = 48f * this.PIXEL;
					difU = 48f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
				
				else if ((int)((float) te.getEnergyStored() / ((TileEntityEnergyBankBase) te).getMaxStorage() * 8f) == 4) {
					max = 64f * this.PIXEL;
					min = 48f * this.PIXEL;
					difU = 32f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
				
				else if ((int)((float) te.getEnergyStored() / ((TileEntityEnergyBankBase) te).getMaxStorage() * 8f) == 5) {
					max = 64f * this.PIXEL;
					min = 48f * this.PIXEL;
					difU = 16f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}

				else if ((int)((float) te.getEnergyStored() / ((TileEntityEnergyBankBase) te).getMaxStorage() * 8f) == 6) {
					max = 80f * this.PIXEL;
					min = 64f * this.PIXEL;
					difU = 64f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
				
				else if ((int)((float) te.getEnergyStored() / ((TileEntityEnergyBankBase) te).getMaxStorage() * 8f) == 7) {
					max = 80f * this.PIXEL;
					min = 64f * this.PIXEL;
					difU = 48f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
				
				else if ((int)((float) te.getEnergyStored() / ((TileEntityEnergyBankBase) te).getMaxStorage() * 8f) == 8) {
					max = 80f * this.PIXEL;
					min = 64f * this.PIXEL;
					difU = 32f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
			}
	
			// -zz
			if (counter == 3) tessHelp.drawZNeg(minVec, maxVec, min, max, difU, difV, false);
	
			// +zz
			if (counter == 2) tessHelp.drawZPos(minVec, maxVec, min, max, difU, difV, false);
	
			// -xx
			if (counter == 4) tessHelp.drawXNeg(minVec, maxVec, min, max, difU, difV, false);
	
			// +xx
			if (counter == 5) tessHelp.drawXPos(minVec, maxVec, min, max, difU, difV, false);
	
			// +yy
			if (counter == 1) tessHelp.drawYPos(minVec, maxVec, min, max, difU, difV, false);
	
			// -yy
			if (counter == 0) tessHelp.drawYNeg(minVec, maxVec, min, max, difU, difV, false);
			
			counter++;
		}

		tess.draw();
	}

}

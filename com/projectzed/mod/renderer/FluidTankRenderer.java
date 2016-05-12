/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.renderer;

import com.hockeyhurd.api.math.Vector3;
import com.hockeyhurd.api.util.TessellatorHelper;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

/**
 * Class handling all client side rendering code for fluid tanks.
 * 
 * @author hockeyhurd
 * @version Jan 22, 2015
 */
@SideOnly(Side.CLIENT)
public class FluidTankRenderer extends TileEntitySpecialRenderer {

	private ResourceLocation texture;
	private final float PIXEL = 1f / 48f;
	private byte tier = 0;
	private boolean renderInside = true;

	private Vector3<Float> minVec, maxVec;
	private Vector3<Float> fluidMinVec, fluidMaxVec;
	private TessellatorHelper tessHelp;

	/**
	 * @param tier
	 */
	public FluidTankRenderer(byte tier) {
		this.texture = new ResourceLocation("projectzed", "textures/blocks/fluidTankTier" + tier + ".png");
		this.tier = tier;

		minVec = new Vector3<Float>(48f / 4f * this.PIXEL, 0f + 0.001f, 48f / 4f * this.PIXEL);
		maxVec = new Vector3<Float>(1f - 48f / 4f * this.PIXEL, 1f /*- 48f / 8f * this.PIXEL*/, 1f - 48f / 4f * this.PIXEL);
		
		fluidMinVec = new Vector3<Float>(5f / 16f, 2f / 16f, 5f / 16f);
		fluidMaxVec = new Vector3<Float>(1f - 5f / 16f, 1f -  1f / 16f, 1f - 5f / 16f);
		
		tessHelp = new TessellatorHelper(null);
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
		drawCuboid(te, new Vector3<Float>(min, min, min), new Vector3<Float>(max, max, max), layer);
	}

	protected void drawCuboid(TileEntityFluidTankBase te, Vector3<Float> minVec, Vector3<Float> maxVec, byte layer) {

		if (te.getWorldObj() != null && te.getWorldObj().getTotalWorldTime() % 20L == 0) {
			te = (TileEntityFluidTankBase) te.getWorldObj().getTileEntity(te.xCoord, te.yCoord, te.zCoord);
			tier = te.getTier();
		}

		tessHelp.startDrawingQuads();

		int counter = 0;
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

			// -zz
			if (counter == 2) tessHelp.drawZNeg(minVec, maxVec, min, max, difU, difV, true);

			// +zz
			if (counter == 3) tessHelp.drawZPos(minVec, maxVec, min, max, difU, difV, true);

			// -xx
			if (counter == 4) tessHelp.drawXNeg(minVec, maxVec, min, max, difU, difV, true);

			// +xx
			if (counter == 5) tessHelp.drawXPos(minVec, maxVec, min, max, difU, difV, true);

			// +yy
			if (counter == 1 && layer == 0) {
				
				if (valve == -1) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 32f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
				
				else if (valve == 0) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 16f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
				
				else if (valve == 1) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 0f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
				
				tessHelp.drawYPos(minVec, maxVec, min, max, difU, difV, true);
			}

			// -yy
			if (counter == 0 && layer == 0) {
				
				if (valve == -1) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 32f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
				
				else if (valve == 0) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 16f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
				
				else if (valve == 1) {
					max = 48f * this.PIXEL;
					min = 32f * this.PIXEL;
					difU = 0f * this.PIXEL;
					difV = 0f * this.PIXEL;
				}
				
				tessHelp.drawYNeg(minVec, maxVec, min, max, difU, difV, true);
			}

			counter++;
		}

		tessHelp.draw();
	}

	/**
	 * Method to handler rendering of fluids inside of the tank.
	 *
	 * @param te tilentity object to reference.
	 * @param vec0 min vector.
	 * @param vec1 max vector.
	 */
	protected void drawFluid(TileEntityFluidTankBase te, Vector3<Float> vec0, Vector3<Float> vec1) {
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
			// System.out.println("null returning!");
			return;
		}

		final Vector3<Float> maxVecY = vec1.copy();
		
		// vec1.y = (3f + this.progressIndex) / 16f;
		vec1.y = (3f + ((int) (te.getTank().getFluidAmount() / (float) (te.getTank().getCapacity()) * 10))) / 16f;
		
		this.bindTexture(TextureMap.locationBlocksTexture);
		Tessellator tess = tessHelp.tess;
		tess.startDrawingQuads();
		
		tessHelp.drawZNeg(icon, vec0, vec1, false);
		tessHelp.drawZPos(icon, vec0, vec1, false);
		tessHelp.drawXNeg(icon, vec0, vec1, false);
		tessHelp.drawXPos(icon, vec0, vec1, false);
		
		tessHelp.drawYNeg(icon, vec0, maxVecY, false);
		tessHelp.drawYPos(icon, vec0, maxVecY, false);
		
		tess.draw();
		
	}

}

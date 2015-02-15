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
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

import org.lwjgl.opengl.GL11;

import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductBase;
import com.projectzed.mod.util.Connection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing rendering code for fluid pipes.
 * 
 * @author hockeyhurd
 * @version Feb 13, 2015
 */
@SideOnly(Side.CLIENT)
public class FluidPipeRenderer extends TileEntitySpecialRenderer {

	private ResourceLocation texture;
	private EnumColor color;
	private final float PIXEL = 1f / 16f;
	private final float TEXTURE_PIXEL = 1f / 32f;
	private boolean renderInside = false;

	private float calc = 11 * PIXEL / 2;
	private float calc2 = 9 * PIXEL / 2;
	private int minU = 11;
	private int maxU = minU + 3;
	private int minV = 0;
	private int maxV = minV + 5;
	
	/**
	 * @param color = color to draw.
	 */
	public FluidPipeRenderer(EnumColor color) {
		super();
		this.color = color;
		this.renderInside = color == EnumColor.CLEAR;
		texture = new ResourceLocation("projectzed", "textures/blocks/pipe_fluid_" + color.getColorAsString() + ".png");
	}

	/* (non-Javadoc)	
	 * @see net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer#renderTileEntityAt(net.minecraft.tileentity.TileEntity, double, double, double, float)
	 */
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
		this.bindTexture(texture);

		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glDisable(GL11.GL_LIGHTING);

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0 % 65536, 0xf0 / 65536);

		int xx = te.xCoord;
		int yy = te.yCoord;
		int zz = te.zCoord;

		Connection xLeft = canConnect(te.getWorldObj(), te, 4, xx - 1, yy, zz); // west
		Connection xRight = canConnect(te.getWorldObj(), te, 5, xx + 1, yy, zz); // east

		Connection yBottom = canConnect(te.getWorldObj(), te, 0, xx, yy - 1, zz);
		Connection yTop = canConnect(te.getWorldObj(), te, 1, xx, yy + 1, zz);

		Connection zLeft = canConnect(te.getWorldObj(), te, 2, xx, yy, zz - 1); // north
		Connection zRight = canConnect(te.getWorldObj(), te, 3, xx, yy, zz + 1); // sound

		// ProjectZed.logHelper.info(xLeft.isConnected());
		
		drawPipe(te, xLeft.isConnected(), xRight.isConnected(), yBottom.isConnected(), yTop.isConnected(), zLeft.isConnected(), zRight.isConnected());

		if (xLeft.isConnected()) drawConnection(ForgeDirection.WEST, xLeft.getType());
		if (xRight.isConnected()) drawConnection(ForgeDirection.EAST, xRight.getType());
		if (yTop.isConnected()) drawConnection(ForgeDirection.UP, yTop.getType());
		if (yBottom.isConnected()) drawConnection(ForgeDirection.DOWN, yBottom.getType());

		if (zLeft.isConnected()) drawConnection(ForgeDirection.NORTH, zLeft.getType());
		if (zRight.isConnected()) drawConnection(ForgeDirection.SOUTH, zRight.getType());

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
		GL11.glPopMatrix();
	}
	
	/**
	 * Determines whether the given pipe can connect to neighboring te.
	 * 
	 * @param world = world object as reference.
	 * @param te = te object as reference.
	 * @param x = position x.
	 * @param y = position y.
	 * @param z = position z.
	 * @return true if can connect, else returns false.
	 */
	private Connection canConnect(World world, TileEntity te, int index, int x, int y, int z) {
		boolean flag = false;
		int type = 0;
		
		if (world.getTileEntity(x, y, z) instanceof IFluidHandler) {
			IFluidHandler cont = (IFluidHandler) world.getTileEntity(x, y, z);
			
			if (cont != null && cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(ForgeDirection.getOrientation(index).getOpposite()) != 0) {
					flag = true;
					type = 2;
				}
				
				return new Connection(flag, type);
			}
			
			else if (cont != null && cont instanceof TileEntityLiquiductBase) {
				
				if (((TileEntityLiquiductBase) cont).getColor() == this.color) {
					flag = true;
					type = 1;
				}
				
				return new Connection(flag, type);
			}
			
			flag = true;
			type = 2;
		}
		
		return new Connection(flag, type);
	}
	
	/**
	 * Method to draw connection from pipe to machine.
	 * 
	 * @param tess = Tessellator object.
	 */
	private void drawConnector(Tessellator tess) {
		// -z
		tess.addVertexWithUV(1 - calc2, 1 - calc2, 1 - calc2, minU * TEXTURE_PIXEL, maxV * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc2, 1, 1 - calc2, maxU * TEXTURE_PIXEL, maxV * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc2, 1, 1 - calc2, maxU * TEXTURE_PIXEL, minV * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc2, 1 - calc2, 1 - calc2, minU * TEXTURE_PIXEL, minV * TEXTURE_PIXEL);

		// +z
		tess.addVertexWithUV(calc2, 1 - calc2, calc2, minU * TEXTURE_PIXEL, maxV * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc2, 1, calc2, maxU * TEXTURE_PIXEL, maxV * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc2, 1, calc2, maxU * TEXTURE_PIXEL, minV * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc2, 1 - calc2, calc2, minU * TEXTURE_PIXEL, minV * TEXTURE_PIXEL);

		// -x
		tess.addVertexWithUV(calc2, 1 - calc2, 1 - calc2, minU * TEXTURE_PIXEL, maxV * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc2, 1, 1 - calc2, maxU * TEXTURE_PIXEL, maxV * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc2, 1, calc2, maxU * TEXTURE_PIXEL, minV * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc2, 1 - calc2, calc2, minU * TEXTURE_PIXEL, minV * TEXTURE_PIXEL);

		// +x
		tess.addVertexWithUV(1 - calc2, 1 - calc2, calc2, minU * TEXTURE_PIXEL, maxV * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc2, 1, calc2, maxU * TEXTURE_PIXEL, maxV * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc2, 1, 1 - calc2, maxU * TEXTURE_PIXEL, minV * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc2, 1 - calc2, 1 - calc2, minU * TEXTURE_PIXEL, minV * TEXTURE_PIXEL);

		// -y
		tess.addVertexWithUV(1 - calc2, 1 - calc2, 1 - calc2, maxU * TEXTURE_PIXEL, maxV * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc2, 1 - calc2, 1 - calc2, maxU * TEXTURE_PIXEL, minV * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc2, 1 - calc2, calc2, minU * TEXTURE_PIXEL, minV * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc2, 1 - calc2, calc2, minU * TEXTURE_PIXEL, maxV * TEXTURE_PIXEL);
	}

	/**
	 * Method used to draw connection to neighboring te if applicable.
	 * 
	 * @param dir = direction to draw to.
	 */
	private void drawConnection(ForgeDirection dir, int type) {

		Tessellator tess = Tessellator.instance;

		tess.startDrawingQuads();

		GL11.glTranslatef(0.5f, 0.5f, 0.5f);
		if (dir.equals(ForgeDirection.UP)) {
		}
		else if (dir.equals(ForgeDirection.DOWN)) GL11.glRotatef(180, 1, 0, 0);
		else if (dir.equals(ForgeDirection.SOUTH)) GL11.glRotatef(90, 1, 0, 0);
		else if (dir.equals(ForgeDirection.NORTH)) GL11.glRotatef(270, 1, 0, 0);
		else if (dir.equals(ForgeDirection.WEST)) GL11.glRotatef(90, 0, 0, 1);
		else if (dir.equals(ForgeDirection.EAST)) GL11.glRotatef(270, 0, 0, 1);

		GL11.glTranslatef(-0.5f, -0.5f, -0.5f);

		// If connected to machine, draw connector.
		if (type == 2) drawConnector(tess);

		// -z
		tess.addVertexWithUV(1 - calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc, 1, 1 - calc, 10 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc, 1, 1 - calc, 10 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);

		if (renderInside) {
			tess.addVertexWithUV(calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, 1, 1 - calc, 10 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, 1, 1 - calc, 10 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		}

		// +z
		tess.addVertexWithUV(calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc, 1, calc, 10 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc, 1, calc, 10 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);

		if (renderInside) {
			tess.addVertexWithUV(1 - calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, 1, calc, 10 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, 1, calc, 10 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		}

		// -x
		tess.addVertexWithUV(calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc, 1, 1 - calc, 10 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc, 1, calc, 10 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);

		if (renderInside) {
			tess.addVertexWithUV(calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, 1, calc, 10 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, 1, 1 - calc, 10 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		}

		// +x
		tess.addVertexWithUV(1 - calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc, 1, calc, 10 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc, 1, 1 - calc, 10 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);

		if (renderInside) {
			tess.addVertexWithUV(1 - calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, 1, 1 - calc, 10 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, 1, calc, 10 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		}

		tess.draw();

		GL11.glTranslatef(0.5f, 0.5f, 0.5f);
		if (dir.equals(ForgeDirection.UP)) {
		}

		else if (dir.equals(ForgeDirection.DOWN)) GL11.glRotatef(-180, 1, 0, 0);
		else if (dir.equals(ForgeDirection.SOUTH)) GL11.glRotatef(-90, 1, 0, 0);
		else if (dir.equals(ForgeDirection.NORTH)) GL11.glRotatef(-270, 1, 0, 0);
		else if (dir.equals(ForgeDirection.WEST)) GL11.glRotatef(-90, 0, 0, 1);
		else if (dir.equals(ForgeDirection.EAST)) GL11.glRotatef(-270, 0, 0, 1);

		GL11.glTranslatef(-0.5f, -0.5f, -0.5f);

	}

	/**
	 * Method used to draw pipe generically.
	 * 
	 * @param te = te object as reference.
	 * @param xLeft = flag for x position-left.
	 * @param xRight = flag for x position-right.
	 * @param yBottom = flag for y position-botton.
	 * @param yTop = flag for y position-top.
	 * @param zLeft = flag for z position-left.
	 * @param zRight = flag for z position-right.
	 */
	private void drawPipe(TileEntity te, boolean xLeft, boolean xRight, boolean yBottom, boolean yTop, boolean zLeft, boolean zRight) {
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();

		if (!zRight) {
			// -Z
			tess.addVertexWithUV(1 - calc, calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, 1 - calc, 1 - calc, 0 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, calc, 1 - calc, 0 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);

			if (renderInside) {
				tess.addVertexWithUV(calc, calc, 1 - calc, 0 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
				tess.addVertexWithUV(calc, 1 - calc, 1 - calc, 0 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
				tess.addVertexWithUV(1 - calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
				tess.addVertexWithUV(1 - calc, calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			}
		}

		if (!zLeft) {
			// +z
			tess.addVertexWithUV(calc, calc, calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, 1 - calc, calc, 0 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, calc, calc, 0 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);

			if (renderInside) {
				tess.addVertexWithUV(1 - calc, calc, calc, 0 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
				tess.addVertexWithUV(1 - calc, 1 - calc, calc, 0 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
				tess.addVertexWithUV(calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
				tess.addVertexWithUV(calc, calc, calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			}
		}

		if (!xLeft) {
			// -x
			tess.addVertexWithUV(calc, calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, 1 - calc, calc, 0 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, calc, calc, 0 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);

			if (renderInside) {
				tess.addVertexWithUV(calc, calc, calc, 0 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
				tess.addVertexWithUV(calc, 1 - calc, calc, 0 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
				tess.addVertexWithUV(calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
				tess.addVertexWithUV(calc, calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			}
		}

		if (!xRight) {
			// +x
			tess.addVertexWithUV(1 - calc, calc, calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, 1 - calc, 1 - calc, 0 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, calc, 1 - calc, 0 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);

			if (renderInside) {
				tess.addVertexWithUV(1 - calc, calc, 1 - calc, 0 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
				tess.addVertexWithUV(1 - calc, 1 - calc, 1 - calc, 0 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
				tess.addVertexWithUV(1 - calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
				tess.addVertexWithUV(1 - calc, calc, calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			}
		}

		if (!yBottom) {
			// -y
			tess.addVertexWithUV(1 - calc, calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, calc, 1 - calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, calc, calc, 0 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, calc, calc, 0 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);

			if (renderInside) {
				tess.addVertexWithUV(1 - calc, calc, calc, 0 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
				tess.addVertexWithUV(calc, calc, calc, 0 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
				tess.addVertexWithUV(calc, calc, 1 - calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
				tess.addVertexWithUV(1 - calc, calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			}
		}

		if (!yTop) {
			// +y
			tess.addVertexWithUV(1 - calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			tess.addVertexWithUV(1 - calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, 1 - calc, calc, 0 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
			tess.addVertexWithUV(calc, 1 - calc, 1 - calc, 0 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);

			if (renderInside) {
				tess.addVertexWithUV(calc, 1 - calc, 1 - calc, 0 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
				tess.addVertexWithUV(calc, 1 - calc, calc, 0 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
				tess.addVertexWithUV(1 - calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
				tess.addVertexWithUV(1 - calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
			}
		}

		tess.draw();
	}

}
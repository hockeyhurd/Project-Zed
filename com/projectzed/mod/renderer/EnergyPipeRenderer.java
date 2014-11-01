package com.projectzed.mod.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.projectzed.api.storage.IEnergyContainer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class handling rendering information for generic energy pipe.
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
@SideOnly(Side.CLIENT)
public class EnergyPipeRenderer extends TileEntitySpecialRenderer {

	private ResourceLocation texture = new ResourceLocation("projectzed", "textures/blocks/pipe_energy_red.png");
	private final float PIXEL = 1f / 16f;
	private final float TEXTURE_PIXEL = 1f / 32f;
	private boolean renderInside = false;

	private float calc = 11 * PIXEL / 2;

	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
		GL11.glTranslated(x, y, z);
		GL11.glDisable(GL11.GL_LIGHTING);
		this.bindTexture(texture);

		int xx = te.xCoord;
		int yy = te.yCoord;
		int zz = te.zCoord;

		boolean xLeft = canConnect(te.getWorldObj(), te, xx - 1, yy, zz); 
		boolean xRight = canConnect(te.getWorldObj(), te, xx + 1, yy, zz);

		boolean yBottom = canConnect(te.getWorldObj(), te, xx, yy - 1, zz);
		boolean yTop = canConnect(te.getWorldObj(), te, xx, yy + 1, zz);

		boolean zLeft = canConnect(te.getWorldObj(), te, xx, yy, zz - 1);
		boolean zRight = canConnect(te.getWorldObj(), te, xx, yy, zz + 1);

		drawPipe(te, xLeft, xRight, yBottom, yTop, zLeft, zRight);

		if (xLeft) drawConnection(ForgeDirection.WEST);
		if (xRight) drawConnection(ForgeDirection.EAST);
		if (yTop) drawConnection(ForgeDirection.UP);
		if (yBottom) drawConnection(ForgeDirection.DOWN);

		if (zLeft) drawConnection(ForgeDirection.NORTH);
		if (zRight) drawConnection(ForgeDirection.SOUTH);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
	}

	private boolean canConnect(World world, TileEntity te, int x, int y, int z) {
		return world.getTileEntity(x, y, z) instanceof IEnergyContainer;
	}

	private void drawConnection(ForgeDirection dir) {

		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();

		GL11.glTranslatef(0.5f, 0.5f, 0.5f);
		if (dir.equals(ForgeDirection.UP)) {}
		else if (dir.equals(ForgeDirection.DOWN)) GL11.glRotatef(180, 1, 0, 0);
		else if (dir.equals(ForgeDirection.SOUTH)) GL11.glRotatef(90, 1, 0, 0);
		else if (dir.equals(ForgeDirection.NORTH)) GL11.glRotatef(270, 1, 0, 0);
		else if (dir.equals(ForgeDirection.WEST)) GL11.glRotatef(90, 0, 0, 1);
		else if (dir.equals(ForgeDirection.EAST)) GL11.glRotatef(270, 0, 0, 1);

		GL11.glTranslatef(-0.5f, -0.5f, -0.5f);

		// -z
		tess.addVertexWithUV(1 - calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc, 1, 1 - calc, 10 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc, 1, 1 - calc, 10 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);

		// +z
		tess.addVertexWithUV(calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc, 1, calc, 10 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc, 1, calc, 10 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);

		// -x
		tess.addVertexWithUV(calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc, 1, 1 - calc, 10 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc, 1, calc, 10 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
		tess.addVertexWithUV(calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
		
		// +x
		tess.addVertexWithUV(1 - calc, 1 - calc, calc, 5 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc, 1, calc, 10 * TEXTURE_PIXEL, 5 * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc, 1, 1 - calc, 10 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);
		tess.addVertexWithUV(1 - calc, 1 - calc, 1 - calc, 5 * TEXTURE_PIXEL, 0 * TEXTURE_PIXEL);

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

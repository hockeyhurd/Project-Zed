package com.projectzed.mod.renderer;

import com.hockeyhurd.hcorelib.api.client.util.RenderHelper;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductBase;
import com.projectzed.mod.util.Connection;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * @author hockeyhurd
 * @version 6/20/2016.
 */
@SideOnly(Side.CLIENT)
public class FluidPipeRenderer extends TileEntitySpecialRenderer<TileEntityLiquiductBase> {

	private final ResourceLocation texture;
	private final EnumColor color;

	private static final float PIXEL = 1.0f / 16.0f;
	private static final float TEXTURE_PIXEL = 1.0f / 32.0f;
	private final boolean renderInside;

	private static float calc = 11 * PIXEL / 2;
	private static float calc2 = 9 * PIXEL / 2;
	private static float oneMinusCalc = 1.0f - calc;
	private static float oneMinusCalc2 = 1.0f - calc2;
	private static int connectorMinU = 11;
	private static int connectorMaxU = connectorMinU + 3;
	private static int connectorMinV = 0;
	private static int connectorMaxV = connectorMinV + 7;
	private static int connectionMinU = 5;
	private static int connectionMaxU = 10;
	private static int connectionMinV = 0;
	private static int connectionMaxV = 7;
	private static int drawMinU = 0;
	private static int drawMaxU = 5;
	private static int drawMinV = 0;
	private static int drawMaxV = 7;

	public FluidPipeRenderer(EnumColor color) {
		this.color = color;

		renderInside = color == EnumColor.CLEAR;
		texture = new ResourceLocation(ProjectZed.assetDir, "textures/blocks/pipe_fluid_" + color.getColorAsString() + ".png");
	}

	@Override
	public void renderTileEntityAt(TileEntityLiquiductBase te, double x, double y, double z, float partialTicks, int destroyStage) {
		final Vector3<Double> renderVec = new Vector3<Double>(x, y, z);
		final Vector3<Integer> atVec = VectorHelper.toVector3i(te.getPos());

		RenderHelper.setupPreRender(texture, renderVec);

		Connection xLeft = canConnect(te.getWorld(), te, 4, atVec.x - 1, atVec.y, atVec.z); // west
		Connection xRight = canConnect(te.getWorld(), te, 5, atVec.x + 1, atVec.y, atVec.z); // east

		Connection yBottom = canConnect(te.getWorld(), te, 0, atVec.x, atVec.y - 1, atVec.z);
		Connection yTop = canConnect(te.getWorld(), te, 1, atVec.x, atVec.y + 1, atVec.z);

		Connection zLeft = canConnect(te.getWorld(), te, 2, atVec.x, atVec.y, atVec.z - 1); // north
		Connection zRight = canConnect(te.getWorld(), te, 3, atVec.x, atVec.y, atVec.z + 1); // sound

		drawPipe(te, renderInside, xLeft.isConnected(), xRight.isConnected(), yBottom.isConnected(), yTop.isConnected(), zLeft.isConnected(), zRight.isConnected());

		if (xLeft.isConnected()) drawConnection(EnumFacing.WEST, xLeft.getType(), renderInside);
		if (xRight.isConnected()) drawConnection(EnumFacing.EAST, xRight.getType(), renderInside);
		if (yTop.isConnected()) drawConnection(EnumFacing.UP, yTop.getType(), renderInside);
		if (yBottom.isConnected()) drawConnection(EnumFacing.DOWN, yBottom.getType(), renderInside);

		if (zLeft.isConnected()) drawConnection(EnumFacing.NORTH, zLeft.getType(), renderInside);
		if (zRight.isConnected()) drawConnection(EnumFacing.SOUTH, zRight.getType(), renderInside);

		RenderHelper.finishPostRender(renderVec);
	}

	/**
	 * Determines whether the given pipe can connect to neighboring te.
	 *
	 * @param world world object as reference.
	 * @param te te object as reference.
	 * @param x position x.
	 * @param y position y.
	 * @param z position z.
	 * @return true if can connect, else returns false.
	 */
	private Connection canConnect(World world, TileEntity te, int index, int x, int y, int z) {
		boolean flag = false;
		int type = 0;

		final BlockPos blockPos = VectorHelper.toBlockPos(x, y, z);
		final TileEntity tileAt = world.getTileEntity(blockPos);
		if (tileAt instanceof IFluidHandler) {
			IFluidHandler cont = (IFluidHandler) tileAt;

			if (cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(EnumFacing.getFront(index).getOpposite()) != 0) {
					flag = true;
					type = 2;
				}

				return new Connection(flag, type);
			}

			else if (cont instanceof TileEntityLiquiductBase) {

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

	private static void drawConnector() {
		// -z
		RenderHelper.addVertUV(oneMinusCalc2, oneMinusCalc2, oneMinusCalc2, connectorMinU * TEXTURE_PIXEL, connectorMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(oneMinusCalc2, 1.0f, oneMinusCalc2, connectorMaxU * TEXTURE_PIXEL, connectorMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc2, 1.0f, oneMinusCalc2, connectorMaxU * TEXTURE_PIXEL, connectorMinV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc2, oneMinusCalc2, oneMinusCalc2, connectorMinU * TEXTURE_PIXEL, connectorMinV * TEXTURE_PIXEL);

		// +z
		RenderHelper.addVertUV(calc2, oneMinusCalc2, calc2, connectorMinU * TEXTURE_PIXEL, connectorMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc2, 1.0f, calc2, connectorMaxU * TEXTURE_PIXEL, connectorMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(oneMinusCalc2, 1.0f, calc2, connectorMaxU * TEXTURE_PIXEL, connectorMinV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(oneMinusCalc2, oneMinusCalc2, calc2, connectorMinU * TEXTURE_PIXEL, connectorMinV * TEXTURE_PIXEL);

		// -x
		RenderHelper.addVertUV(calc2, oneMinusCalc2, oneMinusCalc2, connectorMinU * TEXTURE_PIXEL, connectorMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc2, 1.0f, oneMinusCalc2, connectorMaxU * TEXTURE_PIXEL, connectorMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc2, 1.0f, calc2, connectorMaxU * TEXTURE_PIXEL, connectorMinV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc2, oneMinusCalc2, calc2, connectorMinU * TEXTURE_PIXEL, connectorMinV * TEXTURE_PIXEL);

		// +x
		RenderHelper.addVertUV(oneMinusCalc2, oneMinusCalc2, calc2, connectorMinU * TEXTURE_PIXEL, connectorMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(oneMinusCalc2, 1.0f, calc2, connectorMaxU * TEXTURE_PIXEL, connectorMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(oneMinusCalc2, 1.0f, oneMinusCalc2, connectorMaxU * TEXTURE_PIXEL, connectorMinV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(oneMinusCalc2, oneMinusCalc2, oneMinusCalc2, connectorMinU * TEXTURE_PIXEL, connectorMinV * TEXTURE_PIXEL);

		// -y
		RenderHelper.addVertUV(oneMinusCalc2, oneMinusCalc2, oneMinusCalc2, connectorMaxU * TEXTURE_PIXEL, connectorMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc2, oneMinusCalc2, oneMinusCalc2, connectorMaxU * TEXTURE_PIXEL, connectorMinV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc2, oneMinusCalc2, calc2, connectorMinU * TEXTURE_PIXEL, connectorMinV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(oneMinusCalc2, oneMinusCalc2, calc2, connectorMinU * TEXTURE_PIXEL, connectorMaxV * TEXTURE_PIXEL);
	}

	private static void drawConnection(EnumFacing dir, int type, boolean renderInside) {
		RenderHelper.startDrawingQuads();

		GL11.glTranslatef(0.5f, 0.5f, 0.5f);

		if (dir == EnumFacing.UP) {}
		else if (dir == EnumFacing.DOWN) GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
		else if (dir == EnumFacing.SOUTH) GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		else if (dir == EnumFacing.NORTH) GL11.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
		else if (dir == EnumFacing.WEST) GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
		else if (dir == EnumFacing.EAST) GL11.glRotatef(270.0f, 0.0f, 0.0f, 1.0f);

		GL11.glTranslatef(-0.5f, -0.5f, -0.5f);

		if (type == 2) drawConnector();

		// -z
		RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, oneMinusCalc, connectionMinU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(oneMinusCalc, 1.0f, oneMinusCalc, connectionMaxU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc, 1.0f, oneMinusCalc, connectionMaxU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc, oneMinusCalc, oneMinusCalc, connectionMinU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);

		if (renderInside) {
			RenderHelper.addVertUV(calc, oneMinusCalc, oneMinusCalc, connectionMinU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, 1.0f, oneMinusCalc, connectionMaxU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, 1.0f, oneMinusCalc, connectionMaxU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, oneMinusCalc, connectionMinU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
		}

		// +z
		RenderHelper.addVertUV(calc, oneMinusCalc, calc, connectionMinU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc, 1.0f, calc, connectionMaxU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(oneMinusCalc, 1.0f, calc, connectionMaxU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, calc, connectionMinU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);

		if (renderInside) {
			RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, calc, connectionMinU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, 1.0f, calc, connectionMaxU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, 1.0f, calc, connectionMaxU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, oneMinusCalc, calc, connectionMinU * TEXTURE_PIXEL, connectionMaxV* TEXTURE_PIXEL);
		}

		// -x
		RenderHelper.addVertUV(calc, oneMinusCalc, oneMinusCalc, connectionMinU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc, 1.0f, oneMinusCalc, connectionMaxU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc, 1.0f, calc, connectionMaxU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(calc, oneMinusCalc, calc, connectionMinU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);

		if (renderInside) {
			RenderHelper.addVertUV(calc, oneMinusCalc, calc, connectionMinU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, 1.0f, calc, connectionMaxU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, 1.0f, oneMinusCalc, connectionMaxU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, oneMinusCalc, oneMinusCalc, connectionMinU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
		}

		// +x
		RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, calc, connectionMinU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(oneMinusCalc, 1.0f, calc, connectionMaxU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(oneMinusCalc, 1.0f, oneMinusCalc, connectionMaxU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);
		RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, oneMinusCalc, connectionMinU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);

		if (renderInside) {
			RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, oneMinusCalc, connectionMinU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, 1.0f, oneMinusCalc, connectionMaxU * TEXTURE_PIXEL, connectionMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, 1.0f, calc, connectionMaxU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, calc, connectionMinU * TEXTURE_PIXEL, connectionMaxV * TEXTURE_PIXEL);
		}

		RenderHelper.draw();

		GL11.glTranslatef(0.5f, 0.5f, 0.5f);
		if (dir == EnumFacing.UP) {
		}

		else if (dir == EnumFacing.DOWN) GL11.glRotatef(-180, 1, 0, 0);
		else if (dir == EnumFacing.SOUTH) GL11.glRotatef(-90, 1, 0, 0);
		else if (dir == EnumFacing.NORTH) GL11.glRotatef(-270, 1, 0, 0);
		else if (dir == EnumFacing.WEST) GL11.glRotatef(-90, 0, 0, 1);
		else if (dir == EnumFacing.EAST) GL11.glRotatef(-270, 0, 0, 1);

		GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
	}

	private static void drawPipe(TileEntity te, boolean renderInside, boolean xLeft, boolean xRight, boolean yBottom, boolean yTop, boolean zLeft, boolean zRight) {

		RenderHelper.startDrawingQuads();

		if (!zRight) {
			// -Z
			RenderHelper.addVertUV(oneMinusCalc, calc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, oneMinusCalc, oneMinusCalc, drawMinU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, calc, oneMinusCalc, drawMinU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);

			if (renderInside) {
				RenderHelper.addVertUV(calc, calc, oneMinusCalc, drawMinU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(calc, oneMinusCalc, oneMinusCalc, drawMinU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(oneMinusCalc, calc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
			}
		}

		if (!zLeft) {
			// +z
			RenderHelper.addVertUV(calc, calc, calc, drawMaxU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, oneMinusCalc, calc, drawMaxU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, calc, drawMinU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, calc, calc, drawMinU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);

			if (renderInside) {
				RenderHelper.addVertUV(oneMinusCalc, calc, calc, drawMinU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, calc, drawMinU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(calc, oneMinusCalc, calc, drawMaxU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(calc, calc, calc, drawMaxU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
			}
		}

		if (!xLeft) {
			// -x
			RenderHelper.addVertUV(calc, calc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, oneMinusCalc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, oneMinusCalc, calc, drawMinU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, calc, calc, drawMinU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);

			if (renderInside) {
				RenderHelper.addVertUV(calc, calc, calc, drawMinU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(calc, oneMinusCalc, calc, drawMinU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(calc, oneMinusCalc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(calc, calc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
			}
		}

		if (!xRight) {
			// +x
			RenderHelper.addVertUV(oneMinusCalc, calc, calc, drawMaxU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, calc, drawMaxU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, oneMinusCalc, drawMinU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, calc, oneMinusCalc, drawMinU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);

			if (renderInside) {
				RenderHelper.addVertUV(oneMinusCalc, calc, oneMinusCalc, drawMinU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, oneMinusCalc, drawMinU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, calc, drawMaxU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(oneMinusCalc, calc, calc, drawMaxU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
			}
		}

		if (!yBottom) {
			// -y
			RenderHelper.addVertUV(oneMinusCalc, calc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, calc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, calc, calc, drawMinU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, calc, calc, drawMinU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);

			if (renderInside) {
				RenderHelper.addVertUV(oneMinusCalc, calc, calc, drawMinU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(calc, calc, calc, drawMinU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(calc, calc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(oneMinusCalc, calc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
			}
		}

		if (!yTop) {
			// +y
			RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, calc, drawMaxU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, oneMinusCalc, calc, drawMinU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
			RenderHelper.addVertUV(calc, oneMinusCalc, oneMinusCalc, drawMinU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);

			if (renderInside) {
				RenderHelper.addVertUV(calc, oneMinusCalc, oneMinusCalc, drawMinU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(calc, oneMinusCalc, calc, drawMinU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, calc, drawMaxU * TEXTURE_PIXEL, drawMinV * TEXTURE_PIXEL);
				RenderHelper.addVertUV(oneMinusCalc, oneMinusCalc, oneMinusCalc, drawMaxU * TEXTURE_PIXEL, drawMaxV * TEXTURE_PIXEL);
			}
		}

		RenderHelper.draw();
	}

}

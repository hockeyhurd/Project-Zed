package com.projectzed.mod.renderer;

import com.hockeyhurd.hcorelib.api.client.util.RenderHelper;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.TileEntityWickedClearGlass;
import com.projectzed.mod.util.Connection;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author hockeyhurd
 * @version 6/22/2016.
 */
@SideOnly(Side.CLIENT)
public class WickedClearGlassRenderer extends TileEntitySpecialRenderer<TileEntityWickedClearGlass> {

	private static final ResourceLocation texture = new ResourceLocation(ProjectZed.assetDir, "textures/blocks/wickedClearGlass.png");
	private static final float PIXEL = 1.0f / 96.0f;
	private static final float min = 0.0f, max = 16.0f * PIXEL;
	private Vector3<Float> minVec, maxVec;
	private Connection[] connections = new Connection[EnumFacing.VALUES.length];

	public WickedClearGlassRenderer() {

		minVec = Vector3.zero.getVector3f();
		maxVec = new Vector3<Float>(1.0f, 1.0f, 1.0f);

		for (int i = 0; i < connections.length; i++)
			connections[i] = new Connection(false, 0);
	}

	@Override
	public void renderTileEntityAt(TileEntityWickedClearGlass te, double x, double y, double z, float partialTicks, int destroyStage) {
		for (EnumFacing dir : EnumFacing.VALUES) {
			connections[dir.ordinal()] = getConnection(te, te.getPos().getX() + dir.getFrontOffsetX(), te.getPos().getY() + dir.getFrontOffsetY(),
				te.getPos().getZ() + dir.getFrontOffsetZ());
		}

		final Vector3<Double> renderVec = new Vector3<Double>(x, y, z);
		final Vector3<Integer> atVec = VectorHelper.toVector3i(te.getPos());

		RenderHelper.setupPreRender(texture, renderVec);

		renderCube(te);

		RenderHelper.finishPostRender(renderVec);
	}

	private static Connection getConnection(TileEntityWickedClearGlass te, int x, int y, int z) {
		Connection connection = null;

		if (te != null && te.getWorld() != null) {
			if (te.getWorld().getTileEntity(VectorHelper.toBlockPos(x, y, z)) instanceof TileEntityWickedClearGlass)
				connection = new Connection(true, 1);
			else connection = new Connection(false, 0);
		}

		return connection;
	}

	private void renderCube(TileEntityWickedClearGlass te) {
		RenderHelper.startDrawingQuads();

		drawXNeg();
		drawXPos();

		drawYNeg();
		drawYPos();

		drawZNeg();
		drawZPos();

		RenderHelper.draw();
	}

	private void drawYNeg() {
		float minU = 0.0f;
		float minV = 64.0f;

		if (connections[EnumFacing.DOWN.ordinal()].isConnected()) return;

		if (!connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.WEST.ordinal()].isConnected()) {
			if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 0.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 16.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 16.0f;
			}

			else {
				minU = 16.0f;
				minV = 48.0f;
			}
		}

		else if (connections[EnumFacing.EAST.ordinal()].isConnected() && !connections[EnumFacing.WEST.ordinal()].isConnected()) {
			if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 0.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 48.0f;
				minV = 16.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 16.0f;
			}

			else {
				minU = 48.0f;
				minV = 48.0f;
			}
		}

		else if (!connections[EnumFacing.EAST.ordinal()].isConnected() && !connections[EnumFacing.WEST.ordinal()].isConnected()) {
			if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 48.0f;
				minV = 0.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 0.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 64.0f;
				minV = 16.0f;
			}
		}

		else if (connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.WEST.ordinal()].isConnected()) {
			if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 48.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 48.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 80.0f;
				minV = 16.0f;
			}

			else {
				minU = 16.0f;
				minV = 64.0f;
			}
		}

		RenderHelper.drawYNeg(minVec, maxVec, minU * PIXEL, minV * PIXEL, (minU + 16.0f) * PIXEL, (minV + 16.0f) * PIXEL);
	}

	private void drawYPos() {
		float minU = 0.0f;
		float minV = 64.0f;

		if (connections[EnumFacing.UP.ordinal()].isConnected()) return;

		if (!connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.WEST.ordinal()].isConnected()) {
			if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 0.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 16.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 16.0f;
			}

			else {
				minU = 16.0f;
				minV = 48.0f;
			}
		}

		else if (connections[EnumFacing.EAST.ordinal()].isConnected() && !connections[EnumFacing.WEST.ordinal()].isConnected()) {
			if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 0.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 48.0f;
				minV = 16.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 16.0f;
			}

			else {
				minU = 48.0f;
				minV = 48.0f;
			}
		}

		else if (!connections[EnumFacing.EAST.ordinal()].isConnected() && !connections[EnumFacing.WEST.ordinal()].isConnected()) {
			if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 48.0f;
				minV = 0.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 0.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 64.0f;
				minV = 16.0f;
			}
		}

		else if (connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.WEST.ordinal()].isConnected()) {
			if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 48.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 48.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.SOUTH.ordinal()].isConnected()) {
				minU = 80.0f;
				minV = 16.0f;
			}

			else {
				minU = 16.0f;
				minV = 64.0f;
			}
		}

		RenderHelper.drawYPos(minVec, maxVec, minU * PIXEL, minV * PIXEL, (minU + 16.0f) * PIXEL, (minV + 16.0f) * PIXEL);
	}

	private void drawZNeg() {
		float minU = 0.0f;
		float minV = 64.0f;

		// ProjectZed.logHelper.info(connections[EnumFacing.WEST.getOpposite().ordinal()].isConnected());

		if (connections[EnumFacing.NORTH.ordinal()].isConnected()) return;

		if (!connections[EnumFacing.DOWN.ordinal()].isConnected() && !connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.EAST.ordinal()].isConnected() && !connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 0.0f;
			}

			else if (!connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 0.0f;
			}

			else if (connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 80.0f;
				minV = 16.0f;
			}
		}

		else if (!connections[EnumFacing.DOWN.ordinal()].isConnected() && connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.EAST.ordinal()].isConnected() && !connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 16.0f;
			}

			else if (!connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 16.0f;
			}

			else if (connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 48.0f;
			}

			else {
				minU = 32.0f;
				minV = 0.0f;
			}
		}

		else if (connections[EnumFacing.DOWN.ordinal()].isConnected() && !connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.EAST.ordinal()].isConnected() && !connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 16.0f;
			}

			else if (!connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 48.0f;
				minV = 16.0f;
			}

			else if (connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 48.0f;
			}

			else {
				minU = 48.0f;
				minV = 0.0f;
			}
		}

		else if (connections[EnumFacing.DOWN.ordinal()].isConnected() && connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.EAST.ordinal()].isConnected() && !connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 48.0f;
			}

			else if (!connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 48.0f;
				minV = 48.0f;
			}

			else if (connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 64.0f;
			}

			else {
				minU = 64.0f;
				minV = 16.0f;
			}
		}

		RenderHelper.drawZNeg(minVec, maxVec, minU * PIXEL, minV * PIXEL, (minU + 16.0f) * PIXEL, (minV + 16.0f) * PIXEL);
	}

	private void drawZPos() {
		float minU = 0.0f;
		float minV = 64.0f;

		// ProjectZed.logHelper.info(connections[EnumFacing.WEST.getOpposite().ordinal()].isConnected());

		if (connections[EnumFacing.SOUTH.ordinal()].isConnected()) return;

		if (!connections[EnumFacing.DOWN.ordinal()].isConnected() && !connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.EAST.ordinal()].isConnected() && !connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 0.0f;
			}

			else if (!connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 0.0f;
			}

			else if (connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 80.0f;
				minV = 16.0f;
			}
		}

		else if (!connections[EnumFacing.DOWN.ordinal()].isConnected() && connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.EAST.ordinal()].isConnected() && !connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 16.0f;
			}

			else if (!connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 16.0f;
			}

			else if (connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 48.0f;
			}

			else {
				minU = 32.0f;
				minV = 0.0f;
			}
		}

		else if (connections[EnumFacing.DOWN.ordinal()].isConnected() && !connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.EAST.ordinal()].isConnected() && !connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 16.0f;
			}

			else if (!connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 48.0f;
				minV = 16.0f;
			}

			else if (connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 48.0f;
			}

			else {
				minU = 48.0f;
				minV = 0.0f;
			}
		}

		else if (connections[EnumFacing.DOWN.ordinal()].isConnected() && connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.EAST.ordinal()].isConnected() && !connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 48.0f;
			}

			else if (!connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 48.0f;
				minV = 48.0f;
			}

			else if (connections[EnumFacing.EAST.ordinal()].isConnected() && connections[EnumFacing.EAST.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 64.0f;
			}

			else {
				minU = 64.0f;
				minV = 16.0f;
			}
		}

		RenderHelper.drawZPos(minVec, maxVec, minU * PIXEL, minV * PIXEL, (minU + 16.0f) * PIXEL, (minV + 16.0f) * PIXEL);
	}

	private void drawXNeg() {
		float minU = 0.0f;
		float minV = 64.0f;

		// ProjectZed.logHelper.info(connections[EnumFacing.WEST.getOpposite().ordinal()].isConnected());

		if (connections[EnumFacing.WEST.ordinal()].isConnected()) return;

		if (!connections[EnumFacing.DOWN.ordinal()].isConnected() && !connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 0.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 0.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 80.0f;
				minV = 16.0f;
			}
		}

		else if (!connections[EnumFacing.DOWN.ordinal()].isConnected() && connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 16.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 16.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 48.0f;
			}

			else {
				minU = 32.0f;
				minV = 0.0f;
			}
		}

		else if (connections[EnumFacing.DOWN.ordinal()].isConnected() && !connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 48.0f;
				minV = 16.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 16.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 48.0f;
			}

			else {
				minU = 48.0f;
				minV = 0.0f;
			}
		}

		else if (connections[EnumFacing.DOWN.ordinal()].isConnected() && connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 48.0f;
				minV = 48.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 48.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 64.0f;
			}

			else {
				minU = 64.0f;
				minV = 16.0f;
			}
		}

		RenderHelper.drawXNeg(minVec, maxVec, minU * PIXEL, minV * PIXEL, (minU + 16.0f) * PIXEL, (minV + 16.0f) * PIXEL);
	}

	private void drawXPos() {
		float minU = 0.0f;
		float minV = 64.0f;

		// ProjectZed.logHelper.info(connections[EnumFacing.WEST.getOpposite().ordinal()].isConnected());

		if (connections[EnumFacing.EAST.ordinal()].isConnected()) return;

		if (!connections[EnumFacing.DOWN.ordinal()].isConnected() && !connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 0.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 0.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 80.0f;
				minV = 16.0f;
			}
		}

		else if (!connections[EnumFacing.DOWN.ordinal()].isConnected() && connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 16.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 16.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 0.0f;
				minV = 48.0f;
			}

			else {
				minU = 32.0f;
				minV = 0.0f;
			}
		}

		else if (connections[EnumFacing.DOWN.ordinal()].isConnected() && !connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 48.0f;
				minV = 16.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 16.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 32.0f;
				minV = 48.0f;
			}

			else {
				minU = 48.0f;
				minV = 0.0f;
			}
		}

		else if (connections[EnumFacing.DOWN.ordinal()].isConnected() && connections[EnumFacing.UP.ordinal()].isConnected()) {
			if (connections[EnumFacing.NORTH.ordinal()].isConnected() && !connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 48.0f;
				minV = 48.0f;
			}

			else if (!connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 48.0f;
			}

			else if (connections[EnumFacing.NORTH.ordinal()].isConnected() && connections[EnumFacing.NORTH.getOpposite().ordinal()].isConnected()) {
				minU = 16.0f;
				minV = 64.0f;
			}

			else {
				minU = 64.0f;
				minV = 16.0f;
			}
		}

		RenderHelper.drawXPos(minVec, maxVec, minU * PIXEL, minV * PIXEL, (minU + 16.0f) * PIXEL, (minV + 16.0f) * PIXEL);
	}
	
}

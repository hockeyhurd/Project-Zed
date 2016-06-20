package com.projectzed.mod.renderer;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * @author hockeyhurd
 * @version 6/7/2016.
 */
@SideOnly(Side.CLIENT)
public class EnergyBankRenderer extends TileEntitySpecialRenderer<TileEntityEnergyBankBase> {

	private static final Tessellator tess = Tessellator.getInstance();
	private static final VertexBuffer vertexBuffer = tess.getBuffer();
	private static final ResourceLocation TEXTURE = new ResourceLocation(ProjectZed.assetDir, "textures/blocks/energyCellGeneric.png");
	private static final float PIXEL = 1.0f / 144.0f;
	private static final float bufOffset = 0.0001f;

	@Override
	public void renderTileEntityAt(TileEntityEnergyBankBase te, double x, double y, double z, float partialTicks, int destroyStage) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0 % 0x10000, 0xf0 / 0x10000);
		bindTexture(TEXTURE);

		// drawCuboid(te, 0f, 1f);

		// drawCuboid(te, 0f, 1f, 1);
		drawCuboid(te, 0f, 1f, 0);
		drawCuboid(te, 1f / 48f, 1f - 1f / 48f, 1);
		drawCuboid(te, 2f / 48f, 1f - 2f / 48f, 2);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslated(-x, -y, -z);
		GL11.glPopMatrix();
	}

	private void drawCuboid(TileEntityEnergyBankBase te, float min, float max, int layer) {
		// ProjectZed.logHelper.info("Called!");

		final Vector3<Float> minVec = new Vector3<Float>(min, min, min);
		final Vector3<Float> maxVec = new Vector3<Float>(max, max, max);

		// for (int i = 0; i < 3; i++)
			// drawCuboid(te, minVec, maxVec, i);

		drawCuboid(te, minVec, maxVec, layer);

		// vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
	}

	/**
	 * Method used for drawing energy cell bank into world. <br>
	 * <br>
	 * NOTE: This method resembles
	 *
	 * @param te te to draw as reference.
	 * @param minVec min vec to draw.
	 * @param maxVec max vec to draw.
	 * @param layer layer to draw.
	 */
	protected void drawCuboid(TileEntityEnergyBankBase te, Vector3<Float> minVec, Vector3<Float> maxVec, int layer) {

		if (te.getWorld() != null && te.getWorld().getTotalWorldTime() % 20L == 0) {
			te = (TileEntityEnergyBankBase) te.getWorld().getTileEntity(te.getPos());
		}

		// GL11.glPushMatrix();
		vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		// vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

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

			if (layer == 2) {

				if ((int)((float) te.getEnergyStored() / te.getMaxStorage() * 8f) == 0) {
					max = 48f * PIXEL;
					min = 32f * PIXEL;
					difU = 32f * PIXEL;
					difV = 0f * PIXEL;
				}

				else if ((int)((float) te.getEnergyStored() / te.getMaxStorage() * 8f) == 1) {
					max = 48f * PIXEL;
					min = 32f * PIXEL;
					difU = 16f * PIXEL;
					difV = 0f * PIXEL;
				}

				else if ((int)((float) te.getEnergyStored() / te.getMaxStorage() * 8f) == 2) {
					max = 48f * PIXEL;
					min = 32f * PIXEL;
					difU = 0f * PIXEL;
					difV = 0f * PIXEL;
				}

				else if ((int)((float) te.getEnergyStored() / te.getMaxStorage() * 8f) == 3) {
					max = 64f * PIXEL;
					min = 48f * PIXEL;
					difU = 48f * PIXEL;
					difV = 0f * PIXEL;
				}

				else if ((int)((float) te.getEnergyStored() / te.getMaxStorage() * 8f) == 4) {
					max = 64f * PIXEL;
					min = 48f * PIXEL;
					difU = 32f * PIXEL;
					difV = 0f * PIXEL;
				}

				else if ((int)((float) te.getEnergyStored() / te.getMaxStorage() * 8f) == 5) {
					max = 64f * PIXEL;
					min = 48f * PIXEL;
					difU = 16f * PIXEL;
					difV = 0f * PIXEL;
				}

				else if ((int)((float) te.getEnergyStored() / te.getMaxStorage() * 8f) == 6) {
					max = 80f * PIXEL;
					min = 64f * PIXEL;
					difU = 64f * PIXEL;
					difV = 0f * PIXEL;
				}

				else if ((int)((float) te.getEnergyStored() / te.getMaxStorage() * 8f) == 7) {
					max = 80f * PIXEL;
					min = 64f * PIXEL;
					difU = 48f * PIXEL;
					difV = 0f * PIXEL;
				}

				else if ((int)((float) te.getEnergyStored() / te.getMaxStorage() * 8f) == 8) {
					max = 80f * PIXEL;
					min = 64f * PIXEL;
					difU = 32f * PIXEL;
					difV = 0f * PIXEL;
				}
			}

			final float minU = min - difU;
			final float maxU = max - difU;
			final float minV = min - difV;
			final float maxV = max - difV;

			// -zz
			if (counter == 3) {
				// tessHelp.drawZNeg(minVec, maxVec, min, max, difU, difV, false);
				vertexBuffer.pos(minVec.x, maxVec.y, maxVec.z + bufOffset).tex(minU, minV).endVertex();
				vertexBuffer.pos(minVec.x, minVec.y, maxVec.z + bufOffset).tex(minU, maxV).endVertex();
				vertexBuffer.pos(maxVec.x, minVec.y, maxVec.z + bufOffset).tex(maxU, maxV).endVertex();
				vertexBuffer.pos(maxVec.x, maxVec.y, maxVec.z + bufOffset).tex(maxU, minV).endVertex();
			}

			// +zz
			if (counter == 2) {
				// tessHelp.drawZPos(minVec, maxVec, min, max, difU, difV, false);
				vertexBuffer.pos(maxVec.x, maxVec.y, minVec.z - bufOffset).tex(maxU, minV).endVertex();
				vertexBuffer.pos(maxVec.x, minVec.y, minVec.z - bufOffset).tex(maxU, maxV).endVertex();
				vertexBuffer.pos(minVec.x, minVec.y, minVec.z - bufOffset).tex(minU, maxV).endVertex();
				vertexBuffer.pos(minVec.x, maxVec.y, minVec.z - bufOffset).tex(minU, minV).endVertex();
			}

			// -xx
			if (counter == 4) {
				vertexBuffer.pos(minVec.x - bufOffset, maxVec.y, minVec.z).tex(minU, minV).endVertex();
				vertexBuffer.pos(minVec.x - bufOffset, minVec.y, minVec.z).tex(minU, maxV).endVertex();
				vertexBuffer.pos(minVec.x - bufOffset, minVec.y, maxVec.z).tex(maxU, maxV).endVertex();
				vertexBuffer.pos(minVec.x - bufOffset, maxVec.y, maxVec.z).tex(maxU, minV).endVertex();
			}

			// +xx
			if (counter == 5) {
				// tessHelp.drawXPos(minVec, maxVec, min, max, difU, difV, false);\
				vertexBuffer.pos(maxVec.x + bufOffset, maxVec.y, maxVec.z).tex(maxU, minV).endVertex();
				vertexBuffer.pos(maxVec.x + bufOffset, minVec.y, maxVec.z).tex(maxU, maxV).endVertex();
				vertexBuffer.pos(maxVec.x + bufOffset, minVec.y, minVec.z).tex(minU, maxV).endVertex();
				vertexBuffer.pos(maxVec.x + bufOffset, maxVec.y, minVec.z).tex(minU, minV).endVertex();
			}

			// +yy
			if (counter == 1) {
				// tessHelp.drawYPos(minVec, maxVec, min, max, difU, difV, false);
				vertexBuffer.pos(minVec.x, maxVec.y + bufOffset, minVec.z).tex(maxU, minV).endVertex();
				vertexBuffer.pos(minVec.x, maxVec.y + bufOffset, maxVec.z).tex(maxU, maxV).endVertex();
				vertexBuffer.pos(maxVec.x, maxVec.y + bufOffset, maxVec.z).tex(minU, maxV).endVertex();
				vertexBuffer.pos(maxVec.x, maxVec.y + bufOffset, minVec.z).tex(minU, minV).endVertex();
			}

			// -yy
			if (counter == 0) {
				// tessHelp.drawYNeg(minVec, maxVec, min, max, difU, difV, false);
				vertexBuffer.pos(maxVec.x, minVec.y - bufOffset, minVec.z).tex(minU, minV).endVertex();
				vertexBuffer.pos(maxVec.x, minVec.y - bufOffset, maxVec.z).tex(minU, maxV).endVertex();
				vertexBuffer.pos(minVec.x, minVec.y - bufOffset, maxVec.z).tex(maxU, maxV).endVertex();
				vertexBuffer.pos(minVec.x, minVec.y - bufOffset, minVec.z).tex(maxU, minV).endVertex();
			}

			counter++;
		}

		tess.draw();
		// GL11.glPopMatrix();
	}

}

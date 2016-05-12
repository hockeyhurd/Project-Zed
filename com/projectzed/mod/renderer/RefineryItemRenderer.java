/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.renderer;

import com.hockeyhurd.hcorelib.api.item.AbstractItemRenderer;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Item rendering class for refinery.
 *
 * @author hockeyhurd
 * @version 8/7/2015.
 */
@SideOnly(Side.CLIENT)
public class RefineryItemRenderer extends AbstractItemRenderer {

	private ResourceLocation texture;
	private final float PIXEL = 1f / 64f;
	private final float TEX_SPACING = 0.00001f;
	private Vector3<Float> translation = Vector3.zero.getVector3f();

	private static final Vector3<Float> minP = RefineryRenderer.minP.copy();
	private static final Vector3<Float> maxP = RefineryRenderer.maxP.copy();

	private static final Vector3<Float> minT = RefineryRenderer.minT.copy();
	private static final Vector3<Float> maxT = RefineryRenderer.maxT.copy();

	private static final Vector3<Float> minT2 = RefineryRenderer.minT2.copy();
	private static final Vector3<Float> maxT2 = RefineryRenderer.maxT2.copy();

	private static final Vector3<Float> minF = RefineryRenderer.minF.copy();
	private static final Vector3<Float> maxF = RefineryRenderer.maxF.copy();

	private static final Vector3<Float> minSt = RefineryRenderer.minSt.copy();
	private static final Vector3<Float> maxSt = RefineryRenderer.maxSt.copy();

	private static final Vector3<Float> minSt2 = RefineryRenderer.minSt2.copy();
	private static final Vector3<Float> maxSt2 = RefineryRenderer.maxSt2.copy();

	public RefineryItemRenderer(IIcon icon) {
		super(icon);
		this.texture = new ResourceLocation("projectzed", "textures/blocks/refinery.png");
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		translation.x = 0f;
		translation.y = 0f;
		translation.z = 0f;

		tessHelp.startDrawingQuads();

		// adjust rendering space to match what caller expects
		boolean mustTranslate = false;
		float angle = 0.0f;
		switch (type) {
			case EQUIPPED:
				translation.x = 0.5f;
				translation.y = 0.5f;
				translation.z = 0.75f;
				angle = 90.0f;

				GL11.glTranslatef(translation.x, translation.y, translation.z);
				GL11.glRotatef(angle, 0f, 1f, 0f);

				break; // caller expects us to render over [0,0,0] to [1,1,1], no translation necessary
			case EQUIPPED_FIRST_PERSON: {
				translation.x = 0f;
				translation.y = 0.5f;
				translation.z = 0f;

				GL11.glTranslatef(translation.x, translation.y, translation.z);
				break; // caller expects us to render over [0,0,0] to [1,1,1], no translation necessary
			}
			case ENTITY:
			case INVENTORY: {
				// translate our coordinates so that [0,0,0] to [1,1,1] translates to the [-0.5, -0.5, -0.5] to [0.5, 0.5, 0.5] expected by the caller.
				translation.x = -0.5f;
				translation.y = -1.25f;
				translation.z = -0.5f;
				angle = 180.0f;

				GL11.glTranslatef(translation.x, translation.y, translation.z);
				GL11.glRotatef(angle, 0f, 1f, 0f);
				mustTranslate = true;   // must undo the translation when we're finished rendering
				break;
			}
			default:
				break; // never here
		}

		renderPlatform();
		renderFurnace(false);
		renderSideTanks();
		renderSmokeStack();

		tessHelp.draw();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);

		if (angle != 0.0f) GL11.glRotatef(-angle, 0f, 1f, 0f);
		if (mustTranslate) GL11.glTranslatef(-translation.x, -translation.y, -translation.z);

	}

	private void renderPlatform() {

		tessHelp.setNormal(-1f, 0f, 0f);

		// x-
		tessHelp.addVertUV(minP.x, maxP.y, minP.z, 16f * PIXEL, 1f);
		tessHelp.addVertUV(minP.x, minP.y, minP.z, 16f * PIXEL, 61f * PIXEL);
		tessHelp.addVertUV(minP.x, minP.y, maxP.z, 0f, 61f * PIXEL);
		tessHelp.addVertUV(minP.x, maxP.y, maxP.z, 0f, 1f);

		tessHelp.setNormal(1f, 0f, 0f);

		// x+
		tessHelp.addVertUV(maxP.x, maxP.y, maxP.z, 0f, 1f);
		tessHelp.addVertUV(maxP.x, minP.y, maxP.z, 0f, 61f * PIXEL);
		tessHelp.addVertUV(maxP.x, minP.y, minP.z, 16f * PIXEL, 61f * PIXEL);
		tessHelp.addVertUV(maxP.x, maxP.y, minP.z, 16f * PIXEL, 1f);

		tessHelp.setNormal(0f, 0f, -1f);

		// z-
		tessHelp.addVertUV(minP.x, maxP.y, maxP.z, 16f * PIXEL, 1f);
		tessHelp.addVertUV(minP.x, minP.y, maxP.z, 16f * PIXEL, 61f * PIXEL);
		tessHelp.addVertUV(maxP.x, minP.y, maxP.z, 0f, 61f * PIXEL);
		tessHelp.addVertUV(maxP.x, maxP.y, maxP.z, 0f, 1f);

		tessHelp.setNormal(0f, 0f, 1f);

		// z+
		tessHelp.addVertUV(maxP.x, maxP.y, minP.z, 0f, 1f);
		tessHelp.addVertUV(maxP.x, minP.y, minP.z, 0f, 61f * PIXEL);
		tessHelp.addVertUV(minP.x, minP.y, minP.z, 16f * PIXEL, 61f * PIXEL);
		tessHelp.addVertUV(minP.x, maxP.y, minP.z, 16f * PIXEL, 1f);

		tessHelp.setNormal(0f, -1f, 0f);

		// y-
		tessHelp.addVertUV(minP.x, minP.y, maxP.z, 0f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minP.x, minP.y, minP.z, 0f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxP.x, minP.y, minP.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxP.x, minP.y, maxP.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.setNormal(0f, 1f, 0f);

		// y+
		tessHelp.addVertUV(maxP.x, maxP.y, maxP.z, 16f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxP.x, maxP.y, minP.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minP.x, maxP.y, minP.z, 0f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minP.x, maxP.y, maxP.z, 0f * PIXEL, 48f * PIXEL);

	}

	// TODO-ADD: Add rendering of fluids!!!
	private void renderSideTanks() {
		// x-
		tessHelp.setNormal(-1f, 0f, 0f);

		tessHelp.addVertUV(minT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(minT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);

		tessHelp.addVertUV(maxT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);

		// x+
		tessHelp.setNormal(1f, 0f, 0f);

		tessHelp.addVertUV(maxT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);

		tessHelp.addVertUV(minT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(minT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);

		// z-
		tessHelp.setNormal(0f, 0f, -1f);

		tessHelp.addVertUV(maxT.x, maxT.y, minT.z, 0f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxT.x, minT.y, minT.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);

		tessHelp.addVertUV(maxT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT.x, minT.y, maxT.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT.x, maxT.y, maxT.z, 16f * PIXEL, 16f * PIXEL);

		// z+
		tessHelp.setNormal(0f, 0f, 1f);

		tessHelp.addVertUV(minT.x, maxT.y, maxT.z, 16f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(minT.x, minT.y, maxT.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT.x, minT.y, maxT.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);

		tessHelp.addVertUV(minT.x, maxT.y, minT.z, 16f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(minT.x, minT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT.x, minT.y, minT.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT.x, maxT.y, minT.z, 0f * PIXEL, 16f * PIXEL);

		// y+
		tessHelp.setNormal(0f, 1f, 1f);

		tessHelp.addVertUV(maxT.x, maxT.y, maxT.z, 16f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxT.x, maxT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT.x, maxT.y, minT.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);

		tessHelp.setNormal(0f, -1f, 1f);

		tessHelp.addVertUV(minT.x, maxT.y, maxT.z, 0f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(minT.x, maxT.y, minT.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT.x, maxT.y, minT.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT.x, maxT.y, maxT.z, 16f * PIXEL, 16f * PIXEL);

		// x-
		tessHelp.setNormal(-1f, 0f, 0f);

		tessHelp.addVertUV(minT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(minT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);

		tessHelp.addVertUV(maxT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);

		// x+
		tessHelp.setNormal(1f, 0f, 0f);

		tessHelp.addVertUV(maxT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);

		tessHelp.addVertUV(minT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(minT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);

		// z-
		tessHelp.setNormal(0f, 0f, -1f);

		tessHelp.addVertUV(maxT2.x, maxT2.y, minT2.z, 0f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxT2.x, minT2.y, minT2.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);

		tessHelp.addVertUV(maxT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT2.x, minT2.y, maxT2.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT2.x, maxT2.y, maxT2.z, 16f * PIXEL, 16f * PIXEL);

		// z+
		tessHelp.setNormal(0f, 0f, 1f);

		tessHelp.addVertUV(minT2.x, maxT2.y, maxT2.z, 16f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(minT2.x, minT2.y, maxT2.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT2.x, minT2.y, maxT2.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);

		tessHelp.addVertUV(minT2.x, maxT2.y, minT2.z, 16f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(minT2.x, minT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT2.x, minT2.y, minT2.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT2.x, maxT2.y, minT2.z, 0f * PIXEL, 16f * PIXEL);

		// y+
		tessHelp.setNormal(0f, 1f, 1f);

		tessHelp.addVertUV(maxT2.x, maxT2.y, maxT2.z, 16f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxT2.x, maxT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT2.x, maxT2.y, minT2.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(minT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);

		tessHelp.setNormal(0f, -1f, 1f);

		tessHelp.addVertUV(minT2.x, maxT2.y, maxT2.z, 0f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(minT2.x, maxT2.y, minT2.z, 0f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT2.x, maxT2.y, minT2.z, 16f * PIXEL, 0f * PIXEL);
		tessHelp.addVertUV(maxT2.x, maxT2.y, maxT2.z, 16f * PIXEL, 16f * PIXEL);

		// renderFluids();
	}

	private void renderFurnace(final boolean powered) {
		// x-
		tessHelp.setNormal(-1f, 0f, 0f);

		if (powered) {
			tessHelp.addVertUV(minF.x, maxF.y, minF.z, 48f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(minF.x, minF.y, minF.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(minF.x, minF.y, maxF.z, 64f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(minF.x, maxF.y, maxF.z, 64f * PIXEL, 16f * PIXEL);
		}

		else {
			tessHelp.addVertUV(minF.x, maxF.y, minF.z, 32f * PIXEL, 16f * PIXEL);
			tessHelp.addVertUV(minF.x, minF.y, minF.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(minF.x, minF.y, maxF.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(minF.x, maxF.y, maxF.z, 48f * PIXEL, 16f * PIXEL);
		}

		// x+
		tessHelp.setNormal(1f, 0f, 0f);

		tessHelp.addVertUV(maxF.x, maxF.y, maxF.z, 32f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxF.x, minF.y, maxF.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxF.x, minF.y, minF.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxF.x, maxF.y, minF.z, 16f * PIXEL, 16f * PIXEL);

		// z+
		tessHelp.setNormal(0f, 0f, -1f);

		tessHelp.addVertUV(maxF.x, maxF.y, minF.z, 32f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxF.x, minF.y, minF.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minF.x, minF.y, minF.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minF.x, maxF.y, minF.z, 16f * PIXEL, 16f * PIXEL);

		// z+
		tessHelp.setNormal(0f, 0f, 1f);

		tessHelp.addVertUV(minF.x, maxF.y, maxF.z, 16f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(minF.x, minF.y, maxF.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxF.x, minF.y, maxF.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxF.x, maxF.y, maxF.z, 32f * PIXEL, 16f * PIXEL);

		// y+
		tessHelp.setNormal(0f, 1f, 0f);

		tessHelp.addVertUV(maxF.x, maxF.y, maxF.z, 0f * PIXEL, 16f * PIXEL);
		tessHelp.addVertUV(maxF.x, maxF.y, minF.z, 0f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minF.x, maxF.y, minF.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minF.x, maxF.y, maxF.z, 16f * PIXEL, 16f * PIXEL);

	}

	// TODO-ADD: Add rendering of fluids!!!
	private void renderTopTank() {
		Vector3<Float> min = new Vector3<Float>(0f, 1f - 3f / 16f, 0f);
		Vector3<Float> max = new Vector3<Float>(1f, 1f, 1f);

		{
			tessHelp.setNormal(-1f, 0f, 0f);

			// x-
			tessHelp.addVertUV(min.x, max.y, min.z, 32f * PIXEL, 1f);
			tessHelp.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, max.z, 16f * PIXEL, 1f);

			tessHelp.addVertUV(max.x, max.y, min.z, 32f * PIXEL, 1f);
			tessHelp.addVertUV(max.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, max.z, 16f * PIXEL, 1f);

			tessHelp.setNormal(1f, 0f, 0f);

			// x+
			tessHelp.addVertUV(max.x, max.y, max.z, 16f * PIXEL, 1f);
			tessHelp.addVertUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, min.z, 32f * PIXEL, 1f);

			tessHelp.addVertUV(min.x, max.y, max.z, 16f * PIXEL, 1f);
			tessHelp.addVertUV(min.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, min.z, 32f * PIXEL, 1f);

			tessHelp.setNormal(0f, 0f, -1f);

			// z-
			tessHelp.addVertUV(min.x, max.y, max.z, 32f * PIXEL, 1f);
			tessHelp.addVertUV(min.x, min.y, max.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, max.z, 16f * PIXEL, 1f);

			tessHelp.addVertUV(min.x, max.y, min.z, 32f * PIXEL, 1f);
			tessHelp.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, min.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, min.z, 16f * PIXEL, 1f);

			tessHelp.setNormal(0f, 0f, 1f);

			// z+
			tessHelp.addVertUV(max.x, max.y, min.z, 16f * PIXEL, 1f);
			tessHelp.addVertUV(max.x, min.y, min.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, min.z, 32f * PIXEL, 1f);

			tessHelp.addVertUV(max.x, max.y, max.z, 16f * PIXEL, 1f);
			tessHelp.addVertUV(max.x, min.y, max.z, 16f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, max.z, 32f * PIXEL, 61f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, max.z, 32f * PIXEL, 1f);

			tessHelp.setNormal(0f, -1f, 0f);

			// y-
			tessHelp.addVertUV(min.x, min.y, max.z, 16f * PIXEL, 48f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, max.z, 32f * PIXEL, 48f * PIXEL);

			tessHelp.addVertUV(min.x, max.y, max.z, 16f * PIXEL, 48f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, max.z, 32f * PIXEL, 48f * PIXEL);

			tessHelp.setNormal(0f, 1f, 0f);

			// y+
			tessHelp.addVertUV(max.x, max.y, max.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.addVertUV(max.x, max.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(min.x, max.y, max.z, 16f * PIXEL, 48f * PIXEL);

			tessHelp.addVertUV(max.x, min.y, max.z, 32f * PIXEL, 48f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, min.z, 16f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, max.z, 16f * PIXEL, 48f * PIXEL);

		}

	}

	private void renderSmokeStack() {

		// x-
		tessHelp.setNormal(-1f, 0f, 0f);

		tessHelp.addVertUV(minSt.x, maxSt.y, minSt2.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, minSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, maxSt2.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, maxSt.y, maxSt2.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(maxSt.x, maxSt.y, minSt2.z, 48f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, minSt2.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, maxSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, maxSt.y, maxSt2.z, 32f * PIXEL, 48f * PIXEL);

		// x+
		tessHelp.setNormal(1f, 0f, 0f);

		tessHelp.addVertUV(maxSt.x, maxSt.y, maxSt2.z, 16f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, maxSt2.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, minSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, maxSt.y, minSt2.z, 32f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(minSt.x, maxSt.y, maxSt2.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, maxSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, minSt2.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, maxSt.y, minSt2.z, 48f * PIXEL, 48f * PIXEL);

		// z-
		tessHelp.setNormal(0f, 0f, -1f);

		tessHelp.addVertUV(maxSt2.x, maxSt.y, minSt.z, 16f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, minSt.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, minSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, maxSt.y, minSt.z, 32f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(maxSt2.x, maxSt.y, maxSt.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, maxSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, maxSt.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, maxSt.y, maxSt.z, 48f * PIXEL, 48f * PIXEL);

		// z+
		tessHelp.setNormal(0f, 0f, 1f);

		tessHelp.addVertUV(minSt2.x, maxSt.y, maxSt.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, maxSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, maxSt.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, maxSt.y, maxSt.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(minSt2.x, maxSt.y, minSt.z, 48f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, minSt.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, minSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, maxSt.y, minSt.z, 32f * PIXEL, 48f * PIXEL);

		// y-
			/*tessHelp.setNormal(0f, 1f, 0f);

			tessHelp.addVertUV(max.x, min.y, max.z, 48f * PIXEL, 48f * PIXEL);
			tessHelp.addVertUV(max.x, min.y, min.z, 48f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, min.z, 32f * PIXEL, 32f * PIXEL);
			tessHelp.addVertUV(min.x, min.y, max.z, 32f * PIXEL, 48f * PIXEL);*/

		// y+
		tessHelp.setNormal(0f, 1f, 0f);

		tessHelp.addVertUV(maxSt.x, maxSt.y, maxSt.z, 64f * PIXEL, 64f * PIXEL);
		tessHelp.addVertUV(maxSt.x, maxSt.y, minSt.z, 64f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt.x, maxSt.y, minSt.z, 48f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt.x, maxSt.y, maxSt.z, 48f * PIXEL, 64f * PIXEL);

		// x-/z-
		tessHelp.setNormal(-1f, 0f, -1f);

		tessHelp.addVertUV(minSt2.x, maxSt.y, minSt.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, minSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, minSt2.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, maxSt.y, minSt2.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(maxSt.x, maxSt.y, maxSt2.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, maxSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, maxSt.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, maxSt.y, maxSt.z, 48f * PIXEL, 48f * PIXEL);

		// x+/z-
		tessHelp.setNormal(-1f, 0f, 1f);

		tessHelp.addVertUV(maxSt.x, maxSt.y, minSt2.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, minSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, minSt.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, maxSt.y, minSt.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(minSt2.x, maxSt.y, maxSt.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, maxSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, maxSt2.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt.x, maxSt.y, maxSt2.z, 48f * PIXEL, 48f * PIXEL);

		// x-/z+
		tessHelp.setNormal(-1f, 0f, 1f);

		tessHelp.addVertUV(minSt.x, maxSt.y, maxSt2.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, maxSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, maxSt.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, maxSt.y, maxSt.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(maxSt2.x, maxSt.y, minSt.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, minSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, minSt2.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, maxSt.y, minSt2.z, 48f * PIXEL, 48f * PIXEL);

		// x+/z+
		tessHelp.setNormal(1f, 0f, 1f);

		tessHelp.addVertUV(maxSt2.x, maxSt.y, maxSt.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(maxSt2.x, minSt.y, maxSt.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, minSt.y, maxSt2.z, 16f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(maxSt.x, maxSt.y, maxSt2.z, 16f * PIXEL, 48f * PIXEL);

		tessHelp.addVertUV(minSt.x, maxSt.y, minSt2.z, 32f * PIXEL, 48f * PIXEL);
		tessHelp.addVertUV(minSt.x, minSt.y, minSt2.z, 32f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, minSt.y, minSt.z, 48f * PIXEL, 32f * PIXEL);
		tessHelp.addVertUV(minSt2.x, maxSt.y, minSt.z, 48f * PIXEL, 48f * PIXEL);

	}

}

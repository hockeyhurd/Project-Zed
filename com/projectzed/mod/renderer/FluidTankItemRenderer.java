/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.renderer;

import com.hockeyhurd.api.item.AbstractItemRenderer;
import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

/**
 * Class containing code for rendering fluid tanks as item.
 * 
 * @author hockeyhurd
 * @version Jan 24, 2015
 */
@SideOnly(Side.CLIENT)
public class FluidTankItemRenderer extends AbstractItemRenderer {

	protected Tessellator tess;
	protected final float PIXEL = 1f / 16f;
	protected float calc = 4f * PIXEL;
	protected boolean renderInside = true;
	protected byte tier;
	protected Vector4Helper<Float> vec0, vec1;
	
	/**
	 * @param icon = icon to draw from.
	 */
	public FluidTankItemRenderer(IIcon icon, byte tier) {
		super(icon);
		this.tier = tier;
		this.vec0 = new Vector4Helper<Float>(5f / 16f, 2f / 16f, 5f / 16f);
		this.vec1 = new Vector4Helper<Float>(1f - 5f / 16f, 1f -  1f / 16f, 1f - 5f / 16f);
		this.tess = Tessellator.instance;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.hockeyhurd.api.item.AbstractItemRenderer#renderItem(net.minecraftforge.client.IItemRenderer.ItemRenderType, net.minecraft.item.ItemStack, java.lang.Object[])
	 */
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		tess.startDrawingQuads();

		// adjust rendering space to match what caller expects
		boolean mustTranslate = false;
		switch (type) {
			case EQUIPPED:
			case EQUIPPED_FIRST_PERSON: {
				break; // caller expects us to render over [0,0,0] to [1,1,1], no translation necessary
			}
			case ENTITY:
			case INVENTORY: {
				// translate our coordinates so that [0,0,0] to [1,1,1] translates to the [-0.5, -0.5, -0.5] to [0.5, 0.5, 0.5] expected by the caller.
				GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
				mustTranslate = true;   // must undo the translation when we're finished rendering
				
				break;
			}
			default:
				break; // never here
		}

		boolean hasFluid = item.hasTagCompound() && item.stackTagCompound.getFloat("Fluid ID") >= 0 && item.stackTagCompound.getFloat("Fluid Amount") > 0;

		if (hasFluid) {
			int amount = (int) item.stackTagCompound.getFloat("Fluid Amount");
			FluidStack fluidStack = new FluidStack(FluidRegistry.getFluid((int) item.stackTagCompound.getFloat("Fluid ID")), amount);
			renderFluid(fluidStack);
		}

		// xpos face blue
		IIcon icon = item.getItem().getIconFromDamage(5);
		tess.setNormal(0.0f, 1.0f, 0.0f);
		tess.addVertexWithUV(1f - calc, 0.0, calc, (double) icon.getMaxU(), (double) icon.getMaxV());
		tess.addVertexWithUV(1f - calc, 1.0, calc, (double) icon.getMaxU(), (double) icon.getMinV());
		tess.addVertexWithUV(1f - calc, 1.0, 1f - calc, (double) icon.getMinU(), (double) icon.getMinV());
		tess.addVertexWithUV(1f - calc, 0.0, 1f - calc, (double) icon.getMinU(), (double) icon.getMaxV());
		
		if (renderInside) {
			tess.addVertexWithUV(1f - calc, 0.0, 1f - calc, (double) icon.getMinU(), (double) icon.getMaxV());
			tess.addVertexWithUV(1f - calc, 1.0, 1f - calc, (double) icon.getMinU(), (double) icon.getMinV());
			tess.addVertexWithUV(1f - calc, 1.0, calc, (double) icon.getMaxU(), (double) icon.getMinV());
			tess.addVertexWithUV(1f - calc, 0.0, calc, (double) icon.getMaxU(), (double) icon.getMaxV());
		}

		// xneg face purple
		icon = item.getItem().getIconFromDamage(4);
		tess.addVertexWithUV(calc, 0.0, 1f - calc, (double) icon.getMaxU(), (double) icon.getMaxV());
		tess.addVertexWithUV(calc, 1.0, 1f - calc, (double) icon.getMaxU(), (double) icon.getMinV());
		tess.addVertexWithUV(calc, 1.0, calc, (double) icon.getMinU(), (double) icon.getMinV());
		tess.addVertexWithUV(calc, 0.0, calc, (double) icon.getMinU(), (double) icon.getMaxV());
		
		if (renderInside) {
			tess.addVertexWithUV(calc, 0.0, calc, (double) icon.getMinU(), (double) icon.getMaxV());
			tess.addVertexWithUV(calc, 1.0, calc, (double) icon.getMinU(), (double) icon.getMinV());
			tess.addVertexWithUV(calc, 1.0, 1f - calc, (double) icon.getMaxU(), (double) icon.getMinV());
			tess.addVertexWithUV(calc, 0.0, 1f - calc, (double) icon.getMaxU(), (double) icon.getMaxV());
		}

		// zneg face white
		icon = item.getItem().getIconFromDamage(2);
		tess.addVertexWithUV(calc, 0.0, calc, (double) icon.getMaxU(), (double) icon.getMaxV());
		tess.addVertexWithUV(calc, 1.0, calc, (double) icon.getMaxU(), (double) icon.getMinV());
		tess.addVertexWithUV(1f - calc, 1.0, calc, (double) icon.getMinU(), (double) icon.getMinV());
		tess.addVertexWithUV(1f - calc, 0.0, calc, (double) icon.getMinU(), (double) icon.getMaxV());
		
		if (renderInside) {
			tess.addVertexWithUV(1f - calc, 0.0, calc, (double) icon.getMinU(), (double) icon.getMaxV());
			tess.addVertexWithUV(1f - calc, 1.0, calc, (double) icon.getMinU(), (double) icon.getMinV());
			tess.addVertexWithUV(calc, 1.0, calc, (double) icon.getMaxU(), (double) icon.getMinV());
			tess.addVertexWithUV(calc, 0.0, calc, (double) icon.getMaxU(), (double) icon.getMaxV());
		}

		// zpos face green
		icon = item.getItem().getIconFromDamage(3);
		tess.addVertexWithUV(1.0f - calc, 0.0, 1.0 - calc, (double) icon.getMaxU(), (double) icon.getMaxV());
		tess.addVertexWithUV(1.0f - calc, 1.0, 1.0 - calc, (double) icon.getMaxU(), (double) icon.getMinV());
		tess.addVertexWithUV(calc, 1.0, 1.0f - calc, (double) icon.getMinU(), (double) icon.getMinV());
		tess.addVertexWithUV(calc, 0.0, 1.0f - calc, (double) icon.getMinU(), (double) icon.getMaxV());
		
		if (renderInside) {
			tess.addVertexWithUV(calc, 0.0, 1.0f - calc, (double) icon.getMinU(), (double) icon.getMaxV());
			tess.addVertexWithUV(calc, 1.0, 1.0f - calc, (double) icon.getMinU(), (double) icon.getMinV());
			tess.addVertexWithUV(1.0f - calc, 1.0, 1.0 - calc, (double) icon.getMaxU(), (double) icon.getMinV());
			tess.addVertexWithUV(1.0f - calc, 0.0, 1.0 - calc, (double) icon.getMaxU(), (double) icon.getMaxV());
		}

		// ypos face red
		icon = item.getItem().getIconFromDamage(1);
		tess.addVertexWithUV(1.0f - calc, 1.0, 1.0f - calc, (double) icon.getMaxU(), (double) icon.getMaxV());
		tess.addVertexWithUV(1.0f - calc, 1.0, calc, (double) icon.getMaxU(), (double) icon.getMinV());
		tess.addVertexWithUV(calc, 1.0, calc, (double) icon.getMinU(), (double) icon.getMinV());
		tess.addVertexWithUV(calc, 1.0, 1.0f - calc, (double) icon.getMinU(), (double) icon.getMaxV());
		
		if (renderInside) {
			tess.addVertexWithUV(calc, 1.0, 1.0f - calc, (double) icon.getMinU(), (double) icon.getMaxV());
			tess.addVertexWithUV(calc, 1.0, calc, (double) icon.getMinU(), (double) icon.getMinV());
			tess.addVertexWithUV(1.0f - calc, 1.0, calc, (double) icon.getMaxU(), (double) icon.getMinV());
			tess.addVertexWithUV(1.0f - calc, 1.0, 1.0f - calc, (double) icon.getMaxU(), (double) icon.getMaxV());
		}

		// yneg face yellow
		icon = item.getItem().getIconFromDamage(0);
		tess.addVertexWithUV(calc, 0.0, 1.0f - calc, (double) icon.getMaxU(), (double) icon.getMaxV());
		tess.addVertexWithUV(calc, 0.0, calc, (double) icon.getMaxU(), (double) icon.getMinV());
		tess.addVertexWithUV(1.0f - calc, 0.0, calc, (double) icon.getMinU(), (double) icon.getMinV());
		tess.addVertexWithUV(1.0f - calc, 0.0, 1.0f - calc, (double) icon.getMinU(), (double) icon.getMaxV());
		
		if (renderInside) {
			tess.addVertexWithUV(1.0f - calc, 0.0, 1.0f - calc, (double) icon.getMinU(), (double) icon.getMaxV());
			tess.addVertexWithUV(1.0f - calc, 0.0, calc, (double) icon.getMinU(), (double) icon.getMinV());
			tess.addVertexWithUV(calc, 0.0, calc, (double) icon.getMaxU(), (double) icon.getMinV());
			tess.addVertexWithUV(calc, 0.0, 1.0f - calc, (double) icon.getMaxU(), (double) icon.getMaxV());
		}

		tess.draw();

		if (mustTranslate) GL11.glTranslatef(0.5f, 0.5f, 0.5f);
	}

	/**
	 * Method to handle rendering of fluids inside of tank.
	 *
	 * @param stack fluidstack to render as reference.
	 */
	private void renderFluid(FluidStack stack) {
		int current = stack.amount;
		int max = TileEntityFluidTankBase.TIER_SIZE[this.tier];
		IIcon icon = stack.getFluid().getStillIcon();

		if (icon == null) return;

		final Vector4Helper<Float> maxVecY = vec1.copy();
		// vec1.y = (3f + ((int) (te.getTank().getFluidAmount() / (float) (te.getTank().getCapacity()) * 10))) / 16f;
		vec1.y = (3f + ((int) (current / (float) (max) * 10))) / 16f;

		// this.bindTexture(TextureMap.locationBlocksTexture);
		// Tessellator tess = tessHelp.tess;
		// tess.startDrawingQuads();

		tessHelp.drawZNeg(icon, vec0, vec1, false);
		tessHelp.drawZPos(icon, vec0, vec1, false);
		tessHelp.drawXNeg(icon, vec0, vec1, false);
		tessHelp.drawXPos(icon, vec0, vec1, false);

		tessHelp.drawYNeg(icon, vec0, maxVecY, false);
		tessHelp.drawYPos(icon, vec0, maxVecY, false);

		// tess.draw();
	}

}

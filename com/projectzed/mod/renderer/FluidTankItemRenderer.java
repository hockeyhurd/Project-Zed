package com.projectzed.mod.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

import com.hockeyhurd.api.item.AbstractItemRenderer;

/**
 * Class containing code for rendering fluid tanks as item.
 * 
 * @author hockeyhurd
 * @version Jan 24, 2015
 */
public class FluidTankItemRenderer extends AbstractItemRenderer {

	protected Tessellator tess;
	protected final float PIXEL = 1f / 16f;
	protected float calc = 4f * PIXEL;
	protected boolean renderInside = true;
	
	/**
	 * @param icon = icon to draw from.
	 */
	public FluidTankItemRenderer(IIcon icon) {
		super(icon);
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


}

package com.projectzed.mod.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing rendering instructions for blocks to items.
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
@SideOnly(Side.CLIENT)
public class EnergyPipeItemRenderer implements IItemRenderer {

	private ResourceLocation texture = new ResourceLocation("projectzed", "textures/blocks/pipe_energy.png");
	private boolean generalizeNormals = true;
	private final float PIXEL = 1f / 16f;
	private final float TEXTURE_PIXEL = 1f / 32f;
	private final float calc = 11 * PIXEL / 2;
	
	protected Tessellator tess;
	
	public EnergyPipeItemRenderer() {
		tess = Tessellator.instance;
	}

	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		drawItem(type, item);
	}

	protected void drawItem(ItemRenderType type, ItemStack item) {
		tess.startDrawingQuads();
		
		// adjust rendering space to match what caller expects
		boolean mustTranslate = false;
		switch (type) {
			case EQUIPPED:
			case EQUIPPED_FIRST_PERSON: {
				GL11.glTranslated(0.5F, 0.5F, 0.5F);
				GL11.glRotatef(-45, 1, 0, 0);
				mustTranslate = true;
				break; // caller expects us to render over [0,0,0] to [1,1,1], no translation necessary
			}
			case ENTITY:
			case INVENTORY: {
				// translate our coordinates so that [0,0,0] to [1,1,1] translates to the [-0.5, -0.5, -0.5] to [0.5, 0.5, 0.5] expected by the
				// caller.
				// GL11.glRotatef(45, 1, 0, 0);
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
				mustTranslate = true;   // must undo the translation when we're finished rendering
				break;
			}
			default:
				break; // never here
		}

		// xpos face blue
		IIcon icon = item.getItem().getIconFromDamage(5);
		if (generalizeNormals) tess.setNormal(0.0F, 1.0F, 0.0F);

		/*tess.addVertexWithUV(1.0, 0.0, 0.0, (double) icon.getMaxU(), (double) icon.getMaxV());
		tess.addVertexWithUV(1.0, 1.0, 0.0, (double) icon.getMaxU(), (double) icon.getMinV());
		tess.addVertexWithUV(1.0, 1.0, 1.0, (double) icon.getMinU(), (double) icon.getMinV());
		tess.addVertexWithUV(1.0, 0.0, 1.0, (double) icon.getMinU(), (double) icon.getMaxV()); 
		
		tess.addVertexWithUV(0.0, 0.0, 1.0, (double) icon.getMaxU(), (double) icon.getMaxV());
		tess.addVertexWithUV(0.0, 1.0, 1.0, (double) icon.getMaxU(), (double) icon.getMinV());
		tess.addVertexWithUV(0.0, 1.0, 0.0, (double) icon.getMinU(), (double) icon.getMinV());
		tess.addVertexWithUV(0.0, 0.0, 0.0, (double) icon.getMinU(), (double) icon.getMaxV());
		
		tess.addVertexWithUV(1.0, 1.0, 1.0, (double) icon.getMaxU(), (double) icon.getMaxV());
		tess.addVertexWithUV(1.0, 1.0, 0.0, (double) icon.getMaxU(), (double) icon.getMinV());
		tess.addVertexWithUV(0.0, 1.0, 0.0, (double) icon.getMinU(), (double) icon.getMinV());
		tess.addVertexWithUV(0.0, 1.0, 1.0, (double) icon.getMinU(), (double) icon.getMaxV());*/
		
		float full = 1.0f / 2;
		
		tess.addVertexWithUV(full, 0.0, 0.0, (double) icon.getMaxU(), (double) icon.getMaxV());
		tess.addVertexWithUV(full, full, 0.0, (double) icon.getMaxU(), (double) icon.getMinV());
		tess.addVertexWithUV(full, full, full, (double) icon.getMinU(), (double) icon.getMinV());
		tess.addVertexWithUV(full, 0.0, full, (double) icon.getMinU(), (double) icon.getMaxV()); 
		
		tess.addVertexWithUV(0.0, 0.0, full, (double) icon.getMaxU(), (double) icon.getMaxV());
		tess.addVertexWithUV(0.0, full, full, (double) icon.getMaxU(), (double) icon.getMinV());
		tess.addVertexWithUV(0.0, full, 0.0, (double) icon.getMinU(), (double) icon.getMinV());
		tess.addVertexWithUV(0.0, 0.0, 0.0, (double) icon.getMinU(), (double) icon.getMaxV());
		
		/*tess.addVertexWithUV(full, full, full, (double) icon.getMaxU(), (double) icon.getMaxV());
		tess.addVertexWithUV(full, full, 0.0, (double) icon.getMaxU(), (double) icon.getMinV());
		tess.addVertexWithUV(0.0, full, 0.0, (double) icon.getMinU(), (double) icon.getMinV());
		tess.addVertexWithUV(0.0, full, full, (double) icon.getMinU(), (double) icon.getMaxV());*/

		tess.draw();

		GL11.glRotatef(-45, 1, 0, 0);
		if (mustTranslate) GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		// tess.draw();
	}

}

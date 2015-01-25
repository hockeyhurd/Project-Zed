package com.projectzed.mod.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.projectzed.mod.proxy.ClientProxy;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Renderer code for thickenedGlass.
 * 
 * @author hockeyhurd
 * @version Jan 25, 2015
 */
@SideOnly(Side.CLIENT)
public class ThickenedGlassRenderer implements ISimpleBlockRenderingHandler {

	private Tessellator tess;
	private IIcon icon;
	private double xx, yy, zz, offset;
	private double minU, maxU, minV, maxV;
	
	public ThickenedGlassRenderer() {
	}

	/*
	 * (non-Javadoc)
	 * @see cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#
	 * renderInventoryBlock(net.minecraft.block.Block, int, int,
	 * net.minecraft.client.renderer.RenderBlocks)
	 */
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#renderWorldBlock
	 * (net.minecraft.world.IBlockAccess, int, int, int,
	 * net.minecraft.block.Block, int,
	 * net.minecraft.client.renderer.RenderBlocks)
	 */
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

		xx = x;
		yy = y;
		zz = z;
		offset = 0.01f;

		if (icon == null) icon = block.getBlockTextureFromSide(0);

		minU = icon.getMinU();
		maxU = icon.getMaxU();
		minV = icon.getMinV();
		maxV = icon.getMaxV();

		if (tess == null) tess = Tessellator.instance;
		tess.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0 % 65536, 0xf0 / 65536);

		tess.setNormal(0.0f, 0.0f, -1.0f);
		// z-
		tess.addVertexWithUV(xx + 1, yy + 1, zz, maxU, minV);
		tess.addVertexWithUV(xx + 1, yy, zz, maxU, maxV);
		tess.addVertexWithUV(xx, yy, zz, minU, maxV);
		tess.addVertexWithUV(xx, yy + 1, zz, minU, minV);

		// z- (inside)
		tess.addVertexWithUV(xx, yy + 1, zz + offset, minU, minV);
		tess.addVertexWithUV(xx, yy, zz + offset, minU, maxV);
		tess.addVertexWithUV(xx + 1, yy, zz + offset, maxU, maxV);
		tess.addVertexWithUV(xx + 1, yy + 1, zz + offset, maxU, minV);

		tess.setNormal(0.0f, 0.0f, 1.0f);
		// Z+
		tess.addVertexWithUV(xx, yy + 1, zz + 1, minU, minV);
		tess.addVertexWithUV(xx, yy, zz + 1, minU, maxV);
		tess.addVertexWithUV(xx + 1, yy, zz + 1, maxU, maxV);
		tess.addVertexWithUV(xx + 1, yy + 1, zz + 1, maxU, minV);

		// z+ (inside)
		tess.addVertexWithUV(xx + 1, yy + 1, zz + 1 - offset, maxU, minV);
		tess.addVertexWithUV(xx + 1, yy, zz + 1 - offset, maxU, maxV);
		tess.addVertexWithUV(xx, yy, zz + 1 - offset, minU, maxV);
		tess.addVertexWithUV(xx, yy + 1, zz + 1 - offset, minU, minV);

		tess.setNormal(-1.0f, 0.0f, 0.0f);
		// x-
		tess.addVertexWithUV(xx, yy + 1, zz, minU, minV);
		tess.addVertexWithUV(xx, yy, zz, minU, maxV);
		tess.addVertexWithUV(xx, yy, zz + 1, maxU, maxV);
		tess.addVertexWithUV(xx, yy + 1, zz + 1, maxU, minV);

		// x- (inside)
		tess.addVertexWithUV(xx + offset, yy + 1, zz + 1, maxU, minV);
		tess.addVertexWithUV(xx + offset, yy, zz + 1, maxU, maxV);
		tess.addVertexWithUV(xx + offset, yy, zz, minU, maxV);
		tess.addVertexWithUV(xx + offset, yy + 1, zz, minU, minV);

		tess.setNormal(1.0f, 0.0f, 0.0f);
		// x+
		tess.addVertexWithUV(xx + 1, yy + 1, zz + 1, maxU, minV);
		tess.addVertexWithUV(xx + 1, yy, zz + 1, maxU, maxV);
		tess.addVertexWithUV(xx + 1, yy, zz, minU, maxV);
		tess.addVertexWithUV(xx + 1, yy + 1, zz, minU, minV);

		// x+ (inside)
		tess.addVertexWithUV(xx + 1 - offset, yy + 1, zz, minU, minV);
		tess.addVertexWithUV(xx + 1 - offset, yy, zz, minU, maxV);
		tess.addVertexWithUV(xx + 1 - offset, yy, zz + 1, maxU, maxV);
		tess.addVertexWithUV(xx + 1 - offset, yy + 1, zz + 1, maxU, minV);

		tess.setNormal(0.0f, -1.0f, 0.0f);
		// y-
		tess.addVertexWithUV(xx + 1, yy, zz, maxU, minV);
		tess.addVertexWithUV(xx + 1, yy, zz + 1, maxU, maxV);
		tess.addVertexWithUV(xx, yy, zz + 1, minU, maxV);
		tess.addVertexWithUV(xx, yy, zz, minU, minV);

		// y- (inside)
		tess.addVertexWithUV(xx, yy + offset, zz, minU, minV);
		tess.addVertexWithUV(xx, yy + offset, zz + 1, minU, maxV);
		tess.addVertexWithUV(xx + 1, yy + offset, zz + 1, maxU, maxV);
		tess.addVertexWithUV(xx + 1, yy + offset, zz, maxU, minV);

		tess.setNormal(0.0f, 1.0f, 0.0f);
		// y+
		tess.addVertexWithUV(xx, yy + 1, zz, minU, minV);
		tess.addVertexWithUV(xx, yy + 1, zz + 1, minU, maxV);
		tess.addVertexWithUV(xx + 1, yy + 1, zz + 1, maxU, maxV);
		tess.addVertexWithUV(xx + 1, yy + 1, zz, maxU, minV);

		// y+ (inside)
		tess.addVertexWithUV(xx + 1, yy + 1 - offset, zz, maxU, minV);
		tess.addVertexWithUV(xx + 1, yy + 1 - offset, zz + 1, maxU, maxV);
		tess.addVertexWithUV(xx, yy + 1 - offset, zz + 1, minU, maxV);
		tess.addVertexWithUV(xx, yy + 1 - offset, zz, minU, minV);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#
	 * shouldRender3DInInventory(int)
	 */
	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#getRenderId()
	 */
	@Override
	public int getRenderId() {
		return ClientProxy.thickenedGlass;
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.renderer;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.hockeyhurd.api.math.Vector4Helper;
import com.hockeyhurd.api.util.TessellatorHelper;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityReactorGlass;
import com.projectzed.mod.util.Connection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Renderer code for tileentity nuclearReactorGlass.
 * 
 * @author hockeyhurd
 * @version Mar 6, 2015
 */
@SideOnly(Side.CLIENT)
public class ReactorGlassRenderer extends TileEntitySpecialRenderer {

	private TessellatorHelper tessHelp;
	private Vector4Helper<Float> minVec, maxVec;
	private final float pixel = 1f / 96f;
	private final float min = 0f, max = 16f * this.pixel;
	private Connection[] connections = new Connection[ForgeDirection.VALID_DIRECTIONS.length];
	
	private final ResourceLocation texture;
	
	public ReactorGlassRenderer() {
		tessHelp = new TessellatorHelper(null);
		minVec = Vector4Helper.zero.getVector4f();
		maxVec = new Vector4Helper<Float>(1f, 1f, 1f);
		
		texture = new ResourceLocation(ProjectZed.modID.toLowerCase(), "textures/blocks/nuclearReactorGlass.png");
		
		for (int i = 0; i < connections.length; i++) {
			connections[i] = new Connection(false, 0);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer#renderTileEntityAt(net.minecraft.tileentity.TileEntity, double, double, double, float)
	 */
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
		if (te instanceof TileEntityReactorGlass) {
			GL11.glPushMatrix();
			GL11.glTranslated(x, y, z);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);

			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0xf0 % 65536, 0xf0 / 65536);
			this.bindTexture(texture);
			
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				connections[dir.ordinal()] = getConnection((TileEntityReactorGlass) te, te.xCoord + dir.offsetX, te.yCoord + dir.offsetY, te.zCoord + dir.offsetZ);
				// if (connections[dir.ordinal()].isConnected()) ProjectZed.logHelper.info(dir.name());
			}
			
			renderCube((TileEntityReactorGlass) te, x, y, z);
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glTranslated(-x, -y, -z);
			GL11.glPopMatrix();
		}
	}
	
	private Connection getConnection(TileEntityReactorGlass te, int x, int y, int z) {
		Connection con = null;
		
		if (te != null && te.getWorldObj() != null) {
			
			if (te.getWorldObj().getTileEntity(x, y, z) instanceof TileEntityReactorGlass) con = new Connection(true, 1);
			else con = new Connection(false, 0);
			
		}
		
		return con;
	}

	private void renderCube(TileEntityReactorGlass te, double x, double y, double z) {
		if (te == null || te.getWorldObj() == null) return;

		float difU = 0f * this.pixel;
		float difV = 16f * this.pixel;
		
		tessHelp.tess.startDrawingQuads();
		
		// tessHelp.drawYNeg(minVec, maxVec, min, max, difU, difV, false);
		// tessHelp.drawYPos(minVec, maxVec, min, max, difU, difV, false);
		// tessHelp.drawZNeg(minVec, maxVec, min, max, difU, difV, false);
		// tessHelp.drawZPos(minVec, maxVec, min, max, difU, difV, false);
		
		// tessHelp.drawXNeg(minVec, maxVec, min, max, difU, difV, false);
		// tessHelp.drawXPos(minVec, maxVec, min, max, difU, difV, false);
		
		drawZNeg();
		drawZPos();
		
		// drawXNeg();
		// drawXPos();
		
		tessHelp.tess.draw();
	}

	private void drawZNeg() {
		float difU = 0f * this.pixel;
		float difV = 32f * this.pixel;
		// ProjectZed.logHelper.info(connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected());
		
		if (!connections[ForgeDirection.DOWN.ordinal()].isConnected() && !connections[ForgeDirection.UP.ordinal()].isConnected()) {
			if (connections[ForgeDirection.WEST.ordinal()].isConnected() && !connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected()) {
				difU = 0f * this.pixel;
				difV = 0f * this.pixel;
			}
	
			else if (!connections[ForgeDirection.WEST.ordinal()].isConnected() && connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected()) {
				difU = 80f * this.pixel;
				difV = 0f * this.pixel;
			}
	
			else if (connections[ForgeDirection.WEST.ordinal()].isConnected() && connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected()) {
				difU = 16f * this.pixel;
				difV = 80f * this.pixel;
			}
		}
		
		else if (!connections[ForgeDirection.DOWN.ordinal()].isConnected() && connections[ForgeDirection.UP.ordinal()].isConnected()) {
			if (connections[ForgeDirection.WEST.ordinal()].isConnected() && !connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected()) {
				difU = 64f * this.pixel;
				difV = 80f * this.pixel;
			}
	
			else if (!connections[ForgeDirection.WEST.ordinal()].isConnected() && connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected()) {
				difU = 48f * this.pixel;
				difV = 80f * this.pixel;
			}
	
			else if (connections[ForgeDirection.WEST.ordinal()].isConnected() && connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected()) {
				difU = 64f * this.pixel;
				difV = 48f * this.pixel;
			}
		}
		
		else if (connections[ForgeDirection.DOWN.ordinal()].isConnected() && !connections[ForgeDirection.UP.ordinal()].isConnected()) {
			if (connections[ForgeDirection.WEST.ordinal()].isConnected() && !connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected()) {
				difU = 80f * this.pixel;
				difV = 80f * this.pixel;
			}
	
			else if (!connections[ForgeDirection.WEST.ordinal()].isConnected() && connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected()) {
				difU = 0f * this.pixel;
				difV = 80f * this.pixel;
			}
	
			else if (connections[ForgeDirection.WEST.ordinal()].isConnected() && connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected()) {
				difU = 0f * this.pixel;
				difV = 48f * this.pixel;
			}
			
			else {
				difU = 64f * this.pixel;
				difV = 96f * this.pixel;
			}
		}
		
		else if (connections[ForgeDirection.DOWN.ordinal()].isConnected() && connections[ForgeDirection.UP.ordinal()].isConnected()) {
			if (connections[ForgeDirection.WEST.ordinal()].isConnected() && !connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected()) {
				difU = 80f * this.pixel;
				difV = 48f * this.pixel;
			}
	
			else if (!connections[ForgeDirection.WEST.ordinal()].isConnected() && connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected()) {
				difU = 48f * this.pixel;
				difV = 48f * this.pixel;
			}
	
			else if (connections[ForgeDirection.WEST.ordinal()].isConnected() && connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected()) {
				difU = 80f * this.pixel;
				difV = 32f * this.pixel; 
			}
			
			else {
				difU = 64f * this.pixel;
				difV = 96f * this.pixel;
			}
		}
		
		tessHelp.drawZNeg(minVec, maxVec, min, max, difU, difV, false);
	}
	
	private void drawZPos() {
		float difU = 0f * this.pixel;
		float difV = 32f * this.pixel;
		// ProjectZed.logHelper.info(connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected());
		
		if (!connections[ForgeDirection.DOWN.ordinal()].isConnected() && !connections[ForgeDirection.UP.ordinal()].isConnected()) {
			if (connections[ForgeDirection.EAST.ordinal()].isConnected() && !connections[ForgeDirection.EAST.getOpposite().ordinal()].isConnected()) {
				difU = 0f * this.pixel;
				difV = 0f * this.pixel;
			}
	
			else if (!connections[ForgeDirection.EAST.ordinal()].isConnected() && connections[ForgeDirection.EAST.getOpposite().ordinal()].isConnected()) {
				difU = 80f * this.pixel;
				difV = 0f * this.pixel;
			}
	
			else if (connections[ForgeDirection.EAST.ordinal()].isConnected() && connections[ForgeDirection.EAST.getOpposite().ordinal()].isConnected()) {
				difU = 16f * this.pixel;
				difV = 80f * this.pixel;
			}
		}
		
		else if (!connections[ForgeDirection.DOWN.ordinal()].isConnected() && connections[ForgeDirection.UP.ordinal()].isConnected()) {
			if (connections[ForgeDirection.EAST.ordinal()].isConnected() && !connections[ForgeDirection.EAST.getOpposite().ordinal()].isConnected()) {
				difU = 64f * this.pixel;
				difV = 80f * this.pixel;
			}
	
			else if (!connections[ForgeDirection.EAST.ordinal()].isConnected() && connections[ForgeDirection.EAST.getOpposite().ordinal()].isConnected()) {
				difU = 48f * this.pixel;
				difV = 80f * this.pixel;
			}
	
			else if (connections[ForgeDirection.EAST.ordinal()].isConnected() && connections[ForgeDirection.EAST.getOpposite().ordinal()].isConnected()) {
				difU = 64f * this.pixel;
				difV = 48f * this.pixel;
			}
		}
		
		else if (connections[ForgeDirection.DOWN.ordinal()].isConnected() && !connections[ForgeDirection.UP.ordinal()].isConnected()) {
			if (connections[ForgeDirection.EAST.ordinal()].isConnected() && !connections[ForgeDirection.EAST.getOpposite().ordinal()].isConnected()) {
				difU = 80f * this.pixel;
				difV = 80f * this.pixel;
			}
	
			else if (!connections[ForgeDirection.EAST.ordinal()].isConnected() && connections[ForgeDirection.EAST.getOpposite().ordinal()].isConnected()) {
				difU = 0f * this.pixel;
				difV = 80f * this.pixel;
			}
	
			else if (connections[ForgeDirection.EAST.ordinal()].isConnected() && connections[ForgeDirection.EAST.getOpposite().ordinal()].isConnected()) {
				difU = 0f * this.pixel;
				difV = 48f * this.pixel;
			}
			
			else {
				difU = 64f * this.pixel;
				difV = 96f * this.pixel;
			}
		}
		
		else if (connections[ForgeDirection.DOWN.ordinal()].isConnected() && connections[ForgeDirection.UP.ordinal()].isConnected()) {
			if (connections[ForgeDirection.EAST.ordinal()].isConnected() && !connections[ForgeDirection.EAST.getOpposite().ordinal()].isConnected()) {
				difU = 80f * this.pixel;
				difV = 48f * this.pixel;
			}
	
			else if (!connections[ForgeDirection.EAST.ordinal()].isConnected() && connections[ForgeDirection.EAST.getOpposite().ordinal()].isConnected()) {
				difU = 48f * this.pixel;
				difV = 48f * this.pixel;
			}
	
			else if (connections[ForgeDirection.EAST.ordinal()].isConnected() && connections[ForgeDirection.EAST.getOpposite().ordinal()].isConnected()) {
				difU = 80f * this.pixel;
				difV = 32f * this.pixel; 
			}
			
			else {
				difU = 64f * this.pixel;
				difV = 96f * this.pixel;
			}
		}
		
		tessHelp.drawZPos(minVec, maxVec, min, max, difU, difV, false);
	}
	
	private void drawXNeg() {
		float difU;
		float difV;
		// ProjectZed.logHelper.info(connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected());
		if (connections[ForgeDirection.WEST.ordinal()].isConnected() && !connections[ForgeDirection.WEST.getOpposite().ordinal()].isConnected()) {
			// ProjectZed.logHelper.info(true);
			difU = 0f * this.pixel;
			difV = 0f * this.pixel;
		}
		
		else {
			difU = 0f * this.pixel;
			difV = 16f * this.pixel;
		}
		
		tessHelp.drawXNeg(minVec, maxVec, min, max, difU, difV, false);
	}
	
	private void drawXPos() {
		float difU;
		float difV;
		if (connections[ForgeDirection.EAST.ordinal()].isConnected() && !connections[ForgeDirection.EAST.getOpposite().ordinal()].isConnected()) {
			difU = 48f * this.pixel;
			difV = 0f * this.pixel;
		}
		
		else {
			difU = 0f * this.pixel;
			difV = 16f * this.pixel;
		}
		
		tessHelp.drawXPos(minVec, maxVec, min, max, difU, difV, false);
	}
	
}

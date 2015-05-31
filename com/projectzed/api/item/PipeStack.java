/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.item;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.hockeyhurd.api.math.Vector3;
import com.hockeyhurd.api.util.TessellatorHelper;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class for controlling and handling custom ItemStacks used in transfer pipes.
 * <br>Class derived from BluePower mod (see reference below).
 * @see <a href = https://github.com/Qmunity/BluePower/blob/master/src/main/java/com/bluepowermod/part/tube/TubeStack.java>TubeStack.class</a>
 * 
 * @author hockeyhurd
 * @version May 25, 2015
 */
public class PipeStack {

	public ItemStack stack;
	public final EnumColor color;
	public double progress; // 0 at the start, 0.5 on an intersection, 1 at the end.
	public double oldProgress;
	public ForgeDirection heading;
	public boolean enabled = true; // will be disabled when the client sided stack is at an intersection, at which point it needs to wait for server
    // input. This just serves a visual purpose.
	public int idleCounter; // increased when the stack is standing still. This will cause the client to remove the stack when a timeout occurs.
	private TileEntity target;
	private Vector3<Integer> targetVec;
	private ForgeDirection targetEntryDir = ForgeDirection.UNKNOWN;
	public static final double ITEM_SPEED = 0.0625d;
	private double speed = ITEM_SPEED;
	public static double tickTimeMultiplier = 1d; // Used client side to correct for TPS lag. This is being synchronized from the server.
	
	@SideOnly(Side.CLIENT)
	private static RenderItem customRenderItem;
	private static EntityItem renderedItem;
	private static TessellatorHelper tess;
	private Vector3<Float> minDrawVec = Vector3.zero.getVector3f();
	private Vector3<Float> maxDrawVec = Vector3.zero.getVector3f();
	
	public static RenderMode renderMode;
	
	public static enum RenderMode {
		AUTO, NORMAL, REDUCED, NONE
	}
	
	public PipeStack(ItemStack stack, ForgeDirection from) {
		this(stack, from, EnumColor.GREEN);
	}
	
	public PipeStack(ItemStack stack, ForgeDirection from, EnumColor color) {
		this.stack = stack;
		this.heading = from;
		this.color = color;
	}
	
	public void setSpeed(double speed) {
		// prevent negative speed.
		this.speed = Math.abs(speed);
	}
	
	public boolean update(World world) {
		this.oldProgress = this.progress;
		
		if (this.enabled) {
			boolean isEntering = this.progress < 0.5d;
			this.progress += this.speed * (world.isRemote ? this.tickTimeMultiplier : 1);
			return this.progress >= 0.5d && isEntering;
		}
		
		else {
			this.idleCounter--;
			return false;
		}
	}
	
	public TileEntity getTarget(World world) {
		if (target == null && (targetVec.x != 0 || targetVec.y != 0 || targetVec.z != 0)) target = world.getTileEntity(targetVec.x, targetVec.y, targetVec.z);
		return target;
	}
	
	public ForgeDirection getTargetEntryDir() {
		return this.targetEntryDir;
	}
	
	public void setTarget(TileEntity te, ForgeDirection targetEntryDir) {
		this.target = te;
		this.targetEntryDir = targetEntryDir;
		
		if (target != null) {
			if (targetVec == null) targetVec = Vector3.zero.getVector3i();
			
			targetVec.x = target.xCoord;
			targetVec.y = target.yCoord;
			targetVec.z = target.zCoord;
		}
		
		else targetVec = Vector3.zero.getVector3i();
	}

	public PipeStack copy() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return loadFromNBT(tag);
	}
	
	public void writeToNBT(NBTTagCompound comp) {
		stack.writeToNBT(comp);
		comp.setByte("PipeColor", (byte) color.getID());
		comp.setByte("PipeHeading", (byte) heading.ordinal());
		comp.setDouble("PipeProgress", progress);
		comp.setDouble("PipeSpeed", speed);
		
		if (targetVec == null) targetVec = Vector3.zero.getVector3i();
		comp.setInteger("TargetVecX", targetVec.x);
		comp.setInteger("TargetVecY", targetVec.y);
		comp.setInteger("TargetVecZ", targetVec.z);
		comp.setByte("TargetEntryDir", (byte) targetEntryDir.ordinal());
	}
	
	public static PipeStack loadFromNBT(NBTTagCompound comp) {
		PipeStack stack = new PipeStack(ItemStack.loadItemStackFromNBT(comp), ForgeDirection.getOrientation(comp.getByte("PipeHeading")),
				EnumColor.values()[comp.getByte("PipeColor")]);
		
		stack.progress = comp.getDouble("PipeProgress");
		stack.speed = comp.getDouble("PipeSpeed");
		stack.targetVec = new Vector3(comp.getInteger("TargetVecX"), comp.getInteger("TargetVecY"), comp.getInteger("TargetVecZ"));
		stack.targetEntryDir = ForgeDirection.getOrientation(comp.getByte("TargetEntryDir"));
		
		return stack;
	}
	
	public void writeToPacket(ByteBuf buf) {
		ByteBufUtils.writeItemStack(buf, stack);
		buf.writeByte(heading.ordinal());
		buf.writeByte((byte) color.ordinal());
		buf.writeDouble(speed);
		buf.writeDouble(progress);
	}
	
	public static PipeStack loadFromPacket(ByteBuf buf) {
		PipeStack stack = new PipeStack(ByteBufUtils.readItemStack(buf), ForgeDirection.getOrientation(buf.readByte()),
				EnumColor.values()[buf.readByte()]);
		
		stack.speed = buf.readDouble();
		stack.progress = buf.readDouble();
		
		return stack;
	}
	
	@SideOnly(Side.CLIENT)
	public void render(float partialTick) {

		if (renderMode == RenderMode.AUTO) renderMode = Minecraft.getMinecraft().gameSettings.fancyGraphics ? RenderMode.NORMAL : RenderMode.REDUCED;
		final RenderMode finalRenderMode = renderMode;

		if (customRenderItem == null) {
			customRenderItem = new RenderItem() {

				@Override
				public boolean shouldBob() {

					return false;
				};

				@Override
				public byte getMiniBlockCount(ItemStack stack, byte original) {

					return finalRenderMode == RenderMode.REDUCED ? (byte) 1 : original;
				}
			};

			customRenderItem.setRenderManager(RenderManager.instance);

			renderedItem = new EntityItem(FMLClientHandler.instance().getWorldClient());
			renderedItem.hoverStart = 0.0F;
		}

		renderedItem.setEntityItemStack(stack);

		double renderProgress = (oldProgress + (progress - oldProgress) * partialTick) * 2 - 1;

		GL11.glPushMatrix();
		GL11.glTranslated(heading.offsetX * renderProgress * 0.5, heading.offsetY * renderProgress * 0.5, heading.offsetZ * renderProgress * 0.5);
		
		if (finalRenderMode != RenderMode.NONE) {
			GL11.glPushMatrix();

			if (stack.stackSize > 5) GL11.glScaled(0.8, 0.8, 0.8);
			if (!(stack.getItem() instanceof ItemBlock)) {
				GL11.glScaled(0.8, 0.8, 0.8);
				GL11.glTranslated(0, -0.15, 0);
			}

			customRenderItem.doRender(renderedItem, 0, 0, 0, 0, 0);
			GL11.glPopMatrix();
		}
		
		else {
			float size = 0.02F;
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_QUADS);
			
			if (tess == null) tess = new TessellatorHelper(null);
			// RenderHelper.drawColoredCube(new Vec3dCube(-size, -size, -size, size, size, size), 1, 1, 1, 1);

			minDrawVec.x = -size;
			minDrawVec.y = -size;
			minDrawVec.z = -size;
			
			maxDrawVec.x = size;
			maxDrawVec.y = size;
			maxDrawVec.z = size;
			
			tess.drawXNeg(minDrawVec, maxDrawVec, false);
			tess.drawXPos(minDrawVec, maxDrawVec, false);
			
			tess.drawYNeg(minDrawVec, maxDrawVec, false);
			tess.drawYPos(minDrawVec, maxDrawVec, false);
			
			tess.drawZNeg(minDrawVec, maxDrawVec, false);
			tess.drawZPos(minDrawVec, maxDrawVec, false);
			
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}


		float size = 0.2F;

		int colorInt = ItemDye.field_150922_c[color.ordinal()];
		float red = (colorInt >> 0xf) / 256f;
		float green = (colorInt >> 0x8 & 0xff) / 256f;
		float blue = (colorInt & 0xff) / 256f;

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor3f(red, green, blue);

		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ProjectZed.modID, "textures/blocks/tubes/inside_color_border.png"));
		
		minDrawVec.x = -size;
		minDrawVec.y = -size;
		minDrawVec.z = -size;
		
		maxDrawVec.x = size;
		maxDrawVec.y = size;
		maxDrawVec.z = size;
		
		tess.drawXNeg(minDrawVec, maxDrawVec, false);
		tess.drawXPos(minDrawVec, maxDrawVec, false);
		
		tess.drawYNeg(minDrawVec, maxDrawVec, false);
		tess.drawYPos(minDrawVec, maxDrawVec, false);
		
		tess.drawZNeg(minDrawVec, maxDrawVec, false);
		tess.drawZPos(minDrawVec, maxDrawVec, false);

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);

		GL11.glPopMatrix();
	}
	
}

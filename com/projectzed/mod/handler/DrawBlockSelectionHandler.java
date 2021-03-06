/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.handler;

import com.hockeyhurd.hcorelib.api.math.Color4f;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.IItemAdjustableRadii;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Rendering class for selection box quads.
 *
 * @author hockeyhurd
 * @version 3/28/2016.
 */
@SideOnly(Side.CLIENT)
public class DrawBlockSelectionHandler {

	private float pulse = 0.5f;
	private boolean increasing = true;
	private static Color4f selectionBoxColor; //  = ProjectZed.configHandler.getSelectionBoxColor();

	@SubscribeEvent
	public void onDrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
		final ItemStack stack = event.getPlayer().inventory.getCurrentItem();
		if (stack != null && event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK) {

			if (stack.getItem() instanceof IItemAdjustableRadii)
				drawSelectionBoxQuad(event.getContext(), event.getPlayer(), event.getTarget(), 0, event.getPartialTicks());
		}
	}

	/**
	 * Draws selection box quad.
	 *
	 * @param context Render context.
	 * @param player EntityPlayer.
	 * @param rayTrace Ray trace object.
	 * @param x int.
	 * @param partialTicks float partial ticks.
	 */
	private void drawSelectionBoxQuad(RenderGlobal context, EntityPlayer player, RayTraceResult rayTrace, int x, float partialTicks) {
		if (x == 0 && context != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
			selectionBoxColor = ProjectZed.configHandler.getSelectionBoxColor();

			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);

			GL11.glColor4f(1.0f, 1.0f, 1.0f, getNextPulse());

			GL11.glLineWidth(5.0f);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);

			final float offsetY = 0.002f;
			// final int radii = ((IItemAdjustableRadii) player.inventory.getCurrentItem().getItem()).getRadii();
			final int radii = getRadiiFromPlayer(player);
			double xp, yp, zp;

			IBlockState currentBlock;
			BlockPos blockPos;

			for (int i = -radii; i <= radii; i++) {
				for (int j = -radii; j <= radii; j++) {
					// currentBlock = player.worldObj.getBlock(rayTrace.blockX, rayTrace.blockY, rayTrace.blockZ);

					if (rayTrace.sideHit == EnumFacing.DOWN || rayTrace.sideHit == EnumFacing.UP) {
						blockPos = BlockUtils.createBlockPos(rayTrace.getBlockPos().getX() + i,
								rayTrace.getBlockPos().getY(), rayTrace.getBlockPos().getZ() + j);
						currentBlock = BlockUtils.getBlock(player.worldObj, blockPos);
						if (currentBlock.getBlock().getMaterial(currentBlock) == Material.AIR) continue;

						// currentBlock.getBlock().setBlockBoundsBasedOnState(player.worldObj, rayTrace.blockX + i, rayTrace.blockY, rayTrace.blockZ + j);
						AxisAlignedBB boundingBox = new AxisAlignedBB(blockPos);

						xp = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
						yp = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
						zp = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

						// RenderGlobal.drawOutlinedBoundingBox(boundingBox.expand((double) offsetY, (double) offsetY, (double) offsetY).offset(-xp, -yp, -zp));
						RenderGlobal.drawSelectionBoundingBox(boundingBox.expand((double) offsetY, (double) offsetY, (double) offsetY).offset(-xp, -yp, -zp),
								selectionBoxColor.getR(), selectionBoxColor.getG(), selectionBoxColor.getB(), getNextPulse());
						// RenderGlobal.drawSelectionBoundingBox(
						//		boundingBox.expand((double) offsetY, (double) offsetY, (double) offsetY).offset(-xp, -yp, -zp));
					}

					else if (rayTrace.sideHit == EnumFacing.NORTH || rayTrace.sideHit == EnumFacing.SOUTH) {
						blockPos = BlockUtils
								.createBlockPos(rayTrace.getBlockPos().getX() + i, rayTrace.getBlockPos().getY() + j, rayTrace.getBlockPos().getZ());
						currentBlock = BlockUtils.getBlock(player.worldObj, blockPos);
						if (currentBlock.getMaterial() == Material.AIR) continue;

						// currentBlock.setBlockBoundsBasedOnState(player.worldObj, rayTrace.blockX + i, rayTrace.blockY + j, rayTrace.blockZ);
						AxisAlignedBB boundingBox = new AxisAlignedBB(blockPos);

						xp = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
						yp = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
						zp = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

						/*RenderGlobal.drawOutlinedBoundingBox(
								currentBlock.getSelectedBoundingBoxFromPool(player.worldObj, rayTrace.blockX + i, rayTrace.blockY + j, rayTrace.blockZ)
										.expand((double) offsetY, (double) offsetY, (double) offsetY).getOffsetBoundingBox(-xp, -yp, -zp), -1);*/

						RenderGlobal.drawSelectionBoundingBox(boundingBox.expand((double) offsetY, (double) offsetY, (double) offsetY).offset(-xp, -yp, -zp),
								selectionBoxColor.getR(), selectionBoxColor.getG(), selectionBoxColor.getB(), getNextPulse());
						// RenderGlobal.drawSelectionBoundingBox(
						// 		boundingBox.expand((double) offsetY, (double) offsetY, (double) offsetY).offset(-xp, -yp, -zp));
					}

					else {
						blockPos = BlockUtils
								.createBlockPos(rayTrace.getBlockPos().getX(), rayTrace.getBlockPos().getY() + j, rayTrace.getBlockPos().getZ() + i);
						currentBlock = BlockUtils.getBlock(player.worldObj, blockPos);
						if (currentBlock.getMaterial() == Material.AIR) continue;

						// currentBlock.setBlockBoundsBasedOnState(player.worldObj, rayTrace.blockX, rayTrace.blockY + j, rayTrace.blockZ + i);
						AxisAlignedBB boundingBox = new AxisAlignedBB(blockPos);

						xp = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
						yp = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
						zp = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

						/*RenderGlobal.drawOutlinedBoundingBox(
								currentBlock.getSelectedBoundingBoxFromPool(player.worldObj, rayTrace.blockX, rayTrace.blockY + j, rayTrace.blockZ + i)
										.expand((double) offsetY, (double) offsetY, (double) offsetY).getOffsetBoundingBox(-xp, -yp, -zp), -1);*/

						RenderGlobal.drawSelectionBoundingBox(boundingBox.expand((double) offsetY, (double) offsetY, (double) offsetY).offset(-xp, -yp, -zp),
								selectionBoxColor.getR(), selectionBoxColor.getG(), selectionBoxColor.getB(), getNextPulse());

						// RenderGlobal.drawSelectionBoundingBox(
						//		boundingBox.expand((double) offsetY, (double) offsetY, (double) offsetY).offset(-xp, -yp, -zp));
					}
				}
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
		}
	}

	/**
	 * Adjusts and returns new pulse value.
	 *
	 * @return float.
	 */
	private float getNextPulse() {
		if (pulse >= 0.75f) increasing = false;
		else if (pulse <= 0.25f) increasing = true;

		pulse += increasing ? (0.0015625f) : (-0.0015625f);

		return pulse;
	}

	/**
	 * Gets the radius from either item in player's hands.
	 *
	 * @param player EntityPlayer to check from.
	 * @return int radii result.
	 */
	private static int getRadiiFromPlayer(EntityPlayer player) {
		if (player == null) return 1;

		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

		if (stack.getItem() instanceof IItemAdjustableRadii)
			return ((IItemAdjustableRadii) stack.getItem()).getRadii();

		stack = player.getHeldItem(EnumHand.OFF_HAND);

		if (stack.getItem() instanceof IItemAdjustableRadii)
			return ((IItemAdjustableRadii) stack.getItem()).getRadii();

		return 1;
	}

}

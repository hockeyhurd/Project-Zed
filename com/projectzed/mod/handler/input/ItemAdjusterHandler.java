/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.handler.input;

import com.hockeyhurd.hcorelib.api.handler.input.AbstractKeyBinding;
import com.projectzed.api.util.SidedInfo;
import com.projectzed.mod.item.IItemAdjustable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Input handler class for adjustable items.
 *
 * @author hockeyhurd
 * @version 3/27/2016.
 */
@SideOnly(Side.CLIENT)
public abstract class ItemAdjusterHandler extends AbstractKeyBinding {

	/**
	 * @param name     Name of keybinding.
	 * @param keyCode  Key code for key.
	 * @param category Category to be placed in.
	 */
	public ItemAdjusterHandler(String name, int keyCode, String category) {
		super(name, keyCode, category);
	}

	public static class ItemAdjusterIncrementorHandler extends ItemAdjusterHandler {

		/**
		 * @param name     Name of keybinding.
		 * @param keyCode  Key code for key.
		 * @param category Category to be placed in.
		 */
		public ItemAdjusterIncrementorHandler(String name, int keyCode, String category) {
			super(name, keyCode, category);
		}

		@Override
		protected void activate(InputEvent.KeyInputEvent event) {
			EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();

			// ItemStack current = player.getCurrentEquippedItem();
			/*ItemStack current = player.getHeldItem(player.swingingHand);

			if (current != null) {
				Item item = current.getItem();

				if (item instanceof IItemAdjustable) ((IItemAdjustable) item).increment(player, current);
			}*/

			ItemStack current = player.getHeldItem(EnumHand.MAIN_HAND);

			if (current != null) {
				if (current.getItem() instanceof IItemAdjustable) {
					((IItemAdjustable) current.getItem()).increment(player, current);
					((IItemAdjustable) current.getItem()).sendPacket(current, new SidedInfo(Side.SERVER),
							((IItemAdjustable) current.getItem()).getData());
				}
			}

			else {
				current = player.getHeldItem(EnumHand.OFF_HAND);

				if (current != null && current.getItem() instanceof IItemAdjustable) {
					((IItemAdjustable) current.getItem()).increment(player, current);
					((IItemAdjustable) current.getItem()).sendPacket(current, new SidedInfo(Side.SERVER),
							((IItemAdjustable) current.getItem()).getData());
				}
			}
		}

	}

	public static class ItemAdjusterDecrementorHandler extends ItemAdjusterHandler {

		/**
		 * @param name     Name of keybinding.
		 * @param keyCode  Key code for key.
		 * @param category Category to be placed in.
		 */
		public ItemAdjusterDecrementorHandler(String name, int keyCode, String category) {
			super(name, keyCode, category);
		}

		@Override
		protected void activate(InputEvent.KeyInputEvent event) {
			EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();

			// ItemStack current = player.getCurrentEquippedItem();
			/*ItemStack current = player.getHeldItem(player.swingingHand);

			if (current != null) {
				Item item = current.getItem();

				if (item instanceof IItemAdjustable) ((IItemAdjustable) item).decrement(player, current);
			}*/

			ItemStack current = player.getHeldItem(EnumHand.MAIN_HAND);

			if (current != null) {
				if (current.getItem() instanceof IItemAdjustable) {
					((IItemAdjustable) current.getItem()).decrement(player, current);
					((IItemAdjustable) current.getItem()).sendPacket(current, new SidedInfo(Side.SERVER),
							((IItemAdjustable) current.getItem()).getData());
				}
			}

			else {
				current = player.getHeldItem(EnumHand.OFF_HAND);

				if (current != null && current.getItem() instanceof IItemAdjustable) {
					((IItemAdjustable) current.getItem()).decrement(player, current);
					((IItemAdjustable) current.getItem()).sendPacket(current, new SidedInfo(Side.SERVER),
							((IItemAdjustable) current.getItem()).getData());
				}
			}
		}

	}

}

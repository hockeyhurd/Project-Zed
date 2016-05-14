/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.handler.message;

import com.projectzed.mod.item.IItemAdjustable;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Generic message handling for IItemAdjustable interfaced items.
 *
 * @author hockeyhurd
 * @version 3/24/2016.
 */
public class MessageItemAdjustable implements IMessage, IMessageHandler<MessageItemAdjustable, IMessage> {

	protected boolean increment;

	@Deprecated
	public MessageItemAdjustable() {
		this.increment = false;
	}

	/**
	 * @param increment boolean toggle whether to increment data or not.
	 */
	public MessageItemAdjustable(boolean increment) {
		this.increment = increment;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.increment = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(increment);
	}

	@Override
	public IMessage onMessage(MessageItemAdjustable message, MessageContext ctx) {
		final EntityPlayer player = ctx.getServerHandler().playerEntity;

		if (player != null) {
			final ItemStack stack = player.inventory.getCurrentItem();

			if (stack != null) {
				final Item item = stack.getItem();

				if (item instanceof IItemAdjustable) {
					if (message.increment) ((IItemAdjustable) item).increment(player, stack);
					else ((IItemAdjustable) item).decrement(player, stack);
				}

			}
		}

		return null;
	}

}

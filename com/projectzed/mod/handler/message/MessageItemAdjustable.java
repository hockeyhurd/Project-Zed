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
import net.minecraftforge.fml.common.network.ByteBufUtils;
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

	protected ItemStack stack;
	protected int data;

	@Deprecated
	public MessageItemAdjustable() {
		this.data = 0;
	}

	public MessageItemAdjustable(ItemStack stack, int data) {
		this.stack = stack;
		this.data = data;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.stack = ByteBufUtils.readItemStack(buf);
		this.data = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (stack != null) ByteBufUtils.writeItemStack(buf, stack);
		buf.writeInt(data);
	}

	@Override
	public IMessage onMessage(MessageItemAdjustable message, MessageContext ctx) {
		final EntityPlayer player = ctx.getServerHandler().playerEntity;

		if (player != null && message.stack != null) {

			if (message.stack != null) {
				final Item item = message.stack.getItem();

				if (item instanceof IItemAdjustable) {
					// (message.increment) ((IItemAdjustable) item).increment(player, message.stack);
					// else ((IItemAdjustable) item).decrement(player, message.stack);
					((IItemAdjustable) item).setData(message.stack, message.data);
				}

			}
		}

		return null;
	}

}

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

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.mod.container.ContainerPatternEncoder;
import com.projectzed.mod.tileentity.machine.TileEntityPatternEncoder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author hockeyhurd
 * @version 5/1/2016.
 */
public class MessageTileEntityPatternEncoder implements IMessage, IMessageHandler<MessageTileEntityPatternEncoder, IMessage> {

	public static final byte NOTHING = 0x0;
	public static final byte CLEAR = 0x2;
	public static final byte ENCODE = 0x4;

	private TileEntityPatternEncoder te;
	private Vector3<Integer> vec;
	private int numSlots;
	private ItemStack[] slots;
	private byte buttonHit;

	@Deprecated
	public MessageTileEntityPatternEncoder() {
	}

	public MessageTileEntityPatternEncoder(TileEntityPatternEncoder te) {
		this(te, NOTHING);
	}

	public MessageTileEntityPatternEncoder(TileEntityPatternEncoder te, byte buttonHit) {
		this.te = te;
		this.vec = te.worldVec();
		this.buttonHit = buttonHit;
		this.numSlots = te.getSizeInventory();
		this.slots = new ItemStack[numSlots];

		syncStacks();
	}

	private void syncStacks() {
		if (te != null && numSlots > 0) {
			if (slots == null) slots = new ItemStack[numSlots];

			for (int i = 0; i < slots.length; i++) {
				slots[i] = te.getStackInSlot(i);
			}
		}
	}

	private boolean isArrayValid() {
		return slots != null && slots.length > 0;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		if (vec == null) vec = new Vector3<Integer>();

		vec.x = buf.readInt();
		vec.y = buf.readInt();
		vec.z = buf.readInt();
		buttonHit = buf.readByte();
		numSlots = buf.readInt();
		slots = new ItemStack[numSlots];

		for (int i = 0; i < slots.length; i++) {
			slots[i] = ByteBufUtils.readItemStack(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(vec.x);
		buf.writeInt(vec.y);
		buf.writeInt(vec.z);
		buf.writeByte(buttonHit);
		buf.writeInt(numSlots);

		if (!isArrayValid()) syncStacks();
		for (int i = 0; i < slots.length; i++) {
			ByteBufUtils.writeItemStack(buf, slots[i]);
		}
	}

	@Override
	public IMessage onMessage(MessageTileEntityPatternEncoder message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			TileEntity tileEntity = world.getTileEntity(VectorHelper.toBlockPos(message.vec));

			if (tileEntity != null && te instanceof TileEntityPatternEncoder) {
				TileEntityPatternEncoder te = (TileEntityPatternEncoder) tileEntity;

				if (message.slots != null && message.slots.length > 0) {
					EntityPlayer player = ctx.getServerHandler().playerEntity;
					ContainerPatternEncoder cont = null;

					if (player != null && player.openContainer != null && player.openContainer instanceof ContainerPatternEncoder) {
						cont = (ContainerPatternEncoder) player.openContainer;

						if (message.buttonHit == CLEAR) {
							cont.clearSlots();
							cont.onCraftMatrixChanged(cont.craftMatrix);
						}

						else if (message.buttonHit == ENCODE) te.setEncode(true);
					}

					for (int i = 0; i < message.slots.length; i++) {
						te.setInventorySlotContents(i, message.slots[i]);
					}

					if (cont != null) cont.onCraftMatrixChanged(cont.craftMatrix);

					te.markDirty();
				}
			}
		}

		else if (ctx.side == Side.CLIENT) {
			TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(VectorHelper.toBlockPos(message.vec));

			if (tileEntity != null && tileEntity instanceof TileEntityPatternEncoder) {
				TileEntityPatternEncoder te = (TileEntityPatternEncoder) tileEntity;

				if (message.slots != null && message.slots.length > 0) {
					for (int i = 0; i < message.slots.length; i++) {
						te.setInventorySlotContents(i, message.slots[i]);
					}
				}

				te.markDirty();
			}
		}

		return null;
	}

}

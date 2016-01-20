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

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.mod.tileentity.container.TileEntityNuclearIOPort;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

/**
 * Nuclear IO Port message handler.
 *
 * @author hockeyhurd
 * @version 1/19/2016.
 */
public class MessageTileEntityNuclearIOPort implements IMessage, IMessageHandler<MessageTileEntityNuclearIOPort, IMessage> {

	private final TileEntityNuclearIOPort te;
	private final Vector3<Integer> vec;
	private int burnTime;

	@Deprecated
	public MessageTileEntityNuclearIOPort() {
		this.te = null;
		this.vec = new Vector3<Integer>();
	}

	public MessageTileEntityNuclearIOPort(TileEntityNuclearIOPort te) {
		this.te = te;
		this.vec = te.worldVec();
		this.burnTime = te.getBurnTime();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		vec.x = buf.readInt();
		vec.y = buf.readInt();
		vec.z = buf.readInt();
		burnTime = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(vec.x);
		buf.writeInt(vec.y);
		buf.writeInt(vec.z);
		buf.writeInt(burnTime);
	}

	@Override
	public IMessage onMessage(MessageTileEntityNuclearIOPort message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.vec.x, message.vec.y, message.vec.z);

		if (te != null && te instanceof TileEntityNuclearIOPort) {
			((TileEntityNuclearIOPort) te).setBurnTime(message.burnTime);
		}

		return null;
	}
}

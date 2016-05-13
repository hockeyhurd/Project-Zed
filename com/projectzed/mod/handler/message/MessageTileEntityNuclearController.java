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
import com.projectzed.mod.tileentity.generator.TileEntityNuclearController;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author hockeyhurd
 * @version 1/21/2016.
 */
public class MessageTileEntityNuclearController implements IMessage, IMessageHandler<MessageTileEntityNuclearController, IMessage> {

	private final TileEntityNuclearController te;
	private Vector3<Integer> pos;
	private int burnTime;

	@Deprecated
	public MessageTileEntityNuclearController() {
		this.te = null;
		pos = new Vector3<Integer>();
	}

	public MessageTileEntityNuclearController(TileEntityNuclearController te) {
		this.te = te;
		burnTime = te.getBurnTime();
		pos = te.worldVec();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		burnTime = buf.readInt();

		if (pos == null) pos = new Vector3<Integer>();

		pos.x = buf.readInt();
		pos.y = buf.readInt();
		pos.z = buf.readInt();

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(burnTime);

		if (pos != null) {
			buf.writeInt(pos.x);
			buf.writeInt(pos.y);
			buf.writeInt(pos.z);
		}

	}

	@Override
	public IMessage onMessage(MessageTileEntityNuclearController message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(VectorHelper.toBlockPos(message.pos));

		if (te instanceof TileEntityNuclearController)
			((TileEntityNuclearController) te).setBurnTime(message.burnTime);

		return null;
	}

}

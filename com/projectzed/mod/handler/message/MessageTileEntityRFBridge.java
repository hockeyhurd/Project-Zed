/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.mod.tileentity.container.TileEntityRFBridge;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Class containing messaging code for rf bridge te.
 * 
 * @author hockeyhurd
 * @version Nov 29, 2014
 */
public class MessageTileEntityRFBridge implements IMessage, IMessageHandler<MessageTileEntityRFBridge, IMessage> {

	private TileEntityRFBridge te;
	private Vector3<Integer> vec;
	private int stored, storedRF;

	@Deprecated
	public MessageTileEntityRFBridge() {
	}
	
	public MessageTileEntityRFBridge(TileEntityRFBridge te) {
		this.te = te;
		this.vec = te.worldVec();
		this.stored = te.getEnergyStored();
		this.storedRF = te.storedRF;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		if (vec == null) vec = new Vector3<Integer>();
		vec.x = buf.readInt();
		vec.y = buf.readInt();
		vec.z = buf.readInt();

		this.stored = buf.readInt();
		this.storedRF = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(vec.x);
		buf.writeInt(vec.y);
		buf.writeInt(vec.z);
		buf.writeInt(stored);
		buf.writeInt(storedRF);
	}

	@Override
	public IMessage onMessage(MessageTileEntityRFBridge message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(VectorHelper.toBlockPos(message.vec));
		
		if (te instanceof TileEntityRFBridge) {
			((TileEntityRFBridge) te).setEnergyStored(message.stored);
			((TileEntityRFBridge) te).storedRF = message.storedRF;
		}
		
		return null;
	}

}

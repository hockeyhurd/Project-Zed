/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

import com.projectzed.mod.tileentity.machine.TileEntityRFBridge;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * Class containing messaging code for rf bridge te.
 * 
 * @author hockeyhurd
 * @version Nov 29, 2014
 */
public class MessageTileEntityRFBridge implements IMessage, IMessageHandler<MessageTileEntityRFBridge, IMessage> {

	public TileEntityRFBridge te;
	public int x, y, z;
	public int stored, storedRF;
	
	public MessageTileEntityRFBridge() {
	}
	
	public MessageTileEntityRFBridge(TileEntityRFBridge te) {
		this.te = te;
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.stored = te.getEnergyStored();
		this.storedRF = te.storedRF;
	}
	
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.stored = buf.readInt();
		this.storedRF = buf.readInt();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(stored);
		buf.writeInt(storedRF);
	}

	public IMessage onMessage(MessageTileEntityRFBridge message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);
		
		if (te instanceof TileEntityRFBridge) {
			((TileEntityRFBridge) te).setEnergyStored(message.stored);
			((TileEntityRFBridge) te).storedRF = message.storedRF;
		}
		
		return null;
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

/**
 * TileEntity message handler for packets.
 * 
 * @author hockeyhurd
 * @version Oct 22, 2014
 */
public class MessageTileEntityGenerator implements IMessage, IMessageHandler<MessageTileEntityGenerator, IMessage> {

	public AbstractTileEntityGenerator te;
	public int x, y, z;
	public int stored;
	public boolean powerMode;
	
	public byte tier;
	public boolean tierable;
	
	public MessageTileEntityGenerator() {
	}
	
	public MessageTileEntityGenerator(AbstractTileEntityGenerator te) {
		this.te = te;
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.stored = te.getEnergyStored();
		this.powerMode = te.canProducePower();
		
		this.tierable = te instanceof TileEntitySolarArray;
		if (this.tierable) this.tier = ((TileEntitySolarArray) te).getTier();
	}
	
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.stored = buf.readInt();
		this.powerMode = buf.readBoolean();
		
		this.tierable = buf.readBoolean();
		if (this.tierable) this.tier = buf.readByte();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(stored);
		buf.writeBoolean(powerMode);
		
		buf.writeBoolean(tierable);
		if (this.tierable) buf.writeByte(tier);
	}

	public IMessage onMessage(MessageTileEntityGenerator message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);
		
		if (te instanceof AbstractTileEntityGenerator) {
			((AbstractTileEntityGenerator) te).setEnergyStored(message.stored);
			((AbstractTileEntityGenerator) te).setPowerMode(message.powerMode);
			
			if (te instanceof TileEntitySolarArray && message.tierable) ((TileEntitySolarArray) te).setTier(message.tier);
		}
		
		return null;
	}
}

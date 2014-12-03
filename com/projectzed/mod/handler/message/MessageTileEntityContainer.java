package com.projectzed.mod.handler.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

import com.projectzed.api.tileentity.container.AbstractTileEntityContainer;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageTileEntityContainer implements IMessage, IMessageHandler<MessageTileEntityContainer, IMessage> {

	public AbstractTileEntityContainer te;
	public int x, y, z;
	public int stored;
	
	public MessageTileEntityContainer() {
	}
	
	public MessageTileEntityContainer(AbstractTileEntityContainer te) {
		this.te = te;
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.stored = te.getEnergyStored();
	}
	
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.stored = buf.readInt();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(stored);
	}

	public IMessage onMessage(MessageTileEntityContainer message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);
		
		if (te instanceof AbstractTileEntityContainer) {
			((AbstractTileEntityContainer) te).setEnergyStored(message.stored);
		}
		
		return null;
	}
}

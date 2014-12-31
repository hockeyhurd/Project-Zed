package com.projectzed.mod.handler.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

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

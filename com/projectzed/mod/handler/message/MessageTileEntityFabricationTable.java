package com.projectzed.mod.handler.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

import com.projectzed.mod.tileentity.TileEntityFabricationTable;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * Class containing code for messaging buttons and other 
 * various requests for fabrication table between client and server.
 * 
 * @author hockeyhurd
 * @version Nov 24, 2014
 */
public class MessageTileEntityFabricationTable implements IMessage, IMessageHandler<MessageTileEntityFabricationTable, IMessage> {

	private TileEntityFabricationTable te;
	// private ItemStack[] slots;
	private int x, y, z;
	
	public MessageTileEntityFabricationTable() {
	}
	
	public MessageTileEntityFabricationTable(TileEntityFabricationTable te) {
		this.te = te;
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		
		/*this.slots = new ItemStack[te.getSizeInvenotry()];
		
		for (int i = 0 ; i < this.slots.length; i++) {
			this.slots[i] = te.getStackInSlot(i);
		}*/
	}

	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
	}
	
	public IMessage onMessage(MessageTileEntityFabricationTable message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);
		
		if (te instanceof TileEntityFabricationTable) {
			// ((TileEntityFabricationTable) te).clearCraftingGrid();
		}
		
		return null;
	}

}

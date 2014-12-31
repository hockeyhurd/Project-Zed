package com.projectzed.mod.handler.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.mod.tileentity.TileEntityFabricationTable;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * Class containing code for messaging buttons and other various requests for
 * fabrication table between client and server.
 * 
 * @author hockeyhurd
 * @version Nov 24, 2014
 */
public class MessageTileEntityFabricationTable implements IMessage, IMessageHandler<MessageTileEntityFabricationTable, IMessage> {

	private TileEntityFabricationTable te;
	private ItemStack[] slots;
	private int x, y, z;
	private int numSlots;

	public MessageTileEntityFabricationTable() {
	}

	/**
	 * @param te = te object as reference.
	 */
	public MessageTileEntityFabricationTable(TileEntityFabricationTable te) {
		this.te = te;
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.numSlots = this.te.getSizeInvenotry() - 1;

		this.slots = new ItemStack[numSlots];

		syncStacks();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * cpw.mods.fml.common.network.simpleimpl.IMessage#fromBytes(io.netty.buffer
	 * .ByteBuf)
	 */
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.numSlots = buf.readInt();
		
		this.slots = new ItemStack[this.numSlots];
		for (int i = 0; i < this.slots.length; i++) {
			this.slots[i] = ByteBufUtils.readItemStack(buf);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * cpw.mods.fml.common.network.simpleimpl.IMessage#toBytes(io.netty.buffer
	 * .ByteBuf)
	 */
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.numSlots);

		if (!isArrayValid()) syncStacks();
		for (int i = 0; i < this.slots.length; i++) {
			ByteBufUtils.writeItemStack(buf, this.slots[i]);
		}
	}
	
	private void syncStacks() {
		if (this.te != null) {
			if (this.slots == null) this.slots = new ItemStack[numSlots];
			
			for (int i = 0; i < this.slots.length; i++) {
				this.slots[i] = this.te.getStackInSlot(i + 1);
			}
		}
	}
	
	private boolean isArrayValid() {
		return this.slots != null && this.slots.length > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * cpw.mods.fml.common.network.simpleimpl.IMessageHandler#onMessage(cpw.
	 * mods.fml.common.network.simpleimpl.IMessage,
	 * cpw.mods.fml.common.network.simpleimpl.MessageContext)
	 */
	public IMessage onMessage(MessageTileEntityFabricationTable message, MessageContext ctx) {
		// TileEntity te =
		// FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x,
		// message.y, message.z);

		World world = ctx.getServerHandler().playerEntity.worldObj;
		TileEntity te = world.getTileEntity(message.x, message.y, message.z);

		if (world != null && te != null && te instanceof TileEntityFabricationTable) {
			TileEntityFabricationTable te2 = (TileEntityFabricationTable) te;

			if (message.slots != null && message.slots.length > 0) {
				for (int i = 0; i < message.slots.length; i++) {
					te2.setStackInSlot(message.slots[i], i + 1);
				}
			}
		}

		return null;
	}

}

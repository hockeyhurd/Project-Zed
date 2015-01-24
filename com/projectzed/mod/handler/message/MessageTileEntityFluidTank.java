package com.projectzed.mod.handler.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankBase;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * TileEntity message handler for packets.
 * 
 * @author hockeyhurd
 * @version Jan 23, 2015
 */
public class MessageTileEntityFluidTank implements IMessage, IMessageHandler<MessageTileEntityFluidTank, IMessage>{

	public TileEntityFluidTankBase te;
	public int x, y, z;
	public int stored;
	// public byte tier;
		
	public MessageTileEntityFluidTank() {
	}
	
	/**
	 * @param te = te object as reference.
	 */
	public MessageTileEntityFluidTank(TileEntityFluidTankBase te) {
		this.te = te;
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.stored = te.getTank().getFluidAmount();
		// this.tier = te.getTier();
	}
	
	/*
	 * (non-Javadoc)
	 * @see cpw.mods.fml.common.network.simpleimpl.IMessage#fromBytes(io.netty.buffer.ByteBuf)
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.stored = buf.readInt();
		// this.tier = buf.readByte();
	}

	/*
	 * (non-Javadoc)
	 * @see cpw.mods.fml.common.network.simpleimpl.IMessage#toBytes(io.netty.buffer.ByteBuf)
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.stored);
		// buf.writeByte(this.tier);
	}
	
	/*
	 * (non-Javadoc)
	 * @see cpw.mods.fml.common.network.simpleimpl.IMessageHandler#onMessage(cpw.mods.fml.common.network.simpleimpl.IMessage, cpw.mods.fml.common.network.simpleimpl.MessageContext)
	 */
	@Override
	public IMessage onMessage(MessageTileEntityFluidTank message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);
		
		if (te != null && te instanceof TileEntityFluidTankBase) {
			TileEntityFluidTankBase te2 = (TileEntityFluidTankBase) te;

			/*ProjectZed.logHelper.info(te2.getTank().getFluidAmount());
			if (te2.getTank().getFluid() != null && message.stored > 0) {
				te2.getTank().setFluid(new FluidStack(te2.getTank().getFluid(), message.stored));
			}*/
		}
		
		return null;
	}

}

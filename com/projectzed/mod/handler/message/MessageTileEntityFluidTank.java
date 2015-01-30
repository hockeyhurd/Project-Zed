package com.projectzed.mod.handler.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

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
	public int fluidAmount;
	public int fluidID;
	public byte tier;
	
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
		this.tier = te.getTier();
		this.fluidAmount = te.getTank().getFluidAmount();
		
		FluidStack fluidStack = te.getTank().getFluid();
		this.fluidID = fluidStack != null && fluidStack.getFluid() != null ? fluidStack.fluidID : -1;
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
		this.tier = buf.readByte();
		this.fluidAmount = buf.readInt();
		this.fluidID = buf.readInt();
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
		buf.writeByte(this.tier);
		buf.writeInt(this.fluidAmount);
		buf.writeInt(this.fluidID);
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

			te2.setTier(message.tier);
			
			if (message.fluidID >= 0) {
				// ProjectZed.logHelper.info(message.fluidID);
				Fluid fluid = FluidRegistry.getFluid(message.fluidID);
				FluidStack stack = new FluidStack(fluid, message.fluidAmount);
				// ProjectZed.logHelper.info(te2.getLocalizedFluidName());
				
				te2.getTank().setFluid(stack);
			}
			
			else te2.getTank().setFluid((FluidStack) null);
		}
		
		return null;
	}

}

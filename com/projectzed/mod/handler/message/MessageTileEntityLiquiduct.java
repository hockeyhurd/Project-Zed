/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * TileEntity message handler for fluid container packets.
 * 
 * @author hockeyhurd
 * @version Mar 5, 2015
 */
public class MessageTileEntityLiquiduct implements IMessage, IMessageHandler<MessageTileEntityLiquiduct, IMessage> {

	private TileEntityLiquiductBase te;
	private Vector3<Integer> vec;
	private int fluidAmount;
	private int fluidIDLen;
	private String fluidID;
	private boolean wasTransferred;

	@Deprecated
	public MessageTileEntityLiquiduct() {
	}
	
	/**
	 * @param te tileentity to reference.
	 */
	public MessageTileEntityLiquiduct(TileEntityLiquiductBase te) {
		this.te = te;
		this.vec = te.worldVec();
		// this.fluidAmount = te.getTank().getFluidAmount();
		
		FluidStack fluidStack = te.wasTransferredLastTick() ? te.getTransferredStack().copy() : null;
		this.fluidID = fluidStack != null && fluidStack.getFluid() != null ? fluidStack.getFluid().getName() : null;
		this.fluidIDLen = fluidID != null ? fluidID.length() : -1;
		this.fluidAmount = this.fluidIDLen > 0 ? fluidStack.amount : 0;
		this.wasTransferred = te.wasTransferredLastTick();
	}

	/*
	 * (non-Javadoc)
	 * @see cpw.mods.fml.common.network.simpleimpl.IMessage#fromBytes(io.netty.buffer.ByteBuf)
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		if (vec == null) vec = new Vector3<Integer>();
		vec.x = buf.readInt();
		vec.y = buf.readInt();
		vec.z = buf.readInt();

		this.fluidAmount = buf.readInt();
		this.fluidIDLen = buf.readInt();

		char[] arr = new char[fluidIDLen];
		for (int i = 0; i < fluidIDLen; i++)
			arr[i] = buf.readChar();

		this.wasTransferred = buf.readBoolean();
	}

	/*
	 * (non-Javadoc)
	 * @see cpw.mods.fml.common.network.simpleimpl.IMessage#toBytes(io.netty.buffer.ByteBuf)
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(vec.x);
		buf.writeInt(vec.y);
		buf.writeInt(vec.z);
		buf.writeInt(this.fluidAmount);
		buf.writeInt(this.fluidIDLen);

		if (fluidIDLen > 0) {
			for (char c : fluidID.toCharArray())
				buf.writeChar(c);
		}

		buf.writeBoolean(this.wasTransferred);
	}
	
	@Override
	public IMessage onMessage(MessageTileEntityLiquiduct message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(VectorHelper.toBlockPos(message.vec));
		
		if (te != null && te instanceof TileEntityLiquiductBase) {
			TileEntityLiquiductBase te2 = (TileEntityLiquiductBase) te;
			
			if (message.fluidIDLen > 0) {
				// ProjectZed.logHelper.info("ID:", message.fluidID, "Amount:", message.fluidAmount);
				Fluid fluid = FluidRegistry.getFluid(message.fluidID);
				FluidStack stack = new FluidStack(fluid, message.fluidAmount);
				
				// te2.getTank().setFluid(stack);
				
				te2.setLastTransferredStack(stack != null && stack.amount > 0 ? stack : null);
				te2.setWasTransferredLastTick(message.wasTransferred);
			}
			
			// else te2.getTank().setFluid((FluidStack) null);
			else te2.setLastTransferredStack((FluidStack) null);
		}
		
		return null;
	}

}

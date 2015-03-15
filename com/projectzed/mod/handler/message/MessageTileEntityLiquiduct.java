/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.projectzed.mod.tileentity.container.TileEntityFluidTankBase;
import com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductBase;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * TileEntity message handler for fluid container packets.
 * 
 * @author hockeyhurd
 * @version Mar 5, 2015
 */
public class MessageTileEntityLiquiduct implements IMessage, IMessageHandler<MessageTileEntityLiquiduct, IMessage> {

	public TileEntityLiquiductBase te;
	public int x, y, z;
	public int fluidAmount;
	public int fluidID;
	
	public MessageTileEntityLiquiduct() {
	}
	
	/**
	 * @param te tileentity to reference.
	 */
	public MessageTileEntityLiquiduct(TileEntityLiquiductBase te) {
		this.te = te;
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
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
		buf.writeInt(this.fluidAmount);
		buf.writeInt(this.fluidID);
	}
	
	@Override
	public IMessage onMessage(MessageTileEntityLiquiduct message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);
		
		if (te != null && te instanceof TileEntityFluidTankBase) {
			TileEntityLiquiductBase te2 = (TileEntityLiquiductBase) te;
			
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

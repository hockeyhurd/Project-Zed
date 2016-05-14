/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.StringUtils;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * TileEntity message handler for packets.
 * 
 * @author hockeyhurd
 * @version Jan 23, 2015
 */
public class MessageTileEntityFluidTank implements IMessage, IMessageHandler<MessageTileEntityFluidTank, IMessage> {

	private TileEntityFluidTankBase te;
	private Vector3<Integer> vec;
	private int fluidAmount;
	private int fluidIDLen;
	private String fluidID;
	private byte tier;

	private byte[] openSides = new byte[EnumFacing.VALUES.length];

	@Deprecated
	public MessageTileEntityFluidTank() {
	}
	
	/**
	 * @param te = te object as reference.
	 */
	public MessageTileEntityFluidTank(TileEntityFluidTankBase te) {
		this.te = te;
		this.vec = te.worldVec();
		this.tier = te.getTier();
		this.fluidAmount = te.getTank().getFluidAmount();
		
		FluidStack fluidStack = te.getTank().getFluid();
		this.fluidID = fluidStack != null && fluidStack.getFluid() != null ? fluidStack.getFluid().getName() : null;
		this.fluidIDLen = fluidID != null ? fluidID.length() : -1;
		
		for (int i = 0; i < openSides.length; i++) {
			this.openSides[i] = te.getSideValve(i);
		}
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

		this.tier = buf.readByte();
		this.fluidAmount = buf.readInt();
		this.fluidIDLen = buf.readInt();

		char[] arr = new char[fluidIDLen];
		for (int i = 0; i < fluidIDLen; i++)
			arr[i] = buf.readChar();

		this.fluidID = new String(arr);
		
		for (int i = 0; i < openSides.length; i++) {
			openSides[i] = buf.readByte();
		}
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
		buf.writeByte(this.tier);
		buf.writeInt(this.fluidAmount);
		buf.writeInt(this.fluidIDLen);

		if (fluidIDLen > 0) {
			for (char c : fluidID.toCharArray())
				buf.writeChar(c);
		}
		
		for (byte b : openSides) {
			buf.writeByte(b);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see cpw.mods.fml.common.network.simpleimpl.IMessageHandler#onMessage(cpw.mods.fml.common.network.simpleimpl.IMessage, cpw.mods.fml.common.network.simpleimpl.MessageContext)
	 */
	@Override
	public IMessage onMessage(MessageTileEntityFluidTank message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(VectorHelper.toBlockPos(message.vec));
		
		if (te != null && te instanceof TileEntityFluidTankBase) {
			TileEntityFluidTankBase te2 = (TileEntityFluidTankBase) te;

			te2.setTier(message.tier);
			
			if (StringUtils.nullCheckString(message.fluidID)) {
				// ProjectZed.logHelper.info(message.fluidID);
				Fluid fluid = FluidRegistry.getFluid(message.fluidID);
				FluidStack stack = new FluidStack(fluid, message.fluidAmount);
				// ProjectZed.logHelper.info(te2.getLocalizedFluidName());
				
				te2.getTank().setFluid(stack);
			}
			
			else te2.getTank().setFluid((FluidStack) null);
			
			for (int i = 0; i < message.openSides.length; i++) {
				te2.setSideValve(EnumFacing.VALUES[i], message.openSides[i]);
			}
		}
		
		return null;
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCentrifuge;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;


/**
 * Class used for handling specific synching with centrifuge stuffs.
 * 
 * @author hockeyhurd
 * @version Mar 25, 2015
 */
public class MessageTileEntityCentrifuge implements IMessage, IMessageHandler<MessageTileEntityCentrifuge, IMessage> {

	private TileEntityIndustrialCentrifuge te;
	private Vector3<Integer> vec;
	private byte craftingAmount = 1;
	
	@Deprecated
	public MessageTileEntityCentrifuge() {
	}
	
	/**
	 * @param te te object to reference.
	 */
	public MessageTileEntityCentrifuge(TileEntityIndustrialCentrifuge te) {
		this.te = te;
		this.vec = te.worldVec();
		this.craftingAmount = te.getCraftingAmount();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		if (this.vec == null) this.vec = Vector3.zero.getVector3i();
		
		this.vec.x = buf.readInt();
		this.vec.y = buf.readInt();
		this.vec.z = buf.readInt();
		this.craftingAmount = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.vec.x);
		buf.writeInt(this.vec.y);
		buf.writeInt(this.vec.z);
		buf.writeByte(this.craftingAmount);
	}

	@Override
	public IMessage onMessage(MessageTileEntityCentrifuge message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			TileEntity tileEntity = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.vec.x, message.vec.y, message.vec.z);
			if (tileEntity != null && tileEntity instanceof TileEntityIndustrialCentrifuge)
				((TileEntityIndustrialCentrifuge) tileEntity).setCraftingAmount(message.craftingAmount);
		}
		
		return null;
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector4;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialLoader;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

/**
 * Messaging class for industrialLoader.
 * 
 * @author hockeyhurd
 * @version Apr 25, 2015
 */
public class MessageTileEntityLoader implements IMessage, IMessageHandler<MessageTileEntityLoader, IMessage> {

	private TileEntityIndustrialLoader te;
	private Vector4<Integer> vec;
	private byte radii;
	
	@Deprecated
	public MessageTileEntityLoader() {
	}
	
	public MessageTileEntityLoader(TileEntityIndustrialLoader te) {
		this(te, (byte) -1);
	}
	
	public MessageTileEntityLoader(TileEntityIndustrialLoader te, byte radii) {
		this.te = te;
		this.vec = new Vector4<Integer>(te.xCoord, te.yCoord, te.zCoord);
		this.radii = radii;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		if (this.vec == null) this.vec = new Vector4<Integer>();
		this.vec.x = buf.readInt();
		this.vec.y = buf.readInt();
		this.vec.z = buf.readInt();
		this.radii = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.vec.x);
		buf.writeInt(this.vec.y);
		buf.writeInt(this.vec.z);
		buf.writeByte(this.radii);
	}
	
	@Override
	public IMessage onMessage(MessageTileEntityLoader message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			TileEntity te = world.getTileEntity(message.vec.x, message.vec.y, message.vec.z);
			
			if (world != null && te != null && te instanceof TileEntityIndustrialLoader) {
				TileEntityIndustrialLoader te2 = (TileEntityIndustrialLoader) te;

				te2.setRadii(message.radii);
			}
		}
		
		return null;
	}


}

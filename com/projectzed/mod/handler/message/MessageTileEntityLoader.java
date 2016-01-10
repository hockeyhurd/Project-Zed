/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialLoader;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Messaging class for industrialLoader.
 * 
 * @author hockeyhurd
 * @version Apr 25, 2015
 */
public class MessageTileEntityLoader implements IMessage, IMessageHandler<MessageTileEntityLoader, IMessage> {

	private TileEntityIndustrialLoader te;
	private Vector3<Integer> vec;
	private byte radii;
	private boolean registered, removed;
	
	@Deprecated
	public MessageTileEntityLoader() {
	}
	
	public MessageTileEntityLoader(TileEntityIndustrialLoader te) {
		this(te, (byte) -1);
	}
	
	public MessageTileEntityLoader(TileEntityIndustrialLoader te, byte radii) {
		this.te = te;
		this.vec = te.worldVec();
		this.radii = radii;
		this.registered = te.isRegistered();
		this.removed = te.isRemoved();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		if (this.vec == null) this.vec = new Vector3<Integer>();
		this.vec.x = buf.readInt();
		this.vec.y = buf.readInt();
		this.vec.z = buf.readInt();
		this.radii = buf.readByte();
		this.registered = buf.readBoolean();
		this.removed = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.vec.x);
		buf.writeInt(this.vec.y);
		buf.writeInt(this.vec.z);
		buf.writeByte(this.radii);
		buf.writeBoolean(this.registered);
		buf.writeBoolean(this.removed);
	}
	
	@Override
	public IMessage onMessage(MessageTileEntityLoader message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			TileEntity te = world.getTileEntity(message.vec.x, message.vec.y, message.vec.z);
			
			if (world != null && te != null && te instanceof TileEntityIndustrialLoader) {
				TileEntityIndustrialLoader te2 = (TileEntityIndustrialLoader) te;

				if (message.radii != -1 && message.radii != te2.getRadii()) te2.setRadii(message.radii);
				te2.setRegistered(message.registered);
				te2.setRemoved(message.removed);
			}
		}
		
		else if (ctx.side == Side.CLIENT) {
			TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.vec.x, message.vec.y, message.vec.z);
			
			if (te != null && te instanceof TileEntityIndustrialLoader) {
				if (((TileEntityIndustrialLoader) te).getRadii() != message.radii) ((TileEntityIndustrialLoader) te).setRadii(message.radii);
				// ((TileEntityIndustrialLoader) te).setRegistered(message.registered);
				((TileEntityIndustrialLoader) te).setRemoved(message.removed);
			}
		}
		
		return null;
	}


}

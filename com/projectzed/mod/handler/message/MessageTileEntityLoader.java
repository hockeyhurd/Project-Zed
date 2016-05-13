/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialLoader;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

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
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		if (this.vec == null) this.vec = new Vector3<Integer>();
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
			TileEntity te = world.getTileEntity(VectorHelper.toBlockPos(message.vec));
			
			if (world != null && te != null && te instanceof TileEntityIndustrialLoader) {
				TileEntityIndustrialLoader te2 = (TileEntityIndustrialLoader) te;

				if (message.radii != -1 && message.radii != te2.getRadii()) te2.setRadii(message.radii);
			}
		}
		
		else if (ctx.side == Side.CLIENT) {
			TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(VectorHelper.toBlockPos(message.vec));
			
			if (te != null && te instanceof TileEntityIndustrialLoader) {
				if (((TileEntityIndustrialLoader) te).getRadii() != message.radii) ((TileEntityIndustrialLoader) te).setRadii(message.radii);
			}
		}
		
		return null;
	}


}

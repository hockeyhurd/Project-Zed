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
import net.minecraftforge.common.util.ForgeDirection;

import com.hockeyhurd.api.math.Rect;
import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.tileentity.digger.AbstractTileEntityDigger;
import com.projectzed.api.util.EnumRedstoneType;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

/**
 * Class used to send messages to/from client and server for all digger based machines.
 * 
 * @author hockeyhurd
 * @version Jun 19, 2015
 */
public class MessageTileEntityDigger implements IMessage, IMessageHandler<MessageTileEntityDigger, IMessage> {

	protected AbstractTileEntityDigger te;
	protected int x, y, z;
	protected int stored;
	protected boolean powerMode;
	
	// TODO: Implement these in messaging.
	protected Vector3<Integer> mineVec;
	protected Rect<Integer> quarryRect;
	
	protected EnumRedstoneType redstoneType;
	protected byte[] openSides = new byte[ForgeDirection.VALID_DIRECTIONS.length];
	
	public MessageTileEntityDigger() {
	}

	public MessageTileEntityDigger(AbstractTileEntityDigger te) {
		this.te = te;
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.stored = te.getEnergyStored();
		this.powerMode = te.isPoweredOn();
		
		this.redstoneType = te.getRedstoneType() != null ? te.getRedstoneType() : EnumRedstoneType.LOW;
		
		for (int i = 0; i < openSides.length; i++) {
			this.openSides[i] = te.getSideValve(i);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.stored = buf.readInt();
		this.powerMode = buf.readBoolean();
		
		this.redstoneType = EnumRedstoneType.TYPES[buf.readInt()];
		
		for (int i = 0; i < openSides.length; i++) {
			openSides[i] = buf.readByte();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(stored);
		buf.writeBoolean(powerMode);
		
		buf.writeInt(redstoneType.ordinal());
		
		for (byte b : openSides) {
			buf.writeByte(b);
		}
	}
	
	@Override
	public IMessage onMessage(MessageTileEntityDigger message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);
			
			if (te instanceof AbstractTileEntityDigger) {
				((AbstractTileEntityDigger) te).setEnergyStored(message.stored);
				((AbstractTileEntityDigger) te).setPowerMode(message.powerMode);
				((AbstractTileEntityDigger) te).setRedstoneType(message.redstoneType);
				
				for (int i = 0; i < message.openSides.length; i++) {
					((AbstractTileEntityDigger) te).setSideValve(ForgeDirection.VALID_DIRECTIONS[i], message.openSides[i]);
				}
			}
		}
		
		else if (ctx.side == Side.SERVER) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			TileEntity te = world.getTileEntity(message.x, message.y, message.z);
			
			if (world != null && te != null && te instanceof AbstractTileEntityDigger) {
				AbstractTileEntityDigger te2 = (AbstractTileEntityDigger) te;
				
				te2.setRedstoneType(message.redstoneType);
				
				for (int i = 0; i < message.openSides.length; i++) {
					te2.setSideValve(ForgeDirection.VALID_DIRECTIONS[i], message.openSides[i]);
				}
			}
		}
		
		return null;
	}

}

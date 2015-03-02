/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Class containing creation of message to be sent from either side.
 * 
 * @author hockeyhurd
 * @version Dec 4, 2014
 */
public class MessageTileEntityEnergyContainer implements IMessage, IMessageHandler<MessageTileEntityEnergyContainer, IMessage> {

	public IEnergyContainer te;
	public int x, y, z;
	public int stored;

	// Energy cell specific.
	public boolean isEnergyCell;
	public byte[] openSides = new byte[ForgeDirection.VALID_DIRECTIONS.length];

	public MessageTileEntityEnergyContainer() {
	}

	/**
	 * @param cont = container to get data from.
	 */
	public MessageTileEntityEnergyContainer(IEnergyContainer cont) {
		this.te = cont;
		this.x = cont.worldVec().x;
		this.y = cont.worldVec().y;
		this.z = cont.worldVec().z;
		this.stored = cont.getEnergyStored();

		if (cont instanceof TileEntityEnergyBankBase) {
			isEnergyCell = true;
			TileEntityEnergyBankBase temp = (TileEntityEnergyBankBase) cont;

			for (int i = 0; i < openSides.length; i++) {
				this.openSides[i] = temp.getSideValve(i);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see cpw.mods.fml.common.network.simpleimpl.IMessage#fromBytes(io.netty.buffer.ByteBuf)
	 */
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.stored = buf.readInt();
		this.isEnergyCell = buf.readBoolean();

		if (isEnergyCell) {
			for (int i = 0; i < openSides.length; i++) {
				openSides[i] = buf.readByte();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see cpw.mods.fml.common.network.simpleimpl.IMessage#toBytes(io.netty.buffer.ByteBuf)
	 */
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(stored);
		buf.writeBoolean(isEnergyCell);

		if (isEnergyCell) {
			for (byte b : openSides) {
				buf.writeByte(b);
			}
		}
	}
	
	@Override
	public IMessage onMessage(MessageTileEntityEnergyContainer message, MessageContext ctx) {
		
		if (ctx.side == Side.CLIENT) {
			TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);

			if (te instanceof IEnergyContainer) {
				((IEnergyContainer) te).setEnergyStored(message.stored);
				
				if (te instanceof TileEntityEnergyBankBase) {
					for (int i = 0; i < message.openSides.length; i++) {
						((TileEntityEnergyBankBase) te).setSideValve(ForgeDirection.VALID_DIRECTIONS[i], message.openSides[i]);
					}
				}
			}
		}
		
		else if (ctx.side == Side.SERVER && message.isEnergyCell) {
			TileEntity tileEntity = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
			
			if (tileEntity != null && tileEntity instanceof TileEntityEnergyBankBase) {
				TileEntityEnergyBankBase te = (TileEntityEnergyBankBase) tileEntity;
				
				if (te != null) {
					for (int i = 0; i < message.openSides.length; i++) {
						te.setSideValve(ForgeDirection.VALID_DIRECTIONS[i], message.openSides[i]);
					}
				}
			}
		}

		return null;
	}
	
}

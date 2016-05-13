/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Class containing creation of message to be sent from either side.
 * 
 * @author hockeyhurd
 * @version Dec 4, 2014
 */
public class MessageTileEntityEnergyContainer implements IMessage, IMessageHandler<MessageTileEntityEnergyContainer, IMessage> {

	private IEnergyContainer te;
	private Vector3<Integer> vec;
	private int stored;
	private int maxStorage;
	private int maxImportRate, maxExportRate;
	private EnumFacing lastReceivedDir;

	// Energy cell specific.
	private boolean isEnergyCell;
	private byte[] openSides = new byte[EnumFacing.VALUES.length];

	@Deprecated
	public MessageTileEntityEnergyContainer() {
	}

	/**
	 * @param cont = container to get data from.
	 */
	public MessageTileEntityEnergyContainer(IEnergyContainer cont) {
		this.te = cont;
		this.vec = cont.worldVec();
		this.stored = cont.getEnergyStored();
		this.maxStorage = cont.getMaxStorage();
		this.maxImportRate = cont.getMaxImportRate();
		this.maxExportRate = cont.getMaxExportRate();

		this.lastReceivedDir = cont instanceof TileEntityEnergyPipeBase ? ((TileEntityEnergyPipeBase) te).getLastReceivedDirection() : null;

		if (cont instanceof TileEntityEnergyBankBase) {
			isEnergyCell = true;
			TileEntityEnergyBankBase temp = (TileEntityEnergyBankBase) cont;

			for (int i = 0; i < openSides.length; i++) {
				this.openSides[i] = temp.getSideValve(i);
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		if (vec == null) vec = new Vector3<Integer>();
		vec.x = buf.readInt();
		vec.y = buf.readInt();
		vec.z = buf.readInt();

		this.stored = buf.readInt();
		this.maxStorage = buf.readInt();
		this.maxImportRate = buf.readInt();
		this.maxExportRate = buf.readInt();
		this.isEnergyCell = buf.readBoolean();

		byte dir = buf.readByte();
		this.lastReceivedDir = dir == EnumFacing.VALUES.length ? null : EnumFacing.VALUES[dir];

		if (isEnergyCell) {
			for (int i = 0; i < openSides.length; i++) {
				openSides[i] = buf.readByte();
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(vec.x);
		buf.writeInt(vec.y);
		buf.writeInt(vec.z);
		buf.writeInt(stored);
		buf.writeInt(maxStorage);
		buf.writeInt(maxImportRate);
		buf.writeInt(maxExportRate);
		buf.writeBoolean(isEnergyCell);
		buf.writeByte(lastReceivedDir.ordinal());

		if (isEnergyCell) {
			for (byte b : openSides) {
				buf.writeByte(b);
			}
		}
	}
	
	@Override
	public IMessage onMessage(MessageTileEntityEnergyContainer message, MessageContext ctx) {
		
		if (ctx.side == Side.CLIENT) {
			TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(VectorHelper.toBlockPos(message.vec));

			if (te instanceof IEnergyContainer) {
				((IEnergyContainer) te).setEnergyStored(message.stored);
				((IEnergyContainer) te).setMaxStorage(message.maxStorage);
				
				if (te instanceof TileEntityEnergyBankBase) {
					for (int i = 0; i < message.openSides.length; i++) {
						((TileEntityEnergyBankBase) te).setSideValve(EnumFacing.VALUES[i], message.openSides[i]);
					}
				}

				if (te instanceof TileEntityEnergyPipeBase) ((TileEntityEnergyPipeBase) te).setLastReceivedDirection(message.lastReceivedDir);
			}
		}
		
		else if (ctx.side == Side.SERVER && message.isEnergyCell) {
			TileEntity tileEntity = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(VectorHelper.toBlockPos(message.vec));

			if (tileEntity != null) {
				if (tileEntity instanceof TileEntityEnergyBankBase) {
					TileEntityEnergyBankBase te = (TileEntityEnergyBankBase) tileEntity;

					for (int i = 0; i < message.openSides.length; i++) {
						te.setSideValve(EnumFacing.VALUES[i], message.openSides[i]);
					}
				}

				else if (tileEntity instanceof TileEntityEnergyPipeBase) {
					((TileEntityEnergyPipeBase) tileEntity).setLastReceivedDirection(message.lastReceivedDir);
				}
			}
		}

		return null;
	}
	
}

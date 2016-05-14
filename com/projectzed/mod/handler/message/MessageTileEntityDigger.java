/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.hockeyhurd.hcorelib.api.math.Rect;
import com.hockeyhurd.hcorelib.api.math.Vector2;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.api.tileentity.digger.AbstractTileEntityDigger;
import com.projectzed.api.util.EnumRedstoneType;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Class used to send messages to/from client and server for all digger based machines.
 * 
 * @author hockeyhurd
 * @version Jun 19, 2015
 */
public class MessageTileEntityDigger implements IMessage, IMessageHandler<MessageTileEntityDigger, IMessage> {

	protected AbstractTileEntityDigger te;
	protected Vector3<Integer> vec;
	protected int stored;
	protected int energyBurnRate;
	protected boolean powerMode;

	protected boolean isDigging;
	protected Vector3<Integer> mineVec;
	protected Rect<Integer> quarryRect;
	
	protected EnumRedstoneType redstoneType;
	protected byte[] openSides = new byte[EnumFacing.VALUES.length];

	public MessageTileEntityDigger() {
	}

	public MessageTileEntityDigger(AbstractTileEntityDigger te) {
		this.te = te;
		this.vec = te.worldVec();
		this.stored = te.getEnergyStored();
		this.energyBurnRate = te.getEnergyBurnRate();
		this.powerMode = te.isPoweredOn();
		
		this.redstoneType = te.getRedstoneType() != null ? te.getRedstoneType() : EnumRedstoneType.LOW;
		
		for (int i = 0; i < openSides.length; i++) {
			this.openSides[i] = te.getSideValve(i);
		}

		this.quarryRect = te.getQuarryRect();
		this.mineVec = te.getCurrentMineVec();

		this.isDigging = this.quarryRect != null && this.mineVec != null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		if (vec == null) vec = new Vector3<Integer>();
		vec.x = buf.readInt();
		vec.y = buf.readInt();
		vec.z = buf.readInt();

		this.stored = buf.readInt();
		this.energyBurnRate = buf.readInt();
		this.powerMode = buf.readBoolean();
		
		this.redstoneType = EnumRedstoneType.TYPES[buf.readInt()];
		
		for (int i = 0; i < openSides.length; i++) {
			openSides[i] = buf.readByte();
		}

		this.isDigging = buf.readBoolean();

		if (this.isDigging) {
			int mx = buf.readInt();
			int my = buf.readInt();
			int mz = buf.readInt();

			this.mineVec = new Vector3<Integer>(mx, my, mz);

			int q0x = buf.readInt();
			int q0y = buf.readInt();

			int q1x = buf.readInt();
			int q1y = buf.readInt();

			this.quarryRect = new Rect<Integer>(new Vector2<Integer>(q0x, q0y), new Vector2<Integer>(q1x, q1y));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(vec.x);
		buf.writeInt(vec.y);
		buf.writeInt(vec.z);
		buf.writeInt(stored);
		buf.writeInt(energyBurnRate);
		buf.writeBoolean(powerMode);
		
		buf.writeInt(redstoneType.ordinal());
		
		for (byte b : openSides) {
			buf.writeByte(b);
		}

		buf.writeBoolean(isDigging);

		if (isDigging) {
			buf.writeInt(mineVec.x);
			buf.writeInt(mineVec.y);
			buf.writeInt(mineVec.z);
			buf.writeInt(quarryRect.min.x);
			buf.writeInt(quarryRect.min.y);
			buf.writeInt(quarryRect.max.x);
			buf.writeInt(quarryRect.max.y);
		}
	}
	
	@Override
	public IMessage onMessage(MessageTileEntityDigger message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(VectorHelper.toBlockPos(message.vec));
			
			if (te != null && te instanceof AbstractTileEntityDigger) {
				((AbstractTileEntityDigger) te).setEnergyStored(message.stored);
				((AbstractTileEntityDigger) te).setEnergyBurnRate(message.energyBurnRate);
				((AbstractTileEntityDigger) te).setPowerMode(message.powerMode);
				((AbstractTileEntityDigger) te).setRedstoneType(message.redstoneType);

				if (message.isDigging) {
					((AbstractTileEntityDigger) te).setCurrentMineVec(message.mineVec);
					((AbstractTileEntityDigger) te).setQuarryRect(message.quarryRect);
				}

				for (int i = 0; i < message.openSides.length; i++) {
					((AbstractTileEntityDigger) te).setSideValve(EnumFacing.VALUES[i], message.openSides[i]);
				}

			}
		}
		
		else if (ctx.side == Side.SERVER) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			TileEntity te = world.getTileEntity(VectorHelper.toBlockPos(message.vec));
			
			if (world != null && te != null && te instanceof AbstractTileEntityDigger) {
				AbstractTileEntityDigger te2 = (AbstractTileEntityDigger) te;

				te2.setEnergyStored(message.stored);
				te2.setEnergyBurnRate(message.energyBurnRate);
				te2.setPowerMode(message.powerMode);
				te2.setRedstoneType(message.redstoneType);

				if (message.isDigging) {
					te2.setCurrentMineVec(message.mineVec);
					te2.setQuarryRect(message.quarryRect);
				}

				for (int i = 0; i < message.openSides.length; i++) {
					te2.setSideValve(EnumFacing.VALUES[i], message.openSides[i]);
				}

			}
		}
		
		return null;
	}

}

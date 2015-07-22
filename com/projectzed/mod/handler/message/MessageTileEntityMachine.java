/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.EnumRedstoneType;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCentrifuge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * 
 * @author hockeyhurd
 * @version Oct 23, 2014
 */
public class MessageTileEntityMachine implements IMessage, IMessageHandler<MessageTileEntityMachine, IMessage> {

	private AbstractTileEntityMachine te;
	private int x, y, z;
	private int stored, energyBurnRate;
	private int scaledCookTime;
	private boolean powerMode;
	
	private boolean containsFluid;
	private int fluidStored;
	
	private EnumRedstoneType redstoneType;
	private byte[] openSides = new byte[ForgeDirection.VALID_DIRECTIONS.length];
	
	public MessageTileEntityMachine() {
	}
	
	public MessageTileEntityMachine(AbstractTileEntityMachine te) {
		this.te = te;
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.stored = te.getEnergyStored();
		this.energyBurnRate = te.getEnergyBurnRate();
		this.scaledCookTime = te.scaledTime;
		this.powerMode = te.isPoweredOn();
		this.containsFluid = te instanceof TileEntityIndustrialCentrifuge;
		if (this.containsFluid) this.fluidStored = ((TileEntityIndustrialCentrifuge) te).getTank().getFluidAmount();
		
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
		this.energyBurnRate = buf.readInt();
		this.scaledCookTime = buf.readInt();
		this.powerMode = buf.readBoolean();
		this.containsFluid = buf.readBoolean();
		if (this.containsFluid) this.fluidStored = buf.readInt();
		
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
		buf.writeInt(energyBurnRate);
		buf.writeInt(scaledCookTime);
		buf.writeBoolean(powerMode);
		buf.writeBoolean(containsFluid);
		if (this.containsFluid) buf.writeInt(this.fluidStored);
		
		buf.writeInt(redstoneType.ordinal());
		
		for (byte b : openSides) {
			buf.writeByte(b);
		}
	}

	@Override
	public IMessage onMessage(MessageTileEntityMachine message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);
			
			if (te instanceof AbstractTileEntityMachine) {
				((AbstractTileEntityMachine) te).setEnergyStored(message.stored);
				((AbstractTileEntityMachine) te).setEnergyBurnRate(message.energyBurnRate);
				((AbstractTileEntityMachine) te).setPowerMode(message.powerMode);
				((AbstractTileEntityMachine) te).scaledTime = message.scaledCookTime;
				((AbstractTileEntityMachine) te).setRedstoneType(message.redstoneType);
				
				for (int i = 0; i < message.openSides.length; i++) {
					((AbstractTileEntityMachine) te).setSideValve(ForgeDirection.VALID_DIRECTIONS[i], message.openSides[i]);
				}

				if (message.containsFluid && message.fluidStored > 0) ((TileEntityIndustrialCentrifuge) te).getTank().setFluid(new FluidStack(FluidRegistry.WATER, message.fluidStored)); 
			}
		}
		
		else if (ctx.side == Side.SERVER) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			TileEntity te = world.getTileEntity(message.x, message.y, message.z);
			
			if (world != null && te != null && te instanceof AbstractTileEntityMachine) {
				AbstractTileEntityMachine te2 = (AbstractTileEntityMachine) te;

				te2.setEnergyStored(message.stored);
				te2.setEnergyBurnRate(message.energyBurnRate);
				te2.setPowerMode(message.powerMode);
				te2.scaledTime = message.energyBurnRate;
				te2.setRedstoneType(message.redstoneType);
				
				for (int i = 0; i < message.openSides.length; i++) {
					te2.setSideValve(ForgeDirection.VALID_DIRECTIONS[i], message.openSides[i]);
				}
			}
		}
		
		return null;
	}

}

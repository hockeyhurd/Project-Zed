/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.api.util.EnumRedstoneType;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCentrifuge;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * 
 * @author hockeyhurd
 * @version Oct 23, 2014
 */
public class MessageTileEntityMachine implements IMessage, IMessageHandler<MessageTileEntityMachine, IMessage> {

	private AbstractTileEntityMachine te;
	private Vector3<Integer> vec;
	private int stored, energyBurnRate;
	private int cookTime;
	private int scaledCookTime;
	private boolean powerMode;
	
	private boolean containsFluid;
	private int fluidStored;
	
	private EnumRedstoneType redstoneType;
	private byte[] openSides = new byte[EnumFacing.VALUES.length];
	
	public MessageTileEntityMachine() {
	}
	
	public MessageTileEntityMachine(AbstractTileEntityMachine te) {
		this.te = te;
		this.vec = te.worldVec();
		this.stored = te.getEnergyStored();
		this.energyBurnRate = te.getEnergyBurnRate();
		this.cookTime = te.getCookTime();
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
		if (this.vec == null) this.vec = new Vector3<Integer>();
		this.vec.x = buf.readInt();
		this.vec.y = buf.readInt();
		this.vec.z = buf.readInt();

		this.stored = buf.readInt();
		this.energyBurnRate = buf.readInt();
		this.cookTime = buf.readInt();
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
		if (vec == null) vec = new Vector3<Integer>();

		buf.writeInt(vec.x);
		buf.writeInt(vec.y);
		buf.writeInt(vec.z);
		buf.writeInt(stored);
		buf.writeInt(energyBurnRate);
		buf.writeInt(cookTime);
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
			final TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(VectorHelper.toBlockPos(message.vec));
			
			if (tileEntity instanceof AbstractTileEntityMachine) {
				final AbstractTileEntityMachine te = (AbstractTileEntityMachine) tileEntity;
				
				te.setEnergyStored(message.stored);
				te.setEnergyBurnRate(message.energyBurnRate);
				te.setPowerMode(message.powerMode);
				te.setCookTime(message.cookTime);
				te.scaledTime = message.scaledCookTime;
				te.setRedstoneType(message.redstoneType);
				
				for (int i = 0; i < message.openSides.length; i++) {
					te.setSideValve(EnumFacing.VALUES[i], message.openSides[i]);
				}

				if (message.containsFluid && message.fluidStored > 0)
					((TileEntityIndustrialCentrifuge) tileEntity).getTank().setFluid(new FluidStack(FluidRegistry.WATER, message.fluidStored));
			}
		}
		
		else if (ctx.side == Side.SERVER) {
			final World world = ctx.getServerHandler().playerEntity.worldObj;
			final TileEntity tileEntity = world.getTileEntity(VectorHelper.toBlockPos(message.vec));
			
			if (world != null && tileEntity != null && tileEntity instanceof AbstractTileEntityMachine) {
				final AbstractTileEntityMachine te = (AbstractTileEntityMachine) tileEntity;

				te.setEnergyStored(message.stored);
				te.setEnergyBurnRate(message.energyBurnRate);
				te.setPowerMode(message.powerMode);
				te.scaledTime = message.energyBurnRate;
				te.setRedstoneType(message.redstoneType);
				
				for (int i = 0; i < message.openSides.length; i++) {
					te.setSideValve(EnumFacing.VALUES[i], message.openSides[i]);
				}
			}
		}
		
		return null;
	}

}

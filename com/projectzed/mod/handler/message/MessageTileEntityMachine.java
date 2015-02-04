package com.projectzed.mod.handler.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialCentrifuge;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * 
 * @author hockeyhurd
 * @version Oct 23, 2014
 */
public class MessageTileEntityMachine implements IMessage, IMessageHandler<MessageTileEntityMachine, IMessage> {

	public AbstractTileEntityMachine te;
	public int x, y, z;
	public int stored;
	public boolean powerMode;
	
	public boolean containsFluid;
	public int fluidStored;
	
	public MessageTileEntityMachine() {
	}
	
	public MessageTileEntityMachine(AbstractTileEntityMachine te) {
		this.te = te;
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.stored = te.getEnergyStored();
		this.powerMode = te.isPoweredOn();
		this.containsFluid = te instanceof TileEntityIndustrialCentrifuge;
		if (this.containsFluid) this.fluidStored = ((TileEntityIndustrialCentrifuge) te).getTank().getFluidAmount();
	}
	
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.stored = buf.readInt();
		this.powerMode = buf.readBoolean();
		this.containsFluid = buf.readBoolean();
		if (this.containsFluid) this.fluidStored = buf.readInt();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(stored);
		buf.writeBoolean(powerMode);
		buf.writeBoolean(containsFluid);
		if (this.containsFluid) buf.writeInt(this.fluidStored);
	}

	public IMessage onMessage(MessageTileEntityMachine message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);
		
		if (te instanceof AbstractTileEntityMachine) {
			((AbstractTileEntityMachine) te).setEnergyStored(message.stored);
			((AbstractTileEntityMachine) te).setPowerMode(message.powerMode);
			
			if (message.containsFluid && message.fluidStored > 0) ((TileEntityIndustrialCentrifuge) te).getTank().setFluid(new FluidStack(FluidRegistry.WATER, message.fluidStored)); 
		}
		
		return null;
	}

}

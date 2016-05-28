/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler.message;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.StringUtils;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.api.heat.HeatLogic;
import com.projectzed.api.heat.IHeatable;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * TileEntity message handler for packets.
 * 
 * @author hockeyhurd
 * @version Oct 22, 2014
 */
public class MessageTileEntityGenerator implements IMessage, IMessageHandler<MessageTileEntityGenerator, IMessage> {

	private AbstractTileEntityGenerator te;
	private Vector3<Integer> vec;
	private EnumFacing frontFacing;
	private int stored;
	private boolean powerMode;

	private byte tier;
	private boolean tierable;

	private boolean hasFluidTank;
	private String fluidName;
	private int fluidNameLen;
	private int fluidAmount;

	private boolean hasHeatLogic;
	private int heatAmount;
	private int maxHeatAmount;
	private float heatResistance;

	@Deprecated
	public MessageTileEntityGenerator() {
	}
	
	public MessageTileEntityGenerator(AbstractTileEntityGenerator te) {
		this.te = te;
		this.vec = te.worldVec();
		this.frontFacing = te.getCurrentFacing();
		this.stored = te.getEnergyStored();
		this.powerMode = te.canProducePower();
		
		this.tierable = te instanceof TileEntitySolarArray;
		if (this.tierable) this.tier = ((TileEntitySolarArray) te).getTier();

		if (te instanceof IFluidContainer) {
			hasFluidTank = true;
			fluidName = ((IFluidContainer) te).getFluidID();
			fluidNameLen = StringUtils.nullCheckString(fluidName) ? fluidName.length() : 0;
			fluidAmount = ((IFluidContainer) te).getTank().getFluidAmount();
		}

		if (te instanceof IHeatable) {
			hasHeatLogic = true;
			heatAmount = ((IHeatable) te).getHeatLogic().getHeat();
			maxHeatAmount = ((IHeatable) te).getHeatLogic().getMaxHeat();
			heatResistance = ((IHeatable) te).getHeatLogic().getResistance();
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		if (vec == null) vec = new Vector3<Integer>();
		vec.x = buf.readInt();
		vec.y = buf.readInt();
		vec.z = buf.readInt();

		final int dir = buf.readInt();
		this.frontFacing = EnumFacing.getFront(dir);
		this.stored = buf.readInt();
		this.powerMode = buf.readBoolean();
		
		this.tierable = buf.readBoolean();
		if (this.tierable) this.tier = buf.readByte();

		this.hasFluidTank = buf.readBoolean();

		this.fluidNameLen = buf.readInt();

		char[] arr = new char[fluidNameLen];
		for (int i = 0; i < fluidNameLen; i++)
			arr[i] = buf.readChar();

		this.fluidName = new String(arr);

		this.fluidAmount = buf.readInt();

		this.hasHeatLogic = buf.readBoolean();
		this.heatAmount = buf.readInt();
		this.maxHeatAmount = buf.readInt();
		this.heatResistance = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(vec.x);
		buf.writeInt(vec.y);
		buf.writeInt(vec.z);
		buf.writeInt(frontFacing.ordinal());
		buf.writeInt(stored);
		buf.writeBoolean(powerMode);
		
		buf.writeBoolean(tierable);
		if (this.tierable) buf.writeByte(tier);

		buf.writeBoolean(hasFluidTank);
		buf.writeInt(fluidNameLen);

		for (int i = 0; i < fluidNameLen; i++)
			buf.writeChar(fluidName.charAt(i));

		buf.writeInt(fluidAmount);

		buf.writeBoolean(hasHeatLogic);
		buf.writeInt(heatAmount);
		buf.writeInt(maxHeatAmount);
		buf.writeFloat(heatResistance);
	}

	@Override
	public IMessage onMessage(MessageTileEntityGenerator message, MessageContext ctx) {
		TileEntity te = FMLClientHandler.instance().getClient().theWorld.getTileEntity(VectorHelper.toBlockPos(message.vec));
		
		if (te != null && te instanceof AbstractTileEntityGenerator) {
			((AbstractTileEntityGenerator) te).setFrontFacing(message.frontFacing);
			((AbstractTileEntityGenerator) te).setEnergyStored(message.stored);
			((AbstractTileEntityGenerator) te).setPowerMode(message.powerMode);
			
			if (te instanceof TileEntitySolarArray && message.tierable) ((TileEntitySolarArray) te).setTier(message.tier);

			if (te instanceof IFluidContainer && message.hasFluidTank) {
				FluidStack fluidStack = null;

				if (message.fluidNameLen > 0 && message.fluidAmount >= 0)
					fluidStack = new FluidStack(FluidRegistry.getFluid(message.fluidName), message.fluidAmount);

				((IFluidContainer) te).getTank().setFluid(fluidStack);
			}

			if (te instanceof IHeatable && message.hasHeatLogic) {
				HeatLogic heatLogic = ((IHeatable) te).getHeatLogic();

				heatLogic.setCurrentHeat(message.heatAmount);
				heatLogic.setMaxHeat(message.maxHeatAmount);
				heatLogic.setResistance(message.heatResistance);
			}
		}
		
		return null;
	}
}

/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.handler.message;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.mod.tileentity.container.TileEntityRefinery;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

/**
 * TileEntityRefinery message handler for packets.
 *
 * @author hockeyhurd
 * @version 8/10/2015.
 */
public class MessageTileEntityRefinery implements IMessage, IMessageHandler<MessageTileEntityRefinery, IMessage> {

	private TileEntityRefinery te;
	private Vector3<Integer> vec;
	private List<TankData> tanks;
	private int numTanks;
	private int storedPower;
	private boolean powerMode;

	public MessageTileEntityRefinery() {
		vec = Vector3.zero.getVector3i();
	}

	public MessageTileEntityRefinery(TileEntityRefinery te) {
		this.te = te;
		this.vec = te.worldVec();
		numTanks = te.getNumTanks();
		powerMode = te.isPowered();
		storedPower = te.getEnergyStored();

		tanks = new ArrayList<TankData>(numTanks);

		FluidTank current;
		for (int i = 0; i < numTanks; i++) {
			current = te.getTank(i);
			if (current == null || current.getFluid() == null) tanks.add(new TankData(i, -1, 0));
			else tanks.add(new TankData(i, current.getFluid().getFluidID(), current.getFluidAmount()));
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.vec.x = buf.readInt();
		this.vec.y = buf.readInt();
		this.vec.z = buf.readInt();
		this.numTanks = buf.readInt();
		this.storedPower = buf.readInt();
		this.powerMode = buf.readBoolean();

		if (this.numTanks == 0) return; // no need to continue if we have no tanks!
		if (tanks == null) tanks = new ArrayList<TankData>(this.numTanks);

		for (int i = 0; i < this.numTanks; i++) {
			tanks.add(TankData.readFromTankData(buf));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(vec.x);
		buf.writeInt(vec.y);
		buf.writeInt(vec.z);
		buf.writeInt(numTanks);
		buf.writeInt(storedPower);
		buf.writeBoolean(powerMode);

		if (tanks != null && !tanks.isEmpty()) {
			for (TankData data : tanks) {
				// ProjectZed.logHelper.info(data.toString());
				data.writeTankData(buf);
			}
		}
	}

	@Override
	public IMessage onMessage(MessageTileEntityRefinery message, MessageContext ctx) {
		TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.vec.x, message.vec.y, message.vec.z);

		if (tileEntity != null && tileEntity instanceof TileEntityRefinery) {
			TileEntityRefinery te = (TileEntityRefinery) tileEntity;

			te.setEnergyStored(message.storedPower);
			te.setPowered(message.powerMode);

			if (message.tanks != null && !message.tanks.isEmpty()) {

				FluidStack fluidStack;
				for (TankData data : message.tanks) {
					fluidStack = data.getFluidStack();
					// if (fluidStack != null && fluidStack.getFluid() != null) ProjectZed.logHelper.info(data.tankID, fluidStack.getUnlocalizedName());
					te.getTank(data.tankID).setFluid(fluidStack);
				}
			}
		}

		return null;
	}

	/**
	 * Small helper class for storing TankData more effectively.
	 *
	 * @author hockeyhurd
	 * @version 8/10/2015
	 */
	private static final class TankData {

		int tankID, fluidID, fluidAmount;

		private TankData() {
		}

		TankData(int tankID, int fluidID, int fluidAmount) {
			this.tankID = tankID;
			this.fluidID = fluidID;
			this.fluidAmount = fluidAmount;
		}

		FluidTank generateFluidTank(IFluidContainer cont) {
			final FluidTank fluidTank = new FluidTank(cont.getTank().getCapacity());

			if (fluidID >= 0 && fluidAmount > 0) fluidTank.setFluid(new FluidStack(FluidRegistry.getFluid(fluidID), fluidAmount));

			return fluidTank;
		}

		FluidStack getFluidStack() {
			return fluidID >= 0 && fluidAmount > 0 ? new FluidStack(FluidRegistry.getFluid(fluidID), fluidAmount) : null;
		}

		void writeTankData(ByteBuf buf) {
			buf.writeInt(tankID);
			buf.writeInt(fluidID);
			buf.writeInt(fluidAmount);
		}

		static TankData readFromTankData(ByteBuf buf) {
			// TankData ret = new TankData();
			// ret.tankID = buf.readInt();
			// ret.fluidID = buf.readInt();
			// ret.fluidAmount = buf.readInt();

			int tankID = buf.readInt();
			int fluidID = buf.readInt();
			int fluidAmount = buf.readInt();

			return new TankData(tankID, fluidID, fluidAmount);
		}

		@Override
		public String toString() {
			return String.format("tankID: %d, fluidID: %d, fluidAmount: %d", tankID, fluidID, fluidAmount);
		}

	}

}

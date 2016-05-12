/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/

package com.projectzed.mod.tileentity.container.pipe;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.energy.source.IColorComponent;
import com.projectzed.api.fluid.FluidNetwork;
import com.projectzed.api.fluid.FluidNode;
import com.projectzed.api.fluid.IFluidTile;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityLiquiduct;
import com.projectzed.mod.util.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing te code for liquiducts.
 * <br><bold>NOTE: </bold>Although this class isn't abstract, it should be treated like such.
 * 
 * @author hockeyhurd
 * @version Feb 12, 2015
 */
public class TileEntityLiquiductBase extends AbstractTileEntityPipe implements IFluidContainer, IFluidTile, IColorComponent {

	// protected int maxFluidStorage = 2000;
	protected int maxFluidStorage = 0; // temp set '0'
	protected int importRate, exportRate;
	protected FluidTank internalTank; 
	
	protected FluidNetwork network;
	protected boolean isMaster;
	protected FluidStack lastStackTransfer;
	protected boolean transferredLastTick;
	
	protected boolean hadNetworkNBT;
	protected Vector3<Integer> masterVec;
	
	/**
	 * @param name
	 */
	public TileEntityLiquiductBase(String name) {
		super(name);
		internalTank = new FluidTank(this.maxFluidStorage);
		this.importRate = Reference.Constants.BASE_FLUID_TRANSFER_RATE;
		this.exportRate = Reference.Constants.BASE_FLUID_TRANSFER_RATE;
	}

	/**
	 * Function to get the tank associated with this fluid pipe.
	 * 
	 * @return tank object.
	 */
	public FluidTank getTank() {
		return internalTank;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getLocalizedFluidName()
	 */
	@Override
	public String getLocalizedFluidName() {
		if (getTank() == null || getTank().getFluid() == null) return "<empty fluid>";
		
		String ret = getTank().getFluid().getLocalizedName(); 
		return ret != null && ret.length() > 0 ? ret : "<empty fluid>";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getFluidID()
	 */
	@Override
	public int getFluidID() {
		return getTank().getFluid() != null ? getTank().getFluid().getFluidID() : -1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.source.IColorComponent#getColor()
	 */
	@Override
	public EnumColor getColor() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.source.IColorComponent#setColor(com.projectzed.api.energy.source.EnumColor)
	 */
	@Override
	public void setColor(EnumColor color) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getMaxFluidImportRate()
	 */
	@Override
	public int getMaxFluidImportRate() {
		return this.importRate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getMaxFluidExportRate()
	 */
	@Override
	public int getMaxFluidExportRate() {
		return this.exportRate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#isPipe()
	 */
	@Override
	public boolean isPipe() {
		return true;
	}
	
	protected void importContents() {
		if (this.getWorldObj() == null || this.getWorldObj().isRemote) return;
		
		if (this.internalTank.getFluidAmount() > this.maxFluidStorage) {
			FluidStack copy = this.internalTank.getFluid();
			copy.amount = this.maxFluidStorage;
			this.internalTank.setFluid(copy);
			return;
		}
		
		// FluidNet.importFluidFromNeighbors(this, worldObj, xCoord, yCoord, zCoord, lastReceivedDir);
		// FluidNet.tryClearDirectionalTraffic(this, worldObj, xCoord, yCoord, zCoord, lastReceivedDir);
	}

	protected void exportContents() {
		if (this.getWorldObj() == null || this.getWorldObj().isRemote) return;
		if (this.internalTank.getFluidAmount() == 0) return;
		
		// FluidNet.exportFluidToNeighbors(this, worldObj, xCoord, yCoord, zCoord);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateEntity()
	 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		updateNetwork();
		// importContents();
		// exportContents();
		
		if (!this.worldObj.isRemote && this.worldObj.getTotalWorldTime() % 20L == 0) {
			// if (this.lastReceivedDir != ForgeDirection.UNKNOWN) ProjectZed.logHelper.info(this.lastReceivedDir.name());
			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityLiquiduct(this));
		}
		
		// if (!this.getWorldObj().isRemote) System.out.println(getTank().getFluidAmount());
		// if (!this.getWorldObj().isRemote && getTank().getFluidAmount() > 0) ProjectZed.logHelper.info(getTank().getFluidAmount(), lastReceivedDir.name(), worldVec().toString());
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer#getDescriptionPacket()
	 */
	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityLiquiduct(this));
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#worldVec()
	 */
	@Override
	public Vector3<Integer> worldVec() {
		return new Vector3<Integer>(this.xCoord, this.yCoord, this.zCoord);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateConnections()
	 */
	@Override
	protected void updateConnections() {

		IFluidHandler cont = null;
		
		if (this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof IFluidHandler) {
			cont = (IFluidHandler) this.worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
			
			if (cont instanceof TileEntityLiquiductBase) {
				if (((TileEntityLiquiductBase) cont).getColor() == this.getColor()) connections[0] = ForgeDirection.UP;
			}
			
			else if (cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(ForgeDirection.UP.getOpposite()) != 0) connections[0] = ForgeDirection.UP;
			}
			
			else connections[0] = ForgeDirection.UP;
		}
		
		else connections[0] = null;
		
		if (this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof IFluidHandler) {
			cont = (IFluidHandler) this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
			
			if (cont instanceof TileEntityLiquiductBase) {
				if (((TileEntityLiquiductBase) cont).getColor() == this.getColor()) connections[1] = ForgeDirection.DOWN;
			}
			
			else if (cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(ForgeDirection.UP.getOpposite()) != 0) connections[1] = ForgeDirection.DOWN;
			}
			
			else connections[1] = ForgeDirection.DOWN;
		}
		
		else connections[1] = null;
		
		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof IFluidHandler) {
			cont = (IFluidHandler) this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
			
			if (cont instanceof TileEntityLiquiductBase) {
				if (((TileEntityLiquiductBase) cont).getColor() == this.getColor()) connections[2] = ForgeDirection.NORTH;
			}
			
			else if (cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(ForgeDirection.NORTH.getOpposite()) != 0) connections[2] = ForgeDirection.NORTH;
			}
			
			else connections[2] = ForgeDirection.NORTH;
		}
		
		else connections[2] = null;
		
		if (this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof IFluidHandler) {
			cont = (IFluidHandler) this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
			
			if (cont instanceof TileEntityLiquiductBase) {
				if (((TileEntityLiquiductBase) cont).getColor() == this.getColor()) connections[3] = ForgeDirection.EAST;
			}
			
			else if (cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(ForgeDirection.EAST.getOpposite()) != 0) connections[3] = ForgeDirection.EAST;
			}
			
			else connections[3] = ForgeDirection.EAST;
		}
		
		else connections[3] = null;
		
		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof IFluidHandler) {
			cont = (IFluidHandler) this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
			
			if (cont instanceof TileEntityLiquiductBase) {
				if (((TileEntityLiquiductBase) cont).getColor() == this.getColor()) connections[4] = ForgeDirection.SOUTH;
			}
			
			else if (cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(ForgeDirection.SOUTH.getOpposite()) != 0) connections[4] = ForgeDirection.SOUTH;
			}
			
			else connections[4] = ForgeDirection.SOUTH;
		}
		
		else connections[4] = null;
		
		if (this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof IFluidHandler) {
			cont = (IFluidHandler) this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
			
			if (cont instanceof TileEntityLiquiductBase) {
				if (((TileEntityLiquiductBase) cont).getColor() == this.getColor()) connections[5] = ForgeDirection.WEST;
			}
			
			else if (cont instanceof IModularFrame) {
				if (((IModularFrame) cont).getSideValve(ForgeDirection.WEST.getOpposite()) != 0) connections[5] = ForgeDirection.WEST;
			}
			
			else connections[5] = ForgeDirection.WEST;
		}
		
		else connections[5] = null;
		
	}

	@Override
	public double distanceTo(Vector3<Integer> vec) {
		return this.worldVec().getNetDifference(vec);
	}

	@Override
	public Vector3<Integer> getOffsetVec(int x, int y, int z) {
		final Vector3<Integer> ret = worldVec();

		ret.x += x;
		ret.y += y;
		ret.z += z;

		return ret;
	}

	@Override
	public float getCost() {
		return 1.0f;
	}

	@Override
	public Block getTile(World world) {
		return world != null ? world.getBlock(xCoord, yCoord, zCoord) : null;
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		this.internalTank.readFromNBT(comp);
		
		isMaster = comp.getBoolean("FluidNetworkIsMaster");
		hadNetworkNBT = comp.getBoolean("FluidNetworkHadNetwork");
		
		if (masterVec == null) masterVec = Vector3.zero.getVector3i();
		
		masterVec.x = comp.getInteger("FluidNetworkMasterVector3X");
		masterVec.y = comp.getInteger("FluidNetworkMasterVector3Y");
		masterVec.z = comp.getInteger("FluidNetworkMasterVector3Z");
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		this.internalTank.writeToNBT(comp);
		
		// if (hasFluidNetwork()) network.getNodeAt(worldVec()).writeToNBT(comp);
		hadNetworkNBT = hasFluidNetwork();
		
		comp.setBoolean("FluidNetworkIsMaster", isMaster);
		comp.setBoolean("FluidNetworkHadNetwork", hadNetworkNBT);
		
		if (hasFluidNetwork()) {
			this.masterVec = network.getMasterNode().worldVec().copy();
			
			comp.setInteger("FluidNetworkMasterVector3X", this.masterVec.x);
			comp.setInteger("FluidNetworkMasterVector3Y", this.masterVec.y);
			comp.setInteger("FluidNetworkMasterVector3Z", this.masterVec.z);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#fill(net.minecraftforge.common.util.ForgeDirection, net.minecraftforge.fluids.FluidStack, boolean)
	 */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (!worldObj.isRemote) {
			
			FluidStack altStack = resource.copy();
			altStack.amount = this.getMaxFluidImportRate();
			
			boolean useAlt = resource.amount > altStack.amount;
			int fillAmount = 0;
			
			if (useAlt) fillAmount = internalTank.fill(altStack, doFill);
			else fillAmount = internalTank.fill(resource, doFill);
			
			if (doFill) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				this.markDirty();
				if (this.getBlockType() != null) worldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord, this.getBlockType());
				
				if (useAlt) FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(altStack, worldObj, xCoord, yCoord, zCoord, this.internalTank));
				else FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(resource, worldObj, xCoord, yCoord, zCoord, this.internalTank, fillAmount));
			}
			
			return fillAmount;
		}
		
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#drain(net.minecraftforge.common.util.ForgeDirection, net.minecraftforge.fluids.FluidStack, boolean)
	 */
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return drain(from, resource, -1, doDrain);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#drain(net.minecraftforge.common.util.ForgeDirection, int, boolean)
	 */
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return drain(from, null, maxDrain, doDrain);
	}
	
	/**
	 * Drains fluid from this block to another.
	 * 
	 * @param from = direction drained from.
	 * @param drainFluid = the fluid drained.
	 * @param drainAmount = amount of fluid drained.
	 * @param doDrain = whether draining should be simulated or not.
	 * @return type and amount of fluid drained.
	 */
	protected FluidStack drain(ForgeDirection from, FluidStack drainFluid, int drainAmount, boolean doDrain) {
		
		if (!worldObj.isRemote) {
			FluidStack drainedFluid = (drainFluid != null && drainFluid.isFluidEqual(internalTank.getFluid())) ? internalTank.drain(
					drainFluid.amount, doDrain) : drainAmount >= 0 ? internalTank.drain(drainAmount, doDrain) : null;
			
			FluidStack altStack = drainedFluid.copy();
			altStack.amount = this.getMaxFluidExportRate();
			boolean useAlt = drainAmount > altStack.amount;
			
			if (doDrain && drainedFluid != null && drainedFluid.amount > 0) {
				this.markDirty();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				worldObj.notifyBlockChange(xCoord, yCoord, zCoord, this.getBlockType());
				
				if (useAlt) FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(altStack, worldObj, xCoord, yCoord, zCoord, this.internalTank));
				else FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(drainedFluid, worldObj, xCoord, yCoord, zCoord, this.internalTank));
			}
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#canFill(net.minecraftforge.common.util.ForgeDirection, net.minecraftforge.fluids.Fluid)
	 */
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if (fluid != null && !isFull()) {
			FluidStack tankFluid = this.internalTank.getFluid();
			
			return tankFluid == null || tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#canDrain(net.minecraftforge.common.util.ForgeDirection, net.minecraftforge.fluids.Fluid)
	 */
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if (fluid != null && this.internalTank.getFluidAmount() > 0) {
			FluidStack tankFluid = this.internalTank.getFluid();
			
			return tankFluid != null && tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraftforge.fluids.IFluidHandler#getTankInfo(net.minecraftforge.common.util.ForgeDirection)
	 */
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { this.internalTank.getInfo() };
	}
	
	/**
	 * Gets whether tank is full or not.
	 * 
	 * @return true if full, else returns false.
	 */
	public boolean isFull() {
		return this.internalTank.getFluidAmount() == this.internalTank.getCapacity();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#canBeSourceNode()
	 */
	@Override
	public boolean canBeSourceNode() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#canBeMaster()
	 */
	@Override
	public boolean canBeMaster() {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#isMaster()
	 */
	@Override
	public boolean isMaster() {
		return isMaster;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#setMaster(boolean)
	 */
	@Override
	public void setMaster(boolean master) {
		this.isMaster = master;
	}
	
	public boolean wasTransferredLastTick() {
		return transferredLastTick;
	}
	
	public FluidStack getTransferredStack() {
		return lastStackTransfer;
	}
	
	@SideOnly(Side.CLIENT)
	public void setLastTransferredStack(FluidStack stack) {
		this.lastStackTransfer = stack;
	}
	
	@SideOnly(Side.CLIENT)
	public void setWasTransferredLastTick(boolean value) {
		this.transferredLastTick = value;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#hasFluidNetwork()
	 */
	@Override
	public boolean hasFluidNetwork() {
		return network != null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#getNetwork()
	 */
	@Override
	public FluidNetwork getNetwork() {
		return network;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.fluid.container.IFluidContainer#setFluidNetwork(com.projectzed.api.fluid.FluidNetwork)
	 */
	@Override
	public void setFluidNetwork(FluidNetwork network) {
		this.network = network;
		this.hadNetworkNBT = network == null;
	}
	
	/**
	 * Method used to void all fluid nodes in the network if this is the master.
	 */
	public void voidNetwork() {
		if (network != null && !network.isEmpty() && isMaster) {
			for (FluidNode node : network.getNodes()) {
				if (node.getFluidContainer() instanceof IFluidContainer) {
					IFluidContainer cont = (IFluidContainer) node.getIFluidContainer();
					cont.setFluidNetwork(null);
				}
			}
		}
	}
	
	/**
	 * Main update method used to add adjacent containers/tanks to fluid network and creating/joining fluid network(s).
	 */
	protected void updateNetwork() {
		if (worldObj.isRemote) return;
		
		// run every half second!
		if (worldObj.getTotalWorldTime() % (20L / 2) != 0) return;
		
		// only run if okayed to 
		if (hadNetworkNBT) {
			// ProjectZed.logHelper.info("isMaster:", isMaster);
			// ProjectZed.logHelper.info("hadNetworkNBT", hadNetworkNBT);
			if (hasFluidNetwork()) {
				// ProjectZed.logHelper.info("Network connection established!");
				hadNetworkNBT = false;
			}
			
			else {
				
				// get surrounding fluid handlers so that the network can track this node's connections!
				List<ForgeDirection> directions = new ArrayList<ForgeDirection>(ForgeDirection.VALID_DIRECTIONS.length);
				
				for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
					TileEntity te = worldObj.getTileEntity(worldVec().x + dir.offsetX, worldVec().y + dir.offsetY, worldVec().z + dir.offsetZ); 
					
					if (te instanceof TileEntityLiquiductBase) {
						TileEntityLiquiductBase cont = (TileEntityLiquiductBase) te;
						if (cont.getColor() == getColor() && cont.hasFluidNetwork()) directions.add(dir);
					}
					
					else if (te instanceof IFluidHandler) directions.add(dir);
				}
				
				if (isMaster) {
					
					// create new fluid network.
					if (!hasFluidNetwork()) {
						directions.add(ForgeDirection.UNKNOWN);
						network = new FluidNetwork(this.worldObj, new FluidNode(this, directions.toArray(new ForgeDirection[directions.size()]), worldVec()));
					}
					
					hadNetworkNBT = false;
				}
				
				// not master, contact TileEntity and obtain/add to fluid network this node.
				else {
					// ProjectZed.logHelper.info("Still waiting for hadNetworkNBT, returning..");
					if (this.masterVec == null) return;
					
					IFluidContainer master = (IFluidContainer) worldObj.getTileEntity(this.masterVec.x, this.masterVec.y, this.masterVec.z);
					
					if (master != null && master.hasFluidNetwork()) {
						this.network = master.getNetwork();
						this.network.add(new FluidNode(this, directions.toArray(new ForgeDirection[directions.size()]), worldVec()));
						
						hadNetworkNBT = false;
					}
				}
				
				return;
			}
		}
		
		if (!hasFluidNetwork()) {
			List<ForgeDirection> directions = new ArrayList<ForgeDirection>(ForgeDirection.VALID_DIRECTIONS.length);
			
			// ProjectZed.logHelper.info("Help me find a network!");
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				TileEntity te = worldObj.getTileEntity(worldVec().x + dir.offsetX, worldVec().y + dir.offsetY, worldVec().z + dir.offsetZ); 
				
				if (te instanceof TileEntityLiquiductBase) {
					TileEntityLiquiductBase cont = (TileEntityLiquiductBase) te;
					if (cont.getColor() == getColor() && cont.hasFluidNetwork()) {
						network = cont.getNetwork();
						directions.add(dir);
						// ProjectZed.logHelper.info("Yeah! I found a network and decided to join the fun!", cont.getNetwork().size());
					}
				}
			}
			
			// create new fluid network.
			if (!hasFluidNetwork()) {
				directions.add(ForgeDirection.UNKNOWN);
				network = new FluidNetwork(this.worldObj, new FluidNode(this, directions.toArray(new ForgeDirection[directions.size()]), worldVec()));
				// ProjectZed.logHelper.info("Network still not found, guess I'll start my own!");
			}
			
			// if has a fluid network, add this node to the current network.
			else network.add(new FluidNode(this, directions.toArray(new ForgeDirection[directions.size()]), worldVec()));
		}
		
		// if this node is apart of the fluid network check for other nodes to add and update if is master node.
		else {
			
			// merging networks together if applicable.
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				TileEntity te = worldObj.getTileEntity(worldVec().x + dir.offsetX, worldVec().y + dir.offsetY, worldVec().z + dir.offsetZ);
				if (te == null) continue;
				
				if (te instanceof IFluidHandler) {
					if (te instanceof TileEntityLiquiductBase) {
						TileEntityLiquiductBase te2 = (TileEntityLiquiductBase) te;
						
						if (te2.hasFluidNetwork() && !te2.network.equals(network)) {
							TileEntityLiquiductBase master = (TileEntityLiquiductBase) te2.getNetwork().getMasterNode().getFluidContainer();
							
							// merge smaller (or equal to), other network with this network.
							if (te2.getNetwork().size() <= network.size()) {
								network.merge(master.getNetwork());
								master.voidNetwork();
							}
							
							// merge this smaller network with larger, other network.
							else if (te2.getNetwork().size() > network.size()) {
								master.getNetwork().merge(network);
								if (isMaster()) voidNetwork();
							}
						}
						
						continue;
					}
					
					if (!hasFluidNetwork()) continue;
					
					Vector3<Integer> vec = new Vector3<Integer>(te.xCoord, te.yCoord, te.zCoord);
					List<ForgeDirection> directions = new ArrayList<ForgeDirection>(ForgeDirection.VALID_DIRECTIONS.length);
					FluidNode n = network.getNodeAt(vec);
					if (n != null) {
						if (n.getConnections().length >= 1 && n.getConnections()[0] != ForgeDirection.UNKNOWN) {
							for (ForgeDirection con : n.getConnections()) {
								directions.add(con);
							}
						}
					}
					
					directions.add(dir);
					
					network.add(new FluidNode((IFluidHandler) te, directions.toArray(new ForgeDirection[directions.size()]), vec, FluidNode.appropriateValveType((IFluidHandler) te, dir)));
				}
			}
			
			// if this was the first node placed, then we must call update here!
			if (network != null && !network.isEmpty()) {
				
				// if not master, check if we are now the 'master'.
				/*if (!this.isMaster) */this.isMaster = network.isMasterNode(network.getNodeAt(worldVec()));
				
				if (this.isMaster) network.update();
				
				if (network.getTransferringState()) {
					this.transferredLastTick = true;
					this.lastStackTransfer = network.getTransferredFluid().copy();
				}
				
				else {
					this.transferredLastTick = false;
					this.lastStackTransfer = null;
				}
			}
		}
	}

}

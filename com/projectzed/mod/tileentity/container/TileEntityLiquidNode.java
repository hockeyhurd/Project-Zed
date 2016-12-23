/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

/**
 * TileEntity code for liquidNode.
 * 
 * @author hockeyhurd
 * @version Feb 16, 2015
 */
public class TileEntityLiquidNode extends AbstractTileEntityFluidContainer {

	private EnumFacing lastReceivedDir = null;
	byte[] sides = new byte[EnumFacing.VALUES.length];
	private byte cachedMeta;
	
	public TileEntityLiquidNode() {
		super("liquidNode");
		this.maxFluidStorage = 1000;
		if (this.internalTank == null) this.internalTank = new FluidTank(this.maxFluidStorage);
		else this.internalTank.setCapacity(this.maxFluidStorage);
		
		cachedMeta = -1;
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	@Override
	protected void initContentsArray() {
	}

	@Override
	protected void initSlotsArray() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return null;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return false;
	}

	@Override
	protected void importContents() {
		if (this.worldObj == null || this.worldObj.isRemote) return;
		
		if (this.internalTank.getFluidAmount() > this.maxFluidStorage) {
			FluidStack copy = this.internalTank.getFluid();
			copy.amount = this.maxFluidStorage;
			this.internalTank.setFluid(copy);
		}
		
		// FluidNet.importFluidFromNeighbors(this, worldObj, xCoord, yCoord, zCoord, lastReceivedDir);
		// FluidNet.tryClearDirectionalTraffic(this, worldObj, xCoord, yCoord, zCoord, lastReceivedDir);
	}
	
	protected void exportContents() {
		if (this.worldObj == null || this.worldObj.isRemote) return;
		if (this.internalTank.getFluidAmount() == 0) return;
		if (this.internalTank.getFluid() == null || this.internalTank.getFluid().getFluid() == null) return;
		
		// FluidNet.exportFluidToNeighbors(this, worldObj, xCoord, yCoord, zCoord);
		
		EnumFacing exportSide = null;
		
		for (byte i = 0; i < this.sides.length; i++) {
			if (this.sides[i] == 1) {
				// exportSide = ForgeDirection.getOrientation(i);
				exportSide = EnumFacing.getFront(i);
				break;
			}
		}
		
		if (exportSide != null) {
			final Vector3<Integer> vec = worldVec(); // Float?
			final Vector3<Integer> addVec = new Vector3<Integer>(exportSide.getFrontOffsetX(), exportSide.getFrontOffsetY(), exportSide.getFrontOffsetZ());
			vec.add(addVec);
			TileEntity te = worldObj.getTileEntity(VectorHelper.toBlockPos(vec));
			
			if (te != null && te instanceof IFluidHandler) {
				IFluidHandler tank = (IFluidHandler) te;
				
				if (tank.canFill(exportSide.getOpposite(), this.internalTank.getFluid().getFluid())) {
					FluidStack thisStack = this.getTank().getFluid();
					int amount = getAmountFromTank(tank, thisStack, exportSide.getOpposite());
					
					// if destination tank is empty set to default size.
					if (amount == 0) amount = this.getMaxFluidExportRate();
					
					amount = Math.min(amount, thisStack.amount);
					amount = Math.min(amount, this.getMaxFluidExportRate());
					
					if (amount > 0) {
						FluidStack sendStack = thisStack.copy();
						sendStack.amount = amount;
						
						amount = sendStack.amount = tank.fill(exportSide.getOpposite(), sendStack, false);
						
						this.getTank().drain(amount, true);
						tank.fill(exportSide.getOpposite(), sendStack, true);
					}
				}
			}
		}
	}
	
	private int getAmountFromTank(IFluidHandler tank, FluidStack stack, EnumFacing dir) {
		if (tank != null && stack != null && stack.amount > 0 && dir != null && tank.getTankInfo(dir) != null && tank.getTankInfo(dir).length > 0) {
			for (int i = 0; i < tank.getTankInfo(dir).length; i++) {
				if (tank.getTankInfo(dir)[i].fluid != null && tank.getTankInfo(dir)[i].fluid.amount > 0
						&& tank.getTankInfo(dir)[i].fluid.isFluidEqual(stack)) return tank.getTankInfo(dir)[i].fluid.amount; 
			}
		}
		
		return 0;
	}
	
	@Override
	public void update() {
		// super.updateEntity();
		exportContents();
		
		if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0) {
			// byte currentMeta = (byte) (worldObj.getBlockMetadata(worldVec().x, worldVec().y, worldVec().z) - 1);
			final IBlockState blockState = BlockUtils.getBlock(worldObj, worldVec());
			byte currentMeta = (byte) (blockState.getBlock().getMetaFromState(blockState));

			if (currentMeta != this.cachedMeta) {
				for (EnumFacing dir : EnumFacing.VALUES) {
					if (dir.ordinal() == currentMeta) sides[dir.ordinal()] = 1;
					else sides[dir.ordinal()] = -1;
				}
				
				this.cachedMeta = currentMeta;
				
				this.markDirty();
				// worldObj.markBlockForUpdate(worldVec().x, worldVec().y, worldVec().z);
				worldObj.notifyBlockOfStateChange(pos, blockType);
				worldObj.notifyNeighborsOfStateChange(pos, blockType);
			}
		}
	}

	/*@Override
	public Packet getDescriptionPacket() {
		return null;
	}*/

	@Override
	public NBTTagCompound getUpdateTag() {
		return getTileData();
	}

	@Override
	public boolean canBeSourceNode() {
		return true;
	}
	
	@Override
	public boolean canBeMaster() {
		return false;
	}
	
	// START FLUID API METHODS AND HANDLERS:
	
	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		if (!worldObj.isRemote) {
			if (from != null && sides[from.ordinal()] != -1) return 0;

			int fillAmount = internalTank.fill(resource, doFill);

			if (doFill) {
				worldObj.notifyBlockOfStateChange(pos, blockType);
				worldObj.notifyNeighborsOfStateChange(pos, blockType);

				this.markDirty();
				// if (this.getBlockType() != null) worldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord, this.getBlockType());
				FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(resource, worldObj, pos, this.internalTank, fillAmount));
			}

			return fillAmount;
		}

		return 0;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		return drain(from, resource, -1, doDrain);
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		return drain(from, null, maxDrain, doDrain);
	}

	/**
	 * Drains fluid from this block to another.
	 * 
	 * @param from direction drained from.
	 * @param drainFluid the fluid drained.
	 * @param drainAmount amount of fluid drained.
	 * @param doDrain whether draining should be simulated or not.
	 * @return type and amount of fluid drained.
	 */
	protected FluidStack drain(EnumFacing from, FluidStack drainFluid, int drainAmount, boolean doDrain) {
		if (!worldObj.isRemote) {
			if (from != null && sides[from.ordinal()] != 1) return null;
			
			FluidStack drainedFluid = (drainFluid != null && drainFluid.isFluidEqual(internalTank.getFluid())) ? internalTank.drain(
					drainFluid.amount, doDrain) : drainAmount >= 0 ? internalTank.drain(drainAmount, doDrain) : null;
					
			if (doDrain && drainedFluid != null && drainedFluid.amount > 0) {
				this.markDirty();
				worldObj.notifyBlockOfStateChange(pos, blockType);
				worldObj.notifyNeighborsOfStateChange(pos, blockType);
				FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(drainedFluid, worldObj, pos, this.internalTank, drainedFluid.amount));
			}
			
			return drainedFluid;
		}

		return null;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		if (from != null && sides[from.ordinal()] != -1) return false;
		if (fluid != null && !isFull()) {
			FluidStack tankFluid = this.internalTank.getFluid();
			
			return tankFluid == null || tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}
		
		return false;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		if (from != null && sides[from.ordinal()] != 1) return false;
		if (fluid != null && this.internalTank.getFluidAmount() > 0) {
			FluidStack tankFluid = this.internalTank.getFluid();
			
			return tankFluid != null && tankFluid.isFluidEqual(new FluidStack(fluid, 0));
		}
		
		return false;
	}
	
	@Override
	public EnumFacing getRotatedState(EnumFacing facingDir, IBlockState blockState) {
		if (facingDir == EnumFacing.DOWN || facingDir == EnumFacing.UP) return frontFacing;

		return (frontFacing = frontFacing.rotateY());
	}

}

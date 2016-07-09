/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlock;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.energy.storage.IEnergyContainer;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.api.tileentity.IMultiBlockable;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.block.container.BlockNuclearPowerPort;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityEnergyContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.List;

/**
 * TileEntity code for nuclearPowerPort.
 * 
 * @author hockeyhurd
 * @version Mar 11, 2015
 */
public class TileEntityNuclearPowerPort extends AbstractTileEntityEnergyContainer implements IMultiBlockable<AbstractTileEntityGeneric> {

	private boolean hasMaster;
	private Vector3<Integer> masterVec = Vector3.zero.getVector3i();
	
	public TileEntityNuclearPowerPort() {
		super("nuclearPowerPort");
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
		return new int[0];
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
	public int getMaxImportRate() {
		return 0;
	}

	@Override
	public int getMaxExportRate() {
		if (hasMaster) {
			AbstractTileEntityGenerator te = (AbstractTileEntityGenerator) worldObj.getTileEntity(VectorHelper.toBlockPos(masterVec));
			return te.getMaxExportRate();
		}
		
		return 0;
	}

	@Override
	public int requestPower(IEnergyContainer cont, int amount) {
		if (hasMaster) {
			AbstractTileEntityGenerator te = (AbstractTileEntityGenerator) worldObj.getTileEntity(VectorHelper.toBlockPos(masterVec));
			return te.requestPower(cont, amount);
		}
		
		return 0;
	}

	@Override
	public int addPower(IEnergyContainer cont, int amount) {
		if (hasMaster) {
			AbstractTileEntityGenerator te = (AbstractTileEntityGenerator) worldObj.getTileEntity(VectorHelper.toBlockPos(masterVec));
			return te.addPower(cont, amount);
		}
		
		return 0;
	}

	@Override
	protected void importContents() {
		if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0 && this.hasMaster) {
			AbstractTileEntityGenerator te = (AbstractTileEntityGenerator) worldObj.getTileEntity(VectorHelper.toBlockPos(masterVec));
			
			if (te != null && this.storedPower != te.getEnergyStored()) {
				this.storedPower = te.getEnergyStored();
				PacketHandler.INSTANCE.sendToAll(new MessageTileEntityEnergyContainer(this));
			}
		}
	}

	@Override
	protected void exportContents() {
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityEnergyContainer(this));
		final NBTTagCompound comp = getUpdateTag();
		saveNBT(comp);
		return new SPacketUpdateTileEntity(pos, 1, comp);
	}

	@Override
	public void onDataPacket(NetworkManager manger, SPacketUpdateTileEntity packet) {
		readNBT(packet.getNbtCompound());
		BlockUtils.markBlockForUpdate(worldObj, pos);
	}

	@Override
	public AbstractTileEntityGeneric getInstance() {
		return this;
	}

	@Override
	public AbstractHCoreBlock getBlock() {
		return (AbstractHCoreBlock) ProjectZed.nuclearPowerPort;
	}

	@Override
	public boolean isUnique() {
		return true;
	}

	@Override
	public boolean isSubstituable() {
		return false;
	}

	@Override
	public List<IMultiBlockable> getSubList() {
		return null;
	}

	@Override
	public int getAmountFromSize(int width, int height, int depth) {
		return 1;
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	public void setIsMaster(boolean master) {
	}

	@Override
	public boolean hasMaster() {
		return hasMaster;
	}

	@Override
	public void setHasMaster(boolean master) {
		this.hasMaster = master;
	}

	@Override
	public void setMasterVec(Vector3<Integer> vec) {
		this.masterVec = vec;
		this.setMaxStorage(((AbstractTileEntityGenerator) worldObj.getTileEntity(VectorHelper.toBlockPos(masterVec))).getMaxStorage());
	}

	@Override
	public Vector3<Integer> getMasterVec() {
		return masterVec;
	}

	@Override
	public void reset() {
		this.hasMaster = false;
		this.masterVec = Vector3.zero.getVector3i();
		this.storedPower = 0;
		
		((BlockNuclearPowerPort) blockType).updateMeta(false, worldObj, worldVec());
	}

}

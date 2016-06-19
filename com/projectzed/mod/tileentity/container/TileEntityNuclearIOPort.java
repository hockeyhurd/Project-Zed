/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlock;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.tileentity.IMultiBlockable;
import com.projectzed.api.tileentity.IWrenchable;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.block.container.BlockNuclearIOPort;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityNuclearIOPort;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Class containing tileentity code for nuclearIOPort.
 * 
 * @author hockeyhurd
 * @version Mar 19, 2015
 */
public class TileEntityNuclearIOPort extends AbstractTileEntityNuclearComponent implements IWrenchable {

	// TODO: This class should take over IO of TileEntityNuclearController. For now this TE is set to optional use.

	public static final int MAX_BURN_TIME = 1600; // TODO: Should this be moved?

	private byte meta;
	private int burnTime;
	protected boolean isInputMode;
	
	public TileEntityNuclearIOPort() {
		super("nuclearIOPort");
	}

	@Override
	public String getInventoryName() {
		return "container.nuclearIOPort";
	}
	
	@Override
	protected void initContentsArray() {
		this.slots = new ItemStack[2];
	}
	
	@Override
	public int getSizeInventory() {
		return this.slots.length;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean canUpdate() {
		return true;
	}
	
	@Override
	public void update() {
		super.update();
		
		if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0) {
			byte current = (byte) this.getBlockMetadata();
			if (this.meta != current) setMetaOnUpdate(current);

			PacketHandler.INSTANCE.sendToAllAround(new MessageTileEntityNuclearIOPort(this),
					new TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 0x10));
		}

		else if (worldObj.isRemote && burnTime > 0) {
			burnTime--; // On client side, we can predict that burn time will be decreased each tick, and will be corrected via messages 1/sec.
		}
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0, 1 };
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot != 0 || stack == null) return false;
		return stack.getItem() == ProjectZed.fuelRod || stack.getItemDamage() < stack.getMaxDamage();
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return this.getBlockMetadata() == 1 && isItemValidForSlot(slot, stack);
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return /*this.getBlockMetadata() == 2 &&*/ slot == 1;
	}
	
	/**
	 * Gets the item burn time for stack.
	 * 
	 * @param stack stack to test.
	 * @return item burn time.
	 */
	public static int getItemBurnTime(ItemStack stack) {
		if (stack == null) return 0;
		else {
			Item item = stack.getItem();

			if (item == ProjectZed.fuelRod && stack.getItemDamage() < stack.getMaxDamage()) return MAX_BURN_TIME;
			
			return 0;
		}
	}
	
	/**
	 * Function used to determine if item in slot is fuel.
	 * 
	 * @return true if is fuel, else returns false.
	 */
	public boolean isFuel() {
		return getItemBurnTime(this.slots[0]) > 0;
	}
	
	/**
	 * Method used to consume fuel in given slot.
	 *
	 * @param ouputPort port to send empty fuel cell to if applicable.
	 */
	public void consumeFuel(TileEntityNuclearIOPort ouputPort) {
		if (this.isFuel()) {
			if (this.slots[0] == null) return;
			else {
				ItemStack stack = this.slots[0];
				if (stack.getItemDamage() < stack.getMaxDamage() - 1) {
					stack.setItemDamage(stack.getItemDamage() + 1);
					this.slots[0] = stack;
				}
				
				else {
					this.slots[0] = null;

					// if (ouputPort == null) this.slots[1] = new ItemStack(ProjectZed.emptyFuelRod, 1, 0);
					// else ouputPort.setInventorySlotContents(1, new ItemStack(ProjectZed.emptyFuelRod, 1, 0));
					if (ouputPort == null) this.slots[1] = new ItemStack(ProjectZed.fuelRod, 1, 0);
					else ouputPort.setInventorySlotContents(1, new ItemStack(ProjectZed.fuelRod, 1, 0));
				}
			}
		}
	}

	/**
	 * Function to run a cycle of consuming fuel and setting/resetting applicable data values.
	 *
	 * @param ouputPort port to send resulting data to if applicable.
	 * @return true if successful, else returns false.
	 */
	public boolean runCycle(TileEntityNuclearIOPort ouputPort) {
		boolean flag = false;
		
		if (this.slots[0] != null && isFuel()) {
			if (this.burnTime == 0) {
				this.burnTime = getItemBurnTime(this.slots[0]);
				consumeFuel(ouputPort);
				flag = true;
			}
		}
		
		return flag;
	}
	
	public void tickBurnTime() {
		if (this.burnTime > 0) this.burnTime--;
	}
	
	/**
	 * Gets the current burn time for instance.
	 * 
	 * @return burn time as int value.
	 */
	public int getBurnTime() {
		return this.burnTime;
	}

	/**
	 * Setter method used for syncing with client side.
	 *
	 * @param burnTime Int burn time to set.
	 */
	@SideOnly(Side.CLIENT)
	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}

	/*@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityNuclearIOPort(this));
	}*/

	@Override
	public NBTTagCompound getUpdateTag() {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityNuclearIOPort(this));

		final NBTTagCompound comp = getTileData();
		saveNBT(comp);

		return comp;
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		
		this.meta = comp.getByte("ProjectZedNuclearIOPortMeta");
		this.burnTime = comp.getInteger("ProjectZedNuclearBurnTime");
	}
	
	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);

		comp.setByte("ProjectZedNuclearIOPortMeta", this.meta);
		comp.setInteger("ProjectZedNuclearBurnTime", this.burnTime);
	}
	
	/**
	 * @return the correct meta data for when multiblock structure is 'active'
	 */
	public byte getStoredMeta() {
		return this.meta;
	}
	
	/**
	 * Sets stored meta data as byte.
	 * 
	 * @param meta new meta data value.
	 */
	public void setMetaOnUpdate(byte meta) {
		if (meta > 0) this.meta = meta;
	}
	
	@Override
	public void reset() {
		this.isMaster = false;
		this.hasMaster = false;
		this.masterVec = Vector3.zero.getVector3i();

		final Vector3<Integer> vec = worldVec();
		final IBlockState blockState = BlockUtils.getBlock(worldObj, vec);
		setMetaOnUpdate((byte) blockState.getBlock().getMetaFromState(blockState));
		((BlockNuclearIOPort) blockState).updateMeta(false, worldObj, worldVec());
	}

	@Override
	public boolean isUnique() {
		return false;
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
		return 2;
	}

	@Override
	public AbstractHCoreBlock getBlock() {
		return (AbstractHCoreBlock) ProjectZed.nuclearIOPort;
	}

	@Override
	public EnumFacing getRotatedState(EnumFacing facingDir, IBlockState blockState) {
		return EnumFacing.NORTH;
	}

	@Override
	public EnumFacing getCurrentFacing() {
		return EnumFacing.NORTH;
	}

	@Override
	public void setFrontFacing(EnumFacing facing) {
	}

	@Override
	public boolean canRotateTE() {
		return false;
	}
	
	@Override
	public void onInteract(ItemStack stack, EntityPlayer player, World world, Vector3<Integer> vec) {
		if (!worldObj.isRemote) isInputMode = !isInputMode;
	}

	@Override
	public boolean canSaveDataOnPickup() {
		return true;
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.energy.source.EnumType;
import com.projectzed.api.energy.source.Source;
import com.projectzed.api.tileentity.IMultiBlockable;
import com.projectzed.api.tileentity.IMultiBlockableController;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.block.BlockNuclearChamberWall;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityGenerator;
import com.projectzed.mod.tileentity.container.TileEntityNuclearChamberWall;

/**
 * Class used to calculate and generate power through
 * nuclear fusion.
 * 
 * @author hockeyhurd
 * @version Nov 24, 2014
 */
public class TileEntityNuclearController extends AbstractTileEntityGenerator implements IMultiBlockableController<AbstractTileEntityGenerator> {

	/** Variable tracking whether to use fusion or fission. */
	private boolean fusionMode;
	private boolean poweredLastUpdate = false;
	private boolean poweredThisUpdate = false;
	private int burnTime = 0;
	
	private byte placeDir, size, rel;
	private boolean isMaster, hasMaster;
	private Vector4Helper<Integer> masterVec = Vector4Helper.zero.getVector4i();
	private HashMap<Block, Integer> mbMap;
	private HashMap<Block, List<Vector4Helper<Integer>>> mbMapVec;
	
	public TileEntityNuclearController() {
		super("nuclearController");
		this.maxStored = (int) 1e8;
	}
	
	/**
	 * Sets direction of placed nuclear controller.
	 * 
	 * @param dir direction.
	 * @param size expected size of chamber.
	 */
	public void setPlaceDir(byte dir, byte size, byte rel) {
		this.placeDir = dir;
		this.size = size;
		this.rel = rel;
	}
	
	/**
	 * @return placed direction.
	 */
	public byte getPlaceDir() {
		return this.placeDir;
	}
	
	/**
	 * @return expected chamber size.
	 */
	public byte getChamberSize() {
		return this.size;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#getSizeInventory()
	 */
	public int getSizeInventory() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#getInventoryStackLimit()
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#initContentsArray()
	 */
	protected void initContentsArray() {
		this.slots = new ItemStack[1];
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#initSlotsArray()
	 */
	protected void initSlotsArray() {
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#getAccessibleSlotsFromSide(int)
	 */
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] { 0 };
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#defineSource()
	 */
	public void defineSource() {
		this.source = new Source(EnumType.FISSION);
	}
	
	/**
	 * Handles setting the proper source of this te.
	 * @param type type of source.
	 */
	public void setSource(EnumType type) {
		setSource(type, 1.0f);
	}
	
	/**
	 * Handles setting the proper source of this te.
	 * @param type type of source.
	 * @param modifier modifier amount to offset energy output.
	 */
	public void setSource(EnumType type, float modifier) {
		this.source = new Source(type, modifier);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#canProducePower()
	 */
	@Override
	public boolean canProducePower() {
		boolean flag = false;
		if (worldObj.isRemote) return false;
		else flag = checkMultiBlockForm();
		
		return flag;
	}
	
	/**
	 * Function to check if size of multiblock structure is valid.
	 * 
	 * @return true if valid, else returns false.
	 */
	private boolean isSizeValid() {
		return this.size >= 3 && this.size <= 7;
	}
	
	/**
	 * Get the counter map of all blocks.
	 * 
	 * @return mapping.
	 */
	public HashMap<Block, Integer> getMap() {
		return mbMap;
	}
	
	/**
	 * Get the vector map of all blocks.
	 * 
	 * @return mapping.
	 */
	public HashMap<Block, List<Vector4Helper<Integer>>> getMapVec() {
		return mbMapVec;
	}

	/**
	 * Function used to get the item burn time from given itemstack.
	 * 
	 * @param stack stack to (try) to burn.
	 * @return length or burn time of stack if burnable, else returns false.
	 */
	protected static int getItemBurnTime(ItemStack stack) {
		if (stack == null) return 0;
		else {
			Item item = stack.getItem();

			if (item == ProjectZed.fullFuelRod && stack.getItemDamage() < stack.getMaxDamage()) return 1600;
			
			return 0;
		}
	}

	/**
	 * Function used to determine if item in slot is fuel.
	 * @return true if is fuel, else returns false.
	 */
	protected boolean isFuel() {
		return getItemBurnTime(this.slots[0]) > 0;
	}

	/**
	 * Method used to consume fuel in given slot.
	 */
	protected void consumeFuel() {
		if (this.isFuel()) {
			if (this.slots[0] == null) return;
			else {
				ItemStack stack = this.slots[0];
				if (stack.getItemDamage() < stack.getMaxDamage() - 1) {
					stack.setItemDamage(stack.getItemDamage() + 1);
					this.slots[0] = stack;
				}
				
				else this.slots[0] = new ItemStack(ProjectZed.emptyFuelRod, 1, 0);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#generatePower()
	 */
	@Override
	public void generatePower() {
		if (poweredLastUpdate && this.stored + this.source.getEffectiveSize() <= this.maxStored && this.burnTime > 0) this.stored += this.source.getEffectiveSize();
		if (this.stored > this.maxStored) this.stored = this.maxStored; // Redundancy check.
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#updateEntity()
	 */
	@Override
	public void updateEntity() {
		if (this.worldObj != null && !this.worldObj.isRemote) {
			
			// Small, yet significant optimization to call checking of multiblock structure 1/sec instead of 20/sec.
			if (this.worldObj.getTotalWorldTime() % 20L == 0) poweredLastUpdate = canProducePower();
			
			if (this.slots[0] != null && isFuel() && poweredLastUpdate) {
				if (this.burnTime == 0) {
					this.burnTime = getItemBurnTime(this.slots[0]);
					consumeFuel();
				}
			}
			
			else if (!poweredLastUpdate && this.burnTime > 0) this.burnTime = 0;
			
			if (this.worldObj.getTotalWorldTime() % 20L == 0 && poweredThisUpdate != poweredLastUpdate) resetStructure();

			poweredThisUpdate = poweredLastUpdate;
			this.powerMode = this.burnTime > 0;
			if (this.powerMode) this.burnTime--;
			
			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityGenerator(this));
		}
		
		super.updateEntity();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);
		int time = comp.getInteger("ProjectZedBurnTime");
		this.burnTime = time > 0 ? time : 0;
		
		byte dir = comp.getByte("ProjectZedNuclearDir");
		this.placeDir = (byte) (dir >= 0 && dir < 6 ? dir : this.blockMetadata);

		byte rel = comp.getByte("ProjectZedNuclearRel");
		this.rel = rel > -4 && rel < 4 ? rel : 0;
		
		byte size = comp.getByte("ProjectZedNuclearSize");
		this.size = size > 0 && size <= 7 ? size : 0;
		
		// multiblock stuffs:
		isMaster = comp.getBoolean("ProjectZedIsMaster");
		hasMaster = comp.getBoolean("ProjectZedHasMaster");
		
		if (masterVec == null) masterVec = Vector4Helper.zero.getVector4i();
		masterVec.x = comp.getInteger("ProjectZedMasterX");
		masterVec.y = comp.getInteger("ProjectZedMasterY");
		masterVec.z = comp.getInteger("ProjectZedMasterZ");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		comp.setInteger("ProjectZedBurnTime", this.burnTime);
		comp.setByte("ProjectZedNuclearDir", this.placeDir);
		comp.setByte("ProjectZedNuclearRel", this.rel);
		comp.setByte("ProjectZedNuclearSize", this.size);
		
		// multiblock stuffs:
		comp.setBoolean("ProjectZedIsMaster", isMaster);
		comp.setBoolean("ProjectZedHasMaster", hasMaster);
		comp.setInteger("ProjectZedMasterX", masterVec.x);
		comp.setInteger("ProjectZedMasterY", masterVec.y);
		comp.setInteger("ProjectZedMasterZ", masterVec.z);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#getInstance()
	 */
	@Override
	public AbstractTileEntityGenerator getInstance() {
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#getBlock()
	 */
	@Override
	public Block getBlock() {
		return !this.fusionMode ? ProjectZed.fissionController : ProjectZed.fusionController;
	}

	/**
	 * Function to create a fake instance of IMultiBlockable TE.
	 * @param block block to reference.
	 * @return object if valid, else returns false.
	 */
	private IMultiBlockable<?> createFakeTE(Block block) {
		IMultiBlockable<?> mb = null;
		
		if (block != null && block != Blocks.air && block instanceof AbstractBlockNuclearComponent) {
			if (((AbstractBlockNuclearComponent) block).getTileEntity() instanceof IMultiBlockable<?>) {
				mb = (IMultiBlockable<?>) ((AbstractBlockNuclearComponent) block).getTileEntity();
			}
		}
		
		return mb;
	}

	/**
	 * Function to check if reference mappings match expected mappings.
	 *
	 * @param ref reference map with number of TE's.
	 * @param refVec reference map with coordinate of each TE.
	 * @return true if mapping is valid, else returns false.
	 */
	private boolean isMappingValid(HashMap<Block, Integer> ref, HashMap<Block, List<Vector4Helper<Integer>>> refVec) {
		if (ref == null || ref.size() == 0 || this.size < 3 || refVec == null || refVec.size() == 0) return false;
		
		boolean flag = true;
		
		int counter = 0;
		IMultiBlockable tile;
		for (Block b : ref.keySet()) {
			if (ref.containsKey(b) && refVec.containsKey(b)) {
				for (Vector4Helper<Integer> vec : refVec.get(b)) { 
					tile = (IMultiBlockable) worldObj.getTileEntity(vec.x, vec.y, vec.z);
					if (tile != null) {
						if (!tile.getMasterVec().x.equals(worldVec().x) || !tile.getMasterVec().y.equals(worldVec().y) || !tile.getMasterVec().z.equals(worldVec().z) || !tile.hasMaster()) {
							flag = false;
							break;
						}
						
						if (tile.isSubstituable()) {
							boolean subListContained = false;
							int amount = 0;
							IMultiBlockable instance = null;
							
							for (int i = 0; i < tile.getSubList().size(); i++) {
								instance = (IMultiBlockable) tile.getSubList().get(i);
								if (instance != null && instance.getBlock() != null && ref.containsKey(instance.getBlock())) {
									subListContained = true;
									amount = ref.get(instance.getBlock());
									break;
								}
							}

							if (instance != null && tile.getAmountFromSize(size, size, size) != instance.getAmountFromSize(size, size, size)) {
								flag = false;
								break;
							}
							
							if (ref.get(b) + amount != tile.getAmountFromSize(size, size, size)) {
								flag = false;
								break;
							}
						}
						
						else {
							if (tile.getAmountFromSize(size, size, size) != ref.get(b)) {
								flag = false;
								break;
							}
						}
					}
				}
			}
			
			if (!flag) break;
		}
		
		return flag;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#isUnique()
	 */
	@Override
	public boolean isUnique() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#isSubstituable()
	 */
	@Override
	public boolean isSubstituable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#getSubList()
	 */
	@Override
	public List<IMultiBlockable> getSubList() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#getAmountFromSize(int, int, int)
	 */
	@Override
	public int getAmountFromSize(int width, int height, int depth) {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#isMaster()
	 */
	@Override
	public boolean isMaster() {
		return isMaster;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#setIsMaster(boolean)
	 */
	@Override
	public void setIsMaster(boolean master) {
		this.isMaster = master;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#hasMaster()
	 */
	@Override
	public boolean hasMaster() {
		return hasMaster;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#setHasMaster(boolean)
	 */
	@Override
	public void setHasMaster(boolean master) {
		this.hasMaster = master;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#setMasterVec(com.hockeyhurd.api.math.Vector4Helper)
	 */
	@Override
	public void setMasterVec(Vector4Helper<Integer> vec) {
		this.masterVec = vec;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#getMasterVec()
	 */
	@Override
	public Vector4Helper<Integer> getMasterVec() {
		return masterVec;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockableController#checkMultiBlockForm()
	 */
	@Override
	public boolean checkMultiBlockForm() {
		boolean flag = false;
		
		if (!worldObj.isRemote) {
			// TODO: Verify offsets such that it works past 3x3x3 reaction chamber.
			// TODO: Remove lazy way of checking for all chamber locks, but will do for now.
						
			if (!isSizeValid()) return false;
	
			// int xp = this.xCoord - (placeDir == 1 ? 2 : (placeDir == 3 ? 0 : 1));
			int xp = this.xCoord - (placeDir == 1 ? size - 1 : (placeDir == 3 ? 0 : size / 2));
			int yp = this.yCoord + ((size - 1) / 2);
			// int zp = this.zCoord - (placeDir == 3 ? 1 : (placeDir == 2 ? 2 : (placeDir == 1 ? 1 : 0)));
			int zp = this.zCoord - (placeDir == 3 ? size / 2 : (placeDir == 2 ? size - 1 : (placeDir == 1 ? size / 2 : 0)));
			int counterMaster = 0;
			Vector4Helper<Integer> currentVec;
			boolean show = false;
			mbMap = new HashMap<Block, Integer>();
			mbMapVec = new HashMap<Block, List<Vector4Helper<Integer>>>();
			TileEntity te;
			Block b;
	
			if (show) {
				// System.out.println(size / 2 - 1);
				System.out.println(placeDir);
				System.out.println("1: (" + (xp) + ", " + (yp) + ", " + (zp) + ")");
				System.out.println("2: (" + (xp + size - 1) + ", " + (yp - size + 1) + ", " + (zp + size - 1) + ")");
			}
	
			List<Vector4Helper<Integer>> list;
	
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					for (int z = 0; z < size; z++) {
						currentVec = new Vector4Helper<Integer>(xp + x, yp - y, zp + z);
	
						te = worldObj.getTileEntity(currentVec.x, currentVec.y, currentVec.z);
						if (te != null && te instanceof IMultiBlockable && te.getBlockType() != null && te.getBlockType() != Blocks.air) {
							b = te.getBlockType();
							if (!mbMap.containsKey(b)) mbMap.put(b, 1);
							else mbMap.put(b, mbMap.get(b) + 1);
	
							if (!mbMapVec.containsKey(b)) {
								list = new ArrayList<Vector4Helper<Integer>>();
								list.add(currentVec);
								mbMapVec.put(b, list);
							}
	
							else {
								list = mbMapVec.get(b);
								list.add(currentVec);
								mbMapVec.put(b, list);
							}
	
							if (/* !((IMultiBlockable) te).isMaster() && */!((IMultiBlockable) te).hasMaster()) {
								((IMultiBlockable) te).setHasMaster(true);
								((IMultiBlockable) te).setMasterVec(worldVec());
								
								if (te instanceof TileEntityNuclearChamberWall) {
									((BlockNuclearChamberWall) worldObj.getBlock(currentVec.x, currentVec.y, currentVec.z)).updateStructure(poweredLastUpdate,
											worldObj, currentVec);
								}
							}
	
							else if (((IMultiBlockable) te).isMaster()) counterMaster++;
						}
					}
				}
			}
						
			flag = isMappingValid(mbMap, mbMapVec) && counterMaster == 1;
			
			// ProjectZed.logHelper.info(counterMaster);
			// ProjectZed.logHelper.info(flag ? "working!" : "not working..");
		
		}
		return flag;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockableController#checkForMaster()
	 */
	@Override
	public boolean checkForMaster() {
		return this.isMaster;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockableController#resetStructure()
	 */
	@Override
	public void resetStructure() {
		if (mbMapVec != null && mbMapVec.size() > 0) {
			TileEntity te;
			for (Block b : mbMapVec.keySet()) {
				
				for (Vector4Helper<Integer> vec : mbMapVec.get(b)) {
					te = worldObj.getTileEntity(vec.x, vec.y, vec.z);
					if (te != null && te instanceof IMultiBlockable && !(te instanceof TileEntityNuclearController)) ((IMultiBlockable) te).reset();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#reset()
	 */
	@Override
	public void reset() {
		this.isMaster = false;
		this.hasMaster = false;
		this.masterVec = Vector4Helper.zero.getVector4i();
	}
	
}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.generator;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlock;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.hockeyhurd.hcorelib.api.util.SidedHelper;
import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.block.IMetaUpdate;
import com.projectzed.api.energy.source.EnumType;
import com.projectzed.api.energy.source.Source;
import com.projectzed.api.heat.HeatLogic;
import com.projectzed.api.heat.IHeatable;
import com.projectzed.api.tileentity.IMultiBlockable;
import com.projectzed.api.tileentity.IMultiBlockableController;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityGenerator;
import com.projectzed.mod.handler.message.MessageTileEntityNuclearController;
import com.projectzed.mod.registry.CoolantRegistry;
import com.projectzed.mod.tileentity.TileEntityNuclearControlPort;
import com.projectzed.mod.tileentity.container.TileEntityNuclearIOPort;
import com.projectzed.mod.util.Coolant;
import com.projectzed.mod.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class used to calculate and generate power through
 * nuclear fission/fusion.
 * 
 * @author hockeyhurd
 * @version Nov 24, 2014
 */
public class TileEntityNuclearController extends AbstractTileEntityGenerator implements IMultiBlockableController<AbstractTileEntityGenerator>,
		IHeatable {

	/** Variable tracking whether to use fusion or fission. */
	private boolean fusionMode;
	private boolean poweredLastUpdate = false;
	private boolean poweredThisUpdate = false;

	@SideOnly(Side.CLIENT)
	private int burnTime;

	private int lastStored;
	private byte placeDir, size, rel;
	private boolean isMaster, hasMaster;
	private Vector3<Integer> masterVec = Vector3.zero.getVector3i();
	private Vector3<Integer> minVec = Vector3.zero.getVector3i();
	private Vector3<Integer> maxVec = Vector3.zero.getVector3i();
	private HashMap<Block, Integer> mbMap;
	private HashMap<Block, List<Vector3<Integer>>> mbMapVec;
	private List<Coolant> coolantList;
	private TileEntityNuclearIOPort inputPort, outputPort;
	private HeatLogic heatLogic;

	public TileEntityNuclearController() {
		super("nuclearController");
		this.maxStored = (int) 1e8;
		
		heatLogic = new HeatLogic(2500, 0.05f);
		// heatLogic = new HeatLogic(2500000, 0.05f);
	}

	@Override
	public HeatLogic getHeatLogic() {
		return heatLogic;
	}

	@Override
	public boolean isOverheated() {
		return heatLogic.getHeat() == heatLogic.getMaxHeat();
	}

	/**
	 * Sets direction of placed nuclear controller.
	 * 
	 * @param dir direction.
	 * @param size expected size of chamber.
	 * @param rel relative size.
	 */
	public void setPlaceDir(byte dir, byte size, byte rel) {
		this.placeDir = dir;
		this.size = size;
		this.rel = rel;

		heatLogic.setMaxHeat(HeatLogic.getHeatFromValues(maxStored, getReactorVolume()));
	}

	/**
	 * Gets the reactor's current volume.
	 *
	 * @return Integer volume of nuclear reactor.
	 */
	public int getReactorVolume() {
		return size * size * size;
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
	
	@Override
	public boolean canProducePower() {
		if (worldObj.isRemote) return false;

		return checkMultiBlockForm() && checkCorners();
	}

	/**
	 * Gets the burn time of the reactor.
	 * <br><bold>NOTE: </bold>This is side dependent.
	 *
	 * @return Int burn time left in the reactor.
	 */
	public int getBurnTime() {
		final boolean isServer = SidedHelper.isServer();

		return isServer && inputPort != null ? inputPort.getBurnTime() : !isServer ? burnTime : 0;
	}

	/**
	 * Sets the burn time in the client.
	 *
	 * @param burnTime Int burn time to set.
	 */
	@SideOnly(Side.CLIENT)
	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}

	/**
	 * Function to check if size of multiblock structure is valid.
	 * 
	 * @return true if valid, else returns false.
	 */
	private boolean isSizeValid() {
		return this.size >= 3 && this.size <= 9;
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
	public HashMap<Block, List<Vector3<Integer>>> getMapVec() {
		return mbMapVec;
	}
	
	@Override
	public void generatePower() {
		// if (poweredLastUpdate && this.stored + this.source.getEffectiveSize() <= this.maxStored && inputPort.getBurnTime() > 0) {
		if (poweredLastUpdate && inputPort.getBurnTime() > 0) {
			final int preStored = this.stored;
			// int anmoutToGen = HeatLogic.getEnergyFromTemp(heatLogic.getHeat(), getReactorVolume());
			int anmoutToGen = this.source.getEffectiveSize();

			// anmoutToGen = Math.min(anmoutToGen, source.getEffectiveSize());
			// ProjectZed.logHelper.info("anmoutToGen:", anmoutToGen, "Current Heat:", heatLogic.getHeat());

			// if (this.stored + this.source.getEffectiveSize() <= this.maxStored) this.stored += this.source.getEffectiveSize();
			if (this.stored + anmoutToGen <= this.maxStored) this.stored += anmoutToGen;
			else this.stored = this.maxStored;

			// if (preStored < this.stored) heatLogic.update(true, this.stored, this.getChamberSize());
			// else heatLogic.update(false, this.stored, this.getChamberSize());
			// heatLogic.update(preStored < this.stored, this.stored, this.getChamberSize());
			heatLogic.update(preStored < this.stored, this.stored - this.lastStored, this.getReactorVolume());

			this.lastStored = this.stored;
		}

		if (this.stored > this.maxStored) this.stored = this.maxStored; // Redundancy check.
	}
	
	/**
	 * Gets the 'first' input nuclear io part in mapping.
	 * 
	 * @return nuclear io port object if found, else returns false.
	 */
	private TileEntityNuclearIOPort getInputDataFromIO() {
		TileEntityNuclearIOPort te = null;
		
		if (mbMapVec != null && mbMapVec.size() > 0 && mbMapVec.containsKey(ProjectZed.nuclearIOPort)) {
			
			for (Vector3<Integer> vec : mbMapVec.get(ProjectZed.nuclearIOPort)) {
				// byte meta = (byte) worldObj.getBlockMetadata(vec.x, vec.y, vec.z);
				int meta = BlockUtils.getBlockMetadata(worldObj, vec);

				// it is input!
				final TileEntity tileEntity = worldObj.getTileEntity(VectorHelper.toBlockPos(vec));
				if (meta == 1 && tileEntity instanceof TileEntityNuclearIOPort) {
					te = (TileEntityNuclearIOPort) tileEntity;
					break;
				}
			}
		}
		
		return te;
	}

	/**
	 * Gets the 'first' ouput nuclear io part in mapping.
	 *
	 * @return nuclear io port object if found, else returns false.
	 */
	private TileEntityNuclearIOPort getOutputDataFromIO() {
		TileEntityNuclearIOPort te = null;

		if (mbMapVec != null && mbMapVec.size() > 0 && mbMapVec.containsKey(ProjectZed.nuclearIOPort)) {

			for (Vector3<Integer> vec : mbMapVec.get(ProjectZed.nuclearIOPort)) {
				// byte meta = (byte) worldObj.getBlockMetadata(vec.x, vec.y, vec.z);
				int meta = BlockUtils.getBlockMetadata(worldObj, vec);

				final TileEntity tileEntity = worldObj.getTileEntity(VectorHelper.toBlockPos(vec));
				if (meta == 2 && tileEntity instanceof  TileEntityNuclearIOPort) {
					te = (TileEntityNuclearIOPort) tileEntity;
					break;
				}
			}
		}

		return te;
	}
	
	/**
	 * Checks and consumes input port fuel.
	 *  
	 * @param controlPort flag whether control port is appropriately set.
	 * @return true if successful, else returns false.
	 */
	public boolean checksAndConsumptions(boolean controlPort) {
		return inputPort != null && poweredLastUpdate && controlPort && this.stored < this.maxStored && inputPort.runCycle(outputPort);

		/*boolean flag = false;

		if (inputPort != null) flag = poweredLastUpdate && controlPort && this.stored < this.maxStored && inputPort.runCycle(outputPort);

		return flag;*/
	}

	/**
	 * Checks the coolants in the reactor and reports data to the HeatLogic system
	 * if successful, else if an error has occured, we will return 'FALSE'.
	 *
	 * @return True if successful, else may return false.
	 */
	public boolean checkFluids() {

		// If no fluids, something went wrong...
		if (coolantList == null || coolantList.isEmpty()) return false;

		// Only 1 coolant, makes life easier!
		else if (coolantList.size() == 1) {
			final float effectiveEfficiency = coolantList.get(0).getEffectiveEfficiency(size, size, size, 1);

			heatLogic.setResistance(effectiveEfficiency);
		}

		// We'll see what we can do...
		else {

			// For now, we will just go with the lowest efficiency coolant.
			// TODO: Implement variable efficiency based on percentages of coolants.

			float lowestEff = Float.MAX_VALUE;

			for (Coolant coolant : coolantList) {
				float currentEff = coolant.getEffectiveEfficiency(size, size, size, 1);

				if (currentEff < lowestEff) lowestEff = currentEff;
			}

			heatLogic.setResistance(lowestEff);
		}

		return true;
	}

	/**
	 * Method to handle cooling of Nuclear Reactor.
	 */
	public void doCooling() {
		if (worldObj.isRemote || worldObj.getTotalWorldTime() % 5 != 0) return; // Used to 'slow cooling process'

		final int dif = stored - lastStored;
		heatLogic.update(powerMode, dif != 0 ? dif : -1, getReactorVolume());
	}
	
	@Override
	public void update() {
		if (this.worldObj != null && !this.worldObj.isRemote) {
			
			// Small, yet significant optimization to call checking of multiblock structure 1/sec instead of 20/sec.
			if (this.worldObj.getTotalWorldTime() % 20L == 0) {
				inputPort = getInputDataFromIO();
				if (inputPort == null) return;

				outputPort = getOutputDataFromIO();

				poweredLastUpdate = canProducePower();
			}

			// if (inputPort == null) return;

			final boolean controlPort = checkControlPort();
			final boolean consumptions = checksAndConsumptions(controlPort);
			final boolean checkFluids = checkFluids();

			// if (coolantList != null && !coolantList.isEmpty()) ProjectZed.logHelper.info(coolantList.size());
			// if (coolantList != null && !coolantList.isEmpty()) ProjectZed.logHelper.info(coolantList.get(0).getAmount());

			if (this.worldObj.getTotalWorldTime() % 20L == 0 && poweredThisUpdate != poweredLastUpdate) resetStructure();

			poweredThisUpdate = poweredLastUpdate;
			this.powerMode = inputPort != null && inputPort.getBurnTime() > 0;
			if (this.powerMode && controlPort) {
				generatePower();
				inputPort.tickBurnTime();
			}

			// Machine is off!
			else if (!this.powerMode) doCooling();

			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityGenerator(this));
			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityNuclearController(this));
			this.markDirty();

			// if (this.worldObj.getTotalWorldTime() % 20L == 0) ProjectZed.logHelper.info("Heat:", heatLogic.getHeat());
			// ProjectZed.logHelper.info("Max heat:", heatLogic.getMaxHeat());
		}
		
	}
	
	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);

		this.lastStored = this.stored;

		byte dir = comp.getByte("ProjectZedNuclearDir");
		this.placeDir = (byte) (dir >= 0 && dir < 6 ? dir : this.getBlockMetadata());

		byte rel = comp.getByte("ProjectZedNuclearRel");
		this.rel = rel > -5 && rel < 5 ? rel : 0;
		
		byte size = comp.getByte("ProjectZedNuclearSize");
		this.size = size > 0 && size <= 9 ? size : 0;
		
		// multiblock stuffs:
		isMaster = comp.getBoolean("ProjectZedIsMaster");
		hasMaster = comp.getBoolean("ProjectZedHasMaster");
		
		if (masterVec == null) masterVec = Vector3.zero.getVector3i();
		masterVec.x = comp.getInteger("ProjectZedMasterX");
		masterVec.y = comp.getInteger("ProjectZedMasterY");
		masterVec.z = comp.getInteger("ProjectZedMasterZ");

		heatLogic.readNBT(comp);
	}
	
	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);

		this.lastStored = this.stored;

		comp.setByte("ProjectZedNuclearDir", this.placeDir);
		comp.setByte("ProjectZedNuclearRel", this.rel);
		comp.setByte("ProjectZedNuclearSize", this.size);
		
		// multiblock stuffs:
		comp.setBoolean("ProjectZedIsMaster", isMaster);
		comp.setBoolean("ProjectZedHasMaster", hasMaster);
		comp.setInteger("ProjectZedMasterX", masterVec.x);
		comp.setInteger("ProjectZedMasterY", masterVec.y);
		comp.setInteger("ProjectZedMasterZ", masterVec.z);

		heatLogic.saveNBT(comp);
	}

	/*@Override
	public Packet getDescriptionPacket() {
		super.getDescriptionPacket();
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityNuclearController(this));
	}*/

	@Override
	public NBTTagCompound getUpdateTag() {
		final NBTTagCompound comp = super.getUpdateTag();
		saveNBT(comp);

		return comp;
	}

	/**
	 * Simple getter function to check whether the control rod permits energy generation.
	 * 
	 * @return true if permittable, else returns false.
	 */
	private boolean checkControlPort() {
		boolean flag = true;
		
		if (mbMapVec != null && mbMapVec.size() > 0 && mbMapVec.containsKey(ProjectZed.nuclearControlPort)) {
			Vector3<Integer> vec = mbMapVec.get(ProjectZed.nuclearControlPort).get(0);
			TileEntityNuclearControlPort te = (TileEntityNuclearControlPort) worldObj.getTileEntity(VectorHelper.toBlockPos(vec));
			
			if (te != null && te.hasRedstoneSignal()) flag = false;
		}
		
		return flag;
	}
	
	@Override
	public AbstractTileEntityGenerator getInstance() {
		return this;
	}
	
	@Override
	public AbstractHCoreBlock getBlock() {
		return (AbstractHCoreBlock) (!this.fusionMode ? ProjectZed.fissionController : ProjectZed.fusionController);
	}

	/**
	 * Function to create a fake instance of IMultiBlockable TE.
	 * 
	 * @param block block to reference.
	 * @return object if valid, else returns false.
	 */
	private IMultiBlockable<?> createFakeTE(Block block) {
		IMultiBlockable<?> mb = null;
		
		if (block != null && block != Blocks.AIR && block instanceof AbstractBlockNuclearComponent) {
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
	private boolean isMappingValid(HashMap<Block, Integer> ref, HashMap<Block, List<Vector3<Integer>>> refVec) {
		if (ref == null || ref.size() == 0 || this.size < 3 || refVec == null || refVec.size() == 0) return false;
		
		boolean flag = true;
		
		IMultiBlockable tile;
		for (Block b : ref.keySet()) {
			if (ref.containsKey(b) && refVec.containsKey(b)) {
				for (Vector3<Integer> vec : refVec.get(b)) { 
					tile = (IMultiBlockable) worldObj.getTileEntity(VectorHelper.toBlockPos(vec));
					if (tile != null) {
						if (!tile.getMasterVec().x.equals(worldVec().x) || !tile.getMasterVec().y.equals(worldVec().y) || !tile.getMasterVec().z.equals(worldVec().z) || !tile.hasMaster()) {
							flag = false;
							break;
						}
						
						if (tile.isSubstitutable()) {
							boolean subListContained = false;
							int amount = 0;
							int maxTarget = Integer.MIN_VALUE;
							IMultiBlockable instance = null;
							
							for (int i = 0; i < tile.getSubList().size(); i++) {
								instance = (IMultiBlockable) tile.getSubList().get(i);
								if (instance != null && instance.getBlock() != null && ref.containsKey(instance.getBlock())) {
									subListContained = true;
									maxTarget = Math.max(maxTarget, instance.getAmountFromSize(size, size, size));
									amount += ref.get(instance.getBlock());
								}
							}

							// amount += tile.getAmountFromSize(size, size, size);
							amount += ref.get(b);
							
							if (!subListContained && maxTarget <= 0) {
								maxTarget = tile.getAmountFromSize(size, size, size);
								if (amount != maxTarget) {
									flag = false;
									break;
								}
							}
							
							else if (subListContained && maxTarget > 0) {
								maxTarget = Math.max(maxTarget, tile.getAmountFromSize(size, size, size));
								if (amount != maxTarget) {
									flag = false;
									break;
								}
							}
									
							/*if (!subListContained || (amount != maxTarget && maxTarget > 0)) {
								flag = false;
								break;
							}*/
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
	
	@Override
	public boolean isUnique() {
		return true;
	}

	@Override
	public boolean isSubstitutable() {
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
		return isMaster;
	}

	@Override
	public void setIsMaster(boolean master) {
		this.isMaster = master;
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
	}

	@Override
	public Vector3<Integer> getMasterVec() {
		return masterVec;
	}

	@Override
	public boolean checkMultiBlockForm() {
		boolean flag = false;
		
		if (!worldObj.isRemote) {
			// TODO: Remove lazy way of checking for all chamber locks, but will do for now.
						
			if (!isSizeValid()) return false;
			if (minVec == null) minVec = Vector3.zero.getVector3i();
			if (maxVec == null) maxVec = Vector3.zero.getVector3i();

			// int xp = this.xCoord - (placeDir == 1 ? 2 : (placeDir == 3 ? 0 : 1));
			minVec.x = pos.getX() - (placeDir == 1 ? size - 1 : (placeDir == 3 ? 0 : size / 2));
			minVec.y = pos.getY() + ((size - 1) / 2);
			// int zp = this.zCoord - (placeDir == 3 ? 1 : (placeDir == 2 ? 2 : (placeDir == 1 ? 1 : 0)));
			minVec.z = pos.getZ() - (placeDir == 3 ? size / 2 : (placeDir == 2 ? size - 1 : (placeDir == 1 ? size / 2 : 0)));
			
			maxVec.x = minVec.x + size - 1;
			maxVec.y = minVec.y - size + 1;
			maxVec.z = minVec.z + size - 1;
			
			int counterMaster = 0;
			Vector3<Integer> currentVec;
			BlockPos currentBlockPos;
			Block currentBlock;

			int counter = 0;

			if (mbMap == null) mbMap = new HashMap<Block, Integer>();
			else if (!mbMap.isEmpty()) mbMap.clear();

			if (mbMapVec == null) mbMapVec = new HashMap<Block, List<Vector3<Integer>>>();
			else if (!mbMapVec.isEmpty()) mbMapVec.clear();

			if (coolantList == null) coolantList = new ArrayList<Coolant>(Coolant.calculateChamberSize(size, size, size, 1));
			else if (!coolantList.isEmpty()) coolantList.clear();

			TileEntity te;
			Block b;
			Fluid fluid;
	
			/*if (ProjectZed.configHandler.isDebugMode()) {
				// System.out.println(size / 2 - 1);
				System.out.println(placeDir);
				System.out.println("1: (" + (minVec.x) + ", " + (minVec.y) + ", " + (minVec.z) + ")");
				System.out.println("2: (" + (minVec.x + size - 1) + ", " + (minVec.y - size + 1) + ", " + (minVec.z + size - 1) + ")");
			}*/
	
			List<Vector3<Integer>> list;
	
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					for (int z = 0; z < size; z++) {
						
						currentVec = new Vector3<Integer>(minVec.x + x, minVec.y - y, minVec.z + z);
						currentBlockPos = VectorHelper.toBlockPos(currentVec);
						// currentBlock = worldObj.getBlock(currentBlockPos);
						currentBlock = BlockUtils.getBlock(worldObj, currentBlockPos).getBlock();
						// ProjectZed.logHelper.info(currentBlock.getUnlocalizedName());
						
						if ( (y > 0 && y < size - 1) && (x > 0 && x < size - 1) && (z > 0 && z < size - 1) ) {
							if ( !((y == (size - 1) / 2) && (x == (size - 1) / 2) && (z == (size - 1) / 2)) ) {
								if (!(currentBlock instanceof BlockFluidBase) && !(currentBlock instanceof BlockLiquid) && currentBlock != Blocks.AIR)
									return false;
								
								// fluid = FluidRegistry.lookupFluidForBlock(currentBlock);
								// if (fluid != null && !coolantList.containsKey(fluid)) coolantList.put(fluid, false);
								// if (/*fluid == null*/ !coolantList.containsKey(fluid) && currentBlock != Blocks.air) return false;

								if (currentBlock instanceof BlockFluidBase) {
									fluid = ((BlockFluidBase) currentBlock).getFluid();

									if (CoolantRegistry.instance().isFluidInRegistry(fluid)) {
										Coolant coolant = CoolantRegistry.instance().getCoolantByFluid(fluid);
										coolant.setAmount(Reference.Constants.MILLI_BUCKETS_PER_BLOCK_SPACE);

										if (coolantList.isEmpty()) coolantList.add(coolant);
										else {
											for (Coolant currentCoolant : coolantList) {
												if (currentCoolant.isFluidEqual(coolant)) {
													currentCoolant.addAmount(coolant.getAmount());
													break;
												}
											}
										}
									}
								}

								// Typically water for lava in vanilla.
								else if (currentBlock instanceof BlockLiquid) {

									// If block is water, set fluid to water, else treat it like lava and watch it burn!!!
									fluid = currentBlock == Blocks.WATER ? FluidRegistry.WATER : FluidRegistry.LAVA;

									if (CoolantRegistry.instance().isFluidInRegistry(fluid)) {
										Coolant coolant = CoolantRegistry.instance().getCoolantByFluid(fluid);
										coolant.setAmount(Reference.Constants.MILLI_BUCKETS_PER_BLOCK_SPACE);

										if (coolantList.isEmpty()) coolantList.add(coolant);
										else {
											for (Coolant currentCoolant : coolantList) {
												if (currentCoolant.isFluidEqual(coolant)) {
													currentCoolant.addAmount(coolant.getAmount());
													break;
												}
											}
										}
									}
								}

								// If block is air, deal with it!
								else if (currentBlock == Blocks.AIR) {
									Coolant airCoolant = Coolant.AIR;
									airCoolant.setAmount(Reference.Constants.MILLI_BUCKETS_PER_BLOCK_SPACE);

									if (coolantList.isEmpty()) coolantList.add(airCoolant);
									else {
										for (Coolant currentCoolant : coolantList) {
											if (currentCoolant.isFluidEqual(airCoolant)) {
												currentCoolant.addAmount(airCoolant.getAmount());
												break;
											}
										}
									}
								}
							}
						}

						te = worldObj.getTileEntity(currentBlockPos);
						if (te != null && te instanceof IMultiBlockable && te.getBlockType() != null && te.getBlockType() != Blocks.AIR) {
							counter++;
							b = te.getBlockType();
							if (!mbMap.containsKey(b)) mbMap.put(b, 1);
							else mbMap.put(b, mbMap.get(b) + 1);

							if (!mbMapVec.containsKey(b)) {
								list = new ArrayList<Vector3<Integer>>();
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

								if (currentBlock != null && currentBlock instanceof IMetaUpdate)
									((IMetaUpdate) currentBlock).updateMeta(poweredLastUpdate, worldObj, currentVec);
							}

							else if (((IMultiBlockable) te).isMaster()) counterMaster++;
						}
					}
				}
			}
						
			// ProjectZed.logHelper.info(counter, (size * size * size) - ((size - 2) * (size - 2) * (size - 2)) + 1);
			flag = isMappingValid(mbMap, mbMapVec) && counterMaster == 1 && counter == (size * size * size) - ((size - 2) * (size - 2) * (size - 2)) + 1;
			
			// ProjectZed.logHelper.info(counterMaster);
			// ProjectZed.logHelper.info(flag ? "working!" : "not working..");
		
		}
		return flag;
	}
	
	private boolean checkCorners() {
		boolean flag = false;
		
		if (mbMapVec != null && mbMapVec.size() > 0 && mbMapVec.containsKey(ProjectZed.nuclearChamberLock) && mbMapVec.get(ProjectZed.nuclearChamberLock).size() > 0) {
			flag = true;
			IMultiBlockable current;
			
			for (Vector3<Integer> vec : mbMapVec.get(ProjectZed.nuclearChamberLock)) {
				if (vec.equals(minVec)) continue;
				else if (vec.equals(maxVec)) continue;
				
				else if (vec.y.equals(minVec.y) && vec.x.equals(minVec.x) && vec.z.equals(maxVec.z)) continue;
				else if (vec.y.equals(maxVec.y) && vec.x.equals(minVec.x) && vec.z.equals(maxVec.z)) continue;
				else if (vec.y.equals(minVec.y) && vec.x.equals(maxVec.x) && vec.z.equals(minVec.z)) continue;
				else if (vec.y.equals(maxVec.y) && vec.x.equals(maxVec.x) && vec.z.equals(minVec.z)) continue;
				else if (vec.y.equals(minVec.y) && vec.x.equals(maxVec.x) && vec.z.equals(maxVec.z)) continue;
				else if (vec.y.equals(maxVec.y) && vec.x.equals(minVec.x) && vec.z.equals(minVec.z)) continue;
				
				else {
					flag = false;
					break;
				}
			}
		}
		
		return flag;
	}

	@Override
	public boolean checkForMaster() {
		return this.isMaster;
	}

	@Override
	public void resetStructure() {
		if (mbMapVec != null && mbMapVec.size() > 0) {
			TileEntity te;
			for (Block b : mbMapVec.keySet()) {
				
				for (Vector3<Integer> vec : mbMapVec.get(b)) {
					te = worldObj.getTileEntity(VectorHelper.toBlockPos(vec));
					if (te != null && te instanceof IMultiBlockable && !(te instanceof TileEntityNuclearController)) ((IMultiBlockable) te).reset();
				}
			}
		}
	}

	@Override
	public void reset() {
		this.isMaster = false;
		this.hasMaster = false;
		this.masterVec = Vector3.zero.getVector3i();
	}
	
}

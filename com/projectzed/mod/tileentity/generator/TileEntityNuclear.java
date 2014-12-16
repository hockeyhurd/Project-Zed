package com.projectzed.mod.tileentity.generator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;

import com.projectzed.api.energy.source.EnumType;
import com.projectzed.api.energy.source.Source;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.block.BlockNuclearChamberLock;
import com.projectzed.mod.block.BlockNuclearChamberWall;
import com.projectzed.mod.block.BlockNuclearReactantCore;
import com.projectzed.mod.block.BlockThickenedGlass;
import com.projectzed.mod.block.generator.BlockNuclearController;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityGenerator;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Class used to calculate and generate power through
 * nuclear fusion.
 * 
 * @author hockeyhurd
 * @version Nov 24, 2014
 */
public class TileEntityNuclear extends AbstractTileEntityGenerator {

	/** Variable tracking whether to use fusion or fission. */
	private boolean fusionMode;
	private int burnTime = 0;
	
	private byte placeDir, size, rel;
	private Block[] blocksArray;
	
	public TileEntityNuclear() {
		super("nuclearController");
		this.maxStored = (int) 1e6;
	}
	
	/**
	 * Sets direction of placed nuclear controller.
	 * 
	 * @param dir = direction.
	 * @param size = expected size of chamber.
	 */
	public void setPlaceDir(byte dir, byte size, byte rel) {
		this.placeDir = dir;
		this.size = size;
		this.rel = rel;
		this.blocksArray = new Block[(int)(size * size * size)];
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
	 * @param type = type of source.
	 */
	public void setSource(EnumType type) {
		setSource(type, 1.0f);
	}
	
	/**
	 * Handles setting the proper source of this te.
	 * @param type = type of source.
	 * @param modifier = modifier amount to offset energy output.
	 */
	public void setSource(EnumType type, float modifier) {
		this.source = new Source(type, modifier);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#generatePower()
	 */
	public void generatePower() {
		if (canProducePower() && this.stored + this.source.getEffectiveSize() <= this.maxStored) this.stored += this.source.getEffectiveSize();
		if (this.stored > this.maxStored) this.stored = this.maxStored; // Redundancy check.
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#canProducePower()
	 */
	@Override
	public boolean canProducePower() {
		boolean flag = false;
		if (blocksArray == null || blocksArray.length == 0 || worldObj.getTotalWorldTime() % 20L != 0 || worldObj.isRemote) return false;
		
		else {
			// TODO: Correct offsets.
			
			int xp = this.xCoord + (placeDir == 1 || placeDir == 3 ? rel : -1);
			int yp = this.yCoord + ((size + 1) / 2);
			int zp = this.zCoord + (placeDir == 0 || placeDir == 2 ? rel : -1);
			int counter = 0;

			System.out.println("(" + (xp + size) + ", " + (yp - size) + ", " + (zp + size) + ")");
			
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					for (int z = 0; z < size; z++) {
						// this.blocksArray[x + size * (y * size * z)] = worldObj.getBlock(xp + x, yp + y, zp + z);
						this.blocksArray[counter++] = worldObj.getBlock(xp + x, yp - y, zp + z);
					}
				}
			}
			
			int[] types = new int[6];
			for (Block b : this.blocksArray) {
				if (b instanceof BlockNuclearChamberLock) types[0]++;
				else if (b instanceof BlockNuclearChamberWall) types[1]++;
				else if (b instanceof BlockNuclearReactantCore) types[2]++;
				else if (b instanceof BlockNuclearController) types[3]++;
				else if (b instanceof BlockThickenedGlass) types[4]++;
				else types[5]++;
			}

			/*System.out.println(types[0] + " locks");
			System.out.println(types[1] + " walls");
			System.out.println(types[2] + " cores");
			System.out.println(types[3] + " controllers");
			System.out.println(types[4] + " thickened glass");
			System.out.println(types[5] + " other");*/
			
		}
		
		return flag;
	}

	// TODO: This needs to be changed to uranium / w/e this requires for fuel.
	/**
	 * Function used to get the item burn time from given itemstack.
	 * @param stack = stack to (try) to burn.
	 * @return length or burn time of stack if burnable, else returns false.
	 */
	protected static int getItemBurnTime(ItemStack stack) {
		if (stack == null) return 0;
		else {
			Item item = stack.getItem();

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
				Block block = Block.getBlockFromItem(item);

				if (block == Blocks.wooden_slab) { return 150; }

				if (block.getMaterial() == Material.wood) { return 300; }

				if (block == Blocks.coal_block) { return 16000; }
			}

			if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) return 200;
			if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) return 200;
			if (item instanceof ItemHoe && ((ItemHoe) item).getToolMaterialName().equals("WOOD")) return 200;
			if (item == Items.stick) return 100;
			if (item == Items.coal) return 1600;
			if (item == Items.lava_bucket) return 20000;
			if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
			if (item == Items.blaze_rod) return 2400;
			return GameRegistry.getFuelValue(stack);
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
			else this.slots[0].stackSize--;
			if (this.slots[0].stackSize <= 0) this.slots[0] = (ItemStack) null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#updateEntity()
	 */
	public void updateEntity() {
		if (this.worldObj != null && !this.worldObj.isRemote) {
			if (this.slots[0] != null && isFuel() && canProducePower()) {
				if (this.burnTime == 0) {
					this.burnTime = getItemBurnTime(this.slots[0]);
					consumeFuel();
				}
			}

			this.powerMode = this.burnTime > 0;
			if (this.burnTime > 0) this.burnTime--;

			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityGenerator(this));
		}
		super.updateEntity();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
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
		
		this.blocksArray = size > 0 ? new Block[(int)(size * size * size)] : null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);
		comp.setInteger("ProjectZedBurnTime", this.burnTime);
		comp.setByte("ProjectZedNuclearDir", this.placeDir);
		comp.setByte("ProjectZedNuclearRel", this.rel);
		comp.setByte("ProjectZedNuclearSize", this.size);
	}
	
}

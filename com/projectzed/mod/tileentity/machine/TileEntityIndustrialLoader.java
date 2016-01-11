/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.machine;

import com.hockeyhurd.api.math.Rect;
import com.hockeyhurd.api.math.Vector2;
import com.hockeyhurd.api.math.Vector3;
import com.hockeyhurd.api.util.ChunkHelper;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.api.util.IChunkLoadable;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

/**
 * Class containing tileentity code for industrialLoader.
 * 
 * @author hockeyhurd
 * @version Apr 19, 2015
 */
public class TileEntityIndustrialLoader extends AbstractTileEntityGeneric implements IChunkLoadable {

	private Ticket heldChunk;
	
	public static final byte MIN_RADII = 1;
	public static final byte MAX_RADII = 3; // temp lowered from 6 to 3 until decided how to overcome forge chunk loading limitations.
	private byte radii = 1;
	private byte lastRadii = 1;

	private boolean markedForReinitChunksLoaded = false;
	private Chunk[] chunksLoaded;
	private Rect<Integer> chunkBoundary;
	
	public TileEntityIndustrialLoader() {
		super();
		setCustomName("industrialLoader");

		chunkBoundary = new Rect<Integer>(0, 0, 0, 0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	@Override
	public int getSizeInventory() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 */
	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initContentsArray()
	 */
	@Override
	protected void initContentsArray() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#initSlotsArray()
	 */
	@Override
	protected void initSlotsArray() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#setCustomName(java.lang.String)
	 */
	@Override
	public void setCustomName(String name) {
		this.customName = name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#isItemValidForSlot(int, net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#getAccessibleSlotsFromSide(int)
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[0];
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canInsertItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#canExtractItem(int, net.minecraft.item.ItemStack, int)
	 */
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		this.radii = comp.getByte("ChunkRadii");
		this.lastRadii = this.radii;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.AbstractTileEntityGeneric#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
		comp.setByte("ChunkRadii", this.radii);
		// unloadChunk();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getDescriptionPacket()
	 */
	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityLoader(this));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#onDataPacket(net.minecraft.network.NetworkManager, net.minecraft.network.play.server.S35PacketUpdateTileEntity)
	 */
	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
		PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityLoader(this));
	}

	/**
	 * Gets the radii of the chunks loaded.
	 *
	 * @return Byte radii.
	 */
	public byte getRadii() {
		return radii;
	}

	/**
	 * Sets the radii of the chunk loader.
	 *
	 * @param radii Byte radii to set.
	 */
	public void setRadii(byte radii) {
		this.radii = radii;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0) {

			if (chunksLoaded != null && markedForReinitChunksLoaded) reinitChunksLoaded();

			PacketHandler.INSTANCE.sendToAll(new MessageTileEntityLoader(this, getRadii()));

			if (this.lastRadii != this.radii) {
				loadChunk(null);
				this.lastRadii = this.radii;
			}
		}

	}
	
	// DO CHUNK LOADING HERE:
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#invalidate()
	 */
	@Override
	public void invalidate() {
		// forceChunkLoading(null);
		unloadChunk();
		super.invalidate();
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#validate()
	 */
	@Override
	public void validate() {
		// forceChunkLoading(null);
		loadChunk(null);
		super.validate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.util.IChunkLoadable#loadChunk(net.minecraftforge.common.ForgeChunkManager.Ticket)
	 */
	@Override
	public void loadChunk(Ticket ticket) {

		if (ticket != null) {
			if (this.heldChunk != null) unloadChunk();

			this.heldChunk = ticket;

			Vector2<Integer> vec = new Vector2<Integer>(this.xCoord >> 4, this.zCoord >> 4);

			chunkBoundary.min.x = -this.radii + 1;
			chunkBoundary.min.y = -this.radii + 1;

			chunkBoundary.max.x = (int) this.radii;
			chunkBoundary.max.y = (int) this.radii;

			Vector2<Integer> current = new Vector2<Integer>();
			for (int x = -this.radii + 1; x < this.radii; x++) {
				for (int y = -this.radii + 1; y < this.radii; y++) {
					current.x = vec.x + x;
					current.y = vec.y + y;

					ForgeChunkManager.forceChunk(this.heldChunk, new ChunkCoordIntPair(vec.x + x, vec.y + y));
				}
			}
			
		}
		
		else {
			if (this.heldChunk != null) unloadChunk();

			Ticket newTicket = ForgeChunkManager.requestTicket(ProjectZed.instance, this.worldObj, Type.NORMAL);
			newTicket.getModData().setInteger("xCoord", this.xCoord);
			newTicket.getModData().setInteger("yCoord", this.yCoord);
			newTicket.getModData().setInteger("zCoord", this.zCoord);
			
			this.heldChunk = newTicket;
			
			Vector2<Integer> vec = new Vector2<Integer>(this.xCoord >> 4, this.zCoord >> 4);

			chunkBoundary.min.x = -this.radii + 1;
			chunkBoundary.min.y = -this.radii + 1;

			chunkBoundary.max.x = (int) this.radii;
			chunkBoundary.max.y = (int) this.radii;

			Vector2<Integer> current = new Vector2<Integer>();
			for (int x = -this.radii + 1; x < this.radii; x++) {
				for (int y = -this.radii + 1; y < this.radii; y++) {
					current.x = vec.x + x;
					current.y = vec.y + y;

					ForgeChunkManager.forceChunk(this.heldChunk, new ChunkCoordIntPair(current.x, current.y));
				}
			}
			
		}
	}

	private int getCalculatedArrayLength() {
		return (radii + (radii - 1)) * (radii + (radii - 1));
	}

	private void reinitChunksLoaded() {
		if (!markedForReinitChunksLoaded || chunkBoundary == null) return;

		final int calculatedSize = getCalculatedArrayLength();
		if (chunksLoaded == null || chunksLoaded.length != calculatedSize) chunksLoaded = new Chunk[calculatedSize];

		Vector2<Integer> vec = new Vector2<Integer>(this.xCoord >> 4, this.zCoord >> 4);
		Vector2<Integer> current = new Vector2<Integer>();
		int counter = 0;

		for (int x = -radii + 1; x < radii; x++) {
			for (int z = -radii + 1; z < radii; z++) {
				current.x = vec.x + x;
				current.y = vec.y + z;

				chunksLoaded[counter++] = ChunkHelper.getChunkFromChunkCoordinates(worldObj, current);
			}
		}

		markedForReinitChunksLoaded = false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.util.IChunkLoadable#unloadChunk()
	 */
	@Override
	public void unloadChunk() {
		if (this.heldChunk != null) {
			ForgeChunkManager.releaseTicket(this.heldChunk);
			this.heldChunk = null;

			chunksLoaded = null;
		}
	}

	@Override
	public Chunk[] getChunksLoaded() {
		if (chunksLoaded == null || chunksLoaded.length != getCalculatedArrayLength()) {
			markedForReinitChunksLoaded = true;
			reinitChunksLoaded();
		}

		return chunksLoaded;
	}

	@Override
	public Vector3<Integer> worldVec() {
		return new Vector3<Integer>(xCoord, yCoord, zCoord);
	}

	@Override
	public Rect<Integer> loadedChunkBoundary() {
		return chunkBoundary;
	}

	@Override
	public World getWorld() {
		return worldObj;
	}

}

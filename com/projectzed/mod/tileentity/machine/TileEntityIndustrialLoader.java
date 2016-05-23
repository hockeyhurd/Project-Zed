/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.machine;

import com.hockeyhurd.hcorelib.api.math.Rect;
import com.hockeyhurd.hcorelib.api.math.Vector2;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.ChunkHelper;
import com.projectzed.api.tileentity.AbstractTileEntityGeneric;
import com.projectzed.api.util.IChunkLoadable;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.PacketHandler;
import com.projectzed.mod.handler.message.MessageTileEntityLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
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
	public void setCustomName(String name) {
		this.customName = name;
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
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		this.radii = comp.getByte("ChunkRadii");
		this.lastRadii = this.radii;
	}
	
	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
		comp.setByte("ChunkRadii", this.radii);
		// unloadChunk();
	}
	
	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.INSTANCE.getPacketFrom(new MessageTileEntityLoader(this));
	}
	
	@Override
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
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
	public void update() {
		super.update();

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
	
	@Override
	public void invalidate() {
		// forceChunkLoading(null);
		unloadChunk();
		super.invalidate();
	}

	@Override
	public void validate() {
		// forceChunkLoading(null);
		loadChunk(null);
		super.validate();
	}
	
	@Override
	public void loadChunk(Ticket ticket) {

		if (ticket != null) {
			if (this.heldChunk != null) unloadChunk();

			this.heldChunk = ticket;

			Vector2<Integer> vec = new Vector2<Integer>(pos.getX() >> 4, pos.getZ() >> 4);

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
			newTicket.getModData().setInteger("xCoord", pos.getX());
			newTicket.getModData().setInteger("yCoord", pos.getY());
			newTicket.getModData().setInteger("zCoord", pos.getZ());
			
			this.heldChunk = newTicket;
			
			Vector2<Integer> vec = new Vector2<Integer>(pos.getX() >> 4, pos.getZ() >> 4);

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

		Vector2<Integer> vec = new Vector2<Integer>(pos.getX() >> 4, pos.getZ() >> 4);
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
		return VectorHelper.toVector3i(pos);
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

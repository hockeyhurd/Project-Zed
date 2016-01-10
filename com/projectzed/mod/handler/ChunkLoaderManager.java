/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.handler;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.util.IChunkLoadable;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.message.MessageChunkLoaderManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for managing Chunk loaders in the world globally.
 *
 * @author hockeyhurd
 * @version 1/2/2016.
 */
public final class ChunkLoaderManager {

	private static final ChunkLoaderManager manager = new ChunkLoaderManager();

	private final Map<World, List<IChunkLoadable>> loaders;

	private ChunkLoaderManager() {
		loaders = new HashMap<World, List<IChunkLoadable>>();
	}

	/**
	 * Gets the ChunkLoaderManager instance.
	 *
	 * @return ChunkLoaderManager instance.
	 */
	public static ChunkLoaderManager instance() {
		return manager;
	}

	public Map<World, List<IChunkLoadable>> getLoaders() {
		return loaders;
	}

	public boolean flushWorldData(World world) {
		if (loaders.containsKey(world)) {
			loaders.remove(world);
			return true;
		}

		return false;
	}

	public boolean put(World world, List<IChunkLoadable> loadables) {
		if (loadables == null || loadables.isEmpty()) return false;
		if (loaders.containsKey(loadables.get(0).getWorld())) return false;

		loaders.put(world, loadables);

		return true;
	}

	/**
	 * Checks if internal mapping contains the referenced IChunkLoadable.
	 *
	 * @param loadable IChunkLoadable to reference.
	 * @return Boolean result.
	 */
	public boolean containsIChunkLoadable(IChunkLoadable loadable) {
		if (loadable == null) return false;
		if (loaders.isEmpty()) return false;
		if (!loaders.containsKey(loadable.getWorld())) return false;

		final List<IChunkLoadable> list = loaders.get(loadable.getWorld());
		if (list.isEmpty()) return false;

		final Vector3<Integer> loadVec = loadable.worldVec();

		for (IChunkLoadable chunkLoadable : list) {
			if (chunkLoadable.worldVec().equals(loadVec)) return true;
		}

		return false;
	}

	/**
	 * Adds IChunkLoadable to internal mapping.
	 *
	 * @param loadable IChunkLoadable to reference.
	 * @return Boolean result of action.
	 */
	public boolean addIChunkLoadable(IChunkLoadable loadable) {
		if (loadable == null) {
			ProjectZed.logHelper.warn("A NULL IChunkLoadable was attempted to be added to global mapping!");
			return false;
		}

		final World world = loadable.getWorld();

		if (loaders.containsKey(world) && !containsIChunkLoadable(loadable)) {
			loaders.get(world).add(loadable);
		}

		else {
			final List<IChunkLoadable> list = new ArrayList<IChunkLoadable>();
			list.add(loadable);

			loaders.put(world, list);
		}

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) PacketHandler.INSTANCE.sendToAll(new MessageChunkLoaderManager(this));

		return true;
	}

	/**
	 * Removes IChunkLoadable from internal mapping.
	 *
	 * @param loadable IChunkLoadable to reference.
	 * @return Boolean result of action.
	 */
	public boolean removeIChunkLoadable(IChunkLoadable loadable) {
		if (loadable == null) {
			ProjectZed.logHelper.warn("A NULL IChunkLoadable was attempted to be removed to global mapping!");
			return false;
		}

		final World world = loadable.getWorld();

		if (!loaders.containsKey(world)) {
			ProjectZed.logHelper.warn("Mapping does not contain entry for World:", world);
			return false;
		}

		final Vector3<Integer> loadableVec = loadable.worldVec();
		final List<IChunkLoadable> list = loaders.get(world);

		if (list.isEmpty()) {
			ProjectZed.logHelper.warn("Error trying to remove IChunkLoadable @:", loadableVec, "reason: internal list is empty!");
			return false;
		}

		boolean wasRemoved = false;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).worldVec().equals(loadableVec)) {
				list.remove(i);
				wasRemoved = true;
				break;
			}
		}

		if (wasRemoved) {

			// Ensure we don't have unused empty list in the mapping!
			if (list.isEmpty()) loaders.remove(world);

			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) PacketHandler.INSTANCE.sendToAll(new MessageChunkLoaderManager(this));

			return true;
		}

		ProjectZed.logHelper.warn("Mapping could not find IChunkLoadable @:", loadableVec);
		return false;
	}

	/**
	 * Gets an array of IChunkLoadable's in range of player view distance.
	 *
	 * @param player EntityPlayer to check.
	 * @return Array of IChunkLoadable's. <bold>NOTE: </bold> Could return empty array! i.e. length '0'.
	 */
	public IChunkLoadable[] getIChunkLoadablesInPlayerRange(EntityPlayer player) {
		final World world = player.getEntityWorld();

		// If we don't have any entries of IChunkLoadable's in the player world, then return empty array!
		if (!loaders.containsKey(world)) return new IChunkLoadable[0];

		final List<IChunkLoadable> referenceList = loaders.get(world);
		final List<IChunkLoadable> list = new ArrayList<IChunkLoadable>(referenceList.size());
		// final List<IChunkLoadable> removeList = new ArrayList<IChunkLoadable>(referenceList.size());

		Vector3<Integer> vec;
		for (IChunkLoadable loader : referenceList) {
			vec = loader.worldVec();

			/*if (!(world.getTileEntity(vec.x, vec.y, vec.z) instanceof IChunkLoadable)) {
				removeList.add(loader);
				continue;
			}*/

			if (player.isInRangeToRender3d(vec.x, vec.y, vec.z)) list.add(loader);
		}

		// Remove, the designated removed IChunkLoadable's
		/*if (!removeList.isEmpty()) {
			for (IChunkLoadable toRemove : removeList) {
				vec = toRemove.worldVec();

				for (int i = 0; i < referenceList.size(); i++) {
					if (referenceList.get(i).worldVec().equals(vec)) {
						referenceList.remove(i);
						break;
					}
				}
			}
		}*/

		return !list.isEmpty() ? list.toArray(new IChunkLoadable[list.size()]) : new IChunkLoadable[0];
	}

	/**
	 * Gets an array of IChunkLoadable's in range of a position with max distance.
	 *
	 * @param world World to reference.
	 * @param position Vector3 position.
	 * @param maxDistance Max distance to/from the provided Vector3 position.
	 * @return Array of IChunkLoadable's. <bold>NOTE: </bold> Could return empty array! i.e. length '0'.
	 */
	public IChunkLoadable[] getIChunkLoadablesRange(World world, Vector3<Integer> position, double maxDistance) {
		if (!loaders.containsKey(world)) return new IChunkLoadable[0];

		final List<IChunkLoadable> referenceList = loaders.get(world);
		final List<IChunkLoadable> list = new ArrayList<IChunkLoadable>(referenceList.size());

		Vector3<Integer> vec;
		for (IChunkLoadable loader : referenceList) {
			vec = loader.worldVec();

			if (vec.getNetDifference(position) <= maxDistance) list.add(loader);
		}

		return !list.isEmpty() ? list.toArray(new IChunkLoadable[list.size()]) : new IChunkLoadable[0];
	}

}

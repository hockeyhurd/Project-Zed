/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.handler;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.HashMap;
import java.util.List;

/**
 * The chunkloader event handler.
 * 
 * @author hockeyhurd
 * @version Apr 21, 2015
 */
public class WorldChunkHandler implements LoadingCallback {

	private static final WorldChunkHandler INSTANCE = new WorldChunkHandler();
	private static HashMap<Object, ModContainer> mods = new HashMap<Object, ModContainer>();
	
	private WorldChunkHandler() {
	}

	/**
	 * Registers mod.
	 *
	 * @param mod Mod object.
	 */
	public void registerMod(Object mod) {
		ModContainer container = Loader.instance().getModObjectList().inverse().get(mod);
		if (container == null) throw new NullPointerException("Mod container not found for: " + mod);
		mods.put(mod, container);
		ForgeChunkManager.setForcedChunkLoadingCallback(mod, instance());
	}

	/**
	 * Instance of handler.
	 *
	 * @return Handler instance.
	 */
	public static WorldChunkHandler instance() {
		return INSTANCE;
	}

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		for (Ticket ticket : tickets) {
			Vector3<Integer> vec = new Vector3<Integer>();
			vec.x = ticket.getModData().getInteger("xCoord");
			vec.y = ticket.getModData().getInteger("yCoord");
			vec.z = ticket.getModData().getInteger("zCoord");

			final TileEntity tileEntity = world.getTileEntity(VectorHelper.toBlockPos(vec));
			if (tileEntity != null && tileEntity instanceof TileEntityIndustrialLoader) {
				// ((TileEntityIndustrialLoader) world.getTileEntity(vec.x, vec.y, vec.z)).forceChunkLoading(ticket);
				((TileEntityIndustrialLoader) tileEntity).loadChunk(ticket);
			}
		}
	}

}

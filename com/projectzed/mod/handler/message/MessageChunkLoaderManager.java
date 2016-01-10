/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.handler.message;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.util.IChunkLoadable;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.handler.ChunkLoaderManager;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Messaging class for ChunkLoaderManager.
 *
 * @author hockeyhurd
 * @version 1/10/2016.
 */
public class MessageChunkLoaderManager implements IMessage, IMessageHandler<MessageChunkLoaderManager, IMessage> {

	private static final Minecraft minecraft = Minecraft.getMinecraft();
	private ChunkLoaderManager manager;
	private Map<Integer, List<Vector3<Integer>>> loaderLocations;
	private int numDimensions;

	@Deprecated
	public MessageChunkLoaderManager() {
	}

	public MessageChunkLoaderManager(ChunkLoaderManager manager) {
		this.manager = manager;

		loaderLocations = new HashMap<Integer, List<Vector3<Integer>>>(manager.getLoaders().size());
		numDimensions = manager.getLoaders().size();

		for (World world : manager.getLoaders().keySet()) {
			final int id = world.provider.dimensionId;

			final List<IChunkLoadable> referenceList = manager.getLoaders().get(world);
			final List<Vector3<Integer>> list = new ArrayList<Vector3<Integer>>(referenceList.size());

			for (IChunkLoadable loadable : referenceList) {
				list.add(loadable.worldVec());
			}

			loaderLocations.put(id, list);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		numDimensions = buf.readInt();

		if (loaderLocations == null) loaderLocations = new HashMap<Integer, List<Vector3<Integer>>>(numDimensions);

		for (int i = 0; i < numDimensions; i++) {
			final int currentDim = buf.readInt();
			final int numVecs = buf.readInt();

			List<Vector3<Integer>> list = new ArrayList<Vector3<Integer>>(numVecs);

			for (int v = 0; v < numVecs; v++) {
				Vector3<Integer> vec = new Vector3<Integer>();

				vec.x = buf.readInt();
				vec.y = buf.readInt();
				vec.z = buf.readInt();

				list.add(vec);
			}

			loaderLocations.put(currentDim, list);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(numDimensions);

		for (int dimID : loaderLocations.keySet()) {
			buf.writeInt(dimID);

			List<Vector3<Integer>> referenceList = loaderLocations.get(dimID);

			buf.writeInt(referenceList.size());

			for (Vector3<Integer> vec : referenceList) {
				buf.writeInt(vec.x);
				buf.writeInt(vec.y);
				buf.writeInt(vec.z);
			}
		}
	}

	@Override
	public IMessage onMessage(MessageChunkLoaderManager message, MessageContext ctx) {

		// Ensure we are only receiving on the client side of things.
		if (ctx.side == Side.CLIENT) {
			ProjectZed.logHelper.info("Running!");
			final ChunkLoaderManager clientManager = ChunkLoaderManager.instance();

			for (int dimID : message.loaderLocations.keySet()) {
				final World world = DimensionManager.getWorld(dimID);

				if (world == null) {
					ProjectZed.logHelper.severe("Error! Could not find world with ID:", dimID);
					continue;
				}

				final List<Vector3<Integer>> referenceList = message.loaderLocations.get(dimID);
				final List<IChunkLoadable> list = new ArrayList<IChunkLoadable>(referenceList.size());

				for (Vector3<Integer> vec : referenceList) {
					final TileEntity te = world.getTileEntity(vec.x, vec.y, vec.z);

					if (te instanceof IChunkLoadable) list.add((IChunkLoadable) te);
					else ProjectZed.logHelper.severe("Error! TileEntity @:", vec, "isn't of type: 'IChunkLoadable!'");
				}

				manager.put(world, list);
			}

		}

		return null;
	}

}

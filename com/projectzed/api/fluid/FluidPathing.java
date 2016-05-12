/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.api.fluid;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.pathfinding.AStarAlogirthm;
import com.hockeyhurd.hcorelib.api.math.pathfinding.IPathTile;
import com.hockeyhurd.hcorelib.api.math.pathfinding.PathNode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Adjusted A* algorithm for fluid tiles specifically.
 *
 * @see com.hockeyhurd.api.math.pathfinding.AStarAlogirthm
 *
 * @author hockeyhurd
 * @version 4/14/2016.
 */
public final class FluidPathing extends AStarAlogirthm {

	/**
	 * <bold>WARNING: </bold>Null end point tiles!.
	 */
	public FluidPathing() {
		this(null, null);
	}

	/**
	 * @param startTile IPathTile start.
	 * @param endTile IPathTile end.
	 */
	public FluidPathing(IPathTile startTile, IPathTile endTile) {
		super(startTile, endTile);
	}

	/**
	 * Sets start tile.
	 *
	 * @param startTile IPathTile.
	 */
	public void setStartTile(IPathTile startTile) {
		this.startTile = startTile;
	}

	/**
	 * Sets end tile.
	 *
	 * @param endTile IPathTile.
	 */
	public void setEndTile(IPathTile endTile) {
		this.endTile = endTile;
	}

	/*public void setDiagonalUsage(boolean useDiagonals) {
		this.useDiagonals = useDiagonals;
	}*/

	@Override
	public List<IPathTile> findPath(World world) {
		List<PathNode> openList = new LinkedList<PathNode>();
		List<PathNode> closedList = new LinkedList<PathNode>();
		PathNode current = new PathNode(startTile, null, startTile.getCost(), 0.0d);
		IPathTile[] adjacents;
		IPathTile at;
		// Node at;
		PathNode node;
		Vector3<Integer> atVec;
		final Vector3<Integer> startVec = startTile.worldVec();
		final Vector3<Integer> endVec = endTile.worldVec();
		double gCost;
		double hCost;
		// double hCost = current.distanceTo(endVec);
		// double fCost;

		openList.add(current);

		while (!openList.isEmpty()) {
			Collections.sort(openList, tileSorter);
			current = openList.get(0);

			if (current.vec.equals(endVec)) {
				lastPath = new LinkedList<IPathTile>();

				while (current.parent != null) {
					lastPath.add(current.tile);
					current = current.parent;
				}

				// openList.clear();
				// closedList.clear();
				return lastPath;
			}

			openList.remove(current);
			closedList.add(current);

			adjacents = getAdjacentTiles(world, current.tile);

			for (int i = 0; i < adjacents.length; i++) {
				at = adjacents[i];
				if (at.isSolid()) continue;
				atVec = at.worldVec();
				gCost = current.gCost + current.tile.distanceTo(atVec);
				hCost = atVec.getNetDifference(endVec);
				node = new PathNode(at, current, gCost, hCost);
				if (contains(closedList, atVec) && gCost >= node.gCost) continue;
				if (!contains(openList, atVec) || gCost < node.gCost) openList.add(node);
			}
		}

		// closedList.clear();

		return null;
	}

	/**
	 * Gets the adjacent tiles.
	 *
	 * @param world World to reference.
	 * @param origin IPathTile origin.
	 * @return IPathTile[] adjacent tiles.
	 */
	private static IFluidTile[] getAdjacentTiles(World world, IPathTile origin) {
		if (world == null || origin == null) return new IFluidTile[0];

		// IPathTile[] tiles = new IPathTile[6];
		List<IFluidTile> tiles = new ArrayList<IFluidTile>(6);

		Vector3<Integer> currentVec = origin.getOffsetVec(-1, 0, 0);
		TileEntity te = world.getTileEntity(currentVec.x, currentVec.y, currentVec.z);
		if (te instanceof IFluidTile) tiles.add((IFluidTile) te);

		currentVec = origin.getOffsetVec(1, 0, 0);
		te = world.getTileEntity(currentVec.x, currentVec.y, currentVec.z);
		if (te instanceof IFluidTile) tiles.add((IFluidTile) te);

		currentVec = origin.getOffsetVec(0, 0, -1);
		te = world.getTileEntity(currentVec.x, currentVec.y, currentVec.z);
		if (te instanceof IFluidTile) tiles.add((IFluidTile) te);

		currentVec = origin.getOffsetVec(0, 0, 1);
		te = world.getTileEntity(currentVec.x, currentVec.y, currentVec.z);
		if (te instanceof IFluidTile) tiles.add((IFluidTile) te);

		currentVec = origin.getOffsetVec(0, -1, 0);
		te = world.getTileEntity(currentVec.x, currentVec.y, currentVec.z);
		if (te instanceof IFluidTile) tiles.add((IFluidTile) te);

		currentVec = origin.getOffsetVec(0, 1, 0);
		te = world.getTileEntity(currentVec.x, currentVec.y, currentVec.z);
		if (te instanceof IFluidTile) tiles.add((IFluidTile) te);

		return tiles.toArray(new IFluidTile[tiles.size()]);
	}

}

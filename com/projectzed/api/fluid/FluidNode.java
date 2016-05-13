/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.fluid;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.mod.util.WorldUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing code for a FluidNode, used in and for tracking 
 * fluid containers in FluidNetwork.
 * @see {@link com.projectzed.api.fluid.FluidNetwork FluidNetwork}
 * 
 * @author hockeyhurd
 * @version May 7, 2015
 */
public class FluidNode {

	private IFluidHandler container;
	private EnumFacing[] connections;
	private ValveType valveType;

	@SuppressWarnings("unchecked")
	private Vector3<Integer> vec = Vector3.zero.getVector3i();

	private FluidNetwork network = null;

	protected float cost;

	/**
	 * @param container container/tank.
	 * @param connections connections with other nodes.
	 * @param vec coordinate location.
	 */
	public FluidNode(IFluidContainer container, EnumFacing[] connections, Vector3<Integer> vec) {
		this(container, connections, vec, ValveType.NEUTRAL);
	}
	
	/**
	 * @param container container/tank.
	 * @param connections connections with other nodes.
	 * @param vec coordinate location.
	 * @param type type of valve.
	 */
	public FluidNode(IFluidHandler container, EnumFacing[] connections, Vector3<Integer> vec, ValveType type) {
		this.container = container;
		this.connections = new EnumFacing[connections.length];
		
		for (int i = 0; i < connections.length; i++) {
			this.connections[i] = connections[i];
		}
		
		this.vec = vec;
		this.valveType = type;

		this.cost = 1.0f;
	}
	
	/**
	 * Sets network associated with this fluid node.
	 * 
	 * @param network network to set with.
	 */
	public void setFluidNetwork(FluidNetwork network) {
		this.network = network;
	}
	
	/**
	 * @return fluid network if has one, else can return null.
	 */
	public FluidNetwork getFluidNetwork() {
		return network;
	}
	
	/**
	 * @return flag to check if this node has a network.
	 */
	public boolean hasFluidNetwork() {
		return getFluidNetwork() != null;
	}
	
	/**
	 * Checks for continued connections with other nodes.
	 * 
	 * @return true if has any connections, else returns false.
	 */
	public boolean hasConnections() {
		if (connections == null || connections.length == 0) return false;
		
		for (int i = 0; i < connections.length; i++) {
			if (connections[i] != null || connections[i] != null) return true;
		}
		
		return false;
	}
	
	/**
	 * Gets EnumFacing array of connections to other nodes.
	 * @return
	 */
	public EnumFacing[] getConnections() {
		return connections;
	}
	
	/**
	 * Sets connection on given EnumFacing.
	 * 
	 * @param dir direction to set.
	 */
	public void setConnection(EnumFacing dir) {
		if (dir != null) this.connections[dir.ordinal()] = dir;
	}
	
	/**
	 * Checks if this container/tank has said fluid.
	 * 
	 * @param fluid fluid to check for.
	 * @return true if contains said fluid, else can return false.
	 */
	public boolean containsFluid(Fluid fluid) {
		if (container != null) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (container.canDrain(dir, fluid)) return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Generalized check to see if container/tank has any fluid stored.
	 * 
	 * @return true if contains any fluid, else returns false.
	 */
	public boolean containsFluid() {
		if (container != null) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (container.getTankInfo(dir) != null) {
					for (FluidTankInfo info : container.getTankInfo(dir)) {
						if (info.fluid != null && info.fluid.amount > 0) return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Static method to check if container/tank contains any fluid.
	 * 
	 * @param container cotnainer/tank to check.
	 * @return true if has any fluid, else returns false.
	 */
	public static boolean containsFluid(IFluidHandler container) {
		if (container != null) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (container.getTankInfo(dir) != null) {
					for (FluidTankInfo info : container.getTankInfo(dir)) {
						if (info.fluid != null && info.fluid.amount > 0) return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Gets amout of fluid stored of said fluid.
	 * 
	 * @param fluid fluid to check.
	 * @return amount stored if has said fluid, else returns '0'.
	 */
	public int getAmountStored(Fluid fluid) {
		if (container != null && containsFluid(fluid)) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (container.canDrain(dir, fluid)) {
					for (FluidTankInfo info : container.getTankInfo(dir)) {
						if (info.fluid != null && info.fluid.getFluid().equals(fluid)) return info.fluid.amount;
					}
				}
			}
		}
		
		return 0;
	}
	
	/**
	 * Gets the capacity of stored fluid.
	 * 
	 * @param fluid fluid to reference.
	 * @return capacity if found, else returns '0'.
	 */
	public int getCapacity(Fluid fluid) {
		if (container != null) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (container.canDrain(dir, fluid)) {
					for (FluidTankInfo info : container.getTankInfo(dir)) {
						if (info.fluid != null && info.fluid.getFluid().equals(fluid)) return info.capacity;
					}
				}
			}
		}
		
		return 0;
	}
	
	/**
	 * Internal check to see if fluid node is of source node type.
	 * 
	 * @return true if is source node, else returns false.
	 */
	public boolean isSourceNode() {
		return getIFluidContainer() != null && getIFluidContainer().canBeSourceNode() && containsFluid();
	}
	
	/**
	 * Gets reference of IFluidHandler.
	 * 
	 * @return IFluidHandler instance.
	 */
	public IFluidHandler getFluidContainer() {
		return container;
	}
	
	/**
	 * Helper getter to get if is IFluidContainer type.
	 * 
	 * @return IFluidContainer instance if is instanceof, else returns null.
	 */
	public IFluidContainer getIFluidContainer() {
		return container != null && container instanceof IFluidContainer ? (IFluidContainer) getFluidContainer() : null;
	}
	
	/**
	 * Gets whether fluid node is a pipe or not.
	 * 
	 * @return true if is a pipe, else returns false.
	 */
	public boolean isPipe() {
		return getIFluidContainer() != null && getIFluidContainer().isPipe();
	}
	
	/**
	 * Setter method for setting valve type.
	 * 
	 * @param type type to set.
	 */
	public void setValveType(ValveType type) {
		this.valveType = type;
	}
	
	/**
	 * Gets the valve type at this node.
	 * 
	 * @return valve type.
	 */
	public ValveType getValveType() {
		return this.valveType;
	}
	
	/**
	 * Setter method to update coordinates of this fluid node.
	 * 
	 * @param vec vector to set.
	 */
	public void setWorldVec(Vector3<Integer> vec) {
		this.vec.x = vec.x;
		this.vec.y = vec.y;
		this.vec.z = vec.z;
	}

	/**
	 * Gets world vector coordinates of this fluid node.
	 *
	 * @return world vector coordinates.
	 */
	public Vector3<Integer> worldVec() {
		return vec;
	}

	/**
	 * Helper function to determine and get valve type for said container on EnumFacing side.
	 * 
	 * @param container container to reference.
	 * @param dir directional side to check.
	 * @return valve type.
	 */
	public static ValveType appropriateValveType(IFluidHandler container, EnumFacing dir) {
		if (container instanceof IFluidContainer && ((IFluidContainer) container).isPipe()) return ValveType.NEUTRAL;
		
		boolean in = false;
		boolean out = false;
		
		// first pass, check if has anything.
		// for (FluidTankInfo info : container.getTankInfo(dir)) {
		for (FluidTankInfo info : container.getTankInfo(dir.getOpposite())) {
			if (info != null && info.fluid != null && info.fluid.amount > 0) {
				// out = container.canDrain(dir, info.fluid.getFluid());
				out = container.canDrain(dir.getOpposite(), info.fluid.getFluid());
				break;
			}
		}
		
		// second pass see if full.
		// for (FluidTankInfo info : container.getTankInfo(dir)) {
		for (FluidTankInfo info : container.getTankInfo(dir.getOpposite())) {
			if (info == null) continue;
			
			if (info.fluid != null && info.fluid.amount < info.capacity) {
				// in = container.canFill(dir, info.fluid.getFluid());
				in = container.canFill(dir.getOpposite(), info.fluid.getFluid());
				break;
			}
			
			else {
				in = true;
				break;
			}
		}
		
		// ProjectZed.logHelper.info("dir:", dir, "in:", in, "out:", out);
		// ProjectZed.logHelper.info("dir:", dir.getOpposite(), "in:", in, "out:", out);
		
		return in && !out ? ValveType.INPUT : !in && out ? ValveType.OUTPUT : ValveType.NEUTRAL;
	}

	/**
	 * Gets the IFluidTile at world location.
	 *
	 * @param world World to reference.
	 * @return IFluidTile.
	 */
	public IFluidTile getTileAt(World world) {
		BlockPos pos = VectorHelper.toBlockPos(vec);
		return (IFluidTile) world.getTileEntity(pos);
	}
	
	/**
	 * Main update call for this node. Goal of this method 
	 * is to update checks on things such as fluid capacity, fullness, connections
	 * to main network, etc.
	 */
	public void update() {
		if (hasFluidNetwork()) {
			FluidNode[] surrounding = network.getSurroundingNodes(this);
			if (surrounding == null || surrounding.length == 0) {
				// ProjectZed.logHelper.severe("Error finding surrounding fluid nodes!");
				
				// make sure not only node in network!
				if (network.size() > 1) network.remove(this);
				return;
			}
			
			this.connections = new EnumFacing[surrounding.length];
			
			// clear connections.
			for (int i = 0; i < this.connections.length; i++) {
				this.connections[i] = null;
			}
			
			// apply surrounding connections to this 'connections'.
			for (int i = 0; i < surrounding.length; i++) {
				if (surrounding[i] == null) continue;
				
				this.connections[i] = WorldUtils.getDirectionRelativeTo(worldVec(), surrounding[i].worldVec());
				this.valveType = appropriateValveType(this.container, this.connections[i]);
			}
		}
	}
	
	/**
	 * @param comp NBTTagCompound to read from.
	 */
	public void readFromNBT(NBTTagCompound comp) {
		int numConnections = comp.getInteger("NumConnections");
		int val = 0;
		List<EnumFacing> list = new ArrayList<EnumFacing>(numConnections);
		
		for (int i = 0; i < numConnections; i++) {
			val = comp.getInteger("Direction " + i);
			if (val != -1) list.add(EnumFacing.getFront(val));
		}
		
		connections = list.toArray(new EnumFacing[list.size()]);
		comp.getInteger("ValveType");
	}
	
	/**
	 * @param comp NBTTagCompound to write to.
	 */
	public void writeToNBT(NBTTagCompound comp) {
		comp.setInteger("NumConnections", connections.length);
		
		for (int i = 0; i < connections.length; i++) {
			if (connections[i] != null && connections[i] != null) comp.setInteger("Direction " + i, connections[i].ordinal());
			else comp.setInteger("Directions " + i, -1);
		}
		
		comp.setInteger("ValveType", valveType.ordinal());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((container == null) ? 0 : container.hashCode());
		result = prime * result + ((vec == null) ? 0 : vec.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FluidNode other = (FluidNode) obj;
		if (container == null) {
			if (other.container != null) return false;
		}
		else if (!container.equals(other.container)) return false;
		if (vec == null) {
			if (other.vec != null) return false;
		}
		else if (!vec.equals(other.vec)) return false;
		return true;
	}

}

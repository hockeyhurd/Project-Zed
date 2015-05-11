/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.fluid;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.hockeyhurd.api.math.Vector4;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.mod.util.WorldUtils;

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
	private ForgeDirection[] connections;
	private Vector4<Integer> vec = Vector4.zero.getVector4i();
	private ValveType valveType;
	
	private FluidNetwork network = null;
	
	public FluidNode(IFluidContainer container, ForgeDirection[] connections, Vector4<Integer> vec) {
		this(container, connections, vec, ValveType.NEUTRAL);
	}
	
	public FluidNode(IFluidHandler container, ForgeDirection[] connections, Vector4<Integer> vec, ValveType type) {
		this.container = container;
		this.connections = new ForgeDirection[connections.length];
		
		for (int i = 0; i < connections.length; i++) {
			this.connections[i] = connections[i];
		}
		
		this.vec = vec;
		this.valveType = type;
	}
	
	public void setFluidNetwork(FluidNetwork network) {
		this.network = network;
	}
	
	public FluidNetwork getFluidNetwork() {
		return network;
	}
	
	public boolean hasFluidNetwork() {
		return getFluidNetwork() != null;
	}
	
	public ForgeDirection[] getConnections() {
		return connections;
	}
	
	public void setConnection(ForgeDirection dir) {
		if (dir != ForgeDirection.UNKNOWN) this.connections[dir.ordinal()] = dir;
	}
	
	public boolean containsFluid(Fluid fluid) {
		if (container != null) {
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (container.canDrain(dir, fluid)) return true;
			}
		}
		
		return false;
	}
	
	public boolean containsFluid() {
		if (container != null) {
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (container.getTankInfo(dir) != null) {
					for (FluidTankInfo info : container.getTankInfo(dir)) {
						if (info.fluid != null && info.fluid.amount > 0) return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public static boolean containsFluid(IFluidHandler container) {
		if (container != null) {
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (container.getTankInfo(dir) != null) {
					for (FluidTankInfo info : container.getTankInfo(dir)) {
						if (info.fluid != null && info.fluid.amount > 0) return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public int getAmountStored(Fluid fluid) {
		if (container != null && containsFluid(fluid)) {
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (container.canDrain(dir, fluid)) {
					for (FluidTankInfo info : container.getTankInfo(dir)) {
						if (info.fluid.getFluid().equals(fluid)) return info.fluid.amount;
					}
				}
			}
		}
		
		return 0;
	}
	
	public int getCapacity(Fluid fluid) {
		if (container != null) {
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (container.canDrain(dir, fluid)) {
					for (FluidTankInfo info : container.getTankInfo(dir)) {
						return info.capacity;
					}
				}
			}
		}
		
		return 0;
	}
	
	public boolean isSourceNode() {
		if (getIFluidContainer() != null && getIFluidContainer().isPipe()) return false;
		return this.valveType == ValveType.OUTPUT && containsFluid();
	}
	
	public IFluidHandler getFluidContainer() {
		return container;
	}
	
	public IFluidContainer getIFluidContainer() {
		return container != null && container instanceof IFluidContainer ? (IFluidContainer) getFluidContainer() : null;
	}
	
	public boolean isPipe() {
		return getIFluidContainer() != null ? getIFluidContainer().isPipe() : false;
	}
	
	public void setValveType(ValveType type) {
		this.valveType = type;
	}
	
	public ValveType getValveType() {
		return this.valveType;
	}
	
	public void setWorldVec(Vector4<Integer> vec) {
		this.vec.x = vec.x;
		this.vec.y = vec.y;
		this.vec.z = vec.z;
	}
	
	public static ValveType appropriateValveType(IFluidHandler container, ForgeDirection dir) {
		if (container instanceof IFluidContainer && ((IFluidContainer) container).isPipe()) return ValveType.NEUTRAL;
		
		boolean in = false;
		boolean out = false;
		
		// first pass, check if has anything.
		for (FluidTankInfo info : container.getTankInfo(dir)) {
			if (info != null && info.fluid != null && info.fluid.amount > 0) {
				out = true;
				break;
			}
		}
		
		// second pass see if full.
		for (FluidTankInfo info : container.getTankInfo(dir)) {
			if (info != null && info.fluid != null && info.fluid.amount < info.capacity) {
				in = true;
				break;
			}
		}
		
		return in && !out ? ValveType.INPUT : !in && out ? ValveType.OUTPUT : ValveType.NEUTRAL;
	}
	
	public Vector4<Integer> worldVec() {
		return vec;
	}
	
	/**
	 * Main update call for this node. Goal of this method 
	 * is to update checks on things such as fluid capacity, fullness, connections
	 * to main network, etc.
	 */
	public void update() {
		if (hasFluidNetwork()) {
			FluidNode[] surrounding = network.getSurroundingNodes(this);
			if (surrounding == null || surrounding.length == 0 || surrounding.length != this.connections.length) return;
			
			for (int i = 0; i < this.connections.length; i++) {
				this.connections[i] = ForgeDirection.UNKNOWN;
			}
			
			for (int i = 0; i < surrounding.length; i++) {
				if (surrounding[i] == null) continue;
				
				this.connections[i] = WorldUtils.getDirectionRelativeTo(worldVec(), surrounding[i].worldVec());
				this.valveType = appropriateValveType(this.container, this.connections[i]);
			}
		}
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

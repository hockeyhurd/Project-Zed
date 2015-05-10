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
	private Vector4<Integer> vec = Vector4.zero.getVector4i();
	private ValveType type;
	
	public FluidNode(IFluidContainer container, Vector4<Integer> vec) {
		this(container, vec, ValveType.NEUTRAL);
	}
	
	public FluidNode(IFluidHandler container, Vector4<Integer> vec, ValveType type) {
		this.container = container;
		this.vec = vec;
		this.type = type;
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
		return this.type == ValveType.OUTPUT && containsFluid();
	}
	
	public IFluidHandler getFluidContainer() {
		return container;
	}
	
	public IFluidContainer getIFluidContainer() {
		return container != null && container instanceof IFluidContainer ? (IFluidContainer) getFluidContainer() : null;
	}
	
	public void setValveType(ValveType type) {
		this.type = type;
	}
	
	public ValveType getFluidType() {
		return this.type;
	}
	
	public void setWorldVec(Vector4<Integer> vec) {
		this.vec.x = vec.x;
		this.vec.y = vec.y;
		this.vec.z = vec.z;
	}
	
	public static ValveType appropriateValveType(IFluidHandler container, ForgeDirection dir) {
		boolean in = false;
		boolean out = false;
		boolean containsFluid = containsFluid(container);
		
		if (!containsFluid) in = true;
		else out = true;
		
		return in && !out ? ValveType.INPUT : !in && out ? ValveType.OUTPUT : ValveType.NEUTRAL;
	}
	
	public Vector4<Integer> worldVec() {
		return vec;
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

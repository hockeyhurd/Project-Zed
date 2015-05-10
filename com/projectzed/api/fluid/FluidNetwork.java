/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.fluid;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.hockeyhurd.api.math.Vector4;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductBase;

/**
 * 
 * 
 * @author hockeyhurd
 * @version May 7, 2015
 */
public class FluidNetwork {

	private LinkedList<FluidNode> nodes;
	private FluidNode masterNode;
	
	public FluidNetwork(FluidNode masterNode) {
		nodes = new LinkedList<FluidNode>();
		this.masterNode = masterNode;
		nodes.addLast(masterNode);
	}
	
	public void add(FluidNode node) {
		if (nodes != null && !nodes.contains(node)) nodes.addLast(node);
	}
	
	public void remove(FluidNode node) {
		if (node == null || !nodes.contains(node)) return;
		if (masterNode != null && node.equals(masterNode)) {
			// nodes.removeFirst();
			// masterNode = node;
			masterNode = null;
			
			for (FluidNode n : nodes) {
				if (n.getFluidContainer() instanceof TileEntityLiquiductBase) {
					((TileEntityLiquiductBase) n.getFluidContainer()).voidNetwork();
					masterNode = n;
					break;
				}
			}
			 
			// nodes.addFirst(masterNode);
			flushNetwork();
		}
		
		else nodes.remove(node);
	}
	
	public int size() {
		return nodes != null ? nodes.size() : 0;
	}
	
	public List<FluidNode> getNodes() {
		return nodes;
	}
	
	public FluidNode getNodeAt(Vector4<Integer> vec) {
		if (nodes != null && size() > 0 && vec != null) {
			for (FluidNode node : nodes) {
				if (node.worldVec().equals(vec)) return node;
			}
		}
		
		return null;
	}
	
	public void merge(FluidNetwork other) {
		if (other != null && other.size() > 0) {
			for (FluidNode node : other.getNodes()) {
				add(node);
			}
		}
	}
	
	public boolean isMasterNode(FluidNode node) {
		if (node == null) return false;
		if (masterNode == null) {
			masterNode = node;
			return true;
		}
		
		return masterNode.equals(node);
	}
	
	public FluidNode getMasterNode() {
		return masterNode;
	}
	
	private void flushNetwork() {
		// nodes = new LinkedList<FluidNode>();
		nodes.clear();
		if (masterNode != null) nodes.addFirst(masterNode);
	}
	
	public void update() {
		
		if (nodes != null && nodes.size() > 0) {
			Set<FluidNode> sourceNodes = new HashSet<FluidNode>();
			
			for (FluidNode node : nodes) {
				if (node.isSourceNode()) sourceNodes.add(node);
			}

			if (sourceNodes != null && sourceNodes.size() > 0) {
				Set<FluidNode> acceptorNodes = new HashSet<FluidNode>();
				ProjectZed.logHelper.info(sourceNodes.size());
				
				// check each source
				for (FluidNode srcNode : sourceNodes) {
					
					// check each direction of node.
					for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
						
						// if valid get all tanks on side.
						if (srcNode.getFluidContainer().getTankInfo(dir) != null && srcNode.getFluidContainer().getTankInfo(dir).length > 0) {
							
							// for each tank get its info.
							for (FluidTankInfo info : srcNode.getFluidContainer().getTankInfo(dir)) {

								if (info.fluid != null && !srcNode.getFluidContainer().canDrain(dir.getOpposite(), info.fluid.getFluid())) {
									continue;
								}
								
								// check all available nodes and: 1, see if its not the same source node, and 2 check if can accept fluid. If so, we add it to acceptorNodes.
								for (FluidNode fillNode : nodes) {
									// if (!subNode.equals(node) && subNode.getFluidContainer().canFill(dir, info.fluid.getFluid())) acceptorNodes.add(subNode);
									if (fillNode != srcNode && info.fluid != null && fillNode.getFluidContainer().canFill(dir, info.fluid.getFluid())) {
										int amount = fillNode.getFluidContainer().fill(dir, info.fluid, false);
										
										FluidStack trackedStack = info.fluid.copy();
										trackedStack.amount = Math.min(trackedStack.amount, amount); 
										
										if (!srcNode.getFluidContainer().canDrain(dir.getOpposite(), info.fluid.getFluid())) continue;
										
										fillNode.getFluidContainer().fill(dir, trackedStack, true);
										srcNode.getFluidContainer().drain(dir.getOpposite(), trackedStack, true);
									}
								}
							}
						}
					}
				}
				
				
			}
			
			// flushNetwork();
		}
		
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.fluid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

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

	private final World world;
	private LinkedList<FluidNode> nodes;
	private FluidNode masterNode;
	private final int MAX_IO;
	
	public FluidNetwork(World world, FluidNode masterNode) {
		this.world = world;
		nodes = new LinkedList<FluidNode>();
		this.masterNode = masterNode;
		nodes.addLast(masterNode);
		
		this.MAX_IO = Math.min(masterNode.getIFluidContainer().getMaxFluidExportRate(), masterNode.getIFluidContainer().getMaxFluidImportRate());
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
	
	private boolean exists(FluidNode node) {
		if (node == null) return false;
		TileEntity te = world.getTileEntity(node.worldVec().x, node.worldVec().y, node.worldVec().z);
		
		return te != null && te instanceof IFluidHandler;
	}
	
	public FluidNode[] getSurroundingNodes(FluidNode node) {
		if (node == null) return null;
		
		List<FluidNode> list = new ArrayList<FluidNode>(ForgeDirection.VALID_DIRECTIONS.length);
		Vector4<Integer> offset = node.worldVec().copy();
		FluidNode current;
		
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			offset.x = node.worldVec().x + dir.offsetX;
			offset.y = node.worldVec().y + dir.offsetY;
			offset.z = node.worldVec().z + dir.offsetZ;
			current = getNodeAt(offset);
			
			if (current != null) list.add(current);
		}
		
		FluidNode[] ret = new FluidNode[list.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = list.get(i);
		}
		
		// return new FluidNode[list.size()];
		return ret;
	}
	
	public FluidNode[] getSurroundingNodes(Vector4<Integer> vec) {
		FluidNode origin = getNodeAt(vec);
		if (origin == null) return null;
		
		List<FluidNode> list = new ArrayList<FluidNode>(ForgeDirection.VALID_DIRECTIONS.length);
		Vector4<Integer> offset = vec.copy();
		FluidNode current;
		
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			offset.x = vec.x + dir.offsetX;
			offset.y = vec.y + dir.offsetY;
			offset.z = vec.z + dir.offsetZ;
			current = getNodeAt(offset);
			
			if (current != null) list.add(current);
		}
		
		return new FluidNode[list.size()];
	}
	
	/**
	 * Main method call for network to update fluid transfer.
	 */
	public void update() {
		Set<FluidNode> sourceNodes = null;
		Set<FluidNode> acceptorNodes = null;
		
		if (nodes != null && nodes.size() > 0) {
			sourceNodes = new HashSet<FluidNode>();

			ListIterator<FluidNode> iter = nodes.listIterator();
			
			// find our sources and remove, removed fluid nodes:
			// for (FluidNode node : nodes) {
			while (iter.hasNext()) {
				FluidNode node = iter.next();
				
				if (!exists(node) || !node.hasConnections()) {
					remove(node);
					continue;
				}

				if (!node.hasFluidNetwork()) node.setFluidNetwork(this);

				node.update();

				if (node.isSourceNode()) sourceNodes.add(node);
			}

			// if we have sources
			if (sourceNodes != null && sourceNodes.size() > 0) {
				
				// nodes which can receive fluid.
				acceptorNodes = new HashSet<FluidNode>();
				
				// check each source
				for (FluidNode srcNode : sourceNodes) {
					
					// check each direction of node.
					// for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
					for (ForgeDirection dir : srcNode.getConnections()) {
						
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
									if (!fillNode.equals(srcNode)&& info.fluid != null && fillNode.getValveType() != ValveType.OUTPUT && fillNode.getFluidContainer().canFill(dir, info.fluid.getFluid())) {
										acceptorNodes.add(fillNode);
									}
								}
							}
						}
					}
				}
				
				
			}
			
			// flushNetwork();
		}
		
		// ProjectZed.logHelper.info(sourceNodes != null ? sourceNodes.size() : 0, acceptorNodes != null ? acceptorNodes.size() : 0);
		
		// transfer time!
		if (sourceNodes != null && !sourceNodes.isEmpty() && acceptorNodes != null && !acceptorNodes.isEmpty()) {
			HashMap<FluidNode, FluidToken> tokens = new HashMap<FluidNode, FluidToken>();
			
			for (FluidNode node : sourceNodes) {
				if (node.getConnections().length > 0 /*&& node.getConnections()[0] != ForgeDirection.UNKNOWN*/) {
					for (ForgeDirection dir : node.getConnections()) {
						if (dir == null || dir == ForgeDirection.UNKNOWN) {
							// ProjectZed.logHelper.info(dir);
							continue;
						}
						FluidTankInfo[] info = node.getFluidContainer().getTankInfo(dir);
						
						for (int i = 0; i < info.length; i++) {
							if (info[i] != null && info[i].fluid != null && info[i].fluid.amount > 0) tokens.put(node, new FluidToken(info[i].fluid, dir, i));
						}
					}
				}
			}
			
			// ProjectZed.logHelper.info("Tokens empty?", tokens.isEmpty());
			if (!tokens.isEmpty()) {
				
				for (FluidNode node : tokens.keySet()) {
					FluidToken token = tokens.get(node);
					if (token == null || token.dir == null) {
						ProjectZed.logHelper.warn("token is null!", token.dir == null);
						continue;
					}

					int amount = this.MAX_IO;
					
					for (FluidNode accNode : acceptorNodes) {
						if (accNode.getConnections() != null && accNode.getConnections().length > 0) {
							for (ForgeDirection dir : accNode.getConnections()) {
								if (dir == ForgeDirection.UNKNOWN) continue;
								FluidTankInfo[] info = accNode.getFluidContainer().getTankInfo(dir);

								for (int i = 0; i < info.length; i++) {
									if (info[i] != null) {
										int fluidAmount = info[i].fluid != null ? info[i].fluid.amount : 0;
										// ProjectZed.logHelper.info("Token amount:", token.getAmount());
										amount = Math.min(amount, info[i].capacity - fluidAmount);
										amount = Math.min(amount, token.getAmount());
										FluidStack temp = new FluidStack(token.getFluid(), amount);
										
										if (info[i].fluid != null && info[i].fluid.amount < info[i].capacity && info[i].fluid.getFluid().equals(token.getFluid())) {
											node.getFluidContainer().drain(token.getDirection(), temp, true);
											amount = temp.amount = amount - accNode.getFluidContainer().fill(dir, temp, true);
										}
										
										else if (info[i].fluid == null || info[i].fluid.amount == 0) {
											node.getFluidContainer().drain(token.getDirection(), temp, true);
											amount = temp.amount = amount - accNode.getFluidContainer().fill(dir, temp, true);
										}
										
										if (amount == 0) break;
									}
								}
							}
							
							if (amount == 0) break;
						}
					}
				}
			}
			
		}
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((masterNode == null) ? 0 : masterNode.hashCode());
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FluidNetwork other = (FluidNetwork) obj;
		if (masterNode == null) {
			if (other.masterNode != null) return false;
		}
		else if (!masterNode.equals(other.masterNode)) return false;
		if (nodes == null) {
			if (other.nodes != null) return false;
		}
		else if (!nodes.equals(other.nodes)) return false;
		return true;
	}
	
	class FluidToken {
		
		private FluidStack stack;
		private ForgeDirection dir;
		private int index;
		
		FluidToken(FluidStack stack, ForgeDirection dir, int index) {
			this.stack = stack;
			this.dir = dir;
			this.index = index;
		}
		
		FluidStack getFluidStack() {
			return stack;
		}
		
		Fluid getFluid() {
			return stack.getFluid();
		}
		
		int getAmount() {
			return stack.amount;
		}
		
		ForgeDirection getDirection() {
			return dir;
		}
		
		int getIndex() {
			return index;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((dir == null) ? 0 : dir.hashCode());
			result = prime * result + index;
			result = prime * result + ((stack == null) ? 0 : stack.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			FluidToken other = (FluidToken) obj;
			if (!getOuterType().equals(other.getOuterType())) return false;
			if (dir != other.dir) return false;
			if (index != other.index) return false;
			if (stack == null) {
				if (other.stack != null) return false;
			}
			else if (!stack.equals(other.stack)) return false;
			return true;
		}

		private FluidNetwork getOuterType() {
			return FluidNetwork.this;
		}
	}

}

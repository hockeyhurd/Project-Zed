/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.fluid;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.api.fluid.container.IFluidContainer;
import com.projectzed.mod.ProjectZed;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.*;

/**
 * Fluid Network new and improved as of 5/14/15.
 * <br>Upon completion of this class, (old) FluidNet will be REMOVED!
 * 
 * @author hockeyhurd
 * @version May 7, 2015
 */
public final class FluidNetwork {

	private final World world;
	private LinkedList<FluidNode> nodes, lastUpdateNodes;
	private FluidNode masterNode;
	private final int MAX_IO;

	private boolean markUpdate = false;
	private boolean transferring = false;
	private FluidStack transferredFluid = null;

	private FluidPathing pathing;
	
	/**
	 * @param world world to reference.
	 * @param masterNode master node controlling and calling update functions/methods.
	 */
	public FluidNetwork(World world, FluidNode masterNode) {
		this.world = world;
		nodes = new LinkedList<FluidNode>();
		this.masterNode = masterNode;
		nodes.addLast(masterNode);

		lastUpdateNodes = (LinkedList<FluidNode>) nodes.clone();
		
		this.MAX_IO = Math.min(masterNode.getIFluidContainer().getMaxFluidExportRate(), masterNode.getIFluidContainer().getMaxFluidImportRate());

		pathing = new FluidPathing();
	}
	
	/**
	 * Method used to add a fluid node to internal list/network.
	 * 
	 * @param node node to add.
	 */
	public void add(FluidNode node) {
		if (nodes != null && !nodes.contains(node)) nodes.addLast(node);
	}
	
	/**
	 * Removes given node from internal list.
	 * 
	 * @param node node to remove.
	 */
	public void remove(FluidNode node) {
		if (node == null || !nodes.contains(node)) return;
		if (masterNode != null && node.equals(masterNode)) {
			masterNode.getIFluidContainer().setMaster(false); // removing master, notify said tileentity.
			masterNode = null;
			
			for (FluidNode n : nodes) {
				// if (n.getFluidContainer() instanceof TileEntityLiquiductBase) {
				if (n.getFluidContainer() instanceof IFluidContainer && n.getIFluidContainer().canBeMaster()) {
					masterNode = n;
					break;
				}
			}
			 
			// nodes.addFirst(masterNode);
			// flushNetwork();
		}
		
		else nodes.remove(node);

		markUpdate = true;
	}
	
	/**
	 * Simplified function to get size of internal node list.
	 * 
	 * @return size if list isn't null, else returns '0'.
	 */
	public int size() {
		return nodes != null ? nodes.size() : 0;
	}
	
	/**
	 * @return true if nodes list is null or size is equal to 0, else returns false.
	 */
	public boolean isEmpty() {
		return nodes == null || size() == 0;
	}
	
	/**
	 * Gets list of nodes.
	 * 
	 * @return list of nodes in network.
	 */
	public List<FluidNode> getNodes() {
		return nodes;
	}
	
	/**
	 * Getter function to get a fluid node in the fluid network at given vector.
	 * 
	 * @param vec vector to check.
	 * @return fluid node if found at vector, else can return null.
	 */
	public FluidNode getNodeAt(Vector3<Integer> vec) {
		if (nodes != null && size() > 0 && vec != null) {
			for (FluidNode node : nodes) {
				if (node.worldVec().equals(vec)) return node;
			}
		}
		
		return null;
	}
	
	/**
	 * Method used to merge other fluid network with this network.
	 * 
	 * @param other other network to merge with.
	 */
	public void merge(FluidNetwork other) {
		if (other != null && other.size() > 0) {
			for (FluidNode node : other.getNodes()) {
				add(node);
			}
		}
	}
	
	/**
	 * Gets whether given node is the master node.
	 * 
	 * @param node node to reference.
	 * @return true if is master node, else can return false.
	 */
	public boolean isMasterNode(FluidNode node) {
		if (node == null) return false;
		if (masterNode == null) {
			masterNode = node;
			return true;
		}
		
		return masterNode.equals(node);
	}
	
	/**
	 * Gets master node of fluid network.
	 * 
	 * @return master node if exists, else can return null.
	 */
	public FluidNode getMasterNode() {
		return masterNode;
	}
	
	/**
	 * Flushes all nodes in list excluding master node.
	 */
	private void flushNetwork() {
		// nodes = new LinkedList<FluidNode>();
		nodes.clear();
		if (masterNode != null) nodes.addFirst(masterNode);
	}
	
	/**
	 * Internalized function to check if a given node still exists in world.
	 * 
	 * @param node node to reference.
	 * @return true if exists, else returns false.
	 */
	private boolean exists(FluidNode node) {
		if (node == null || node.worldVec() == null) return false;
		TileEntity te = world.getTileEntity(node.worldVec().x, node.worldVec().y, node.worldVec().z);
		
		return te != null && te instanceof IFluidHandler;
	}
	
	/**
	 * Helper function to get surrouding fluid nodes of a fluid node at said node.
	 * 
	 * @param node node to check at.
	 * @return array of adjacent fluid nodes if any, else can return null/empty array.
	 */
	public FluidNode[] getSurroundingNodes(FluidNode node) {
		if (node == null) return null;
		
		List<FluidNode> list = new ArrayList<FluidNode>(ForgeDirection.VALID_DIRECTIONS.length);
		Vector3<Integer> offset = node.worldVec().copy();
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
	
	/**
	 * Helper function to get surrouding fluid nodes of a fluid node at said vector.
	 * 
	 * @param vec vector to check at.
	 * @return array of adjacent fluid nodes if any, else can return null/empty array.
	 */
	public FluidNode[] getSurroundingNodes(Vector3<Integer> vec) {
		FluidNode origin = getNodeAt(vec);
		if (origin == null) return null;
		
		List<FluidNode> list = new ArrayList<FluidNode>(ForgeDirection.VALID_DIRECTIONS.length);
		Vector3<Integer> offset = vec.copy();
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

	// TODO: Implement this method and check each node has more than 1 connection and/or has a 'path' to master node.
	private void detectChanges() {
		markUpdate = false;
	}
	
	/**
	 * Main method call for network to update fluid transfer.
	 */
	public void update() {
		if (markUpdate) {
			detectChanges();
			return;
		}

		boolean transferring = /*this.transferring =*/ false;
		FluidStack transferredFluid = /*this.transferredFluid =*/ null;
		Set<FluidNode> sourceNodes = null;
		Set<FluidNode> acceptorNodes = null;
		
		if (nodes != null && !nodes.isEmpty()) {
			sourceNodes = new HashSet<FluidNode>();

			// ListIterator<FluidNode> iter = nodes.listIterator();
			List<Integer> nodesToRemove = new ArrayList<Integer>(nodes.size());
			
			// find our sources and remove, removed fluid nodes:
			// for (FluidNode node : nodes) {
			// while (iter.hasNext()) {
			
			FluidNode node;
			for (int i = 0; i < nodes.size(); i++) {
				node = nodes.get(i);
				// FluidNode node = iter.next();
				
				if (!node.equals(masterNode) && (node == null || !exists(node)) /*|| !node.hasConnections()*/) {
					// remove(node);
					nodesToRemove.add(i);
					continue;
				}

				if (!node.hasFluidNetwork()) node.setFluidNetwork(this);

				node.update();

				if (node.isSourceNode()) sourceNodes.add(node);
			}
			
			if (!nodesToRemove.isEmpty()) {
				for (int i = 0; i < nodesToRemove.size(); i++) {
					if (i >= nodes.size()) break;
					ProjectZed.logHelper.warn("Removing:", nodes.get((int) nodesToRemove.get(i)));
					nodes.remove((int) nodesToRemove.get(i));
				}
				
				if (nodes.isEmpty()) return;
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
									if (!fillNode.equals(srcNode) && info.fluid != null && fillNode.getValveType() != ValveType.OUTPUT
											&& fillNode.getFluidContainer().canFill(dir, info.fluid.getFluid())) {
									// if (!fillNode.equals(srcNode)&& info.fluid != null && fillNode.getValveType() == ValveType.INPUT && fillNode.getFluidContainer().canFill(dir, info.fluid.getFluid())) {
										if (fillNode.containsFluid(info.fluid.getFluid())
												&& fillNode.getAmountStored(info.fluid.getFluid()) == fillNode.getCapacity(info.fluid.getFluid())) continue;
										acceptorNodes.add(fillNode);
									}
								}
							}
						}
					}
				}
				
				
			}
			
		}
		
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
							if (info[i] != null && info[i].fluid != null && info[i].fluid.amount > 0) tokens.put(node, new FluidToken(info[i].fluid, dir.getOpposite(), i));
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

					// Set pathing start tile.
					pathing.setStartTile(node.getTileAt(world));

					int amount = this.MAX_IO;
					for (FluidNode accNode : acceptorNodes) {
						if (accNode.getConnections() != null && accNode.getConnections().length > 0 && !node.equals(accNode)) {

							// TimeLapse timeLapse = new TimeLapse();
							// Set pathing end tile.
							pathing.setEndTile(accNode.getTileAt(world));

							// Check to see if a valid path has been found, if not continue to next node.
							if (pathing.findPath(world) == null) {
								if (ProjectZed.configHandler.isDebugMode())
									ProjectZed.logHelper.warn("Can't find a path!");

								// ProjectZed.logHelper.info("Found path in:", timeLapse.getEffectiveTimeSince());
								continue;
							}

							else if (ProjectZed.configHandler.isDebugMode()) ProjectZed.logHelper.info("Established a path!");

							for (ForgeDirection dir : accNode.getConnections()) {
								if (dir == ForgeDirection.UNKNOWN) continue;
								FluidTankInfo[] info = accNode.getFluidContainer().getTankInfo(dir);

								for (int i = 0; i < info.length; i++) {
									if (info[i] != null) {
										int fluidAmount = info[i].fluid != null ? info[i].fluid.amount : 0;
										// ProjectZed.logHelper.info("Token amount:", token.getAmount());
										amount = Math.min(amount, info[i].capacity - fluidAmount);
										amount = Math.min(amount, token.getAmount());
										int startAmount = amount;
										FluidStack temp = new FluidStack(token.getFluid(), amount);
										
										if (info[i].fluid != null && info[i].fluid.amount < info[i].capacity && info[i].fluid.getFluid().equals(token.getFluid())) {
											// amount = temp.amount = amount - accNode.getFluidContainer().fill(dir, temp, true);
											amount -= accNode.getFluidContainer().fill(dir, temp, false);
											
											if (amount < 0) amount = 0;
											if (amount == 0) {
												accNode.getFluidContainer().fill(dir, temp, true);
												node.getFluidContainer().drain(token.getDirection(), temp, true);
											}
											
											else {
												temp.amount = amount;
											
												accNode.getFluidContainer().fill(dir, temp, true);
												node.getFluidContainer().drain(token.getDirection(), temp, true);
											}
											
											// node.getFluidContainer().drain(token.getDirection(), temp, true);
										}
										
										else if (info[i].fluid == null || info[i].fluid.amount == 0) {
											// amount = temp.amount = amount - accNode.getFluidContainer().fill(dir, temp, true);
											amount -= accNode.getFluidContainer().fill(dir, temp, false);
											
											if (amount < 0) amount = 0;
											temp.amount = amount;
											
											accNode.getFluidContainer().fill(dir, temp, true);
											node.getFluidContainer().drain(token.getDirection(), temp, true);
										}

										if (startAmount > amount) {
											transferring = true;
											transferredFluid = temp.copy();
											transferredFluid.amount = startAmount - Math.abs(amount);
										}
										
										if (amount == 0 && transferring) break;
									}
								}
							}
							
							if (amount == 0 && transferring) break;
						}
					}
					
					if (amount == 0 && transferring) continue;
				}
			}
			
		}
		
		this.transferring = transferring;
		this.transferredFluid = transferredFluid;
		
	}
	
	/**
	 * @return true if network transferred something last tick, else returns false.
	 */
	public boolean getTransferringState() {
		return transferring;
	}
	
	/**
	 * @return last transferred fluid in network.
	 */
	public FluidStack getTransferredFluid() {
		return transferredFluid;
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
	
	/**
	 * Small helper class for easily creating tokens (fluid nodes) to mapping. 
	 * <br><bold>NOTE: </bold>This class is intended for use on 'sourceNodes' only!
	 * 
	 * @author hockeyhurd
	 * @version May 14, 2015
	 */
	class FluidToken {
		
		private FluidStack stack;
		private ForgeDirection dir;
		private int index;
		
		/**
		 * @param stack fluid stack to track.
		 * @param dir direction can drain from.
		 * @param index index of FluidTankInfo array.
		 */
		FluidToken(FluidStack stack, ForgeDirection dir, int index) {
			this.stack = stack;
			this.dir = dir;
			this.index = index;
		}
		
		/**
		 * Stored fluid stack.
		 * 
		 * @return fluidstack.
		 */
		FluidStack getFluidStack() {
			return stack;
		}
		
		/**
		 * Gets fluid in fluid stack.
		 * 
		 * @return fluid contained in fluidstack.
		 */
		Fluid getFluid() {
			return stack.getFluid();
		}
		
		/**
		 * Gets amount/size of fluidstack.
		 * 
		 * @return amount/size of fluidstack.
		 */
		int getAmount() {
			return stack.amount;
		}
		
		/**
		 * Direction to drain from.
		 * 
		 * @return ForgeDirection to drain from.
		 */
		ForgeDirection getDirection() {
			return dir;
		}
		
		/**
		 * @return index of FluidTankInfo array.
		 */
		int getIndex() {
			return index;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
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
			if (dir != other.dir) return false;
			if (index != other.index) return false;
			if (stack == null) {
				if (other.stack != null) return false;
			}
			else if (!stack.equals(other.stack)) return false;
			return true;
		}
	}

}

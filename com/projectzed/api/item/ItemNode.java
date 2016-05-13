/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.item;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * 
 * 
 * @author hockeyhurd
 * @version May 24, 2015
 */
public class ItemNode {

	private TileEntity te;
	private IInventory inv;
	private EnumFacing[] connections;
	private Vector3<Integer> vec = Vector3.zero.getVector3i();
	
	private ItemNetwork network;
	
	public ItemNode(TileEntity te) {
		this(te, new EnumFacing[] { null });
	}
	
	public ItemNode(TileEntity te, EnumFacing[] connections) {
		this(te, connections, new Vector3<Integer>(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ()));
	}
	
	public ItemNode(TileEntity te, EnumFacing[] connections, Vector3<Integer> vec) {
		this.te = te;
		this.inv = (IInventory) (te instanceof IInventory ? te : null);
		this.vec = vec;
		this.connections = new EnumFacing[connections.length];
		
		for (byte i = 0; i < connections.length; i++) {
			this.connections[i] = connections[i];
		}
	}
	
	public void setItemNetwork(ItemNetwork network) {
		this.network = network;
	}
	
	public ItemNetwork getItemNetwork() {
		return network;
	}
	
	public boolean hasItemNetwork() {
		return network != null;
	}
	
	public boolean hasConnections() {
		if (connections == null || connections.length == 0) return false;
		
		for (byte i = 0; i < connections.length; i++) {
			if (connections[i] != null || connections[i] != null) return true;
		}
		
		return false;
	}
	
	public EnumFacing[] getConnections() {
		return connections;
	}
	
	public void setConnection(EnumFacing dir) {
		if (connections != null && connections.length > 0 && dir != null && dir != null) this.connections[dir.ordinal()] = dir;
	}
	
	public IInventory getIInventory() {
		return inv;
	}
	
	public boolean isIInventory() {
		return te != null && inv != null;
	}
	
	public boolean isEmpty() {
		if (!isIInventory()) return true;
		
		ItemStack current = null;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			current = inv.getStackInSlot(i);
			
			if (current != null && current.stackSize > 0) return false;
		}
		
		return true;
	}
	
	public boolean hasItemStack(ItemStack stack, final boolean checkSize) {
		if (!isIInventory() || stack == null || stack.stackSize == 0) return false;
		
		ItemStack current = null;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			current = inv.getStackInSlot(i);
			
			if (current == null || current.stackSize == 0) continue;
			
			if (checkSize && current.isItemEqual(stack)) return true;
			else if (!checkSize && ItemStack.areItemStacksEqual(stack, current)) return true;
		}
		
		return false;
	}
	
	public int getEmptySlot() {
		if (!isIInventory()) return -1;
		if (isEmpty()) return 0;
		
		ItemStack current = null;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			current = inv.getStackInSlot(i);
			if (current == null || current.stackSize == 0) return i;
		}
		
		return -1;
	}
	
	public boolean hasEmptySlot() {
		int slot = getEmptySlot(); 
		return slot >= 0 && slot < inv.getSizeInventory();
	}
	
	public ItemStack addStack(ItemStack stack) {
		if (!isIInventory() || (!hasItemStack(stack, false) && !isEmpty())) return stack;
		
		// first pass add to existing inv slots.
		ItemStack current = null;
		int amount;
		final int MAX_XFER = inv.getInventoryStackLimit();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			current = inv.getStackInSlot(i);
			if (current == null || current.stackSize == 0) continue;
			else if (current != null && current.isItemEqual(stack)) {
				amount = current.getMaxStackSize() - current.stackSize;
				amount = Math.min(amount, stack.stackSize);
				amount = Math.min(amount, MAX_XFER);
				
				current.stackSize += amount;
				stack.stackSize -= amount;
				
				if (stack.stackSize == 0) return stack = null;				
			}
		}
		
		amount = stack.stackSize;
		amount = Math.min(amount, MAX_XFER);
		
		int slot = getEmptySlot();
		ItemStack buffer = stack.copy();
		buffer.stackSize = amount;
		
		while (slot >= 0 && slot < inv.getSizeInventory() && stack != null && stack.stackSize > 0) {
			inv.setInventorySlotContents(slot, buffer);
			stack.stackSize -= buffer.stackSize;
			if (stack.stackSize == 0) return stack = null;
			
			// continue loop:
			amount = stack.stackSize;
			amount = Math.min(amount, MAX_XFER);
			buffer = stack.copy();
			buffer.stackSize = amount;
			slot = getEmptySlot();
		}
		
		return stack;
	}
	
}

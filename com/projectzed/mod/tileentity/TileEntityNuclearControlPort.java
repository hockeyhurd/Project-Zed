/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlock;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.api.tileentity.IMultiBlockable;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.block.BlockNuclearControlPort;

import java.util.List;

/**
 * TileEntity code for 
 * 
 * @author hockeyhurd
 * @version Mar 12, 2015
 */
public class TileEntityNuclearControlPort extends AbstractTileEntityNuclearComponent {

	private boolean isActive;
	private boolean hasMaster;
	private Vector3<Integer> masterVec = Vector3.zero.getVector3i();
	
	public TileEntityNuclearControlPort() {
		super("nuclearControlPort");
	}
	
	/**
	 * Sets whether is receiving a redstone signal or not.
	 * 
	 * @param active set to true if receiving a redstone signal, else set to false.
	 */
	public void setRedstoneSignal(boolean active) {
		this.isActive = active;
	}
	
	/**
	 * Gets whether is receiving a redstone signal or not.
	 * 
	 * @return true if has a redstone signal, else returns false.
	 */
	public boolean hasRedstoneSignal() {
		return isActive;
	}

	@Override
	public void reset() {
		this.isActive = false;
		this.hasMaster = false;
		this.masterVec = Vector3.zero.getVector3i();
		
		((BlockNuclearControlPort) blockType).updateMeta(false, worldObj, worldVec());
	}

	@Override
	public boolean isUnique() {
		return true;
	}

	@Override
	public boolean isSubstitutable() {
		return false;
	}

	@Override
	public List<IMultiBlockable> getSubList() {
		return null;
	}

	@Override
	public int getAmountFromSize(int width, int height, int depth) {
		return 1;
	}

	@Override
	public AbstractHCoreBlock getBlock() {
		return (AbstractHCoreBlock) ProjectZed.nuclearControlPort;
	}

}

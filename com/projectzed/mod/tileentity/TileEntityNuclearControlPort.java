/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity;

import java.util.List;

import net.minecraft.block.Block;

import com.hockeyhurd.api.math.Vector4Helper;
import com.projectzed.api.tileentity.IMultiBlockable;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.block.BlockNuclearControlPort;

/**
 * TileEntity code for 
 * 
 * @author hockeyhurd
 * @version Mar 12, 2015
 */
public class TileEntityNuclearControlPort extends AbstractTileEntityNuclearComponent {

	private boolean isActive;
	private boolean hasMaster;
	private Vector4Helper<Integer> masterVec = Vector4Helper.zero.getVector4i();
	
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

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#reset()
	 */
	@Override
	public void reset() {
		this.isActive = false;
		this.hasMaster = false;
		this.masterVec = Vector4Helper.zero.getVector4i();
		
		((BlockNuclearControlPort) worldObj.getBlock(worldVec().x, worldVec().y, worldVec().z)).updateMeta(false, worldObj, worldVec());
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isUnique()
	 */
	@Override
	public boolean isUnique() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isSubstituable()
	 */
	@Override
	public boolean isSubstituable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getSubList()
	 */
	@Override
	public List<IMultiBlockable> getSubList() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getAmountFromSize(int, int, int)
	 */
	@Override
	public int getAmountFromSize(int width, int height, int depth) {
		return 1;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getBlock()
	 */
	@Override
	public Block getBlock() {
		return ProjectZed.nuclearControlPort;
	}

}

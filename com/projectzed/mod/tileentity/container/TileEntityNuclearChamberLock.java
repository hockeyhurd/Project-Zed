/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.projectzed.api.tileentity.IMultiBlockable;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.block.BlockNuclearChamberLock;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

/**
 * Class containing code for nuclear chamber lock TE.
 * 
 * @author hockeyhurd
 * @version Feb 23, 2015
 */
public class TileEntityNuclearChamberLock extends AbstractTileEntityNuclearComponent {

	public TileEntityNuclearChamberLock() {
		super("nuclearChamberLock");
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getBlock()
	 */
	@Override
	public Block getBlock() {
		return ProjectZed.nuclearChamberLock;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isUnique()
	 */
	@Override
	public boolean isUnique() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isSubstituable()
	 */
	@Override
	public boolean isSubstituable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getAmountFromSize(int, int, int)
	 */
	@Override
	public int getAmountFromSize(int width, int height, int depth) {
		return 8;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getSubList()
	 */
	@Override
	public List<IMultiBlockable> getSubList() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#reset()
	 */
	@Override
	public void reset() {
		this.isMaster = false;
		this.hasMaster = false;
		this.masterVec = Vector3.zero.getVector3i();
		
		((BlockNuclearChamberLock) worldObj.getBlock(worldVec().x, worldVec().y, worldVec().z)).updateMeta(false, worldObj, worldVec());
	}
	
	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		
		// multiblock stuffs:
		isMaster = comp.getBoolean("ProjectZedIsMaster");
		hasMaster = comp.getBoolean("ProjectZedHasMaster");

		if (masterVec == null) masterVec = Vector3.zero.getVector3i();
		masterVec.x = comp.getInteger("ProjectZedMasterX");
		masterVec.y = comp.getInteger("ProjectZedMasterY");
		masterVec.z = comp.getInteger("ProjectZedMasterZ");
	}
	
	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);

		// multiblock stuffs:
		comp.setBoolean("ProjectZedIsMaster", isMaster);
		comp.setBoolean("ProjectZedHasMaster", hasMaster);
		comp.setInteger("ProjectZedMasterX", masterVec.x);
		comp.setInteger("ProjectZedMasterY", masterVec.y);
		comp.setInteger("ProjectZedMasterZ", masterVec.z);
	}

}

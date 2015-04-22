/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;

import com.hockeyhurd.api.math.Vector4;
import com.projectzed.api.tileentity.IMultiBlockable;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.util.WorldUtils;

/**
 * Class containing tileentity code for nuclearReactorGlass.
 * 
 * @author hockeyhurd
 * @version Mar 6, 2015
 */
public class TileEntityReactorGlass extends AbstractTileEntityNuclearComponent {

	public List<IMultiBlockable> subList;
	
	public TileEntityReactorGlass() {
		super("nuclearReactorGlass");
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getBlock()
	 */
	@Override
	public Block getBlock() {
		return ProjectZed.nuclearReactorGlass;
	}
	
	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#reset()
	 */
	@Override
	public void reset() {
		this.isMaster = false;
		this.hasMaster = false;
		this.masterVec = Vector4.zero.getVector4i();
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isUnique()
	 */
	@Override
	public boolean isUnique() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isSubstituable()
	 */
	@Override
	public boolean isSubstituable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getSubList()
	 */
	@Override
	public List<IMultiBlockable> getSubList() {
		if (subList == null) {
			subList = new ArrayList<IMultiBlockable>();
			subList.add(WorldUtils.createFakeTE(ProjectZed.nuclearChamberWall));
			subList.add(WorldUtils.createFakeTE(ProjectZed.nuclearPowerPort));
			subList.add(WorldUtils.createFakeTE(ProjectZed.nuclearControlPort));
		}
		
		return subList;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getAmountFromSize(int, int, int)
	 */
	@Override
	public int getAmountFromSize(int width, int height, int depth) {
		return width * height * depth - 11 - ((width - 2) * (height - 2) * (depth - 2));
	}

}

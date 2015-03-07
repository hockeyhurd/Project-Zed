/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container;

import java.util.List;

import com.projectzed.api.tileentity.IMultiBlockable;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;

/**
 * Class containing tileentity code for nuclearReactorGlass.
 * 
 * @author hockeyhurd
 * @version Mar 6, 2015
 */
public class TileEntityReactorGlass extends AbstractTileEntityNuclearComponent {

	public TileEntityReactorGlass() {
		super("nuclearReactorGlass");
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IMultiBlockable#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isUnique()
	 */
	@Override
	public boolean isUnique() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#isSubstituable()
	 */
	@Override
	public boolean isSubstituable() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getSubList()
	 */
	@Override
	public List<IMultiBlockable> getSubList() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent#getAmountFromSize(int, int, int)
	 */
	@Override
	public int getAmountFromSize(int width, int height, int depth) {
		// TODO Auto-generated method stub
		return 0;
	}

}

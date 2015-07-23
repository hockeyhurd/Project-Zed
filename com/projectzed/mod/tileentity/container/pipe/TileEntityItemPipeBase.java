/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container.pipe;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.energy.source.IColorComponent;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.api.util.EnumFrameType;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * 
 * 
 * @author hockeyhurd
 * @version Jun 4, 2015
 */
public class TileEntityItemPipeBase extends AbstractTileEntityPipe implements IColorComponent {

	protected EnumColor color;
	
	public TileEntityItemPipeBase(String name, EnumColor color) {
		super(name);
		this.color = color;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.source.IColorComponent#getColor()
	 */
	@Override
	public EnumColor getColor() {
		return this.color;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.energy.source.IColorComponent#setColor(com.projectzed.api.energy.source.EnumColor)
	 */
	@Override
	public void setColor(EnumColor color) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.IWrenchable#worldVec()
	 */
	@Override
	public Vector3<Integer> worldVec() {
		return new Vector3<Integer>(this.xCoord, this.yCoord, this.zCoord);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#updateConnections()
	 */
	@Override
	protected void updateConnections() {
		
		TileEntity te = null;
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			te = worldObj.getTileEntity(worldVec().x + dir.offsetX, worldVec().y + dir.offsetY, worldVec().z + dir.offsetZ);
			
			if (te == null) connections[dir.ordinal()] = null;
			else {
				if (te instanceof TileEntityItemPipeBase) {
					if (((TileEntityItemPipeBase) te).getColor() == getColor()) connections[dir.ordinal()] = dir;
				}
				
				else if (te instanceof IModularFrame && ((IModularFrame) te).getType() == EnumFrameType.ITEM) {
					if (((IModularFrame) te).getSideValve(ForgeDirection.UP.getOpposite()) != 0) connections[dir.ordinal()] = ForgeDirection.UP;
				}
				
				else if (te instanceof IInventory && ((IInventory) te).getSizeInventory() > 0) connections[dir.ordinal()] = dir;
				
				else connections[dir.ordinal()] = null;
			}
			
		}
		
	}

}

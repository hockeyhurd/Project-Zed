/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.tileentity.container.pipe;

import java.util.HashMap;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.hockeyhurd.api.math.Vector3;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.energy.source.IColorComponent;
import com.projectzed.api.tileentity.IModularFrame;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.api.util.EnumFrameType;
import com.projectzed.mod.ProjectZed;

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
			if (te != null && te instanceof TileEntityItemPipeBase && ((TileEntityItemPipeBase) te).getColor() == getColor()) {
				ProjectZed.logHelper.info(dir.name(), connections[dir.ordinal()]);
				connections[dir.ordinal()] = dir;
				continue;
			}
			
			if (te == null || !(te instanceof IInventory) || ((IInventory) te).getSizeInventory() == 0) continue;
			
			// do special checks for modular frame machines:
			if (te instanceof IModularFrame && ((IModularFrame) te).getType() == EnumFrameType.ITEM) {
				connections[dir.ordinal()] = ((IModularFrame) te).getSideValve(dir) != 0 ? dir : null;
			}
			
			else {
				// if is an ISidedInventory:
				if (te instanceof ISidedInventory) {
					connections[dir.ordinal()] = ((ISidedInventory) te).getAccessibleSlotsFromSide(dir.ordinal()) != null
							&& ((ISidedInventory) te).getAccessibleSlotsFromSide(dir.ordinal()).length > 0 ? dir : null;
				}
				
				// else normal IInventory:
				else connections[dir.ordinal()] = dir;
			}
			
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.tileentity.container.AbstractTileEntityPipe#dataToSave()
	 */
	@Override
	public HashMap<String, Number> dataToSave() {
		// TODO Auto-generated method stub
		return null;
	}

}

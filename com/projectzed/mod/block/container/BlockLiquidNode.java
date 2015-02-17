/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.projectzed.api.block.AbstractBlockFluidContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityLiquidNode;

/**
 * Block code for liquid node.
 * 
 * @author hockeyhurd
 * @version Feb 16, 2015
 */
public class BlockLiquidNode extends AbstractBlockFluidContainer {

	/**
	 * @param material
	 */
	public BlockLiquidNode(Material material) {
		super(material, ProjectZed.assetDir, "liquidNode");
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockFluidContainer#getTileEntity()
	 */
	@Override
	public AbstractTileEntityFluidContainer getTileEntity() {
		return new TileEntityLiquidNode();
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockFluidContainer#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockFluidContainer#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	@Override
	protected void doBreakBlock(World world, int x, int y, int z) {
	}

}

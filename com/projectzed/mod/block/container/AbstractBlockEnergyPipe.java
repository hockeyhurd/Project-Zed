/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import com.projectzed.api.block.AbstractBlockPipe;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Class containing block code for energy pipes.
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public abstract class AbstractBlockEnergyPipe extends AbstractBlockPipe {

	protected EnumColor color;
	
	/**
	 * @param material
	 * @param name
	 */
	public AbstractBlockEnergyPipe(Material material, String name, EnumColor color) {
		super(material, name);
		this.color = color;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState blockState) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isOpaqueCube(IBlockState blockState) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		// Create tile entity object at world coordinate.
		TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) world.getTileEntity(pos);

		// Check if block exists.
		if (pipe != null) {
			// this.setBlockBounds(11 * PIXEL / 2, 11 * PIXEL / 2, 11 * PIXEL /
			// 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2);

			// Check if same block is next to this block.
			boolean up = pipe.getConnection(0) != null;
			boolean down = pipe.getConnection(1) != null;
			boolean north = pipe.getConnection(2)!= null;
			boolean east = pipe.getConnection(3) != null;
			boolean south = pipe.getConnection(4) != null;
			boolean west = pipe.getConnection(5) != null;

			// Calculate min values.
			float minX = CALC - (west ? CALC : 0);
			float minY = CALC - (down ? CALC : 0);
			float minZ = CALC - (north ? CALC : 0);

			// Calculate max values.
			float maxX = 1 - CALC + (east ? CALC : 0);
			float maxY = 1 - CALC + (up ? CALC : 0);
			float maxZ = 1 - CALC + (south ? CALC : 0);

			// Set bounds after calculations completed.
			// this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
			return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
		}

		return super.getBoundingBox(state, world, pos);
	}

	@Override
	public abstract AbstractTileEntityPipe getTileEntity();

	@Override
	protected void doBreakBlock(World world, BlockPos pos) {
	}
	
}

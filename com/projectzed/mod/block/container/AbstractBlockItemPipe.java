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
import com.projectzed.mod.proxy.ClientProxy;
import com.projectzed.mod.tileentity.container.pipe.TileEntityItemPipeBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class containing block code for item pipes.
 * 
 * @author hockeyhurd
 * @version Jun 4, 2015
 */
public abstract class AbstractBlockItemPipe extends AbstractBlockPipe {

	protected EnumColor color;
	protected final boolean opaque;
	
	/**
	 * @param material
	 * @param name
	 */
	public AbstractBlockItemPipe(Material material, String name, EnumColor color) {
		super(material, name);
		this.color = color;
		this.opaque = color == EnumColor.GREEN_OPAQUE;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockPipe#getRenderType()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return !this.opaque ? ClientProxy.itemPipeGreen : ClientProxy.itemPipeGreenOpaque;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockPipe#createTileEntity(net.minecraft.world.World, int)
	 */
	@Override
	public TileEntity createNewTileEntity(World world, int id) {
		return getTileEntity();
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockPipe#getTileEntity()
	 */
	@Override
	public abstract AbstractTileEntityPipe getTileEntity();

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockPipe#getSelectedBoundingBoxFromPool(net.minecraft.world.World, int, int, int)
	 */
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		// Create tile entity object at world coordinate.
		TileEntityItemPipeBase pipe = (TileEntityItemPipeBase) world.getTileEntity(pos);

		// Check if block exists.
		if (pipe != null) {
			// this.setBlockBounds(11 * PIXEL / 2, 11 * PIXEL / 2, 11 * PIXEL /
			// 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2);

			// Check if same block is next to this block.
			boolean up = pipe.getConnection(EnumFacing.UP.ordinal()) != null;
			boolean down = pipe.getConnection(EnumFacing.DOWN.ordinal()) != null;
			boolean north = pipe.getConnection(EnumFacing.NORTH.ordinal()) != null;
			boolean east = pipe.getConnection(EnumFacing.EAST.ordinal()) != null;
			boolean south = pipe.getConnection(EnumFacing.SOUTH.ordinal()) != null;
			boolean west = pipe.getConnection(EnumFacing.WEST.ordinal()) != null;

			// Calculate min values.
			float minX = CALC - (west ? CALC : 0);
			float minY = CALC - (down ? CALC : 0);
			float minZ = CALC - (north ? CALC : 0);

			// Calculate max values.
			float maxX = 1 - CALC + (east ? CALC : 0);
			float maxY = 1 - CALC + (up ? CALC : 0);
			float maxZ = 1 - CALC + (south ? CALC : 0);

			// Set bounds after calculations completed.
			return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
		}

		return super.getBoundingBox(state, world, pos);
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockPipe#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	@Override
	protected void doBreakBlock(World world, BlockPos pos) {
	}

}

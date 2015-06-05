/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.projectzed.api.block.AbstractBlockPipe;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.proxy.ClientProxy;
import com.projectzed.mod.tileentity.container.pipe.TileEntityItemPipeBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockPipe#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "pipe_item_item_" + this.color.getColorAsString());
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
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		// Create tile entity object at world coordinate.
		TileEntityItemPipeBase pipe = (TileEntityItemPipeBase) world.getTileEntity(x, y, z);

		// Check if block exists.
		if (pipe != null) {
			// this.setBlockBounds(11 * PIXEL / 2, 11 * PIXEL / 2, 11 * PIXEL /
			// 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2);

			// Check if same block is next to this block.
			boolean up = pipe.getConnection(ForgeDirection.UP.ordinal()) != null;
			boolean down = pipe.getConnection(ForgeDirection.DOWN.ordinal()) != null;
			boolean north = pipe.getConnection(ForgeDirection.NORTH.ordinal()) != null;
			boolean east = pipe.getConnection(ForgeDirection.EAST.ordinal()) != null;
			boolean south = pipe.getConnection(ForgeDirection.SOUTH.ordinal()) != null;
			boolean west = pipe.getConnection(ForgeDirection.WEST.ordinal()) != null;

			// Calculate min values.
			float minX = CALC - (west ? CALC : 0);
			float minY = CALC - (down ? CALC : 0);
			float minZ = CALC - (north ? CALC : 0);

			// Calculate max values.
			float maxX = 1 - CALC + (east ? CALC : 0);
			float maxY = 1 - CALC + (up ? CALC : 0);
			float maxZ = 1 - CALC + (south ? CALC : 0);

			// Set bounds after calculations completed.
			this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
		}

		return AxisAlignedBB.getBoundingBox(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getCollisionBoundingBoxFromPool(net.minecraft.world.World, int, int, int)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		// Create tile entity object at world coordinate.
		TileEntityItemPipeBase pipe = (TileEntityItemPipeBase) world.getTileEntity(x, y, z);

		// Check if block exists.
		if (pipe != null) {
			// this.setBlockBounds(11 * PIXEL / 2, 11 * PIXEL / 2, 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2);

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
			this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
		}

		return AxisAlignedBB.getBoundingBox(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockPipe#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	@Override
	protected void doBreakBlock(World world, int x, int y, int z) {
	}

}

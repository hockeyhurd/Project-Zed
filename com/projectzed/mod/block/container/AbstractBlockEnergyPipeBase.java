package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.projectzed.api.block.AbstractBlockPipe;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.proxy.ClientProxy;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing block code for energy pipes.
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public abstract class AbstractBlockEnergyPipeBase extends AbstractBlockPipe {

	protected EnumColor color;
	
	/**
	 * @param material
	 * @param name
	 */
	public AbstractBlockEnergyPipeBase(Material material, String name, EnumColor color) {
		super(material, name);
		this.color = color;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "pipe_energy_item_" + this.color.getColorAsString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.block.AbstractBlockPipe#getRenderType()
	 */
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return ClientProxy.energyPipeRed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.projectzed.api.block.AbstractBlockPipe#getCollisionBoundingBoxFromPool(net.minecraft.world.World, int, int, int)
	 */
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		// Create tile entity object at world coordinate.
		TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) world.getTileEntity(x, y, z);

		// Check if block exists.
		if (pipe != null) {
			// this.setBlockBounds(11 * PIXEL / 2, 11 * PIXEL / 2, 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2);

			// Check if same block is next to this block.
			boolean up = pipe.connections[0] != null;
			boolean down = pipe.connections[1] != null;
			boolean north = pipe.connections[2] != null;
			boolean east = pipe.connections[3] != null;
			boolean south = pipe.connections[4] != null;
			boolean west = pipe.connections[5] != null;
			
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		// Create tile entity object at world coordinate.
		TileEntityEnergyPipeBase pipe = (TileEntityEnergyPipeBase) world.getTileEntity(x, y, z);

		// Check if block exists.
		if (pipe != null) {
			// this.setBlockBounds(11 * PIXEL / 2, 11 * PIXEL / 2, 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2);

			// Check if same block is next to this block.
			boolean up = pipe.connections[0] != null;
			boolean down = pipe.connections[1] != null;
			boolean north = pipe.connections[2] != null;
			boolean east = pipe.connections[3] != null;
			boolean south = pipe.connections[4] != null;
			boolean west = pipe.connections[5] != null;
			
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
	 * @see com.projectzed.api.block.AbstractBlockPipe#getTileEntity()
	 */
	@Override
	public abstract AbstractTileEntityPipe getTileEntity();

}

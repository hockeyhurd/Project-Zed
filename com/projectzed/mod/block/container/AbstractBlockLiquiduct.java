/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/

package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.projectzed.api.block.AbstractBlockPipe;
import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.proxy.ClientProxy;
import com.projectzed.mod.tileentity.container.pipe.TileEntityLiquiductBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Abstract block class for liquiducts.
 * 
 * @author hockeyhurd
 * @version Feb 12, 2015
 */
public abstract class AbstractBlockLiquiduct extends AbstractBlockPipe {

	protected EnumColor color;
	
	/**
	 * @param material
	 * @param assetDir
	 * @param name
	 */
	public AbstractBlockLiquiduct(Material material, String name, EnumColor color) {
		super(material, name);
		this.color = color;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockPipe#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "pipe_fluid_item_" + this.color.getColorAsString());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockPipe#getRenderType()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return ClientProxy.liquiductBlue;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	@Override
	public TileEntity createNewTileEntity(World world, int id) {
		return getTileEntity();
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockPipe#getTileEntity()
	 */
	@Override
	public abstract AbstractTileEntityPipe getTileEntity();
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockPipe#getSelectedBoundingBoxFromPool(net.minecraft.world.World, int, int, int)
	 */
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		// Create tile entity object at world coordinate.
		TileEntityLiquiductBase pipe = (TileEntityLiquiductBase) world.getTileEntity(x, y, z);

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

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getCollisionBoundingBoxFromPool(net.minecraft.world.World, int, int, int)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		// Create tile entity object at world coordinate.
		TileEntityLiquiductBase pipe = (TileEntityLiquiductBase) world.getTileEntity(x, y, z);

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
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockPlacedBy(net.minecraft.world.World, int, int, int, net.minecraft.entity.EntityLivingBase, net.minecraft.item.ItemStack)
	 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase e, ItemStack stack) {
		if (stack.hasTagCompound() && world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityLiquiductBase) {
			TileEntityLiquiductBase te = (TileEntityLiquiductBase) world.getTileEntity(x, y, z);
			
			int id = (int) stack.stackTagCompound.getFloat("Fluid Amount");
			if (id == -1) return;
			
			int amount = (int) stack.stackTagCompound.getFloat("Fluid ID");
			
			te.getTank().setFluid(new FluidStack(FluidRegistry.getFluid(id), amount));
		}
	}

}

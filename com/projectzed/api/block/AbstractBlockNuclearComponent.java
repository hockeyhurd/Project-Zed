/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Framework for nuclear chamber parts.
 * 
 * @author hockeyhurd
 * @version Feb 23, 2015
 */
public abstract class AbstractBlockNuclearComponent extends BlockContainer {

	/** Name of the block */
	protected String name;

	/** Asset directory of block. */
	protected String assetDir;
	
	protected Random random = new Random();
	
	/**
	 * @param material
	 * @param assetDir
	 * @param name
	 */
	public AbstractBlockNuclearComponent(String name) {
		super(Material.iron);
		this.assetDir = ProjectZed.assetDir;
		this.name = name;
		this.setBlockName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(1.0f);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(assetDir + name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	@Override
	public TileEntity createNewTileEntity(World world, int id) {
		return getTileEntity();
	}

	/**
	 * Abstract getter function to get associated TE object.
	 * 
	 * @return TE object associated with block.
	 */
	public abstract AbstractTileEntityNuclearComponent getTileEntity();
	
}

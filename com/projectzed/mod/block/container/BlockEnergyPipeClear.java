package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.api.source.EnumColor;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeClear;

/**
 * 
 * @author hockeyhurd
 * @version Nov 15, 2014
 */
public class BlockEnergyPipeClear extends BlockEnergyPipeBase {

	/**
	 * @param material
	 * @param name
	 * @param color
	 */
	public BlockEnergyPipeClear(Material material, String name, EnumColor color) {
		super(material, name, color);
	}
	
	public TileEntity createTileEntity(World world, int id) {
		return new TileEntityEnergyPipeClear();
	}

}

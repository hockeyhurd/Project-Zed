package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.api.source.EnumColor;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeRed;

/**
 * Class used to create more specific instance of base class
 * in which this class inherits from.
 * 
 * @author hockeyhurd
 * @version Nov 13, 2014
 */
public class BlockEnergyPipeRed extends BlockEnergyPipeBase {

	/**
	 * @param material
	 * @param name
	 * @param color
	 */
	public BlockEnergyPipeRed(Material material, String name, EnumColor color) {
		super(material, name, color);
	}
	
	public TileEntity createNewTileEntity(World world, int id) {
		return new TileEntityEnergyPipeRed();
	}

}

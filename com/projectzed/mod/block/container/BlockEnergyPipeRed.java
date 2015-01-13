package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeRed;

/**
 * Class used to create more specific instance of base class
 * in which this class inherits from.
 * 
 * @author hockeyhurd
 * @version Nov 13, 2014
 */
public class BlockEnergyPipeRed extends AbstractBlockEnergyPipeBase {

	/**
	 * @param material
	 * @param name
	 * @param color
	 */
	public BlockEnergyPipeRed(Material material, String name, EnumColor color) {
		super(material, name, color);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.block.container.BlockEnergyPipeBase#getTileEntity()
	 */
	@Override
	public AbstractTileEntityPipe getTileEntity() {
		return new TileEntityEnergyPipeRed();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	@Override
	public TileEntity createNewTileEntity(World world, int id) {
		return getTileEntity();
	}

}

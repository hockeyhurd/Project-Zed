package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.api.energy.source.EnumColor;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipeClear;

/**
 * 
 * @author hockeyhurd
 * @version Nov 15, 2014
 */
public class BlockEnergyPipeClear extends AbstractBlockEnergyPipeBase {

	/**
	 * @param material
	 * @param name
	 * @param color
	 */
	public BlockEnergyPipeClear(Material material, String name, EnumColor color) {
		super(material, name, color);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.mod.block.container.BlockEnergyPipeBase#getTileEntity()
	 */
	@Override
	public AbstractTileEntityPipe getTileEntity() {
		return new TileEntityEnergyPipeClear();
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

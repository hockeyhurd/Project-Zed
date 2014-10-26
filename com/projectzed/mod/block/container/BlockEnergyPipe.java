package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.projectzed.api.block.AbstractBlockPipe;
import com.projectzed.mod.proxy.ClientProxy;
import com.projectzed.mod.tileentity.container.pipe.TileEntityEnergyPipe;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public class BlockEnergyPipe extends AbstractBlockPipe {

	/**
	 * @param material
	 * @param name
	 */
	public BlockEnergyPipe(Material material, String name) {
		super(material, name);
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockPipe#getRenderType()
	 */
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return ClientProxy.energyPipe;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockPipe#createNewTileEntity(net.minecraft.world.World, int)
	 */
	public TileEntity createNewTileEntity(World world, int id) {
		return new TileEntityEnergyPipe(this.customName);
	}

}

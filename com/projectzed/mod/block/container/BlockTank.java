package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;

/**
 * 
 * 
 * @author hockeyhurd
 * @version Jan 7, 2015
 */
public class BlockTank extends AbstractBlockContainer {

	/**
	 * @param material
	 * @param assetDir
	 * @param name
	 */
	public BlockTank(Material material, String name) {
		super(material, ProjectZed.assetDir, name);
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#getTileEntity()
	 */
	@Override
	public AbstractTileEntityEnergyContainer getTileEntity() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	@Override
	protected void doBreakBlock(World world, int x, int y, int z) {
		// TODO Auto-generated method stub

	}

}

package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityContainer;
import com.projectzed.mod.ProjectZed;

/**
 * Class containing block code for RF bridge.
 * 
 * @author hockeyhurd
 * @version Nov 29, 2014
 */
public class BlockRFBridge extends AbstractBlockContainer {

	public BlockRFBridge(Material material) {
		super(material, ProjectZed.assetDir, "bridgeRF");
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#getTileEntity()
	 */
	protected AbstractTileEntityContainer getTileEntity() {
		return new TileEntityRFBridge();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	protected void doBreakBlock(World world, int x, int y, int z) {
		TileEntityRFBridge te = (TileEntityRFBridge) world.getTileEntity(x, y, z);
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}


}

package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;

import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.mod.proxy.ClientProxy;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankTier2;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Tier 2 fluid tank block code.
 * 
 * @author hockeyhurd
 * @version Jan 24, 2015
 */
public class BlockTankTier2 extends AbstractBlockTankBase {

	/**
	 * @param material
	 */
	public BlockTankTier2(Material material) {
		super(material, "fluidTankTier2");
		this.tier = (byte) 2;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.mod.block.container.BlockTankBase#getRenderType()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return ClientProxy.fluidTankTier2;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.mod.block.container.BlockTankBase#getTileEntity()
	 */
	@Override
	public AbstractTileEntityFluidContainer getTileEntity() {
		return new TileEntityFluidTankTier2();
	}

}

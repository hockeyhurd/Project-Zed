package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;

import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.mod.proxy.ClientProxy;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankTier1;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Tier 1 fluid tank block code.
 * 
 * @author hockeyhurd
 * @version Jan 24, 2015
 */
public class BlockTankTier1 extends AbstractBlockTankBase {

	/**
	 * @param material
	 */
	public BlockTankTier1(Material material) {
		super(material, "fluidTankTier1");
		this.tier = (byte) 1;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.mod.block.container.BlockTankBase#getRenderType()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return ClientProxy.fluidTankTier1;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.mod.block.container.BlockTankBase#getTileEntity()
	 */
	@Override
	public AbstractTileEntityFluidContainer getTileEntity() {
		return new TileEntityFluidTankTier1();
	}

}

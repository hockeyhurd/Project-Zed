package com.projectzed.mod.block.container;

import net.minecraft.block.material.Material;

import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.mod.proxy.ClientProxy;
import com.projectzed.mod.tileentity.container.TileEntityFluidTankTier3;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Tier 3 fluid tank block code.
 * 
 * @author hockeyhurd
 * @version Jan 24, 2015
 */
public class BlockTankTier3 extends AbstractBlockTankBase {

	/**
	 * @param material
	 */
	public BlockTankTier3(Material material) {
		super(material, "fluidTankTier3");
		this.tier = (byte) 3;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.mod.block.container.BlockTankBase#getRenderType()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return ClientProxy.fluidTankTier3;
	}

	/* (non-Javadoc)
	 * @see com.projectzed.mod.block.container.BlockTankBase#getTileEntity()
	 */
	@Override
	public AbstractTileEntityFluidContainer getTileEntity() {
		TileEntityFluidTankTier3 tank = new TileEntityFluidTankTier3();
		tank.setTier((byte) 2);
		return tank;
	}

}

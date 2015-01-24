package com.projectzed.mod.tileentity.container;

/**
 * Tier 1 fluid tank te code.
 * 
 * @author hockeyhurd
 * @version Jan 24, 2015
 */
public class TileEntityFluidTankTier1 extends TileEntityFluidTankBase {

	/**
	 * @see com.projectzed.mod.tileentity.container.TileEntityFluidTankBase#TileEntityFluidTankBase()
	 */
	public TileEntityFluidTankTier1() {
		this.tier = 1;
		this.maxFluidStorage = this.TIER_SIZE[this.tier];
		this.customName += this.tier;
	}

}

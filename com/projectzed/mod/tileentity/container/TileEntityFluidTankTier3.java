package com.projectzed.mod.tileentity.container;

/**
 * Tier 3 fluid tank te code.
 * 
 * @author hockeyhurd
 * @version Jan 24, 2015
 */
public class TileEntityFluidTankTier3 extends TileEntityFluidTankBase {

	/**
	 * @see com.projectzed.mod.tileentity.container.TileEntityFluidTankBase#TileEntityFluidTankBase()
	 */
	public TileEntityFluidTankTier3() {
		this.tier = 3;
		this.maxFluidStorage = this.TIER_SIZE[this.tier];
		this.customName += this.tier;
	}

}

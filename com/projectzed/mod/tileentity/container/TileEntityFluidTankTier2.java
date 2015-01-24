package com.projectzed.mod.tileentity.container;

/**
 * Tier 2 fluid tank te code.
 * 
 * @author hockeyhurd
 * @version Jan 24, 2015
 */
public class TileEntityFluidTankTier2 extends TileEntityFluidTankBase {

	/**
	 * @see com.projectzed.mod.tileentity.container.TileEntityFluidTankBase#TileEntityFluidTankBase()
	 */
	public TileEntityFluidTankTier2() {
		this.tier = 2;
		this.maxFluidStorage = this.TIER_SIZE[this.tier];
		this.customName += this.tier;
	}

}

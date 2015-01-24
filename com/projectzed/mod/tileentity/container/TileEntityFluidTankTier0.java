package com.projectzed.mod.tileentity.container;

/**
 * Tier 0 fluid tank te code.
 * 
 * @author hockeyhurd
 * @version Jan 24, 2015
 */
public class TileEntityFluidTankTier0 extends TileEntityFluidTankBase {

	/**
	 * @see com.projectzed.mod.tileentity.container.TileEntityFluidTankBase#TileEntityFluidTankBase()
	 */
	public TileEntityFluidTankTier0() {
		this.tier = 0;
		this.maxFluidStorage = this.TIER_SIZE[this.tier];
		this.customName += this.tier;
	}
	
}

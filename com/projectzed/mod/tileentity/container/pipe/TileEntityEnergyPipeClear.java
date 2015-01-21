package com.projectzed.mod.tileentity.container.pipe;

import com.projectzed.api.energy.source.EnumColor;

/**
 * 
 * @author hockeyhurd
 * @version Nov 15, 2014
 */
public class TileEntityEnergyPipeClear extends TileEntityEnergyPipeBase {

	public TileEntityEnergyPipeClear() {
		super();
		this.maxPowerStorage *= 8;
		this.importRate *= 8;
		this.exportRate *= 8;
	}
	
	public EnumColor getColor() {
		return EnumColor.CLEAR;
	}

}

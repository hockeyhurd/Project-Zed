package com.projectzed.mod.tileentity.container.pipe;

import com.projectzed.api.source.EnumColor;

/**
 * Class used to create more specific instance of base class
 * in which this class inherits from.
 * 
 * @author hockeyhurd
 * @version Nov 13, 2014
 */
public class TileEntityEnergyPipeOrange extends TileEntityEnergyPipeBase {

	public TileEntityEnergyPipeOrange() {
		super();
		this.maxStorage *= 2;
		this.importRate *= 2;
		this.exportRate *= 2;
	}
	
	public EnumColor getColor() {
		return EnumColor.ORANGE;
	}
	
}

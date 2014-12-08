package com.projectzed.mod.renderer;

import net.minecraft.util.IIcon;

import com.hockeyhurd.api.item.AbstractItemRenderer;

/**
 * Simple class extending AbstractItemRenderer.
 * <br><br>For more info on implementation, see: {@link com.hockeyhurd.api.item.AbstractItemRenderer}.
 * 
 * @author hockeyhurd
 * @version Dec 7, 2014
 */
public class EnergyBankItemRenderer extends AbstractItemRenderer {
	
	/**
	 * @param icon = icon to draw from.
	 */
	public EnergyBankItemRenderer(IIcon icon) {
		super(icon);
	}

}

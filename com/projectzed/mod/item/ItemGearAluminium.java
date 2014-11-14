package com.projectzed.mod.item;

import com.hockeyhurd.api.item.AbstractItemMetalic;
import com.projectzed.mod.ProjectZed;

/**
 * Class containing code for gear aluminium.
 * 
 * @author hockeyhurd
 * @version Nov 14, 2014
 */
public class ItemGearAluminium extends AbstractItemMetalic {

	public ItemGearAluminium(String name, String assetDir) {
		super(name, assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

}

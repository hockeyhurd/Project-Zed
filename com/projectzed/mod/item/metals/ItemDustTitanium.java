package com.projectzed.mod.item.metals;

import com.hockeyhurd.api.item.AbstractItemMetalic;
import com.projectzed.mod.ProjectZed;

/**
 * 
 * @author hockeyhurd
 * @version Nov 5, 2014
 */
public class ItemDustTitanium extends AbstractItemMetalic {

	/**
	 * @param name
	 * @param assetDir
	 */
	public ItemDustTitanium(String name, String assetDir) {
		super(name, assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

}

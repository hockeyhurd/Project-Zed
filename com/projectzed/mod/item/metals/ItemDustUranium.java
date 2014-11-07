package com.projectzed.mod.item.metals;

import com.hockeyhurd.api.item.AbstractItemMetalic;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.creativetabs.ProjectZedCreativeTab;

/**
 * 
 * @author hockeyhurd
 * @version Nov 6, 2014
 */
public class ItemDustUranium extends AbstractItemMetalic {

	/**
	 * @param name
	 * @param assetDir
	 */
	public ItemDustUranium(String name, String assetDir) {
		super(name, assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

}

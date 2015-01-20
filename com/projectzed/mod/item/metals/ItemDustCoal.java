package com.projectzed.mod.item.metals;

import com.hockeyhurd.api.item.AbstractItemMetalic;
import com.projectzed.mod.ProjectZed;

/**
 * 
 * 
 * @author hockeyhurd
 * @version Jan 20, 2015
 */
public class ItemDustCoal extends AbstractItemMetalic {

	/**
	 * @param name
	 * @param assetDir
	 */
	public ItemDustCoal(String name, String assetDir) {
		super(name, assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

}

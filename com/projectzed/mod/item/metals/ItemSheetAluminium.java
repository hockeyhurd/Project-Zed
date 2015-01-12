package com.projectzed.mod.item.metals;

import com.hockeyhurd.api.item.AbstractItemMetalic;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.creativetabs.ProjectZedCreativeTab;

/**
 * Class containging code for sheet of aluminium.
 * 
 * @author hockeyhurd
 * @version Nov 14, 2014
 */
public class ItemSheetAluminium extends AbstractItemMetalic {

	public ItemSheetAluminium(String name, String assetDir) {
		super(name, assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

}

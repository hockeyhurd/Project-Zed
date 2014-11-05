package com.projectzed.mod.item;

import com.hockeyhurd.api.item.AbstractItemMetalic;
import com.projectzed.mod.ProjectZed;

/**
 * 
 * @author hockeyhurd
 * @version Nov 4, 2014
 */
public class ItemDustIron extends AbstractItemMetalic {

	public ItemDustIron(String name, String assetDir) {
		super(name, assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

}

package com.projectzed.mod.item;

import com.hockeyhurd.api.item.AbstractItemMetalic;
import com.projectzed.mod.ProjectZed;

/**
 * Class containing code for gold dust.
 * 
 * @author hockeyhurd
 * @version Nov 4, 2014
 */
public class ItemDustGold extends AbstractItemMetalic {

	public ItemDustGold(String name, String assetDir) {
		super(name, assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

}

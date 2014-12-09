package com.projectzed.mod.item;

import com.hockeyhurd.api.item.AbstractItemMetalic;
import com.projectzed.mod.ProjectZed;

/**
 * Class containing code for forgingHammer.
 * 
 * @author hockeyhurd
 * @version Dec 9, 2014
 */
public class ItemForgingHammer extends AbstractItemMetalic {

	public ItemForgingHammer() {
		super("forgingHammer", ProjectZed.assetDir);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

}

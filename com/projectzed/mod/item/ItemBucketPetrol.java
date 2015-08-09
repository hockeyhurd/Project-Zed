/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.item;

import com.hockeyhurd.api.item.AbstractItemBucket;
import com.projectzed.mod.ProjectZed;
import net.minecraft.block.Block;

/**
 * @author hockeyhurd
 * @version 8/8/2015.
 */
public class ItemBucketPetrol extends AbstractItemBucket {

	/**
	 * @param name     name of item bucket.
	 * @param block    reference to block.
	 */
	public ItemBucketPetrol(String name, Block block) {
		super(name, ProjectZed.assetDir, block);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

}

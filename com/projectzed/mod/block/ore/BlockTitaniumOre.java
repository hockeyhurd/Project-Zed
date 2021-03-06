/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.ore;

import com.hockeyhurd.hcorelib.api.block.AbstractBlockOre;
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.mod.ProjectZed;
import net.minecraft.block.material.Material;

/**
 * Block class for Titanium Ore.
 *
 * @author hockeyhurd
 * @version Nov 5, 2014
 */
public class BlockTitaniumOre extends AbstractBlockOre {

	/**
	 * @param material
	 * @param name
	 */
	public BlockTitaniumOre(Material material, String assetDir, String name) {
		super(material, ProjectZed.modCreativeTab, assetDir, name);
	}

	@Override
	public EnumHarvestLevel getHarvestLevel() {
		return EnumHarvestLevel.PICKAXE_IRON;
	}
}

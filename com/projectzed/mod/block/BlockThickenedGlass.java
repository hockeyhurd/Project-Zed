/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import com.projectzed.mod.ProjectZed;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import java.util.Random;

/**
 * Class containing block code for thickenedGlass.
 * 
 * @author hockeyhurd
 * @version Nov 20, 2014
 */
public class BlockThickenedGlass extends BlockGlass {

	public BlockThickenedGlass() {
		super(Material.glass, false);
		this.setRegistryName("thickenedGlass");
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(0.75f);
		this.setResistance(2000.0f);
		this.setStepSound(SoundType.GLASS);
		this.setLightOpacity(0);
	}
	
	@Override
	public boolean isVisuallyOpaque() {
		return false;
	}
	
	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

}

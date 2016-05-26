/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlockContainer;
import com.hockeyhurd.hcorelib.api.tileentity.AbstractTile;
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.TileEntityWickedClearGlass;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import java.util.Random;

/**
 * Block class for wickedClearGlass.
 * 
 * @author hockeyhurd
 * @version Mar 26, 2015
 */
public class BlockWickedClearGlass extends AbstractHCoreBlockContainer {

	public BlockWickedClearGlass() {
		super(Material.glass, ProjectZed.modCreativeTab, ProjectZed.assetDir, "wickedClearGlass");
		this.setResistance(2000.0f);
		this.setStepSound(SoundType.GLASS);
		this.setLightOpacity(0);
	}

	@Override
	public Block getBlock() {
		return this;
	}

	@Override
	public float getBlockHardness() {
		return 0.75f;
	}

	@Override
	public EnumHarvestLevel getHarvestLevel() {
		return EnumHarvestLevel.PICKAXE_STONE;
	}

	@Override
	public AbstractTile getTileEntity() {
		return new TileEntityWickedClearGlass();
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

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import com.hockeyhurd.hcorelib.api.block.IHBlock;
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.mod.ProjectZed;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

/**
 * Class containing block code for thickenedGlass.
 * 
 * @author hockeyhurd
 * @version Nov 20, 2014
 */
public class BlockThickenedGlass extends BlockGlass implements IHBlock {

	private static final String name = "thickenedGlass";
	private static final ResourceLocation resourceLocation = new ResourceLocation(ProjectZed.assetDir, name);
	private ItemBlock itemBlock;

	public BlockThickenedGlass() {
		super(Material.GRASS, false);

		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(getBlockHardness());
		this.setResistance(2000.0f);
		this.setSoundType(SoundType.GLASS);
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

	@Override
	public BlockThickenedGlass getBlock() {
		return this;
	}

	@Override
	public boolean hasSpecialRenderer() {
		return false;
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return resourceLocation;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemBlock getItemBlock() {
		return itemBlock != null ? itemBlock : (itemBlock = new ItemBlock(this));
	}

	@Override
	public float getBlockHardness() {
		return 0.75f;
	}

	@Override
	public EnumHarvestLevel getHarvestLevel() {
		return EnumHarvestLevel.PICKAXE_WOOD;
	}
}

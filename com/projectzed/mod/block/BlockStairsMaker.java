/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.block;

import com.hockeyhurd.hcorelib.api.block.IHBlock;
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.mod.ProjectZed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

/**
 * Simple block class for creating stairs more easily.
 *
 * @author hockeyhurd
 * @version 7/9/2015.
 */
public class BlockStairsMaker extends BlockStairs implements IHBlock {

	private String name;
	private ResourceLocation resourceLocation;
	private ItemBlock itemBlock;

	public BlockStairsMaker(Block block) {
		super(block.getDefaultState());

		this.name = getName(block) + "Stairs";

		setCreativeTab(ProjectZed.modCreativeTab);
		setUnlocalizedName(name);
		setRegistryName(name);
		setHardness(getBlockHardness());
		setHarvestLevel(getHarvestLevel().getTypeName(), getHarvestLevel().getLevel());
		setDefaultState(blockState.getBaseState());

		resourceLocation = new ResourceLocation(ProjectZed.assetDir, name);
	}

	private String getName(Block block) {
		final String rawName = block.getUnlocalizedName().substring(5);
		final StringBuilder newName = new StringBuilder(rawName.length());

		for (char c : rawName.toCharArray()) {
			if (c != ' ' && c != '.') newName.append(c);
		}

		return newName.toString();
	}

	@Override
	public Block getBlock() {
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
		return 1.0f;
	}

	@Override
	public EnumHarvestLevel getHarvestLevel() {
		return EnumHarvestLevel.PICKAXE_WOOD;
	}
}

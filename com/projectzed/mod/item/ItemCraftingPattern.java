/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.item;

import com.hockeyhurd.api.item.AbstractHCoreItem;
import com.projectzed.api.item.IPattern;
import com.projectzed.mod.ProjectZed;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

/**
 * Item class for crafting patterns.
 *
 * @author hockeyhurd
 * @version 4/30/2016.
 */
public class ItemCraftingPattern extends AbstractHCoreItem implements IPattern {

	private boolean encoded;
	private final ItemStack[][] pattern;
	private ItemStack result;
	private int size;

	public ItemCraftingPattern(String name, boolean encoded) {
		this(name, encoded, 3);
	}

	public ItemCraftingPattern(String name, boolean encoded, int size) {
		super(ProjectZed.modCreativeTab, name, ProjectZed.assetDir);
		this.encoded = encoded;
		pattern = new ItemStack[size][size];
	}

	@Override
	public boolean hasPattern() {
		return encoded;
	}

	@Override
	public ItemStack[][] getPattern() {
		return pattern;
	}

	@Override
	public ItemStack getCraftingResult() {
		return result;
	}

	@Override
	public boolean isPatternEqual(ItemStack[][] pattern) {
		if (pattern == null || size != pattern.length || pattern[0] == null || size != pattern[0].length)
			return false;

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				if (!ItemStack.areItemStacksEqual(this.pattern[y][x], pattern[y][x]))
					return false;
			}
		}

		return true;
	}

	@Override
	public void setPattern(ItemStack[][] pattern, ItemStack resultStack) {
		if (resultStack == null || pattern == null || size != pattern.length || pattern[0] == null || size != pattern[0].length)
			return;

		this.result = resultStack;

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				this.pattern[y][x] = pattern[y][x];
			}
		}
	}

	@Override
	public void clearPattern() {
		result = null;

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				this.pattern[y][x] = null;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		if (!encoded || result == null) return;

		list.add(EnumChatFormatting.GREEN + "Result: " + result.getDisplayName());

		for (int y = 0; y < size; y++) {
			String[] strings = new String[size];
			for (int x = 0; x < size; x++) {
				strings[x] = result.getDisplayName();
			}

			list.add(getConcatString("[ ", " ]", strings));
		}
	}

	@SideOnly(Side.CLIENT)
	private String getConcatString(String prefix, String suffix, String[] strings) {
		StringBuilder builder = new StringBuilder();
		builder.append(prefix);

		for (int i = 0; i < strings.length; i++) {
			builder.append(strings[i]);
			if (i + 1 < strings.length) builder.append(", ");
		}

		builder.append(suffix);

		return builder.toString();
	}

}

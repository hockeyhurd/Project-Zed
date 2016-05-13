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

import com.hockeyhurd.hcorelib.api.item.AbstractHCoreItem;
import com.hockeyhurd.hcorelib.api.math.Vector2;
import com.projectzed.api.item.IPattern;
import com.projectzed.mod.ProjectZed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Item class for crafting patterns.
 *
 * @author hockeyhurd
 * @version 4/30/2016.
 */
public class ItemCraftingPattern extends AbstractHCoreItem implements IPattern {

	private static final String COMP_HAS_PATTERN = "ItemCraftingPattern:hasPattern";
	private static final String COMP_RESULT = "ItemCraftingPattern:Result";
	private static final String COMP_SIZE_X = "ItemCraftingPattern:sizeX";
	private static final String COMP_SIZE_Y = "ItemCraftingPattern:sizeY";
	private static final String COMP_ITEMS = "ItemCraftingPattern:Items";

	private boolean encoded;
	private final ItemStack[][] pattern;
	private ItemStack result;
	private Vector2<Integer> size;

	public ItemCraftingPattern(String name, boolean encoded) {
		this(name, encoded, 3);
	}

	public ItemCraftingPattern(String name, boolean encoded, int size) {
		super(ProjectZed.modCreativeTab, name, ProjectZed.assetDir);
		this.encoded = encoded;

		this.size = new Vector2<Integer>(size, size);
		pattern = new ItemStack[size][size];

		setMaxStackSize(encoded ? 1 : 0x40);
	}

	private static NBTTagCompound getOrInitNBT(ItemStack stack) {
		NBTTagCompound comp = null;
		if (!stack.hasTagCompound()) {
			comp = new NBTTagCompound();
			stack.setTagCompound(comp);

			final ItemCraftingPattern item = (ItemCraftingPattern) stack.getItem();

			comp.setBoolean(COMP_HAS_PATTERN, false);
			comp.setInteger(COMP_SIZE_X, item.size.x);
			comp.setInteger(COMP_SIZE_Y, item.size.y);

			NBTTagList tagList = comp.getTagList(COMP_ITEMS, 10);

			comp.setTag(COMP_ITEMS, tagList);
		}

		else comp = stack.getTagCompound();

		return comp;
	}

	@Override
	public Vector2<Integer> getPatternSize() {
		return size;
	}

	@Override
	public boolean hasPattern(ItemStack stack) {
		if (stack.getItem() instanceof ItemCraftingPattern) {
			NBTTagCompound comp = getOrInitNBT(stack);

			return comp.getBoolean(COMP_HAS_PATTERN);
		}

		return false;
	}

	@Override
	public ItemStack[][] getPattern(ItemStack stack) {
		if (stack.getItem() instanceof ItemCraftingPattern) {
			final NBTTagCompound comp = getOrInitNBT(stack);
			final NBTTagList tagList = comp.getTagList(COMP_ITEMS, 10);

			final Vector2<Integer> vec = new Vector2<Integer>();
			vec.x = comp.getInteger(COMP_SIZE_X);
			vec.y = comp.getInteger(COMP_SIZE_Y);
			final int size = vec.x * vec.y;

			final ItemStack[][] pattern = new ItemStack[vec.y][vec.x];

			for (int i = 0; i < tagList.tagCount() - 1; i++) {
				NBTTagCompound temp = tagList.getCompoundTagAt(i);
				byte b0 = temp.getByte("Slot");

				if (b0 >= 0 && b0 < size)
					pattern[i / vec.y][i % vec.x] = ItemStack.loadItemStackFromNBT(temp);
			}

			return pattern;
		}

		return new ItemStack[0][0];
	}

	@Override
	public ItemStack getCraftingResult(ItemStack stack) {
		if (stack.getItem() instanceof ItemCraftingPattern) {
			final NBTTagCompound comp = getOrInitNBT(stack);

			if (comp.getBoolean(COMP_HAS_PATTERN)) {
				final NBTTagList tagList = comp.getTagList(COMP_ITEMS, 10);
				final NBTTagCompound stackComp = tagList.getCompoundTagAt(tagList.tagCount() - 1);
				return ItemStack.loadItemStackFromNBT(stackComp);
			}
		}

		return null;
	}

	@Override
	public boolean isPatternEqual(ItemStack stack, ItemStack[][] otherPattern) {
		if (!(stack.getItem() instanceof ItemCraftingPattern)) return false;
		if (otherPattern == null /*|| size.y != otherPattern.length*/ || otherPattern[0] == null /*|| size.x != otherPattern[0].length*/)
			return false;

		ItemStack[][] pattern = getPattern(stack);
		if (pattern.length != otherPattern.length || pattern[0].length != otherPattern[0].length) return false;

		for (int y = 0; y < pattern.length; y++) {
			for (int x = 0; x < pattern[y].length; x++) {
				ItemStack thisStack = pattern[y][x];
				ItemStack otherStack = otherPattern[y][x];

				if (!ItemStack.areItemStacksEqual(thisStack, otherStack)) return false;
			}
		}

		return true;
	}

	@Override
	public void setPattern(ItemStack stack, ItemStack[][] pattern, ItemStack resultStack) {
		if (!(stack.getItem() instanceof ItemCraftingPattern)) return;
		if (resultStack == null || pattern == null /*|| size.y != pattern.length*/ || pattern[0] == null /*|| size.x != pattern[0].length*/)
			return;

		final NBTTagCompound comp = getOrInitNBT(stack);

		comp.setBoolean(COMP_HAS_PATTERN, true);
		comp.setInteger(COMP_SIZE_X, pattern[0].length);
		comp.setInteger(COMP_SIZE_Y, pattern.length);

		final NBTTagList tagList = comp.getTagList(COMP_ITEMS, 10);

		for (int y = 0; y < pattern.length; y++) {
			for (int x = 0; x < pattern[y].length; x++) {
				if (pattern[y][x] != null) {
					NBTTagCompound temp = new NBTTagCompound();
					temp.setByte("Slot", (byte) (x + y * pattern.length));
					pattern[y][x].writeToNBT(temp);
					tagList.appendTag(temp);
				}
			}
		}

		comp.setTag(COMP_ITEMS, tagList);
	}

	@Override
	public void clearPattern(ItemStack stack) {
		if (!(stack.getItem() instanceof ItemCraftingPattern)) return;

		final NBTTagCompound comp = getOrInitNBT(stack);
		if (!comp.getBoolean(COMP_HAS_PATTERN)) return;

		comp.setBoolean(COMP_HAS_PATTERN, false);
		comp.setInteger(COMP_SIZE_X, 0);
		comp.setInteger(COMP_SIZE_Y, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		if (!encoded || result == null) return;

		list.add(TextFormatting.GREEN + "Result: " + result.getDisplayName());

		for (int y = 0; y < size.y; y++) {
			String[] strings = new String[size.y];
			for (int x = 0; x < size.x; x++) {
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

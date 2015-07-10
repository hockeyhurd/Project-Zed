/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.block;

import com.projectzed.mod.ProjectZed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

/**
 * Simple block class for creating stairs more easily.
 *
 * @author hockeyhurd
 * @version 7/9/2015.
 */
public class BlockStairsMaker extends BlockStairs {

	public BlockStairsMaker(Block block) {
		this(block, 0);
	}

	public BlockStairsMaker(Block block, int id) {
		super(block, id);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setBlockName(getName(block) + "Stairs");
	}

	private String getName(Block block) {
		String rawName = block.getUnlocalizedName().substring(0x4);
		String newName = "";
		boolean flag = false;
		for (char c : rawName.toCharArray()) {
			if (c != ' ' && c != '.') {
				// if (flag) c = String.valueOf(c).toUpperCase().charAt(0);
				newName += c;
				flag = false;
			}
			else flag = true;
		}

		return newName;
	}

}

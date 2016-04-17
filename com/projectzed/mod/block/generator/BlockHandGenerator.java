/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.block.generator;

import com.projectzed.api.block.AbstractBlockKineticGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.generator.AbstractTileEntityKineticGenerator;
import com.projectzed.mod.tileentity.generator.TileEntityHandGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Block class for handGen.
 *
 * @author hockeyhurd
 * @version 4/17/2016.
 */
public class BlockHandGenerator extends AbstractBlockKineticGenerator {

	/**
	 * @param material material of block
	 */
	public BlockHandGenerator(Material material) {
		super(material, "handGen");
	}

	@Override
	public AbstractTileEntityKineticGenerator getTileEntity() {
		return new TileEntityHandGenerator();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntityHandGenerator te = (TileEntityHandGenerator) world.getTileEntity(x, y, z);

			if (te != null) te.incrementHandCounter();

			return true;
		}

		player.swingItem();

		return false;
	}

}

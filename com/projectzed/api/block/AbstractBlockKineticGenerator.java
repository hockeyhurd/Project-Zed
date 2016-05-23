/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.api.block;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.generator.AbstractTileEntityKineticGenerator;
import com.projectzed.mod.tileentity.generator.TileEntityLavaGenerator;
import com.projectzed.mod.util.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * General abstract Block class for kinetic generators.
 *
 * @author hockeyhurd
 * @version 4/17/2016.
 */
public abstract class AbstractBlockKineticGenerator extends AbstractBlockGenerator {

	/**
	 * @param material material of block
	 * @param name String name
	 */
	public AbstractBlockKineticGenerator(Material material, String name) {
		super(material, name);
	}

	@Override
	public abstract AbstractTileEntityKineticGenerator getTileEntity();

	@Override
	protected void doBreakBlock(World world, BlockPos blockPos) {
		TileEntityLavaGenerator te = (TileEntityLavaGenerator) world.getTileEntity(blockPos);

		WorldUtils.dropItemsFromContainerOnBreak(te);

		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

}

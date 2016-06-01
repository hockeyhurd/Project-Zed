/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.tileentity.generator;

import com.projectzed.api.energy.source.EnumType;
import com.projectzed.api.energy.source.Source;
import com.projectzed.api.tileentity.generator.AbstractTileEntityFluidGenerator;
import com.projectzed.mod.block.generator.BlockLavaGenerator;

/**
 * TileEntity class for lavaGen.
 *
 * @author hockeyhurd
 * @version 6/30/2015.
 */
public class TileEntityLavaGenerator extends AbstractTileEntityFluidGenerator {

	public TileEntityLavaGenerator() {
		super("lavaGen");
	}

	@Override
	public void defineSource() {
		this.source = new Source(EnumType.LAVA);
	}

	@Override
	public void update() {
		super.update();

		if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0 /*&& blockType != null && blockType instanceof BlockLavaGenerator*/) {
			((BlockLavaGenerator) blockType).updateBlockState(canProducePower(), worldObj, pos);
		}
	}

}

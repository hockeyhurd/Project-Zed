/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.tileentity.generator;

import com.projectzed.api.energy.source.EnumType;
import com.projectzed.api.energy.source.Source;
import com.projectzed.api.tileentity.generator.AbstractTileEntityFluidGenerator;

/**
 * TileEntity class for petrolGen.
 *
 * @author hockeyhurd
 * @version 8/17/2015.
 */
public class TileEntityPetrolGenerator extends AbstractTileEntityFluidGenerator {

	public TileEntityPetrolGenerator() {
		super("petrolGen");
		this.consumationAmount = 200;
	}

	@Override
	public void defineSource() {
		this.source = new Source(EnumType.FUEL);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!worldObj.isRemote) {
			this.powerMode = this.burnTime > 0 && this.stored < this.maxStored && this.burnTime > 0;
		}

	}

}

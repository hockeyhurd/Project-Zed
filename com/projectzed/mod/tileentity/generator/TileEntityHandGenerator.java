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

/**
 * TileEntity class for handGen.
 *
 * @author hockeyhurd
 * @version 4/17/2016.
 */
public class TileEntityHandGenerator extends AbstractTileEntityKineticGenerator {

	private int handCounter = 0;

	public TileEntityHandGenerator() {
		super("handGen");
	}

	@Override
	public void defineSource() {
		this.source = new Source(EnumType.KINETIC);
	}

	public void addToHandCounter(int amount) {
		this.handCounter += amount;
	}

	public void incrementHandCounter() {
		this.handCounter++;
	}

	@Override
	public boolean canProducePower() {
		if (handCounter - 1 >= 0) {
			handCounter--;
			return true;
		}

		return false;
	}

}

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
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.block.generator.BlockPetrolGenerator;
import net.minecraft.nbt.NBTTagCompound;

/**
 * TileEntity class for petrolGen.
 *
 * @author hockeyhurd
 * @version 8/17/2015.
 */
public class TileEntityPetrolGenerator extends AbstractTileEntityFluidGenerator {

	private int level, lastLevel;

	public TileEntityPetrolGenerator() {
		super("petrolGen");
		this.consumationAmount = 200;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAndCheckLevel() {
		int currentAmount = internalTank.getFluidAmount();
		int capacity = internalTank.getCapacity();
		float percentage = (float) currentAmount / capacity;

		level = (int) Math.floor(percentage * 100.0f) / 8;
		if (lastLevel != level) {
			lastLevel = level;

			if (!worldObj.isRemote && blockType instanceof BlockPetrolGenerator) {
				ProjectZed.logHelper.info("level:", level);
				((BlockPetrolGenerator) blockType).updateBlockState(canProducePower(), worldObj, pos);
			}
		}

		return level;
	}

	@Override
	public void defineSource() {
		this.source = new Source(EnumType.FUEL);
	}

	@Override
	public void update() {
		super.update();

		if (!worldObj.isRemote) {
			this.powerMode = this.burnTime > 0 && this.stored < this.maxStored && this.burnTime > 0;

			if (worldObj.getTotalWorldTime() % 20L == 0) getAndCheckLevel();
		}

	}

	@Override
	public void readNBT(NBTTagCompound comp) {
		super.readNBT(comp);
		level = comp.getInteger("fluidLevel");
	}

	@Override
	public void saveNBT(NBTTagCompound comp) {
		super.saveNBT(comp);
		comp.setInteger("fluidLevel", level);
	}

}

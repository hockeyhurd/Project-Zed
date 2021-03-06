/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.mod.block.fluids;

import com.hockeyhurd.hcorelib.api.block.fluid.AbstractBlockFluid;
import com.projectzed.mod.ProjectZed;
import net.minecraftforge.fluids.Fluid;

/**
 * Block class for fluidPetrol.
 *
 * @author hockeyhurd
 * @version 8/8/2015.
 */
public class BlockFluidPetrol extends AbstractBlockFluid {

	public BlockFluidPetrol(String name, Fluid fluid) {
		super(name, ProjectZed.assetDir, fluid, ProjectZed.MATERIAL_PETROL);
	}

}

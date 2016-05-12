/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.registry;

import com.hockeyhurd.hcorelib.api.util.LogicHelper;
import com.hockeyhurd.hcorelib.api.util.NumberParser;
import com.projectzed.api.registry.IRegistrable;
import com.projectzed.api.util.FluidUtils;
import com.projectzed.mod.util.Coolant;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.HashMap;

/**
 * @author hockeyhurd
 * @version 11/12/2015.
 */
public class CoolantRegistry implements IRegistrable {

	private static final CoolantRegistry reg = new CoolantRegistry();

	private HashMap<Fluid, Float> coolantMap;

	private CoolantRegistry() {
		init();
	}

	/**
	 * Main init method for registry.
	 */
	private void init() {
		coolantMap = new HashMap<Fluid, Float>();

		coolantMap.put(FluidRegistry.WATER, 0.75f);
	}

	/**
	 * Gets the instance of registry.
	 *
	 * @return class instance.
	 */
	public static CoolantRegistry instance() {
		return reg;
	}

	@Override
	public IRegistrable getInstance() {
		return instance();
	}

	@Override
	public boolean addToRegistry(String key, String value) {
		if (key == null || key.length() == 0 || value == null || value.length() == 0) return false;

		final float val = NumberParser.parseFloat(value);
		if (val <= 0.0f) return false;

		final Fluid fluid = FluidUtils.getFluidByName(key);
		if (fluid == null) return false;

		coolantMap.put(fluid, val);

		return true;
	}

	@Override
	public boolean addToRegistry(String[] key, String[] value) {
		if (!LogicHelper.nullCheckString(key) || !LogicHelper.nullCheckString(value)) return false;
		if (key.length == 1 && value.length == 1) {

			final float val = NumberParser.parseFloat(value[0]);
			if (val <= 0.0f) return false;

			final Fluid fluid = FluidUtils.getFluidByName(key[0]);
			if (fluid == null) return false;

			coolantMap.put(fluid, val);

			return true;
		}

		return false;
	}

	/**
	 * Gets whether fluid is in coolant registry.
	 *
	 * @param fluid Fluid to reference.
	 * @return True if in registry, else may return false.
	 */
	public boolean isFluidInRegistry(Fluid fluid) {
		return fluid != null && coolantMap.containsKey(fluid);
	}

	/**
	 * Gets Coolant from internal coolant mapping.
	 *
	 * @param fluid Fluid to reference.
	 * @return Coolant object (NOTE: the amount of coolant will be set to auto-full by default!).
	 */
	public Coolant getCoolantByFluid(Fluid fluid) {
		if (fluid == null || !coolantMap.containsKey(fluid)) return Coolant.AIR;

		return new Coolant(fluid, coolantMap.get(fluid));
	}

	/**
	 * Checks if coolant is a valid coolant by referencing this internal mapping.
	 *
	 * @param coolant Coolant to reference.
	 * @return True if coolant is valid, else returns false.
	 */
	public boolean isCoolantValid(Coolant coolant) {
		return coolant != null && coolantMap.containsKey(coolant.getFluid()) && coolantMap.get(coolant.getFluid()) == coolant.getBaseEfficiency();
	}

}

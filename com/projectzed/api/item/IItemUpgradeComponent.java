/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.api.item;

import com.projectzed.api.tileentity.digger.AbstractTileEntityDigger;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;

/**
 * Interface for upgrade components that are primarily used in
 * altering machine behavior.
 * <br><bold>NOTE: </bold> This interface is intended to be implemented on the item
 * side of things.
 *
 * @author hockeyhurd
 * @version 6/29/2015.
 */
public interface IItemUpgradeComponent {

	/**
	 * @return max number of upgrades or stack size.
	 */
	int maxSize();

	/**
	 * Function to get the energy burn rate relative to number of this upgrade in use.
	 *
	 * @param stackSize amount of upgrades in use.
	 * @param originalRate original burn rate or equivalent to no upgrades in use.
	 * @return adjusted burning of energy rate.
	 */
	float energyBurnRateRelativeToSize(int stackSize, float originalRate);

	/**
	 * Function to get operation speed of machine relative to number of this upgrade in use.
	 *
	 * @param stackSize amount of upgrades in use.
	 * @param originalTickTime original number of ticks per operation or equivalent to no upgrades in use.
	 * @return operation speed or number of ticks.
	 */
	float operationSpeedRelativeToSize(int stackSize, float originalTickTime);

	/**
	 * Function to get effect on machines.
	 *
	 * @param te tileentity to reference.
	 * @param simulate set to true to simulate actions, else false to perform changes.
	 * @return true if successful/allowed to use said upgrade, else returns false.
	 */
	boolean effectOnMachines(AbstractTileEntityMachine te, boolean simulate);

	/**
	 * Function to get effect on generators.
	 *
	 * @param te tileentity to reference.
	 * @param simulate set to true to simulate actions, else false to perform changes.
	 * @return true if successful/allowed to use said upgrade, else returns false.
	 */
	boolean effectOnGenerators(AbstractTileEntityGenerator te, boolean simulate);

	/**
	 * Function to get effect on diggers.
	 *
	 * @param te tileentity to reference.
	 * @param simulate set to true to simulate actions, else false to perform changes.
	 * @return true if successful/allowed to use said upgrade, else returns false.
	 */
	boolean effectOnDiggers(AbstractTileEntityDigger te, boolean simulate);

}

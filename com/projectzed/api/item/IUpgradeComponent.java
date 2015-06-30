/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 *
 */

package com.projectzed.api.item;

/**
 * Interface for upgrade components that are primarily used in
 * altering machine behavior.
 *
 * @author hockeyhurd
 * @version 6/29/2015.
 */
public interface IUpgradeComponent {

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

}

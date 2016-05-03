/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.tileentity.interfaces;

import com.projectzed.api.util.SidedInfo;

/**
 * Interfacing for tile entities who have encodable ability.
 *
 * @author hockeyhurd
 * @version 5/3/2016.
 */
public interface IEncodable {

	/**
	 * Encoding callback function.
	 *
	 * @param simulate boolean flag whether to simulate or perform encoding.
	 * @return boolean result.
	 */
	boolean encode(boolean simulate);

	/**
	 * Attempts to send packet info to corresponding side.
	 *
	 * @param sidedInfo SidedInfo to reference.
	 */
	void sendMessage(SidedInfo sidedInfo);

}

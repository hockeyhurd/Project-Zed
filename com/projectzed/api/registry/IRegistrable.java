/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.api.registry;

/**
 * API interface hook to allow adding of other
 * mod('s) blocks, items, etc. to a given registry.
 *
 * @author hockeyhurd
 * @version 11/12/2015.
 */
public interface IRegistrable {

	/**
	 * Ensures we have access to class instance.
	 * <br><bold>USE WITH CAUTION!</bold>
	 *
	 * @return class instance.
	 */
	IRegistrable getInstance();

	/**
	 * Attempts to add a key and value to the given registry.
	 * <br>If registry needs multiple values, use other method!
	 *
	 * @param key key represented as a String.
	 * @param value value represented as a String.
	 * @return result of addition to registry.
	 */
	boolean addToRegistry(String key, String value);

	/**
	 * Attempts to break down key 'keys' and key 'values'
	 * to add relevant data.
	 * <br><bold>NOTE: </bold> Some classes may not use this
	 * and in turn will just return 'false'.
	 *
	 * <br><bold>P.S. </bold> Do <bold>NOT</bold> use this
	 * as a means to add multiple registers into one. This <bold>WILL FAIL!</bold>
	 *
	 * @param multiKey array of key data.
	 * @param multiValue array of value data.
	 * @return result of addition to registry.
	 */
	boolean addToRegistry(String[] multiKey, String[] multiValue);

}

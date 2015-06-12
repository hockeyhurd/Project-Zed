/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item.tools;

import com.projectzed.mod.registry.interfaces.IToolSetRegistry;
import com.projectzed.mod.registry.tools.ChainsawSetRegistry;

/**
 * ItemTool class for generic chainsaw.
 * 
 * @author hockeyhurd
 * @version Jun 11, 2015
 */
public class ItemChainsaw extends AbstractItemPowered {

	/**
	 * @param mat tool material of chainsaw.
	 * @param name name of chainsaw.
	 */
	public ItemChainsaw(ToolMaterial mat, String name) {
		super(mat, name, ChainsawSetRegistry.instance());
	}

	/**
	 * @param mat tool material of chainsaw.
	 * @param name name of chainsaw.
	 * @param capacity capacity of chainsaw.
	 * @param chargeRate charge rate of chainsaw. 
	 * @param set set of materials to reference.
	 */
	public ItemChainsaw(ToolMaterial mat, String name, int capacity, int chargeRate, IToolSetRegistry reg) {
		super(mat, name, capacity, chargeRate, reg);
	}

}

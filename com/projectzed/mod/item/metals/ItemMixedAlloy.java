/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item.metals;

import com.hockeyhurd.api.item.AbstractHCoreItem;
import com.projectzed.mod.ProjectZed;

/**
 * Class containing code for mixed metal alloy.
 * 
 * @author hockeyhurd
 * @version Jan 11, 2015
 */
public class ItemMixedAlloy extends AbstractHCoreItem {

	/**
	 * @param name
	 * @param assetDir
	 */
	public ItemMixedAlloy(String name, String assetDir) {
		super(ProjectZed.modCreativeTab, name, assetDir);
	}

}

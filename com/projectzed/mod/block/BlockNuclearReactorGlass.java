/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.tileentity.container.TileEntityReactorGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

/**
 * Class containing block code for nuclearReactorGlass.
 * 
 * @author hockeyhurd
 * @version Mar 6, 2015
 */
public class BlockNuclearReactorGlass extends AbstractBlockNuclearComponent {

	public BlockNuclearReactorGlass() {
		super(Material.GLASS, "nuclearReactorGlass");
		this.setResistance(2000.0f);
		this.setSoundType(SoundType.GLASS);
		this.setLightOpacity(0);
	}

	@Override
	public boolean hasSpecialRenderer() {
		// return true;
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState blockState) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState blockState) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState blockState) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public float getBlockHardness() {
		return 2.0f;
	}

	@Override
	public AbstractTileEntityNuclearComponent getTileEntity() {
		return new TileEntityReactorGlass();
	}

}

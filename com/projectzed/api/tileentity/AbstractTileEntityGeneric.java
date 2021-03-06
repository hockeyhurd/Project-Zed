/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.tileentity;

import com.hockeyhurd.hcorelib.api.tileentity.AbstractTileContainer;
import com.hockeyhurd.hcorelib.api.util.StringUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * Class used for easily creating a generic tile entity.
 * 
 * @author hockeyhurd
 * @version Oct 21, 2014
 */
public abstract class AbstractTileEntityGeneric extends AbstractTileContainer implements ITickable {

	public AbstractTileEntityGeneric() {
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos blockPos, IBlockState oldState, IBlockState newState) {
		// TODO: Figure this out!
		// return oldState.getBlock() == newState.getBlock();
		// return true;
		// return super.shouldRefresh(world, blockPos, oldState, newState);
		return oldState.getBlock() != newState.getBlock();
	}

	/**
	 * Main update method for a given entity. <br>
	 * NOTE: This should be overriden 99% of the time.
	 */
	@Override
	public void update() {
		if (!canUpdate()) return;
	}

	@Override
	public String getName() {
		return customName;
	}

	@Override
	public boolean hasCustomName() {
		return StringUtils.nullCheckString(customName);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation(customName);
	}

	protected boolean canUpdate() {
		return true;
	}

}

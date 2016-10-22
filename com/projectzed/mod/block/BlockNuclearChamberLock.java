/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.block.IMetaUpdate;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.tileentity.container.TileEntityNuclearChamberLock;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Class containing block code for nuclearChamberLock.
 * 
 * @author hockeyhurd
 * @version Dec 14, 2014
 */
public class BlockNuclearChamberLock extends AbstractBlockNuclearComponent implements IMetaUpdate {



	public BlockNuclearChamberLock() {
		super("nuclearChamberLock");
	}
	
	@Override
	public AbstractTileEntityNuclearComponent getTileEntity() {
		return new TileEntityNuclearChamberLock();
	}

	@Override
	public void updateMeta(boolean isConnected, World world, Vector3<Integer> vec) {
		updateMeta(isConnected ? 1 : 0, world, vec);
	}
	
	@Override
	public void updateMeta(int meta, World world, Vector3<Integer> vec) {
		final BlockPos blockPos = VectorHelper.toBlockPos(vec);
		final TileEntity te = world.getTileEntity(blockPos);
		
		if (te != null && te instanceof TileEntityNuclearChamberLock) {
			// world.setBlockMetadataWithNotify(vec.x, vec.y, vec.z, meta, 2);
			// world.markBlockForUpdate(vec.x, vec.y, vec.z);
			final Block block = ((TileEntityNuclearChamberLock) te).getBlock();
			BlockUtils.setBlock(world, blockPos, block.getStateFromMeta(meta));
			world.notifyBlockOfStateChange(blockPos, block);
			world.notifyNeighborsOfStateChange(blockPos, block);
		}
	}
	 
}

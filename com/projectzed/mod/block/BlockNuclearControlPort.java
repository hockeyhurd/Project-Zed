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
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.block.IMetaUpdate;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.tileentity.TileEntityNuclearControlPort;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Class containing block code for nuclearControlPort.
 * 
 * @author hockeyhurd
 * @version Mar 12, 2015
 */
public class BlockNuclearControlPort extends AbstractBlockNuclearComponent implements IMetaUpdate {

	private boolean active;

	public BlockNuclearControlPort() {
		super(Material.rock, "nuclearControlPort");
	}

	@Override
	public Block getBlock() {
		return this;
	}

	@Override
	public float getBlockHardness() {
		return 1.0f;
	}

	@Override
	public EnumHarvestLevel getHarvestLevel() {
		return EnumHarvestLevel.PICKAXE_STONE;
	}

	@Override
	public AbstractTileEntityNuclearComponent getTileEntity() {
		return new TileEntityNuclearControlPort();
	}
	
	@Override
	public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState state, Block neighborBlock) {
		if (!world.isRemote) {
			TileEntityNuclearControlPort te = (TileEntityNuclearControlPort) world.getTileEntity(blockPos);
			
			if (te != null) {
				boolean active = world.isBlockIndirectlyGettingPowered(blockPos) > 0;
				te.setRedstoneSignal(active);
				updateMeta(active ? 1 : 0, world, te.worldVec());
				if (active) this.setLightLevel(0.75f);
				else this.setLightLevel(0.0f);
			}
		}
	}

	@Override
	public void updateMeta(boolean isActive, World world, Vector3<Integer> vec) {
		// updateMeta(isActive ? 1 : 0, world, vec);
	}

	@Override
	public void updateMeta(int meta, World world, Vector3<Integer> vec) {
		final BlockPos blockPos = VectorHelper.toBlockPos(vec);
		final TileEntityNuclearControlPort te = (TileEntityNuclearControlPort) world.getTileEntity(blockPos);
		
		if (te != null) {
			// world.setBlockMetadataWithNotify(vec.x, vec.y, vec.z, meta, 2);
			// world.markBlockForUpdate(vec.x, vec.y, vec.z);
			final IBlockState blockState = BlockUtils.getBlock(world, blockPos).getBlock().getStateFromMeta(meta);
			BlockUtils.setBlock(world, blockPos, blockState);
			world.notifyBlockOfStateChange(blockPos, blockState.getBlock());
			world.notifyNeighborsOfStateChange(blockPos, blockState.getBlock());
		}
	}

}

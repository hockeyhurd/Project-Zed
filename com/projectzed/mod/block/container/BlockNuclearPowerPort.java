/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.block.IMetaUpdate;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityNuclearPowerPort;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Class containing code for nuclearPowerPort.
 * 
 * @author hockeyhurd
 * @version Mar 11, 2015
 */
public class BlockNuclearPowerPort extends AbstractBlockContainer implements IMetaUpdate {

	public BlockNuclearPowerPort() {
		super(Material.ROCK, ProjectZed.assetDir, "nuclearPowerPort");
	}
	

	@Override
	public void updateMeta(boolean isActive, World world, Vector3<Integer> vec) {
		updateMeta(isActive ? 1 : 0, world, vec);
	}
	
	@Override
	public void updateMeta(int meta, World world, Vector3<Integer> vec) {
		final BlockPos blockPos = VectorHelper.toBlockPos(vec);
		final TileEntityNuclearPowerPort te = (TileEntityNuclearPowerPort) world.getTileEntity(blockPos);

		if (te != null) {
			// world.setBlockMetadataWithNotify(vec.x, vec.y, vec.z, meta, 2);
			// world.markBlockForUpdate(vec.x, vec.y, vec.z);
			IBlockState blockState = te.getBlock().getStateFromMeta(meta);
			BlockUtils.setBlock(world, blockPos, blockState);
			BlockUtils.updateAndNotifyNeighborsOfBlockUpdate(world, blockPos);
		}
	}

	@Override
	public AbstractTileEntityEnergyContainer getTileEntity() {
		return new TileEntityNuclearPowerPort();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	protected void doBreakBlock(World world, BlockPos blockPos) {
		TileEntityNuclearPowerPort te = (TileEntityNuclearPowerPort) world.getTileEntity(blockPos);
		if (te != null && te.hasMaster()) ProjectZed.logHelper.info("Broke TE @:", te.worldVec().toString(), "with stored power of:", te.getEnergyStored());
	}

}

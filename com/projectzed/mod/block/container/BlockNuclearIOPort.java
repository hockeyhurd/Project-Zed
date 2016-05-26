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
import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.block.IMetaUpdate;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.container.TileEntityNuclearIOPort;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * Class containing block code for nuclearIOPort.
 * 
 * @author hockeyhurd
 * @version Mar 19, 2015
 */
public class BlockNuclearIOPort extends AbstractBlockNuclearComponent implements IMetaUpdate {

	public BlockNuclearIOPort() {
		super("nuclearIOPort");
	}
	

	@Override
	public void updateMeta(boolean isActive, World world, Vector3<Integer> vec) {
		final BlockPos blockPos = VectorHelper.toBlockPos(vec);
		final TileEntity te = world.getTileEntity(blockPos);
		
		if (te != null && te instanceof TileEntityNuclearIOPort) {
			byte meta = ((TileEntityNuclearIOPort) te).getStoredMeta();
			
			if (meta == 0) meta = 1;
			
			// world.setBlockMetadataWithNotify(vec.x, vec.y, vec.z, meta, 2);
			// world.markBlockForUpdate(vec.x, vec.y, vec.z);
			final IBlockState blockState = ((TileEntityNuclearIOPort) te).getBlock().getStateFromMeta(te.getBlockMetadata());
			BlockUtils.setBlock(world, blockPos, blockState);
			BlockUtils.updateAndNotifyNeighborsOfBlockUpdate(world, blockPos);

			((TileEntityNuclearIOPort) te).setMetaOnUpdate(meta);
		}
	}

	@Override
	public void updateMeta(int meta, World world, Vector3<Integer> vec) {
		final BlockPos blockPos = VectorHelper.toBlockPos(vec);
		TileEntity te = world.getTileEntity(blockPos);
		
		if (te != null && te instanceof TileEntityNuclearIOPort) {
			
			// world.setBlockMetadataWithNotify(vec.x, vec.y, vec.z, meta, 2);
			// world.markBlockForUpdate(vec.x, vec.y, vec.z);
			final IBlockState blockState = ((TileEntityNuclearIOPort) te).getBlock().getStateFromMeta(te.getBlockMetadata());
			BlockUtils.setBlock(world, blockPos, blockState);
			BlockUtils.updateAndNotifyNeighborsOfBlockUpdate(world, blockPos);
			
			((TileEntityNuclearIOPort) te).setMetaOnUpdate((byte) meta);
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase e, ItemStack stack) {
		if (stack.hasTagCompound()) {
			NBTTagCompound comp = stack.getTagCompound();
			byte meta = comp.getByte("Meta");
			
			TileEntityNuclearIOPort te = (TileEntityNuclearIOPort) world.getTileEntity(blockPos);
			
			if (meta == 0) updateMeta(1, world, VectorHelper.toVector3i(blockPos));
			
			else updateMeta(meta, world, VectorHelper.toVector3i(blockPos));
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityNuclearIOPort te = (TileEntityNuclearIOPort) world.getTileEntity(blockPos);
			if (te != null) {
				if (player.getActiveItemStack() == null || !(player.getActiveItemStack().getItem() instanceof ItemWrench)) FMLNetworkHandler
						.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityNuclearIOPort.class),
								world, blockPos.getX(), blockPos.getY(), blockPos.getZ());

				else return false;
			}
			
			return true;
		}
	}
	
	@Override
	public AbstractTileEntityNuclearComponent getTileEntity() {
		return new TileEntityNuclearIOPort();
	}

}

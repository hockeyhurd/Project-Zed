/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.item.tools;

import com.hockeyhurd.hcorelib.api.item.AbstractHCoreItem;
import com.hockeyhurd.hcorelib.api.math.Vector3;
import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.tileentity.IWrenchable;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Class containing code for the main wrenching tool.
 * 
 * @author hockeyhurd
 * @version Feb 3, 2015
 */
public class ItemWrench extends AbstractHCoreItem {

	/**
	 * @param name name of wrench.
	 */
	public ItemWrench(String name) {
		super(ProjectZed.modCreativeTab, ProjectZed.assetDir, name);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float clickX, float clickY, float clickZ) {
		EnumActionResult used = EnumActionResult.FAIL;
		
		if (!world.isRemote) {

			final Vector3<Integer> vecClick = VectorHelper.toVector3i(pos);
			IBlockState blockState = BlockUtils.getBlock(world, vecClick.x, vecClick.y, vecClick.z);
			Block b = blockState.getBlock();
			final TileEntity te = world.getTileEntity(pos);

			if (b != null && b != Blocks.air && te != null && te instanceof IWrenchable) {
				IWrenchable wrenchableTE = (IWrenchable) te;

				// Waila waila = new Waila(stack, world, player, null, 0);
				// waila.finder(false);

				// EnumFacing facingDir = waila.getSideHit();
				EnumFacing facingDir = player.getHorizontalFacing();

				if (wrenchableTE.canRotateTE() && !player.isSneaking()) {
					used = EnumActionResult.PASS;
					final int meta = wrenchableTE.getRotatedState(facingDir, blockState).getHorizontalIndex();
					// IBlockState newState = blockState.getBlock().getStateFromMeta(meta);
					// blockState = blockState.getBlock().getStateFromMeta(meta);
					IBlockState newState = blockState.getActualState(world, pos);
					ProjectZed.logHelper.info("Here!");

					// BlockUtils.setBlock(world, pos, blockState);
					BlockUtils.setBlock(world, pos, newState);
					BlockUtils.updateAndNotifyNeighborsOfBlockUpdate(world, pos);
					te.markDirty();
				}

				else if (player.isSneaking() && wrenchableTE.canSaveDataOnPickup()) {
					used = EnumActionResult.PASS;
					
					ItemStack itemToDrop = new ItemStack(b, 1);

					NBTTagCompound comp;
					if (itemToDrop.hasTagCompound()) comp = itemToDrop.getTagCompound();
					else {
						comp = new NBTTagCompound();
						itemToDrop.setTagCompound(comp);
					}

					wrenchableTE.saveNBT(comp);

					BlockUtils.setBlockToAir(world, vecClick);
					WorldUtils.addItemDrop(itemToDrop, world, vecClick.x, vecClick.y, vecClick.z);
				}
				
				wrenchableTE.onInteract(stack, player, world, vecClick);
				
			}
		}
		
		// player.swingItem();
		player.swingArm(hand);
		return used;
	}

}

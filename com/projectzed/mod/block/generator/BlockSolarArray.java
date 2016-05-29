/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.generator;

import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.projectzed.api.block.AbstractBlockGenerator;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * Class containing solar block array.
 * 
 * @author hockeyhurd
 * @version Oct 20, 2014
 */
public class BlockSolarArray extends AbstractBlockGenerator {

	private final byte TIER;
	
	/**
	 * @param material material of block
	 */
	public BlockSolarArray(Material material, byte tier) {
		super(material, "solarArray" + (tier > 0 ? tier : ""));
		this.TIER = tier;
	}

	@Override
	public AbstractTileEntityGenerator getTileEntity() {
		TileEntitySolarArray te = new TileEntitySolarArray();
		/*if (this.TIER > 0) */te.setTier(this.TIER);
		return te;
	}

	@Override
	public void updateBlockState(boolean active, World world, BlockPos blockPos) {
		final TileEntity tileentity = world.getTileEntity(blockPos);
		final IBlockState metaState = tileentity.getBlockType().getStateFromMeta(tileentity.getBlockMetadata());

		BlockUtils.updateAndNotifyNeighborsOfBlockUpdate(world, blockPos);

		if (tileentity != null) {
			tileentity.validate();
			world.setTileEntity(blockPos, tileentity);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase player, ItemStack stack) {
		/*int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if (l == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		if (l == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if (l == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);*/

		if (stack.hasDisplayName()) ((TileEntitySolarArray) world.getTileEntity(blockPos)).setCustomName(stack.getDisplayName());
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntitySolarArray te = (TileEntitySolarArray) world.getTileEntity(blockPos);
			if (te != null) FMLNetworkHandler
					.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntitySolarArray.class),
							world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
			return true;
		}
	}

	@Override
	protected void doBreakBlock(World world, BlockPos blockPos) {
		TileEntitySolarArray te = (TileEntitySolarArray) world.getTileEntity(blockPos);
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

	/**
	 * Method used to detect if a block is above this block (blocking our sunlight).
	 *
	 * @param world world object.
	 * @param blockPos Block position.
	 */
	public boolean canSeeAbove(World world, BlockPos blockPos) {
		boolean clear = true;
		
		if (!world.isRemote) {
			for (int yy = blockPos.getY() + 1; yy < 255; yy++) {
				// Block b = world.getBlock(x, yy, z);
				Block b = BlockUtils.getBlock(world, blockPos.getX(), yy, blockPos.getZ()).getBlock();
				if (b != null && !(b instanceof BlockAir)) {
					clear = false;
					break;
				}
			}
		}
		
		return clear;
	}

}

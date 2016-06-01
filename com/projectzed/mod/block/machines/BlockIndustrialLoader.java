/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.machines;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlockContainer;
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * Class containing block code for industrialLoader.
 * 
 * @author hockeyhurd
 * @version Apr 19, 2015
 */
public class BlockIndustrialLoader extends AbstractHCoreBlockContainer {

	public BlockIndustrialLoader() {
		super(Material.rock, ProjectZed.modCreativeTab, ProjectZed.assetDir, "industrialLoader");
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState blockState, EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityIndustrialLoader te = (TileEntityIndustrialLoader) world.getTileEntity(pos);
			if (te != null) {
				if (player.getActiveItemStack() == null || !(player.getActiveItemStack().getItem() instanceof ItemWrench))
					FMLNetworkHandler
							.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityIndustrialLoader.class),
									world, pos.getX(), pos.getY(), pos.getZ());

				else return false;
			}
			return true;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase placer, ItemStack stack) {
		final TileEntityIndustrialLoader te = (TileEntityIndustrialLoader) world.getTileEntity(blockPos);
		final boolean isServerSide = !world.isRemote;

		if (isServerSide && te != null) {
			if (stack.hasTagCompound()) {
				te.readNBT(stack.getTagCompound());
				te.markDirty();
			}

			if (stack.hasDisplayName()) te.setCustomName(stack.getDisplayName());
		}
	}

	@Override
	public Block getBlock() {
		return this;
	}

	@Override
	public float getBlockHardness() {
		return 2.0f;
	}

	@Override
	public EnumHarvestLevel getHarvestLevel() {
		return EnumHarvestLevel.PICKAXE_STONE;
	}

	@Override
	public TileEntityIndustrialLoader getTileEntity() {
		return new TileEntityIndustrialLoader();
	}

	@Override
	public TileEntityIndustrialLoader createNewTileEntity(World world, int id) {
		return getTileEntity();
	}

}

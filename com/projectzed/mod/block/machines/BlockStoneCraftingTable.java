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
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.machine.TileEntityStoneCraftingTable;
import com.projectzed.mod.util.WorldUtils;
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
 * Block class for stone crafting table.
 * 
 * @author hockeyhurd
 * @version Mar 31, 2015
 */
public class BlockStoneCraftingTable extends AbstractHCoreBlockContainer {

	/**
	 * @param material
	 */
	public BlockStoneCraftingTable(Material material) {
		super(material, ProjectZed.modCreativeTab, ProjectZed.assetDir, "stoneCraftingTable");
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase player, ItemStack stack) {
		/*int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if (l == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		if (l == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if (l == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);*/

		final EnumFacing dir = player.getHorizontalFacing();
		final TileEntityStoneCraftingTable tileEntity = (TileEntityStoneCraftingTable) world.getTileEntity(blockPos);
		tileEntity.setFrontFacing(dir);

		if (stack.hasDisplayName()) tileEntity.setCustomName(stack.getDisplayName());
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityStoneCraftingTable te = (TileEntityStoneCraftingTable) world.getTileEntity(blockPos);
			if (te != null) FMLNetworkHandler
					.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityStoneCraftingTable.class),
							world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
			return true;
		}
	}

	@Override
	public void breakBlock(World world, BlockPos blockPos, IBlockState oldBlock) {
		TileEntityStoneCraftingTable te = (TileEntityStoneCraftingTable) world.getTileEntity(blockPos);
		
		if (te != null) WorldUtils.dropItemsFromContainerOnBreak(te);
		
		super.breakBlock(world, blockPos, oldBlock);
	}

	@Override
	public TileEntityStoneCraftingTable getTileEntity() {
		return new TileEntityStoneCraftingTable();
	}

	@Override
	public BlockStoneCraftingTable getBlock() {
		return this;
	}

	@Override
	public float getBlockHardness() {
		return 1.0f;
	}

	@Override
	public EnumHarvestLevel getHarvestLevel() {
		return EnumHarvestLevel.PICKAXE_WOOD;
	}

}

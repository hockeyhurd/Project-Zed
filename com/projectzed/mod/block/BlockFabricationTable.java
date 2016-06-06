/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlockContainer;
import com.hockeyhurd.hcorelib.api.tileentity.AbstractTile;
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.TileEntityFabricationTable;
import com.projectzed.mod.util.WorldUtils;
import net.minecraft.block.Block;
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

import java.util.Random;

/**
 * Class containing code for fabrication table and its properties.
 * 
 * @author hockeyhurd
 * @version Nov 22, 2014
 */
public class BlockFabricationTable extends AbstractHCoreBlockContainer {

	private static Random random = new Random();

	public BlockFabricationTable(Material material) {
		super(material, ProjectZed.modCreativeTab, ProjectZed.assetDir, "fabricationTable");
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
		return EnumHarvestLevel.PICKAXE_WOOD;
	}

	@Override
	public AbstractTile getTileEntity() {
		return new TileEntityFabricationTable();
	}


	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityFabricationTable te = (TileEntityFabricationTable) world.getTileEntity(blockPos);
			if (te != null) FMLNetworkHandler
					.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityFabricationTable.class),
							world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
			return true;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase placer, ItemStack stack) {
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState oldBlock) {
		final TileEntity tileEntity = world.getTileEntity(pos);

		if (!world.isRemote && tileEntity != null) {
			TileEntityFabricationTable te = (TileEntityFabricationTable) tileEntity;
			
			ItemStack[] stacks = new ItemStack[te.getSizeInventory()];
			for (int i = 0; i < te.getSizeInventory(); i++) {
				if (te.getStackInSlot(i) != null) stacks[i] = te.getStackInSlot(i);
			}
			
			WorldUtils.addItemDrop(stacks, world, pos.getX(), pos.getY(), pos.getZ(), random);
		}
		super.breakBlock(world, pos, oldBlock);
	}

}

/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.generator;

import com.projectzed.api.block.AbstractBlockGenerator;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.generator.TileEntityFurnaceGenerator;
import com.projectzed.mod.util.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * Block class for furnace generator.
 *
 * @author hockeyhurd
 * @version Nov 18, 2014
 */
public class BlockFurnaceGenerator extends AbstractBlockGenerator {
	
	public BlockFurnaceGenerator(Material material) {
		super(material, "furnaceGen");
	}

	@Override
	public AbstractTileEntityGenerator getTileEntity() {
		return new TileEntityFurnaceGenerator();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityFurnaceGenerator te = (TileEntityFurnaceGenerator) world.getTileEntity(blockPos);
			if (te != null) FMLNetworkHandler
					.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityFurnaceGenerator.class),
							world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
			return true;
		}
	}

	@Override
	protected void doBreakBlock(World world, BlockPos blockPos) {
		TileEntityFurnaceGenerator te = (TileEntityFurnaceGenerator) world.getTileEntity(blockPos);
		
		WorldUtils.dropItemsFromContainerOnBreak(te);
		
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

}

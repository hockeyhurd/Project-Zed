/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container.digger;

import com.hockeyhurd.hcorelib.api.util.ChatUtils;
import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.digger.TileEntityIndustrialQuarry;
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
 * Class for industrialQuarry.
 * 
 * @author hockeyhurd
 * @version Jun 18, 2015
 */
public class BlockIndustrialQuarry extends AbstractBlockContainer {

	/**
	 * @param material
	 */
	public BlockIndustrialQuarry(Material material) {
		super(material, ProjectZed.assetDir, "industrialQuarry");
	}

	@Override
	public AbstractTileEntityEnergyContainer getTileEntity() {
		return new TileEntityIndustrialQuarry();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;
		
		else {
			TileEntityIndustrialQuarry te = (TileEntityIndustrialQuarry) world.getTileEntity(blockPos);
			if (te != null) {
				FMLNetworkHandler
						.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityIndustrialQuarry.class),
								world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
				player.addChatComponentMessage(ChatUtils.createComponent(false, "is done: " + te.isDone()));
			}

			return true;
		}
	}
	
	/*@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase e, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, e, stack);
		
		if (!world.isRemote) {
		}
	}*/

	@Override
	protected void doBreakBlock(World world, BlockPos blockPos) {
		TileEntityIndustrialQuarry te = (TileEntityIndustrialQuarry) world.getTileEntity(blockPos);
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

}

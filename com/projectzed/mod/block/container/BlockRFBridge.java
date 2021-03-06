/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.container.TileEntityRFBridge;
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
 * Class containing block code for RF bridge.
 * 
 * @author hockeyhurd
 * @version Nov 29, 2014
 */
public class BlockRFBridge extends AbstractBlockContainer {

	private boolean flip;

	public BlockRFBridge(Material material, boolean flip) {
		super(material, ProjectZed.assetDir, !flip ? "bridgeMcUToRF" : "bridgeRFToMcU");
		this.flip = flip;
	}

	@Override
	public AbstractTileEntityEnergyContainer getTileEntity() {
		TileEntityRFBridge te = new TileEntityRFBridge();
		te.setFlip(this.flip);
		return te;
	}

	@Override
	protected void doBreakBlock(World world, BlockPos blockPos) {
		TileEntityRFBridge te = (TileEntityRFBridge) world.getTileEntity(blockPos);
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityRFBridge te = (TileEntityRFBridge) world.getTileEntity(blockPos);
			if (te != null) {
				if (player.getActiveItemStack() == null || !(player.getActiveItemStack().getItem() instanceof ItemWrench))
					FMLNetworkHandler.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityRFBridge.class), world,
							blockPos.getX(), blockPos.getY(), blockPos.getZ());

				else return false;
			}

			return true;
		}
	}

}

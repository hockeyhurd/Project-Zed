/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container.digger;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.digger.TileEntityIndustrialQuarry;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;

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
		this.setBlockName("industrialQuarry");
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#getTileEntity()
	 */
	@Override
	public AbstractTileEntityEnergyContainer getTileEntity() {
		return new TileEntityIndustrialQuarry();
	}

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;
		
		else {
			TileEntityIndustrialQuarry te = (TileEntityIndustrialQuarry) world.getTileEntity(x, y, z);
			if (te != null) FMLNetworkHandler.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityIndustrialQuarry.class), world, x, y, z);
			return true;
		}
	}
	
	/*@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase e, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, e, stack);
		
		if (!world.isRemote) {
		}
	}*/

	/* (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockContainer#doBreakBlock(net.minecraft.world.World, int, int, int)
	 */
	@Override
	protected void doBreakBlock(World world, int x, int y, int z) {
		TileEntityIndustrialQuarry te = (TileEntityIndustrialQuarry) world.getTileEntity(x, y, z);
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

}

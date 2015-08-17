/*
 * This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3
 *  of the License, or (at your option) any later version. Project-Zed is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 *  have received a copy of the GNU General Public License along with Project-Zed. If not, see <http://www.gnu
 *  .org/licenses/>
 */

package com.projectzed.mod.block.generator;

import com.projectzed.api.block.AbstractBlockGenerator;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.generator.TileEntityPetrolGenerator;
import com.projectzed.mod.util.WorldUtils;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Block class for petrolGen.
 *
 * @author hockeyhurd
 * @version 8/17/2015.
 */
public class BlockPetrolGenerator extends AbstractBlockGenerator {

	@SideOnly(Side.CLIENT)
	private IIcon frontOn;

	/**
	 * @param material = material of block
	 */
	public BlockPetrolGenerator(Material material) {
		super(material, "petrolGen");
		this.setBlockName("petrolGen");
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(1.0f);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "generic_side");
		this.top = this.base = reg.registerIcon(ProjectZed.assetDir + "generic_base");
		this.front = reg.registerIcon(ProjectZed.assetDir + this.name + "_front");
		this.frontOn = reg.registerIcon(ProjectZed.assetDir + this.name + "_front_on");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TileEntityPetrolGenerator te = (TileEntityPetrolGenerator) world.getTileEntity(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);

		if (side == 3 && meta == 0) return this.front;
		return side == 0 || side == 1 ? this.base : (side != meta ? this.blockIcon : (te.canProducePower() ? this.frontOn : this.front));
	}

	@Override
	public AbstractTileEntityGenerator getTileEntity() {
		return new TileEntityPetrolGenerator();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityPetrolGenerator te = (TileEntityPetrolGenerator) world.getTileEntity(x, y, z);
			if (te != null) FMLNetworkHandler
					.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityPetrolGenerator.class), world, x, y, z);
			return true;
		}
	}

	@Override
	protected void doBreakBlock(World world, int x, int y, int z) {
		TileEntityPetrolGenerator te = (TileEntityPetrolGenerator) world.getTileEntity(x, y, z);

		WorldUtils.dropItemsFromContainerOnBreak(te);

		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}
}

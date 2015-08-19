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

import java.util.Random;

/**
 * Block class for petrolGen.
 *
 * @author hockeyhurd
 * @version 8/17/2015.
 */
public class BlockPetrolGenerator extends AbstractBlockGenerator {

	/*@SideOnly(Side.CLIENT)
	private IIcon frontOn;*/

	@SideOnly(Side.CLIENT)
	private IIcon[] frontIcons = new IIcon[9];

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
		// this.front = reg.registerIcon(ProjectZed.assetDir + this.name + "_front");
		// this.frontOn = reg.registerIcon(ProjectZed.assetDir + this.name + "_front_on");

		for (int i = 0; i < frontIcons.length; i++) {
			frontIcons[i] = reg.registerIcon(ProjectZed.assetDir + this.name + '_' + i);
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side == 0 || side == 1 ? this.base : (side != meta && side != 3 ? this.blockIcon : frontIcons[0]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TileEntityPetrolGenerator te = (TileEntityPetrolGenerator) world.getTileEntity(x, y, z);
		int progress = (int) ((te.getTank().getFluidAmount() / (float) te.getTank().getCapacity()) * (te.getTank().getCapacity() / 2f / 1000f));

		// ensure nothing weird can happen, i.e. array index out of bound exceptions.
		if (progress < 0) progress = 0;
		else if (progress >= frontIcons.length) progress = frontIcons.length - 1;

		int meta = world.getBlockMetadata(x, y, z);

		return side == 0 || side == 1 ? this.base : (side != meta ? this.blockIcon : (this.frontIcons[progress]));
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
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (((TileEntityPetrolGenerator) world.getTileEntity(x, y, z)).isPowered()) {
			int metaData = world.getBlockMetadata(x, y, z);
			float f = (float) x + 0.5F;
			float f1 = (float) y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
			float f2 = (float) z + 0.5F;
			float f3 = 0.52F;
			float f4 = random.nextFloat() * 0.6F - 0.3F;

			if (metaData == 4) {
				world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
			}
			else if (metaData == 5) {
				world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
			}
			else if (metaData == 2) {
				world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
			}
			else if (metaData == 3) {
				world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected void doBreakBlock(World world, int x, int y, int z) {
		TileEntityPetrolGenerator te = (TileEntityPetrolGenerator) world.getTileEntity(x, y, z);

		WorldUtils.dropItemsFromContainerOnBreak(te);

		ProjectZed.logHelper.info("Stored McU:", te.getEnergyStored());
		ProjectZed.logHelper.info("Stored Petrol (mb):", te.getTank().getFluidAmount());
	}
}

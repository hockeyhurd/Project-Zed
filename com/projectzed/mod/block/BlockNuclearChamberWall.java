/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
 * with Project-Zed. If not, see <http://www.gnu.org/licenses/>
 */
package com.projectzed.mod.block;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.hockeyhurd.api.util.BlockHelper;
import com.projectzed.api.block.AbstractBlockNuclearComponent;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityNuclearChamberWall;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for nuclearChamberWall.
 * 
 * @author hockeyhurd
 * @version Dec 12, 2014
 */
public class BlockNuclearChamberWall extends AbstractBlockNuclearComponent {

	private IIcon vert, horiz, both;

	public BlockNuclearChamberWall() {
		super("nuclearChamberWall");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.minecraft.block.Block#registerBlockIcons(net.minecraft.client.renderer
	 * .texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = vert = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall_vert");
		horiz = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall_horiz");
		both = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall_both");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side == 0 && meta == 0) return vert;
		return meta == 0 || meta == 1 ? vert : (meta == 2 ? horiz : both);
	}

	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockPlacedBy(net.minecraft.world.World,
	 * int, int, int, net.minecraft.entity.EntityLivingBase,
	 * net.minecraft.item.ItemStack)
	 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		BlockHelper bh = new BlockHelper(world, (EntityPlayer) player);

		int[] ret = isBlockAdjacent(bh, x, y, z);
		int interpret = ret[1] >= 4 ? 3 : ret[0] == 1 ? 1 : 2;
		ProjectZed.logHelper.info(ret[0] >= 3);
		
		world.setBlockMetadataWithNotify(x, y, z, interpret, 2);
		// if (!yCheck) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		// else world.setBlockMetadataWithNotify(x, y, z, 1, 2);
	}

	private int[] isBlockAdjacent(BlockHelper bh, int x, int y, int z) {
		int ret = 0;
		int counter = 0;
		if (bh.blockExists(x, y - 1, z) || bh.blockExists(x, y + 1, z)) {
			if ((bh.getBlock(x, y - 1, z) instanceof BlockNuclearChamberWall || (bh.getBlock(x, y - 1, z) instanceof BlockNuclearChamberLock))
					&& (bh.getBlock(x, y + 1, z) instanceof BlockNuclearChamberWall || (bh.getBlock(x, y + 1, z) instanceof BlockNuclearChamberLock)))
				ret = 1;
		}

		for (int xx = -1; xx <= 1; xx++) {
			for (int zz = -1; zz <= 1; zz++) {
				if (xx == 0 && zz == 0) continue;

				if (bh.blockExists(x + xx, y, z + zz)) {
					if (bh.getBlock(x + xx, y, z + zz) instanceof BlockNuclearChamberWall
							|| bh.getBlock(x + xx, y, z + zz) instanceof BlockNuclearChamberLock) {
						if (ret == 1) {
							ret = 3;
							break;
						}
						else ret = 2;
					}
				}
			}

			if (ret >= 3) break;
		}
		
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			if (bh.blockExists(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)) {
				if (bh.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof BlockNuclearChamberWall
						|| bh.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof BlockNuclearChamberLock) {
					counter++;
				}
			}
		}

		return new int[] { ret, counter };
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.projectzed.api.block.AbstractBlockNuclearComponent#getTileEntity()
	 */
	@Override
	public AbstractTileEntityNuclearComponent getTileEntity() {
		return new TileEntityNuclearChamberWall();
	}

}

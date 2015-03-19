/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import net.minecraft.block.BlockTNT;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.entity.EntityAtomicBomb;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing block code for atomicBomb.
 * 
 * @author hockeyhurd
 * @version Mar 18, 2015
 */
public class BlockAtomicBomb extends BlockTNT {

	public BlockAtomicBomb() {
		this.setBlockName("atomicBomb");
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setBlockUnbreakable();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "atomicBomb");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		return blockIcon;
	}
	
	/**
     * Called upon the block being destroyed by an explosion
     */
	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
		if (!world.isRemote) {
			EntityAtomicBomb bomb = new EntityAtomicBomb(world, (double) ((float) x + 0.5F), (double) ((float) y + 0.5F),
					(double) ((float) z + 0.5F), explosion.getExplosivePlacedBy());
			bomb.fuse = world.rand.nextInt(bomb.fuse / 4) + bomb.fuse / 8;
			world.spawnEntityInWorld(bomb);
		}
	}
	
    @Override
	public void func_150114_a(World world, int x, int y, int z, int ignition, EntityLivingBase entity) {
		if (!world.isRemote) {
			if ((ignition & 1) == 1) {
				EntityAtomicBomb bomb = new EntityAtomicBomb(world, (double) ((float) x + 0.5F), (double) ((float) y + 0.5F),
						(double) ((float) z + 0.5F), entity);
				world.spawnEntityInWorld(bomb);
				world.playSoundAtEntity(bomb, "game.tnt.primed", 1.0F, 1.0F);
			}
		}
	}

}

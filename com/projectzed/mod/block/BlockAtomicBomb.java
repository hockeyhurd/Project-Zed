/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block;

import com.hockeyhurd.hcorelib.api.block.IHBlock;
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.entity.EntityAtomicBomb;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * Class containing block code for atomicBomb.
 * 
 * @author hockeyhurd
 * @version Mar 18, 2015
 */
public class BlockAtomicBomb extends BlockTNT implements IHBlock {

	public static final String name = "atomicBomb";
	public static final ResourceLocation resourceLocation = new ResourceLocation(ProjectZed.assetDir, name);
	private ItemBlock itemBlock;

	public BlockAtomicBomb() {
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setBlockUnbreakable();
	}

	/**
     * Called upon the block being destroyed by an explosion
     */
	@Override
	public void onBlockDestroyedByExplosion(World world, BlockPos blockPos, Explosion explosion) {
		if (!world.isRemote) {
			EntityAtomicBomb bomb = new EntityAtomicBomb(world, (double) ((float) blockPos.getX() + 0.5f), (double) ((float) blockPos.getY() + 0.5f),
					(double) ((float) blockPos.getZ() + 0.5f), explosion.getExplosivePlacedBy());
			final int fuse = bomb.getFuse();
			bomb.setFuse(world.rand.nextInt(fuse / 4) + fuse / 8);
			world.spawnEntityInWorld(bomb);
		}
	}
	
    @Override
	public void explode(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entity) {
		if (!world.isRemote) {
			if (blockState.getValue(EXPLODE)) {
				EntityAtomicBomb bomb = new EntityAtomicBomb(world, (double) ((float) blockPos.getX() + 0.5f), (double) ((float) blockPos.getY() + 0.5f),
						(double) ((float) blockPos.getZ() + 0.5f), entity);
				world.spawnEntityInWorld(bomb);
				// world.playSoundAtEntity(bomb, "game.tnt.primed", 1.0f, 1.0f);
				world.playSound(null, bomb.posX, bomb.posY, bomb.posZ, SoundEvents.entity_tnt_primed, SoundCategory.BLOCKS, 1.0f, 1.0f);
			}
		}
	}

	@Override
	public BlockAtomicBomb getBlock() {
		return this;
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return resourceLocation;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemBlock getItemBlock() {
		return itemBlock != null ? itemBlock : (itemBlock = new ItemBlock(this));
	}

	@Override
	public float getBlockHardness() {
		return blockHardness;
	}

	@Override
	public EnumHarvestLevel getHarvestLevel() {
		return EnumHarvestLevel.UNBREAKABLE;
	}
}

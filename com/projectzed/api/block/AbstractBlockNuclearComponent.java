/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.block;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlockContainer;
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.api.tileentity.container.AbstractTileEntityNuclearComponent;
import com.projectzed.mod.ProjectZed;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Framework for nuclear chamber parts.
 * 
 * @author hockeyhurd
 * @version Feb 23, 2015
 */
public abstract class AbstractBlockNuclearComponent extends AbstractHCoreBlockContainer {

	// protected static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	protected static final PropertyBool IS_MULTIBLOCK = PropertyBool.create("multiblock");

	protected static final Random random = new Random();
	
	/**
	 * @param material
	 * @param name
	 */
	public AbstractBlockNuclearComponent(Material material, String name) {
		super(material, ProjectZed.modCreativeTab, ProjectZed.assetDir, name);

		// setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(IS_MULTIBLOCK, false));
		setDefaultState(blockState.getBaseState().withProperty(IS_MULTIBLOCK, false));
	}

	@Override
	public Block getBlock() {
		return this;
	}

	@Override
	public float getBlockHardness() {
		return 1.0f;
	}

	@Override
	public EnumHarvestLevel getHarvestLevel() {
		return EnumHarvestLevel.PICKAXE_STONE;
	}

	/**
	 * @param name
	 */
	public AbstractBlockNuclearComponent(String name) {
		this(Material.IRON, name);
	}

	@Override
	public AbstractTileEntityNuclearComponent createNewTileEntity(World world, int id) {
		return getTileEntity();
	}

	/**
	 * Abstract getter function to get associated TE object.
	 * 
	 * @return TE object associated with block.
	 */
	public abstract AbstractTileEntityNuclearComponent getTileEntity();


	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase placer, ItemStack stack) {
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing sideHit, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public IBlockState getActualState(IBlockState blockState, IBlockAccess world, BlockPos blockPos) {
		final AbstractTileEntityNuclearComponent te = (AbstractTileEntityNuclearComponent) world.getTileEntity(blockPos);

		// if (te != null) return blockState.withProperty(IS_MULTIBLOCK, te.is)
		return blockState;
	}

	@Override
	public int getMetaFromState(IBlockState blockState) {
		return blockState.getValue(IS_MULTIBLOCK).booleanValue() ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(IS_MULTIBLOCK, meta > 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, IS_MULTIBLOCK);
	}
}

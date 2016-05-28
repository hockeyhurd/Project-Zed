/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.block;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlockContainer;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
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

/**
 * Class used to easily generator and normalize any generator.
 * 
 * @author hockeyhurd
 * @version Oct 28, 2014
 */
public abstract class AbstractBlockGenerator extends AbstractHCoreBlockContainer {

	protected static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	protected static final PropertyBool ACTIVE = PropertyBool.create("active");

	/**
	 * @param material material of block
	 * @param name String name.
	 */
	public AbstractBlockGenerator(Material material, String name) {
		this(material, name, true);
	}

	/**
	 * @param material material of block
	 * @param name String name.
	 * @param setDefaultState flag whether we can set the default state from this
	 *                        parent class.
	 */
	public AbstractBlockGenerator(Material material, String name, boolean setDefaultState) {
		super(material, ProjectZed.modCreativeTab, ProjectZed.assetDir, name);

		if (setDefaultState)
			setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
	}

	@Override
	public AbstractBlockGenerator getBlock() {
		return this;
	}

	@Override
	public float getBlockHardness() {
		return 2.0f;
	}

	@Override
	public EnumHarvestLevel getHarvestLevel() {
		return EnumHarvestLevel.PICKAXE_STONE;
	}

	/**
	 * Method used to grab exact tile entity associated with this block.
	 * <br>Example: return new TileEntityRFBridge().
	 */
	@Override
	public abstract AbstractTileEntityGenerator getTileEntity();

	@Override
	public AbstractTileEntityGenerator createNewTileEntity(World world, int id) {
		return getTileEntity();
	}

	@Override
	public EnumFacing[] getValidRotations(World world, BlockPos pos) {
		final AbstractTileEntityGenerator te = (AbstractTileEntityGenerator) world.getTileEntity(pos);
		if (te != null && te.canRotateTE())
			return EnumFacing.HORIZONTALS;

		return super.getValidRotations(world, pos);
	}

	@Override
	public IBlockState getActualState(IBlockState blockState, IBlockAccess world, BlockPos pos) {
		/*final AbstractTileEntityGenerator te = (AbstractTileEntityGenerator) world.getTileEntity(pos);

		if (te != null && te.canRotateTE()) {
			EnumFacing dir = te.getCurrentFacing();
			if (dir == null || dir == EnumFacing.DOWN || dir == EnumFacing.UP) dir = EnumFacing.NORTH;
			return blockState.withProperty(FACING, dir).withProperty(ACTIVE, te.canProducePower());
		}*/

		// return blockState.withProperty(FACING, EnumFacing.NORTH);
		return blockState;
	}

	@Override
	public int getMetaFromState(IBlockState blockState) {
		return blockState.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, ACTIVE);
	}

	/**
	 * Method used to update block's state
	 *
	 * @param active whether currently active or not.
	 * @param world world object.
	 * @param blockPos Block position.
	 */
	public void updateBlockState(boolean active, World world, BlockPos blockPos) {
		if (world.isRemote) return;

		final AbstractTileEntityGenerator tileEntity = (AbstractTileEntityGenerator) world.getTileEntity(blockPos);
		// keepInventory = true;

		if (tileEntity != null) {

			IBlockState blockState = BlockUtils.getBlock(world, blockPos);
			blockState = blockState.withProperty(FACING, tileEntity.getCurrentFacing()).withProperty(ACTIVE, active);
			BlockUtils.setBlock(world, blockPos, blockState);

			tileEntity.markDirty();
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState block, EntityLivingBase player, ItemStack stack) {
		// int dir = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		// if (dir == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		// if (dir == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		// if (dir == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		// if (dir == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);

		final AbstractTileEntityGenerator te = (AbstractTileEntityGenerator) world.getTileEntity(pos);
		if (te != null) {
			te.setFrontFacing(player.getHorizontalFacing().getOpposite());

			if (stack.hasTagCompound()) {
				te.readNBT(stack.getTagCompound());
				te.markDirty();
			}
		}

		if (stack.hasDisplayName()) te.setCustomName(stack.getDisplayName());
	}

	@Override
	public abstract boolean onBlockActivated(World world, BlockPos pos, IBlockState block, EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing side, float hitX, float hitY, float hitZ);

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState oldBlock) {
		doBreakBlock(world, pos);
		super.breakBlock(world, pos, oldBlock);
	}
	
	/**
	 * Method allows for insertion into braekBlock method while keeping this abstract.
	 */
	protected abstract void doBreakBlock(World world, BlockPos pos);

}

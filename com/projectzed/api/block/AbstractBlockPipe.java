/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.block;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlockContainer;
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.api.tileentity.container.AbstractTileEntityPipe;
import com.projectzed.mod.ProjectZed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Class containing necessary abstractions for a generic pipe.
 * 
 * @author hockeyhurd
 * @version Oct 25, 2014
 */
public abstract class AbstractBlockPipe extends AbstractHCoreBlockContainer {

	protected static final float PIXEL = 1f / 16f;
	protected static final float CALC = 11 * PIXEL / 2;
	protected static final AxisAlignedBB DEFAULT_BOUNDING_BOX = new AxisAlignedBB(11 * PIXEL / 2, 11 * PIXEL / 2,
			11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2);

	/**
	 * @param material material of pipe
	 * @param name name of block.
	 */
	public AbstractBlockPipe(Material material, String name) {
		super(material, ProjectZed.modCreativeTab, ProjectZed.assetDir, name);
	}

	@Override
	public boolean hasSpecialRenderer() {
		// return true;
		return false;
	}

	@Override
	public AbstractBlockPipe getBlock() {
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

	@Override
	public boolean isFullyOpaque(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState blockState) {
		return false;
	}

	/**
	 * @return tileentity object associated with this pipe.
	 */
	public abstract AbstractTileEntityPipe getTileEntity();

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		// this.setBlockBounds(11 * PIXEL / 2, 11 * PIXEL / 2, 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2, 1 - 11 * PIXEL / 2);

		return DEFAULT_BOUNDING_BOX;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
		return getBoundingBox(state, world, pos);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
		return getBoundingBox(state, world, pos);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase e, ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound() != null) {
			NBTTagCompound comp = stack.getTagCompound();

			AbstractTileEntityPipe te = (AbstractTileEntityPipe) world.getTileEntity(pos);
			te.readNBT(comp);
		}
	}

	protected boolean canConnect(World world, double x, double y, double z) {
		return world.getBlockState(new BlockPos((int) x, (int) y, (int) z)).getBlock() == this;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState oldBlock) {
		doBreakBlock(world, pos);
		super.breakBlock(world, pos, oldBlock);
	}
	
	/**
	 * Method allows for control of behavior of block when being destroyed.
	 * 
	 * @param world world object.
	 * @param pos BlockPos.
	 */
	protected abstract void doBreakBlock(World world, BlockPos pos);

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing sideHit, float hitX, float hitY, float hitZ) {
		return false;
	}

}

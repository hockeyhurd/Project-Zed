/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.api.block;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlockContainer;
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Class used to easily create and normalize any block container (energy, fluid, etc.).
 * 
 * @author hockeyhurd
 * @version Nov 29, 2014
 */
public abstract class AbstractBlockContainer extends AbstractHCoreBlockContainer {

	protected static final Random random = new Random();
	
	/**
	 * @param material material of block.
	 * @param assetDir asset directory to find icon img.
	 * @param name name of block.
	 */
	public AbstractBlockContainer(Material material, String assetDir, String name) {
		super(material, ProjectZed.modCreativeTab, assetDir, name);
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
	 * Method used to grab exact tile entity associated with this block.
	 * <br>Example: return new TileEntityRFBridge().
	 */
	public abstract AbstractTileEntityEnergyContainer getTileEntity();
	
	@Override
	public AbstractTileEntityEnergyContainer createNewTileEntity(World world, int id) {
		return getTileEntity();
	}
	
	@Override
	public abstract boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem,
			EnumFacing side, float hitX, float hitY, float hitZ);
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase e, ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound() != null) {
			NBTTagCompound comp = stack.getTagCompound();
			
			AbstractTileEntityEnergyContainer te = (AbstractTileEntityEnergyContainer) world.getTileEntity(pos);
			te.readNBT(comp);
		}
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

}

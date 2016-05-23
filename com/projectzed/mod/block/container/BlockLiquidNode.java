/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.hockeyhurd.hcorelib.api.util.Waila;
import com.projectzed.api.block.AbstractBlockFluidContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityFluidContainer;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.tileentity.container.TileEntityLiquidNode;
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

/**
 * Block code for liquid node.
 * 
 * @author hockeyhurd
 * @version Feb 16, 2015
 */
public class BlockLiquidNode extends AbstractBlockFluidContainer {
	
	// private IIcon input, output, neutral;
	
	/**
	 * @param material
	 */
	public BlockLiquidNode(Material material) {
		super(material, ProjectZed.assetDir, "liquidNode");
	}

	@Override
	public AbstractTileEntityFluidContainer getTileEntity() {
		return new TileEntityLiquidNode();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState block, EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase player, ItemStack stack) {
		/*int side = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (side == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		if (side == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		if (side == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		if (side == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);*/
		
		if (!(player instanceof EntityPlayer)) return;
		
		Waila waila = new Waila(stack, world, (EntityPlayer) player, null, 0);
		waila.finder(false);
		
		EnumFacing w = waila.getSideHit();
		
		// if (w == 4) w--;

		// world.setBlockMetadataWithNotify(x, y, z, w, 2);
		// world.setBlockMetadataWithNotify(x, y, z, dir.ordinal() + 1, 2);
		BlockUtils.setBlock(world, blockPos, blockState.getBlock().getStateFromMeta(w.ordinal()));

		if (stack.hasDisplayName()) ((AbstractTileEntityMachine) world.getTileEntity(blockPos)).setCustomName(stack.getDisplayName());
		
		if (stack.hasTagCompound() && stack.hasTagCompound()) {
			NBTTagCompound comp = stack.getTagCompound();

			AbstractTileEntityFluidContainer te = (AbstractTileEntityFluidContainer) world.getTileEntity(blockPos);
			te.readNBT(comp);
		}
	}
	
	@Override
	protected void doBreakBlock(World world, BlockPos blockPos) {
	}

}

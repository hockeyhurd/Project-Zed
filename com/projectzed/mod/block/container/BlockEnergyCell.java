/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.container;

import com.projectzed.api.block.AbstractBlockContainer;
import com.projectzed.api.tileentity.container.AbstractTileEntityEnergyContainer;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.container.TileEntityEnergyBankBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * Class containing block code for energy bank cell.
 * 
 * @author hockeyhurd
 * @version Dec 3, 2014
 */
public class BlockEnergyCell extends AbstractBlockContainer {

	private final int TIER;
	
	/**
	 * @param material
	 * @param name
	 */
	public BlockEnergyCell(Material material, String name) {
		super(material, ProjectZed.assetDir, name);
		this.TIER = Integer.parseInt("" + name.charAt(name.length() - 1));
	}

	@Override
	public boolean hasSpecialRenderer() {
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState blockState) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isOpaqueCube(IBlockState blockState) {
		return false;
	}

	@Override
	public AbstractTileEntityEnergyContainer getTileEntity() {
		TileEntityEnergyBankBase te = new TileEntityEnergyBankBase();
		
		if (this.TIER > 0) te.setTier(this.TIER);
		return te;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState block, EntityPlayer player, EnumHand hand, ItemStack stack,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityEnergyBankBase te = (TileEntityEnergyBankBase) world.getTileEntity(pos);
			if (te != null) {
				if (stack == null || !(stack.getItem() instanceof ItemWrench))
					FMLNetworkHandler
							.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityEnergyBankBase.class),
									world, pos.getX(), pos.getY(), pos.getZ());

				else return false;
			}

			return true;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState block, EntityLivingBase e, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, block, e, stack);
		if (stack.hasTagCompound() && stack.getTagCompound() != null) {
			NBTTagCompound comp = stack.getTagCompound();

			TileEntityEnergyBankBase te = (TileEntityEnergyBankBase) world.getTileEntity(pos);
			te.readNBT(comp);

			/*for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				te.setSideValve(dir, comp.getByte(dir.name()));
			}*/
		}
	}

	@Override
	protected void doBreakBlock(World world, BlockPos pos) {
		TileEntityEnergyBankBase te = (TileEntityEnergyBankBase) world.getTileEntity(pos);
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

}

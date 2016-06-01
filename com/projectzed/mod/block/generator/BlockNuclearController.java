/* This file is part of Project-Zed. Project-Zed is free software: you can redistribute it and/or modify it under the terms of the GNU General Public 
* License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Project-Zed is 
* distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
* PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along 
* with Project-Zed. If not, see <http://www.gnu.org/licenses/>
*/
package com.projectzed.mod.block.generator;

import com.hockeyhurd.hcorelib.api.math.VectorHelper;
import com.hockeyhurd.hcorelib.api.util.BlockUtils;
import com.hockeyhurd.hcorelib.api.util.ChatUtils;
import com.projectzed.api.block.AbstractBlockGenerator;
import com.projectzed.api.energy.source.EnumType;
import com.projectzed.api.tileentity.IMultiBlockableController;
import com.projectzed.api.tileentity.generator.AbstractTileEntityGenerator;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.generator.TileEntityNuclearController;
import com.projectzed.mod.tileentity.generator.TileEntitySolarArray;
import com.projectzed.mod.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * Class containing code for nuclear controller, main hub for multi-block structure and used as main source of 'power generation', particle effects,
 * etc.
 * 
 * @author hockeyhurd
 * @version Nov 24, 2014
 */
public class BlockNuclearController extends AbstractBlockGenerator {

	/** Variable tracking whether to use fusion or fission. */
	private final boolean FUSION_MODE; 
	
	/** Variable tracking the place direction so we know where core should be. */
	private byte placeDir;
	
	/** Variable tracking size of nuclear reactant chamber */
	private byte size;
	
	/** Variable containing relative location. */
	private byte rel;
	
	public static final float[] TIERED_MOD = new float[] {
		1.0f, 1.5f, 2.5f, 4.0f, 	
	};
	
	/**
	 * @param material
	 * @param fusion toggle whether is fusion controller or not.
	 */
	public BlockNuclearController(Material material, boolean fusion) {
		super(material, "nuclearController" + (fusion ? "Fusion" : "Fission"));
		this.FUSION_MODE = fusion;
	}

	@Override
	public AbstractTileEntityGenerator getTileEntity() {
		TileEntityNuclearController te = new TileEntityNuclearController();
		te.setPlaceDir(placeDir, size, rel);
		te.getSource().setModifier(getModifierFromSize());
		if (this.FUSION_MODE) te.setSource(EnumType.FUSION);
		return te;
	}
	
	private float getModifierFromSize() {
		if (this.size <= 0) return 1.0f;
		
		int index = (this.size - 1) / 2;
		return index > 0 ? TIERED_MOD[index - 1] : index <= 0 ? TIERED_MOD[index] : 1.0f;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityNuclearController te = (TileEntityNuclearController) world.getTileEntity(blockPos);
			if (te != null) {
				if (stack == null || !(stack.getItem() instanceof ItemWrench))
					FMLNetworkHandler
							.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityNuclearController.class),
									world, blockPos.getX(), blockPos.getY(), blockPos.getZ());

				else return false;
			}

			return true;
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState block, EntityLivingBase player, ItemStack stack) {
		TileEntityNuclearController cont = (TileEntityNuclearController) world.getTileEntity(blockPos);
		if (cont == null) return;
		
		cont.setHasMaster(true);
		cont.setIsMaster(true);
		cont.setMasterVec(VectorHelper.toVector3i(blockPos));
		
		int dir = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		
		this.placeDir = (byte) dir;
		this.size = getSizeFromDir(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), dir);
		if (size > 9 && player instanceof EntityPlayer)
			((EntityPlayer) player).addChatComponentMessage(ChatUtils.createComponent(false, "Block Placed incorrectly!"));
		// System.out.println("Placed Dir: " + this.placeDir);
		// System.out.println("Size: " + this.size + "x" + this.size);
		
		// if (dir == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		// if (dir == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		// if (dir == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		// if (dir == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		if (stack.hasDisplayName()) ((TileEntitySolarArray) world.getTileEntity(blockPos)).setCustomName(stack.getDisplayName());
	}
	
	/**
	 * Function used to determine size of nuclear chamber 
	 * 
	 * @param world world object as reference.
	 * @param x x-position of this block.
	 * @param y y-position of this block.
	 * @param z z-position of this block.
	 * @param dir direction to check from.
	 * @return total size of reactant chamber.
	 */
	private byte getSizeFromDir(World world, int x, int y, int z, int dir) {
		byte size = 0;

		/*
		 * 0 = +z
		 * 1 = -x
		 * 2 = -z
		 * 3 = +x
		 */
		
		if (dir == 0) {
			while (BlockUtils.getBlock(world, x, y, z + size).getBlock() != ProjectZed.nuclearReactantCore && size < 4) {
				size++;
			}
		}
		
		else if (dir == 2) {
			while (BlockUtils.getBlock(world, x, y, z + size).getBlock() != ProjectZed.nuclearReactantCore && size > -5) {
				size--;
			}
		}
		
		else if (dir == 1) {
			while (BlockUtils.getBlock(world, x + size, y, z).getBlock() != ProjectZed.nuclearReactantCore && size > -5) {
				size--;
			}
		}
		
		else if (dir == 3) {
			while (BlockUtils.getBlock(world, x + size, y, z).getBlock() != ProjectZed.nuclearReactantCore && size < 4) {
				size++;
			}
		}
		
		else size = Byte.MAX_VALUE;
		
		this.rel = size;
		size = (byte) Math.abs(size);
		if (size == 1) return (byte) (size * 3);
		else if (size != Byte.MAX_VALUE && size < 5) return (byte) (size * 3 - (size - 1));
		else return 0;
	}
	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState block, Block neighborBlock) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof IMultiBlockableController<?>) {
			IMultiBlockableController<AbstractTileEntityGenerator> mb = (IMultiBlockableController<AbstractTileEntityGenerator>) te;
			if (mb.hasMaster()) {
				if (mb.isMaster()) {
					if (!mb.checkMultiBlockForm()) mb.resetStructure();
				}
				
				else if (!mb.checkForMaster()) {
					mb.reset();
					// world.markBlockForUpdate(x, y, z);
					world.notifyBlockOfStateChange(pos, block.getBlock());
				}
			}
		}
		
		super.onNeighborBlockChange(world, pos, block, neighborBlock);
	}

	@Override
	protected void doBreakBlock(World world, BlockPos pos) {
		TileEntityNuclearController te = (TileEntityNuclearController) world.getTileEntity(pos);

		if (te.getMapVec() != null && te.getMapVec().size() > 0) te.resetStructure();
		
		WorldUtils.dropItemsFromContainerOnBreak(te);
		
		ProjectZed.logHelper.info("Stored:", te.getEnergyStored());
	}

	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	/*@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		// If active.
		// if (this.field_149932_b) {
		int meta = world.getBlockMetadata(x, y, z);
		float xx = (float) x + 0.5F;
		float yy = (float) y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
		float zz = (float) z + 0.5F;
		
		float yOffset = 2f;
		double vel = 0.25d;

		if (meta == 4) {
			world.spawnParticle("smoke", (double) xx, (double) (yy + yOffset), (double) zz, vel, 0.0D, -vel);
			world.spawnParticle("flame", (double) xx, (double) (yy + yOffset), (double) zz, vel, 0.0D, -vel);
		}
		else if (meta == 5) {
			world.spawnParticle("smoke", (double) xx, (double) (yy + yOffset), (double) zz, -vel, 0.0D, vel);
			world.spawnParticle("flame", (double) xx, (double) (yy + yOffset), (double) zz, -vel, 0.0D, vel);
		}
		else if (meta == 2) {
			world.spawnParticle("smoke", (double) xx, (double) (yy + yOffset), (double) zz, vel, 0.0D, vel);
			world.spawnParticle("flame", (double) xx, (double) (yy + yOffset), (double) zz, vel, 0.0D, vel);
		}
		else if (meta == 3) {
			world.spawnParticle("smoke", (double) xx, (double) (yy + yOffset), (double) zz, -vel, 0.0D, -vel);
			world.spawnParticle("flame", (double) xx, (double) (yy + yOffset), (double) zz, -vel, 0.0D, -vel);
		}
		// }
	} */

}

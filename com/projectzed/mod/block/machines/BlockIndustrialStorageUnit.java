package com.projectzed.mod.block.machines;

import com.projectzed.api.block.AbstractBlockMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.item.tools.ItemWrench;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialStorageUnit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

/**
 * @author hockeyhurd
 * @version 1/17/2017.
 */
public class BlockIndustrialStorageUnit extends AbstractBlockMachine {

	public BlockIndustrialStorageUnit(String name) {
		super(name);
	}

	@Override
	public TileEntityIndustrialStorageUnit getTileEntity() {
		return new TileEntityIndustrialStorageUnit();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand,
			ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityIndustrialStorageUnit te = (TileEntityIndustrialStorageUnit) world.getTileEntity(blockPos);
			if (te != null) {
				if (player.getActiveItemStack() == null || !(player.getActiveItemStack().getItem() instanceof ItemWrench))
					FMLNetworkHandler
							.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityIndustrialStorageUnit.class),
									world, blockPos.getX(), blockPos.getY(), blockPos.getZ());

				else return false;
			}

			return true;
		}
	}

}

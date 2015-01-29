package com.projectzed.mod.block.machines;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.projectzed.api.block.AbstractBlockMachine;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialFurnace;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;

/**
 * Class containing the power controlled furnace.
 * 
 * @author hockeyhurd
 * @version Oct 22, 2014
 */
public class BlockIndustrialFurnace extends AbstractBlockMachine {

	public BlockIndustrialFurnace() {
		super("industrialFurnace");
		this.name = "industrialFurnace";
		this.setBlockName(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockMachine#getTileEntity()
	 */
	@Override
	public AbstractTileEntityMachine getTileEntity() {
		return new TileEntityIndustrialFurnace();
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockMachine#getBlockInstance()
	 */
	@Override
	protected Block getBlockInstance() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.projectzed.api.block.AbstractBlockMachine#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float, float, float)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			AbstractTileEntityMachine te = (AbstractTileEntityMachine) world.getTileEntity(x, y, z);
			// if (te != null) FMLNetworkHandler.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntitySolarArray.class),
			// world, x, y, z);
			if (te != null) FMLNetworkHandler.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityIndustrialFurnace.class), world, x, y, z);
			return true;
		}
	}

}

package com.projectzed.mod.block.machines;

import net.minecraft.block.Block;

import com.projectzed.api.block.AbstractBlockMachine;
import com.projectzed.api.tileentity.machine.AbstractTileEntityMachine;
import com.projectzed.mod.tileentity.machine.TileEntityIndustrialFurnace;

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

	protected AbstractTileEntityMachine getTileEntity() {
		return new TileEntityIndustrialFurnace();
	}

	protected Block getBlockInstance() {
		return this;
	}

}

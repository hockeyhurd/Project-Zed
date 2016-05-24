package com.projectzed.mod.block.ore;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlock;
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.mod.ProjectZed;
import net.minecraft.block.material.Material;

/**
 * @author hockeyhurd
 * @version 3/3/2015.
 */
public class BlockCopper extends AbstractHCoreBlock {

	public BlockCopper(Material mat, String name) {
		super(mat, ProjectZed.modCreativeTab, ProjectZed.assetDir, name);
	}

	@Override
	public float getBlockHardness() {
		return 5.0f;
	}

	@Override
	public EnumHarvestLevel getHarvestLevel() {
		return EnumHarvestLevel.PICKAXE_STONE;
	}
}

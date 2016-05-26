package com.projectzed.mod.block.ore;

import com.hockeyhurd.hcorelib.api.block.AbstractHCoreBlock;
import com.hockeyhurd.hcorelib.api.util.enums.EnumHarvestLevel;
import com.projectzed.mod.ProjectZed;
import net.minecraft.block.material.Material;

/**
 * @author hockeyhurd
 * @version 3/3/2015.
 */
public class BlockAluminium extends AbstractHCoreBlock {

	public BlockAluminium(Material mat, String name) {
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

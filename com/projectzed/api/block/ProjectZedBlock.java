package com.projectzed.api.block;

import com.projectzed.mod.ProjectZed;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

/**
 * @author hockeyhurd
 * @version 3/3/2015.
 */
public class ProjectZedBlock extends Block {

	public final String NAME;

	public ProjectZedBlock(Material mat, String name) {
		super(mat);
		this.NAME = name;
		this.setBlockName(name);
		this.setHardness(2.0f);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + NAME);
	}

}

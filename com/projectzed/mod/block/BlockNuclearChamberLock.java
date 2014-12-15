package com.projectzed.mod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing block code for nuclearChamberLock.
 * 
 * @author hockeyhurd
 * @version Dec 14, 2014
 */
public class BlockNuclearChamberLock extends Block {

	public BlockNuclearChamberLock() {
		super(Material.rock);
		this.setBlockName("nuclearChamberLock");
		this.setHardness(1.0f);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

	@SideOnly(Side.CLIENT) 
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberLock");
	}
	
}

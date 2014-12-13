package com.projectzed.mod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for nuclearChamberWall.
 * 
 * @author hockeyhurd
 * @version Dec 12, 2014
 */
public class BlockNuclearChamberWall extends Block {

	public BlockNuclearChamberWall() {
		super(Material.rock);
		this.setBlockName("nuclearChamberWall");
		this.setHardness(1.0f);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

	// TODO: Make this connected textures/rotatable on placed.
	
	@SideOnly(Side.CLIENT) 
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "nuclearChamberWall");
	}
	
}

package com.projectzed.mod.block;

import java.util.Random;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing block code for thickenedGlass.
 * 
 * @author hockeyhurd
 * @version Nov 20, 2014
 */
public class BlockThickenedGlass extends BlockGlass {

	public BlockThickenedGlass() {
		super(Material.glass, false);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setBlockName("thickenedGlass");
		this.setHardness(0.75f);
		this.setResistance(2000.0f);
		this.setStepSound(soundTypeGlass);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "thickenedGlass");
	}
	
	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

}

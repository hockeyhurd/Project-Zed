package com.projectzed.mod.block.generator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing solar block array.
 * 
 * @author hockeyhurd
 * @version Oct 20, 2014
 */
public class BlockSolarArray extends Block {

	private IIcon top, base;
	
	/**
	 * @param material = material of block
	 */
	public BlockSolarArray(Material material) {
		super(material);
		this.setBlockName("solarArray");
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(1.0f);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "solar_side");
		this.top = reg.registerIcon(ProjectZed.assetDir + "solar_top");
		this.base = reg.registerIcon(ProjectZed.assetDir + "solar_base");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return side == 1 ? this.top : (side  == 0 ? this.base : this.blockIcon);
	}

}

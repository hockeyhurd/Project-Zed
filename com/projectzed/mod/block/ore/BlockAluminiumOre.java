package com.projectzed.mod.block.ore;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.hockeyhurd.api.block.AbstractBlockOre;
import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author hockeyhurd
 * @version Nov 6, 2014
 */
public class BlockAluminiumOre extends AbstractBlockOre {

	/**
	 * @param material
	 * @param assetDir
	 * @param name
	 */
	public BlockAluminiumOre(Material material, String assetDir, String name) {
		super(material, assetDir, name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.fileName = name;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + this.fileName);
	}

}

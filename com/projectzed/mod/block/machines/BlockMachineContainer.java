package com.projectzed.mod.block.machines;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for machine block container.
 * 
 * @author hockeyhurd
 * @version Nov 14, 2014
 */
public class BlockMachineContainer extends Block {

	private String name;
	
	public BlockMachineContainer() {
		super(Material.rock);
		this.name = "machineContainer";
		this.setBlockName(this.name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
		this.setHardness(0.5f);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "generic_base");
	}

}

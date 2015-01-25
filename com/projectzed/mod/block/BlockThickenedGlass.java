package com.projectzed.mod.block;

import java.util.Random;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.proxy.ClientProxy;

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
		this.setLightOpacity(0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockBreakable#registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + "thickenedGlass");
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockGlass#renderAsNormalBlock()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockBreakable#isOpaqueCube()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#getRenderType()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return ClientProxy.thickenedGlass;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.Block#canRenderInPass(int)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderInPass(int pass) {
		ClientProxy.renderPass = pass;
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockGlass#getRenderBlockPass()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.block.BlockGlass#quantityDropped(java.util.Random)
	 */
	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

}

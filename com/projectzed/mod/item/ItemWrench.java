package com.projectzed.mod.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.hockeyhurd.api.math.Vector4Helper;
import com.hockeyhurd.api.util.BlockHelper;
import com.projectzed.api.tileentity.IWrenchable;
import com.projectzed.mod.ProjectZed;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for the main wrenching tool.
 * 
 * @author hockeyhurd
 * @version Feb 3, 2015
 */
public class ItemWrench extends Item {

	private final String NAME;
	private BlockHelper bh;
	
	/**
	 * @param name name of wrench.
	 */
	public ItemWrench(String name) {
		this.NAME = name;
		this.setUnlocalizedName(name);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#registerIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		itemIcon = reg.registerIcon(ProjectZed.assetDir + this.NAME);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.minecraft.item.Item#onItemUse(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int, int, float, float, float)
	 */
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ) {
		boolean used = false;
		
		if (!world.isRemote) {
			if (bh == null) bh = new BlockHelper(world, player);
			
			Vector4Helper<Integer> vecClick = new Vector4Helper<Integer>((int) clickX, (int) clickY, (int) clickZ);
			Block b = bh.getBlock(vecClick.x, vecClick.y, vecClick.z); 
			TileEntity te = world.getTileEntity(vecClick.x, vecClick.y, vecClick.z);
			
			if (b != null && b != Blocks.air && te != null && te instanceof IWrenchable) {
				IWrenchable wrench = (IWrenchable) te;
			}
			
		}
		
		return used;
	}

}

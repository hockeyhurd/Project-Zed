package com.projectzed.mod.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.projectzed.mod.ProjectZed;
import com.projectzed.mod.registry.TileEntityRegistry;
import com.projectzed.mod.tileentity.TileEntityFabricationTable;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Class containing code for fabrication table and its properties.
 * 
 * @author hockeyhurd
 * @version Nov 22, 2014
 */
public class BlockFabricationTable extends BlockContainer {

	private String name;
	private IIcon base, top;

	public BlockFabricationTable(Material material) {
		super(material);
		this.name = "fabricationTable";
		this.setBlockName(this.name);
		this.setHardness(1.0f);
		this.setCreativeTab(ProjectZed.modCreativeTab);
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = reg.registerIcon(ProjectZed.assetDir + this.name + "_side");
		this.top = reg.registerIcon(ProjectZed.assetDir + this.name + "_top");
		this.base = reg.registerIcon(ProjectZed.assetDir + "generic_base");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? this.top : (side == 0 ? this.base : this.blockIcon);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.block.Block#onBlockActivated(net.minecraft.world.World, int, int, int, net.minecraft.entity.player.EntityPlayer, int, float,
	 * float, float)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;

		else {
			TileEntityFabricationTable te = (TileEntityFabricationTable) world.getTileEntity(x, y, z);
			if (te != null) FMLNetworkHandler.openGui(player, ProjectZed.instance, TileEntityRegistry.instance().getID(TileEntityFabricationTable.class), world, x, y, z);
			return true;
		}
	}

	public TileEntity createNewTileEntity(World world, int id) {
		return new TileEntityFabricationTable();
	}

}
